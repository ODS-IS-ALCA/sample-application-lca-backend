package net.ouranos_ecos.domain.repository;

import java.util.List;

import net.ouranos_ecos.domain.model.CfpResponseModel;
import net.ouranos_ecos.domain.model.LcaModel;
import net.ouranos_ecos.domain.model.ResponseProductLcaCfpModel;

public interface LcaResponseCfpRepository {

  /**
   * LCA回答CFP情報取得
   * 
   * @param operatorId
   * @param producttraceId
   * @param responseId
   * @return ResponseProductLcaCfpModel
   */
  ResponseProductLcaCfpModel select(String operatorId, String producttraceId, String responseId);

  /**
   * LCA回答CFP情報登録
   * 
   * @param cfpResponseModel
   * @param responseid
   * @param lcaModelList
   * @return int[]
   */
  int[] insertCfpResponse(CfpResponseModel cfpResponseModel, String responseid, List<LcaModel> lcaModelList)
      throws Exception;

}
