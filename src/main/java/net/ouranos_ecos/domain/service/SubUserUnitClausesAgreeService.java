package net.ouranos_ecos.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.ouranos_ecos.AppConfig;
import net.ouranos_ecos.domain.common.ConnectionUtil;
import net.ouranos_ecos.domain.common.Constants;
import net.ouranos_ecos.domain.common.ErrorUtil;
import net.ouranos_ecos.domain.model.UnitDbCertificationModel;
import net.ouranos_ecos.domain.model.UnitDbSubUserClausesAgreeModel;
import net.ouranos_ecos.domain.repository.jdbc.RequestJdbc;

/**
 * SubUserUnitClausesAgreeServiceクラス。
 */
@Service
@Slf4j
public class SubUserUnitClausesAgreeService {

  @Inject
  ObjectMapper mapper;

  @Inject
  RequestJdbc jdbc;

  @Autowired
  private AppConfig appConfig;

  @Transactional
  public ResponseEntity<byte[]> postRequest(HttpServletRequest request) {

    ResponseEntity<byte[]> responseEntity = null;

    // modelクラスの初期化
    UnitDbSubUserClausesAgreeModel unitRequestModel = new UnitDbSubUserClausesAgreeModel();
    UnitDbCertificationModel unitDModel = new UnitDbCertificationModel();

    try {
      // リクエスト内容の取得
      String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
      // リクエスト内容を対応するクラスに格納
      unitRequestModel = mapper.readValue(requestBody, UnitDbSubUserClausesAgreeModel.class);

      // 子ユーザーIDリスト
      List<String> subUserIdList = unitRequestModel.getSubUserIdList();
      if (subUserIdList.isEmpty()) {
        // 取得できない場合
        return ErrorUtil.badRequestResponse(mapper, "", request.getMethod());
      }
      // トレース識別子リスト
      List<String> subTradeIdList = unitRequestModel.getTradeIdList();
      if (subTradeIdList.isEmpty()) {
        // 取得できない場合
        return ErrorUtil.badRequestResponse(mapper, "", request.getMethod());
      }

      Boolean bSubUserLicenseSucccess = false;
      for (int i = 0; i < subUserIdList.size(); i++) {
        // 準使用者情報更新API（原単位DBシステム）を実行
        UnitDbCertificationModel unitDbCertificationModel = ConnectionUtil.execSubUserInfoUpdataApi(appConfig,
            subUserIdList.get(i));
        // 【1：成功】を返却した場合
        if (Constants.UNITDB_CERTIFICATION_RESULT_SUCCESS.equals(unitDbCertificationModel.getResult())) {
          // 準使用者ライセンス認証API（原単位DBシステム）を実行
          unitDbCertificationModel = ConnectionUtil.execSubUserLicenseApi(appConfig, mapper,
              unitRequestModel.getOperatorId(), subTradeIdList.get(i));
          // 【1：成功】を返却した場合
          if (Constants.UNITDB_CERTIFICATION_RESULT_SUCCESS.equals(unitDbCertificationModel.getResult())) {
            bSubUserLicenseSucccess = true;
          }
        }
      }

      if (bSubUserLicenseSucccess) {
        // 【１：成功】を返却し処理を終了
        unitDModel.setResult(Constants.UNITDB_CERTIFICATION_RESULT_SUCCESS);
      } else {
        // フロントに【３：失敗】を返却し処理を終了
        unitDModel.setResult(Constants.UNITDB_CERTIFICATION_RESULT_FAILURE);
      }

      // レスポンスを返却
      byte[] json = mapper.writeValueAsBytes(unitDModel);
      responseEntity = ResponseEntity.status(HttpStatus.OK).headers(new HttpHeaders()).body(json);

    } catch (Exception e) {
      log.warn("予期せぬエラー", e);
      e.printStackTrace();
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, "", request.getMethod());
    }

    return responseEntity;
  }

}