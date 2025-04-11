package net.ouranos_ecos.domain.repository;

import java.text.ParseException;
import java.util.List;

import net.ouranos_ecos.domain.model.CfpRequestModel;
import net.ouranos_ecos.domain.model.CfpResponseModel;
import net.ouranos_ecos.domain.model.CfpResponseProductModel;

public interface ResponseRepository {
	
	/**
	 * 回答情報を取得
	 * @param operatorId
	 * @param requestId
	 * @return List<CfpResponseProductModel>
	 */
	 List<CfpResponseProductModel> select(String operatorId, String requestId);
	 
	  /**
	   * 回答情報の依頼識別子を取得
	   * @param operatorId
	   * @param responseId
	   * @return String
	   */
	  String selectRequestId(String operatorId, String responseId);
	 
	 /**
	  * 回答情報（response）を登録
	  * @param cfpResponseModel
	  * @param responseid
	  * @return insertCfpResponse
	  */
	 int insertCfpResponse(CfpResponseModel cfpResponseModel, String responseid);

   /**
    * 回答情報一覧画面のデータを取得
    * 
    * @param operatorid
    * @param limit
    * @param after
    * @return List<CfpRequestModel>
    * @throws ParseException
    */
   List<CfpRequestModel> selectCfpRequestList(String operatorid, int limit, String after) throws ParseException ;

}