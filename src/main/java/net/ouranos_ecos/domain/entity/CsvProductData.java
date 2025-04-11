package net.ouranos_ecos.domain.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsvProductData implements Serializable {

  private static final long serialVersionUID = 1L;

  // 内部事業者識別子（公開法人番号）
  @JsonProperty("openOperatorId")
  private String openOperatorId;

  // 製品トレース識別子
  @JsonProperty("productTraceId")
  private String productTraceId;

  // 製品名
  @JsonProperty("productItem")
  private String productItem;

  // 納入品番
  @JsonProperty("supplyItemNo")
  private String supplyItemNo;

  // 納入工場
  @JsonProperty("supplyFuctory")
  private String supplyFuctory;

  // 生産工場所在地
  @JsonProperty("fuctoryAddress")
  private String fuctoryAddress;

  // 回答者情報
  @JsonProperty("responceInfo")
  private String responceInfo;
}
