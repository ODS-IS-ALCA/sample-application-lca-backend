package net.ouranos_ecos.domain.repository;

import net.ouranos_ecos.domain.model.CfpCalcRequestModel;

public interface CfpCalcRequestRepoitory {

  /**
   * CFP算出依頼取得
   * @param operatorId
   * @param traceId
   * @return CfpCalcRequestModel
   */
  CfpCalcRequestModel select(String operatorId, String traceId);
}
