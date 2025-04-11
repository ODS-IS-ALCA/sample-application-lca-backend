package net.ouranos_ecos.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原単位_電力
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitElectricModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 電力コード
  @JsonProperty("electricCd")
  private String electricCd;

  // 国
  @JsonProperty("electricCountry")
  private String electricCountry;

  // 年
  @JsonProperty("electricYear")
  private Integer electricYear;

  // シナリオ
  @JsonProperty("electricScenario")
  private String electricScenario;

  // 再生可能エネルギー比率
  @JsonProperty("electricEnergyRatio")
  private BigDecimal electricEnergyRatio;

  // 電力原単位
  @JsonProperty("electricBaseUnit")
  private BigDecimal electricBaseUnit;

}
