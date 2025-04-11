package net.ouranos_ecos.domain.repository.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.ouranos_ecos.domain.model.LcaMaterialModel;
import net.ouranos_ecos.domain.repository.LcaMaterialRepository;

/**
 * LcaMaterialRepository実装クラス。
 */
@Repository
public class LcaMaterialJdbc implements LcaMaterialRepository {

  @Autowired
  private JdbcTemplate jdbc;

  /**
   * LCA材料マスターを取得
   * 
   * @return LcamaterialModel
   * @throws DataAccessException
   */
  @Override
  public List<LcaMaterialModel> selectLcaMaterial() {

    String sql = "SELECT lcamaterialno, lcamaterialcd, lcamaterialname FROM"
        + " public.lcamaterial  ORDER BY  lcamaterialno";

    // 部品構成情報Tを検索する。
    List<Map<String, Object>> resultList = jdbc.queryForList(sql);

    List<LcaMaterialModel> lcamaterialList = new ArrayList<LcaMaterialModel>();

    LcaMaterialModel blankEntity = new LcaMaterialModel();
    blankEntity.setLcaMaterialCd("");
    blankEntity.setLcaMaterialName("");
    lcamaterialList.add(blankEntity);

    for (Map<String, Object> resultMap : resultList) {

      LcaMaterialModel entity = new LcaMaterialModel();
      entity.setLcaMaterialNo((Integer) resultMap.get("lcamaterialno"));
      String lcaMaterialCd = (String) resultMap.get("lcamaterialcd");
      lcaMaterialCd = lcaMaterialCd.trim();
      entity.setLcaMaterialCd(lcaMaterialCd);
      entity.setLcaMaterialName((String) resultMap.get("lcamaterialname"));

      lcamaterialList.add(entity);
    }

    return lcamaterialList;

  }
}
