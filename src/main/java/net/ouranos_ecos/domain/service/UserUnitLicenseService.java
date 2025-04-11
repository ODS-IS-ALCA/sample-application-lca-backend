package net.ouranos_ecos.domain.service;

import java.util.ArrayList;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.ouranos_ecos.AppConfig;
import net.ouranos_ecos.domain.common.ConnectionUtil;
import net.ouranos_ecos.domain.common.Constants;
import net.ouranos_ecos.domain.common.ErrorUtil;
import net.ouranos_ecos.domain.model.CfpResponseTransModel;
import net.ouranos_ecos.domain.model.UnitDbCertificationModel;
import net.ouranos_ecos.domain.repository.jdbc.RequestJdbc;

/**
 * UserUnitLicenseServiceクラス。
 */
@Service
public class UserUnitLicenseService {

  @Inject
  ObjectMapper mapper;

  @Inject
  RequestJdbc jdbc;

  @Autowired
  private AppConfig appConfig;

  public ResponseEntity<byte[]> getRequest(HttpServletRequest request) {

    ResponseEntity<byte[]> responseEntity = null;

    try {

      String operatorId = request.getParameter("operatorId");

      if (operatorId == null || "".equals(operatorId)) {
        // 内部事業者識別子が取得できない場合
        return ErrorUtil.badRequestResponse(mapper, "", request.getMethod());
      }

      UnitDbCertificationModel unitDModel = new UnitDbCertificationModel();

      if (ConnectionUtil.isLocalEnvironment(request)) {
        // ローカル環境の場合は、【1：成功】を返却し処理を終了
        unitDModel.setResult(Constants.UNITDB_CERTIFICATION_RESULT_SUCCESS);
        // レスポンスを返却
        byte[] json = mapper.writeValueAsBytes(unitDModel);
        responseEntity = ResponseEntity.status(HttpStatus.OK).headers(new HttpHeaders()).body(json);
        return responseEntity;
      }

      // 使用者ライセンス認証API(TRUE)場合、フロントに【1：成功】を返却し処理を終了
      if (ConnectionUtil.execPrimaryUserLicenseApi(appConfig, operatorId)) {
        unitDModel.setResult(Constants.UNITDB_CERTIFICATION_RESULT_SUCCESS);
      } else {
        // Httpステータス：200 or 201ではない場合、準使用者ライセンス認証API（原単位DBシステム）を実行
        List<CfpResponseTransModel> cfpRTModelList = jdbc.selectRequestIdList(operatorId);
        // 依頼識別子が１件もない場合はフロントに【３：失敗】を返却し処理を終了
        if (cfpRTModelList.size() == 0) {
          unitDModel.setResult(Constants.UNITDB_CERTIFICATION_RESULT_FAILURE);
        } else {
          Boolean bSucccess = false;
          Boolean bClauseNoAgree = false;
          List<String> subUserIdList = new ArrayList<String>();
          List<String> tradeIdList = new ArrayList<String>();
          for (CfpResponseTransModel model : cfpRTModelList) {
            UnitDbCertificationModel unitDbCertificationModel = ConnectionUtil.execSubUserLicenseApi(appConfig, mapper,
                operatorId, model.getRequestId());
            // 【1：成功】を返却した場合
            if (Constants.UNITDB_CERTIFICATION_RESULT_SUCCESS.equals(unitDbCertificationModel.getResult())) {
              bSucccess = true;
            }
            // 【２：約款同意】を返却した場合
            if (Constants.UNITDB_CERTIFICATION_RESULT_CLAUSE_NO_AGREE.equals(unitDbCertificationModel.getResult())) {
              bClauseNoAgree = true;
              subUserIdList.add(unitDbCertificationModel.getSubUserIdList().get(0));
              tradeIdList.add(unitDbCertificationModel.getTradeIdList().get(0));
            }
          }

          // Httpステータス：415が1件以上返却された場合、【２：約款同意】を返却し処理を終了
          if (bClauseNoAgree) {
            unitDModel.setResult(Constants.UNITDB_CERTIFICATION_RESULT_CLAUSE_NO_AGREE);
            unitDModel.setOperatorId(operatorId);
            unitDModel.setSubUserIdList(subUserIdList);
            unitDModel.setTradeIdList(tradeIdList);
          } else {
            // Httpステータス：415がなく、200 or 201が1件以上返却された場合、【１：成功】を返却し処理を終了
            if (bSucccess) {
              unitDModel.setResult(Constants.UNITDB_CERTIFICATION_RESULT_SUCCESS);
            } else {
              // フロントに【３：失敗】を返却し処理を終了
              unitDModel.setResult(Constants.UNITDB_CERTIFICATION_RESULT_FAILURE);
            }
          }
        }
      }

      // レスポンスを返却
      byte[] json = mapper.writeValueAsBytes(unitDModel);
      responseEntity = ResponseEntity.status(HttpStatus.OK).headers(new HttpHeaders()).body(json);

    } catch (Exception e) {
      e.printStackTrace();
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, "", request.getMethod());
    }

    return responseEntity;
  }
}