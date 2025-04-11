package net.ouranos_ecos.domain.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原単位情報
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 原単位_材料
  private List<UnitMaterialsModel> unitMaterialsModel;
  // 原単位_エネルギー
  private List<UnitEnergyModel> unitEnergyModel;
  // 原単位_廃棄物
  private List<UnitWasteModel> unitWasteModel;
  // 原単位_重量法
  private List<UnitTransportWeightModel> unitTransportWeightModel;
  // 原単位_燃料法
  private List<UnitTransportFuelModel> unitTransportFuelModel;
  // 原単位_燃費法
  private List<UnitTransportFuelEconomyModel> unitTransportFuelEconomyModel;
  // 原単位_改良トンキロ法
  private List<UnitTransportTonkgModel> unitTransportTonkgModel;
  // 原単位_電力
  private List<UnitElectricModel> unitElectricModel;
  // 原単位_資材製造
  private List<UnitResourcesModel> unitResourcesModel;
  
}
