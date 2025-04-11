package net.ouranos_ecos.domain.service;

import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.ouranos_ecos.domain.common.ErrorUtil;
import net.ouranos_ecos.domain.model.LoginInputModel;
import net.ouranos_ecos.domain.model.LoginOutputModel;
import net.ouranos_ecos.domain.repository.jdbc.LoginJdbc;

/**
 * LoginServiceクラス。
 */
@Service
@Slf4j
public class LoginService {

  @Inject
  LoginJdbc loginJdbc;

  @Inject
  ObjectMapper mapper;

  @Transactional
  public ResponseEntity<byte[]> postRequest(HttpServletRequest request) {

    LoginInputModel loginInputModel = new LoginInputModel();

    ResponseEntity<byte[]> responseEntity = null;

    // リクエスト内容を取得
    try {
      String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
      loginInputModel = mapper.readValue(requestBody, LoginInputModel.class);

      if (loginInputModel.getOperatorAccountId() == null || "".equals(loginInputModel.getOperatorAccountId())
          || loginInputModel.getAccountPassword() == null || "".equals(loginInputModel.getAccountPassword())) {
        // APIのエラーレスポンスの生成
        responseEntity = ErrorUtil.badRequestResponse(mapper, "", request.getMethod());
      }
      // DB接続
      String operatorid = loginJdbc.selectByIdandPassword(loginInputModel.getOperatorAccountId(),
          loginInputModel.getAccountPassword());

      if (operatorid != null && !"".equals(operatorid)) {
        // ログ出力に追加
        MDC.put("operatorId", operatorid);
        // ログイン成功
        LoginOutputModel model = new LoginOutputModel();
        model.setOperatorId(operatorid);
        byte[] json = mapper.writeValueAsBytes(model);
        responseEntity = ResponseEntity.status(HttpStatus.CREATED).headers(new HttpHeaders()).body(json);
      } else {
        // APIのエラーレスポンスの生成
        responseEntity = ErrorUtil.badRequestResponse(mapper, operatorid, request.getMethod());
      }
    } catch (Exception e) {
      log.warn("予期せぬエラー", e);
      // なんらかのエラーが発生した場合
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, "", request.getMethod());
    }

    return responseEntity;
  }
}