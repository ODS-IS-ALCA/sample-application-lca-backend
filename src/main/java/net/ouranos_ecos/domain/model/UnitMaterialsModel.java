package net.ouranos_ecos.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原単位_材料
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitMaterialsModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 材料コード
  @JsonProperty("materialCd")
  private String materialCd;

  // 材料カテゴリー
  @JsonProperty("materialCategory")
  private String materialCategory;

  // 材料名
  @JsonProperty("materialName")
  private String materialName;

  // 全体排出分
  @JsonProperty("materialTotalEmissions")
  private BigDecimal materialTotalEmissions;

  // リサイクル材使用率_0%狙い
  @JsonProperty("materialRecycleUsageRate0")
  private BigDecimal materialRecycleUsageRate0;

  // リサイクル材使用率_100%狙い
  @JsonProperty("materialRecycleUsageRate100")
  private BigDecimal materialRecycleUsageRate100;

  // リサイクル材使用率_デフォルト
  @JsonProperty("materialRecycleUsageRateDefault")
  private BigDecimal materialRecycleUsageRateDefault;

  // 素材原単位_直接排出_0%狙い
  @JsonProperty("materialUnitDirectEmissions0")
  private BigDecimal materialUnitDirectEmissions0;

  // 素材原単位_直接排出_100%狙い
  @JsonProperty("materialUnitDirectEmissions100")
  private BigDecimal materialUnitDirectEmissions100;

  // 素材原単位_直接排出_デフォルト
  @JsonProperty("materialUnitDirectEmissionsDefault")
  private BigDecimal materialUnitDirectEmissionsDefault;

  // 素材_消費電力_0%狙い
  @JsonProperty("materialPowerConsumption0")
  private BigDecimal materialPowerConsumption0;

  // 素材_消費電力_100%狙い
  @JsonProperty("materialPowerConsumption100")
  private BigDecimal materialPowerConsumption100;

  // 素材_消費電力_デフォルト
  @JsonProperty("materialPowerConsumptionDefault")
  private BigDecimal materialPowerConsumptionDefault;

  // 部品加工歩留り_デフォルト
  @JsonProperty("materialComponentProcessingYield")
  private BigDecimal materialComponentProcessingYield;

  // 廃棄物
  @JsonProperty("materialWaste")
  private String materialWaste;
}
