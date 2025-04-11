package net.ouranos_ecos.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原単位_エネルギー
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitEnergyModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // エネルギーコード
  @JsonProperty("energyCd")
  private String energyCd;

  // データ種類
  @JsonProperty("energyType")
  private String energyType;

  // 電力
  @JsonProperty("energyElectric")
  private BigDecimal energyElectric;

  // A重油
  @JsonProperty("energyCrudeoila")
  private BigDecimal energyCrudeoila;

  // C重油
  @JsonProperty("energyCrudeoilc")
  private BigDecimal energyCrudeoilc;

  // 灯油
  @JsonProperty("energyKerosene")
  private BigDecimal energyKerosene;

  // 軽油
  @JsonProperty("energyDiesel")
  private BigDecimal energyDiesel;

  // ガソリン
  @JsonProperty("energyGasoline")
  private BigDecimal energyGasoline;

  // 天然ガス液(NGL)
  @JsonProperty("energyNgl")
  private BigDecimal energyNgl;

  // 液化石油ガス(LPG)
  @JsonProperty("energyLpg")
  private BigDecimal energyLpg;

  // 天然ｶﾞｽ(LNG)
  @JsonProperty("energyLng")
  private BigDecimal energyLng;

  // 都市ガス
  @JsonProperty("energyCitygus")
  private BigDecimal energyCitygus;

  // Free①
  @JsonProperty("energyFree1")
  private BigDecimal energyFree1;

  // Free②
  @JsonProperty("energyFree2")
  private BigDecimal energyFree2;

}
