package net.ouranos_ecos.domain.service;

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
import net.ouranos_ecos.domain.model.ResponseProductLcaCfpModel;
import net.ouranos_ecos.domain.model.ResponseProductModel;
import net.ouranos_ecos.domain.repository.jdbc.LcaResponseCfpJdbc;
import net.ouranos_ecos.domain.repository.jdbc.RequestJdbc;
import net.ouranos_ecos.domain.repository.jdbc.ResponseJdbc;

/**
 * LcaResponseCfpServiceクラス。
 */
@Service
@Slf4j
public class LcaResponseCfpService {

  @Inject
  LcaResponseCfpJdbc lcaResponseCfpJdbc;
  
  @Inject
  ResponseJdbc responseJdbc;
  
  @Inject
  RequestJdbc requestJdbc;

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

      // 回答識別子を取得
      String responseId = request.getParameter("responseId");
      if (responseId == null || "".equals(responseId)) {
        // 製品トレース識別子が取得できない場合
        return ErrorUtil.badRequestResponse(mapper, operatorId, request.getMethod());
      }

      // パラメータでLCA回答CFP情報取得
      ResponseProductLcaCfpModel lcaResponseCfpModel = lcaResponseCfpJdbc.select(operatorId, producttraceId,
          responseId);

      // レスポンスを返却
      byte[] json = mapper.writeValueAsBytes(lcaResponseCfpModel);
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

    // modelクラスの初期化
    ResponseProductModel responseProductModel = new ResponseProductModel();

    try {
      // リクエスト内容の取得
      String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
      // リクエスト内容を対応するクラスに格納
      responseProductModel = mapper.readValue(requestBody, ResponseProductModel.class);

      // 回答情報の依頼識別子を取得
      String requestId= responseJdbc.selectRequestId(responseProductModel.getOperatorId(), responseProductModel.getResponseId());
      
      // 依頼情報の受入済フラグを更新
      requestJdbc.updateAcceptedFlag(responseProductModel.getOperatorId(), requestId);

      // JSON変換
      byte[] json = mapper.writeValueAsBytes(Constants.SUCSESS_MESSAGE);
      responseEntity = ResponseEntity.status(HttpStatus.CREATED).headers(new HttpHeaders()).body(json);

    } catch (Exception e) {
      log.warn("予期せぬエラー", e);
      e.printStackTrace();
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, request.getParameter("operatorId"),
          request.getMethod());
    }

    return responseEntity;
  }
}