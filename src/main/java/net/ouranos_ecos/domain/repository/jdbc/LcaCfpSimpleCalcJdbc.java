package net.ouranos_ecos.domain.repository.jdbc;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.ouranos_ecos.domain.model.LcaCfpSimpleCalcModel;
import net.ouranos_ecos.domain.repository.LcaCfpSimpleCalcRepository;

/**
 * LcaCfpSimpleCalcRepository実装クラス。
 */
@Repository
public class LcaCfpSimpleCalcJdbc implements LcaCfpSimpleCalcRepository {

  @Autowired
  private JdbcTemplate jdbc;

  @Override
  public int insert(LcaCfpSimpleCalcModel model) {

    String sql = "INSERT INTO public.lcacfpsimplecalc(operatorid, cfpid, traceid, mreport, "
        + "mmeasuremethods, mcountrycd, mateialpir, mpcrelv, mcrotherindustry, "
        + "munclassifiable, mtotal, myieldrate, minputweight, mbaseunitemissions, mdirectghg, "
        + "mengyrate, melectricbaseunit, mpowerconsumption, melectricghg, preport, "
        + "pmeasuremethods, pcountrycd, pmanufacturingdivision, p1cd, p2cd, p3cd, p4cd, "
        + "pengyrate, pelectricbaseunit, pelectricamount, pcrudeoila, pcrudeoilc, pkerosene, "
        + "pdiesel, pgasoline, pngl, plpg, plng, pcitygus, pfree1, pfree2, "
        + "potherwastereport, rreport, rmeasuremethods, rindustrialwatersupply, rwatersupply, "
        + "rcompressedair15, rcompressedair90, rthinner, rammonia, rnitricacid, rcausticsoda, "
        + "rhydrochloricacid, racetylene, rinorganicchemicalindustrialproducts, rsulfuricacid, "
        + "ranhydrouschromicacid, rorganicchemicalindustrialproducts, rcleaningagents, "
        + "rcelluloseadhesives, rlubricatingoil, rfree1, rfree2, wreport, wmeasuremethods, "
        + "wash, winorganicsludgemining, worganicsludgemanufacturing, wwasteplasticsmanufacturing, "
        + "wmetalscrap, wceramicscrap, wslag, wdust, wwasteoilfrompetroleum, "
        + "wnaturalfiberscrap, wrubberscrap, wwasteacid, wwastealkali, wfree1, wfree2, "
        + "tmaterialreport, tpartreport, tmeasuremethods, tweightmaterialinput, "
        + "tweightmaterialemissions, tweightparttotal, tweightpartemissions, tfuelmaterialtype, "
        + "tfuelmaterialconsumption, tfuelmaterialemissions, tfuelparttype, tfuelpartconsumption, "
        + "tfuelpartemissions, tfueleconomymaterialtype, tfueleconomymaterialmileage, "
        + "tfueleconomymaterialfueleconomy, tfueleconomymaterialemissions, tfueleconomyparttype, "
        + "tfueleconomypartmileage, tfueleconomypartfueleconomy, tfueleconomypartemissions, "
        + "ttonkgmaterialtype, ttonkgmaterialmileage, ttonkgmaterialemissions, ttonkgparttype, "
        + "ttonkgpartmileage, ttonkgpartemissions, createdat, modifiedat, deleteflag) VALUES ("
        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    Date nowDate = new Date();

    int result = jdbc.update(sql, model.getOperatorId(), model.getCfpId(), model.getTraceId(), model.getMReport(),
        model.getMMeasureMethods(), model.getMCountryCd(), model.getMateialPir(), model.getMPcRelv(),
        model.getMCrOtherIndustry(), model.getMUnclassifiable(), model.getMTotal(), model.getMYieldRate(),
        model.getMInputWeight(), model.getMBaseUnitEmissions(), model.getMDirectGhg(), model.getMEnergyRate(),
        model.getMElectricBaseUnit(), model.getMPowerConsumption(), model.getMElectricGhg(), model.getPReport(),
        model.getPMeasureMethods(), model.getPCountryCd(), model.getPManufacturingDivision(), model.getP1Cd(),
        model.getP2Cd(), model.getP3Cd(), model.getP4Cd(), model.getPEngyRate(), model.getPElectricBaseUnit(),
        model.getPElectricAmount(), model.getPCrudeOilA(), model.getPCrudeOilC(), model.getPKerosene(),
        model.getPDiesel(), model.getPGasoline(), model.getPNgl(), model.getPLpg(), model.getPLng(),
        model.getPCityGus(), model.getPFree1(), model.getPFree2(), model.getPOtherWasteReport(), model.getRReport(),
        model.getRMeasureMethods(), model.getRIndustrialWaterSupply(), model.getRWaterSupply(),
        model.getRCompressedAir15(), model.getRCompressedAir90(), model.getRThinner(), model.getRAmmonia(),
        model.getRNitricAcid(), model.getRCausticSoda(), model.getRHydrochloricAcid(), model.getRAcetylene(),
        model.getRInorganicChemicalIndustrialProducts(), model.getRSulfuricAcid(), model.getRAnhydrousChromicAcid(),
        model.getROrganicChemicalIndustrialProducts(), model.getRCleaningAgents(), model.getRCelluloseAdhesives(),
        model.getRLubricatingOil(), model.getRFree1(), model.getRFree2(), model.getWReport(),
        model.getWMeasureMethods(), model.getWAsh(), model.getWInorganicSludgeMining(),
        model.getWOrganicSludgeManufacturing(), model.getWWastePlasticsManufacturing(), model.getWMetalScrap(),
        model.getWCeramicScrap(), model.getWSlag(), model.getWDust(), model.getWWasteOilFromPetroleum(),
        model.getWNaturalFiberScrap(), model.getWRubberScrap(), model.getWWasteAcid(), model.getWWasteAlkali(),
        model.getWFree1(), model.getWFree2(), model.getTMaterialReport(), model.getTPartReport(),
        model.getTMeasureMethods(), model.getTWeightMaterialInput(), model.getTWeightMaterialEmissions(),
        model.getTWeightPartTotal(), model.getTWeightPartEmissions(), model.getTFuelMaterialType(),
        model.getTFuelMaterialConsumption(), model.getTFuelMaterialEmissions(), model.getTFuelPartType(),
        model.getTFuelPartConsumption(), model.getTFuelPartEmissions(), model.getTFuelEconomyMaterialType(),
        model.getTFuelEconomyMaterialMileage(), model.getTFuelEconomyMaterialFuelEconomy(),
        model.getTFuelEconomyMaterialEmissions(), model.getTFuelEconomyPartType(), model.getTFuelEconomyPartMileage(),
        model.getTFuelEconomyPartFuelEconomy(), model.getTFuelEconomyPartEmissions(), model.getTTonKgMaterialType(),
        model.getTTonKgMaterialMileage(), model.getTTonKgMaterialEmissions(), model.getTTonKgPartType(),
        model.getTTonKgPartMileage(), model.getTTonKgPartEmissions(), nowDate, nowDate, false);
    return result;

  }

  @Override
  public int[] insertLcaCfpSimpleCalc(List<LcaCfpSimpleCalcModel> lcaCSCModelList) throws Exception {

    Date nowDate = new Date();

    List<Object[]> batchArgs = lcaCSCModelList.stream()
        .map(model -> new Object[]{model.getOperatorId(), model.getCfpId(), model.getTraceId(), model.getMReport(),
            model.getMMeasureMethods(), model.getMCountryCd(), model.getMateialPir(), model.getMPcRelv(),
            model.getMCrOtherIndustry(), model.getMUnclassifiable(), model.getMTotal(), model.getMYieldRate(),
            model.getMInputWeight(), model.getMBaseUnitEmissions(), model.getMDirectGhg(), model.getMEnergyRate(),
            model.getMElectricBaseUnit(), model.getMPowerConsumption(), model.getMElectricGhg(), model.getPReport(),
            model.getPMeasureMethods(), model.getPCountryCd(), model.getPManufacturingDivision(), model.getP1Cd(),
            model.getP2Cd(), model.getP3Cd(), model.getP4Cd(), model.getPEngyRate(), model.getPElectricBaseUnit(),
            model.getPElectricAmount(), model.getPCrudeOilA(), model.getPCrudeOilC(), model.getPKerosene(),
            model.getPDiesel(), model.getPGasoline(), model.getPNgl(), model.getPLpg(), model.getPLng(),
            model.getPCityGus(), model.getPFree1(), model.getPFree2(), model.getPOtherWasteReport(), model.getRReport(),
            model.getRMeasureMethods(), model.getRIndustrialWaterSupply(), model.getRWaterSupply(),
            model.getRCompressedAir15(), model.getRCompressedAir90(), model.getRThinner(), model.getRAmmonia(),
            model.getRNitricAcid(), model.getRCausticSoda(), model.getRHydrochloricAcid(), model.getRAcetylene(),
            model.getRInorganicChemicalIndustrialProducts(), model.getRSulfuricAcid(), model.getRAnhydrousChromicAcid(),
            model.getROrganicChemicalIndustrialProducts(), model.getRCleaningAgents(), model.getRCelluloseAdhesives(),
            model.getRLubricatingOil(), model.getRFree1(), model.getRFree2(), model.getWReport(),
            model.getWMeasureMethods(), model.getWAsh(), model.getWInorganicSludgeMining(),
            model.getWOrganicSludgeManufacturing(), model.getWWastePlasticsManufacturing(), model.getWMetalScrap(),
            model.getWCeramicScrap(), model.getWSlag(), model.getWDust(), model.getWWasteOilFromPetroleum(),
            model.getWNaturalFiberScrap(), model.getWRubberScrap(), model.getWWasteAcid(), model.getWWasteAlkali(),
            model.getWFree1(), model.getWFree2(), model.getTMaterialReport(), model.getTPartReport(),
            model.getTMeasureMethods(), model.getTWeightMaterialInput(), model.getTWeightMaterialEmissions(),
            model.getTWeightPartTotal(), model.getTWeightPartEmissions(), model.getTFuelMaterialType(),
            model.getTFuelMaterialConsumption(), model.getTFuelMaterialEmissions(), model.getTFuelPartType(),
            model.getTFuelPartConsumption(), model.getTFuelPartEmissions(), model.getTFuelEconomyMaterialType(),
            model.getTFuelEconomyMaterialMileage(), model.getTFuelEconomyMaterialFuelEconomy(),
            model.getTFuelEconomyMaterialEmissions(), model.getTFuelEconomyPartType(), model.getTFuelEconomyPartMileage(),
            model.getTFuelEconomyPartFuelEconomy(), model.getTFuelEconomyPartEmissions(), model.getTTonKgMaterialType(),
            model.getTTonKgMaterialMileage(), model.getTTonKgMaterialEmissions(), model.getTTonKgPartType(),
            model.getTTonKgPartMileage(), model.getTTonKgPartEmissions(), nowDate, nowDate, false})
        .collect(Collectors.toList());

    String sql = "INSERT INTO public.lcacfpsimplecalc(operatorid, cfpid, traceid, mreport, "
        + "mmeasuremethods, mcountrycd, mateialpir, mpcrelv, mcrotherindustry, "
        + "munclassifiable, mtotal, myieldrate, minputweight, mbaseunitemissions, mdirectghg, "
        + "mengyrate, melectricbaseunit, mpowerconsumption, melectricghg, preport, "
        + "pmeasuremethods, pcountrycd, pmanufacturingdivision, p1cd, p2cd, p3cd, p4cd, "
        + "pengyrate, pelectricbaseunit, pelectricamount, pcrudeoila, pcrudeoilc, pkerosene, "
        + "pdiesel, pgasoline, pngl, plpg, plng, pcitygus, pfree1, pfree2, "
        + "potherwastereport, rreport, rmeasuremethods, rindustrialwatersupply, rwatersupply, "
        + "rcompressedair15, rcompressedair90, rthinner, rammonia, rnitricacid, rcausticsoda, "
        + "rhydrochloricacid, racetylene, rinorganicchemicalindustrialproducts, rsulfuricacid, "
        + "ranhydrouschromicacid, rorganicchemicalindustrialproducts, rcleaningagents, "
        + "rcelluloseadhesives, rlubricatingoil, rfree1, rfree2, wreport, wmeasuremethods, "
        + "wash, winorganicsludgemining, worganicsludgemanufacturing, wwasteplasticsmanufacturing, "
        + "wmetalscrap, wceramicscrap, wslag, wdust, wwasteoilfrompetroleum, "
        + "wnaturalfiberscrap, wrubberscrap, wwasteacid, wwastealkali, wfree1, wfree2, "
        + "tmaterialreport, tpartreport, tmeasuremethods, tweightmaterialinput, "
        + "tweightmaterialemissions, tweightparttotal, tweightpartemissions, tfuelmaterialtype, "
        + "tfuelmaterialconsumption, tfuelmaterialemissions, tfuelparttype, tfuelpartconsumption, "
        + "tfuelpartemissions, tfueleconomymaterialtype, tfueleconomymaterialmileage, "
        + "tfueleconomymaterialfueleconomy, tfueleconomymaterialemissions, tfueleconomyparttype, "
        + "tfueleconomypartmileage, tfueleconomypartfueleconomy, tfueleconomypartemissions, "
        + "ttonkgmaterialtype, ttonkgmaterialmileage, ttonkgmaterialemissions, ttonkgparttype, "
        + "ttonkgpartmileage, ttonkgpartemissions, createdat, modifiedat, deleteflag) VALUES ("
        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // まとめて更新
    int[] results = jdbc.batchUpdate(sql, batchArgs);

    for (int value : results) {
      if (value == 0) {
        // 更新出来ない行がある場合エラーとする
        throw new Exception();
      }
    }
    return results;
  }

  @Override
  public int update(LcaCfpSimpleCalcModel model) {

    String sql = "UPDATE public.lcacfpsimplecalc SET mreport = ?, mmeasuremethods = ?, mcountrycd = ?, "
        + "mateialpir = ?, mpcrelv = ?, mcrotherindustry = ?, munclassifiable = ?, mtotal = ?, "
        + "myieldrate = ?, minputweight = ?, mbaseunitemissions = ?, mdirectghg = ?, "
        + "mengyrate = ?, melectricbaseunit = ?, mpowerconsumption = ?, melectricghg = ?, "
        + "preport = ?, pmeasuremethods = ?, pcountrycd = ?, pmanufacturingdivision = ?, p1cd = ?, "
        + "p2cd = ?, p3cd = ?, p4cd = ?, pengyrate = ?, pelectricbaseunit = ?, "
        + "pelectricamount = ?, pcrudeoila = ?, pcrudeoilc = ?, pkerosene = ?, pdiesel = ?, "
        + "pgasoline = ?, pngl = ?, plpg = ?, plng = ?, pcitygus = ?, pfree1 = ?, "
        + "pfree2 = ?, potherwastereport = ?, rreport = ?, rmeasuremethods = ?, "
        + "rindustrialwatersupply = ?, rwatersupply = ?, rcompressedair15 = ?, rcompressedair90 = ?, "
        + "rthinner = ?, rammonia = ?, rnitricacid = ?, rcausticsoda = ?, rhydrochloricacid = ?, "
        + "racetylene = ?, rinorganicchemicalindustrialproducts = ?, rsulfuricacid = ?, "
        + "ranhydrouschromicacid = ?, rorganicchemicalindustrialproducts = ?, rcleaningagents = ?, "
        + "rcelluloseadhesives = ?, rlubricatingoil = ?, rfree1 = ?, rfree2 = ?, wreport = ?, "
        + "wmeasuremethods = ?, wash = ?, winorganicsludgemining = ?, worganicsludgemanufacturing = ?, "
        + "wwasteplasticsmanufacturing = ?, wmetalscrap = ?, wceramicscrap = ?, wslag = ?, "
        + "wdust = ?, wwasteoilfrompetroleum = ?, wnaturalfiberscrap = ?, wrubberscrap = ?, "
        + "wwasteacid = ?, wwastealkali = ?, wfree1 = ?, wfree2 = ?, tmaterialreport = ?, "
        + "tpartreport = ?, tmeasuremethods = ?, tweightmaterialinput = ?, "
        + "tweightmaterialemissions = ?, tweightparttotal = ?, tweightpartemissions = ?, "
        + "tfuelmaterialtype = ?, tfuelmaterialconsumption = ?, tfuelmaterialemissions = ?, "
        + "tfuelparttype = ?, tfuelpartconsumption = ?, tfuelpartemissions = ?, "
        + "tfueleconomymaterialtype = ?, tfueleconomymaterialmileage = ?, "
        + "tfueleconomymaterialfueleconomy = ?, tfueleconomymaterialemissions = ?, tfueleconomyparttype = ?, "
        + "tfueleconomypartmileage = ?, tfueleconomypartfueleconomy = ?, tfueleconomypartemissions = ?, "
        + "ttonkgmaterialtype = ?, ttonkgmaterialmileage = ?, ttonkgmaterialemissions = ?, "
        + "ttonkgparttype = ?, ttonkgpartmileage = ?, ttonkgpartemissions = ?, modifiedat = ? "
        + "WHERE operatorid = ? and cfpid = ?";

    Date nowDate = new Date();

    int result = jdbc.update(sql, model.getMReport(), model.getMMeasureMethods(), model.getMCountryCd(),
        model.getMateialPir(), model.getMPcRelv(), model.getMCrOtherIndustry(), model.getMUnclassifiable(),
        model.getMTotal(), model.getMYieldRate(), model.getMInputWeight(), model.getMBaseUnitEmissions(),
        model.getMDirectGhg(), model.getMEnergyRate(), model.getMElectricBaseUnit(), model.getMPowerConsumption(),
        model.getMElectricGhg(), model.getPReport(), model.getPMeasureMethods(), model.getPCountryCd(),
        model.getPManufacturingDivision(), model.getP1Cd(), model.getP2Cd(), model.getP3Cd(), model.getP4Cd(),
        model.getPEngyRate(), model.getPElectricBaseUnit(), model.getPElectricAmount(), model.getPCrudeOilA(),
        model.getPCrudeOilC(), model.getPKerosene(), model.getPDiesel(), model.getPGasoline(), model.getPNgl(),
        model.getPLpg(), model.getPLng(), model.getPCityGus(), model.getPFree1(), model.getPFree2(),
        model.getPOtherWasteReport(), model.getRReport(), model.getRMeasureMethods(), model.getRIndustrialWaterSupply(),
        model.getRWaterSupply(), model.getRCompressedAir15(), model.getRCompressedAir90(), model.getRThinner(),
        model.getRAmmonia(), model.getRNitricAcid(), model.getRCausticSoda(), model.getRHydrochloricAcid(),
        model.getRAcetylene(), model.getRInorganicChemicalIndustrialProducts(), model.getRSulfuricAcid(),
        model.getRAnhydrousChromicAcid(), model.getROrganicChemicalIndustrialProducts(), model.getRCleaningAgents(),
        model.getRCelluloseAdhesives(), model.getRLubricatingOil(), model.getRFree1(), model.getRFree2(),
        model.getWReport(), model.getWMeasureMethods(), model.getWAsh(), model.getWInorganicSludgeMining(),
        model.getWOrganicSludgeManufacturing(), model.getWWastePlasticsManufacturing(), model.getWMetalScrap(),
        model.getWCeramicScrap(), model.getWSlag(), model.getWDust(), model.getWWasteOilFromPetroleum(),
        model.getWNaturalFiberScrap(), model.getWRubberScrap(), model.getWWasteAcid(), model.getWWasteAlkali(),
        model.getWFree1(), model.getWFree2(), model.getTMaterialReport(), model.getTPartReport(),
        model.getTMeasureMethods(), model.getTWeightMaterialInput(), model.getTWeightMaterialEmissions(),
        model.getTWeightPartTotal(), model.getTWeightPartEmissions(), model.getTFuelMaterialType(),
        model.getTFuelMaterialConsumption(), model.getTFuelMaterialEmissions(), model.getTFuelPartType(),
        model.getTFuelPartConsumption(), model.getTFuelPartEmissions(), model.getTFuelEconomyMaterialType(),
        model.getTFuelEconomyMaterialMileage(), model.getTFuelEconomyMaterialFuelEconomy(),
        model.getTFuelEconomyMaterialEmissions(), model.getTFuelEconomyPartType(), model.getTFuelEconomyPartMileage(),
        model.getTFuelEconomyPartFuelEconomy(), model.getTFuelEconomyPartEmissions(), model.getTTonKgMaterialType(),
        model.getTTonKgMaterialMileage(), model.getTTonKgMaterialEmissions(), model.getTTonKgPartType(),
        model.getTTonKgPartMileage(), model.getTTonKgPartEmissions(), nowDate, model.getOperatorId(), model.getCfpId());
    return result;
  }

  @Override
  public int deleteByRowNo(String operatorId, String productTraceId, String rowNos) {

    String sql = "DELETE FROM public.lcacfpsimplecalc WHERE traceid IN ("
        + "SELECT traceid FROM public.lcapartsstructure WHERE operatorid = ? AND producttraceid = ? AND rowno IN ("
        + rowNos + "))";

    int result = jdbc.update(sql, operatorId, productTraceId);

    return result;
  }
}
