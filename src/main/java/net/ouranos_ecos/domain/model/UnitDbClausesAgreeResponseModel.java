package net.ouranos_ecos.domain.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原単位DB約款同意レスポンス情報
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitDbClausesAgreeResponseModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 子ユーザーID
  private String subUserId;

  // 約款同意ステータス
  private boolean agreeStatus;

  // DB名
  private String databaseName;

  // バージョン
  private String version;

  // 認証情報
  private String licenseInfo;

  // 期限切れ時間
  private String expiredDate;

}
