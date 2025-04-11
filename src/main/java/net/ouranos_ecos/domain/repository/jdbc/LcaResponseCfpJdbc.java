package net.ouranos_ecos.domain.repository.jdbc;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.ouranos_ecos.domain.common.Constants;
import net.ouranos_ecos.domain.common.ConverterUtil;
import net.ouranos_ecos.domain.model.CfpResponseModel;
import net.ouranos_ecos.domain.model.LcaModel;
import net.ouranos_ecos.domain.model.LcaResponseModel;
import net.ouranos_ecos.domain.model.ResponseProductLcaCfpModel;
import net.ouranos_ecos.domain.model.ResponseProductModel;
import net.ouranos_ecos.domain.repository.LcaResponseCfpRepository;

/**
 * LcaResponseCfpRepository実装クラス。
 */
@Repository
public class LcaResponseCfpJdbc implements LcaResponseCfpRepository {

  @Autowired
  private JdbcTemplate jdbc;
  
  
  @Override
  public ResponseProductLcaCfpModel select(String operatorId, String producttraceId, String responseId) {

    ResponseProductLcaCfpModel responseCfpModel = new ResponseProductLcaCfpModel();

    // 製品情報を取得
    String sql = "SELECT rp.operatorid, rp.responseid, rp.producttraceid, rp.productitem, rp.supplyitemno, "
        + "rp.supplyfuctory, rp.fuctoryaddress, rp.responceinfo, rp.materialstotal, rp.gco2eqtotal, rp.cfpmodifiedat, req.acceptedflag "
        + "FROM public.responseproduct rp "
        + "LEFT JOIN public.response res "
        + "ON rp.operatorid = res.operatorid AND rp.responseid = res.responseid "
        + "LEFT JOIN public .request req "
        + "ON res.operatorid = req.requestedtooperatorid AND res.requestid = req.requestid "
        + "WHERE rp.operatorid = ? AND rp.responseid = ? AND rp.producttraceid = ? AND rp.deleteflag IS NOT True;";

    Map<String, Object> resultMap = jdbc.queryForMap(sql, operatorId, responseId, producttraceId);

    ResponseProductModel productModel = new ResponseProductModel();
    productModel.setOperatorId((String) resultMap.get("operatorid"));
    productModel.setResponseId((String) resultMap.get("responseId"));
    productModel.setProductTraceId((String) resultMap.get("producttraceid"));
    productModel.setProductItem((String) resultMap.get("productitem"));
    productModel.setSupplyItemNo((String) resultMap.get("supplyitemno"));
    productModel.setSupplyFuctory((String) resultMap.get("supplyfuctory"));
    productModel.setFuctoryAddress((String) resultMap.get("fuctoryaddress"));
    productModel.setResponceInfo((String) resultMap.get("responceinfo"));
    productModel.setAcceptedFlag((boolean) resultMap.get("acceptedflag"));
    productModel.setMaterialsTotal(ConverterUtil.toString((BigDecimal) resultMap.get("materialstotal")));
    productModel.setGco2eqTotal(ConverterUtil.toString((BigDecimal) resultMap.get("gco2eqtotal")));
    if ((Date) resultMap.get("cfpmodifiedat") != null) {
      SimpleDateFormat format = new SimpleDateFormat(Constants.YYYYMMDD_HHMMSS);
      productModel.setCfpModifieDat(format.format((Date) resultMap.get("cfpmodifiedat")));
    }
    responseCfpModel.setResponseProductModel(productModel);

    // LCA部品構成情報とLCACFP情報を結合して取得
    String sql2 = "SELECT l1.operatorid, l1.responseid, l1.traceid, l1.partsname, l1.partsLabelname, "
        + "l1.supportpartsname, l1.partsstructurelevel, l1.number, l1.mass, l1.totalmass, "
        + "l1.materialcd, l1.materialstandard, l1.materialcategory, l1.lcamaterialcd, "
        + "l3.lcamaterialname, l1.partsprocurementcd, l1.materiaprocurementcd, l1.endflag, "
        + "l1.bottomlayerflag, l1.producttraceid, l1.rowno, l2.cfpid, l2.mreport, "
        + "l2.mmeasuremethods, l2.mcountrycd, l2.mateialpir, l2.mpcrelv, l2.mcrotherindustry, "
        + "l2.mUnclassifiable, l2.mtotal, l2.myieldrate, l2.minputweight, l2.mbaseunitemissions, "
        + "l2.mdirectghg, l2.mengyrate, l2.melectricbaseunit, l2.mpowerconsumption, "
        + "l2.melectricghg, l2.preport, l2.pmeasuremethods, l2.pcountrycd, "
        + "l2.pmanufacturingdivision, l2.p1cd, l2.p2cd, l2.p3cd, l2.p4cd, l2.pengyrate, "
        + "l2.pelectricbaseunit, l2.pelectricamount, l2.pcrudeoila, l2.pcrudeoilc, l2.pkerosene, "
        + "l2.pdiesel, l2.pgasoline, l2.pngl, l2.plpg, l2.plng, l2.pcitygus, l2.pfree1, "
        + "l2.pfree2, l2.potherwastereport, l2.rreport, l2.rmeasuremethods, "
        + "l2.rindustrialwatersupply, l2.rwatersupply, l2.rcompressedair15, l2.rcompressedair90, "
        + "l2.rthinner, l2.rammonia, l2.rnitricacid, l2.rcausticsoda, l2.rhydrochloricacid, "
        + "l2.racetylene, l2.rinorganicchemicalindustrialproducts, l2.rsulfuricacid, "
        + "l2.ranhydrouschromicacid, l2.rorganicchemicalindustrialproducts, l2.rcleaningagents, "
        + "l2.rcelluloseadhesives, l2.rlubricatingoil, l2.rfree1, l2.rfree2, l2.wreport, "
        + "l2.wmeasuremethods, l2.wash, l2.winorganicsludgemining, l2.worganicsludgemanufacturing, "
        + "l2.wwasteplasticsmanufacturing, l2.wmetalscrap, l2.wceramicscrap, l2.wslag, l2.wdust, "
        + "l2.wwasteoilfrompetroleum, l2.wnaturalfiberscrap, l2.wrubberscrap, l2.wwasteacid, "
        + "l2.wwastealkali, l2.wfree1, l2.wfree2, l2.tmaterialreport, l2.tpartreport, "
        + "l2.tmeasuremethods, l2.tweightmaterialinput, l2.tweightmaterialemissions, "
        + "l2.tweightparttotal, l2.tweightpartemissions, l2.tfuelmaterialtype, "
        + "l2.tfuelmaterialconsumption, l2.tfuelmaterialemissions, l2.tfuelparttype, "
        + "l2.tfuelpartconsumption, l2.tfuelpartemissions, l2.tfueleconomymaterialtype, "
        + "l2.tfueleconomymaterialmileage, l2.tfueleconomymaterialfueleconomy, "
        + "l2.tfueleconomymaterialemissions, l2.tfueleconomyparttype, l2.tfueleconomypartmileage, "
        + "l2.tfueleconomypartfueleconomy, l2.tfueleconomypartemissions, l2.ttonkgmaterialtype, "
        + "l2.ttonkgmaterialmileage, l2.ttonkgmaterialemissions, l2.ttonkgparttype, "
        + "l2.ttonkgpartmileage, l2.ttonkgpartemissions "
        + "FROM public.lcaresponsepartsstructure l1 "
        + "LEFT JOIN "
        + "public.lcaresponsecfp l2 ON l1.traceid = l2.traceid  AND l1.operatorid = l2.operatorid AND l1.responseid = l2.responseid "
        + "LEFT JOIN "
        + "public.lcamaterial l3 ON l1.lcamaterialcd = l3.lcamaterialcd "
        + "WHERE l1.operatorid = ? AND l1.producttraceid = ? AND l1.responseid = ? ORDER BY l1.rowno;";

    List<Map<String, Object>> resultList = jdbc.queryForList(sql2, operatorId, producttraceId, responseId);

    List<LcaResponseModel> entityList = new ArrayList<LcaResponseModel>();

    for (Map<String, Object> map : resultList) {

      LcaResponseModel model = new LcaResponseModel();
      model.setOperatorId((String) map.get("operatorid"));
      model.setResponseId((String) map.get("responseid"));
      model.setTraceId((String) map.get("traceid"));
      model.setPartsName((String) map.get("partsname"));
      model.setPartsLabelName((String) map.get("partsLabelname"));
      model.setSupportPartsName((String) map.get("supportpartsname"));
      model.setPartsStructureLevel((Integer) map.get("partsstructurelevel"));
      model.setNumber((BigDecimal) map.get("number"));
      model.setMass((BigDecimal) map.get("mass"));
      model.setTotalMass((BigDecimal) map.get("totalmass"));
      model.setMaterialCd((String) map.get("materialcd"));
      model.setMaterialStandard((String) map.get("materialstandard"));
      model.setMaterialCategory((String) map.get("materialcategory"));
      model.setLcaMaterialCd((String) map.get("lcamaterialcd"));
      model.setLcaMaterialName((String) map.get("lcamaterialname"));
      model.setPartsProcurementCd((String) map.get("partsprocurementcd"));
      model.setMateriaProcurementCd((String) map.get("materiaprocurementcd"));
      model.setEndFlag((boolean) map.get("endflag"));
      model.setBottomLayerFlag((boolean) map.get("bottomlayerflag"));
      model.setProductTraceId((String) map.get("producttraceid"));
      model.setRowNo((Integer) map.get("rowno"));
      model.setCfpId((String) map.get("cfpid"));
      model.setMReport((String) map.get("mreport"));
      model.setMMeasureMethods((String) map.get("mmeasuremethods"));
      model.setMCountryCd((String) map.get("mcountrycd"));
      model.setMateialPir((Integer) map.get("mateialpir"));
      model.setMPcRelv((Integer) map.get("mpcrelv"));
      model.setMCrOtherIndustry((Integer) map.get("mcrotherindustry"));
      model.setMUnclassifiable((Integer) map.get("mUnclassifiable"));
      model.setMTotal((Integer) map.get("mtotal"));
      model.setMYieldRate((BigDecimal) map.get("myieldrate"));
      model.setMInputWeight((BigDecimal) map.get("minputweight"));
      model.setMBaseUnitEmissions((BigDecimal) map.get("mbaseunitemissions"));
      model.setMDirectGhg((BigDecimal) map.get("mdirectghg"));
      model.setMEnergyRate((Integer) map.get("mengyrate"));
      model.setMElectricBaseUnit((BigDecimal) map.get("melectricbaseunit"));
      model.setMPowerConsumption((BigDecimal) map.get("mpowerconsumption"));
      model.setMElectricGhg((BigDecimal) map.get("melectricghg"));
      model.setPReport((String) map.get("preport"));
      model.setPMeasureMethods((String) map.get("pmeasuremethods"));
      model.setPCountryCd((String) map.get("pcountrycd"));
      model.setPManufacturingDivision((String) map.get("pmanufacturingdivision"));
      model.setP1Cd((String) map.get("p1cd"));
      model.setP2Cd((String) map.get("p2cd"));
      model.setP3Cd((String) map.get("p3cd"));
      model.setP4Cd((String) map.get("p4cd"));
      model.setPEngyRate((Integer) map.get("pengyrate"));
      model.setPElectricBaseUnit((BigDecimal) map.get("pelectricbaseunit"));
      model.setPElectricAmount((BigDecimal) map.get("pelectricamount"));
      model.setPCrudeOilA((BigDecimal) map.get("pcrudeoila"));
      model.setPCrudeOilC((BigDecimal) map.get("pcrudeoilc"));
      model.setPKerosene((BigDecimal) map.get("pkerosene"));
      model.setPDiesel((BigDecimal) map.get("pdiesel"));
      model.setPGasoline((BigDecimal) map.get("pgasoline"));
      model.setPNgl((BigDecimal) map.get("pngl"));
      model.setPLpg((BigDecimal) map.get("plpg"));
      model.setPLng((BigDecimal) map.get("plng"));
      model.setPCityGus((BigDecimal) map.get("pcitygus"));
      model.setPFree1((BigDecimal) map.get("pfree1"));
      model.setPFree2((BigDecimal) map.get("pfree2"));
      model.setPOtherWasteReport((BigDecimal) map.get("potherwastereport"));
      model.setRReport((String) map.get("rreport"));
      model.setRMeasureMethods((String) map.get("rmeasuremethods"));
      model.setRIndustrialWaterSupply((BigDecimal) map.get("rindustrialwatersupply"));
      model.setRWaterSupply((BigDecimal) map.get("rwatersupply"));
      model.setRCompressedAir15((BigDecimal) map.get("rcompressedair15"));
      model.setRCompressedAir90((BigDecimal) map.get("rcompressedair90"));
      model.setRThinner((BigDecimal) map.get("rthinner"));
      model.setRAmmonia((BigDecimal) map.get("rammonia"));
      model.setRNitricAcid((BigDecimal) map.get("rnitricacid"));
      model.setRCausticSoda((BigDecimal) map.get("rcausticsoda"));
      model.setRHydrochloricAcid((BigDecimal) map.get("rhydrochloricacid"));
      model.setRAcetylene((BigDecimal) map.get("racetylene"));
      model.setRInorganicChemicalIndustrialProducts((BigDecimal) map.get("rinorganicchemicalindustrialproducts"));
      model.setRSulfuricAcid((BigDecimal) map.get("rsulfuricacid"));
      model.setRAnhydrousChromicAcid((BigDecimal) map.get("ranhydrouschromicacid"));
      model.setROrganicChemicalIndustrialProducts((BigDecimal) map.get("rorganicchemicalindustrialproducts"));
      model.setRCleaningAgents((BigDecimal) map.get("rcleaningagents"));
      model.setRCelluloseAdhesives((BigDecimal) map.get("rcelluloseadhesives"));
      model.setRLubricatingOil((BigDecimal) map.get("rlubricatingoil"));
      model.setRFree1((BigDecimal) map.get("rfree1"));
      model.setRFree2((BigDecimal) map.get("rfree2"));
      model.setWReport((String) map.get("wreport"));
      model.setWMeasureMethods((String) map.get("wmeasuremethods"));
      model.setWAsh((BigDecimal) map.get("wash"));
      model.setWInorganicSludgeMining((BigDecimal) map.get("winorganicsludgemining"));
      model.setWOrganicSludgeManufacturing((BigDecimal) map.get("worganicsludgemanufacturing"));
      model.setWWastePlasticsManufacturing((BigDecimal) map.get("wwasteplasticsmanufacturing"));
      model.setWMetalScrap((BigDecimal) map.get("wmetalscrap"));
      model.setWCeramicScrap((BigDecimal) map.get("wceramicscrap"));
      model.setWSlag((BigDecimal) map.get("wslag"));
      model.setWDust((BigDecimal) map.get("wdust"));
      model.setWWasteOilFromPetroleum((BigDecimal) map.get("wwasteoilfrompetroleum"));
      model.setWNaturalFiberScrap((BigDecimal) map.get("wnaturalfiberscrap"));
      model.setWRubberScrap((BigDecimal) map.get("wrubberscrap"));
      model.setWWasteAcid((BigDecimal) map.get("wwasteacid"));
      model.setWWasteAlkali((BigDecimal) map.get("wwastealkali"));
      model.setWFree1((BigDecimal) map.get("wfree1"));
      model.setWFree2((BigDecimal) map.get("wfree2"));
      model.setTMaterialReport((String) map.get("tmaterialreport"));
      model.setTPartReport((String) map.get("tpartreport"));
      model.setTMeasureMethods((String) map.get("tmeasuremethods"));
      model.setTWeightMaterialInput((BigDecimal) map.get("tweightmaterialinput"));
      model.setTWeightMaterialEmissions((BigDecimal) map.get("tweightmaterialemissions"));
      model.setTWeightPartTotal((BigDecimal) map.get("tweightparttotal"));
      model.setTWeightPartEmissions((BigDecimal) map.get("tweightpartemissions"));
      model.setTFuelMaterialType((String) map.get("tfuelmaterialtype"));
      model.setTFuelMaterialConsumption((BigDecimal) map.get("tfuelmaterialconsumption"));
      model.setTFuelMaterialEmissions((BigDecimal) map.get("tfuelmaterialemissions"));
      model.setTFuelPartType((String) map.get("tfuelparttype"));
      model.setTFuelPartConsumption((BigDecimal) map.get("tfuelpartconsumption"));
      model.setTFuelPartEmissions((BigDecimal) map.get("tfuelpartemissions"));
      model.setTFuelEconomyMaterialType((String) map.get("tfueleconomymaterialtype"));
      model.setTFuelEconomyMaterialMileage((BigDecimal) map.get("tfueleconomymaterialmileage"));
      model.setTFuelEconomyMaterialFuelEconomy((BigDecimal) map.get("tfueleconomymaterialfueleconomy"));
      model.setTFuelEconomyMaterialEmissions((BigDecimal) map.get("tfueleconomymaterialemissions"));
      model.setTFuelEconomyPartType((String) map.get("tfueleconomyparttype"));
      model.setTFuelEconomyPartMileage((BigDecimal) map.get("tfueleconomypartmileage"));
      model.setTFuelEconomyPartFuelEconomy((BigDecimal) map.get("tfueleconomypartfueleconomy"));
      model.setTFuelEconomyPartEmissions((BigDecimal) map.get("tfueleconomypartemissions"));
      model.setTTonKgMaterialType((String) map.get("ttonkgmaterialtype"));
      model.setTTonKgMaterialMileage((BigDecimal) map.get("ttonkgmaterialmileage"));
      model.setTTonKgMaterialEmissions((BigDecimal) map.get("ttonkgmaterialemissions"));
      model.setTTonKgPartType((String) map.get("ttonkgparttype"));
      model.setTTonKgPartMileage((BigDecimal) map.get("ttonkgpartmileage"));
      model.setTTonKgPartEmissions((BigDecimal) map.get("ttonkgpartemissions"));

      entityList.add(model);
    }
    responseCfpModel.setLcaResponseModel(entityList);

    return responseCfpModel;

  }
  
  @Override
  public int[] insertCfpResponse(CfpResponseModel cfpResponseModel, String responseid, List<LcaModel> lcaModelList)
      throws Exception {

    Date nowDate = new Date();

    List<Object[]> batchArgs = lcaModelList.stream()
        .map(model -> new Object[]{cfpResponseModel.getOperatorId(), responseid, model.getCfpId(), model.getTraceId(),
            model.getMReport(), model.getMMeasureMethods(), model.getMCountryCd(), model.getMateialPir(),
            model.getMPcRelv(), model.getMCrOtherIndustry(), model.getMUnclassifiable(), model.getMTotal(),
            model.getMYieldRate(), model.getMInputWeight(), model.getMBaseUnitEmissions(), model.getMDirectGhg(),
            model.getMEnergyRate(), model.getMElectricBaseUnit(), model.getMPowerConsumption(), model.getMElectricGhg(),
            model.getPReport(), model.getPMeasureMethods(), model.getPCountryCd(), model.getPManufacturingDivision(),
            model.getP1Cd(), model.getP2Cd(), model.getP3Cd(), model.getP4Cd(), model.getPEngyRate(),
            model.getPElectricBaseUnit(), model.getPElectricAmount(), model.getPCrudeOilA(), model.getPCrudeOilC(),
            model.getPKerosene(), model.getPDiesel(), model.getPGasoline(), model.getPNgl(), model.getPLpg(),
            model.getPLng(), model.getPCityGus(), model.getPFree1(), model.getPFree2(), model.getPOtherWasteReport(),
            model.getRReport(), model.getRMeasureMethods(), model.getRIndustrialWaterSupply(), model.getRWaterSupply(),
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
            model.getTFuelEconomyMaterialEmissions(), model.getTFuelEconomyPartType(),
            model.getTFuelEconomyPartMileage(), model.getTFuelEconomyPartFuelEconomy(),
            model.getTFuelEconomyPartEmissions(), model.getTTonKgMaterialType(), model.getTTonKgMaterialMileage(),
            model.getTTonKgMaterialEmissions(), model.getTTonKgPartType(), model.getTTonKgPartMileage(),
            model.getTTonKgPartEmissions(), nowDate, nowDate, false})
        .collect(Collectors.toList());

    String sql = "INSERT INTO public.lcaresponsecfp(operatorid, responseid, cfpid, traceid, mreport, mmeasuremethods, mcountrycd, "
        + "mateialpir, mpcrelv, mcrotherindustry, munclassifiable, mtotal, myieldrate, minputweight, mbaseunitemissions, mdirectghg, "
        + "mengyrate, melectricbaseunit, mpowerconsumption, melectricghg, preport, pmeasuremethods, pcountrycd, pmanufacturingdivision, "
        + "p1cd, p2cd, p3cd, p4cd, pengyrate, pelectricbaseunit, pelectricamount, pcrudeoila, pcrudeoilc, pkerosene, pdiesel, pgasoline, pngl, "
        + "plpg, plng, pcitygus, pfree1, pfree2, potherwastereport, rreport, rmeasuremethods, rindustrialwatersupply, rwatersupply, "
        + "rcompressedair15, rcompressedair90, rthinner, rammonia, rnitricacid, rcausticsoda, rhydrochloricacid, racetylene, "
        + "rinorganicchemicalindustrialproducts, rsulfuricacid, ranhydrouschromicacid, rorganicchemicalindustrialproducts, rcleaningagents, "
        + "rcelluloseadhesives, rlubricatingoil, rfree1, rfree2, wreport, wmeasuremethods, wash, winorganicsludgemining, "
        + "worganicsludgemanufacturing, wwasteplasticsmanufacturing, wmetalscrap, wceramicscrap, wslag, wdust, wwasteoilfrompetroleum, "
        + "wnaturalfiberscrap, wrubberscrap, wwasteacid, wwastealkali, wfree1, wfree2, tmaterialreport, tpartreport, tmeasuremethods, "
        + "tweightmaterialinput, tweightmaterialemissions, tweightparttotal, tweightpartemissions, tfuelmaterialtype, tfuelmaterialconsumption, "
        + "tfuelmaterialemissions, tfuelparttype, tfuelpartconsumption, tfuelpartemissions, tfueleconomymaterialtype, "
        + "tfueleconomymaterialmileage, tfueleconomymaterialfueleconomy, tfueleconomymaterialemissions, tfueleconomyparttype, "
        + "tfueleconomypartmileage, tfueleconomypartfueleconomy, tfueleconomypartemissions, ttonkgmaterialtype, ttonkgmaterialmileage, "
        + "ttonkgmaterialemissions, ttonkgparttype, ttonkgpartmileage, ttonkgpartemissions, createdat, modifiedat, deleteflag) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
        + "?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
}
