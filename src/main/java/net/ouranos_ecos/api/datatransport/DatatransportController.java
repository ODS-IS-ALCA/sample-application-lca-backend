package net.ouranos_ecos.api.datatransport;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.ouranos_ecos.domain.service.CfpCalcRequestService;
import net.ouranos_ecos.domain.service.CfpRequestResponseService;
import net.ouranos_ecos.domain.service.CfpResponseService;
import net.ouranos_ecos.domain.service.LcaCfpResultService;
import net.ouranos_ecos.domain.service.LcaCfpService;
import net.ouranos_ecos.domain.service.LcaMaterialService;
import net.ouranos_ecos.domain.service.LcaResponseCfpService;
import net.ouranos_ecos.domain.service.ProcessingStepService;
import net.ouranos_ecos.domain.service.ProductLcaPartService;
import net.ouranos_ecos.domain.service.ProductService;
import net.ouranos_ecos.domain.service.ProductionCountryService;
import net.ouranos_ecos.domain.service.ResponseProductService;
import net.ouranos_ecos.domain.service.UnitService;

/**
 * データ流通
 */

@RestController
@RequestMapping("api/v1/datatransport")
public class DatatransportController {

  @Inject
  private ProductService productService;
  @Inject
  private LcaCfpService lcaCfpService;
  @Inject
  private ProductionCountryService productionCountryService;
  @Inject
  private ProcessingStepService processingStepService;
  @Inject
  private LcaMaterialService lcaMaterialService;
  @Inject
  private ProductLcaPartService productLcaPartService;
  @Inject
  private UnitService unitService;
  @Inject
  private LcaCfpResultService lcaCfpResultService;
  @Inject
  private CfpRequestResponseService cfpReqResService;
  @Inject
  private CfpResponseService cfpResService;
  @Inject
  private CfpResponseService cfpResponseService;
  @Inject
  private CfpCalcRequestService cfpCalcRequestService;
  @Inject
  private ResponseProductService responseProductService;
  @Inject
  private LcaResponseCfpService lcaResponseCfpService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<byte[]> get(HttpServletRequest request) {

    String dataTarget = request.getParameter("dataTarget");

    // dataTargeによる処理分岐
    switch (dataTarget) {

      case "product" :
        // 製品情報一覧取得
        return productService.getRequest(request);

      case "lcaCfp" :
        // LCACFP情報取得
        return lcaCfpService.getRequest(request);

      case "productionCountry" :
        // 生産国取得
        return productionCountryService.getRequest(request);

      case "processingStep" :
        // 加工工程取得
        return processingStepService.getRequest(request);

      case "lcaMaterial" :
        // LCA材料取得
        return lcaMaterialService.getRequest(request);

      case "unit" :
        // 原単位情報取得
        return unitService.getRequest(request);

      case "lcaCfpResult" :
        // LCACFP結果情報取得
        return lcaCfpResultService.getRequest(request);

      case "cfpRequestResponse" :
        // 依頼・回答情報取得
        return cfpReqResService.getRequest(request);

      case "cfpCalcRequest" :
        // CFP算出依頼情報取得
        return cfpCalcRequestService.getRequest(request);

      case "cfpRequestList" :
        // 受領依頼一覧情報取得
        return cfpResponseService.getRequest(request);

      case "responseProduct" :
        // 回答一覧取得
        return responseProductService.getRequest(request);

      case "lcaResponseCfp" :
        // LCA回答CFP情報取得
        return lcaResponseCfpService.getRequest(request);

      case "productLcaPart" :
        // 製品_LCS部品構成取得
        return productLcaPartService.getRequest(request);

    }
    return null;
  }

  @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<byte[]> put(HttpServletRequest request) {

    String dataTarget = request.getParameter("dataTarget");

    // dataTargeによる処理分岐
    switch (dataTarget) {

      case "lcaCfp" :
        // LCACFP情報登録
        return lcaCfpService.putRequest(request);

      case "productLcaPart" :
        // 製品_LCS部品構成登録
        return productLcaPartService.putRequest(request);

      case "cfpCalcRequest" :
        // CFP算出依頼登録
        return cfpCalcRequestService.putRequest(request);

      case "cfpResponse" :
        // 回答登録
        return cfpResService.putRequest(request);

      case "lcaResponseCfp" :
        // LCA回答CFP情報受入登録API
        return lcaResponseCfpService.putRequest(request);
    }
    return null;
  }
}
