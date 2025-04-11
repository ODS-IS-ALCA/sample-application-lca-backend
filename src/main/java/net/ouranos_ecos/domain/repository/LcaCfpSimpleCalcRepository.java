package net.ouranos_ecos.domain.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import net.ouranos_ecos.domain.model.LcaCfpSimpleCalcModel;

public interface LcaCfpSimpleCalcRepository {
	
  /**
   * LCACFP簡易計算情報を挿入
   * 
   * @param lcaCfpSimpleCalcModel
   * @return int
   * @throws DataAccessException
   */
  int insert(LcaCfpSimpleCalcModel lcaCfpSimpleCalcModel);
  
  /**
   * LCACFP簡易計算情報を更新
   * @param model
   * @return
   * @throws DataAccessException
   */
  int update(LcaCfpSimpleCalcModel model);
  
  /**
   * 指定した行番号に紐づく、LCACFP簡易計算情報を削除
   * @param operatorId
   * @param productTraceId
   * @param rowNos
   * @return
   * @throws DataAccessException
   */
  int deleteByRowNo(String operatorId, String productTraceId, String rowNos);

  /**
   * LcaCfpSimpleCalc情報リストを登録
   * 
   * @param lcaCSCModelList
   * @return
   * @throws DataAccessException
   */
  int[] insertLcaCfpSimpleCalc(List<LcaCfpSimpleCalcModel> lcaCSCModelList) throws Exception;
}
