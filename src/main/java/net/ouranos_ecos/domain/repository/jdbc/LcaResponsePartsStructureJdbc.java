package net.ouranos_ecos.domain.repository.jdbc;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.ouranos_ecos.domain.model.CfpResponseModel;
import net.ouranos_ecos.domain.model.LcaModel;
import net.ouranos_ecos.domain.repository.LcaResponsePartsStructureRepository;

/**
 * LcaResponsePartsStructureRepository実装クラス。
 */
@Repository
public class LcaResponsePartsStructureJdbc implements LcaResponsePartsStructureRepository {

  @Autowired
  private JdbcTemplate jdbc;

  @Override
  public int[] insertCfpResponse(CfpResponseModel cfpResponseModel, String responseid, List<LcaModel> lcaModelList)
      throws Exception {

    Date nowDate = new Date();

    List<Object[]> batchArgs = lcaModelList.stream()
        .map(model -> new Object[]{cfpResponseModel.getOperatorId(), responseid, model.getTraceId(),
            model.getPartsName(), model.getPartsLabelName(), model.getSupportPartsName(),
            model.getPartsStructureLevel(), model.getNumber(), model.getMass(), model.getTotalMass(),
            model.getMaterialCd(), model.getMaterialStandard(), model.getMaterialCategory(), model.getLcaMaterialCd(),
            model.getPartsProcurementCd(), model.getMateriaProcurementCd(), model.isEndFlag(),
            model.isBottomLayerFlag(), cfpResponseModel.getProductTraceId(), model.getRowNo(), nowDate, nowDate, false})
        .collect(Collectors.toList());

    String sql = "INSERT INTO lcaresponsepartsstructure(operatorid, responseid, traceid, partsname,partslabelname, supportpartsname, "
        + "partsstructurelevel, number, mass, totalmass, materialcd, materialstandard, materialcategory, lcamaterialcd, partsprocurementcd, "
        + "materiaprocurementcd, endflag, bottomlayerflag, producttraceid, rowno, createdat, modifiedat, deleteflag) "
        + "VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    // まとめて更新
    int[] results = jdbc.batchUpdate(sql, batchArgs);

    for (int value : results) {
      if (value == 0) {
        // 更新出来ない行がある場合エラーとする
        throw new Exception();
      }
    }
    return results;
  }

}
