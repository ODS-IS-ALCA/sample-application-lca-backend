package net.ouranos_ecos;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class AppConfig {
  
  @Value("${LCA_DB_ENDPOINT}")
  private String url;

  @Value("${LCA_DB_USER}")
  private String name;

  @Value("${LCA_DB_PASSWORD}")
  private String pass;
  
  @Value("${PRIMARYUSER_LICENSE_API_URL}")
  private String primaryUserLicenseApiUrl;
  
  @Value("${SUBUSER_LICENSE_API_URL}")
  private String subUserLicenseApiUrl;
  
  @Value("${SUBUSER_INFO_UPDATE_API_URL}")
  private String subUserInfoUpdateApiUrl;

  @Bean(name = "appDataSource")
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource dataSource() {
    return DataSourceBuilder.create()
        .url(url)
        .username(name)
        .password(pass)
        .driverClassName("org.postgresql.Driver")
        .build();
  }

  @Bean(name = "applicationJdbcTemplate")
  public JdbcTemplate applicationDataConnection() {
    return new JdbcTemplate(dataSource());
  }
  
  /**
   * 原単位DB_使用者ライセンス認証API URL
   * @return
   */
  public String getPrimaryUserLicenseApiUrl() {
    return primaryUserLicenseApiUrl;
  }
  
  /**
   * 原単位DB_準使用者ライセンス認証API URL
   * @return
   */
  public String getSubUserLicenseApiUrl() {
    return subUserLicenseApiUrl;
  }
  
  /**
   * 原単位DB_準使用者情報更新API URL
   * @return
   */
  public String getSubUserInfoUpdateApiUrl() {
    return subUserInfoUpdateApiUrl;
  }
}