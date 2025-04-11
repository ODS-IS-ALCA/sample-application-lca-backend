package net.ouranos_ecos.domain.common;

import java.sql.Timestamp;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.ouranos_ecos.exception_handler.ApiDataDistributionErrorResponse;

/**
 * エラー関連の共通処理クラス
 */
@Component
public class ErrorUtil {

  /**
   * エラーレスポンス返却　(500エラー)
   * システムの内部にてエラーが発生している場合
   * 
   * @param id
   * @param method
   * @return
   */
  public static ResponseEntity<byte[]> internalServerErrorResponse(ObjectMapper mapper, String id, String method) {

    ApiDataDistributionErrorResponse apiErrRes = new ApiDataDistributionErrorResponse("InternalServerError",
        "Unexpected error occurred",
        "id: " + id + " , timeStamp: " + new Timestamp(System.currentTimeMillis()) + ", method: " + method);

    byte[] json = null;

    try {
      json = mapper.writeValueAsBytes(apiErrRes);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(new HttpHeaders()).body(json);
  }

  /**
   * エラーレスポンス返却　(400エラー)
   * リクエスト自体に問題がある場合
   * 
   * @param id
   * @param method
   * @return
   */
  public static ResponseEntity<byte[]> badRequestResponse(ObjectMapper mapper, String id, String method) {

    ApiDataDistributionErrorResponse apiErrRes = new ApiDataDistributionErrorResponse("BadRequest",
        "Invalid request parameters, dataTarget: Unexpected query parameter",
        "id: " + id + " , timeStamp: " + new Timestamp(System.currentTimeMillis()) + ", method: " + method);

    byte[] json = null;

    try {
      json = mapper.writeValueAsBytes(apiErrRes);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(new HttpHeaders()).body(json);
  }

  /**
   * エラーレスポンス返却　(400エラー)
   * リクエスト自体に問題がある場合(エラーメッセージを自定義)
   * 
   * @param mapper
   * @param message
   * @return
   */
  public static ResponseEntity<byte[]> badRequestResponse(ObjectMapper mapper, String message) {

    ApiDataDistributionErrorResponse apiErrRes = new ApiDataDistributionErrorResponse("BadRequest", message,
        "timeStamp: " + new Timestamp(System.currentTimeMillis()));

    byte[] json = null;

    try {
      json = mapper.writeValueAsBytes(apiErrRes);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(new HttpHeaders()).body(json);
  }

  /**
   * エラーレスポンス返却　(404エラー)
   * 要求されたリソースが存在しない場合
   * 
   * @param id
   * @param method
   * @return
   */
  public static ResponseEntity<byte[]> notFoundResponse(ObjectMapper mapper, String id, String method) {

    ApiDataDistributionErrorResponse apiErrRes = new ApiDataDistributionErrorResponse("NotFound", "Endpoint Not Found",
        "id: " + id + " , timeStamp: " + new Timestamp(System.currentTimeMillis()) + ", method: " + method);

    byte[] json = null;

    try {
      json = mapper.writeValueAsBytes(apiErrRes);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(new HttpHeaders()).body(json);
  }
}
