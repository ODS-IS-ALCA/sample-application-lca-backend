package net.ouranos_ecos.domain.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 製品情報
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 内部事業者識別子
  @JsonProperty("operatorId")
  private String operatorId;

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

  //  回答者情報
  @JsonProperty("responceInfo")
  private String responceInfo;

  // 更新日時
  @JsonProperty("modifiedAt")
  private String modifiedAt;

  // 素材合計重量(g)
  @JsonProperty("materialsTotal")
  private String materialsTotal;

  // CFP(g-CO2wq)
  @JsonProperty("gco2eqTotal")
  private String gco2eqTotal;

  // 最終更新日
  @JsonProperty("cfpModifieDat")
  private String cfpModifieDat;

}
