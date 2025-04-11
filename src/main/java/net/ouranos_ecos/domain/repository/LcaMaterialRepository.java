package net.ouranos_ecos.domain.repository;

import java.util.List;

import net.ouranos_ecos.domain.model.LcaMaterialModel;

public interface LcaMaterialRepository {

  /**
   * LCA材料マスターを取得
   * 
   * @return LcamaterialModel
   */
  List<LcaMaterialModel> selectLcaMaterial();

}