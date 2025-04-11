package net.ouranos_ecos.domain.repository;

import java.text.ParseException;
import java.util.List;

import net.ouranos_ecos.domain.model.LcaCfpModel;
import net.ouranos_ecos.domain.model.ProductLcaCfpModel;

public interface LcaCfpRepository {
	
	/**
	 * 製品情報,LCA部品構成情報,LCACFP情報を取得
	 * @param operatorid
	 * @param producttraceId
	 * @return ProductLcaCfpModel
	 * @throws ParseException
	 */
	 ProductLcaCfpModel select(String operatorId, String producttraceId)
	      throws ParseException;
	 
	 /**
	  * LCACFP情報を登録
	  * @param lcaCfpModel
	  * @return int
	  */
   int insert(LcaCfpModel lcaCfpModel);
   
   /**
    * LCACFP情報を更新
    * @param lcaCfpModel
    * @return int
    */
   int update(LcaCfpModel lcaCfpModel);
   
   /**
    * 指定した行番号に紐づく、LCACFP情報を削除
    * @param operatorId
    * @param productTraceId
    * @param rowNos
    * @return int
    */
   int deleteByRowNo(String operatorId, String productTraceId, String rowNos);
   
   /**
    * LCACFP情報を取得
    * @param operatorId
    * @param traceId
    * @return LcaCfpModel
    */
   LcaCfpModel selectByTraceId(String operatorId, String traceId);

   /**
    * LCACFP情報リストを登録
    * @param lcaCfpModelList
    * @return int[]
    * @throws Exception
    */
   int[] insertLcaCfp(List<LcaCfpModel> lcaCfpModelList) throws Exception;

   /**
    * LCACFP情報リストを更新
    * @param lcaCfpModelList
    * @return int[]
    * @throws Exception
    */
   int[] updateLcaCfp(List<LcaCfpModel> lcaCfpModelList) throws Exception;

}
