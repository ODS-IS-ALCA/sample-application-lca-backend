package net.ouranos_ecos.domain.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import net.ouranos_ecos.domain.entity.CsvCfpInfoData;
import net.ouranos_ecos.domain.entity.CsvInputEnegryData;
import net.ouranos_ecos.domain.entity.CsvInputMaterialData;
import net.ouranos_ecos.domain.entity.CsvOutputData;
import net.ouranos_ecos.domain.entity.LcaCfpResultCsvData;
import net.ouranos_ecos.domain.model.LcaCfpResultModel;
import net.ouranos_ecos.domain.model.LcaModel;
import net.ouranos_ecos.domain.model.ProductLcaCfpModel;
import net.ouranos_ecos.domain.model.ProductModel;

/**
 * 計算処理の共通クラス
 */
@Component
public class CalcUtil {

  /**
   * 合計値の計算処理を行う。
   * 
   * @param lcaCfpModel
   * @return LcaCfpResultModel
   */
  public static LcaCfpResultModel calcLcaCfpResult(ProductLcaCfpModel lcaCfpModel) {

    LcaCfpResultModel resultModel = new LcaCfpResultModel();

    // 製品情報
    ProductModel productModel = lcaCfpModel.getProductModel();

    // CFP情報
    List<LcaModel> lcaModelList = lcaCfpModel.getLcaModel();

    // 製品名
    resultModel.setProductItem(productModel.getProductItem());
    // 納入品番
    resultModel.setSupplyItemNo(productModel.getSupplyItemNo());
    // 納入工場
    resultModel.setSupplyFuctory(productModel.getSupplyFuctory());
    // 生産工場所在地国/都市)
    resultModel.setFuctoryAddress(productModel.getFuctoryAddress());
    // 回答者情報
    resultModel.setResponceInfo(productModel.getResponceInfo());

    BigDecimal iron = BigDecimal.ZERO;
    BigDecimal aluminum = BigDecimal.ZERO;
    BigDecimal copper = BigDecimal.ZERO;
    BigDecimal nonFerrousMetals = BigDecimal.ZERO;
    BigDecimal resin = BigDecimal.ZERO;
    BigDecimal others = BigDecimal.ZERO;
    BigDecimal actualElectricPower = BigDecimal.ZERO;
    BigDecimal actualCrudeOilA = BigDecimal.ZERO;
    BigDecimal actualCrudeOilC = BigDecimal.ZERO;
    BigDecimal actualKerosene = BigDecimal.ZERO;
    BigDecimal actualDiesel = BigDecimal.ZERO;
    BigDecimal actualGasoline = BigDecimal.ZERO;
    BigDecimal actualNgl = BigDecimal.ZERO;
    BigDecimal actualLpg = BigDecimal.ZERO;
    BigDecimal actualLng = BigDecimal.ZERO;
    BigDecimal actualCityGas = BigDecimal.ZERO;
    BigDecimal actualAdd1 = BigDecimal.ZERO;
    BigDecimal actualAdd2 = BigDecimal.ZERO;
    BigDecimal simpleElectricPower = BigDecimal.ZERO;
    BigDecimal simpleCrudeOilA = BigDecimal.ZERO;
    BigDecimal simpleCrudeOilC = BigDecimal.ZERO;
    BigDecimal simpleKerosene = BigDecimal.ZERO;
    BigDecimal simpleDiesel = BigDecimal.ZERO;
    BigDecimal simpleGasoline = BigDecimal.ZERO;
    BigDecimal simpleNgl = BigDecimal.ZERO;
    BigDecimal simpleLpg = BigDecimal.ZERO;
    BigDecimal simpleLng = BigDecimal.ZERO;
    BigDecimal simpleCityGas = BigDecimal.ZERO;
    BigDecimal simpleAdd1 = BigDecimal.ZERO;
    BigDecimal simpleAdd2 = BigDecimal.ZERO;
    BigDecimal partsIn = BigDecimal.ZERO;
    BigDecimal partsOut = BigDecimal.ZERO;
    BigDecimal materialIron = BigDecimal.ZERO;
    BigDecimal materialAluminum = BigDecimal.ZERO;
    BigDecimal materialCopper = BigDecimal.ZERO;
    BigDecimal materialNonFerrousMetals = BigDecimal.ZERO;
    BigDecimal materialResin = BigDecimal.ZERO;
    BigDecimal materialOthers = BigDecimal.ZERO;
    BigDecimal resources = BigDecimal.ZERO;
    BigDecimal transportMaterial = BigDecimal.ZERO;
    BigDecimal transportParts = BigDecimal.ZERO;
    BigDecimal waste = BigDecimal.ZERO;

    for (LcaModel lcaModel : lcaModelList) {
      // LCA材料コードに値がある場合
      if (StringUtils.hasText(lcaModel.getLcaMaterialCd())) {

        // 合計質量の値が無ければ0とする。
        BigDecimal totalMass = ConverterUtil.valueOrZero(lcaModel.getTotalMass());
        // 素材報告値の値が無ければ0とする。
        BigDecimal mreport = ConverterUtil.valueOrZero(lcaModel.getMReport());

        if (lcaModel.getLcaMaterialCd().startsWith(Constants.LCA_MATERIAL_CD_1)) {
          // LCA材料コードが「1_」から始まるレコードの合計質量、素材報告値を合計する
          iron = iron.add(totalMass);
          materialIron = materialIron.add(mreport);
        } else if (lcaModel.getLcaMaterialCd().startsWith(Constants.LCA_MATERIAL_CD_2)) {
          // LCA材料コードが「2_」から始まるレコードの合計質量、素材報告値を合計する
          aluminum = aluminum.add(totalMass);
          materialAluminum = materialAluminum.add(mreport);
        } else if (lcaModel.getLcaMaterialCd().startsWith(Constants.LCA_MATERIAL_CD_3)) {
          // LCA材料コードが「3_」から始まるレコードの合計質量、素材報告値を合計する
          copper = copper.add(totalMass);
          materialCopper = materialCopper.add(mreport);
        } else if (lcaModel.getLcaMaterialCd().startsWith(Constants.LCA_MATERIAL_CD_4)) {
          // LCA材料コードが「4_」から始まるレコードの合計質量、素材報告値を合計する
          nonFerrousMetals = nonFerrousMetals.add(totalMass);
          materialNonFerrousMetals = materialNonFerrousMetals.add(mreport);
        } else if (lcaModel.getLcaMaterialCd().startsWith(Constants.LCA_MATERIAL_CD_5)) {
          // LCA材料コードが「5_」から始まるレコードの合計質量、素材報告値を合計する
          resin = resin.add(totalMass);
          materialResin = materialResin.add(mreport);
        } else {
          // LCA材料コードが上記以外から始まるレコードの合計質量、素材報告値を合計する
          others = others.add(totalMass);
          materialOthers = materialOthers.add(mreport);
        }
      }

      if (Constants.MEASURE_METHOD_JISOKU.equals(lcaModel.getPMeasureMethods())) {
        // 加工測定方法が「実測」の加工消費電力量を合計する
        actualElectricPower = actualElectricPower.add(ConverterUtil.valueOrZero(lcaModel.getPElectricAmount()));
        // 加工測定方法が「実測」の加工消費エネルギー量A重油を合計する
        actualCrudeOilA = actualCrudeOilA.add(ConverterUtil.valueOrZero(lcaModel.getPCrudeOilA()));
        // 加工測定方法が「実測」の加工消費エネルギー量C重油を合計する
        actualCrudeOilC = actualCrudeOilC.add(ConverterUtil.valueOrZero(lcaModel.getPCrudeOilC()));
        // 加工測定方法が「実測」の加工消費エネルギー量灯油を合計する
        actualKerosene = actualKerosene.add(ConverterUtil.valueOrZero(lcaModel.getPKerosene()));
        // 加工測定方法が「実測」の加工消費エネルギー量軽油を合計する
        actualDiesel = actualDiesel.add(ConverterUtil.valueOrZero(lcaModel.getPDiesel()));
        // 加工測定方法が「実測」の加工消費エネルギー量ガソリンを合計する
        actualGasoline = actualGasoline.add(ConverterUtil.valueOrZero(lcaModel.getPGasoline()));
        // 加工測定方法が「実測」の加工消費エネルギー量NGLを合計する
        actualNgl = actualNgl.add(ConverterUtil.valueOrZero(lcaModel.getPNgl()));
        // 加工測定方法が「実測」の加工消費エネルギー量LPGを合計する
        actualLpg = actualLpg.add(ConverterUtil.valueOrZero(lcaModel.getPLpg()));
        // 加工測定方法が「実測」の加工消費エネルギー量LNGを合計する
        actualLng = actualLng.add(ConverterUtil.valueOrZero(lcaModel.getPLng()));
        // 加工測定方法が「実測」の加工消費エネルギー量都市ガスを合計する
        actualCityGas = actualCityGas.add(ConverterUtil.valueOrZero(lcaModel.getPCityGus()));
        // 加工測定方法が「実測」の加工消費エネルギー量フリー1を合計する
        actualAdd1 = actualAdd1.add(ConverterUtil.valueOrZero(lcaModel.getPFree1()));
        // 加工測定方法が「実測」の加工消費エネルギー量フリー2を合計する
        actualAdd2 = actualAdd2.add(ConverterUtil.valueOrZero(lcaModel.getPFree2()));
      } else if (Constants.MEASURE_METHOD_KANI.equals(lcaModel.getPMeasureMethods())) {
        // 加工測定方法が「簡易」の加工消費電力量を合計する
        simpleElectricPower = simpleElectricPower.add(ConverterUtil.valueOrZero(lcaModel.getPElectricAmount()));
        // 加工測定方法が「簡易」の加工消費エネルギー量A重油を合計する
        simpleCrudeOilA = simpleCrudeOilA.add(ConverterUtil.valueOrZero(lcaModel.getPCrudeOilA()));
        // 加工測定方法が「簡易」の加工消費エネルギー量C重油を合計する
        simpleCrudeOilC = simpleCrudeOilC.add(ConverterUtil.valueOrZero(lcaModel.getPCrudeOilC()));
        // 加工測定方法が「簡易」の加工消費エネルギー量灯油を合計する
        simpleKerosene = simpleKerosene.add(ConverterUtil.valueOrZero(lcaModel.getPKerosene()));
        // 加工測定方法が「簡易」の加工消費エネルギー量軽油を合計する
        simpleDiesel = simpleDiesel.add(ConverterUtil.valueOrZero(lcaModel.getPDiesel()));
        // 加工測定方法が「簡易」の加工消費エネルギー量ガソリンを合計する
        simpleGasoline = simpleGasoline.add(ConverterUtil.valueOrZero(lcaModel.getPGasoline()));
        // 加工測定方法が「簡易」の加工消費エネルギー量NGLを合計する
        simpleNgl = simpleNgl.add(ConverterUtil.valueOrZero(lcaModel.getPNgl()));
        // 加工測定方法が「簡易」の加工消費エネルギー量LPGを合計する
        simpleLpg = simpleLpg.add(ConverterUtil.valueOrZero(lcaModel.getPLpg()));
        // 加工測定方法が「簡易」の加工消費エネルギー量LNGを合計する
        simpleLng = simpleLng.add(ConverterUtil.valueOrZero(lcaModel.getPLng()));
        // 加工測定方法が「簡易」の加工消費エネルギー量都市ガスを合計する
        simpleCityGas = simpleCityGas.add(ConverterUtil.valueOrZero(lcaModel.getPCityGus()));
        // 加工測定方法が「簡易」の加工消費エネルギー量フリー1を合計する
        simpleAdd1 = simpleAdd1.add(ConverterUtil.valueOrZero(lcaModel.getPFree1()));
        // 加工測定方法が「簡易」の加工消費エネルギー量フリー2を合計する
        simpleAdd2 = simpleAdd2.add(ConverterUtil.valueOrZero(lcaModel.getPFree2()));
      }

      // 加工報告値の値が無ければ0とする。
      BigDecimal preport = ConverterUtil.valueOrZero(lcaModel.getPReport());

      if (Constants.CODE_01.equals(lcaModel.getPManufacturingDivision())) {
        // 加工製造区分が「内製」の加工報告値を合計する
        partsIn = partsIn.add(preport);
      } else if (Constants.CODE_02.equals(lcaModel.getPManufacturingDivision())) {
        // 加工製造区分が「外製」の加工報告値を合計する
        partsOut = partsOut.add(preport);
      }
      // 資材報告値を合計する
      resources = resources.add(ConverterUtil.valueOrZero(lcaModel.getRReport()));
      // 輸送材料報告値を合計する
      transportMaterial = transportMaterial.add(ConverterUtil.valueOrZero(lcaModel.getTMaterialReport()));
      // 輸送部品報告値を合計する
      transportParts = transportParts.add(ConverterUtil.valueOrZero(lcaModel.getTPartReport()));
      // 廃棄物報告値を合計する
      waste = waste.add(ConverterUtil.valueOrZero(lcaModel.getWReport()));

    }
    // 鉄
    resultModel.setIron(ConverterUtil.formatCommas(iron));
    // アルミ
    resultModel.setAluminum(ConverterUtil.formatCommas(aluminum));
    // 銅
    resultModel.setCopper(ConverterUtil.formatCommas(copper));
    // 非鉄金属
    resultModel.setNonFerrousMetals(ConverterUtil.formatCommas(nonFerrousMetals));
    // 樹脂
    resultModel.setResin(ConverterUtil.formatCommas(resin));
    // その他
    resultModel.setOthers(ConverterUtil.formatCommas(others));
    // 材料合計 （「鉄」「アルミ」「銅」「非鉄金属」「樹脂」「その他」の合計）
    resultModel.setMaterialsTotal(
        ConverterUtil.formatCommas(iron.add(aluminum).add(copper).add(nonFerrousMetals).add(resin).add(others)));
    // 実測-電力
    resultModel.setActualElectricPower(ConverterUtil.formatCommas(actualElectricPower));
    // 実測-A重油
    resultModel.setActualCrudeOilA(ConverterUtil.formatCommas(actualCrudeOilA));
    // 実測-C重油
    resultModel.setActualCrudeOilC(ConverterUtil.formatCommas(actualCrudeOilC));
    // 実測-灯油
    resultModel.setActualKerosene(ConverterUtil.formatCommas(actualKerosene));
    // 実測-軽油
    resultModel.setActualDiesel(ConverterUtil.formatCommas(actualDiesel));
    // 実測-ガソリン
    resultModel.setActualGasoline(ConverterUtil.formatCommas(actualGasoline));
    // 実測-天然ガス液(NGL)
    resultModel.setActualNgl(ConverterUtil.formatCommas(actualNgl));
    // 実測-液化石油ガス(LPG)
    resultModel.setActualLpg(ConverterUtil.formatCommas(actualLpg));
    // 実測-天然ｶﾞｽ(LNG)
    resultModel.setActualLng(ConverterUtil.formatCommas(actualLng));
    // 実測-都市ガス
    resultModel.setActualCityGas(ConverterUtil.formatCommas(actualCityGas));
    // 実測-追加①
    resultModel.setActualAdd1(ConverterUtil.formatCommas(actualAdd1));
    // 実測-追加②
    resultModel.setActualAdd2(ConverterUtil.formatCommas(actualAdd2));
    // 簡易計算-電力
    resultModel.setSimpleElectricPower(ConverterUtil.formatCommas(simpleElectricPower));
    // 簡易計算-A重油
    resultModel.setSimpleCrudeOilA(ConverterUtil.formatCommas(simpleCrudeOilA));
    // 簡易計算-C重油
    resultModel.setSimpleCrudeOilC(ConverterUtil.formatCommas(simpleCrudeOilC));
    // 簡易計算-灯油
    resultModel.setSimpleKerosene(ConverterUtil.formatCommas(simpleKerosene));
    // 簡易計算-軽油
    resultModel.setSimpleDiesel(ConverterUtil.formatCommas(simpleDiesel));
    // 簡易計算-ガソリン
    resultModel.setSimpleGasoline(ConverterUtil.formatCommas(simpleGasoline));
    // 簡易計算-天然ガス液(NGL)
    resultModel.setSimpleNgl(ConverterUtil.formatCommas(simpleNgl));
    // 簡易計算-液化石油ガス(LPG)
    resultModel.setSimpleLpg(ConverterUtil.formatCommas(simpleLpg));
    // 簡易計算-天然ｶﾞｽ(LNG)
    resultModel.setSimpleLng(ConverterUtil.formatCommas(simpleLng));
    // 簡易計算-都市ガス
    resultModel.setSimpleCityGas(ConverterUtil.formatCommas(simpleCityGas));
    // 簡易計算-追加①
    resultModel.setSimpleAdd1(ConverterUtil.formatCommas(simpleAdd1));
    // 簡易計算-追加②
    resultModel.setSimpleAdd2(ConverterUtil.formatCommas(simpleAdd2));
    // 合計-電力(実測-電力 と 簡易計算-電力 の合計)
    resultModel.setTotalElectricPower(ConverterUtil.formatCommas(actualElectricPower.add(simpleElectricPower)));
    // 合計-A重油 (実測-A重油 と 簡易計算-A重油 の合計)
    resultModel.setTotalCrudeOilA(ConverterUtil.formatCommas(actualCrudeOilA.add(simpleCrudeOilA)));
    // 合計-C重油 (実測-C重油 と 簡易計算-C重油 の合計)
    resultModel.setTotalCrudeOilC(ConverterUtil.formatCommas(actualCrudeOilC.add(simpleCrudeOilC)));
    // 合計-灯油 (実測-灯油 と 簡易計算-灯油 の合計)
    resultModel.setTotalKerosene(ConverterUtil.formatCommas(actualKerosene.add(simpleKerosene)));
    // 合計-軽油 (実測-軽油 と 簡易計算-軽油 の合計)
    resultModel.setTotalDiesel(ConverterUtil.formatCommas(actualDiesel.add(simpleDiesel)));
    // 合計-ガソリン (実測-ガソリン と 簡易計算-ガソリン の合計)
    resultModel.setTotalGasoline(ConverterUtil.formatCommas(actualGasoline.add(simpleGasoline)));
    // 合計-天然ガス液(NGL) (実測-天然ガス液(NGL) と 簡易計算-天然ガス液(NGL) の合計)
    resultModel.setTotalNgl(ConverterUtil.formatCommas(actualNgl.add(simpleNgl)));
    // 合計-液化石油ガス(LPG) (実測-液化石油ガス(LPG) と 簡易計算-液化石油ガス(LPG) の合計)
    resultModel.setTotalLpg(ConverterUtil.formatCommas(actualLpg.add(simpleLpg)));
    // 合計-天然ｶﾞｽ(LNG) (実測-天然ｶﾞｽ(LNG) と 簡易計算-天然ｶﾞｽ(LNG) の合計)
    resultModel.setTotalLng(ConverterUtil.formatCommas(actualLng.add(simpleLng)));
    // 合計-都市ガス (実測-都市ガス と 簡易計算-都市ガス の合計)
    resultModel.setTotalCityGas(ConverterUtil.formatCommas(actualCityGas.add(simpleCityGas)));
    // 合計-追加① (実測-追加① と 簡易計算-追加① の合計)
    resultModel.setTotalAdd1(ConverterUtil.formatCommas(actualAdd1.add(simpleAdd1)));
    // 合計-追加② (実測-追加② と 簡易計算-追加② の合計)
    resultModel.setTotalAdd2(ConverterUtil.formatCommas(actualAdd2.add(simpleAdd2)));
    // 部品加工-内製
    resultModel.setPartsIn(ConverterUtil.formatCommas(partsIn));
    // 部品加工-外製
    resultModel.setPartsOut(ConverterUtil.formatCommas(partsOut));
    // 材料製造-鉄
    resultModel.setMaterialIron(ConverterUtil.formatCommas(materialIron));
    // 材料製造-アルミ
    resultModel.setMaterialAluminum(ConverterUtil.formatCommas(materialAluminum));
    // 材料製造-銅
    resultModel.setMaterialCopper(ConverterUtil.formatCommas(materialCopper));
    // 材料製造-非鉄金属
    resultModel.setMaterialNonFerrousMetals(ConverterUtil.formatCommas(materialNonFerrousMetals));
    // 材料製造-樹脂
    resultModel.setMaterialResin(ConverterUtil.formatCommas(materialResin));
    // 材料製造-その他
    resultModel.setMaterialOthers(ConverterUtil.formatCommas(materialOthers));
    // 計 (「鉄」「アルミ」「銅」「非鉄金属」「樹脂」「その他」の合計)
    BigDecimal subtotal = materialIron.add(materialAluminum).add(materialCopper).add(materialNonFerrousMetals)
        .add(materialResin).add(materialOthers);
    resultModel.setSubTotal(ConverterUtil.formatCommas(subtotal));
    // 資材製造
    resultModel.setResources(ConverterUtil.formatCommas(resources));
    // 輸送-材料輸送
    resultModel.setTransportMaterial(ConverterUtil.formatCommas(transportMaterial));
    // 輸送-部品輸送
    resultModel.setTransportParts(ConverterUtil.formatCommas(transportParts));
    // 廃棄
    resultModel.setWaste(ConverterUtil.formatCommas(waste));
    // 合計 (「部品加工-内製」「部品加工-外製」「計」「資材製造」「輸送-材料輸送」「輸送-部品輸送」「廃棄」の合計)
    resultModel.setTotal(
        ConverterUtil.formatCommas(partsIn.add(partsOut).add(subtotal).add(resources).add(transportMaterial).add(transportParts).add(waste)));

    return resultModel;

  }

  /**
   * 合計値の計算処理を行う。
   * 
   * @param csvCfpInfoDataList
   * @return CsvData
   */
  public static LcaCfpResultCsvData calcLcaCfpResult(List<CsvCfpInfoData> csvCfpInfoDataList) {

    LcaCfpResultCsvData csvData = new LcaCfpResultCsvData();

    BigDecimal iron = BigDecimal.ZERO;
    BigDecimal aluminum = BigDecimal.ZERO;
    BigDecimal copper = BigDecimal.ZERO;
    BigDecimal nonFerrousMetals = BigDecimal.ZERO;
    BigDecimal resin = BigDecimal.ZERO;
    BigDecimal others = BigDecimal.ZERO;
    BigDecimal actualElectricPower = BigDecimal.ZERO;
    BigDecimal actualCrudeOilA = BigDecimal.ZERO;
    BigDecimal actualCrudeOilC = BigDecimal.ZERO;
    BigDecimal actualKerosene = BigDecimal.ZERO;
    BigDecimal actualDiesel = BigDecimal.ZERO;
    BigDecimal actualGasoline = BigDecimal.ZERO;
    BigDecimal actualNgl = BigDecimal.ZERO;
    BigDecimal actualLpg = BigDecimal.ZERO;
    BigDecimal actualLng = BigDecimal.ZERO;
    BigDecimal actualCityGas = BigDecimal.ZERO;
    BigDecimal actualAdd1 = BigDecimal.ZERO;
    BigDecimal actualAdd2 = BigDecimal.ZERO;
    BigDecimal simpleElectricPower = BigDecimal.ZERO;
    BigDecimal simpleCrudeOilA = BigDecimal.ZERO;
    BigDecimal simpleCrudeOilC = BigDecimal.ZERO;
    BigDecimal simpleKerosene = BigDecimal.ZERO;
    BigDecimal simpleDiesel = BigDecimal.ZERO;
    BigDecimal simpleGasoline = BigDecimal.ZERO;
    BigDecimal simpleNgl = BigDecimal.ZERO;
    BigDecimal simpleLpg = BigDecimal.ZERO;
    BigDecimal simpleLng = BigDecimal.ZERO;
    BigDecimal simpleCityGas = BigDecimal.ZERO;
    BigDecimal simpleAdd1 = BigDecimal.ZERO;
    BigDecimal simpleAdd2 = BigDecimal.ZERO;
    BigDecimal partsIn = BigDecimal.ZERO;
    BigDecimal partsOut = BigDecimal.ZERO;
    BigDecimal materialIron = BigDecimal.ZERO;
    BigDecimal materialAluminum = BigDecimal.ZERO;
    BigDecimal materialCopper = BigDecimal.ZERO;
    BigDecimal materialNonFerrousMetals = BigDecimal.ZERO;
    BigDecimal materialResin = BigDecimal.ZERO;
    BigDecimal materialOthers = BigDecimal.ZERO;
    BigDecimal resources = BigDecimal.ZERO;
    BigDecimal transportMaterial = BigDecimal.ZERO;
    BigDecimal transportParts = BigDecimal.ZERO;
    BigDecimal waste = BigDecimal.ZERO;

    for (CsvCfpInfoData csvCfpInfoData : csvCfpInfoDataList) {
      // LCA材料コードに値がある場合
      if (StringUtils.hasText(csvCfpInfoData.getLcaMaterialCd())) {

        // 合計質量の値が無ければ0とする。
        BigDecimal totalMass = ConverterUtil.valueOrZero(csvCfpInfoData.getTotalMass());
        // 素材報告値の値が無ければ0とする。
        BigDecimal mreport = ConverterUtil.valueOrZero(csvCfpInfoData.getMReport());

        if (csvCfpInfoData.getLcaMaterialCd().startsWith(Constants.LCA_MATERIAL_CD_1)) {
          // LCA材料コードが「1_」から始まるレコードの合計質量、素材報告値を合計する
          iron = iron.add(totalMass);
          materialIron = materialIron.add(mreport);
        } else if (csvCfpInfoData.getLcaMaterialCd().startsWith(Constants.LCA_MATERIAL_CD_2)) {
          // LCA材料コードが「2_」から始まるレコードの合計質量、素材報告値を合計する
          aluminum = aluminum.add(totalMass);
          materialAluminum = materialAluminum.add(mreport);
        } else if (csvCfpInfoData.getLcaMaterialCd().startsWith(Constants.LCA_MATERIAL_CD_3)) {
          // LCA材料コードが「3_」から始まるレコードの合計質量、素材報告値を合計する
          copper = copper.add(totalMass);
          materialCopper = materialCopper.add(mreport);
        } else if (csvCfpInfoData.getLcaMaterialCd().startsWith(Constants.LCA_MATERIAL_CD_4)) {
          // LCA材料コードが「4_」から始まるレコードの合計質量、素材報告値を合計する
          nonFerrousMetals = nonFerrousMetals.add(totalMass);
          materialNonFerrousMetals = materialNonFerrousMetals.add(mreport);
        } else if (csvCfpInfoData.getLcaMaterialCd().startsWith(Constants.LCA_MATERIAL_CD_5)) {
          // LCA材料コードが「5_」から始まるレコードの合計質量、素材報告値を合計する
          resin = resin.add(totalMass);
          materialResin = materialResin.add(mreport);
        } else {
          // LCA材料コードが上記以外から始まるレコードの合計質量、素材報告値を合計する
          others = others.add(totalMass);
          materialOthers = materialOthers.add(mreport);
        }
      }

      if (Constants.MEASURE_METHOD_JISOKU.equals(csvCfpInfoData.getPMeasureMethods())) {
        // 加工測定方法が「実測」の加工消費電力量を合計する
        actualElectricPower = actualElectricPower.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPElectricAmount()));
        // 加工測定方法が「実測」の加工消費エネルギー量A重油を合計する
        actualCrudeOilA = actualCrudeOilA.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPCrudeOilA()));
        // 加工測定方法が「実測」の加工消費エネルギー量C重油を合計する
        actualCrudeOilC = actualCrudeOilC.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPCrudeOilC()));
        // 加工測定方法が「実測」の加工消費エネルギー量灯油を合計する
        actualKerosene = actualKerosene.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPKerosene()));
        // 加工測定方法が「実測」の加工消費エネルギー量軽油を合計する
        actualDiesel = actualDiesel.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPDiesel()));
        // 加工測定方法が「実測」の加工消費エネルギー量ガソリンを合計する
        actualGasoline = actualGasoline.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPGasoline()));
        // 加工測定方法が「実測」の加工消費エネルギー量NGLを合計する
        actualNgl = actualNgl.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPNgl()));
        // 加工測定方法が「実測」の加工消費エネルギー量LPGを合計する
        actualLpg = actualLpg.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPLpg()));
        // 加工測定方法が「実測」の加工消費エネルギー量LNGを合計する
        actualLng = actualLng.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPLng()));
        // 加工測定方法が「実測」の加工消費エネルギー量都市ガスを合計する
        actualCityGas = actualCityGas.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPCityGus()));
        // 加工測定方法が「実測」の加工消費エネルギー量フリー1を合計する
        actualAdd1 = actualAdd1.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPFree1()));
        // 加工測定方法が「実測」の加工消費エネルギー量フリー2を合計する
        actualAdd2 = actualAdd2.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPFree2()));
      } else if (Constants.MEASURE_METHOD_KANI.equals(csvCfpInfoData.getPMeasureMethods())) {
        // 加工測定方法が「簡易」の加工消費電力量を合計する
        simpleElectricPower = simpleElectricPower.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPElectricAmount()));
        // 加工測定方法が「簡易」の加工消費エネルギー量A重油を合計する
        simpleCrudeOilA = simpleCrudeOilA.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPCrudeOilA()));
        // 加工測定方法が「簡易」の加工消費エネルギー量C重油を合計する
        simpleCrudeOilC = simpleCrudeOilC.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPCrudeOilC()));
        // 加工測定方法が「簡易」の加工消費エネルギー量灯油を合計する
        simpleKerosene = simpleKerosene.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPKerosene()));
        // 加工測定方法が「簡易」の加工消費エネルギー量軽油を合計する
        simpleDiesel = simpleDiesel.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPDiesel()));
        // 加工測定方法が「簡易」の加工消費エネルギー量ガソリンを合計する
        simpleGasoline = simpleGasoline.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPGasoline()));
        // 加工測定方法が「簡易」の加工消費エネルギー量NGLを合計する
        simpleNgl = simpleNgl.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPNgl()));
        // 加工測定方法が「簡易」の加工消費エネルギー量LPGを合計する
        simpleLpg = simpleLpg.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPLpg()));
        // 加工測定方法が「簡易」の加工消費エネルギー量LNGを合計する
        simpleLng = simpleLng.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPLng()));
        // 加工測定方法が「簡易」の加工消費エネルギー量都市ガスを合計する
        simpleCityGas = simpleCityGas.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPCityGus()));
        // 加工測定方法が「簡易」の加工消費エネルギー量フリー1を合計する
        simpleAdd1 = simpleAdd1.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPFree1()));
        // 加工測定方法が「簡易」の加工消費エネルギー量フリー2を合計する
        simpleAdd2 = simpleAdd2.add(ConverterUtil.valueOrZero(csvCfpInfoData.getPFree2()));
      }

      // 加工報告値の値が無ければ0とする。
      BigDecimal preport = ConverterUtil.valueOrZero(csvCfpInfoData.getPReport());

      if (Constants.CODE_01.equals(csvCfpInfoData.getPManufacturingDivision())) {
        // 加工製造区分が「内製」の加工報告値を合計する
        partsIn = partsIn.add(preport);
      } else if (Constants.CODE_02.equals(csvCfpInfoData.getPManufacturingDivision())) {
        // 加工製造区分が「外製」の加工報告値を合計する
        partsOut = partsOut.add(preport);
      }
      // 資材報告値を合計する
      resources = resources.add(ConverterUtil.valueOrZero(csvCfpInfoData.getRReport()));
      // 輸送材料報告値を合計する
      transportMaterial = transportMaterial.add(ConverterUtil.valueOrZero(csvCfpInfoData.getTMaterialReport()));
      // 輸送部品報告値を合計する
      transportParts = transportParts.add(ConverterUtil.valueOrZero(csvCfpInfoData.getTPartReport()));
      // 廃棄物報告値を合計する
      waste = waste.add(ConverterUtil.valueOrZero(csvCfpInfoData.getWReport()));

    }

    CsvInputMaterialData csvInputMaterialData = new CsvInputMaterialData();
    // 鉄
    csvInputMaterialData.setIron(ConverterUtil.formatCommas(iron));
    // アルミ
    csvInputMaterialData.setAluminum(ConverterUtil.formatCommas(aluminum));
    // 銅
    csvInputMaterialData.setCopper(ConverterUtil.formatCommas(copper));
    // 非鉄金属
    csvInputMaterialData.setNonFerrousMetals(ConverterUtil.formatCommas(nonFerrousMetals));
    // 樹脂
    csvInputMaterialData.setResin(ConverterUtil.formatCommas(resin));
    // その他
    csvInputMaterialData.setOthers(ConverterUtil.formatCommas(others));
    // 材料合計 （「鉄」「アルミ」「銅」「非鉄金属」「樹脂」「その他」の合計）
    csvInputMaterialData.setMaterialsTotal(
        ConverterUtil.formatCommas(iron.add(aluminum).add(copper).add(nonFerrousMetals).add(resin).add(others)));
    csvData.setCsvInputMaterialData(csvInputMaterialData);

    List<CsvInputEnegryData> csvInputEnegryData = new ArrayList<CsvInputEnegryData>();

    // 実測
    CsvInputEnegryData csvInputEnegryData1 = new CsvInputEnegryData();
    csvInputEnegryData1.setMethod("実測");
    // 電力
    csvInputEnegryData1.setElectricPower(ConverterUtil.formatCommas(actualElectricPower));
    // A重油
    csvInputEnegryData1.setCrudeOilA(ConverterUtil.formatCommas(actualCrudeOilA));
    // C重油
    csvInputEnegryData1.setCrudeOilC(ConverterUtil.formatCommas(actualCrudeOilC));
    // 灯油
    csvInputEnegryData1.setKerosene(ConverterUtil.formatCommas(actualKerosene));
    // 軽油
    csvInputEnegryData1.setDiesel(ConverterUtil.formatCommas(actualDiesel));
    // ガソリン
    csvInputEnegryData1.setGasoline(ConverterUtil.formatCommas(actualGasoline));
    // 天然ガス液(NGL)
    csvInputEnegryData1.setNgl(ConverterUtil.formatCommas(actualNgl));
    // 液化石油ガス(LPG)
    csvInputEnegryData1.setLpg(ConverterUtil.formatCommas(actualLpg));
    // 天然ｶﾞｽ(LNG)
    csvInputEnegryData1.setLng(ConverterUtil.formatCommas(actualLng));
    // 都市ガス
    csvInputEnegryData1.setCityGas(ConverterUtil.formatCommas(actualCityGas));
    // 追加①
    csvInputEnegryData1.setAdd1(ConverterUtil.formatCommas(actualAdd1));
    // 追加②
    csvInputEnegryData1.setAdd2(ConverterUtil.formatCommas(actualAdd2));
    csvInputEnegryData.add(csvInputEnegryData1);

    // 簡易
    CsvInputEnegryData csvInputEnegryData2 = new CsvInputEnegryData();
    csvInputEnegryData2.setMethod("簡易");
    // 電力
    csvInputEnegryData2.setElectricPower(ConverterUtil.formatCommas(simpleElectricPower));
    // A重油
    csvInputEnegryData2.setCrudeOilA(ConverterUtil.formatCommas(simpleCrudeOilA));
    // C重油
    csvInputEnegryData2.setCrudeOilC(ConverterUtil.formatCommas(simpleCrudeOilC));
    // 灯油
    csvInputEnegryData2.setKerosene(ConverterUtil.formatCommas(simpleKerosene));
    // 軽油
    csvInputEnegryData2.setDiesel(ConverterUtil.formatCommas(simpleDiesel));
    // ガソリン
    csvInputEnegryData2.setGasoline(ConverterUtil.formatCommas(simpleGasoline));
    // 天然ガス液(NGL)
    csvInputEnegryData2.setNgl(ConverterUtil.formatCommas(simpleNgl));
    // 液化石油ガス(LPG)
    csvInputEnegryData2.setLpg(ConverterUtil.formatCommas(simpleLpg));
    // 天然ｶﾞｽ(LNG)
    csvInputEnegryData2.setLng(ConverterUtil.formatCommas(simpleLng));
    // 都市ガス
    csvInputEnegryData2.setCityGas(ConverterUtil.formatCommas(simpleCityGas));
    // 追加①
    csvInputEnegryData2.setAdd1(ConverterUtil.formatCommas(simpleAdd1));
    // 追加②
    csvInputEnegryData2.setAdd2(ConverterUtil.formatCommas(simpleAdd2));
    csvInputEnegryData.add(csvInputEnegryData2);

    // 合計
    CsvInputEnegryData csvInputEnegryData3 = new CsvInputEnegryData();
    csvInputEnegryData3.setMethod("合計");
    // 電力(実測-電力 と 簡易計算-電力 の合計)
    csvInputEnegryData3.setElectricPower(ConverterUtil.formatCommas(actualElectricPower.add(simpleElectricPower)));
    // A重油 (実測-A重油 と 簡易計算-A重油 の合計)
    csvInputEnegryData3.setCrudeOilA(ConverterUtil.formatCommas(actualCrudeOilA.add(simpleCrudeOilA)));
    // C重油 (実測-C重油 と 簡易計算-C重油 の合計)
    csvInputEnegryData3.setCrudeOilC(ConverterUtil.formatCommas(actualCrudeOilC.add(simpleCrudeOilC)));
    // 灯油 (実測-灯油 と 簡易計算-灯油 の合計)
    csvInputEnegryData3.setKerosene(ConverterUtil.formatCommas(actualKerosene.add(simpleKerosene)));
    // 軽油 (実測-軽油 と 簡易計算-軽油 の合計)
    csvInputEnegryData3.setDiesel(ConverterUtil.formatCommas(actualDiesel.add(simpleDiesel)));
    // ガソリン (実測-ガソリン と 簡易計算-ガソリン の合計)
    csvInputEnegryData3.setGasoline(ConverterUtil.formatCommas(actualGasoline.add(simpleGasoline)));
    // 天然ガス液(NGL) (実測-天然ガス液(NGL) と 簡易計算-天然ガス液(NGL) の合計)
    csvInputEnegryData3.setNgl(ConverterUtil.formatCommas(actualNgl.add(simpleNgl)));
    // 液化石油ガス(LPG) (実測-液化石油ガス(LPG) と 簡易計算-液化石油ガス(LPG) の合計)
    csvInputEnegryData3.setLpg(ConverterUtil.formatCommas(actualLpg.add(simpleLpg)));
    // 天然ｶﾞｽ(LNG) (実測-天然ｶﾞｽ(LNG) と 簡易計算-天然ｶﾞｽ(LNG) の合計)
    csvInputEnegryData3.setLng(ConverterUtil.formatCommas(actualLng.add(simpleLng)));
    // 都市ガス (実測-都市ガス と 簡易計算-都市ガス の合計)
    csvInputEnegryData3.setCityGas(ConverterUtil.formatCommas(actualCityGas.add(simpleCityGas)));
    // 追加① (実測-追加① と 簡易計算-追加① の合計)
    csvInputEnegryData3.setAdd1(ConverterUtil.formatCommas(actualAdd1.add(simpleAdd1)));
    // 追加② (実測-追加② と 簡易計算-追加② の合計)
    csvInputEnegryData3.setAdd2(ConverterUtil.formatCommas(actualAdd2.add(simpleAdd2)));
    csvInputEnegryData.add(csvInputEnegryData3);
    csvData.setCsvInputEnegryData(csvInputEnegryData);

    CsvOutputData csvOutputData = new CsvOutputData();
    // 部品加工-内製
    csvOutputData.setPartsIn(ConverterUtil.formatCommas(partsIn));
    // 部品加工-外製
    csvOutputData.setPartsOut(ConverterUtil.formatCommas(partsOut));
    // 材料製造-鉄
    csvOutputData.setMaterialIron(ConverterUtil.formatCommas(materialIron));
    // 材料製造-アルミ
    csvOutputData.setMaterialAluminum(ConverterUtil.formatCommas(materialAluminum));
    // 材料製造-銅
    csvOutputData.setMaterialCopper(ConverterUtil.formatCommas(materialCopper));
    // 材料製造-非鉄金属
    csvOutputData.setMaterialNonFerrousMetals(ConverterUtil.formatCommas(materialNonFerrousMetals));
    // 材料製造-樹脂
    csvOutputData.setMaterialResin(ConverterUtil.formatCommas(materialResin));
    // 材料製造-その他
    csvOutputData.setMaterialOthers(ConverterUtil.formatCommas(materialOthers));
    // 計 (「鉄」「アルミ」「銅」「非鉄金属」「樹脂」「その他」の合計)
    BigDecimal subtotal = materialIron.add(materialAluminum).add(materialCopper).add(materialNonFerrousMetals)
        .add(materialResin).add(materialOthers);
    csvOutputData.setSubTotal(ConverterUtil.formatCommas(subtotal));
    // 資材製造
    csvOutputData.setResources(ConverterUtil.formatCommas(resources));
    // 輸送-材料輸送
    csvOutputData.setTransportMaterial(ConverterUtil.formatCommas(transportMaterial));
    // 輸送-部品輸送
    csvOutputData.setTransportParts(ConverterUtil.formatCommas(transportParts));
    // 廃棄
    csvOutputData.setWaste(ConverterUtil.formatCommas(waste));
    // 合計 (「部品加工-内製」「部品加工-外製」「計」「資材製造」「輸送-材料輸送」「輸送-部品輸送」「廃棄」の合計)
    csvOutputData.setTotal(
        ConverterUtil.formatCommas(partsIn.add(partsOut).add(subtotal).add(resources).add(transportMaterial).add(transportParts).add(waste)));
    csvData.setCsvOutputData(csvOutputData);

    return csvData;

  }
}
