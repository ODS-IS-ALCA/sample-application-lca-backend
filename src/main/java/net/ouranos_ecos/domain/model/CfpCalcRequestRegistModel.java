package net.ouranos_ecos.domain.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 依頼情報
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CfpCalcRequestRegistModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 内部事業者識別子
  @JsonProperty("operatorId")
  private String operatorId;
  
  // 依頼元事業者識別子
  @JsonProperty("requestedFromOperatorId")
  private String requestedFromOperatorId;
  
  // 依頼元トレース識別子
  @JsonProperty("requestedFromTraceId")
  private String requestedFromTraceId;
  
  // 依頼先事業者識別子
  @JsonProperty("requestedToOperatorId")
  private String requestedToOperatorId;
  
  // 依頼メッセージ
  @JsonProperty("requestMessage")
  private String requestMessage;
  
  // 回答単位
  @JsonProperty("responseUnit")
  private String responseUnit;
  
}
