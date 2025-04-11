package net.ouranos_ecos.domain.service;

import java.util.ArrayList;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.ouranos_ecos.domain.common.CalcUtil;
import net.ouranos_ecos.domain.common.ErrorUtil;
import net.ouranos_ecos.domain.model.LcaCfpResultModel;
import net.ouranos_ecos.domain.model.LcaModel;
import net.ouranos_ecos.domain.model.LcaResponseModel;
import net.ouranos_ecos.domain.model.ProductLcaCfpModel;
import net.ouranos_ecos.domain.model.ProductModel;
import net.ouranos_ecos.domain.model.ResponseProductLcaCfpModel;
import net.ouranos_ecos.domain.model.ResponseProductModel;
import net.ouranos_ecos.domain.repository.jdbc.LcaCfpJdbc;
import net.ouranos_ecos.domain.repository.jdbc.LcaResponseCfpJdbc;

/**
 * LcaCfpResultServiceクラス。
 */
@Service
@Slf4j
public class LcaCfpResultService {

  @Inject
  LcaCfpJdbc lcaCfpJdbc;

  @Inject
  LcaResponseCfpJdbc lcaResponseCfpJdbc;
  
  @Inject
  ObjectMapper mapper;

  @Transactional
  public ResponseEntity<byte[]> getRequest(HttpServletRequest request) {

    ResponseEntity<byte[]> responseEntity = null;

    try {

      // 内部事業者識別子を取得
      String operatorId = request.getParameter("operatorId");
      if (!StringUtils.hasText(operatorId)) {
        // 内部事業者識別子が取得できない場合
        return ErrorUtil.badRequestResponse(mapper, "", request.getMethod());
      }

      // 製品トレース識別子を取得
      String producttraceId = request.getParameter("productTraceId");
      if (!StringUtils.hasText(producttraceId)) {
        // 製品トレース識別子が取得できない場合
        return ErrorUtil.badRequestResponse(mapper, operatorId, request.getMethod());
      }

      LcaCfpResultModel resultModel = null;
      // 製品トレース識別子を取得
      String responseId = request.getParameter("responseId");

      if (StringUtils.hasText(responseId)) {
        // パラメータで製品情報、LCA部品構成情報、LCACFP情報を取得する。
        ResponseProductLcaCfpModel responseModel = lcaResponseCfpJdbc.select(operatorId, producttraceId, responseId);
        // 計算して、レスポンスにセット
        resultModel = CalcUtil.calcLcaCfpResult(copy(responseModel));
      } else {
        // パラメータで製品情報、LCA部品構成情報、LCACFP情報を取得する。
        ProductLcaCfpModel lcaCfpModel = lcaCfpJdbc.select(operatorId, producttraceId);
        // 計算して、レスポンスにセット
        resultModel = CalcUtil.calcLcaCfpResult(lcaCfpModel);
      }
      // レスポンスを返却
      byte[] json = mapper.writeValueAsBytes(resultModel);
      responseEntity = ResponseEntity.status(HttpStatus.OK).headers(new HttpHeaders()).body(json);

    } catch (Exception e) {
      log.warn("予期せぬエラー", e);
      e.printStackTrace();
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, request.getParameter("operatorId"),
          request.getMethod());
    }

    return responseEntity;
  }


  /**
   * ResponseProductLcaCfpModelをProductLcaCfpModelにコピーする
   * 
   * @param responseModel
   * @return
   */
  private ProductLcaCfpModel copy(ResponseProductLcaCfpModel responseModel) {

    // 返却Model
    ProductLcaCfpModel resultModel = new ProductLcaCfpModel();
    // コピー元 (ResponseProductModel)
    ResponseProductModel responseProductModel = responseModel.getResponseProductModel();
    // コピー先 (ProductModel)
    ProductModel productModel = new ProductModel();
    productModel.setOperatorId(responseProductModel.getOperatorId());
    productModel.setProductTraceId(responseProductModel.getProductTraceId());
    productModel.setProductItem(responseProductModel.getProductItem());
    productModel.setSupplyItemNo(responseProductModel.getSupplyItemNo());
    productModel.setSupplyFuctory(responseProductModel.getSupplyFuctory());
    productModel.setFuctoryAddress(responseProductModel.getFuctoryAddress());
    productModel.setResponceInfo(responseProductModel.getResponceInfo());
    productModel.setMaterialsTotal(responseProductModel.getMaterialsTotal());
    productModel.setGco2eqTotal(responseProductModel.getGco2eqTotal());
    productModel.setCfpModifieDat(responseProductModel.getCfpModifieDat());
    resultModel.setProductModel(productModel);

    // コピー元 (LcaResponseModel)
    List<LcaResponseModel> lcaResponseModelList = responseModel.getLcaResponseModel();
    // コピー先 (LcaModel)
    List<LcaModel> lcaModelList = new ArrayList<LcaModel>();

    for (LcaResponseModel resModel : lcaResponseModelList) {

      LcaModel model = new LcaModel();
      model.setOperatorId(resModel.getOperatorId());
      model.setTraceId(resModel.getTraceId());
      model.setPartsName(resModel.getPartsName());
      model.setPartsLabelName(resModel.getPartsLabelName());
      model.setSupportPartsName(resModel.getSupportPartsName());
      model.setPartsStructureLevel(resModel.getPartsStructureLevel());
      model.setNumber(resModel.getNumber());
      model.setMass(resModel.getMass());
      model.setTotalMass(resModel.getTotalMass());
      model.setMaterialCd(resModel.getMaterialCd());
      model.setMaterialStandard(resModel.getMaterialStandard());
      model.setMaterialCategory(resModel.getMaterialCategory());
      model.setLcaMaterialCd(resModel.getLcaMaterialCd());
      model.setLcaMaterialName(resModel.getLcaMaterialName());
      model.setPartsProcurementCd(resModel.getPartsProcurementCd());
      model.setMateriaProcurementCd(resModel.getMateriaProcurementCd());
      model.setMateriaProcurementCd(resModel.getMateriaProcurementCd());
      model.setEndFlag(resModel.isEndFlag());
      model.setBottomLayerFlag(resModel.isBottomLayerFlag());
      model.setProductTraceId(resModel.getProductTraceId());
      model.setRowNo(resModel.getRowNo());
      model.setCfpId(resModel.getCfpId());
      model.setMReport(resModel.getMReport());
      model.setMMeasureMethods(resModel.getMMeasureMethods());
      model.setMCountryCd(resModel.getMCountryCd());
      model.setMateialPir(resModel.getMateialPir());
      model.setMPcRelv(resModel.getMPcRelv());
      model.setMCrOtherIndustry(resModel.getMCrOtherIndustry());
      model.setMUnclassifiable(resModel.getMUnclassifiable());
      model.setMTotal(resModel.getMTotal());
      model.setMYieldRate(resModel.getMYieldRate());
      model.setMInputWeight(resModel.getMInputWeight());
      model.setMBaseUnitEmissions(resModel.getMBaseUnitEmissions());
      model.setMDirectGhg(resModel.getMDirectGhg());
      model.setMEnergyRate(resModel.getMEnergyRate());
      model.setMElectricBaseUnit(resModel.getMElectricBaseUnit());
      model.setMPowerConsumption(resModel.getMPowerConsumption());
      model.setMElectricGhg(resModel.getMElectricGhg());
      model.setPReport(resModel.getPReport());
      model.setPMeasureMethods(resModel.getPMeasureMethods());
      model.setPCountryCd(resModel.getPCountryCd());
      model.setPManufacturingDivision(resModel.getPManufacturingDivision());
      model.setP1Cd(resModel.getP1Cd());
      model.setP2Cd(resModel.getP2Cd());
      model.setP3Cd(resModel.getP3Cd());
      model.setP4Cd(resModel.getP4Cd());
      model.setPEngyRate(resModel.getPEngyRate());
      model.setPElectricBaseUnit(resModel.getPElectricBaseUnit());
      model.setPElectricAmount(resModel.getPElectricAmount());
      model.setPCrudeOilA(resModel.getPCrudeOilA());
      model.setPCrudeOilC(resModel.getPCrudeOilC());
      model.setPKerosene(resModel.getPKerosene());
      model.setPDiesel(resModel.getPDiesel());
      model.setPGasoline(resModel.getPGasoline());
      model.setPNgl(resModel.getPNgl());
      model.setPLpg(resModel.getPLpg());
      model.setPLng(resModel.getPLng());
      model.setPCityGus(resModel.getPCityGus());
      model.setPFree1(resModel.getPFree1());
      model.setPFree2(resModel.getPFree2());
      model.setPOtherWasteReport(resModel.getPOtherWasteReport());
      model.setRReport(resModel.getRReport());
      model.setRMeasureMethods(resModel.getRMeasureMethods());
      model.setRIndustrialWaterSupply(resModel.getRIndustrialWaterSupply());
      model.setRWaterSupply(resModel.getRWaterSupply());
      model.setRCompressedAir15(resModel.getRCompressedAir15());
      model.setRCompressedAir90(resModel.getRCompressedAir90());
      model.setRThinner(resModel.getRThinner());
      model.setRAmmonia(resModel.getRAmmonia());
      model.setRNitricAcid(resModel.getRNitricAcid());
      model.setRCausticSoda(resModel.getRCausticSoda());
      model.setRHydrochloricAcid(resModel.getRHydrochloricAcid());
      model.setRAcetylene(resModel.getRAcetylene());
      model.setRInorganicChemicalIndustrialProducts(resModel.getRInorganicChemicalIndustrialProducts());
      model.setRSulfuricAcid(resModel.getRSulfuricAcid());
      model.setRAnhydrousChromicAcid(resModel.getRAnhydrousChromicAcid());
      model.setROrganicChemicalIndustrialProducts(resModel.getROrganicChemicalIndustrialProducts());
      model.setRCleaningAgents(resModel.getRCleaningAgents());
      model.setRCelluloseAdhesives(resModel.getRCelluloseAdhesives());
      model.setRLubricatingOil(resModel.getRLubricatingOil());
      model.setRFree1(resModel.getRFree1());
      model.setRFree2(resModel.getRFree2());
      model.setWReport(resModel.getWReport());
      model.setWMeasureMethods(resModel.getWMeasureMethods());
      model.setWAsh(resModel.getWAsh());
      model.setWInorganicSludgeMining(resModel.getWInorganicSludgeMining());
      model.setWOrganicSludgeManufacturing(resModel.getWOrganicSludgeManufacturing());
      model.setWWastePlasticsManufacturing(resModel.getWWastePlasticsManufacturing());
      model.setWMetalScrap(resModel.getWMetalScrap());
      model.setWCeramicScrap(resModel.getWCeramicScrap());
      model.setWSlag(resModel.getWSlag());
      model.setWDust(resModel.getWDust());
      model.setWWasteOilFromPetroleum(resModel.getWWasteOilFromPetroleum());
      model.setWNaturalFiberScrap(resModel.getWNaturalFiberScrap());
      model.setWRubberScrap(resModel.getWRubberScrap());
      model.setWWasteAcid(resModel.getWWasteAcid());
      model.setWWasteAlkali(resModel.getWWasteAlkali());
      model.setWFree1(resModel.getWFree1());
      model.setWFree2(resModel.getWFree2());
      model.setTMaterialReport(resModel.getTMaterialReport());
      model.setTPartReport(resModel.getTPartReport());
      model.setTMeasureMethods(resModel.getTMeasureMethods());
      model.setTWeightMaterialInput(resModel.getTWeightMaterialInput());
      model.setTWeightMaterialEmissions(resModel.getTWeightMaterialEmissions());
      model.setTWeightPartTotal(resModel.getTWeightPartTotal());
      model.setTWeightPartEmissions(resModel.getTWeightPartEmissions());
      model.setTFuelMaterialType(resModel.getTFuelMaterialType());
      model.setTFuelMaterialConsumption(resModel.getTFuelMaterialConsumption());
      model.setTFuelMaterialEmissions(resModel.getTFuelMaterialEmissions());
      model.setTFuelPartType(resModel.getTFuelPartType());
      model.setTFuelPartConsumption(resModel.getTFuelPartConsumption());
      model.setTFuelPartEmissions(resModel.getTFuelPartEmissions());
      model.setTFuelEconomyMaterialType(resModel.getTFuelEconomyMaterialType());
      model.setTFuelEconomyMaterialMileage(resModel.getTFuelEconomyMaterialMileage());
      model.setTFuelEconomyMaterialFuelEconomy(resModel.getTFuelEconomyMaterialFuelEconomy());
      model.setTFuelEconomyMaterialEmissions(resModel.getTFuelEconomyMaterialEmissions());
      model.setTFuelEconomyPartType(resModel.getTFuelEconomyPartType());
      model.setTFuelEconomyPartMileage(resModel.getTFuelEconomyPartMileage());
      model.setTFuelEconomyPartFuelEconomy(resModel.getTFuelEconomyPartFuelEconomy());
      model.setTFuelEconomyPartEmissions(resModel.getTFuelEconomyPartEmissions());
      model.setTTonKgMaterialType(resModel.getTTonKgMaterialType());
      model.setTTonKgMaterialMileage(resModel.getTTonKgMaterialMileage());
      model.setTTonKgMaterialEmissions(resModel.getTTonKgMaterialEmissions());
      model.setTTonKgPartType(resModel.getTTonKgPartType());
      model.setTTonKgPartMileage(resModel.getTTonKgPartMileage());
      model.setTTonKgPartEmissions(resModel.getTTonKgPartEmissions());

      lcaModelList.add(model);
    }
    resultModel.setLcaModel(lcaModelList);

    return resultModel;
  }

}