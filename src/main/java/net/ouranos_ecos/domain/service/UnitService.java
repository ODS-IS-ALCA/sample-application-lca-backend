package net.ouranos_ecos.domain.service;

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
import net.ouranos_ecos.domain.model.UnitModel;
import net.ouranos_ecos.domain.repository.jdbc.UnitJdbc;

/**
 * UnitServiceクラス。
 */
@Service
@Slf4j
public class UnitService {

  @Inject
  UnitJdbc unitJdbc;

  @Inject
  ObjectMapper mapper;

  @Transactional
  public ResponseEntity<byte[]> getRequest(HttpServletRequest request) {

    ResponseEntity<byte[]> responseEntity = null;

    try {

      // 原単位情報(IDEA)を取得する。
      // 他算定方法でCFP計算する場合には、下記処理を分離し、算定方法に応じたデータセットとの紐づけが必要。
      UnitModel unitModel = unitJdbc.selectUnit();

      // レスポンスを返却
      byte[] json = mapper.writeValueAsBytes(unitModel);
      responseEntity = ResponseEntity.status(HttpStatus.OK).headers(new HttpHeaders()).body(json);

    } catch (Exception e) {
      log.warn("予期せぬエラー", e);
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, "", request.getMethod());
    }

    return responseEntity;
  }
}