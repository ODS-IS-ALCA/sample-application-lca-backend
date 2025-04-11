package net.ouranos_ecos.domain.repository;

import java.util.List;
import java.util.Map;

public interface ProcessingStepRepository {

  /**
   * 加工工程を取得
   * @return List<Map<String, Object>>
   */
	List<Map<String, Object>> get();

}