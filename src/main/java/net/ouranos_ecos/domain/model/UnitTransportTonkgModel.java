package net.ouranos_ecos.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原単位_改良トンキロ法
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitTransportTonkgModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 輸送_改良トンキロ法コード
  @JsonProperty("tonkgCd")
  private String tonkgCd;

  // 燃料・種類
  @JsonProperty("tonkgFuel")
  private String tonkgFuel;

  // 最大積載量
  @JsonProperty("tonkgMaxPayload")
  private BigDecimal tonkgMaxPayload;

  // 積載率(みなし）
  @JsonProperty("tonkgLoadFactor")
  private BigDecimal tonkgLoadFactor;

  // トンキロ係数L
  @JsonProperty("tonkgCoefficientL")
  private BigDecimal tonkgCoefficientL;

  // 燃焼
  @JsonProperty("tonkgCombustion")
  private BigDecimal tonkgCombustion;

  // 燃料製造
  @JsonProperty("tonkgFuelProduction")
  private BigDecimal tonkgFuelProduction;

  // 合計
  @JsonProperty("tonkgTotal")
  private BigDecimal tonkgTotal;

  // トンキロ係数kg
  @JsonProperty("tonkgCoefficientKg")
  private BigDecimal tonkgCoefficientKg;

}
