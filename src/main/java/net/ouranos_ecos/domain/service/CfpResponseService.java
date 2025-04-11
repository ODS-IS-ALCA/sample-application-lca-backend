package net.ouranos_ecos.domain.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.ouranos_ecos.domain.common.Constants;
import net.ouranos_ecos.domain.common.ErrorUtil;
import net.ouranos_ecos.domain.model.CfpRequestModel;
import net.ouranos_ecos.domain.model.CfpResponseModel;
import net.ouranos_ecos.domain.model.ProductLcaCfpModel;
import net.ouranos_ecos.domain.repository.jdbc.LcaCfpJdbc;
import net.ouranos_ecos.domain.repository.jdbc.LcaResponseCfpJdbc;
import net.ouranos_ecos.domain.repository.jdbc.LcaResponsePartsStructureJdbc;
import net.ouranos_ecos.domain.repository.jdbc.ResponseJdbc;
import net.ouranos_ecos.domain.repository.jdbc.ResponseProductJdbc;

/**
 * CfpResponseServiceクラス。
 */
@Service
@Slf4j
public class CfpResponseService {

  @Inject
  ResponseJdbc responseJdbc;
  @Inject
  ResponseProductJdbc responseProductJdbc;
  @Inject
  LcaResponsePartsStructureJdbc lcaResponsePartsStructureJdbc;
  @Inject
  LcaResponseCfpJdbc lcaResponseCfpJdbc;
  @Inject
  LcaCfpJdbc lcaCfpJdbc;

  @Inject
  ObjectMapper mapper;

  @Transactional
  public ResponseEntity<byte[]> getRequest(HttpServletRequest request) {

    String operatorid = request.getParameter("operatorId");

    ResponseEntity<byte[]> responseEntity = null;

    try {
      if (operatorid == null || "".equals(operatorid)) {
        // Queryパラメータが取得できない場合
        return ErrorUtil.badRequestResponse(mapper, "", request.getMethod());
      }
      // リミットを取得（表示する最大件数）
      int limit = Integer.parseInt(request.getParameter("limit"));
      // afterを取得（前回リストの最後の更新日）
      String after = request.getParameter("after");
      
      // DB接続
      List<CfpRequestModel> cfpRequestList = responseJdbc.selectCfpRequestList(operatorid, limit, after);

      HttpHeaders headers = new HttpHeaders();
      if (cfpRequestList.size() == limit) {
        // 100件取得した場合、最後のレコードの更新日を使って次があるか確認する。
        CfpRequestModel lastModel = cfpRequestList.get(cfpRequestList.size() - 1);
        List<CfpRequestModel> list = responseJdbc.selectCfpRequestList(operatorid, limit, lastModel.getModifiedAt());
        if (!list.isEmpty()) {
          // 次がある場合、更新日をheaderにセットする。(>ボタンが表示される)
          headers.add("Link", "after=" + lastModel.getModifiedAt());
        }
      }
      
      // JSON変換
      byte[] json = mapper.writeValueAsBytes(cfpRequestList);

      responseEntity = ResponseEntity.status(HttpStatus.OK).headers(headers).body(json);

    } catch (Exception e) {
      log.warn("予期せぬエラー", e);
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, operatorid, request.getMethod());
    }

    return responseEntity;
  }

  @Transactional
  public ResponseEntity<byte[]> putRequest(HttpServletRequest request) {

    ResponseEntity<byte[]> responseEntity = null;

    // modelクラスの初期化
    CfpResponseModel cfpResponseModel = new CfpResponseModel();

    int result = 0;
    try {
      // リクエスト内容の取得
      String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
      // リクエスト内容を対応するクラスに格納
      cfpResponseModel = mapper.readValue(requestBody, CfpResponseModel.class);

      // 回答識別子
      String responseid = UUID.randomUUID().toString();

      // 回答情報（response）を登録
      result = responseJdbc.insertCfpResponse(cfpResponseModel, responseid);

      // パラメータで製品情報、LCA部品構成情報、LCACFP情報を取得する。
      ProductLcaCfpModel lcaCfpModel = lcaCfpJdbc.select(cfpResponseModel.getOperatorId(),
          cfpResponseModel.getProductTraceId());

      // 回答製品情報（responseproduct）登録
      result = responseProductJdbc.insertCfpResponse(cfpResponseModel, responseid, lcaCfpModel.getProductModel());

      // LCA回答部品構成情報（lcaresponsepartsstructure）登録
      lcaResponsePartsStructureJdbc.insertCfpResponse(cfpResponseModel, responseid, lcaCfpModel.getLcaModel());

      // LCA回答CFP情報（lcaresponsecfp）登録
      lcaResponseCfpJdbc.insertCfpResponse(cfpResponseModel, responseid, lcaCfpModel.getLcaModel());

      if (result == 0) {
        responseEntity = ErrorUtil.badRequestResponse(mapper, "", request.getMethod());
      } else {
        // JSON変換
        byte[] json = mapper.writeValueAsBytes(Constants.SUCSESS_MESSAGE);
        responseEntity = ResponseEntity.status(HttpStatus.CREATED).headers(new HttpHeaders()).body(json);
      }
    } catch (Exception e) {
      log.warn("予期せぬエラー", e);
      e.printStackTrace();
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, request.getParameter("operatorId"),
          request.getMethod());
    }

    return responseEntity;
  }

}