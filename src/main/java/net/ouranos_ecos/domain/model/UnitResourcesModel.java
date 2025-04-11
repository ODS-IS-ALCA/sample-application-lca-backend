package net.ouranos_ecos.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原単位_資材製造
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitResourcesModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 資材コード
  @JsonProperty("resourcesCd")
  private String resourcesCd;

  // 製品名
  @JsonProperty("resourcesProductName")
  private String resourcesProductName;

  // 製品名詳細
  @JsonProperty("resourcesProductDetails")
  private String resourcesProductDetails;

  // 製品コード
  @JsonProperty("resourcesProductCode")
  private String resourcesProductCode;

  // 単位
  @JsonProperty("resourcesUnit")
  private String resourcesUnit;

  // CO2原単位
  @JsonProperty("resourcesCo2Unit")
  private BigDecimal resourcesCo2Unit;

}
