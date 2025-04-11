package net.ouranos_ecos.domain.repository.jdbc;

import java.math.BigDecimal;
import java.text.ParseException;
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
import net.ouranos_ecos.domain.model.LcaCfpModel;
import net.ouranos_ecos.domain.model.LcaModel;
import net.ouranos_ecos.domain.model.ProductLcaCfpModel;
import net.ouranos_ecos.domain.model.ProductModel;
import net.ouranos_ecos.domain.repository.LcaCfpRepository;

/**
 * LcaCfpRepository実装クラス。
 */
@Repository
public class LcaCfpJdbc implements LcaCfpRepository {

  @Autowired
  private JdbcTemplate jdbc;
  
  @Override
  public ProductLcaCfpModel select(String operatorId, String producttraceId)
      throws ParseException {

    ProductLcaCfpModel lcaCfpModel = new ProductLcaCfpModel();
    
    // 製品情報を取得
    String sql = "SELECT operatorid , producttraceid , productitem , supplyitemno"
        + " , supplyfuctory , fuctoryaddress , responceinfo, materialstotal, gco2eqtotal, cfpmodifiedat  "
        + "FROM public.product WHERE operatorid = ? AND producttraceid = ? AND deleteflag IS NOT True";
    
    Map<String, Object> resultMap = jdbc.queryForMap(sql, operatorId, producttraceId);

    ProductModel productModel = new ProductModel();
    productModel.setOperatorId((String) resultMap.get("operatorid"));
    productModel.setProductTraceId((String) resultMap.get("producttraceid"));
    productModel.setProductItem((String) resultMap.get("productitem"));
    productModel.setSupplyItemNo((String) resultMap.get("supplyitemno"));
    productModel.setSupplyFuctory((String) resultMap.get("supplyfuctory"));
    productModel.setFuctoryAddress((String) resultMap.get("fuctoryaddress"));
    productModel.setResponceInfo((String) resultMap.get("responceinfo"));
    productModel.setMaterialsTotal(ConverterUtil.toString((BigDecimal) resultMap.get("materialstotal")));
    productModel.setGco2eqTotal(ConverterUtil.toString((BigDecimal) resultMap.get("gco2eqtotal")));
    if ((Date) resultMap.get("cfpmodifiedat") != null) {
      SimpleDateFormat format = new SimpleDateFormat(Constants.YYYYMMDD_HHMMSS);
      productModel.setCfpModifieDat(format.format((Date) resultMap.get("cfpmodifiedat")));
    }
    lcaCfpModel.setProductModel(productModel);
    
    // LCA部品構成情報とLCACFP情報を結合して取得
    String sql2 = "SELECT l1.operatorid, "
        + "l1.traceid, "
        + "l1.partsname, "
        + "l1.partsLabelname, "
        + "l1.supportpartsname, "
        + "l1.partsstructurelevel, "
        + "l1.number, "
        + "l1.mass, "
        + "l1.totalmass, "
        + "l1.materialcd, "
        + "l1.materialstandard, "
        + "l1.materialcategory, "
        + "l1.lcamaterialcd, "
        + "l3.lcamaterialname, "
        + "l1.partsprocurementcd, "
        + "l1.materiaprocurementcd, "
        + "l1.endflag, "
        + "l1.bottomlayerflag, "
        + "l1.producttraceid, "
        + "l1.rowno, "
        + "l2.cfpid, "
        + "l2.mreport, "
        + "l2.mmeasuremethods, "
        + "l2.mcountrycd, "
        + "l2.mateialpir, "
        + "l2.mpcrelv, "
        + "l2.mcrotherindustry, "
        + "l2.mUnclassifiable, "
        + "l2.mtotal, "
        + "l2.myieldrate, "
        + "l2.minputweight, "
        + "l2.mbaseunitemissions, "
        + "l2.mdirectghg, "
        + "l2.mengyrate, "
        + "l2.melectricbaseunit, "
        + "l2.mpowerconsumption, "
        + "l2.melectricghg, "
        + "l2.preport, "
        + "l2.pmeasuremethods, "
        + "l2.pcountrycd, "
        + "l2.pmanufacturingdivision, "
        + "l2.p1cd, "
        + "l2.p2cd, "
        + "l2.p3cd, "
        + "l2.p4cd, "
        + "l2.pengyrate, "
        + "l2.pelectricbaseunit, "
        + "l2.pelectricamount, "
        + "l2.pcrudeoila, "
        + "l2.pcrudeoilc, "
        + "l2.pkerosene, "
        + "l2.pdiesel, "
        + "l2.pgasoline, "
        + "l2.pngl, "
        + "l2.plpg, "
        + "l2.plng, "
        + "l2.pcitygus, "
        + "l2.pfree1, "
        + "l2.pfree2, "
        + "l2.potherwastereport, "
        + "l2.rreport, "
        + "l2.rmeasuremethods, "
        + "l2.rindustrialwatersupply, "
        + "l2.rwatersupply, "
        + "l2.rcompressedair15, "
        + "l2.rcompressedair90, "
        + "l2.rthinner, "
        + "l2.rammonia, "
        + "l2.rnitricacid, "
        + "l2.rcausticsoda, "
        + "l2.rhydrochloricacid, "
        + "l2.racetylene, "
        + "l2.rinorganicchemicalindustrialproducts, "
        + "l2.rsulfuricacid, "
        + "l2.ranhydrouschromicacid, "
        + "l2.rorganicchemicalindustrialproducts, "
        + "l2.rcleaningagents, "
        + "l2.rcelluloseadhesives, "
        + "l2.rlubricatingoil, "
        + "l2.rfree1, "
        + "l2.rfree2, "
        + "l2.wreport, "
        + "l2.wmeasuremethods, "
        + "l2.wash, "
        + "l2.winorganicsludgemining, "
        + "l2.worganicsludgemanufacturing, "
        + "l2.wwasteplasticsmanufacturing, "
        + "l2.wmetalscrap, "
        + "l2.wceramicscrap, "
        + "l2.wslag, "
        + "l2.wdust, "
        + "l2.wwasteoilfrompetroleum, "
        + "l2.wnaturalfiberscrap, "
        + "l2.wrubberscrap, "
        + "l2.wwasteacid, "
        + "l2.wwastealkali, "
        + "l2.wfree1, "
        + "l2.wfree2, "
        + "l2.tmaterialreport, "
        + "l2.tpartreport, "
        + "l2.tmeasuremethods, "
        + "l2.tweightmaterialinput, "
        + "l2.tweightmaterialemissions, "
        + "l2.tweightparttotal, "
        + "l2.tweightpartemissions, "
        + "l2.tfuelmaterialtype, "
        + "l2.tfuelmaterialconsumption, "
        + "l2.tfuelmaterialemissions, "
        + "l2.tfuelparttype, "
        + "l2.tfuelpartconsumption, "
        + "l2.tfuelpartemissions, "
        + "l2.tfueleconomymaterialtype, "
        + "l2.tfueleconomymaterialmileage, "
        + "l2.tfueleconomymaterialfueleconomy, "
        + "l2.tfueleconomymaterialemissions, "
        + "l2.tfueleconomyparttype, "
        + "l2.tfueleconomypartmileage, "
        + "l2.tfueleconomypartfueleconomy, "
        + "l2.tfueleconomypartemissions, "
        + "l2.ttonkgmaterialtype, "
        + "l2.ttonkgmaterialmileage, "
        + "l2.ttonkgmaterialemissions, "
        + "l2.ttonkgparttype, "
        + "l2.ttonkgpartmileage, "
        + "l2.ttonkgpartemissions "
        + "FROM public.lcapartsstructure l1 "
        + "LEFT JOIN public.lcacfp l2 ON l1.traceid = l2.traceid  "
        + "AND l1.operatorid = l2.operatorid "
        + "LEFT JOIN public.lcamaterial l3 ON l1.lcamaterialcd = l3.lcamaterialcd "
        + "WHERE l1.operatorid = ? "
        + "AND l1.producttraceid = ? "
        + "ORDER BY "
        + "l1.rowno;";

    List<Map<String, Object>> resultList = jdbc.queryForList(sql2, operatorId, producttraceId);
    
    List<LcaModel> entityList = new ArrayList<LcaModel>();

    for (Map<String, Object> map : resultList) {

      LcaModel model = new LcaModel();
      model.setOperatorId((String) map.get("operatorid"));
      model.setTraceId((String) map.get("traceid"));
      model.setPartsName((String) map.get("partsname"));
      model.setPartsLabelName((String) map.get("partsLabelname"));
      model.setSupportPartsName((String) map.get("supportpartsname"));
      model.setPartsStructureLevel((Integer) map.get("partsstructurelevel"));
      model.setNumber(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("number")));
      model.setMass(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("mass")));
      model.setTotalMass(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("totalmass")));
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
      model.setMYieldRate(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("myieldrate")));
      model.setMInputWeight(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("minputweight")));
      model.setMBaseUnitEmissions(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("mbaseunitemissions")));
      model.setMDirectGhg(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("mdirectghg")));
      model.setMEnergyRate((Integer) map.get("mengyrate"));
      model.setMElectricBaseUnit(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("melectricbaseunit")));
      model.setMPowerConsumption(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("mpowerconsumption")));
      model.setMElectricGhg(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("melectricghg")));
      model.setPReport((String) map.get("preport"));
      model.setPMeasureMethods((String) map.get("pmeasuremethods"));
      model.setPCountryCd((String) map.get("pcountrycd"));
      model.setPManufacturingDivision((String) map.get("pmanufacturingdivision"));
      model.setP1Cd((String) map.get("p1cd"));
      model.setP2Cd((String) map.get("p2cd"));
      model.setP3Cd((String) map.get("p3cd"));
      model.setP4Cd((String) map.get("p4cd"));
      model.setPEngyRate((Integer) map.get("pengyrate"));
      model.setPElectricBaseUnit(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("pelectricbaseunit")));
      model.setPElectricAmount(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("pelectricamount")));
      model.setPCrudeOilA(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("pcrudeoila")));
      model.setPCrudeOilC(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("pcrudeoilc")));
      model.setPKerosene(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("pkerosene")));
      model.setPDiesel(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("pdiesel")));
      model.setPGasoline(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("pgasoline")));
      model.setPNgl(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("pngl")));
      model.setPLpg(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("plpg")));
      model.setPLng(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("plng")));
      model.setPCityGus(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("pcitygus")));
      model.setPFree1(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("pfree1")));
      model.setPFree2(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("pfree2")));
      model.setPOtherWasteReport(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("potherwastereport")));
      model.setRReport((String) map.get("rreport"));
      model.setRMeasureMethods((String) map.get("rmeasuremethods"));
      model.setRIndustrialWaterSupply(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("rindustrialwatersupply")));
      model.setRWaterSupply(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("rwatersupply")));
      model.setRCompressedAir15(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("rcompressedair15")));
      model.setRCompressedAir90(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("rcompressedair90")));
      model.setRThinner(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("rthinner")));
      model.setRAmmonia(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("rammonia")));
      model.setRNitricAcid(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("rnitricacid")));
      model.setRCausticSoda(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("rcausticsoda")));
      model.setRHydrochloricAcid(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("rhydrochloricacid")));
      model.setRAcetylene(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("racetylene")));
      model.setRInorganicChemicalIndustrialProducts(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("rinorganicchemicalindustrialproducts")));
      model.setRSulfuricAcid(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("rsulfuricacid")));
      model.setRAnhydrousChromicAcid(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("ranhydrouschromicacid")));
      model.setROrganicChemicalIndustrialProducts(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("rorganicchemicalindustrialproducts")));
      model.setRCleaningAgents(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("rcleaningagents")));
      model.setRCelluloseAdhesives(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("rcelluloseadhesives")));
      model.setRLubricatingOil(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("rlubricatingoil")));
      model.setRFree1(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("rfree1")));
      model.setRFree2(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("rfree2")));
      model.setWReport((String) map.get("wreport"));
      model.setWMeasureMethods((String) map.get("wmeasuremethods"));
      model.setWAsh(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("wash")));
      model.setWInorganicSludgeMining(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("winorganicsludgemining")));
      model.setWOrganicSludgeManufacturing(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("worganicsludgemanufacturing")));
      model.setWWastePlasticsManufacturing(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("wwasteplasticsmanufacturing")));
      model.setWMetalScrap(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("wmetalscrap")));
      model.setWCeramicScrap(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("wceramicscrap")));
      model.setWSlag(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("wslag")));
      model.setWDust(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("wdust")));
      model.setWWasteOilFromPetroleum(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("wwasteoilfrompetroleum")));
      model.setWNaturalFiberScrap(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("wnaturalfiberscrap")));
      model.setWRubberScrap(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("wrubberscrap")));
      model.setWWasteAcid(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("wwasteacid")));
      model.setWWasteAlkali(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("wwastealkali")));
      model.setWFree1(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("wfree1")));
      model.setWFree2(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("wfree2")));
      model.setTMaterialReport((String) map.get("tmaterialreport"));
      model.setTPartReport((String) map.get("tpartreport"));
      model.setTMeasureMethods((String) map.get("tmeasuremethods"));
      model.setTWeightMaterialInput(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("tweightmaterialinput")));
      model.setTWeightMaterialEmissions(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("tweightmaterialemissions")));
      model.setTWeightPartTotal(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("tweightparttotal")));
      model.setTWeightPartEmissions(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("tweightpartemissions")));
      model.setTFuelMaterialType((String) map.get("tfuelmaterialtype"));
      model.setTFuelMaterialConsumption(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("tfuelmaterialconsumption")));
      model.setTFuelMaterialEmissions(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("tfuelmaterialemissions")));
      model.setTFuelPartType((String) map.get("tfuelparttype"));
      model.setTFuelPartConsumption(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("tfuelpartconsumption")));
      model.setTFuelPartEmissions(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("tfuelpartemissions")));
      model.setTFuelEconomyMaterialType((String) map.get("tfueleconomymaterialtype"));
      model.setTFuelEconomyMaterialMileage(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("tfueleconomymaterialmileage")));
      model.setTFuelEconomyMaterialFuelEconomy(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("tfueleconomymaterialfueleconomy")));
      model.setTFuelEconomyMaterialEmissions(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("tfueleconomymaterialemissions")));
      model.setTFuelEconomyPartType((String) map.get("tfueleconomyparttype"));
      model.setTFuelEconomyPartMileage(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("tfueleconomypartmileage")));
      model.setTFuelEconomyPartFuelEconomy(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("tfueleconomypartfueleconomy")));
      model.setTFuelEconomyPartEmissions(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("tfueleconomypartemissions")));
      model.setTTonKgMaterialType((String) map.get("ttonkgmaterialtype"));
      model.setTTonKgMaterialMileage(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("ttonkgmaterialmileage")));
      model.setTTonKgMaterialEmissions(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("ttonkgmaterialemissions")));
      model.setTTonKgPartType((String) map.get("ttonkgparttype"));
      model.setTTonKgPartMileage(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("ttonkgpartmileage")));
      model.setTTonKgPartEmissions(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) map.get("ttonkgpartemissions")));

      entityList.add(model);
    }
    lcaCfpModel.setLcaModel(entityList);
    
    return lcaCfpModel;

  }

  @Override
  public int insert(LcaCfpModel model) {
    
    String sql = "INSERT INTO public.lcacfp("
        + "operatorid, "
        + "cfpid, "
        + "traceid, "
        + "mreport, "
        + "mmeasuremethods, "
        + "mcountrycd, "
        + "mateialpir, "
        + "mpcrelv, "
        + "mcrotherindustry, "
        + "munclassifiable, "
        + "mtotal, "
        + "myieldrate, "
        + "minputweight, "
        + "mbaseunitemissions, "
        + "mdirectghg, "
        + "mengyrate, "
        + "melectricbaseunit, "
        + "mpowerconsumption, "
        + "melectricghg, "
        + "preport, "
        + "pmeasuremethods, "
        + "pcountrycd, "
        + "pmanufacturingdivision, "
        + "p1cd, "
        + "p2cd, "
        + "p3cd, "
        + "p4cd, "
        + "pengyrate, "
        + "pelectricbaseunit, "
        + "pelectricamount, "
        + "pcrudeoila, "
        + "pcrudeoilc, "
        + "pkerosene, "
        + "pdiesel, "
        + "pgasoline, "
        + "pngl, "
        + "plpg, "
        + "plng, "
        + "pcitygus, "
        + "pfree1, "
        + "pfree2, "
        + "potherwastereport, "
        + "rreport, "
        + "rmeasuremethods, "
        + "rindustrialwatersupply, "
        + "rwatersupply, "
        + "rcompressedair15, "
        + "rcompressedair90, "
        + "rthinner, "
        + "rammonia, "
        + "rnitricacid, "
        + "rcausticsoda, "
        + "rhydrochloricacid, "
        + "racetylene, "
        + "rinorganicchemicalindustrialproducts, "
        + "rsulfuricacid, "
        + "ranhydrouschromicacid, "
        + "rorganicchemicalindustrialproducts, "
        + "rcleaningagents, "
        + "rcelluloseadhesives, "
        + "rlubricatingoil, "
        + "rfree1, "
        + "rfree2, "
        + "wreport, "
        + "wmeasuremethods, "
        + "wash, "
        + "winorganicsludgemining, "
        + "worganicsludgemanufacturing, "
        + "wwasteplasticsmanufacturing, "
        + "wmetalscrap, "
        + "wceramicscrap, "
        + "wslag, "
        + "wdust, "
        + "wwasteoilfrompetroleum, "
        + "wnaturalfiberscrap, "
        + "wrubberscrap, "
        + "wwasteacid, "
        + "wwastealkali, "
        + "wfree1, "
        + "wfree2, "
        + "tmaterialreport, "
        + "tpartreport, "
        + "tmeasuremethods, "
        + "tweightmaterialinput, "
        + "tweightmaterialemissions, "
        + "tweightparttotal, "
        + "tweightpartemissions, "
        + "tfuelmaterialtype, "
        + "tfuelmaterialconsumption, "
        + "tfuelmaterialemissions, "
        + "tfuelparttype, "
        + "tfuelpartconsumption, "
        + "tfuelpartemissions, "
        + "tfueleconomymaterialtype, "
        + "tfueleconomymaterialmileage, "
        + "tfueleconomymaterialfueleconomy, "
        + "tfueleconomymaterialemissions, "
        + "tfueleconomyparttype, "
        + "tfueleconomypartmileage, "
        + "tfueleconomypartfueleconomy, "
        + "tfueleconomypartemissions, "
        + "ttonkgmaterialtype, "
        + "ttonkgmaterialmileage, "
        + "ttonkgmaterialemissions, "
        + "ttonkgparttype, "
        + "ttonkgpartmileage, "
        + "ttonkgpartemissions, "
        + "createdat, "
        + "modifiedat, "
        + "deleteflag) "
        + "VALUES ("
        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
        + ")";

    Date nowDate = new Date();

    int result = jdbc.update(sql,
        model.getOperatorId(),
        model.getCfpId(),
        model.getTraceId(),
        model.getMReport(),
        model.getMMeasureMethods(),
        model.getMCountryCd(),
        model.getMateialPir(),
        model.getMPcRelv(),
        model.getMCrOtherIndustry(),
        model.getMUnclassifiable(),
        model.getMTotal(),
        model.getMYieldRate(),
        model.getMInputWeight(),
        model.getMBaseUnitEmissions(),
        model.getMDirectGhg(),
        model.getMEnergyRate(),
        model.getMElectricBaseUnit(),
        model.getMPowerConsumption(),
        model.getMElectricGhg(),
        model.getPReport(),
        model.getPMeasureMethods(),
        model.getPCountryCd(),
        model.getPManufacturingDivision(),
        model.getP1Cd(),
        model.getP2Cd(),
        model.getP3Cd(),
        model.getP4Cd(),
        model.getPEngyRate(),
        model.getPElectricBaseUnit(),
        model.getPElectricAmount(),
        model.getPCrudeOilA(),
        model.getPCrudeOilC(),
        model.getPKerosene(),
        model.getPDiesel(),
        model.getPGasoline(),
        model.getPNgl(),
        model.getPLpg(),
        model.getPLng(),
        model.getPCityGus(),
        model.getPFree1(),
        model.getPFree2(),
        model.getPOtherWasteReport(),
        model.getRReport(),
        model.getRMeasureMethods(),
        model.getRIndustrialWaterSupply(),
        model.getRWaterSupply(),
        model.getRCompressedAir15(),
        model.getRCompressedAir90(),
        model.getRThinner(),
        model.getRAmmonia(),
        model.getRNitricAcid(),
        model.getRCausticSoda(),
        model.getRHydrochloricAcid(),
        model.getRAcetylene(),
        model.getRInorganicChemicalIndustrialProducts(),
        model.getRSulfuricAcid(),
        model.getRAnhydrousChromicAcid(),
        model.getROrganicChemicalIndustrialProducts(),
        model.getRCleaningAgents(),
        model.getRCelluloseAdhesives(),
        model.getRLubricatingOil(),
        model.getRFree1(),
        model.getRFree2(),
        model.getWReport(),
        model.getWMeasureMethods(),
        model.getWAsh(),
        model.getWInorganicSludgeMining(),
        model.getWOrganicSludgeManufacturing(),
        model.getWWastePlasticsManufacturing(),
        model.getWMetalScrap(),
        model.getWCeramicScrap(),
        model.getWSlag(),
        model.getWDust(),
        model.getWWasteOilFromPetroleum(),
        model.getWNaturalFiberScrap(),
        model.getWRubberScrap(),
        model.getWWasteAcid(),
        model.getWWasteAlkali(),
        model.getWFree1(),
        model.getWFree2(),
        model.getTMaterialReport(),
        model.getTPartReport(),
        model.getTMeasureMethods(),
        model.getTWeightMaterialInput(),
        model.getTWeightMaterialEmissions(),
        model.getTWeightPartTotal(),
        model.getTWeightPartEmissions(),
        model.getTFuelMaterialType(),
        model.getTFuelMaterialConsumption(),
        model.getTFuelMaterialEmissions(),
        model.getTFuelPartType(),
        model.getTFuelPartConsumption(),
        model.getTFuelPartEmissions(),
        model.getTFuelEconomyMaterialType(),
        model.getTFuelEconomyMaterialMileage(),
        model.getTFuelEconomyMaterialFuelEconomy(),
        model.getTFuelEconomyMaterialEmissions(),
        model.getTFuelEconomyPartType(),
        model.getTFuelEconomyPartMileage(),
        model.getTFuelEconomyPartFuelEconomy(),
        model.getTFuelEconomyPartEmissions(),
        model.getTTonKgMaterialType(),
        model.getTTonKgMaterialMileage(),
        model.getTTonKgMaterialEmissions(),
        model.getTTonKgPartType(),
        model.getTTonKgPartMileage(),
        model.getTTonKgPartEmissions(),
        nowDate,
        nowDate,
        false);
    return result;
    
  }

  @Override
  public int[] insertLcaCfp(List<LcaCfpModel> lcaPSModelList) throws Exception {

    Date nowDate = new Date();

    List<Object[]> batchArgs = lcaPSModelList.stream()
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
            model.getTFuelEconomyMaterialEmissions(), model.getTFuelEconomyPartType(),
            model.getTFuelEconomyPartMileage(), model.getTFuelEconomyPartFuelEconomy(),
            model.getTFuelEconomyPartEmissions(), model.getTTonKgMaterialType(), model.getTTonKgMaterialMileage(),
            model.getTTonKgMaterialEmissions(), model.getTTonKgPartType(), model.getTTonKgPartMileage(),
            model.getTTonKgPartEmissions(), nowDate, nowDate, false})
        .collect(Collectors.toList());

    String sql = "INSERT INTO public.lcacfp(operatorid, cfpid, traceid, mreport, "
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
  public int update(LcaCfpModel model) {
    
    String sql = "UPDATE public.lcacfp SET "
        + "mreport = ?, "
        + "mmeasuremethods = ?, "
        + "mcountrycd = ?, "
        + "mateialpir = ?, "
        + "mpcrelv = ?, "
        + "mcrotherindustry = ?, "
        + "munclassifiable = ?, "
        + "mtotal = ?, "
        + "myieldrate = ?, "
        + "minputweight = ?, "
        + "mbaseunitemissions = ?, "
        + "mdirectghg = ?, "
        + "mengyrate = ?, "
        + "melectricbaseunit = ?, "
        + "mpowerconsumption = ?, "
        + "melectricghg = ?, "
        + "preport = ?, "
        + "pmeasuremethods = ?, "
        + "pcountrycd = ?, "
        + "pmanufacturingdivision = ?, "
        + "p1cd = ?, "
        + "p2cd = ?, "
        + "p3cd = ?, "
        + "p4cd = ?, "
        + "pengyrate = ?, "
        + "pelectricbaseunit = ?, "
        + "pelectricamount = ?, "
        + "pcrudeoila = ?, "
        + "pcrudeoilc = ?, "
        + "pkerosene = ?, "
        + "pdiesel = ?, "
        + "pgasoline = ?, "
        + "pngl = ?, "
        + "plpg = ?, "
        + "plng = ?, "
        + "pcitygus = ?, "
        + "pfree1 = ?, "
        + "pfree2 = ?, "
        + "potherwastereport = ?, "
        + "rreport = ?, "
        + "rmeasuremethods = ?, "
        + "rindustrialwatersupply = ?, "
        + "rwatersupply = ?, "
        + "rcompressedair15 = ?, "
        + "rcompressedair90 = ?, "
        + "rthinner = ?, "
        + "rammonia = ?, "
        + "rnitricacid = ?, "
        + "rcausticsoda = ?, "
        + "rhydrochloricacid = ?, "
        + "racetylene = ?, "
        + "rinorganicchemicalindustrialproducts = ?, "
        + "rsulfuricacid = ?, "
        + "ranhydrouschromicacid = ?, "
        + "rorganicchemicalindustrialproducts = ?, "
        + "rcleaningagents = ?, "
        + "rcelluloseadhesives = ?, "
        + "rlubricatingoil = ?, "
        + "rfree1 = ?, "
        + "rfree2 = ?, "
        + "wreport = ?, "
        + "wmeasuremethods = ?, "
        + "wash = ?, "
        + "winorganicsludgemining = ?, "
        + "worganicsludgemanufacturing = ?, "
        + "wwasteplasticsmanufacturing = ?, "
        + "wmetalscrap = ?, "
        + "wceramicscrap = ?, "
        + "wslag = ?, "
        + "wdust = ?, "
        + "wwasteoilfrompetroleum = ?, "
        + "wnaturalfiberscrap = ?, "
        + "wrubberscrap = ?, "
        + "wwasteacid = ?, "
        + "wwastealkali = ?, "
        + "wfree1 = ?, "
        + "wfree2 = ?, "
        + "tmaterialreport = ?, "
        + "tpartreport = ?, "
        + "tmeasuremethods = ?, "
        + "tweightmaterialinput = ?, "
        + "tweightmaterialemissions = ?, "
        + "tweightparttotal = ?, "
        + "tweightpartemissions = ?, "
        + "tfuelmaterialtype = ?, "
        + "tfuelmaterialconsumption = ?, "
        + "tfuelmaterialemissions = ?, "
        + "tfuelparttype = ?, "
        + "tfuelpartconsumption = ?, "
        + "tfuelpartemissions = ?, "
        + "tfueleconomymaterialtype = ?, "
        + "tfueleconomymaterialmileage = ?, "
        + "tfueleconomymaterialfueleconomy = ?, "
        + "tfueleconomymaterialemissions = ?, "
        + "tfueleconomyparttype = ?, "
        + "tfueleconomypartmileage = ?, "
        + "tfueleconomypartfueleconomy = ?, "
        + "tfueleconomypartemissions = ?, "
        + "ttonkgmaterialtype = ?, "
        + "ttonkgmaterialmileage = ?, "
        + "ttonkgmaterialemissions = ?, "
        + "ttonkgparttype = ?, "
        + "ttonkgpartmileage = ?, "
        + "ttonkgpartemissions = ?, "
        + "modifiedat = ? "
        + "WHERE operatorid = ? "
        + "and cfpid = ?";

    Date nowDate = new Date();

    int result = jdbc.update(sql,
        model.getMReport(),
        model.getMMeasureMethods(),
        model.getMCountryCd(),
        model.getMateialPir(),
        model.getMPcRelv(),
        model.getMCrOtherIndustry(),
        model.getMUnclassifiable(),
        model.getMTotal(),
        model.getMYieldRate(),
        model.getMInputWeight(),
        model.getMBaseUnitEmissions(),
        model.getMDirectGhg(),
        model.getMEnergyRate(),
        model.getMElectricBaseUnit(),
        model.getMPowerConsumption(),
        model.getMElectricGhg(),
        model.getPReport(),
        model.getPMeasureMethods(),
        model.getPCountryCd(),
        model.getPManufacturingDivision(),
        model.getP1Cd(),
        model.getP2Cd(),
        model.getP3Cd(),
        model.getP4Cd(),
        model.getPEngyRate(),
        model.getPElectricBaseUnit(),
        model.getPElectricAmount(),
        model.getPCrudeOilA(),
        model.getPCrudeOilC(),
        model.getPKerosene(),
        model.getPDiesel(),
        model.getPGasoline(),
        model.getPNgl(),
        model.getPLpg(),
        model.getPLng(),
        model.getPCityGus(),
        model.getPFree1(),
        model.getPFree2(),
        model.getPOtherWasteReport(),
        model.getRReport(),
        model.getRMeasureMethods(),
        model.getRIndustrialWaterSupply(),
        model.getRWaterSupply(),
        model.getRCompressedAir15(),
        model.getRCompressedAir90(),
        model.getRThinner(),
        model.getRAmmonia(),
        model.getRNitricAcid(),
        model.getRCausticSoda(),
        model.getRHydrochloricAcid(),
        model.getRAcetylene(),
        model.getRInorganicChemicalIndustrialProducts(),
        model.getRSulfuricAcid(),
        model.getRAnhydrousChromicAcid(),
        model.getROrganicChemicalIndustrialProducts(),
        model.getRCleaningAgents(),
        model.getRCelluloseAdhesives(),
        model.getRLubricatingOil(),
        model.getRFree1(),
        model.getRFree2(),
        model.getWReport(),
        model.getWMeasureMethods(),
        model.getWAsh(),
        model.getWInorganicSludgeMining(),
        model.getWOrganicSludgeManufacturing(),
        model.getWWastePlasticsManufacturing(),
        model.getWMetalScrap(),
        model.getWCeramicScrap(),
        model.getWSlag(),
        model.getWDust(),
        model.getWWasteOilFromPetroleum(),
        model.getWNaturalFiberScrap(),
        model.getWRubberScrap(),
        model.getWWasteAcid(),
        model.getWWasteAlkali(),
        model.getWFree1(),
        model.getWFree2(),
        model.getTMaterialReport(),
        model.getTPartReport(),
        model.getTMeasureMethods(),
        model.getTWeightMaterialInput(),
        model.getTWeightMaterialEmissions(),
        model.getTWeightPartTotal(),
        model.getTWeightPartEmissions(),
        model.getTFuelMaterialType(),
        model.getTFuelMaterialConsumption(),
        model.getTFuelMaterialEmissions(),
        model.getTFuelPartType(),
        model.getTFuelPartConsumption(),
        model.getTFuelPartEmissions(),
        model.getTFuelEconomyMaterialType(),
        model.getTFuelEconomyMaterialMileage(),
        model.getTFuelEconomyMaterialFuelEconomy(),
        model.getTFuelEconomyMaterialEmissions(),
        model.getTFuelEconomyPartType(),
        model.getTFuelEconomyPartMileage(),
        model.getTFuelEconomyPartFuelEconomy(),
        model.getTFuelEconomyPartEmissions(),
        model.getTTonKgMaterialType(),
        model.getTTonKgMaterialMileage(),
        model.getTTonKgMaterialEmissions(),
        model.getTTonKgPartType(),
        model.getTTonKgPartMileage(),
        model.getTTonKgPartEmissions(),
        nowDate,
        model.getOperatorId(),
        model.getCfpId()
        );
    return result;
  }
  
  @Override
  public int[] updateLcaCfp(List<LcaCfpModel> lcaCfpModelList) throws Exception {

    Date nowDate = new Date();

    List<Object[]> batchArgs = lcaCfpModelList.stream().map(model -> new Object[]{model.getMReport(),
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
        model.getTTonKgPartMileage(), model.getTTonKgPartEmissions(), nowDate, model.getOperatorId(), model.getCfpId()})
        .collect(Collectors.toList());

    String sql = "UPDATE public.lcacfp SET mreport = ?, mmeasuremethods = ?, mcountrycd = ?, "
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
  public int deleteByRowNo(String operatorId, String productTraceId, String rowNos) {

    String sql = "DELETE FROM public.lcacfp WHERE traceid IN ("
        + "SELECT traceid FROM public.lcapartsstructure WHERE operatorid = ? AND producttraceid = ? AND rowno IN ("
        + rowNos + "))";

    int result = jdbc.update(sql, operatorId, productTraceId);

    return result;
  }
  
  @Override
  public LcaCfpModel selectByTraceId(String operatorId, String traceId) {

    String sql = "SELECT operatorid, cfpid, traceid, mreport, mmeasuremethods, mcountrycd, mateialpir, "
        + "mpcrelv, mcrotherindustry, munclassifiable, mtotal, myieldrate, minputweight, "
        + "mbaseunitemissions, mdirectghg, mengyrate, melectricbaseunit, mpowerconsumption, "
        + "melectricghg, preport, pmeasuremethods, pcountrycd, pmanufacturingdivision, p1cd, p2cd, "
        + "p3cd, p4cd, pengyrate, pelectricbaseunit, pelectricamount, pcrudeoila, pcrudeoilc, pkerosene, "
        + "pdiesel, pgasoline, pngl, plpg, plng, pcitygus, pfree1, pfree2, potherwastereport, rreport, "
        + "rmeasuremethods, rindustrialwatersupply, rwatersupply, rcompressedair15, rcompressedair90, "
        + "rthinner, rammonia, rnitricacid, rcausticsoda, rhydrochloricacid, racetylene, "
        + "rinorganicchemicalindustrialproducts, rsulfuricacid, ranhydrouschromicacid, "
        + "rorganicchemicalindustrialproducts, rcleaningagents, rcelluloseadhesives, rlubricatingoil, "
        + "rfree1, rfree2, wreport, wmeasuremethods, wash, winorganicsludgemining, "
        + "worganicsludgemanufacturing, wwasteplasticsmanufacturing, wmetalscrap, wceramicscrap, "
        + "wslag, wdust, wwasteoilfrompetroleum, wnaturalfiberscrap , wrubberscrap, wwasteacid, "
        + "wwastealkali, wfree1, wfree2, tmaterialreport, tpartreport, tmeasuremethods, tweightmaterialinput, "
        + "tweightmaterialemissions, tweightparttotal, tweightpartemissions, tfuelmaterialtype, "
        + "tfuelmaterialconsumption, tfuelmaterialemissions, tfuelparttype, tfuelpartconsumption, "
        + "tfuelpartemissions, tfueleconomymaterialtype, tfueleconomymaterialmileage, "
        + "tfueleconomymaterialfueleconomy, tfueleconomymaterialemissions, tfueleconomyparttype, "
        + "tfueleconomypartmileage, tfueleconomypartfueleconomy, tfueleconomypartemissions, "
        + "ttonkgmaterialtype, ttonkgmaterialmileage, ttonkgmaterialemissions, ttonkgparttype, "
        + "ttonkgpartmileage, ttonkgpartemissions "
        + "FROM public.lcacfp "
        + "WHERE operatorid = ? AND traceid = ? ";

    Map<String, Object> map = jdbc.queryForMap(sql, operatorId.trim(), traceId);

    LcaCfpModel model = new LcaCfpModel();

    model.setOperatorId((String) map.get("operatorid"));
    model.setCfpId((String) map.get("cfpid"));
    model.setTraceId((String) map.get("traceid"));
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

    return model;

  }
}
