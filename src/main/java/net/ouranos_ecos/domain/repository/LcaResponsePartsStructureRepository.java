package net.ouranos_ecos.domain.repository;

import java.util.List;

import net.ouranos_ecos.domain.model.CfpResponseModel;
import net.ouranos_ecos.domain.model.LcaModel;

public interface LcaResponsePartsStructureRepository {

  /**
   * LCA回答部品構成情報登録
   * @param cfpResponseModel
   * @param responseid
   * @param lcaModelList
   * @return  int[]
   * @throws Exception
   */
  int[] insertCfpResponse(CfpResponseModel cfpResponseModel, String responseid, List<LcaModel> lcaModelList)
      throws Exception;

}