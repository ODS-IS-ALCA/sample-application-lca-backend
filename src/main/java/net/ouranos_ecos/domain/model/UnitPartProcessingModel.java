package net.ouranos_ecos.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原単位_部品加工
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitPartProcessingModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 部品加工コード
  private String partProcessingCd;

  // 分類
  private String partProcessingType;

  // 加工工程
  private String partProcessingStep;

  // 単位
  private String partProcessingUnit;

  // 電力
  private BigDecimal partProcessingElectric;

  // A重油
  private BigDecimal partProcessingCrudeoilA;

  // C重油
  private BigDecimal partProcessingCrudeoilC;

  // 灯油
  private BigDecimal partProcessingKerosene;

  // 軽油
  private BigDecimal partProcessingDiesel;

  // ガソリン
  private BigDecimal partProcessingGasoline;

  // NGL
  private BigDecimal partProcessingNGL;

  // LPG
  private BigDecimal partProcessingLPG;

  // 天然ｶﾞｽ(LNG)
  private BigDecimal partProcessingLNG;

  // 都市ｶﾞｽ
  private BigDecimal partProcessingCityGus;

  // Free①
  private BigDecimal partProcessingFree1;

  // Free②
  private BigDecimal partProcessingFree2;

  // OUTPUT
  private BigDecimal partProcessingOutput;

  // OUTPUT(電力由来を含まない)
  private BigDecimal partProcessingOutputElectDerived;

  // 登録日時
  private String createDat;

  // 更新日時
  private String modifieDat;

  // 論理削除フラグ
  private Boolean deleteFlag;

}