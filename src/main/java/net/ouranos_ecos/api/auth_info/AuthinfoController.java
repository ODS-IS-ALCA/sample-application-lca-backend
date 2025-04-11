
package net.ouranos_ecos.api.auth_info;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.ouranos_ecos.domain.service.OperatorService;
import net.ouranos_ecos.domain.service.SubUserUnitClausesAgreeService;
import net.ouranos_ecos.domain.service.UserUnitLicenseService;

/**
 * authinfo ユーザ認証システム
 */
@RestController
@RequestMapping("api/v1/authInfo")
public class AuthinfoController {

  @Inject
  private OperatorService operatorService;

  @Inject
  private UserUnitLicenseService userUnitLicenseService;

  @Inject
  private SubUserUnitClausesAgreeService subUserUnitClausesAgreeService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<byte[]> get(HttpServletRequest request) {

    String dataTarget = request.getParameter("dataTarget");
    // dataTargeによる処理分岐
    switch (dataTarget) {

      case "operatorList" :
        // 事業者情報一覧取得API
        return operatorService.getRequest(request);

      case "userUnitLicense" :
        // 原単位DB認証API
        return userUnitLicenseService.getRequest(request);

    }

    return null;
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<byte[]> post(HttpServletRequest request) {

    String dataTarget = request.getParameter("dataTarget");

    // dataTargeによる処理分岐
    switch (dataTarget) {

      case "subUserUnitClausesAgree" :
        // 原単位DB準使用者約款同意API
        return subUserUnitClausesAgreeService.postRequest(request);

    }

    return null;
  }
}
