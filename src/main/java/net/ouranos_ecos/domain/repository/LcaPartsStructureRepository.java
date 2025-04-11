package net.ouranos_ecos.domain.repository;

import java.text.ParseException;
import java.util.List;

import org.springframework.dao.DataAccessException;

import net.ouranos_ecos.domain.entity.LcaCfpResultCsvData;
import net.ouranos_ecos.domain.model.LcaPartsStructureModel;

public interface LcaPartsStructureRepository {

  /**
   * トレース識別子（親）で、LCA部品構成情報Tを更新
   * 
   * @param traceId
   * @return int
   * @throws DataAccessException
   */
  int insert(LcaPartsStructureModel parentsParts) throws DataAccessException;
  
  /**
   * LCA部品構成情報を取得
   * @param operatorid
   * @param traceid
   * @return
   */
  LcaPartsStructureModel select(String operatorid, String traceid);
  
  /**
   * LCA部品構成情報リストを取得
   * @param operatorid
   * @param traceid
   * @return
   */
  List<LcaPartsStructureModel> selectByProductPrimaryKey(String operatorid, String productTraceId);

  /**
   * (回答画面からのDLの場合)回答製品情報、LCA回答部品構成情報、LCACFP簡易計算情報　を取得
   * (簡易計算結果DLの場合)製品情報、部品構成情報、LCACFP簡易計算情報 を取得
   * (最新計算結果DLの場合)製品情報、部品構成情報、LCACFP情報　を取得
   * 
   * @param operatorId
   * @param producttraceId
   * @param responseId
   * @param dlFlg
   * @return LcaCfpResultCsvData
   * @throws ParseException
   */
  LcaCfpResultCsvData selectCfpCsv(String operatorId, String producttraceId, String responseId, String dlFlg)
      throws ParseException;

  /**
   * 指定した行番号に紐づく、LCA部品構成情報を削除
   * 
   * @param operatorId
   * @param productTraceId
   * @param rowNos
   * @return int
   */
  int deleteByRowNo(String operatorId, String productTraceId, String rowNos);

  /**
   * 指定した行番号のLCA部品構成情報を更新
   * @param model
   * @return int
   */
  int updateMatchingRowNo(LcaPartsStructureModel model);
  
  /**
   * LCA部品構成情報の行番号を更新
   * @param modelList
   * @return int[]
   * @throws Exception
   */
  int[] updateCfpRowNo(List<LcaPartsStructureModel> modelList) throws Exception;

  /**
   * リストで、LCA部品構成情報Tを更新
   * 
   * @param lcaModelList
   * @return int[]
   * @throws Exception
   */
  int[] insertLcaPartsStructure(List<LcaPartsStructureModel> lcaModelList) throws Exception;

}