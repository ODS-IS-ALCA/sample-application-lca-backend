package net.ouranos_ecos.domain.repository;

import net.ouranos_ecos.domain.model.StandardPartProcessingModel;

public interface StandardPartProcessingRepository {

  /**
   * 標準工程_部品加工を取得
   * @param standardPartCd
   * @return StandardPartProcessingModel
   */
  StandardPartProcessingModel get(String standardPartCd);

}