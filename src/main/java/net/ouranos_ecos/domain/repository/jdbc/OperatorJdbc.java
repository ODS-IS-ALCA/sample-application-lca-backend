package net.ouranos_ecos.domain.repository.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.ouranos_ecos.domain.model.OperatorModel;
import net.ouranos_ecos.domain.repository.OperatorRepository;

/**
 * OperatorRepository実装クラス。
 */
@Repository
public class OperatorJdbc implements OperatorRepository {

  @Autowired
  private JdbcTemplate jdbc;

  @Override
  public Map<String, Object> get(String openOperatorId) {

    Map<String, Object> map = new HashMap<>();

    try {
      map = jdbc.queryForMap("SELECT * FROM operator WHERE openoperatorid = ?", openOperatorId);
    } catch (EmptyResultDataAccessException e) {
      return map;
    }
    return map;
  }
  
  @Override
  public List<OperatorModel> get() {

    String sql = "SELECT operatorid, openoperatorid, operatorname FROM operator;";

    List<Map<String, Object>> resultList = jdbc.queryForList(sql);
    // 結果返却用の変数
    List<OperatorModel> operatorList = new ArrayList<OperatorModel>();

    for (Map<String, Object> map : resultList) {
      OperatorModel operator = new OperatorModel();

      operator.setOperatorId(map.get("operatorid").toString());
      operator.setOpenOperatorId(map.get("openoperatorid").toString());
      operator.setOperatorName(map.get("operatorname").toString());

      operatorList.add(operator);
    }

    return operatorList;
  }
}