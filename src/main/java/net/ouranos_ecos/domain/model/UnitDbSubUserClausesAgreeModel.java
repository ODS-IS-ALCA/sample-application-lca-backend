package net.ouranos_ecos.domain.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原単位DB認証情報
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitDbSubUserClausesAgreeModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 事業者識別子
  private String operatorId;

  // 子ユーザーID
  private List<String> subUserIdList;

  // トレース識別子
  private List<String> tradeIdList;

}
