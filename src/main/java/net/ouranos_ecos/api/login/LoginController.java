
package net.ouranos_ecos.api.login;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.ouranos_ecos.domain.service.LoginService;

/**
 * ユーザ当人認証API
 */
@RestController
@RequestMapping("auth/login")
public class LoginController {

  @Inject
  private LoginService loginService;

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<byte[]> post(HttpServletRequest request) {
    return loginService.postRequest(request);
  }
}