package net.ouranos_ecos.domain.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 依頼情報
 */
@Data
public class CfpRequestModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 依頼識別子
  @JsonProperty("requestId")
  private String requestId;

  // 依頼元事業者識別子
  @JsonProperty("requestedFromOperatorId")
  private String requestedFromOperatorId;

  // 依頼元事業者名
  @JsonProperty("requestedFromOperatorName")
  private String requestedFromOperatorName;

  // 品番
  @JsonProperty("partsName")
  private String partsName;

  // 品名
  @JsonProperty("partsLabelName")
  private String partsLabelName;

  // 補助項目
  @JsonProperty("supportPartsName")
  private String supportPartsName;

  // 回答単位
  @JsonProperty("responseUnit")
  private String responseUnit;

  // 依頼メッセージ
  @JsonProperty("requestMessage")
  private String requestMessage;

  // 依頼日時
  @JsonProperty("requesteDat")
  private String requesteDat;

  // 依頼ステータスコード
  @JsonProperty("responseStatus")
  private String responseStatus;
  
  // 更新日時
  @JsonProperty("modifiedAt")
  private String modifiedAt;

}
