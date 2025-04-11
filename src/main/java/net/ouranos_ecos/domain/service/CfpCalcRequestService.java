package net.ouranos_ecos.domain.service;

import java.util.Map;
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
import net.ouranos_ecos.domain.model.CfpCalcRequestModel;
import net.ouranos_ecos.domain.model.CfpCalcRequestRegistModel;
import net.ouranos_ecos.domain.repository.jdbc.CfpCalcRequestJdbc;
import net.ouranos_ecos.domain.repository.jdbc.OperatorJdbc;
import net.ouranos_ecos.domain.repository.jdbc.RequestJdbc;

/**
 * CfpCalcRequestServiceクラス。
 */
@Service
@Slf4j
public class CfpCalcRequestService {

  @Inject
  RequestJdbc requestJdbc;
  @Inject
  OperatorJdbc operatorJdbc;
  @Inject
  CfpCalcRequestJdbc cfpCalcRequestJdbc;
  
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

      CfpCalcRequestModel cfpCalcRequest = cfpCalcRequestJdbc.select(operatorId, producttraceId);

      // レスポンスを返却
      byte[] json = mapper.writeValueAsBytes(cfpCalcRequest);
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
    CfpCalcRequestRegistModel cfpCalcRequestRegistModel = new CfpCalcRequestRegistModel();

    try {
      // リクエスト内容の取得
      String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
      // リクエスト内容を対応するクラスに格納
      cfpCalcRequestRegistModel = mapper.readValue(requestBody, CfpCalcRequestRegistModel.class);

      Map<String, Object> map = operatorJdbc.get(cfpCalcRequestRegistModel.getRequestedToOperatorId());
      cfpCalcRequestRegistModel.setRequestedToOperatorId(map.get("operatorid").toString());
      int result = requestJdbc.insertRequest(cfpCalcRequestRegistModel);

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