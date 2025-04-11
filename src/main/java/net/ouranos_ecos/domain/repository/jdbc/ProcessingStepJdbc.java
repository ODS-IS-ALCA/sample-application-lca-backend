package net.ouranos_ecos.domain.repository.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.ouranos_ecos.domain.repository.ProcessingStepRepository;

/**
 * ProcessingStepRepository実装クラス。
 */
@Repository
public class ProcessingStepJdbc implements ProcessingStepRepository {

  @Autowired
  private JdbcTemplate jdbc;

  @Override
  public List<Map<String, Object>> get() {

    List<Map<String, Object>> resultList = jdbc.queryForList("SELECT * FROM processingstep WHERE deleteflag IS NOT True");
    return resultList;
  }

}