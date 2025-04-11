package net.ouranos_ecos.domain.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ログイン情報
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginOutputModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 内部事業者識別子
  private String operatorId;

}