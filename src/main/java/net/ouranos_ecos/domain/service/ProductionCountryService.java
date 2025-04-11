package net.ouranos_ecos.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.ouranos_ecos.domain.common.ErrorUtil;
import net.ouranos_ecos.domain.model.ProductionCountryModel;
import net.ouranos_ecos.domain.repository.jdbc.ProductionCountryJdbc;

/**
 * ProductionCountryServiceクラス
 */
@Service
@Slf4j
public class ProductionCountryService {

  @Inject
  ProductionCountryJdbc productionCountryJdbc;

  @Inject
  ObjectMapper mapper;

  @Transactional
  public ResponseEntity<byte[]> getRequest(HttpServletRequest request) {

    ResponseEntity<byte[]> responseEntity = null;

    List<ProductionCountryModel> modelList = new ArrayList<ProductionCountryModel>();

    try {
      // DB接続
      List<Map<String, Object>> resultList = productionCountryJdbc.get();

      // JSON変換
      byte[] json = null;

      if (resultList.isEmpty()) {
        // 取得結果がない場合はエラーメッセージを返す
        json = mapper.writeValueAsBytes("取得に失敗しました。");
        responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).headers(new HttpHeaders()).body(json);
        return responseEntity;
      }

      ProductionCountryModel modelBrank = new ProductionCountryModel();
      modelBrank.setProductionCountryCd("");
      modelBrank.setProductionCountryName("");
      modelList.add(modelBrank);

      for (Map<String, Object> result : resultList) {
        ProductionCountryModel model = new ProductionCountryModel();
        model.setProductionCountryCd((String) result.get("productioncountrycd"));
        model.setProductionCountryName((String) result.get("productioncountryname"));
        modelList.add(model);
      }

      // JSON文字列に変換
      json = mapper.writeValueAsBytes(modelList);
      responseEntity = ResponseEntity.status(HttpStatus.OK).headers(new HttpHeaders()).body(json);

    } catch (Exception e) {
      log.warn("予期せぬエラー", e);
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, "", request.getMethod());
    }

    return responseEntity;
  }

}