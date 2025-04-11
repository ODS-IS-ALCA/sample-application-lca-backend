package net.ouranos_ecos.domain.repository;

import org.springframework.dao.DataAccessException;

public interface LoginRepository {

  /**
   * ログイン情報取得
   * @param operatorAccountId
   * @param accountPassword
   * @return String
   * @throws DataAccessException
   */
  String selectByIdandPassword(String operatorAccountId, String accountPassword);

}