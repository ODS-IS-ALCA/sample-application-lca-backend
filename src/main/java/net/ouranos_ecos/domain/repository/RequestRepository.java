package net.ouranos_ecos.domain.repository;

import java.util.List;

import net.ouranos_ecos.domain.model.CfpCalcRequestRegistModel;
import net.ouranos_ecos.domain.model.CfpResponseTransModel;

public interface RequestRepository {
	
	/**
	 * 依頼情報を取得
	 * @param operatorId
	 * @param requestId
	 * @return CfpResponseTransModel
	 */
	CfpResponseTransModel select(String operatorId, String requestId);
	
	/**
	 * 依頼情報の受入済フラグを更新
	 * @param operatorId
	 * @param requestId
	 * @return int
	 */
	int updateAcceptedFlag(String operatorId, String requestId);
	
	/**
	 * 依頼情報を登録
	 * @param cfpCalcRequestModel
	 * @return int
	 */
	int insertRequest(CfpCalcRequestRegistModel cfpCalcRequestModel);

  /**
   * 依頼情報を取得
   * 
   * @param cfpCalcRequestModel
   * @return int
   */
	List<CfpResponseTransModel> selectRequestIdList(String operatorId);

}