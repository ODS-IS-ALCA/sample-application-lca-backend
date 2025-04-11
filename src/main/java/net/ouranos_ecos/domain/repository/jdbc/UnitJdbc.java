package net.ouranos_ecos.domain.repository.jdbc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.ouranos_ecos.domain.model.UnitElectricModel;
import net.ouranos_ecos.domain.model.UnitEnergyModel;
import net.ouranos_ecos.domain.model.UnitMaterialsModel;
import net.ouranos_ecos.domain.model.UnitModel;
import net.ouranos_ecos.domain.model.UnitPartProcessingModel;
import net.ouranos_ecos.domain.model.UnitResourcesModel;
import net.ouranos_ecos.domain.model.UnitTransportFuelEconomyModel;
import net.ouranos_ecos.domain.model.UnitTransportFuelModel;
import net.ouranos_ecos.domain.model.UnitTransportTonkgModel;
import net.ouranos_ecos.domain.model.UnitTransportWeightModel;
import net.ouranos_ecos.domain.model.UnitWasteModel;
import net.ouranos_ecos.domain.repository.UnitRepository;

/**
 * UnitRepository実装クラス。
 */
@Repository
public class UnitJdbc implements UnitRepository {

  @Autowired
  private JdbcTemplate jdbc;

  @Override
  public UnitModel selectUnit() {

    UnitModel unitModel = new UnitModel();

    // 原単位_材料
    getUnitMaterials(unitModel);

    // 原単位_エネルギー
    getUnitEnergy(unitModel);

    // 原単位_廃棄物
    getUnitWaste(unitModel);

    // 原単位_輸送_重量法
    getUnitTransportWeight(unitModel);

    // 原単位_輸送_燃料法
    getUnitTransportFuel(unitModel);

    // 原単位_輸送_燃費法
    getUnitTransportFuelEconomy(unitModel);

    // 原単位_輸送_改良トンキロ法
    getUnitTransportTonkg(unitModel);

    // 原単位_電力
    getUnitElectric(unitModel);

    // 原単位_資材製造
    getUnitResources(unitModel);

    return unitModel;

  }

  /**
   * 原単位_材料 取得
   * 
   * @param unitModel
   */
  private void getUnitMaterials(UnitModel unitModel) {

    String sql = "SELECT materialcd , materialcategory , materialname"
        + " , materialtotalemissions , materialrecycleusagerate0 , materialrecycleusagerate100"
        + " , materialrecycleusageratedefault , materialunitdirectemissions0 , materialunitdirectemissions100"
        + " , materialunitdirectemissionsdefault , materialpowerconsumption0 , materialpowerconsumption100"
        + " , materialpowerconsumptiondefault , materialcomponentprocessingyield , materialwaste FROM"
        + " public.unitmaterials WHERE deleteflag IS NOT True ORDER BY materialcd";

    List<Map<String, Object>> resultLiist = jdbc.queryForList(sql);

    List<UnitMaterialsModel> modelList = new ArrayList<UnitMaterialsModel>();

    for (Map<String, Object> map : resultLiist) {

      UnitMaterialsModel model = new UnitMaterialsModel();
      model.setMaterialCd((String) map.get("materialcd"));
      model.setMaterialCategory((String) map.get("materialcategory"));
      model.setMaterialName((String) map.get("materialname"));
      model.setMaterialTotalEmissions((BigDecimal) map.get("materialtotalemissions"));
      model.setMaterialRecycleUsageRate0((BigDecimal) map.get("materialrecycleusagerate0"));
      model.setMaterialRecycleUsageRate100((BigDecimal) map.get("materialrecycleusagerate100"));
      model.setMaterialRecycleUsageRateDefault((BigDecimal) map.get("materialrecycleusageratedefault"));
      model.setMaterialUnitDirectEmissions0((BigDecimal) map.get("materialunitdirectemissions0"));
      model.setMaterialUnitDirectEmissions100((BigDecimal) map.get("materialunitdirectemissions100"));
      model.setMaterialUnitDirectEmissionsDefault((BigDecimal) map.get("materialunitdirectemissionsdefault"));
      model.setMaterialPowerConsumption0((BigDecimal) map.get("materialpowerconsumption0"));
      model.setMaterialPowerConsumption100((BigDecimal) map.get("materialpowerconsumption100"));
      model.setMaterialPowerConsumptionDefault((BigDecimal) map.get("materialpowerconsumptiondefault"));
      model.setMaterialComponentProcessingYield((BigDecimal) map.get("materialcomponentprocessingyield"));
      model.setMaterialWaste((String) map.get("materialwaste"));
      modelList.add(model);
    }
    unitModel.setUnitMaterialsModel(modelList);
  }

  /**
   * 原単位_エネルギー
   * 
   * @param unitModel
   */
  private void getUnitEnergy(UnitModel unitModel) {

    String sql = "SELECT energycd , energytype , energyelectric , energycrudeoila"
        + " , energycrudeoilc , energykerosene , energydiesel , energygasoline , energyngl"
        + " , energylpg , energylng , energycitygus , energyfree1 , energyfree2 FROM"
        + " public.unitenergy WHERE deleteflag IS NOT True ORDER BY energycd";

    List<Map<String, Object>> resultLiist = jdbc.queryForList(sql);

    List<UnitEnergyModel> modelList = new ArrayList<UnitEnergyModel>();

    for (Map<String, Object> map : resultLiist) {

      UnitEnergyModel model = new UnitEnergyModel();
      model.setEnergyCd((String) map.get("energycd"));
      model.setEnergyType((String) map.get("energytype"));
      model.setEnergyElectric((BigDecimal) map.get("energyelectric"));
      model.setEnergyCrudeoila((BigDecimal) map.get("energycrudeoila"));
      model.setEnergyCrudeoilc((BigDecimal) map.get("nergycrudeoilc"));
      model.setEnergyKerosene((BigDecimal) map.get("energykerosene"));
      model.setEnergyDiesel((BigDecimal) map.get("energydiesel"));
      model.setEnergyGasoline((BigDecimal) map.get("energygasoline"));
      model.setEnergyNgl((BigDecimal) map.get("energyngl"));
      model.setEnergyLpg((BigDecimal) map.get("energylpg"));
      model.setEnergyLng((BigDecimal) map.get("energylng"));
      model.setEnergyCitygus((BigDecimal) map.get("energycitygus"));
      model.setEnergyFree1((BigDecimal) map.get("energyfree1"));
      model.setEnergyFree2((BigDecimal) map.get("energyfree2"));
      modelList.add(model);
    }
    unitModel.setUnitEnergyModel(modelList);
  }

  /**
   * 原単位_廃棄物
   * 
   * @param unitModel
   */
  private void getUnitWaste(UnitModel unitModel) {

    String sql = "SELECT wastecd , wasteproductname , wasteproductdetails"
        + " , wasteproductcode , wasteunit , wasteco2unit FROM public.unitwaste WHERE"
        + " deleteflag IS NOT True ORDER BY wastecd";

    List<Map<String, Object>> resultLiist = jdbc.queryForList(sql);

    List<UnitWasteModel> modelList = new ArrayList<UnitWasteModel>();

    for (Map<String, Object> map : resultLiist) {

      UnitWasteModel model = new UnitWasteModel();
      model.setWasteCd((String) map.get("wastecd"));
      model.setWasteProductName((String) map.get("wasteproductname"));
      model.setWasteProductDetails((String) map.get("wasteproductdetails"));
      model.setWasteProductCode((String) map.get("wasteproductcode"));
      model.setWasteUnit((String) map.get("wasteunit"));
      model.setWasteCo2Unit((BigDecimal) map.get("wasteco2unit"));
      modelList.add(model);
    }
    unitModel.setUnitWasteModel(modelList);
  }

  /**
   * 原単位_輸送_重量法
   * 
   * @param unitModel
   */
  private void getUnitTransportWeight(UnitModel unitModel) {

    String sql = "SELECT weightcd , weightfuel , weightvehicle , weighttransport"
        + " , weightemissions FROM public.unittransportweight WHERE deleteflag IS NOT True  ORDER BY weightcd";

    List<Map<String, Object>> resultLiist = jdbc.queryForList(sql);

    List<UnitTransportWeightModel> modelList = new ArrayList<UnitTransportWeightModel>();

    for (Map<String, Object> map : resultLiist) {

      UnitTransportWeightModel model = new UnitTransportWeightModel();
      model.setWeightCd((String) map.get("weightcd"));
      model.setWeightFuel((String) map.get("weightfuel"));
      model.setWeightVehicle((BigDecimal) map.get("weightvehicle"));
      model.setWeightTransport((BigDecimal) map.get("weighttransport"));
      model.setWeightEmissions((BigDecimal) map.get("weightemissions"));
      modelList.add(model);
    }
    unitModel.setUnitTransportWeightModel(modelList);
  }

  /**
   * 原単位_輸送_燃料法
   * 
   * @param unitModel
   */
  private void getUnitTransportFuel(UnitModel unitModel) {

    String sql = "SELECT fuelcd , fuel , fuelconsumption , fuelcombustion"
        + " , fuelproduction , fueltotal , fuelmissions FROM public.unittransportfuel WHERE"
        + " deleteflag IS NOT True ORDER BY fuelcd";

    List<Map<String, Object>> resultLiist = jdbc.queryForList(sql);

    List<UnitTransportFuelModel> modelList = new ArrayList<UnitTransportFuelModel>();

    for (Map<String, Object> map : resultLiist) {

      UnitTransportFuelModel model = new UnitTransportFuelModel();
      model.setFuelCd((String) map.get("fuelcd"));
      model.setFuel((String) map.get("fuel"));
      model.setFuelConsumption((BigDecimal) map.get("fuelconsumption"));
      model.setFuelCombustion((BigDecimal) map.get("fuelcombustion"));
      model.setFuelProduction((BigDecimal) map.get("fuelproduction"));
      model.setFuelTotal((BigDecimal) map.get("fueltotal"));
      model.setFuelMissions((BigDecimal) map.get("fuelmissions"));
      modelList.add(model);
    }
    unitModel.setUnitTransportFuelModel(modelList);
  }

  /**
   * 原単位_輸送_燃費法
   * 
   * @param unitModel
   */
  private void getUnitTransportFuelEconomy(UnitModel unitModel) {

    String sql = "SELECT fueleconomycd , fueleconomyfuel , fueleconomy"
        + " , fueleconomytransport , fueleconomycombustionl , fueleconomyproduction , fueleconomytotal"
        + " , fueleconomymissions , fueleconomycombustionkm FROM public.unittransportfueleconomy"
        + " WHERE deleteflag IS NOT True ORDER BY fueleconomycd";

    List<Map<String, Object>> resultLiist = jdbc.queryForList(sql);

    List<UnitTransportFuelEconomyModel> modelList = new ArrayList<UnitTransportFuelEconomyModel>();

    for (Map<String, Object> map : resultLiist) {

      UnitTransportFuelEconomyModel model = new UnitTransportFuelEconomyModel();
      model.setFuelEconomyCd((String) map.get("fueleconomycd"));
      model.setFuelEconomyFuel((String) map.get("fueleconomyfuel"));
      model.setFuelEconomy((BigDecimal) map.get("fueleconomy"));
      model.setFuelEconomyTransport((BigDecimal) map.get("fueleconomytransport"));
      model.setFuelEconomyCombustionL((BigDecimal) map.get("fueleconomycombustionl"));
      model.setFuelEconomyProduction((BigDecimal) map.get("fueleconomyproduction"));
      model.setFuelEconomyTotal((BigDecimal) map.get("fueleconomytotal"));
      model.setFuelEconomyMissions((BigDecimal) map.get("fueleconomymissions"));
      model.setFuelEconomyCombustionKm((BigDecimal) map.get("fueleconomycombustionkm"));
      modelList.add(model);
    }
    unitModel.setUnitTransportFuelEconomyModel(modelList);
  }

  /**
   * 原単位_輸送_改良トンキロ法
   * 
   * @param unitModel
   */
  private void getUnitTransportTonkg(UnitModel unitModel) {

    String sql = "SELECT tonkgcd , tonkgfuel , tonkgmaxpayload , tonkgloadfactor"
        + " , tonkgcoefficientl , tonkgcombustion , tonkgfuelproduction , tonkgtotal"
        + " , tonkgcoefficientkg FROM public.unittransporttonkg WHERE deleteflag IS NOT True ORDER BY tonkgcd";

    List<Map<String, Object>> resultLiist = jdbc.queryForList(sql);

    List<UnitTransportTonkgModel> modelList = new ArrayList<UnitTransportTonkgModel>();

    for (Map<String, Object> map : resultLiist) {

      UnitTransportTonkgModel model = new UnitTransportTonkgModel();
      model.setTonkgCd((String) map.get("tonkgcd"));
      model.setTonkgFuel((String) map.get("tonkgfuel"));
      model.setTonkgMaxPayload((BigDecimal) map.get("tonkgmaxpayload"));
      model.setTonkgLoadFactor((BigDecimal) map.get("tonkgloadfactor"));
      model.setTonkgCoefficientL((BigDecimal) map.get("tonkgcoefficientl"));
      model.setTonkgCombustion((BigDecimal) map.get("tonkgcombustion"));
      model.setTonkgFuelProduction((BigDecimal) map.get("tonkgfuelproduction"));
      model.setTonkgTotal((BigDecimal) map.get("tonkgtotal"));
      model.setTonkgCoefficientKg((BigDecimal) map.get("tonkgcoefficientkg"));
      modelList.add(model);
    }
    unitModel.setUnitTransportTonkgModel(modelList);
  }

  /**
   * 原単位_電力
   * 
   * @param unitModel
   */
  private void getUnitElectric(UnitModel unitModel) {

    String sql = "SELECT electriccd , electriccountry , electricyear , electricscenario"
        + " , electricenergyratio , electricbaseunit"
        + " FROM public.unitelectric WHERE deleteflag IS NOT True ORDER BY electriccd";

    List<Map<String, Object>> resultLiist = jdbc.queryForList(sql);

    List<UnitElectricModel> modelList = new ArrayList<UnitElectricModel>();

    for (Map<String, Object> map : resultLiist) {

      UnitElectricModel model = new UnitElectricModel();
      model.setElectricCd((String) map.get("electriccd"));
      model.setElectricCountry((String) map.get("electriccountry"));
      model.setElectricYear((Integer) map.get("electricyear"));
      model.setElectricScenario((String) map.get("electricscenario"));
      model.setElectricEnergyRatio((BigDecimal) map.get("electricenergyratio"));
      model.setElectricBaseUnit((BigDecimal) map.get("electricbaseunit"));
      modelList.add(model);
    }
    unitModel.setUnitElectricModel(modelList);
  }

  /**
   * 原単位_資材製造
   * 
   * @param unitModel
   */
  private void getUnitResources(UnitModel unitModel) {

    String sql = "SELECT resourcescd , resourcesproductname , resourcesproductdetails , resourcesproductcode"
        + " , resourcesunit , resourcesco2unit"
        + " FROM public.unitresources WHERE deleteflag IS NOT True ORDER BY resourcescd";

    List<Map<String, Object>> resultLiist = jdbc.queryForList(sql);

    List<UnitResourcesModel> modelList = new ArrayList<UnitResourcesModel>();

    for (Map<String, Object> map : resultLiist) {

      UnitResourcesModel model = new UnitResourcesModel();
      model.setResourcesCd((String) map.get("resourcescd"));
      model.setResourcesProductName((String) map.get("resourcesproductname"));
      model.setResourcesProductDetails((String) map.get("resourcesproductdetails"));
      model.setResourcesProductCode((String) map.get("resourcesproductcode"));
      model.setResourcesUnit((String) map.get("resourcesunit"));
      model.setResourcesCo2Unit((BigDecimal) map.get("resourcesco2unit"));
      modelList.add(model);
    }
    unitModel.setUnitResourcesModel(modelList);
  }
  
  @Override
  public UnitElectricModel getUnitElectric(String electricCd) {

    String sql = "SELECT electriccd , electriccountry , electricyear , electricscenario"
        + " , electricenergyratio , electricbaseunit FROM public.unitelectric WHERE electricCd = ?";

    Map<String, Object> result = jdbc.queryForMap(sql, electricCd);

    UnitElectricModel model = new UnitElectricModel();
    model.setElectricCd((String) result.get("electriccd"));
    model.setElectricCountry((String) result.get("electriccountry"));
    model.setElectricYear((Integer) result.get("electricyear"));
    model.setElectricScenario((String) result.get("electricscenario"));
    model.setElectricEnergyRatio((BigDecimal) result.get("electricenergyratio"));
    model.setElectricBaseUnit((BigDecimal) result.get("electricbaseunit"));

    return model;

  }

  @Override
  public UnitMaterialsModel getUnitMaterials(String lcaMaterialCd) {

    String sql = "SELECT materialcd , materialcategory , materialname"
        + " , materialtotalemissions , materialrecycleusagerate0 , materialrecycleusagerate100"
        + " , materialrecycleusageratedefault , materialunitdirectemissions0 , materialunitdirectemissions100"
        + " , materialunitdirectemissionsdefault , materialpowerconsumption0 , materialpowerconsumption100"
        + " , materialpowerconsumptiondefault , materialcomponentprocessingyield , materialwaste FROM"
        + " public.unitmaterials WHERE materialcd = ?";
    
    Map<String, Object> result = jdbc.queryForMap(sql, lcaMaterialCd);

    UnitMaterialsModel model = new UnitMaterialsModel();
    model.setMaterialCd((String) result.get("materialcd"));
    model.setMaterialCategory((String) result.get("materialcategory"));
    model.setMaterialName((String) result.get("materialname"));
    model.setMaterialTotalEmissions((BigDecimal) result.get("materialtotalemissions"));
    model.setMaterialRecycleUsageRate0((BigDecimal) result.get("materialrecycleusagerate0"));
    model.setMaterialRecycleUsageRate100((BigDecimal) result.get("materialrecycleusagerate100"));
    model.setMaterialRecycleUsageRateDefault((BigDecimal) result.get("materialrecycleusageratedefault"));
    model.setMaterialUnitDirectEmissions0((BigDecimal) result.get("materialunitdirectemissions0"));
    model.setMaterialUnitDirectEmissions100((BigDecimal) result.get("materialunitdirectemissions100"));
    model.setMaterialUnitDirectEmissionsDefault((BigDecimal) result.get("materialunitdirectemissionsdefault"));
    model.setMaterialPowerConsumption0((BigDecimal) result.get("materialpowerconsumption0"));
    model.setMaterialPowerConsumption100((BigDecimal) result.get("materialpowerconsumption100"));
    model.setMaterialPowerConsumptionDefault((BigDecimal) result.get("materialpowerconsumptiondefault"));
    model.setMaterialComponentProcessingYield((BigDecimal) result.get("materialcomponentprocessingyield"));
    model.setMaterialWaste((String) result.get("materialwaste"));

    return model;

  }
  
  @Override
  public List<UnitEnergyModel> getUnitEnergy() {

    String sql = "SELECT energycd , energytype , energyelectric , energycrudeoila"
        + " , energycrudeoilc , energykerosene , energydiesel , energygasoline , energyngl"
        + " , energylpg , energylng , energycitygus , energyfree1 , energyfree2 FROM"
        + " public.unitenergy WHERE deleteflag IS NOT True ORDER BY energycd";

    List<Map<String, Object>> resultLiist = jdbc.queryForList(sql);

    List<UnitEnergyModel> modelList = new ArrayList<UnitEnergyModel>();

    for (Map<String, Object> map : resultLiist) {

      UnitEnergyModel model = new UnitEnergyModel();
      model.setEnergyCd((String) map.get("energycd"));
      model.setEnergyType((String) map.get("energytype"));
      model.setEnergyElectric((BigDecimal) map.get("energyelectric"));
      model.setEnergyCrudeoila((BigDecimal) map.get("energycrudeoila"));
      model.setEnergyCrudeoilc((BigDecimal) map.get("nergycrudeoilc"));
      model.setEnergyKerosene((BigDecimal) map.get("energykerosene"));
      model.setEnergyDiesel((BigDecimal) map.get("energydiesel"));
      model.setEnergyGasoline((BigDecimal) map.get("energygasoline"));
      model.setEnergyNgl((BigDecimal) map.get("energyngl"));
      model.setEnergyLpg((BigDecimal) map.get("energylpg"));
      model.setEnergyLng((BigDecimal) map.get("energylng"));
      model.setEnergyCitygus((BigDecimal) map.get("energycitygus"));
      model.setEnergyFree1((BigDecimal) map.get("energyfree1"));
      model.setEnergyFree2((BigDecimal) map.get("energyfree2"));
      modelList.add(model);
    }
    return modelList;
  }

  @Override
  public UnitWasteModel getUnitWaste(String wastecd) {

    String sql = "SELECT wastecd , wasteproductname , wasteproductdetails"
        + " , wasteproductcode , wasteunit , wasteco2unit FROM public.unitwaste WHERE"
        + " deleteflag IS NOT True AND wastecd = ? ";

    Map<String, Object> result = jdbc.queryForMap(sql, wastecd);

    UnitWasteModel model = new UnitWasteModel();
    model.setWasteCd((String) result.get("wastecd"));
    model.setWasteProductName((String) result.get("wasteproductname"));
    model.setWasteProductDetails((String) result.get("wasteproductdetails"));
    model.setWasteProductCode((String) result.get("wasteproductcode"));
    model.setWasteUnit((String) result.get("wasteunit"));
    model.setWasteCo2Unit((BigDecimal) result.get("wasteco2unit"));
    return model;

  }
  
  @Override
  public List<UnitTransportWeightModel> getUnitTransportWeight() {

    String sql = "SELECT weightcd , weightfuel , weightvehicle , weighttransport"
        + " , weightemissions FROM public.unittransportweight WHERE deleteflag IS NOT True  ORDER BY weightcd";

    List<Map<String, Object>> resultLiist = jdbc.queryForList(sql);

    List<UnitTransportWeightModel> modelList = new ArrayList<UnitTransportWeightModel>();

    for (Map<String, Object> map : resultLiist) {

      UnitTransportWeightModel model = new UnitTransportWeightModel();
      model.setWeightCd((String) map.get("weightcd"));
      model.setWeightFuel((String) map.get("weightfuel"));
      model.setWeightVehicle((BigDecimal) map.get("weightvehicle"));
      model.setWeightTransport((BigDecimal) map.get("weighttransport"));
      model.setWeightEmissions((BigDecimal) map.get("weightemissions"));
      modelList.add(model);
    }
    return modelList;
  }
  
  @Override
  public UnitPartProcessingModel getUnitPartProcessing(String partProcessingCd) {

    String sql = "SELECT partprocessingcd, partprocessingtype, partprocessingstep, partprocessingunit"
        + " , COALESCE(partprocessingelectric,0) AS partprocessingelectric"
        + " , COALESCE(partprocessingcrudeoila,0) AS partprocessingcrudeoila "
        + " , COALESCE(partprocessingcrudeoilc,0) AS partprocessingcrudeoilc"
        + " , COALESCE(partprocessingkerosene,0) AS partprocessingkerosene"
        + " , COALESCE(partprocessingdiesel,0) AS partprocessingdiesel"
        + " , COALESCE(partprocessinggasoline,0) AS partprocessinggasoline"
        + " , COALESCE(partprocessingngl,0) AS partprocessingngl"
        + " , COALESCE(partprocessinglpg,0) AS partprocessinglpg "
        + " , COALESCE(partprocessinglng,0) AS partprocessinglng "
        + " , COALESCE(partprocessingcitygus,0) AS partprocessingcitygus "
        + " , COALESCE(partprocessingfree1,0) AS partprocessingfree1 "
        + " , COALESCE(partprocessingfree2,0) AS partprocessingfree2 "
        + " , COALESCE(partprocessingoutput,0) AS partprocessingoutput "
        + " , COALESCE(partprocessingoutputelectderived,0) AS partprocessingoutputelectderived  "
        + " FROM public.unitpartprocessing " + " WHERE deleteflag IS NOT True AND partprocessingcd = ?";

    List<Map<String, Object>> resultLiist = jdbc.queryForList(sql, partProcessingCd);

    List<UnitPartProcessingModel> modelList = new ArrayList<UnitPartProcessingModel>();

    for (Map<String, Object> map : resultLiist) {

      UnitPartProcessingModel model = new UnitPartProcessingModel();
      model.setPartProcessingCd((String) map.get("partprocessingcd"));
      model.setPartProcessingType((String) map.get("partprocessingtype"));
      model.setPartProcessingStep((String) map.get("partprocessingstep"));
      model.setPartProcessingUnit((String) map.get("partprocessingunit"));
      model.setPartProcessingElectric((BigDecimal) map.get("partprocessingelectric"));
      model.setPartProcessingCrudeoilA((BigDecimal) map.get("partprocessingcrudeoila"));
      model.setPartProcessingCrudeoilC((BigDecimal) map.get("partprocessingcrudeoilc"));
      model.setPartProcessingKerosene((BigDecimal) map.get("partprocessingkerosene"));
      model.setPartProcessingDiesel((BigDecimal) map.get("partprocessingdiesel"));
      model.setPartProcessingGasoline((BigDecimal) map.get("partprocessinggasoline"));
      model.setPartProcessingNGL((BigDecimal) map.get("partprocessingngl"));
      model.setPartProcessingLPG((BigDecimal) map.get("partprocessinglpg"));
      model.setPartProcessingLNG((BigDecimal) map.get("partprocessinglng"));
      model.setPartProcessingCityGus((BigDecimal) map.get("partprocessingcitygus"));
      model.setPartProcessingFree1((BigDecimal) map.get("partprocessingfree1"));
      model.setPartProcessingFree2((BigDecimal) map.get("partprocessingfree2"));
      model.setPartProcessingOutput((BigDecimal) map.get("partprocessingoutput"));
      model.setPartProcessingOutputElectDerived((BigDecimal) map.get("partprocessingoutputelectderived"));
      modelList.add(model);
    }
    
    UnitPartProcessingModel unitPartProcessingModel = new UnitPartProcessingModel();
    
    if (modelList.size() > 0 ) {
      unitPartProcessingModel = modelList.get(0);
    }
    return unitPartProcessingModel;
  }
}
