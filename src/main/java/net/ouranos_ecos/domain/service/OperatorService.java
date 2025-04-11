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
import net.ouranos_ecos.domain.model.OperatorModel;
import net.ouranos_ecos.domain.repository.jdbc.OperatorJdbc;

/**
 * OperatorServiceクラス。
 */
@Service
@Slf4j
public class OperatorService {

  @Inject
  OperatorJdbc operatorJdbc;

  @Inject
  ObjectMapper mapper;

  @Transactional
  public ResponseEntity<byte[]> getRequest(HttpServletRequest request) {

    List<OperatorModel> operatorlist = null;

    ResponseEntity<byte[]> responseEntity = null;

    try {
      // 事業者情報取得
      operatorlist = operatorJdbc.get();

      // JSON変換
      byte[] json = null;

      if (operatorlist.isEmpty()) {
        // 取得結果がない場合は空で返す
        json = mapper.writeValueAsBytes("");
      } else {
        // JSON文字列に変換
        json = mapper.writeValueAsBytes(operatorlist);
      }

      responseEntity = ResponseEntity.status(HttpStatus.OK).headers(new HttpHeaders()).body(json);

    } catch (Exception e) {
      log.warn("予期せぬエラー", e);
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, "", request.getMethod());
    }
    return responseEntity;
  }

}
