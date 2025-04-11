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
import net.ouranos_ecos.domain.model.ResponseProductModel;
import net.ouranos_ecos.domain.repository.jdbc.ResponseProductJdbc;

/**
 * ResponseProductServiceクラス。
 */
@Service
@Slf4j
public class ResponseProductService {

  @Inject
  ResponseProductJdbc responseProductJdbc;

  @Inject
  ObjectMapper mapper;

  @Transactional
  public ResponseEntity<byte[]> getRequest(HttpServletRequest request) {

    ResponseEntity<byte[]> responseEntity = null;

    // トレース識別子を取得
    String operatorid = request.getParameter("operatorId");

    try {
      if (operatorid == null || "".equals(operatorid)) {
        // Queryパラメータが取得できない場合
        return ErrorUtil.badRequestResponse(mapper, "", request.getMethod());
      }
      // リミットを取得（表示する最大件数）
      int limit = Integer.parseInt(request.getParameter("limit"));
      // afterを取得（前回リストの最後の更新日）
      String after = request.getParameter("after");

      // パラメータで部品情報Tを取得する。
      List<ResponseProductModel> productModelList = responseProductJdbc.select(operatorid, limit, after);

      HttpHeaders headers = new HttpHeaders();
      if (productModelList.size() == limit) {
        // 100件取得した場合、最後のレコードの更新日を使って次があるか確認する。
        ResponseProductModel lastProduct = productModelList.get(productModelList.size() - 1);
        List<ResponseProductModel> list = responseProductJdbc.select(operatorid, limit, lastProduct.getModifiedAt());
        if (!list.isEmpty()) {
          // 次がある場合、更新日をheaderにセットする。(>ボタンが表示される)
          headers.add("Link", "after=" + lastProduct.getModifiedAt());
        }
      }
      // レスポンスを返却
      byte[] json = mapper.writeValueAsBytes(productModelList);
      responseEntity = ResponseEntity.status(HttpStatus.OK).headers(headers).body(json);

    } catch (Exception e) {
      log.warn("予期せぬエラー", e);
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, operatorid, request.getMethod());
    }

    return responseEntity;
  }
}