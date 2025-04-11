package net.ouranos_ecos.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原単位_廃棄物
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitWasteModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 廃棄物コード
  @JsonProperty("wasteCd")
  private String wasteCd;

  // 製品名
  @JsonProperty("wasteProductName")
  private String wasteProductName;

  // 製品名詳細
  @JsonProperty("wasteProductDetails")
  private String wasteProductDetails;

  // 製品コード
  @JsonProperty("wasteProductCode")
  private String wasteProductCode;

  // 単位
  @JsonProperty("wasteUnit")
  private String wasteUnit;

  // CO2原単位
  @JsonProperty("wasteCo2Unit")
  private BigDecimal wasteCo2Unit;

}
