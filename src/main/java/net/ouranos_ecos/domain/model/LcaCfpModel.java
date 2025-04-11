package net.ouranos_ecos.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 製品情報
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LcaCfpModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 内部事業者識別子
  @JsonProperty("operatorId")
  private String operatorId;
  // CFP情報識別子
  @JsonProperty("cfpId")
  private String cfpId;
  // トレース識別子
  @JsonProperty("traceId")
  private String traceId;
  // 素材報告値
  @JsonProperty("mReport")
  private String mReport;
  // 素材測定方法
  @JsonProperty("mMeasureMethods")
  private String mMeasureMethods;
  // 素材生産国コード
  @JsonProperty("mCountryCd")
  private String mCountryCd;
  // 素材リサイクル分類PIR
  @JsonProperty("mateialPir")
  private Integer mateialPir;
  // 素材リサイクル分類PCRELV
  @JsonProperty("mPcRelv")
  private Integer mPcRelv;
  // 素材リサイクル分類PCR他産業
  @JsonProperty("mCrOtherIndustry")
  private Integer mCrOtherIndustry;
  // 素材リサイクル分類分類不可
  @JsonProperty("mUnclassifiable")
  private Integer mUnclassifiable;
  // 素材リサイクル分類合計
  @JsonProperty("mTotal")
  private Integer mTotal;
  // 素材重量計算歩留り率
  @JsonProperty("mYieldRate")
  private BigDecimal mYieldRate;
  // 素材重量計算投入質量
  @JsonProperty("mInputWeight")
  private BigDecimal mInputWeight;
  // 素材直接排出原単位排出量
  @JsonProperty("mBaseUnitEmissions")
  private BigDecimal mBaseUnitEmissions;
  // 素材直接排出GHG排出
  @JsonProperty("mDirectGhg")
  private BigDecimal mDirectGhg;
  // 素材電力排出エネルギー比率
  @JsonProperty("mEnergyRate")
  private Integer mEnergyRate;
  // 素材電力排出電力原単位
  @JsonProperty("mElectricBaseUnit")
  private BigDecimal mElectricBaseUnit;
  // 素材電力排出消費電力
  @JsonProperty("mPowerConsumption")
  private BigDecimal mPowerConsumption;
  // 素材電力排出電力GHG排出
  @JsonProperty("mElectricGhg")
  private BigDecimal mElectricGhg;
  // 加工報告値
  @JsonProperty("pReport")
  private String pReport;
  // 加工測定方法
  @JsonProperty("pMeasureMethods")
  private String pMeasureMethods;
  // 加工生産国コード
  @JsonProperty("pCountryCd")
  private String pCountryCd;
  // 加工製造区分
  @JsonProperty("pManufacturingDivision")
  private String pManufacturingDivision;
  // 加工1コード
  @JsonProperty("p1Cd")
  private String p1Cd;
  // 加工2コード
  @JsonProperty("p2Cd")
  private String p2Cd;
  // 加工3コード
  @JsonProperty("p3Cd")
  private String p3Cd;
  // 加工4コード
  @JsonProperty("p4Cd")
  private String p4Cd;
  // 加工消費電力エネルギー比率
  @JsonProperty("pEngyRate")
  private Integer pEngyRate;
  // 加工消費電力原単位
  @JsonProperty("pElectricBaseUnit")
  private BigDecimal pElectricBaseUnit;
  // 加工消費電力量
  @JsonProperty("pElectricAmount")
  private BigDecimal pElectricAmount;
  // 加工消費エネルギー量A重油
  @JsonProperty("pCrudeOilA")
  private BigDecimal pCrudeOilA;
  // 加工消費エネルギー量C重油
  @JsonProperty("pCrudeOilC")
  private BigDecimal pCrudeOilC;
  // 加工消費エネルギー量灯油
  @JsonProperty("pKerosene")
  private BigDecimal pKerosene;
  // 加工消費エネルギー量軽油
  @JsonProperty("pDiesel")
  private BigDecimal pDiesel;
  // 加工消費エネルギー量ガソリン
  @JsonProperty("pGasoline")
  private BigDecimal pGasoline;
  // 加工消費エネルギー量NGL
  @JsonProperty("pNgl")
  private BigDecimal pNgl;
  // 加工消費エネルギー量LPG
  @JsonProperty("pLpg")
  private BigDecimal pLpg;
  // 加工消費エネルギー量LNG
  @JsonProperty("pLng")
  private BigDecimal pLng;
  // 加工消費エネルギー量都市ガス
  @JsonProperty("pCityGus")
  private BigDecimal pCityGus;
  // 加工消費エネルギー量フリー1
  @JsonProperty("pFree1")
  private BigDecimal pFree1;
  // 加工消費エネルギー量フリー2
  @JsonProperty("pFree2")
  private BigDecimal pFree2;
  // 加工その他
  @JsonProperty("pOtherWasteReport")
  private BigDecimal pOtherWasteReport;
  // 資材報告値
  @JsonProperty("rReport")
  private String rReport;
  // 資材測定方法
  @JsonProperty("rMeasureMethods")
  private String rMeasureMethods;
  // 資材工業用水道
  @JsonProperty("rIndustrialWaterSupply")
  private BigDecimal rIndustrialWaterSupply;
  // 資材上水道
  @JsonProperty("rWaterSupply")
  private BigDecimal rWaterSupply;
  // 資材圧縮空気(15m3/ min)
  @JsonProperty("rCompressedAir15")
  private BigDecimal rCompressedAir15;
  // 資材圧縮空気(90m3/ min)
  @JsonProperty("rCompressedAir90")
  private BigDecimal rCompressedAir90;
  // 資材シンナー
  @JsonProperty("rThinner")
  private BigDecimal rThinner;
  // 資材アンモニア
  @JsonProperty("rAmmonia")
  private BigDecimal rAmmonia;
  // 資材硝酸
  @JsonProperty("rNitricAcid")
  private BigDecimal rNitricAcid;
  // 資材か性ソーダ
  @JsonProperty("rCausticSoda")
  private BigDecimal rCausticSoda;
  // 資材塩酸
  @JsonProperty("rHydrochloricAcid")
  private BigDecimal rHydrochloricAcid;
  // 資材アセチレン
  @JsonProperty("rAcetylene")
  private BigDecimal rAcetylene;
  // 資材その他の無機化学工業製品
  @JsonProperty("rInorganicChemicalIndustrialProducts")
  private BigDecimal rInorganicChemicalIndustrialProducts;
  // 資材硫酸
  @JsonProperty("rSulfuricAcid")
  private BigDecimal rSulfuricAcid;
  // 資材無水クロム酸
  @JsonProperty("rAnhydrousChromicAcid")
  private BigDecimal rAnhydrousChromicAcid;
  // 資材その他の有機化学工業製品
  @JsonProperty("rOrganicChemicalIndustrialProducts")
  private BigDecimal rOrganicChemicalIndustrialProducts;
  // 資材その他の洗浄剤
  @JsonProperty("rCleaningAgents")
  private BigDecimal rCleaningAgents;
  // 資材セルロース系接着剤
  @JsonProperty("rCelluloseAdhesives")
  private BigDecimal rCelluloseAdhesives;
  // 資材潤滑油 (グリースを含む)
  @JsonProperty("rLubricatingOil")
  private BigDecimal rLubricatingOil;
  // 資材Free①
  @JsonProperty("rFree1")
  private BigDecimal rFree1;
  // 資材Free②
  @JsonProperty("rFree2")
  private BigDecimal rFree2;
  // 廃棄物報告値
  @JsonProperty("wReport")
  private String wReport;
  // 廃棄物測定方法
  @JsonProperty("wMeasureMethods")
  private String wMeasureMethods;
  // 廃棄物燃え殻
  @JsonProperty("wAsh")
  private BigDecimal wAsh;
  // 廃棄物鉱業等無機性汚泥
  @JsonProperty("wInorganicSludgeMining")
  private BigDecimal wInorganicSludgeMining;
  // 廃棄物製造業有機性汚泥
  @JsonProperty("wOrganicSludgeManufacturing")
  private BigDecimal wOrganicSludgeManufacturing;
  // 廃棄物製造排出廃プラスチック
  @JsonProperty("wWastePlasticsManufacturing")
  private BigDecimal wWastePlasticsManufacturing;
  // 廃棄物金属くず
  @JsonProperty("wMetalScrap")
  private BigDecimal wMetalScrap;
  // 廃棄物陶磁器くず
  @JsonProperty("wCeramicScrap")
  private BigDecimal wCeramicScrap;
  // 廃棄物鉱さい
  @JsonProperty("wSlag")
  private BigDecimal wSlag;
  // 廃棄物ばいじん
  @JsonProperty("wDust")
  private BigDecimal wDust;
  // 廃棄物石油由来廃油
  @JsonProperty("wWasteOilFromPetroleum")
  private BigDecimal wWasteOilFromPetroleum;
  // 廃棄物天然繊維くず
  @JsonProperty("wNaturalFiberScrap")
  private BigDecimal wNaturalFiberScrap;
  // 廃棄物ゴムくず
  @JsonProperty("wRubberScrap")
  private BigDecimal wRubberScrap;
  // 廃棄物廃酸
  @JsonProperty("wWasteAcid")
  private BigDecimal wWasteAcid;
  // 廃棄物廃アルカリ
  @JsonProperty("wWasteAlkali")
  private BigDecimal wWasteAlkali;
  // 廃棄物フリー1
  @JsonProperty("wFree1")
  private BigDecimal wFree1;
  // 廃棄物フリー2
  @JsonProperty("wFree2")
  private BigDecimal wFree2;
  // 輸送材料報告値
  @JsonProperty("tMaterialReport")
  private String tMaterialReport;
  // 輸送部品報告値
  @JsonProperty("tPartReport")
  private String tPartReport;
  // 輸送測定方法
  @JsonProperty("tMeasureMethods")
  private String tMeasureMethods;
  // 輸送重量材料投入質量
  @JsonProperty("tWeightMaterialInput")
  private BigDecimal tWeightMaterialInput;
  // 輸送重量材料排出量
  @JsonProperty("tWeightMaterialEmissions")
  private BigDecimal tWeightMaterialEmissions;
  // 輸送重量部品合計質量
  @JsonProperty("tWeightPartTotal")
  private BigDecimal tWeightPartTotal;
  // 輸送重量部品排出量
  @JsonProperty("tWeightPartEmissions")
  private BigDecimal tWeightPartEmissions;
  // 輸送燃料材料燃料種別
  @JsonProperty("tFuelMaterialType")
  private String tFuelMaterialType;
  // 輸送燃料材料燃料使用量
  @JsonProperty("tFuelMaterialConsumption")
  private BigDecimal tFuelMaterialConsumption;
  // 輸送燃料材料排出量
  @JsonProperty("tFuelMaterialEmissions")
  private BigDecimal tFuelMaterialEmissions;
  // 輸送燃料部品燃料種別
  @JsonProperty("tFuelPartType")
  private String tFuelPartType;
  // 輸送燃料部品燃料使用量
  @JsonProperty("tFuelPartConsumption")
  private BigDecimal tFuelPartConsumption;
  // 輸送燃料部品排出量
  @JsonProperty("tFuelPartEmissions")
  private BigDecimal tFuelPartEmissions;
  // 輸送燃費材料燃料種別
  @JsonProperty("tFuelEconomyMaterialType")
  private String tFuelEconomyMaterialType;
  // 輸送燃費材料走行距離
  @JsonProperty("tFuelEconomyMaterialMileage")
  private BigDecimal tFuelEconomyMaterialMileage;
  // 輸送燃費材料燃費
  @JsonProperty("tFuelEconomyMaterialFuelEconomy")
  private BigDecimal tFuelEconomyMaterialFuelEconomy;
  // 輸送燃費材料排出量
  @JsonProperty("tFuelEconomyMaterialEmissions")
  private BigDecimal tFuelEconomyMaterialEmissions;
  // 輸送燃費部品燃料種別
  @JsonProperty("tFuelEconomyPartType")
  private String tFuelEconomyPartType;
  // 輸送燃費部品走行距離
  @JsonProperty("tFuelEconomyPartMileage")
  private BigDecimal tFuelEconomyPartMileage;
  // 輸送燃費部品燃費
  @JsonProperty("tFuelEconomyPartFuelEconomy")
  private BigDecimal tFuelEconomyPartFuelEconomy;
  // 輸送燃費部品排出量
  @JsonProperty("tFuelEconomyPartEmissions")
  private BigDecimal tFuelEconomyPartEmissions;
  // 輸送トンキロ材料輸送種別
  @JsonProperty("tTonKgMaterialType")
  private String tTonKgMaterialType;
  // 輸送トンキロ材料輸送距離
  @JsonProperty("tTonKgMaterialMileage")
  private BigDecimal tTonKgMaterialMileage;
  // 輸送トンキロ材料排出量
  @JsonProperty("tTonKgMaterialEmissions")
  private BigDecimal tTonKgMaterialEmissions;
  // 輸送トンキロ部品輸送種別
  @JsonProperty("tTonKgPartType")
  private String tTonKgPartType;
  // 輸送トンキロ部品輸送距離
  @JsonProperty("tTonKgPartMileage")
  private BigDecimal tTonKgPartMileage;
  // 輸送トンキロ部品排出量
  @JsonProperty("tTonKgPartEmissions")
  private BigDecimal tTonKgPartEmissions;
}


