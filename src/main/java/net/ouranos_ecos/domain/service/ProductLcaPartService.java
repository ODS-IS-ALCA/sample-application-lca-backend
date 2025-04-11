package net.ouranos_ecos.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.ouranos_ecos.domain.common.Constants;
import net.ouranos_ecos.domain.common.ConverterUtil;
import net.ouranos_ecos.domain.common.ErrorUtil;
import net.ouranos_ecos.domain.model.LcaCfpModel;
import net.ouranos_ecos.domain.model.LcaCfpSimpleCalcModel;
import net.ouranos_ecos.domain.model.LcaPartsStructureModel;
import net.ouranos_ecos.domain.model.ProductInfoModel;
import net.ouranos_ecos.domain.model.ProductModel;
import net.ouranos_ecos.domain.model.StandardPartProcessingModel;
import net.ouranos_ecos.domain.model.UnitElectricModel;
import net.ouranos_ecos.domain.model.UnitEnergyModel;
import net.ouranos_ecos.domain.model.UnitMaterialsModel;
import net.ouranos_ecos.domain.model.UnitPartProcessingModel;
import net.ouranos_ecos.domain.model.UnitTransportWeightModel;
import net.ouranos_ecos.domain.model.UnitWasteModel;
import net.ouranos_ecos.domain.repository.jdbc.LcaCfpJdbc;
import net.ouranos_ecos.domain.repository.jdbc.LcaCfpSimpleCalcJdbc;
import net.ouranos_ecos.domain.repository.jdbc.LcaPartsStructureJdbc;
import net.ouranos_ecos.domain.repository.jdbc.ProductJdbc;
import net.ouranos_ecos.domain.repository.jdbc.StandardPartProcessingJdbc;
import net.ouranos_ecos.domain.repository.jdbc.UnitJdbc;

/**
 * ProductLcaPartServiceクラス。
 */
@Service
public class ProductLcaPartService {

  @Inject
  ObjectMapper mapper;

  @Inject
  ProductJdbc productJdbc;
  @Inject
  LcaPartsStructureJdbc lcaPartsStructureJdbc;
  @Inject
  LcaCfpJdbc lcaCfpJdbc;
  @Inject
  LcaCfpSimpleCalcJdbc lcaCfpSimpleCalcJdbc;
  @Inject
  UnitJdbc unitJdbc;
  @Inject
  StandardPartProcessingJdbc standardPartProcessingJdbc;

  final BigDecimal BIGDECLIMAL_HUNDRED = BigDecimal.valueOf(100);
  final BigDecimal BIGDECLIMAL_THOUSAND = BigDecimal.valueOf(1000);

  @Transactional
  public ResponseEntity<byte[]> getRequest(HttpServletRequest request) {

    ResponseEntity<byte[]> responseEntity = null;

    try {

      // 内部事業者識別子を取得
      String operatorId = request.getParameter("operatorId");
      if (operatorId == null || "".equals(operatorId)) {
        // 内部事業者識別子が取得できない場合
        return ErrorUtil.badRequestResponse(mapper, "", request.getMethod());
      }

      // 製品トレース識別子を取得
      String productTraceId = request.getParameter("productTraceId");
      if (productTraceId == null || "".equals(productTraceId)) {
        // 製品トレース識別子が取得できない場合
        return ErrorUtil.badRequestResponse(mapper, operatorId, request.getMethod());
      }

      // パラメータでLCA回答CFP情報取得
      ProductInfoModel productInfoModel = new ProductInfoModel();
      // 親部品情報
      ProductModel parentPartsModel = productJdbc.select(operatorId, productTraceId);
      // 子部品情報
      List<LcaPartsStructureModel> lcaPartsStructureModel = lcaPartsStructureJdbc.selectByProductPrimaryKey(operatorId, productTraceId);

      productInfoModel.setProductModel(parentPartsModel);
      productInfoModel.setLcaPartsStructureModel(lcaPartsStructureModel);

      // レスポンスを返却
      byte[] json = mapper.writeValueAsBytes(productInfoModel);
      responseEntity = ResponseEntity.status(HttpStatus.OK).headers(new HttpHeaders()).body(json);

    } catch (Exception e) {
      e.printStackTrace();
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, request.getParameter("operatorId"),
          request.getMethod());
    }

    return responseEntity;
  }
  
  @Transactional
  public ResponseEntity<byte[]> putRequest(HttpServletRequest request) {

    ResponseEntity<byte[]> responseEntity = null;

    // modelクラスの初期化
    ProductInfoModel productInfoModel = new ProductInfoModel();

    // Errorフラグ
    int result = 0;

    try {
      // リクエスト内容の取得
      String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
      // リクエスト内容を対応するクラスに格納
      productInfoModel = mapper.readValue(requestBody, ProductInfoModel.class);

      // CFP依頼フラグのチェック
      responseEntity = validateRequestTargetFlag(productInfoModel.getLcaPartsStructureModel());
      if (responseEntity != null) {
        return responseEntity;
      }

      String productTraceId = productInfoModel.getProductModel().getProductTraceId();
      if (productTraceId == null || "".equals(productTraceId)) {
        // 登録処理
        result = registProcess(productInfoModel.getProductModel(), productInfoModel.getLcaPartsStructureModel());
      } else {
        // 編集処理
        result = editProcess(productInfoModel.getProductModel(), productInfoModel.getLcaPartsStructureModel());
      }
      // レスポンスメッセージを登録
      if (result == 0) {
        responseEntity = ErrorUtil.badRequestResponse(mapper, "", request.getMethod());
      } else {
        byte[] json = mapper.writeValueAsBytes(Constants.SUCSESS_MESSAGE);
        responseEntity = ResponseEntity.status(HttpStatus.CREATED).headers(new HttpHeaders()).body(json);
      }

    } catch (JsonProcessingException e) {
      e.printStackTrace();
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, "", request.getMethod());
    } catch (Exception e) {
      e.printStackTrace();
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, "", request.getMethod());
    }

    return responseEntity;
  }

  /**
   * CFP依頼フラグのチェックを行う
   * 
   * @param childrenParts
   * @return
   */
  private ResponseEntity<byte[]> validateRequestTargetFlag(List<LcaPartsStructureModel> childrenParts) {

    ResponseEntity<byte[]> responseEntity = null;

    // CFP依頼フラグのチェック処理を行う
    Boolean bRequestTargetFlag = false;
    String sPartsName = "";

    for (int i = 0; i < childrenParts.size(); i++) {
      LcaPartsStructureModel childPart = childrenParts.get(i);
      // CFP依頼フラグ(requestTargetFlag)がtrueの場合、
      if (childPart.getRequestTargetFlag() != null && childPart.getRequestTargetFlag()) {
        // 該当行のLCA材料名称(lcaMaterialCd)に値がある場合、エラー
        if (childPart.getLcaMaterialCd() != "" && childPart.getLcaMaterialCd() != null) {
          bRequestTargetFlag = true;
          if (sPartsName.length() == 0) {
            sPartsName = sPartsName + childPart.getPartsName();
          } else {
            sPartsName = sPartsName + " , " + childPart.getPartsName();
          }
        } else {
          // 該当行のLCA材料名称(lcaMaterialCd)に値がない、かつ、次の行がある場合
          if (i < childrenParts.size() - 1) {
            LcaPartsStructureModel childPartNext = childrenParts.get(i + 1);
            // 該当行の構成品レベル(partsStructureLevel) < 次の行の構成品レベル、エラー
            if (childPart.getPartsStructureLevel() < childPartNext.getPartsStructureLevel()) {
              bRequestTargetFlag = true;
              if (sPartsName.length() == 0) {
                sPartsName = sPartsName + childPart.getPartsName();
              } else {
                sPartsName = sPartsName + " , " + childPart.getPartsName();
              }
            }
          }
        }
      }
    }
    sPartsName = Constants.CFPFLAG_CHECK_NG_MSG + "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　" + sPartsName;

    if (bRequestTargetFlag) {
      responseEntity = ErrorUtil.badRequestResponse(mapper, sPartsName);
    }
    return responseEntity;
  }

  /**
   * 登録処理を行う
   * 
   * @param productModel
   * @param childrenParts
   * @return
   * @throws Exception 
   */
  private int registProcess(ProductModel productModel, List<LcaPartsStructureModel> childrenParts) throws Exception {

    // Errorフラグ
    int result = 0;

    String traceId = UUID.randomUUID().toString();
    productModel.setProductTraceId(traceId);
    // 製品情報（product） 親部品insert処理
    result = productJdbc.insert(productModel);
    // 編集後製品情報（product）保存リスト
    List<LcaPartsStructureModel> editLcaPSModelList = new ArrayList<LcaPartsStructureModel>();
    // 編集後LCACFP情報（lcacfp）保存リスト
    List<LcaCfpModel> editLcaCfpModelList = new ArrayList<LcaCfpModel>();
    // 編集後LCACFP簡易計算情報 保存リスト
    List<LcaCfpSimpleCalcModel> editLcaCfpSCModelList = new ArrayList<LcaCfpSimpleCalcModel>();
  
    int romNo = 0;
    for (LcaPartsStructureModel childParts : childrenParts) {

      // UUIDの設定（子部品）
      String childTraceId = UUID.randomUUID().toString();
      childParts.setTraceId(childTraceId);
      childParts.setProductTraceId(traceId);
      childParts.setOperatorId(productModel.getOperatorId());
      childParts.setRowNo(romNo);

      if ("".equals(childParts.getLcaMaterialCd())) {
        // 空文字の場合はnullとする
        childParts.setLcaMaterialCd(null);
      }
      // 部品構成情報（lcapartsstructure）情報保存  
      editLcaPSModelList.add(childParts);
      romNo = romNo + 1;

      LcaCfpModel lcaCfpModel = new LcaCfpModel();
      lcaCfpModel.setOperatorId(productModel.getOperatorId());
      lcaCfpModel.setCfpId(UUID.randomUUID().toString());
      lcaCfpModel.setTraceId(childTraceId);

      // 設定値修正
      modifylcaCfp(childParts, lcaCfpModel);
      // LCACFP情報（lcacfp）情報保存 
      editLcaCfpModelList.add(lcaCfpModel);
      
      LcaCfpSimpleCalcModel lcaCfpSimpleCalcModel = new LcaCfpSimpleCalcModel();
      ConverterUtil.copyFields(lcaCfpModel, lcaCfpSimpleCalcModel);

      // LCACFP簡易計算情報 情報保存
      editLcaCfpSCModelList.add(lcaCfpSimpleCalcModel);
    }

    if (!childrenParts.isEmpty()) {
      // 部品構成情報（lcapartsstructure）一括登録
      lcaPartsStructureJdbc.insertLcaPartsStructure(editLcaPSModelList);
      // LCACFP情報（lcacfp） 一括登録
      lcaCfpJdbc.insertLcaCfp(editLcaCfpModelList);
      // LCACFP簡易計算情報 一括登録
      lcaCfpSimpleCalcJdbc.insertLcaCfpSimpleCalc(editLcaCfpSCModelList);
      // 製品情報のCFP合計値の計算を行う
      calcCfpForProduct(productModel);

      // 製品情報（product）親部品更新処理
      result = productJdbc.updateForCfpInfo(productModel);
    }
    return result;
  }

  /**
   * 編集処理を行う
   * 
   * @param productModel
   * @param editList
   * @return
   * @throws Exception
   */
  private int editProcess(ProductModel productModel, List<LcaPartsStructureModel> editList) throws Exception {

    // 製品情報を更新
    productJdbc.update(productModel);
    
    // 現在のLCA部品構成情報(lcapartsstructure)を取得
    List<LcaPartsStructureModel> currentList = lcaPartsStructureJdbc
        .selectByProductPrimaryKey(productModel.getOperatorId(), productModel.getProductTraceId());

    // 更新行処理
    boolean isUpdate = updateRow(editList, currentList);

    // 削除行処理
    boolean isDelete = deleteRow(productModel, editList, currentList);
    if (isDelete) {
      // 現在のLCA部品構成情報(lcapartsstructure)を再取得
      currentList = lcaPartsStructureJdbc.selectByProductPrimaryKey(productModel.getOperatorId(),
          productModel.getProductTraceId());
    }

    // 追加行処理
    boolean isAdd = addRow(productModel, editList, currentList);

    if (isDelete || isAdd) {
      // 行番号を0から順に設定
      for (int i = 0; i < currentList.size(); i++) {
        currentList.get(i).setRowNo(i);
      }
      // 行番号を更新する
      lcaPartsStructureJdbc.updateCfpRowNo(currentList);
    }

    if (isUpdate || isDelete || isAdd) {
      // 製品情報のCFP合計値の計算を行う
      calcCfpForProduct(productModel);
      // 製品情報を更新
      productJdbc.updateForCfpInfo(productModel);
    }

    return 1;
  }

  /**
   * 編集-更新行の処理を行う。
   * 
   * @param editList
   * @param currentList
   * @return
   */
  private boolean updateRow(List<LcaPartsStructureModel> editList, List<LcaPartsStructureModel> currentList) {

    // Errorフラグ
    boolean isUpdate = false;

    // 更新行の処理
    for (LcaPartsStructureModel baseModel : currentList) {
      for (LcaPartsStructureModel editModel : editList) {

        if (baseModel.getRowNo().equals(editModel.getRowNo())) {
          // 行番号が一致する場合
          if (isAfterNumberModified(baseModel, editModel)) {
            // 個数以降の更新の場合(算出依頼フラグ除く)

            if ("".equals(editModel.getLcaMaterialCd())) {
              // LCA材料コードの選択が空の場合はnullとする
              editModel.setLcaMaterialCd(null);
            }

            // rowNoの一致するLCA部品構成情報を更新する
            lcaPartsStructureJdbc.updateMatchingRowNo(editModel);

            // 更新対象のLCACFP情報を取得
            LcaCfpModel lcaCfpModel = lcaCfpJdbc.selectByTraceId(editModel.getOperatorId(), editModel.getTraceId());

            LcaCfpModel lcaCfpEditModel = new LcaCfpModel();
            lcaCfpEditModel.setOperatorId(lcaCfpModel.getOperatorId());
            lcaCfpEditModel.setCfpId(lcaCfpModel.getCfpId());
            lcaCfpEditModel.setTraceId(lcaCfpModel.getTraceId());

            // LCACFP情報を再計算する
            modifylcaCfp(editModel, lcaCfpEditModel);
            // LCACFP情報を更新する
            lcaCfpJdbc.update(lcaCfpEditModel);

            LcaCfpSimpleCalcModel lcaCfpSimpleCalcModel = new LcaCfpSimpleCalcModel();
            ConverterUtil.copyFields(lcaCfpModel, lcaCfpSimpleCalcModel);
            // LCACFP情報を更新する
            lcaCfpSimpleCalcJdbc.update(lcaCfpSimpleCalcModel);

            isUpdate = true;

          } else if (isBeforeNumberModified(baseModel, editModel)) {
            // [品番～構成品レベル]と[算出依頼フラグ]の更新の場合

            if ("".equals(editModel.getLcaMaterialCd())) {
              // LCA材料コードの選択が空の場合はnullとする
              editModel.setLcaMaterialCd(null);
            }

            // rowNoの一致するLCA部品構成情報を更新する
            lcaPartsStructureJdbc.updateMatchingRowNo(editModel);
          }
          break;
        }
      }
    }
    return isUpdate;
  }

  /**
   * 編集-削除行の処理を行う。
   * 
   * @param productModel
   * @param editList
   * @param currentList
   * @return
   */
  private boolean deleteRow(ProductModel productModel, List<LcaPartsStructureModel> editList,
      List<LcaPartsStructureModel> currentList) {

    boolean isDelete = false;

    // 削除処理
    // editList の rowNo を抽出
    Set<Integer> editRowNos = editList.stream().map(LcaPartsStructureModel::getRowNo).collect(Collectors.toSet());

    // currentList の中で editList に存在しない rowNo を特定
    List<LcaPartsStructureModel> notInEdit = currentList.stream().filter(item -> !editRowNos.contains(item.getRowNo()))
        .collect(Collectors.toList());

    if (!notInEdit.isEmpty()) {
      // rowNoをカンマ区切りのStringに変換
      String rowNos = notInEdit.stream().map(item -> item.getRowNo().toString()).collect(Collectors.joining(", "));

      // LCACFP情報(lcacfp)を削除
      lcaCfpJdbc.deleteByRowNo(productModel.getOperatorId(), productModel.getProductTraceId(), rowNos);
      // LCACFP簡易計算情報(lcacfpsimplecalc)を削除
      lcaCfpSimpleCalcJdbc.deleteByRowNo(productModel.getOperatorId(), productModel.getProductTraceId(), rowNos);
      // LCA部品構成情報(lcapartsstructure)を削除
      lcaPartsStructureJdbc.deleteByRowNo(productModel.getOperatorId(), productModel.getProductTraceId(), rowNos);

      isDelete = true;
    }
    return isDelete;
  }

  /**
   * * 編集-追加行の処理を行う。
   * 
   * @param productModel
   * @param editList
   * @param currentList
   * @return
   */
  private boolean addRow(ProductModel productModel, List<LcaPartsStructureModel> editList,
      List<LcaPartsStructureModel> currentList) {

    boolean isAdd = false;

    for (int i = 0; i < editList.size(); i++) {
      if (editList.get(i).getRowNo() == null) {
        // rowNoに値がない場合

        LcaPartsStructureModel editModel = editList.get(i);

        editModel.setTraceId(UUID.randomUUID().toString());
        editModel.setProductTraceId(productModel.getProductTraceId());
        editModel.setOperatorId(productModel.getOperatorId());

        if ("".equals(editModel.getLcaMaterialCd())) {
          // LCA材料コードの選択が空の場合はnullとする
          editModel.setLcaMaterialCd(null);
        }
        // 部品構成insert処理
        lcaPartsStructureJdbc.insert(editModel);

        // 元リストの同じ場所に追加する
        currentList.add(i, editModel);

        LcaCfpModel lcaCfpModel = new LcaCfpModel();
        lcaCfpModel.setOperatorId(productModel.getOperatorId());
        lcaCfpModel.setCfpId(UUID.randomUUID().toString());
        lcaCfpModel.setTraceId(editModel.getTraceId());

        // LCACFP情報を計算する
        modifylcaCfp(editModel, lcaCfpModel);
        // LCACFP情報を登録する
        lcaCfpJdbc.insert(lcaCfpModel);

        LcaCfpSimpleCalcModel lcaCfpSimpleCalcModel = new LcaCfpSimpleCalcModel();
        ConverterUtil.copyFields(lcaCfpModel, lcaCfpSimpleCalcModel);
        // LCACFP情報を登録する
        lcaCfpSimpleCalcJdbc.insert(lcaCfpSimpleCalcModel);

        isAdd = true;
      }
    }

    return isAdd;
  }

  /**
   * 製品情報のCFP合計値の計算を行う
   * 
   * @param productModel
   */
  private void calcCfpForProduct(ProductModel productModel) {

    // 素材合計重量(g)
    BigDecimal materialsTotal = BigDecimal.ZERO;
    // CFP(g-CO2wq)
    BigDecimal gco2eqTotal = BigDecimal.ZERO;

    List<LcaPartsStructureModel> currentList = lcaPartsStructureJdbc
        .selectByProductPrimaryKey(productModel.getOperatorId(), productModel.getProductTraceId());

    for (LcaPartsStructureModel currentModel : currentList) {

      // 更新対象のLCACFP情報を取得
      LcaCfpModel lcaCfpModel = lcaCfpJdbc.selectByTraceId(currentModel.getOperatorId(), currentModel.getTraceId());

      // 素材報告値
      BigDecimal bMReport = BigDecimal.ZERO;

      // 合計値の算出
      if (currentModel.getLcaMaterialCd() != null && currentModel.getLcaMaterialCd() != "") {
        BigDecimal totalMass = BigDecimal.valueOf(currentModel.getTotalMass().doubleValue());
        materialsTotal = materialsTotal.add(totalMass);
        bMReport = ConverterUtil.valueOrZero(lcaCfpModel.getMReport());
      }

      // 加工報告値
      BigDecimal bPReport = ConverterUtil.valueOrZero(lcaCfpModel.getPReport());
      BigDecimal bRReport = ConverterUtil.valueOrZero(lcaCfpModel.getRReport());
      BigDecimal bTMaterialReport = ConverterUtil.valueOrZero(lcaCfpModel.getTMaterialReport());
      BigDecimal bTPartReport = ConverterUtil.valueOrZero(lcaCfpModel.getTPartReport());
      BigDecimal bWReport = ConverterUtil.valueOrZero(lcaCfpModel.getWReport());

      gco2eqTotal = gco2eqTotal.add(bMReport).add(bPReport).add(bRReport).add(bTMaterialReport).add(bTPartReport)
          .add(bWReport);

    }

    productModel.setMaterialsTotal(materialsTotal.toString());
    productModel.setGco2eqTotal(gco2eqTotal.toString());
  }

  /**
   * 個数以降の更新の場合(算出依頼フラグ除く)チェック
   * 
   * @param baseModel
   * @param editModel
   * @return
   */
  private boolean isAfterNumberModified(LcaPartsStructureModel baseModel, LcaPartsStructureModel editModel) {

    if (baseModel.getLcaMaterialCd() == null) {
      // LCA材料コードの登録値がnullの場合は空とする
      baseModel.setLcaMaterialCd("");
    }

    boolean result = false;

    if (baseModel.getNumber() != null && !baseModel.getNumber().equals(editModel.getNumber())) {
      result = true;
    } else if (baseModel.getMass() != null && !baseModel.getMass().toString().equals(editModel.getMass().toString())) {
      result = true;
    } else if (baseModel.getTotalMass() != null
        && !baseModel.getTotalMass().toString().equals(editModel.getTotalMass().toString())) {
      result = true;
    } else if (baseModel.getMaterialCd() != null && !baseModel.getMaterialCd().equals(editModel.getMaterialCd())) {
      result = true;
    } else if (baseModel.getMaterialStandard() != null
        && !baseModel.getMaterialStandard().equals(editModel.getMaterialStandard())) {
      result = true;
    } else if (baseModel.getMaterialCategory() != null
        && !baseModel.getMaterialCategory().equals(editModel.getMaterialCategory())) {
      result = true;
    } else if (baseModel.getLcaMaterialCd() != null
        && !baseModel.getLcaMaterialCd().trim().equals(editModel.getLcaMaterialCd())) {
      result = true;
    } else if (baseModel.getPartsProcurementCd() != null
        && !baseModel.getPartsProcurementCd().trim().equals(editModel.getPartsProcurementCd())) {
      result = true;
    } else if (baseModel.getMateriaProcurementCd() != null
        && !baseModel.getMateriaProcurementCd().trim().equals(editModel.getMateriaProcurementCd())) {
      result = true;
    }
    return result;
  }

  /**
   * [品番～構成品レベル]と[算出依頼フラグ]の更新の場合チェック
   * 
   * @param baseModel
   * @param editModel
   * @return
   */
  private boolean isBeforeNumberModified(LcaPartsStructureModel baseModel, LcaPartsStructureModel editModel) {

    boolean result = false;

    if (baseModel.getPartsName() != null && !baseModel.getPartsName().equals(editModel.getPartsName())) {
      result = true;
    } else if (baseModel.getPartsLabelName() != null
        && !baseModel.getPartsLabelName().equals(editModel.getPartsLabelName())) {
      result = true;
    } else if (baseModel.getSupportPartsName() != null
        && !baseModel.getSupportPartsName().equals(editModel.getSupportPartsName())) {
      result = true;
    } else if (baseModel.getPartsStructureLevel() != null
        && !baseModel.getPartsStructureLevel().equals(editModel.getPartsStructureLevel())) {
      result = true;
    } else if (baseModel.getRequestTargetFlag() != null
        && !baseModel.getRequestTargetFlag().equals(editModel.getRequestTargetFlag())) {
      result = true;
    }
    return result;
  }

  /**
   * LCACFP情報（lcacfp）の設定値を計算し、更新
      // 原単位情報(IDEA)を取得あり。
      // 他算定方法でCFP計算する場合には、処理を分離し、算定方法に応じたデータセットとの紐づけが必要。
   * 
   * @param lcaCfpModel
   * @return LcaCfpModel
   * @throws DataAccessException
   */
  private void modifylcaCfp(LcaPartsStructureModel childParts, LcaCfpModel lcaCfpModel) {

    // 2-2 素材生産国コード
    // 合計質量
    BigDecimal bTotalMass = BigDecimal.valueOf(childParts.getTotalMass().doubleValue());
    if (bTotalMass.compareTo(BigDecimal.ZERO) > 0) {
      lcaCfpModel.setMCountryCd(Constants.COUNTRY_CD_JAPAN);
    }

    // 2-3 素材電力排出エネルギー比率
    // 2-4 素材電力排出電力原単位
    UnitElectricModel unitElectricModel = new UnitElectricModel();
    if (lcaCfpModel.getMCountryCd() != "" && lcaCfpModel.getMCountryCd() != null) {

      // 原単位_電力テーブル(unitelectric)を検索
      unitElectricModel = unitJdbc.getUnitElectric(Constants.COUNTRY_CD_JAPAN);
      BigDecimal electricEnergyRatio = unitElectricModel.getElectricEnergyRatio();
      electricEnergyRatio = electricEnergyRatio.multiply(BIGDECLIMAL_HUNDRED);

      BigDecimal electricBaseUnit = unitElectricModel.getElectricBaseUnit();
      lcaCfpModel.setMEnergyRate(electricEnergyRatio.intValue());
      lcaCfpModel.setMElectricBaseUnit(electricBaseUnit);
      // 3-2 加工消費電力エネルギー比率
      // 3-2 加工消費電力原単位
      lcaCfpModel.setPEngyRate(electricEnergyRatio.intValue());
      lcaCfpModel.setPElectricBaseUnit(electricBaseUnit);
    }

    // 素材リサイクル分類分類不可
    BigDecimal mUnclassifiable = BigDecimal.ZERO;
    // 2-5 素材重量計算歩留り率
    BigDecimal bMYieldRate = BigDecimal.ZERO;
    // 2-6 素材重量計算投入質量
    BigDecimal bMInputWeight = BigDecimal.ZERO;
    // 2-7 素材直接排出原単位排出量
    BigDecimal bMBaseUnitEmissions = BigDecimal.ZERO;
    // 2-8 素材直接排出GHG排出
    BigDecimal bMDirectGhg = BigDecimal.ZERO;
    // 2-9 素材電力排出消費電力
    BigDecimal bMPowerConsumption = BigDecimal.ZERO;
    // 2-10 素材電力排出電力GHG排出
    BigDecimal bMElectricGhg = BigDecimal.ZERO;
    // 廃棄物 5-1計算用
    String materialWaste = "";
    if (childParts.getLcaMaterialCd() != "" && childParts.getLcaMaterialCd() != null) {

      // 原単位_材料テーブル(unitmaterials)を検索
      UnitMaterialsModel unitModey = unitJdbc.getUnitMaterials(childParts.getLcaMaterialCd());

      mUnclassifiable = ConverterUtil.toNumber(unitModey.getMaterialRecycleUsageRateDefault());
      mUnclassifiable = mUnclassifiable.multiply(BIGDECLIMAL_HUNDRED);

      // 2-5 素材重量計算歩留り率
      bMYieldRate = ConverterUtil.toNumber(unitModey.getMaterialComponentProcessingYield());
      bMYieldRate = bMYieldRate.multiply(BIGDECLIMAL_HUNDRED);

      // 2-6 素材重量計算投入質量
      if (bMYieldRate.compareTo(BigDecimal.ZERO) > 0) {
        bMInputWeight = bMYieldRate.divide(BIGDECLIMAL_HUNDRED, 10, RoundingMode.HALF_UP);
        bMInputWeight = bTotalMass.divide(bMInputWeight, 10, RoundingMode.HALF_UP);
        bMInputWeight = bMInputWeight.divide(BIGDECLIMAL_THOUSAND);
      } else {
        bMInputWeight = bTotalMass.divide(BIGDECLIMAL_THOUSAND);
      }

      BigDecimal rate0 = ConverterUtil.toNumber(unitModey.getMaterialRecycleUsageRate0());
      BigDecimal rate100 = ConverterUtil.toNumber(unitModey.getMaterialRecycleUsageRate100());
      BigDecimal emissions0 = ConverterUtil.toNumber(unitModey.getMaterialUnitDirectEmissions0());
      BigDecimal emissions100 = ConverterUtil.toNumber(unitModey.getMaterialUnitDirectEmissions100());
      BigDecimal consumption0 = ConverterUtil.toNumber(unitModey.getMaterialPowerConsumption0());
      BigDecimal consumption100 = ConverterUtil.toNumber(unitModey.getMaterialPowerConsumption100());

      // 2-7 素材直接排出原単位排出量
      bMBaseUnitEmissions = linearInterpolationCalc(rate0, rate100, emissions0, emissions100, mUnclassifiable);

      // 2-8 素材直接排出GHG排出
      bMDirectGhg = bMInputWeight.multiply(bMBaseUnitEmissions).multiply(BIGDECLIMAL_THOUSAND);

      // 2-9 素材電力排出消費電力
      bMPowerConsumption = linearInterpolationCalc(rate0, rate100, consumption0, consumption100, mUnclassifiable);

      // 2-10 素材電力排出電力GHG排出
      bMElectricGhg = bMInputWeight.multiply(ConverterUtil.toNumber(lcaCfpModel.getMElectricBaseUnit()))
          .multiply(bMPowerConsumption).multiply(BIGDECLIMAL_THOUSAND);

      // 廃棄物 5-1計算用
      materialWaste = unitModey.getMaterialWaste();
      if (materialWaste == null) {
        materialWaste = "00";
      }

    }
    lcaCfpModel.setMUnclassifiable(mUnclassifiable.intValue());
    lcaCfpModel.setMTotal(mUnclassifiable.intValue());
    lcaCfpModel.setMYieldRate(bMYieldRate);
    lcaCfpModel.setMInputWeight(bMInputWeight);
    lcaCfpModel.setMBaseUnitEmissions(bMBaseUnitEmissions);

    lcaCfpModel.setMPowerConsumption(bMPowerConsumption);


    // 2-11 素材報告値
    BigDecimal bMReport = BigDecimal.ZERO;
    if (bTotalMass.compareTo(BigDecimal.ZERO) > 0) {
      bMReport = bMDirectGhg.add(bMElectricGhg);
    }
    lcaCfpModel.setMReport(bMReport.toString());
    
    lcaCfpModel.setMDirectGhg(bMDirectGhg);
    lcaCfpModel.setMElectricGhg(bMElectricGhg);

    // 2-12 素材測定方法
    String sMMeasureMethods = "";
    if (bMReport.compareTo(BigDecimal.ZERO) > 0) {
      sMMeasureMethods = Constants.MEASURE_METHOD_KANI;
    }
    lcaCfpModel.setMMeasureMethods(sMMeasureMethods);

    // 3-1 加工生産国コード
    // 3-1 加工製造区分
    if (bTotalMass.compareTo(BigDecimal.ZERO) > 0) {
      lcaCfpModel.setPCountryCd(Constants.COUNTRY_CD_JAPAN);
      lcaCfpModel.setPManufacturingDivision(Constants.CODE_01);
    }

    // 3-3 加工1コード
    // 3-3 加工2コード
    // 3-3 加工3コード
    // 3-3 加工4コード
    StandardPartProcessingModel standardPPModey = new StandardPartProcessingModel();
    if (childParts.getLcaMaterialCd() != "" && childParts.getLcaMaterialCd() != null) {

      // 標準工程_部品加工テーブルテーブル(standardpartprocessing)を検索
      standardPPModey = standardPartProcessingJdbc.get(childParts.getLcaMaterialCd());
      lcaCfpModel.setP1Cd(standardPPModey.getStandardPartProcessing1());
      lcaCfpModel.setP2Cd(standardPPModey.getStandardPartProcessing2());
      lcaCfpModel.setP3Cd(standardPPModey.getStandardPartProcessing3());
      lcaCfpModel.setP4Cd(standardPPModey.getStandardPartProcessing4());
    }

    // 3-4 加工消費電力量
    BigDecimal bPElectricAmount = BigDecimal.ZERO;
    // 3-4 加工消費エネルギー量A重油
    BigDecimal bPCrudeOilA = BigDecimal.ZERO;
    // 3-4 加工消費エネルギー量C重油
    BigDecimal bPCrudeOilC = BigDecimal.ZERO;
    // 3-4 加工消費エネルギー量灯油
    BigDecimal bPKerosene = BigDecimal.ZERO;
    // 3-4 加工消費軽油
    BigDecimal bPDiesel = BigDecimal.ZERO;
    // 3-4 加工消費エネルギー量ガソリン
    BigDecimal bPGasoline = BigDecimal.ZERO;
    // 3-4 加工消費エネルギー量NGL
    BigDecimal bPNGL = BigDecimal.ZERO;
    // 3-4 加工消費エネルギー量LPG
    BigDecimal bPLPG = BigDecimal.ZERO;
    // 3-4 加工消費エネルギー量LNG
    BigDecimal bPLNG = BigDecimal.ZERO;
    // 3-4 加工消費エネルギー量都市ガス
    BigDecimal bPCityGus = BigDecimal.ZERO;
    // 3-4 加工消費エネルギー量都市フリー1
    BigDecimal bPFree1 = BigDecimal.ZERO;
    // 3-4 加工消費エネルギー量都市フリー2
    BigDecimal bPFree2 = BigDecimal.ZERO;

    // 3-4 加工1コードより関連データ取得
    UnitPartProcessingModel cd1UnitPartProcessing = new UnitPartProcessingModel();
    if (lcaCfpModel.getP1Cd() != "" && lcaCfpModel.getP1Cd() != null) {

      // 原単位_部品加工テーブルテーブル(standardpartprocessing)を検索
      cd1UnitPartProcessing = unitJdbc.getUnitPartProcessing(lcaCfpModel.getP1Cd());
      bPElectricAmount = bPElectricAmount.add(cd1UnitPartProcessing.getPartProcessingElectric());
      bPCrudeOilA = bPCrudeOilA.add(cd1UnitPartProcessing.getPartProcessingCrudeoilA());
      bPCrudeOilC = bPCrudeOilC.add(cd1UnitPartProcessing.getPartProcessingCrudeoilC());
      bPKerosene = bPKerosene.add(cd1UnitPartProcessing.getPartProcessingKerosene());
      bPDiesel = bPDiesel.add(cd1UnitPartProcessing.getPartProcessingDiesel());
      bPGasoline = bPGasoline.add(cd1UnitPartProcessing.getPartProcessingGasoline());
      bPNGL = bPNGL.add(cd1UnitPartProcessing.getPartProcessingNGL());
      bPLPG = bPLPG.add(cd1UnitPartProcessing.getPartProcessingLPG());
      bPLNG = bPLNG.add(cd1UnitPartProcessing.getPartProcessingLNG());
      bPCityGus = bPCityGus.add(cd1UnitPartProcessing.getPartProcessingCityGus());
      bPFree1 = bPFree1.add(cd1UnitPartProcessing.getPartProcessingFree1());
      bPFree2 = bPFree2.add(cd1UnitPartProcessing.getPartProcessingFree2());

    }
    // 3-4 加工2コードより関連データ取得
    UnitPartProcessingModel cd2UnitPartProcessing = new UnitPartProcessingModel();
    if (lcaCfpModel.getP2Cd() != "" && lcaCfpModel.getP2Cd() != null) {

      // 原単位_部品加工テーブルテーブル(standardpartprocessing)を検索
      cd2UnitPartProcessing = unitJdbc.getUnitPartProcessing(lcaCfpModel.getP2Cd());
      bPElectricAmount = bPElectricAmount.add(cd2UnitPartProcessing.getPartProcessingElectric());
      bPCrudeOilA = bPCrudeOilA.add(cd2UnitPartProcessing.getPartProcessingCrudeoilA());
      bPCrudeOilC = bPCrudeOilC.add(cd2UnitPartProcessing.getPartProcessingCrudeoilC());
      bPKerosene = bPKerosene.add(cd2UnitPartProcessing.getPartProcessingKerosene());
      bPDiesel = bPDiesel.add(cd2UnitPartProcessing.getPartProcessingDiesel());
      bPGasoline = bPGasoline.add(cd2UnitPartProcessing.getPartProcessingGasoline());
      bPNGL = bPNGL.add(cd2UnitPartProcessing.getPartProcessingNGL());
      bPLPG = bPLPG.add(cd2UnitPartProcessing.getPartProcessingLPG());
      bPLNG = bPLNG.add(cd2UnitPartProcessing.getPartProcessingLNG());
      bPCityGus = bPCityGus.add(cd2UnitPartProcessing.getPartProcessingCityGus());
      bPFree1 = bPFree1.add(cd2UnitPartProcessing.getPartProcessingFree1());
      bPFree2 = bPFree2.add(cd2UnitPartProcessing.getPartProcessingFree2());

    }
    // 3-4 加工3コードより関連データ取得
    UnitPartProcessingModel cd3UnitPartProcessing = new UnitPartProcessingModel();
    if (lcaCfpModel.getP3Cd() != "" && lcaCfpModel.getP3Cd() != null) {

      // 原単位_部品加工テーブルテーブル(standardpartprocessing)を検索
      cd3UnitPartProcessing = unitJdbc.getUnitPartProcessing(lcaCfpModel.getP3Cd());
      bPElectricAmount = bPElectricAmount.add(cd3UnitPartProcessing.getPartProcessingElectric());
      bPCrudeOilA = bPCrudeOilA.add(cd3UnitPartProcessing.getPartProcessingCrudeoilA());
      bPCrudeOilC = bPCrudeOilC.add(cd3UnitPartProcessing.getPartProcessingCrudeoilC());
      bPKerosene = bPKerosene.add(cd3UnitPartProcessing.getPartProcessingKerosene());
      bPDiesel = bPDiesel.add(cd3UnitPartProcessing.getPartProcessingDiesel());
      bPGasoline = bPGasoline.add(cd3UnitPartProcessing.getPartProcessingGasoline());
      bPNGL = bPNGL.add(cd3UnitPartProcessing.getPartProcessingNGL());
      bPLPG = bPLPG.add(cd3UnitPartProcessing.getPartProcessingLPG());
      bPLNG = bPLNG.add(cd3UnitPartProcessing.getPartProcessingLNG());
      bPCityGus = bPCityGus.add(cd3UnitPartProcessing.getPartProcessingCityGus());
      bPFree1 = bPFree1.add(cd3UnitPartProcessing.getPartProcessingFree1());
      bPFree2 = bPFree2.add(cd3UnitPartProcessing.getPartProcessingFree2());

    }
    // 3-4 加工4コードより関連データ取得
    UnitPartProcessingModel cd4UnitPartProcessing = new UnitPartProcessingModel();
    if (lcaCfpModel.getP4Cd() != "" && lcaCfpModel.getP4Cd() != null) {

      // 原単位_部品加工テーブルテーブル(standardpartprocessing)を検索
      cd4UnitPartProcessing = unitJdbc.getUnitPartProcessing(lcaCfpModel.getP4Cd());
      bPElectricAmount = bPElectricAmount.add(cd4UnitPartProcessing.getPartProcessingElectric());
      bPCrudeOilA = bPCrudeOilA.add(cd4UnitPartProcessing.getPartProcessingCrudeoilA());
      bPCrudeOilC = bPCrudeOilC.add(cd4UnitPartProcessing.getPartProcessingCrudeoilC());
      bPKerosene = bPKerosene.add(cd4UnitPartProcessing.getPartProcessingKerosene());
      bPDiesel = bPDiesel.add(cd4UnitPartProcessing.getPartProcessingDiesel());
      bPGasoline = bPGasoline.add(cd4UnitPartProcessing.getPartProcessingGasoline());
      bPNGL = bPNGL.add(cd4UnitPartProcessing.getPartProcessingNGL());
      bPLPG = bPLPG.add(cd4UnitPartProcessing.getPartProcessingLPG());
      bPLNG = bPLNG.add(cd4UnitPartProcessing.getPartProcessingLNG());
      bPCityGus = bPCityGus.add(cd4UnitPartProcessing.getPartProcessingCityGus());
      bPFree1 = bPFree1.add(cd4UnitPartProcessing.getPartProcessingFree1());
      bPFree2 = bPFree2.add(cd4UnitPartProcessing.getPartProcessingFree2());

    }

    // 3-4 加工消費電力量
    bPElectricAmount = bPElectricAmount.multiply(bTotalMass).divide(BIGDECLIMAL_THOUSAND);
    lcaCfpModel.setPElectricAmount(bPElectricAmount);
    // 3-4 加工消費エネルギー量A重油
    bPCrudeOilA = bPCrudeOilA.multiply(bTotalMass).divide(BIGDECLIMAL_THOUSAND);
    lcaCfpModel.setPCrudeOilA(bPCrudeOilA);
    // 3-4 加工消費エネルギー量C重油
    bPCrudeOilC = bPCrudeOilC.multiply(bTotalMass).divide(BIGDECLIMAL_THOUSAND);
    lcaCfpModel.setPCrudeOilC(bPCrudeOilC);
    // 3-4 加工消費エネルギー量灯油
    bPKerosene = bPKerosene.multiply(bTotalMass).divide(BIGDECLIMAL_THOUSAND);
    lcaCfpModel.setPKerosene(bPKerosene);
    // 3-4 加工消費エネルギー量軽油
    bPDiesel = bPDiesel.multiply(bTotalMass).divide(BIGDECLIMAL_THOUSAND);
    lcaCfpModel.setPDiesel(bPDiesel);
    // 3-4 加工消費エネルギー量ガソリン
    bPGasoline = bPGasoline.multiply(bTotalMass).divide(BIGDECLIMAL_THOUSAND);
    lcaCfpModel.setPGasoline(bPGasoline);
    // 3-4 加工消費エネルギー量NGL
    bPNGL = bPNGL.multiply(bTotalMass).divide(BIGDECLIMAL_THOUSAND);
    lcaCfpModel.setPNgl(bPNGL);
    // 3-4 加工消費エネルギー量LPG
    bPLPG = bPLPG.multiply(bTotalMass).divide(BIGDECLIMAL_THOUSAND);
    lcaCfpModel.setPLpg(bPLPG);
    // 3-4 加工消費エネルギー量LNG
    bPLNG = bPLNG.multiply(bTotalMass).divide(BIGDECLIMAL_THOUSAND);
    lcaCfpModel.setPLng(bPLNG);
    // 3-4 加工消費エネルギー量都市ガス
    bPCityGus = bPCityGus.multiply(bTotalMass).divide(BIGDECLIMAL_THOUSAND);
    lcaCfpModel.setPCityGus(bPCityGus);
    // 3-4 加工消費エネルギー量フリー1
    bPFree1 = bPFree1.multiply(bTotalMass).divide(BIGDECLIMAL_THOUSAND);
    lcaCfpModel.setPFree1(bPFree1);
    // 3-4 加工消費エネルギー量フリー2
    bPFree2 = bPFree2.multiply(bTotalMass).divide(BIGDECLIMAL_THOUSAND);
    lcaCfpModel.setPFree2(bPFree2);

    // 3-5 加工報告値
    BigDecimal bPreport = BigDecimal.ZERO;
    // 電量
    BigDecimal bEnergyElectricAmount = BigDecimal.ZERO;
    // A重油
    BigDecimal bEnergyCrudeOilA = BigDecimal.ZERO;
    // C重油
    BigDecimal bEnergyCrudeOilC = BigDecimal.ZERO;
    // 灯油
    BigDecimal bEnergyKerosene = BigDecimal.ZERO;
    // 軽油
    BigDecimal bEnergyDiesel = BigDecimal.ZERO;
    // ガソリン
    BigDecimal bEnergyGasoline = BigDecimal.ZERO;
    // 天然ガス液(NGL)
    BigDecimal bEnergyNGL = BigDecimal.ZERO;
    // 液化石油ガス(LPG)
    BigDecimal bEnergyLPG = BigDecimal.ZERO;
    // 天然ｶﾞｽ(LNG)
    BigDecimal bEnergyLNG = BigDecimal.ZERO;
    // 都市ガス
    BigDecimal bEnergyCityGus = BigDecimal.ZERO;
    // Free①
    BigDecimal bEnergyFree1 = BigDecimal.ZERO;
    // Free②
    BigDecimal bEnergyFree2 = BigDecimal.ZERO;

    // 原単位_エネルギーテーブル(unitenergy)を検索 燃料製造
    List<UnitEnergyModel> modelList = unitJdbc.getUnitEnergy();

    for (UnitEnergyModel tmpeModel : modelList) {
      bEnergyCrudeOilA = bEnergyCrudeOilA.add(ConverterUtil.toNumber(tmpeModel.getEnergyCrudeoila()));
      bEnergyCrudeOilC = bEnergyCrudeOilC.add(ConverterUtil.toNumber(tmpeModel.getEnergyCrudeoilc()));
      bEnergyKerosene = bEnergyKerosene.add(ConverterUtil.toNumber(tmpeModel.getEnergyKerosene()));
      bEnergyDiesel = bEnergyDiesel.add(ConverterUtil.toNumber(tmpeModel.getEnergyDiesel()));
      bEnergyGasoline = bEnergyGasoline.add(ConverterUtil.toNumber(tmpeModel.getEnergyGasoline()));
      bEnergyNGL = bEnergyNGL.add(ConverterUtil.toNumber(tmpeModel.getEnergyNgl()));
      bEnergyLPG = bEnergyLPG.add(ConverterUtil.toNumber(tmpeModel.getEnergyLpg()));
      bEnergyLNG = bEnergyLNG.add(ConverterUtil.toNumber(tmpeModel.getEnergyLng()));
      bEnergyCityGus = bEnergyCityGus.add(ConverterUtil.toNumber(tmpeModel.getEnergyCitygus()));
      bEnergyFree1 = bEnergyFree1.add(ConverterUtil.toNumber(tmpeModel.getEnergyFree1()));
      bEnergyFree2 = bEnergyFree2.add(ConverterUtil.toNumber(tmpeModel.getEnergyFree2()));

    }

    // 電量
    bEnergyElectricAmount = bPElectricAmount.multiply(ConverterUtil.toNumber(lcaCfpModel.getPElectricBaseUnit()));
    // A重油
    bEnergyCrudeOilA = bEnergyCrudeOilA.multiply(bPCrudeOilA);
    // C重油
    bEnergyCrudeOilC = bEnergyCrudeOilC.multiply(bPCrudeOilC);
    // 灯油
    bEnergyKerosene = bEnergyKerosene.multiply(bPKerosene);
    // 軽油
    bEnergyDiesel = bEnergyDiesel.multiply(bPDiesel);
    // ガソリン
    bEnergyGasoline = bEnergyGasoline.multiply(bPGasoline);
    // 天然ガス液(NGL)
    bEnergyNGL = bEnergyNGL.multiply(bPNGL);
    // 液化石油ガス(LPG)
    bEnergyLPG = bEnergyLPG.multiply(bPLPG);
    // 天然ｶﾞｽ(LNG)
    bEnergyLNG = bEnergyLNG.multiply(bPLNG);
    // 都市ガス
    bEnergyCityGus = bEnergyCityGus.multiply(bPCityGus);
    // Free①
    bEnergyFree1 = bEnergyFree1.multiply(bPFree1);
    // Free②
    bEnergyFree2 = bEnergyFree2.multiply(bPFree2);

    // 3-5 加工報告値
    bPreport = bEnergyElectricAmount.add(bEnergyCrudeOilA).add(bEnergyCrudeOilC).add(bEnergyKerosene).add(bEnergyDiesel)
        .add(bEnergyGasoline).add(bEnergyNGL).add(bEnergyLPG).add(bEnergyLNG).add(bEnergyCityGus).add(bEnergyFree1)
        .add(bEnergyFree2).multiply(BIGDECLIMAL_THOUSAND);
    lcaCfpModel.setPReport(bPreport.toString());
    // 3-6 加工測定方法
    if (bPreport.compareTo(BigDecimal.ZERO) > 0) {
      lcaCfpModel.setPMeasureMethods(Constants.MEASURE_METHOD_KANI);
    }

    // 4-1 資材報告値
    lcaCfpModel.setRReport("0");

    // 5-1 廃棄物質量
    // 廃棄物質量
    BigDecimal bWasteWeight = BigDecimal.ZERO;
    bWasteWeight = bTotalMass.divide(BIGDECLIMAL_THOUSAND);
    bWasteWeight = bMInputWeight.subtract(bWasteWeight);

    if (childParts.getLcaMaterialCd() != "" && childParts.getLcaMaterialCd() != null) {

      switch (materialWaste) {
        case "01" :
          // 燃え殻
          lcaCfpModel.setWAsh(bWasteWeight);
          break;
        case "02" :
          // 鉱業等無機性汚泥
          lcaCfpModel.setWInorganicSludgeMining(bWasteWeight);
          break;
        case "03" :
          // 製造業有機性汚泥
          lcaCfpModel.setWOrganicSludgeManufacturing(bWasteWeight);
          break;
        case "04" :
          // 製造排出廃プラスチック
          lcaCfpModel.setWWastePlasticsManufacturing(bWasteWeight);
          break;
        case "05" :
          // 金属くず
          lcaCfpModel.setWMetalScrap(bWasteWeight);
          break;
        case "06" :
          // 廃棄物陶磁器くず
          lcaCfpModel.setWCeramicScrap(bWasteWeight);
          break;
        case "07" :
          // 廃棄物鉱さい
          lcaCfpModel.setWSlag(bWasteWeight);
          break;
        case "08" :
          // 廃棄物ばいじん
          lcaCfpModel.setWDust(bWasteWeight);
          break;
        case "09" :
          // 廃棄物石油由来廃油
          lcaCfpModel.setWWasteOilFromPetroleum(bWasteWeight);
          break;
        case "10" :
          // 廃棄物天然繊維くず
          lcaCfpModel.setWNaturalFiberScrap(bWasteWeight);
          break;
        case "11" :
          // 廃棄物ゴムくず
          lcaCfpModel.setWRubberScrap(bWasteWeight);
          break;
        case "12" :
          // 廃棄物廃酸
          lcaCfpModel.setWWasteAcid(bWasteWeight);
          break;
        case "13" :
          // 廃棄物廃アルカリ
          lcaCfpModel.setWWasteAlkali(bWasteWeight);
          break;
        default :
      }

      // 5-2 廃棄物報告値
      BigDecimal bWReport = BigDecimal.ZERO;
      if (bTotalMass.compareTo(BigDecimal.ZERO) > 0) {
        if (materialWaste != "00") {
          UnitWasteModel unitWasteModel = unitJdbc.getUnitWaste(materialWaste);
          bWReport = bWasteWeight.multiply(unitWasteModel.getWasteCo2Unit()).multiply(BIGDECLIMAL_THOUSAND);
          lcaCfpModel.setWReport(bWReport.toString());
        }
      }

      // 5-3 廃棄物測定方法
      if (bWReport.compareTo(BigDecimal.ZERO) > 0) {
        lcaCfpModel.setWMeasureMethods(Constants.MEASURE_METHOD_KANI);
      }
    }

    // 6-1
    // 輸送燃料材料排出量
    lcaCfpModel.setTFuelMaterialEmissions(BigDecimal.ZERO);
    // 輸送燃料部品排出量
    lcaCfpModel.setTFuelPartEmissions(BigDecimal.ZERO);
    // 輸送燃費材料排出量
    lcaCfpModel.setTFuelEconomyMaterialEmissions(BigDecimal.ZERO);
    // 輸送燃費部品排出量
    lcaCfpModel.setTFuelEconomyPartEmissions(BigDecimal.ZERO);
    // 輸送トンキロ材料排出量
    lcaCfpModel.setTTonKgMaterialEmissions(BigDecimal.ZERO);
    // 輸送トンキロ部品排出量
    lcaCfpModel.setTTonKgPartEmissions(BigDecimal.ZERO);

    // 輸送重量材料投入質量
    lcaCfpModel.setTWeightMaterialInput(bMInputWeight);
    // 輸送重量部品合計質量
    BigDecimal bTWeightPartTotal = BigDecimal.ZERO;
    if (childParts.getLcaMaterialCd() != "" && childParts.getLcaMaterialCd() != null) {
      bTWeightPartTotal = bTotalMass.divide(BIGDECLIMAL_THOUSAND);
    }
    lcaCfpModel.setTWeightPartTotal(bTWeightPartTotal);

    // 輸送重量材料排出量
    BigDecimal tWeightMaterialEmissions = BigDecimal.ZERO;
    // 輸送重量部品排出量
    BigDecimal tWeightPartEmissions = BigDecimal.ZERO;

    // 原単位_輸送_重量法テーブル(unittransportweight)を検索
    List<UnitTransportWeightModel> list = unitJdbc.getUnitTransportWeight();

    for (UnitTransportWeightModel model : list) {

      if (Constants.CODE_01.equals(model.getWeightCd())) {
        if (bMInputWeight.compareTo(BigDecimal.ZERO) > 0) {
          tWeightMaterialEmissions = model.getWeightTransport().multiply(bMInputWeight).multiply(BIGDECLIMAL_THOUSAND);
        }

      } else if (Constants.CODE_02.equals(model.getWeightCd())) {
        if (bTWeightPartTotal.compareTo(BigDecimal.ZERO) > 0) {
          tWeightPartEmissions = model.getWeightTransport().multiply(bTWeightPartTotal).multiply(BIGDECLIMAL_THOUSAND);
        }
      }

      lcaCfpModel.setTWeightMaterialEmissions(tWeightMaterialEmissions);
      lcaCfpModel.setTWeightPartEmissions(tWeightPartEmissions);
    }

    // 輸送材料報告値
    BigDecimal tMaterialReport = BigDecimal.ZERO;
    if (bTotalMass.compareTo(BigDecimal.ZERO) > 0) {
      if (tWeightMaterialEmissions.compareTo(BigDecimal.ZERO) > 0) {
        tMaterialReport = tWeightMaterialEmissions;
      }
    }
    lcaCfpModel.setTMaterialReport(tMaterialReport.toString());

    // 輸送部品報告値
    BigDecimal tPartReport = BigDecimal.ZERO;
    if (bTotalMass.compareTo(BigDecimal.ZERO) > 0) {
      if (tWeightPartEmissions.compareTo(BigDecimal.ZERO) > 0) {
        tPartReport = tWeightPartEmissions;
      }
    }
    lcaCfpModel.setTPartReport(tPartReport.toString());

    // 輸送測定方法
    String tMeasureMethods = "";
    if (tMaterialReport.add(tPartReport).compareTo(BigDecimal.ZERO) > 0) {
      if (tWeightMaterialEmissions.add(tWeightPartEmissions).compareTo(BigDecimal.ZERO) > 0) {
        tMeasureMethods = Constants.MEASURE_METHOD_KANI;
      } else {
        tMeasureMethods = Constants.MEASURE_METHOD_JISOKU;
      }
    }
    lcaCfpModel.setTMeasureMethods(tMeasureMethods);

  }

  /**
   * 線形補完計算
   * 
   * @param rate0
   *          [リサイクル材使用率_0%狙い(unitMaterialsModel.materialRecycleUsageRate0)]
   * @param rate100
   *          [リサイクル材使用率_100%狙い(unitMaterialsModel.materialRecycleUsageRate100)]
   * @param zero
   *          [素材原単位_直接排出_0%狙い(unitMaterialsModel.materialUnitDirectEmissions0]
   *          [素材_消費電力_0%狙い(unitMaterialsModel.materialPowerConsumption0)]
   * @param hundred
   *          [素材原単位_直接排出_0%狙い(unitMaterialsModel.materialUnitDirectEmissions0]
   *          [素材_消費電力_100%狙い(unitMaterialsModel.materialPowerConsumption100)]
   * @param mTotal
   *          [リサイクル分類 合計]
   * @return
   */
  private BigDecimal linearInterpolationCalc(BigDecimal rate0, BigDecimal rate100, BigDecimal zero, BigDecimal hundred,
      BigDecimal mTotal) {

    // ③-(③-④)/(② * 100 -①)×[リサイクル分類 合計]（四捨五入して少数点以下３桁）
    BigDecimal result = BigDecimal.ZERO;
    BigDecimal calc1 = zero.subtract(hundred);
    rate100 = rate100.multiply(BIGDECLIMAL_HUNDRED);
    BigDecimal calc2 = rate100.subtract(rate0);
    BigDecimal calc3 = calc1;
    if (calc2.compareTo(BigDecimal.ZERO) != 0) {
      calc3 = calc1.divide(calc2);
    }
    BigDecimal calc4 = calc3.multiply(mTotal);
    result = zero.subtract(calc4);
    return result;
  }

}
