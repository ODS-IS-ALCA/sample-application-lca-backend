package net.ouranos_ecos.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原単位_燃料法
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitTransportFuelModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 輸送_燃料法コード
  @JsonProperty("fuelCd")
  private String fuelCd;

  // 燃料
  @JsonProperty("fuel")
  private String fuel;

  // 燃料使用量 L
  @JsonProperty("fuelConsumption")
  private BigDecimal fuelConsumption;

  // 燃焼
  @JsonProperty("fuelCombustion")
  private BigDecimal fuelCombustion;

  // 燃料製造
  @JsonProperty("fuelProduction")
  private BigDecimal fuelProduction;

  // 合計
  @JsonProperty("fuelTotal")
  private BigDecimal fuelTotal;

  // 排出量
  @JsonProperty("fuelMissions")
  private BigDecimal fuelMissions;

}
