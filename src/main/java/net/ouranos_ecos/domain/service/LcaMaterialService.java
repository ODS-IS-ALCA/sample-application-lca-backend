package net.ouranos_ecos.domain.service;

import java.util.List;

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
import net.ouranos_ecos.domain.model.LcaMaterialModel;
import net.ouranos_ecos.domain.repository.jdbc.LcaMaterialJdbc;

/**
 * LcaMaterialServiceクラス。
 */
@Service
@Slf4j
public class LcaMaterialService {

  @Inject
  LcaMaterialJdbc lcaMaterialJdbc;

  @Inject
  ObjectMapper mapper;

  @Transactional
  public ResponseEntity<byte[]> getRequest(HttpServletRequest request) {

    ResponseEntity<byte[]> responseEntity = null;

    // リクエスト内容を取得
    try {
      List<LcaMaterialModel> lcaMaterialList = null;

      // DB接続
      lcaMaterialList = lcaMaterialJdbc.selectLcaMaterial();

      byte[] json = null;

      if (lcaMaterialList.isEmpty()) {
        // 取得結果がない場合は空で返す
        json = mapper.writeValueAsBytes("");
      } else {
        // JSON文字列に変換
        json = mapper.writeValueAsBytes(lcaMaterialList);
      }

      responseEntity = ResponseEntity.status(HttpStatus.OK).headers(new HttpHeaders()).body(json);

    } catch (Exception e) {
      // なんらかのエラーが発生した場合
      log.warn("予期せぬエラー", e);
      e.printStackTrace();
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, "", request.getMethod());
    }

    return responseEntity;
  }

}