package net.ouranos_ecos.domain.repository.jdbc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.ouranos_ecos.domain.common.CalcUtil;
import net.ouranos_ecos.domain.common.Constants;
import net.ouranos_ecos.domain.common.ConverterUtil;
import net.ouranos_ecos.domain.entity.CsvCfpInfoData;
import net.ouranos_ecos.domain.entity.CsvProductData;
import net.ouranos_ecos.domain.entity.LcaCfpResultCsvData;
import net.ouranos_ecos.domain.model.LcaPartsStructureModel;
import net.ouranos_ecos.domain.repository.LcaPartsStructureRepository;

/**
 * LcaPartsStructureRepository実装クラス。
 */
@Repository
public class LcaPartsStructureJdbc implements LcaPartsStructureRepository {

  @Autowired
  private JdbcTemplate jdbc;

  @Override
  public int insert(LcaPartsStructureModel childParts) {

    Date nowDate = new Date();

    int result = jdbc.update("INSERT INTO public.lcapartsstructure(operatorid, traceid, partsname"
        + ", partslabelname, supportpartsname, partsstructurelevel, number, mass, totalmass"
        + ", materialcd, materialstandard, materialcategory, lcamaterialcd, partsprocurementcd"
        + ", materiaprocurementcd, endflag, bottomlayerflag, producttraceid, rowno, createdat"
        + ", modifiedat, deleteflag, requesttargetflag)VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
        childParts.getOperatorId(), childParts.getTraceId(), childParts.getPartsName(), childParts.getPartsLabelName(),
        childParts.getSupportPartsName(), childParts.getPartsStructureLevel(), childParts.getNumber(),
        childParts.getMass(), childParts.getTotalMass(), childParts.getMaterialCd(), childParts.getMaterialStandard(),
        childParts.getMaterialCategory(), childParts.getLcaMaterialCd(), childParts.getPartsProcurementCd(),
        childParts.getMateriaProcurementCd(), false, false, childParts.getProductTraceId(), childParts.getRowNo(),
        nowDate, nowDate, false, childParts.getRequestTargetFlag());

    return result;
  }

  @Override
  public int[] insertLcaPartsStructure(List<LcaPartsStructureModel> lcaModelList) throws Exception {

    Date nowDate = new Date();

    List<Object[]> batchArgs = lcaModelList.stream()
        .map(model -> new Object[]{model.getOperatorId(), model.getTraceId(), model.getPartsName(),
            model.getPartsLabelName(), model.getSupportPartsName(), model.getPartsStructureLevel(), model.getNumber(),
            model.getMass(), model.getTotalMass(), model.getMaterialCd(), model.getMaterialStandard(),
            model.getMaterialCategory(), model.getLcaMaterialCd(), model.getPartsProcurementCd(),
            model.getMateriaProcurementCd(), false, false, model.getProductTraceId(),
            model.getRowNo(), nowDate, nowDate, false, model.getRequestTargetFlag()})
        .collect(Collectors.toList());

    String sql = "INSERT INTO public.lcapartsstructure(operatorid, traceid, partsname, partslabelname, supportpartsname, partsstructurelevel, number"
        + ", mass, totalmass, materialcd, materialstandard, materialcategory, lcamaterialcd, partsprocurementcd, materiaprocurementcd, endflag"
        + ", bottomlayerflag, producttraceid, rowno, createdat, modifiedat, deleteflag, requesttargetflag)"
        + "VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

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
  public LcaPartsStructureModel select(String operatorid, String traceid) {

    String sql = "SELECT operatorid, traceid, partsname, partslabelname, supportpartsname, partsstructurelevel, "
        + "number, mass, totalmass, materialcd, materialstandard, materialcategory, lcamaterialcd, partsprocurementcd, "
        + "materiaprocurementcd, endflag, bottomlayerflag, producttraceid, rowno, requesttargetflag "
        + "FROM public.lcapartsstructure WHERE operatorid = ? AND traceid = ? AND deleteflag IS NOT True";

    Map<String, Object> resultMap = jdbc.queryForMap(sql, operatorid, traceid);

    LcaPartsStructureModel model = new LcaPartsStructureModel();
    model.setOperatorId((String) resultMap.get("operatorid"));
    model.setTraceId((String) resultMap.get("traceid"));
    model.setPartsName((String) resultMap.get("partsname"));
    model.setPartsLabelName((String) resultMap.get("partslabelname"));
    model.setSupportPartsName((String) resultMap.get("supportpartsname"));
    model.setPartsStructureLevel((Integer) resultMap.get("partsstructurelevel"));
    model.setNumber((BigDecimal) resultMap.get("number"));
    model.setMass((BigDecimal) resultMap.get("mass"));
    model.setTotalMass((BigDecimal) resultMap.get("totalmass"));
    model.setMaterialCd((String) resultMap.get("materialcd"));
    model.setMaterialStandard((String) resultMap.get("materialstandard"));
    model.setMaterialCategory((String) resultMap.get("materialcategory"));
    model.setLcaMaterialCd((String) resultMap.get("lcamaterialcd"));
    model.setPartsProcurementCd((String) resultMap.get("partsprocurementcd"));
    model.setMateriaProcurementCd((String) resultMap.get("materiaprocurementcd"));
    model.setEndFlag((boolean) resultMap.get("endflag"));
    model.setBottomLayerFlag((boolean) resultMap.get("bottomlayerflag"));
    model.setProductTraceId((String) resultMap.get("producttraceid"));
    model.setRowNo((Integer) resultMap.get("rowno"));
    if (resultMap.get("requesttargetflag") != null) {
      model.setRequestTargetFlag((boolean) resultMap.get("requesttargetflag"));
    }
    return model;

  }
  
  @Override
  public List<LcaPartsStructureModel> selectByProductPrimaryKey(String operatorId, String productTraceId) {

    List<LcaPartsStructureModel> resultList = new ArrayList<LcaPartsStructureModel>();
    
    String sql = "SELECT t1.operatorid, t1.traceid, t1.partsname, t1.partslabelname, "
        + "COALESCE(t1.supportpartsname, '') as supportpartsname, "
        + "t1.partsstructurelevel,t1.number, t1.mass, "
        + "t1.totalmass, t1.materialcd, t1.materialstandard, t1.materialcategory, "
        + "t1.lcamaterialcd, t1.partsprocurementcd,t1.materiaprocurementcd,"
        + "t1.endflag, t1.bottomlayerflag, t1.producttraceid, t1.rowno, t1.requesttargetflag,"
        + "CASE WHEN t2.requestid IS NULL THEN false ELSE true END AS requestFlag "
        + "FROM public.lcapartsstructure t1 LEFT JOIN public.request t2 ON t1.traceid = t2.requestedfromtraceid "
        + "WHERE t1.operatorid = ? AND  t1.producttraceid = ? AND t1.deleteflag IS NOT True ORDER BY rowno";

    List<Map<String, Object>> list = jdbc.queryForList(sql, operatorId, productTraceId);

    for (Map<String, Object> resultMap : list) {
      LcaPartsStructureModel model = new LcaPartsStructureModel();
      model.setOperatorId((String) resultMap.get("operatorid"));
      model.setTraceId((String) resultMap.get("traceid"));
      model.setPartsName((String) resultMap.get("partsname"));
      model.setPartsLabelName((String) resultMap.get("partslabelname"));
      model.setSupportPartsName((String) resultMap.get("supportpartsname"));
      model.setPartsStructureLevel((Integer) resultMap.get("partsstructurelevel"));
      model.setNumber(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) resultMap.get("number")));
      model.setMass(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) resultMap.get("mass")));
      model.setTotalMass(ConverterUtil.roundUpToSixDecimalPlaces((BigDecimal) resultMap.get("totalmass")));
      model.setMaterialCd((String) resultMap.get("materialcd"));
      model.setMaterialStandard((String) resultMap.get("materialstandard"));
      model.setMaterialCategory((String) resultMap.get("materialcategory"));
      String lcamaterialcd = (String) resultMap.get("lcamaterialcd");
      if (!Objects.isNull(lcamaterialcd)) {
        lcamaterialcd = lcamaterialcd.trim();
      }
      model.setLcaMaterialCd(lcamaterialcd);
      model.setPartsProcurementCd((String) resultMap.get("partsprocurementcd"));
      model.setMateriaProcurementCd((String) resultMap.get("materiaprocurementcd"));
      model.setEndFlag((boolean) resultMap.get("endflag"));
      model.setBottomLayerFlag((boolean) resultMap.get("bottomlayerflag"));
      model.setProductTraceId((String) resultMap.get("producttraceid"));
      model.setRowNo((Integer) resultMap.get("rowno"));
      if (resultMap.get("requesttargetflag") != null) {
        model.setRequestTargetFlag((boolean) resultMap.get("requesttargetflag"));
      }
      model.setRequestFlag((boolean) resultMap.get("requestFlag"));

      resultList.add(model);
    }
    return resultList;

  }
  
  @Override
  public int deleteByRowNo(String operatorId, String productTraceId, String rowNos) {

    String sql = "DELETE FROM public.lcapartsstructure WHERE operatorid = ? AND producttraceid = ? AND rowno IN ("
        + rowNos + ")";

    int result = jdbc.update(sql, operatorId, productTraceId);

    return result;
  }
  
  @Override
  public int updateMatchingRowNo(LcaPartsStructureModel model) {

    String sql = "UPDATE public.lcapartsstructure "
        + "SET partsname = ? , partslabelname = ?, supportpartsname = ?, "
        + "partsstructurelevel = ?, number = ?, mass = ?, totalmass = ?, "
        + "materialcd = ?, materialstandard = ?, materialcategory = ?, "
        + "lcamaterialcd = ?, partsprocurementcd = ?, materiaprocurementcd = ?, "
        + "modifiedat = ?, requesttargetflag = ? "
        + "WHERE operatorid = ? AND traceid = ? AND rowno = ?";

    int result = jdbc.update(sql, 
        model.getPartsName(),
        model.getPartsLabelName(),
        model.getSupportPartsName(),
        model.getPartsStructureLevel(),
        model.getNumber(),
        model.getMass(),
        model.getTotalMass(),
        model.getMaterialCd(),
        model.getMaterialStandard(),
        model.getMaterialCategory(),
        model.getLcaMaterialCd(),
        model.getPartsProcurementCd(),
        model.getMateriaProcurementCd(),
        new Date(),
        model.getRequestTargetFlag(),
        model.getOperatorId(),
        model.getTraceId(),
        model.getRowNo());

    return result;
  }
  
  @Override
  public int[] updateCfpRowNo(List<LcaPartsStructureModel> modelList)
      throws Exception {

    List<Object[]> batchArgs = modelList.stream()
        .map(model -> new Object[]{model.getRowNo(), new Date(), model.getOperatorId(), model.getTraceId()})
        .collect(Collectors.toList());

    String sql = "UPDATE public.lcapartsstructure SET rowno = ? , modifiedat = ? WHERE operatorid = ? AND traceid = ?";

    // まとめて更新
    int[] results = jdbc.batchUpdate(sql, batchArgs);
    
    for (int value : results) {
      if (value == 0) {
        //更新出来ない行がある場合エラーとする
        throw new Exception();
      }
    }
    return results;
  }

  @Override
  public LcaCfpResultCsvData selectCfpCsv(String operatorId, String producttraceId, String responseId, String dlFlg) {

    LcaCfpResultCsvData csvData = new LcaCfpResultCsvData();

    // 製品情報を取得
    Map<String, Object> resultMap = new HashMap<String, Object>();
    StringBuilder sql = new StringBuilder()
        .append("SELECT op.openoperatorid, pr.producttraceid, pr.productitem, pr.supplyitemno, "
            + "pr.supplyfuctory, pr.fuctoryaddress, pr.responceinfo ");

    if (responseId != null) {
      // 回答画面からの遷移の場合、回答製品情報を取得
      sql.append("FROM public.responseproduct pr LEFT JOIN operator op ON pr.operatorid = op.operatorid "
          + "WHERE pr.operatorid = ? AND pr.responseid = ? AND pr.producttraceid = ? AND pr.deleteflag IS NOT True");

      resultMap = jdbc.queryForMap(sql.toString(), operatorId, responseId, producttraceId);
    } else {
      // 最新計算・簡易計算の場合、製品情報を取得
      sql.append("FROM public.product pr LEFT JOIN operator op ON pr.operatorid = op.operatorid "
          + "WHERE pr.operatorid = ? AND pr.producttraceid = ? AND pr.deleteflag IS NOT True");

      resultMap = jdbc.queryForMap(sql.toString(), operatorId, producttraceId);
    }

    CsvProductData csvProductData = new CsvProductData();
    csvProductData.setOpenOperatorId((String) resultMap.get("openoperatorid"));
    csvProductData.setProductTraceId((String) resultMap.get("producttraceid"));
    csvProductData.setProductItem((String) resultMap.get("productitem"));
    csvProductData.setSupplyItemNo((String) resultMap.get("supplyitemno"));
    csvProductData.setSupplyFuctory((String) resultMap.get("supplyfuctory"));
    csvProductData.setFuctoryAddress((String) resultMap.get("fuctoryaddress"));
    csvProductData.setResponceInfo((String) resultMap.get("responceinfo"));
    csvData.setCsvProductData(csvProductData);

    // 部品構成情報とCFP情報を結合して取得
    List<Map<String, Object>> resultList = new ArrayList<>();
    StringBuilder sql2 = new StringBuilder().append("SELECT l1.partsname, l1.partsLabelname, "
        + "l1.supportpartsname, l1.partsstructurelevel, l1.number, l1.mass, l1.totalmass, "
        + "l1.materialcd, l1.materialstandard, l1.materialcategory, l1.lcamaterialcd, l3.lcamaterialname, "
        + "l1.partsprocurementcd, l1.materiaprocurementcd, l1.producttraceid, "
        + "l2.cfpid, l2.mreport, l2.mmeasuremethods, mc.productioncountryname AS mproductioncountryname, "
        + "l2.mateialpir, l2.mpcrelv, l2.mcrotherindustry, l2.mUnclassifiable, l2.mtotal, l2.myieldrate, "
        + "l2.minputweight, l2.mbaseunitemissions, l2.mdirectghg, l2.mengyrate, l2.melectricbaseunit, "
        + "l2.mpowerconsumption, l2.melectricghg, l2.preport, l2.pmeasuremethods, "
        + "pc.productioncountryname AS pproductioncountryname, l2.pmanufacturingdivision, "
        + "p1.processingstepname AS p1processingstepname, p2.processingstepname AS p2processingstepname, "
        + "p3.processingstepname AS p3processingstepname, p4.processingstepname AS p4processingstepname, "
        + "l2.pengyrate, l2.pelectricbaseunit, l2.pelectricamount, l2.pcrudeoila, l2.pcrudeoilc, l2.pkerosene, "
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
        + "l2.tfueleconomymaterialmileage, l2.tfueleconomymaterialfueleconomy,  l2.tfueleconomymaterialemissions, "
        + "l2.tfueleconomyparttype, l2.tfueleconomypartmileage, l2.tfueleconomypartfueleconomy, "
        + "l2.tfueleconomypartemissions, l2.ttonkgmaterialtype, l2.ttonkgmaterialmileage, "
        + "l2.ttonkgmaterialemissions, l2.ttonkgparttype, l2.ttonkgpartmileage, l2.ttonkgpartemissions ");

    if (responseId != null) {
      // 回答一覧画面からの遷移の場合、LCA回答部品構成情報とLCA回答CFP情報を結合して取得
      sql2.append("FROM public.lcaresponsepartsstructure l1 "
          + "LEFT JOIN public.lcaresponsecfp l2 ON l1.traceid = l2.traceid AND l1.operatorid = l2.operatorid AND l1.responseid = l2.responseid "
          + "LEFT JOIN public.lcamaterial l3 ON l1.lcamaterialcd = l3.lcamaterialcd "
          + "LEFT JOIN public.productioncountry mc ON l2.mcountrycd = mc.productioncountrycd "
          + "LEFT JOIN public.productioncountry pc ON l2.pcountrycd = pc.productioncountrycd "
          + "LEFT JOIN public.processingstep p1 ON l2.p1cd = p1.processingstepcd "
          + "LEFT JOIN public.processingstep p2 ON l2.p2cd = p2.processingstepcd "
          + "LEFT JOIN public.processingstep p3 ON l2.p3cd = p3.processingstepcd "
          + "LEFT JOIN public.processingstep p4 ON l2.p4cd = p4.processingstepcd "
          + "WHERE l1.operatorid = ? AND l1.producttraceid = ? AND l1.responseid = ? ORDER BY l1.rowno;");

      resultList = jdbc.queryForList(sql2.toString(), operatorId, producttraceId, responseId);

    } else if (dlFlg.equals("jisoku")) {
      // 最新計算の場合、部品構成情報とLCACFP情報を結合して取得
      sql2.append("FROM public.lcapartsstructure l1 "
          + "LEFT JOIN public.lcacfp l2 ON l1.traceid = l2.traceid AND l1.operatorid = l2.operatorid "
          + "LEFT JOIN public.lcamaterial l3 ON l1.lcamaterialcd = l3.lcamaterialcd "
          + "LEFT JOIN public.productioncountry mc ON l2.mcountrycd = mc.productioncountrycd "
          + "LEFT JOIN public.productioncountry pc ON l2.pcountrycd = pc.productioncountrycd "
          + "LEFT JOIN public.processingstep p1 ON l2.p1cd = p1.processingstepcd "
          + "LEFT JOIN public.processingstep p2 ON l2.p2cd = p2.processingstepcd "
          + "LEFT JOIN public.processingstep p3 ON l2.p3cd = p3.processingstepcd "
          + "LEFT JOIN public.processingstep p4 ON l2.p4cd = p4.processingstepcd "
          + "WHERE l1.operatorid = ? AND l1.producttraceid = ? ORDER BY l1.rowno;");

      resultList = jdbc.queryForList(sql2.toString(), operatorId, producttraceId);

    } else if (dlFlg.equals("kani")) {
      // 簡易計算の場合、部品構成情報とLCACFP簡易計算情報を結合して取得
      sql2.append("FROM public.lcapartsstructure l1 "
          + "LEFT JOIN public.lcacfpsimplecalc l2 ON l1.traceid = l2.traceid AND l1.operatorid = l2.operatorid "
          + "LEFT JOIN public.lcamaterial l3 ON l1.lcamaterialcd = l3.lcamaterialcd "
          + "LEFT JOIN public.productioncountry mc ON l2.mcountrycd = mc.productioncountrycd "
          + "LEFT JOIN public.productioncountry pc ON l2.pcountrycd = pc.productioncountrycd "
          + "LEFT JOIN public.processingstep p1 ON l2.p1cd = p1.processingstepcd "
          + "LEFT JOIN public.processingstep p2 ON l2.p2cd = p2.processingstepcd "
          + "LEFT JOIN public.processingstep p3 ON l2.p3cd = p3.processingstepcd "
          + "LEFT JOIN public.processingstep p4 ON l2.p4cd = p4.processingstepcd "
          + "WHERE l1.operatorid = ? AND l1.producttraceid = ? ORDER BY l1.rowno;");

      resultList = jdbc.queryForList(sql2.toString(), operatorId, producttraceId);
    }

    List<CsvCfpInfoData> csvCfpInfoDataList = new ArrayList<CsvCfpInfoData>();

    for (Map<String, Object> map : resultList) {

      CsvCfpInfoData model = new CsvCfpInfoData();
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
      // 注釈は仮でセット
      model.setNotes("");
      model.setPartsProcurementCd(
          Optional.ofNullable(map.get("partsprocurementcd")).map(Constants.PROCURREMENT::get).orElse(null));
      model.setMateriaProcurementCd(
          Optional.ofNullable(map.get("materiaprocurementcd")).map(Constants.PROCURREMENT::get).orElse(null));
      model.setProductTraceId((String) map.get("producttraceid"));
      model.setCfpId((String) map.get("cfpid"));
      model.setTraceId((String) map.get("traceid"));
      model.setMReport((String) map.get("mreport"));
      model.setMMeasureMethods((String) map.get("mmeasuremethods"));
      model.setMProductionCountryName((String) map.get("mproductioncountryname"));
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
      model.setPProductionCountryName((String) map.get("pproductioncountryname"));
      model.setPManufacturingDivision((String) map.get("pmanufacturingdivision"));
      model.setP1ProcessingStepName((String) map.get("p1processingstepname"));
      model.setP2ProcessingStepName((String) map.get("p2processingstepname"));
      model.setP3ProcessingStepName((String) map.get("p3processingstepname"));
      model.setP4ProcessingStepName((String) map.get("p4processingstepname"));
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
      model.setTFuelMaterialType(
          Optional.ofNullable(map.get("tfuelmaterialtype")).map(Constants.FUEL_TYPE::get).orElse(null));
      model.setTFuelMaterialConsumption((BigDecimal) map.get("tfuelmaterialconsumption"));
      model.setTFuelMaterialEmissions((BigDecimal) map.get("tfuelmaterialemissions"));
      model.setTFuelPartType(Optional.ofNullable(map.get("tfuelparttype")).map(Constants.FUEL_TYPE::get).orElse(null));
      model.setTFuelPartConsumption((BigDecimal) map.get("tfuelpartconsumption"));
      model.setTFuelPartEmissions((BigDecimal) map.get("tfuelpartemissions"));
      model.setTFuelEconomyMaterialType(
          Optional.ofNullable(map.get("tfueleconomymaterialtype")).map(Constants.FUEL_TYPE::get).orElse(null));
      model.setTFuelEconomyMaterialMileage((BigDecimal) map.get("tfueleconomymaterialmileage"));
      model.setTFuelEconomyMaterialFuelEconomy((BigDecimal) map.get("tfueleconomymaterialfueleconomy"));
      model.setTFuelEconomyMaterialEmissions((BigDecimal) map.get("tfueleconomymaterialemissions"));
      model.setTFuelEconomyPartType(
          Optional.ofNullable(map.get("tfueleconomyparttype")).map(Constants.FUEL_TYPE::get).orElse(null));
      model.setTFuelEconomyPartMileage((BigDecimal) map.get("tfueleconomypartmileage"));
      model.setTFuelEconomyPartFuelEconomy((BigDecimal) map.get("tfueleconomypartfueleconomy"));
      model.setTFuelEconomyPartEmissions((BigDecimal) map.get("tfueleconomypartemissions"));
      model.setTTonKgMaterialType(
          Optional.ofNullable(map.get("ttonkgmaterialtype")).map(Constants.TONKG_TYPE::get).orElse(null));
      model.setTTonKgMaterialMileage((BigDecimal) map.get("ttonkgmaterialmileage"));
      model.setTTonKgMaterialEmissions((BigDecimal) map.get("ttonkgmaterialemissions"));
      model.setTTonKgPartType(
          Optional.ofNullable(map.get("ttonkgmaterialtype")).map(Constants.TONKG_TYPE::get).orElse(null));
      model.setTTonKgPartMileage((BigDecimal) map.get("ttonkgpartmileage"));
      model.setTTonKgPartEmissions((BigDecimal) map.get("ttonkgpartemissions"));

      csvCfpInfoDataList.add(model);
    }

    // 計算結果の取得
    LcaCfpResultCsvData calcResult = CalcUtil.calcLcaCfpResult(csvCfpInfoDataList);

    // lcaMaterilaCdをlcaMaterialNameに
    // pManufacturingDivisionを内製、外製に上書き
    for (int i = 0; i < csvCfpInfoDataList.size(); i++) {
      CsvCfpInfoData model = csvCfpInfoDataList.get(i);
      model.setLcaMaterialCd((String) resultList.get(i).get("lcamaterialname"));
      model.setPManufacturingDivision(Optional.ofNullable(resultList.get(i).get("pmanufacturingdivision"))
          .map(Constants.MANUFACTURING::get).orElse(null));
    }

    // 計算結果をセット
    csvData.setCsvInputMaterialData(calcResult.getCsvInputMaterialData());
    csvData.setCsvInputEnegryData(calcResult.getCsvInputEnegryData());
    csvData.setCsvOutputData(calcResult.getCsvOutputData());
    csvData.setCsvCfpInfoData(csvCfpInfoDataList);

    return csvData;
  }
}
