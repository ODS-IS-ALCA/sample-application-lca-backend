package net.ouranos_ecos.domain.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 依頼リスト情報
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalcRequestModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // トレース識別子
  private String traceId;

  // 依頼ステータスコード
  private String requestStatus;

  // 品番
  private String partsName;

  // 品名
  private String partsLabelName;

  // 補助項目
  private String supportPartsName;

  // 公開法人番号
  private String requestedToOperatorId;

  // 事業者名
  private String requestedToOperatorName;

  // 回答単位
  private String responseUnit;

  // 依頼メッセージ
  private String requestMessage;

}