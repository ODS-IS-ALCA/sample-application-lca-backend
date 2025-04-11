package net.ouranos_ecos.domain.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsvInputEnegryData implements Serializable {

  private static final long serialVersionUID = 1L;

  @JsonProperty("method")
  private String method;
  // 実測-電力
  @JsonProperty("electricPower")
  private String electricPower;
  // 実測-A重油
  @JsonProperty("crudeOilA")
  private String crudeOilA;
  // 実測-C重油
  @JsonProperty("crudeOilC")
  private String crudeOilC;
  // 実測-灯油
  @JsonProperty("kerosene")
  private String kerosene;
  // 実測-軽油
  @JsonProperty("diesel")
  private String diesel;
  // 実測-ガソリン
  @JsonProperty("gasoline")
  private String gasoline;
  // 実測-天然ガス液(NGL)
  @JsonProperty("ngl")
  private String ngl;
  // 実測-液化石油ガス(LPG)
  @JsonProperty("lpg")
  private String lpg;
  // 実測-天然ｶﾞｽ(LNG)
  @JsonProperty("lng")
  private String lng;
  // 実測-都市ガス
  @JsonProperty("cityGas")
  private String cityGas;
  // 実測-追加①
  @JsonProperty("add1")
  private String add1;
  // 実測-追加②
  @JsonProperty("add2")
  private String add2;

}
