package net.ouranos_ecos.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原単位_重量法
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitTransportWeightModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 輸送_重量法コード
  @JsonProperty("weightCd")
  private String weightCd;

  // 燃料
  @JsonProperty("weightFuel")
  private String weightFuel;

  // 車両重量
  @JsonProperty("weightVehicle")
  private BigDecimal weightVehicle;

  // 輸送係数
  @JsonProperty("weightTransport")
  private BigDecimal weightTransport;

  // 排出量
  @JsonProperty("weightEmissions")
  private BigDecimal weightEmissions;

}
