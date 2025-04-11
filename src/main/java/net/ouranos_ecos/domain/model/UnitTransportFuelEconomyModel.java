package net.ouranos_ecos.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原単位_燃費法
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitTransportFuelEconomyModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 輸送_燃費法コード
  @JsonProperty("fuelEconomyCd")
  private String fuelEconomyCd;

  // 燃料
  @JsonProperty("fuelEconomyFuel")
  private String fuelEconomyFuel;

  // 燃費
  @JsonProperty("fuelEconomy")
  private BigDecimal fuelEconomy;

  // 輸送距離
  @JsonProperty("fuelEconomyTransport")
  private BigDecimal fuelEconomyTransport;

  // 燃焼L
  @JsonProperty("fuelEconomyCombustionL")
  private BigDecimal fuelEconomyCombustionL;

  // 燃料製造
  @JsonProperty("fuelEconomyProduction")
  private BigDecimal fuelEconomyProduction;

  // 合計
  @JsonProperty("fuelEconomyTotal")
  private BigDecimal fuelEconomyTotal;

  // 排出量
  @JsonProperty("fuelEconomyMissions")
  private BigDecimal fuelEconomyMissions;

  // 燃焼km
  @JsonProperty("fuelEconomyCombustionKm")
  private BigDecimal fuelEconomyCombustionKm;

}
