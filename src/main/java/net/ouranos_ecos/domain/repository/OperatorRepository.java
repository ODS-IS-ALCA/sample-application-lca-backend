package net.ouranos_ecos.domain.repository;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ouranos_ecos.domain.model.OperatorModel;

public interface OperatorRepository {

  /**
   * 事業者情報を取得
   * @param openOperatorId
   * @return Map<String, Object>
   */
  Map<String, Object> get(String openOperatorId);
  
  /**
   * 事業者情報を全件取得
   * @return List<OperatorListModel>
   * @throws DataAccessException
   */
  List<OperatorModel> get();

}