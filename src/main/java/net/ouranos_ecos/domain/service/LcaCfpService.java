package net.ouranos_ecos.domain.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.ouranos_ecos.domain.common.Constants;
import net.ouranos_ecos.domain.common.ErrorUtil;
import net.ouranos_ecos.domain.model.LcaCfpModel;
import net.ouranos_ecos.domain.model.LcaPartsStructureModel;
import net.ouranos_ecos.domain.model.ProductLcaCfpModel;
import net.ouranos_ecos.domain.model.ProductModel;
import net.ouranos_ecos.domain.repository.jdbc.LcaCfpJdbc;
import net.ouranos_ecos.domain.repository.jdbc.LcaPartsStructureJdbc;
import net.ouranos_ecos.domain.repository.jdbc.ProductJdbc;

/**
 * LcaCfpServiceクラス。
 */
@Service
@Slf4j
public class LcaCfpService {

  @Inject
  LcaCfpJdbc lcaCfpJdbc;

  @Inject
  LcaPartsStructureJdbc lcaPartsStructureJdbc;

  @Inject
  ProductJdbc productJdbc;

  @Inject
  ObjectMapper mapper;

  @Transactional
  public ResponseEntity<byte[]> getRequest(HttpServletRequest request) {

    ResponseEntity<byte[]> responseEntity = null;

    try {

      // 内部事業者識別子を取得
      String operatorId = request.getParameter("operatorId");
      if (operatorId == null || "".equals(operatorId)) {
        // 内部事業者識別子が取得できない場合
        return ErrorUtil.badRequestResponse(mapper, "", request.getMethod());
      }

      // 製品トレース識別子を取得
      String producttraceId = request.getParameter("productTraceId");
      if (producttraceId == null || "".equals(producttraceId)) {
        // 製品トレース識別子が取得できない場合
        return ErrorUtil.badRequestResponse(mapper, operatorId, request.getMethod());
      }

      // パラメータで部品情報Tを取得する。
      ProductLcaCfpModel lcaCfpModel = lcaCfpJdbc.select(operatorId, producttraceId);

      // レスポンスを返却
      byte[] json = mapper.writeValueAsBytes(lcaCfpModel);
      responseEntity = ResponseEntity.status(HttpStatus.OK).headers(new HttpHeaders()).body(json);

    } catch (Exception e) {
      log.warn("予期せぬエラー", e);
      e.printStackTrace();
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, request.getParameter("operatorId"),
          request.getMethod());
    }

    return responseEntity;
  }

  @Transactional
  public ResponseEntity<byte[]> putRequest(HttpServletRequest request) {

    ResponseEntity<byte[]> responseEntity = null;

    try {
      String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

      // リクエスト内容を対応するクラスに格納
      List<LcaCfpModel> lcaCfpList = mapper.readValue(requestBody, new TypeReference<List<LcaCfpModel>>() {
      });

      String operatorId = "";
      String productTraceId = "";
      BigDecimal materialsTotal = BigDecimal.ZERO;
      BigDecimal gco2eqTotal = BigDecimal.ZERO;

      // 更新用LcaCfpModelList
      List<LcaCfpModel> updateLlcaCfpModelList = new ArrayList<LcaCfpModel>();

      int result = 0;
      for (LcaCfpModel lcaCfpModel : lcaCfpList) {
        updateLlcaCfpModelList.add(lcaCfpModel);
        // LCA部品構成情報を取得
        LcaPartsStructureModel partsStructureModel = lcaPartsStructureJdbc.select(lcaCfpModel.getOperatorId(),
            lcaCfpModel.getTraceId());

        operatorId = partsStructureModel.getOperatorId();
        productTraceId = partsStructureModel.getProductTraceId();

        // 部品構成情報.LCA材料コードの値が存在する場合
        if (partsStructureModel.getLcaMaterialCd() != null && !"".equals(partsStructureModel.getLcaMaterialCd())) {
          // 部品構成情報.合計質量を合計する
          materialsTotal = materialsTotal.add((BigDecimal) partsStructureModel.getTotalMass());

          // LCACFP情報.素材報告値、加工報告値、資材報告値、廃棄物報告値、輸送材料報告値、輸送部品報告値を合計するの合計
          if (lcaCfpModel.getMReport() != null) {
            gco2eqTotal = gco2eqTotal.add(new BigDecimal(lcaCfpModel.getMReport()));
          }
        }
        if (lcaCfpModel.getPReport() != null) {
          gco2eqTotal = gco2eqTotal.add(new BigDecimal(lcaCfpModel.getPReport()));
        }
        if (lcaCfpModel.getRReport() != null) {
          gco2eqTotal = gco2eqTotal.add(new BigDecimal(lcaCfpModel.getRReport()));
        }
        if (lcaCfpModel.getWReport() != null) {
          gco2eqTotal = gco2eqTotal.add(new BigDecimal(lcaCfpModel.getWReport()));
        }
        if (lcaCfpModel.getTMaterialReport() != null) {
          gco2eqTotal = gco2eqTotal.add(new BigDecimal(lcaCfpModel.getTMaterialReport()));
        }
        if (lcaCfpModel.getTPartReport() != null) {
          gco2eqTotal = gco2eqTotal.add(new BigDecimal(lcaCfpModel.getTPartReport()));
        }
      }

      // 更新用LcaCfpModelListがNULLではない場合、更新処理を行う
      if (!updateLlcaCfpModelList.isEmpty()) {
        lcaCfpJdbc.updateLcaCfp(updateLlcaCfpModelList);
      }

      // 製品情報を更新する
      ProductModel productModel = new ProductModel();
      productModel.setOperatorId(operatorId);
      productModel.setProductTraceId(productTraceId);
      productModel.setMaterialsTotal(materialsTotal.toString());
      productModel.setGco2eqTotal(gco2eqTotal.toString());
      result = productJdbc.updateForCfpInfo(productModel);

      // JSON変換
      byte[] json = null;

      if (result == 0) {
        responseEntity = ErrorUtil.badRequestResponse(mapper, "", request.getMethod());
      } else {
        json = mapper.writeValueAsBytes(Constants.SUCSESS_MESSAGE);
        responseEntity = ResponseEntity.status(HttpStatus.CREATED).headers(new HttpHeaders()).body(json);
      }
    } catch (IOException e) {
      log.warn("予期せぬエラー", e);
      e.printStackTrace();
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, "", request.getMethod());
    } catch (Exception e) {
      e.printStackTrace();
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, "", request.getMethod());
    }

    return responseEntity;
  }
}