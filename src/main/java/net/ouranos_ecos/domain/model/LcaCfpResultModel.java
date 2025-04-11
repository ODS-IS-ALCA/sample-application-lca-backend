package net.ouranos_ecos.domain.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LCACFP結果情報
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LcaCfpResultModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 製品名
  @JsonProperty("productItem")
  private String productItem;
  // 納入品番
  @JsonProperty("supplyItemNo")
  private String supplyItemNo;
  // 納入工場
  @JsonProperty("supplyFuctory")
  private String supplyFuctory;
  // 生産工場所在地(国/都市)
  @JsonProperty("fuctoryAddress")
  private String fuctoryAddress;
  // 回答者情報
  @JsonProperty("responceInfo")
  private String responceInfo;
  // 鉄
  @JsonProperty("iron")
  private String iron;
  // アルミ
  @JsonProperty("aluminum")
  private String aluminum;
  // 銅
  @JsonProperty("copper")
  private String copper;
  // 非鉄金属
  @JsonProperty("nonFerrousMetals")
  private String nonFerrousMetals;
  // 樹脂
  @JsonProperty("resin")
  private String resin;
  // その他
  @JsonProperty("others")
  private String others;
  // 材料合計
  @JsonProperty("materialsTotal")
  private String materialsTotal;
  // 実測-電力
  @JsonProperty("actualElectricPower")
  private String actualElectricPower;
  // 実測-A重油
  @JsonProperty("actualCrudeOilA")
  private String actualCrudeOilA;
  // 実測-C重油
  @JsonProperty("actualCrudeOilC")
  private String actualCrudeOilC;
  // 実測-灯油
  @JsonProperty("actualKerosene")
  private String actualKerosene;
  // 実測-軽油
  @JsonProperty("actualDiesel")
  private String actualDiesel;
  // 実測-ガソリン
  @JsonProperty("actualGasoline")
  private String actualGasoline;
  // 実測-天然ガス液(NGL)
  @JsonProperty("actualNgl")
  private String actualNgl;
  // 実測-液化石油ガス(LPG)
  @JsonProperty("actualLpg")
  private String actualLpg;
  // 実測-天然ｶﾞｽ(LNG)
  @JsonProperty("actualLng")
  private String actualLng;
  // 実測-都市ガス
  @JsonProperty("actualCityGas")
  private String actualCityGas;
  // 実測-追加①
  @JsonProperty("actualAdd1")
  private String actualAdd1;
  // 実測-追加②
  @JsonProperty("actualAdd2")
  private String actualAdd2;
  // 簡易計算-電力
  @JsonProperty("simpleElectricPower")
  private String simpleElectricPower;
  // 簡易計算-A重油
  @JsonProperty("simpleCrudeOilA")
  private String simpleCrudeOilA;
  // 簡易計算-C重油
  @JsonProperty("simpleCrudeOilC")
  private String simpleCrudeOilC;
  // 簡易計算-灯油
  @JsonProperty("simpleKerosene")
  private String simpleKerosene;
  // 簡易計算-軽油
  @JsonProperty("simpleDiesel")
  private String simpleDiesel;
  // 簡易計算-ガソリン
  @JsonProperty("simpleGasoline")
  private String simpleGasoline;
  // 簡易計算-天然ガス液(NGL)
  @JsonProperty("simpleNgl")
  private String simpleNgl;
  // 簡易計算-液化石油ガス(LPG)
  @JsonProperty("simpleLpg")
  private String simpleLpg;
  // 簡易計算-天然ｶﾞｽ(LNG)
  @JsonProperty("simpleLng")
  private String simpleLng;
  // 簡易計算-都市ガス
  @JsonProperty("simpleCityGas")
  private String simpleCityGas;
  // 簡易計算-追加①
  @JsonProperty("simpleAdd1")
  private String simpleAdd1;
  // 簡易計算-追加②
  @JsonProperty("simpleAdd2")
  private String simpleAdd2;
  // 合計-電力
  @JsonProperty("totalElectricPower")
  private String totalElectricPower;
  // 合計-A重油
  @JsonProperty("totalCrudeOilA")
  private String totalCrudeOilA;
  // 合計-C重油
  @JsonProperty("totalCrudeOilC")
  private String totalCrudeOilC;
  // 合計-灯油
  @JsonProperty("totalKerosene")
  private String totalKerosene;
  // 合計-軽油
  @JsonProperty("totalDiesel")
  private String totalDiesel;
  // 合計-ガソリン
  @JsonProperty("totalGasoline")
  private String totalGasoline;
  // 合計-天然ガス液(NGL)
  @JsonProperty("totalNgl")
  private String totalNgl;
  // 合計-液化石油ガス(LPG)
  @JsonProperty("totalLpg")
  private String totalLpg;
  // 合計-天然ｶﾞｽ(LNG)
  @JsonProperty("totalLng")
  private String totalLng;
  // 合計-都市ガス
  @JsonProperty("totalCityGas")
  private String totalCityGas;
  // 合計-追加①
  @JsonProperty("totalAdd1")
  private String totalAdd1;
  // 合計-追加②
  @JsonProperty("totalAdd2")
  private String totalAdd2;
  // 部品加工-内製
  @JsonProperty("partsIn")
  private String partsIn;
  // 部品加工-外製
  @JsonProperty("partsOut")
  private String partsOut;
  // 材料製造-鉄
  @JsonProperty("materialIron")
  private String materialIron;
  // 材料製造-アルミ
  @JsonProperty("materialAluminum")
  private String materialAluminum;
  // 材料製造-銅
  @JsonProperty("materialCopper")
  private String materialCopper;
  // 材料製造-非鉄金属
  @JsonProperty("materialNonFerrousMetals")
  private String materialNonFerrousMetals;
  // 材料製造-樹脂
  @JsonProperty("materialResin")
  private String materialResin;
  // 材料製造-その他
  @JsonProperty("materialOthers")
  private String materialOthers;
  // 計
  @JsonProperty("subTotal")
  private String subTotal;
  // 資材製造
  @JsonProperty("resources")
  private String resources;
  // 輸送-材料輸送
  @JsonProperty("transportMaterial")
  private String transportMaterial;
  // 輸送-部品輸送
  @JsonProperty("transportParts")
  private String transportParts;
  // 廃棄
  @JsonProperty("waste")
  private String waste;
  // 合計
  @JsonProperty("total")
  private String total;

}
