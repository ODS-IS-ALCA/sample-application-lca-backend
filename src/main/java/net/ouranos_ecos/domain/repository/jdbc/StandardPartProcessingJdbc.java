package net.ouranos_ecos.domain.repository.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.ouranos_ecos.domain.model.StandardPartProcessingModel;
import net.ouranos_ecos.domain.repository.StandardPartProcessingRepository;

/**
 * StandardPartProcessingRepository実装クラス。
 */
@Repository
public class StandardPartProcessingJdbc implements StandardPartProcessingRepository {

	@Autowired
	private JdbcTemplate jdbc;

	@Override
  public StandardPartProcessingModel get(String standardPartCd) {

    String sql = "SELECT standardpartcd, standardpartcategory, standardpartname, standardpartprocessing1"
        + " , standardpartprocessing2, standardpartprocessing3, standardpartprocessing4"
        + " FROM public.standardpartprocessing WHERE deleteflag IS NOT True AND standardpartcd = ?";

    List<Map<String, Object>> resultLiist = jdbc.queryForList(sql, standardPartCd);

    List<StandardPartProcessingModel> modelList = new ArrayList<StandardPartProcessingModel>();

    for (Map<String, Object> map : resultLiist) {

      StandardPartProcessingModel model = new StandardPartProcessingModel();
      model.setStandardPartCd((String) map.get("standardpartcd"));
      model.setStandardPartCategory((String) map.get("standardpartcategory"));
      model.setStandardPartName((String) map.get("standardpartname"));
      model.setStandardPartProcessing1((String) map.get("standardpartprocessing1"));
      model.setStandardPartProcessing2((String) map.get("standardpartprocessing2"));
      model.setStandardPartProcessing3((String) map.get("standardpartprocessing3"));
      model.setStandardPartProcessing4((String) map.get("standardpartprocessing4"));
      modelList.add(model);
    }
    StandardPartProcessingModel standardPartProcessingModel = new StandardPartProcessingModel();
    if (modelList.size() > 0) {
      standardPartProcessingModel = modelList.get(0);
    }

    return standardPartProcessingModel;
  }

}