package net.ouranos_ecos.domain.service;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.ouranos_ecos.domain.common.ErrorUtil;
import net.ouranos_ecos.domain.model.CfpRequestResponse;
import net.ouranos_ecos.domain.model.CfpResponseProductModel;
import net.ouranos_ecos.domain.model.CfpResponseTransModel;
import net.ouranos_ecos.domain.repository.jdbc.RequestJdbc;
import net.ouranos_ecos.domain.repository.jdbc.ResponseJdbc;

/**
 * CfpRequestResponseServiceクラス。
 */
@Service
@Slf4j
public class CfpRequestResponseService {

  @Inject
  RequestJdbc requestJdbc;
  @Inject
  ResponseJdbc responseJdbc;

  @Inject
  ObjectMapper mapper;

  @Transactional
  public ResponseEntity<byte[]> getRequest(HttpServletRequest request) {

    ResponseEntity<byte[]> responseEntity = null;

    try {

      // 内部事業者識別子を取得
      String operatorId = request.getParameter("operatorId");
      if (!StringUtils.hasText(operatorId)) {
        // 内部事業者識別子が取得できない場合
        return ErrorUtil.badRequestResponse(mapper, "", request.getMethod());
      }

      // 依頼識別子を取得
      String requestId = request.getParameter("requestId");
      if (!StringUtils.hasText(requestId)) {
        // 依頼識別子が取得できない場合
        return ErrorUtil.badRequestResponse(mapper, operatorId, request.getMethod());
      }

      // 返却用モデル
      CfpRequestResponse cfpRequestResponse = new CfpRequestResponse();

      // パラメータで依頼情報を取得する。
      CfpResponseTransModel cfpResponseTransModel = requestJdbc.select(operatorId, requestId);

      cfpRequestResponse.setCfpResponseTransModel(cfpResponseTransModel);

      // パラメータで回答情報を取得する。
      List<CfpResponseProductModel> modelList = responseJdbc.select(operatorId, requestId);

      cfpRequestResponse.setCfpResponseProductModel(modelList);

      // レスポンスを返却
      byte[] json = mapper.writeValueAsBytes(cfpRequestResponse);
      responseEntity = ResponseEntity.status(HttpStatus.OK).headers(new HttpHeaders()).body(json);

    } catch (Exception e) {
      log.warn("予期せぬエラー", e);
      e.printStackTrace();
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, request.getParameter("operatorId"),
          request.getMethod());
    }

    return responseEntity;
  }

}