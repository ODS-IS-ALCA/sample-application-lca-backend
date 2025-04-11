package net.ouranos_ecos.domain.repository.jdbc;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.ouranos_ecos.domain.repository.LoginRepository;

/**
 * LoginRepository実装クラス。
 */
@Repository
public class LoginJdbc implements LoginRepository {

	@Autowired
	private JdbcTemplate jdbc;

	@Override
  public String selectByIdandPassword(String operatorAccountId, String accountPassword) {

    String sql = "SELECT operatorid , openoperatorid , operatorname , operatoraddress , userid"
        + " , password , createdat , modifiedat FROM public.operator WHERE userid = ?"
        + " AND password = ?";

    try {
      Map<String, Object> resultMap = jdbc.queryForMap(sql, operatorAccountId, accountPassword);
      // operatoridを返却する。
      return (String) resultMap.get("operatorid");

    } catch (IncorrectResultSizeDataAccessException e) {
      // 1件も取得できない場合
      return null;
    }
  }

}