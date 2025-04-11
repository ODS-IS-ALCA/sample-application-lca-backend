package net.ouranos_ecos.domain.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.ouranos_ecos.AppConfig;
import net.ouranos_ecos.domain.model.UnitDbCertificationModel;
import net.ouranos_ecos.domain.model.UnitDbClausesAgreeResponseModel;

/**
 * 接続関連の共通処理クラス
 */
@Component
public class ConnectionUtil {

  /**
   * ローカル環境かどうか判定する
   * 
   * @param request
   * @return
   */
  public static boolean isLocalEnvironment(HttpServletRequest request) {

    String remoteAddr = request.getRemoteAddr();
    String serverName = request.getServerName();

    return "127.0.0.1".equals(remoteAddr) || "127.0.0.1".equals(serverName) || "localhost".equalsIgnoreCase(serverName);
  }

  /**
   * 原単位DB_準使用者情報更新API を実行
   * 
   * @param operatorId
   * @return UnitDbCertificationModel
   * @throws IOException
   */
  public static UnitDbCertificationModel execSubUserInfoUpdataApi(AppConfig appConfig, String subUserId)
      throws IOException {

    UnitDbCertificationModel unitDbCModel = new UnitDbCertificationModel();
    // 使用者ライセンス認証APIを実行
    // 送信先のURLを指定
    String strUrl = appConfig.getSubUserInfoUpdateApiUrl() + subUserId;
    URL url = new URL(strUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    // POSTメソッドを指定
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
    connection.setRequestProperty("Accept", "application/json");
    connection.setDoOutput(true);

    // 送信するデータを作成
    String jsonInputString = "{\"agreeStatus\": true}";

    // データを送信
    try (OutputStream os = connection.getOutputStream()) {
      byte[] input = jsonInputString.getBytes("utf-8");
      os.write(input, 0, input.length);
    }

    // レスポンスコードを取得
    int responseCode = connection.getResponseCode();

    // Httpステータス：200 or 201で、accessTokenが返却された場合、【1：成功】を返却
    if (Constants.HTTP_STATUS_NORMAL.equals(String.valueOf(responseCode))
        || Constants.HTTP_STATUS_CREATED.equals(String.valueOf(responseCode))) {
      unitDbCModel.setResult(Constants.UNITDB_CERTIFICATION_RESULT_SUCCESS);
    } else {
      // Httpステータス：200、415以外で、【３：失敗】を返却し処理を終了
      unitDbCModel.setResult(Constants.UNITDB_CERTIFICATION_RESULT_FAILURE);
    }

    // 接続を切断
    connection.disconnect();

    return unitDbCModel;

  }

  /**
   * 原単位DB_使用者ライセンス認証API を実行
   * 
   * @param operatorId
   * @return Boolean
   * @throws IOException
   */
  public static Boolean execPrimaryUserLicenseApi(AppConfig appConfig, String operatorId) throws IOException {

    Boolean runResult = false;
    // 使用者ライセンス認証APIを実行
    // 送信先のURLを指定
    String strUrl = appConfig.getPrimaryUserLicenseApiUrl() + operatorId;
    URL url = new URL(strUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    // POSTメソッドを指定
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
    connection.setRequestProperty("Accept", "application/json");
    connection.setDoOutput(true);

    // レスポンスコードを取得
    int responseCode = connection.getResponseCode();

    // Httpステータス：200 or 201で、accessTokenが返却された場合、trueを返却
    if (Constants.HTTP_STATUS_NORMAL.equals(String.valueOf(responseCode))
        || Constants.HTTP_STATUS_CREATED.equals(String.valueOf(responseCode))) {
      runResult = true;
    }

    // 接続を切断
    connection.disconnect();

    return runResult;

  }

  /**
   * 原単位DB_準使用者ライセンス認証API を実行
   * 
   * @param operatorId
   * @param tradeId
   * @return UnitDbCertificationModel
   * @throws IOException
   */
  public static UnitDbCertificationModel execSubUserLicenseApi(AppConfig appConfig, ObjectMapper mapper,
      String operatorId, String tradeId) throws IOException {

    UnitDbCertificationModel unitDbCModel = new UnitDbCertificationModel();
    // 使用者ライセンス認証APIを実行
    // 送信先のURLを指定
    String strUrl = appConfig.getSubUserLicenseApiUrl() + operatorId;
    URL url = new URL(strUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    // POSTメソッドを指定
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
    connection.setRequestProperty("Accept", "application/json");
    connection.setDoOutput(true);

    // 送信するデータを作成
    String jsonInputString = "{\"tradeId\": \"" + tradeId + "\"}";

    // データを送信
    try (OutputStream os = connection.getOutputStream()) {
      byte[] input = jsonInputString.getBytes("utf-8");
      os.write(input, 0, input.length);
    }

    // レスポンスコードを取得
    int responseCode = connection.getResponseCode();

    List<String> subUserIdList = new ArrayList<String>();
    List<String> tradeIdList = new ArrayList<String>();

    // Httpステータス：200 or 201で、accessTokenが返却された場合、【1：成功】を返却
    if (Constants.HTTP_STATUS_NORMAL.equals(String.valueOf(responseCode))
        || Constants.HTTP_STATUS_CREATED.equals(String.valueOf(responseCode))) {
      unitDbCModel.setResult(Constants.UNITDB_CERTIFICATION_RESULT_SUCCESS);
    } else if (Constants.HTTP_STATUS_NO_CLAUSE.equals(String.valueOf(responseCode))) {
      // Httpステータス：415で、【２：約款同意】を返却し処理を終了
      unitDbCModel.setResult(Constants.UNITDB_CERTIFICATION_RESULT_CLAUSE_NO_AGREE);
      unitDbCModel.setOperatorId(operatorId);
      tradeIdList.add(tradeId);
      unitDbCModel.setTradeIdList(tradeIdList);
      // レスポンスボディを取得
      String responseLine;
      try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
        StringBuilder response = new StringBuilder();
        while ((responseLine = br.readLine()) != null) {
          response.append(responseLine.trim());
        }

        // JSONオブジェクトのインスタンス作成
        // リクエスト内容を対応するクラスに格納
        UnitDbClausesAgreeResponseModel[] unitDbCARModel = mapper.readValue(response.toString(),
            UnitDbClausesAgreeResponseModel[].class);

        subUserIdList.add(unitDbCARModel[0].getSubUserId());
        unitDbCModel.setSubUserIdList(subUserIdList);
      }

    } else {
      // Httpステータス：200、201、415以外で、【３：失敗】を返却し処理を終了
      unitDbCModel.setResult(Constants.UNITDB_CERTIFICATION_RESULT_FAILURE);
    }

    // 接続を切断
    connection.disconnect();

    return unitDbCModel;

  }
}
