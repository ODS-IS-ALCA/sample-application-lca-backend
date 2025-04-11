package net.ouranos_ecos.domain.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ログイン返却情報
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginInputModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 事業者アカウント
  private String operatorAccountId;

  // パスワード
  private String accountPassword;
}