package net.ouranos_ecos.domain.service;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.ouranos_ecos.domain.common.Constants;
import net.ouranos_ecos.domain.common.ConverterUtil;
import net.ouranos_ecos.domain.common.ErrorUtil;
import net.ouranos_ecos.domain.entity.LcaCfpResultCsvData;
import net.ouranos_ecos.domain.repository.jdbc.LcaPartsStructureJdbc;

/**
 * LcaCfpResultCsvServiceクラス。
 */

@Service
@Slf4j
public class LcaCfpResultCsvService {

  @Inject
  ObjectMapper mapper;
  @Inject
  LcaPartsStructureJdbc lcaPartsStructureJdbc;

  @Transactional
  public ResponseEntity<byte[]> getRequest(HttpServletRequest request) {

    ResponseEntity<byte[]> responseEntity = null;

    try {
      // 内部事業者識別子を取得
      String operatorId = request.getParameter("operatorId");
      if (!StringUtils.hasText(operatorId)) {
        // 内部事業者識別子が取得できない場合
        return ErrorUtil.badRequestResponse(mapper, "", request.getMethod());
      }

      // 製品トレース識別子を取得
      String productTraceId = request.getParameter("productTraceId");
      //
      String dlFlg = request.getParameter("dlFlg");
      if (!StringUtils.hasText(productTraceId) || !StringUtils.hasText(dlFlg)) {
        // 製品トレース識別子とダウンロードフラグが取得できない場合
        return ErrorUtil.badRequestResponse(mapper, operatorId, request.getMethod());
      }

      // 製品トレース識別子を取得
      String responseId = request.getParameter("responseId");

      // (回答画面からのDLの場合)回答製品情報、LCA回答部品構成情報、LCACFP簡易計算情報 を取得
      // (簡易計算結果DLの場合)製品情報、部品構成情報、LCACFP簡易計算情報 を取得
      // (最新計算結果DLの場合)製品情報、部品構成情報、LCACFP情報 を取得
      LcaCfpResultCsvData csvData = lcaPartsStructureJdbc.selectCfpCsv(operatorId, productTraceId, responseId, dlFlg);

      // csvの文字列セット
      StringBuilder csvText = new StringBuilder();

      // 製品情報の追加
      csvText.append(ConverterUtil.convertObjectToCsv(csvData.getCsvProductData(), Constants.PRODUCT_HEADER));
      // インプット情報の追加
      csvText.append(Constants.NEW_LINE);
      csvText.append(Constants.INPUT_INFO);
      csvText.append(Constants.NEW_LINE);
      csvText
          .append(ConverterUtil.convertObjectToCsv(csvData.getCsvInputMaterialData(), Constants.INPUT_MATRIALS_HEADER));
      csvText.append(Constants.NEW_LINE);
      csvText.append(ConverterUtil.convertObjectToCsv(csvData.getCsvInputEnegryData(), Constants.INPUT_ENEGRY_HEADER));
      // アウトプット情報の追加
      csvText.append(Constants.NEW_LINE);
      csvText.append(Constants.OUTPUT_INFO);
      csvText.append(Constants.NEW_LINE);
      csvText.append(ConverterUtil.convertObjectToCsv(csvData.getCsvOutputData(), Constants.OUTPUT_HEADER));
      // 部品構成・CFP詳細情報の追加
      csvText.append(Constants.NEW_LINE);
      csvText.append(Constants.CFP_INFO);
      csvText.append(Constants.NEW_LINE);
      csvText.append(ConverterUtil.convertObjectToCsv(csvData.getCsvCfpInfoData(), Constants.CFP_INFO_HEADER));

      // Byte変換(UTF-8(Bom付))
      byte[] bom = new byte[]{(byte) 0xef, (byte) 0xbb, (byte) 0xbf};
      byte[] csvBytes = csvText.toString().getBytes("UTF-8");
      byte[] csvWithBom = new byte[bom.length + csvBytes.length];
      System.arraycopy(bom, 0, csvWithBom, 0, bom.length);
      System.arraycopy(csvBytes, 0, csvWithBom, bom.length, csvBytes.length);

      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", "text/csv");

      responseEntity = ResponseEntity.status(HttpStatus.OK).headers(headers).body(csvWithBom);

    } catch (Exception e) {
      log.warn("予期せぬエラー", e);
      e.printStackTrace();
      responseEntity = ErrorUtil.internalServerErrorResponse(mapper, request.getParameter("operatorId"),
          request.getMethod());
    }

    return responseEntity;
  }

}
