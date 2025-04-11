package net.ouranos_ecos.domain.repository.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.ouranos_ecos.domain.model.CalcRequestModel;
import net.ouranos_ecos.domain.model.CfpCalcRequestModel;
import net.ouranos_ecos.domain.model.ProductModel;
import net.ouranos_ecos.domain.repository.CfpCalcRequestRepoitory;

/**
 * CfpCalcRequestRepository実装クラス。
 */
@Repository
public class CfpCalcRequestJdbc implements CfpCalcRequestRepoitory {

  @Autowired
  private JdbcTemplate jdbc;

  @Override
  public CfpCalcRequestModel select(String operatorId, String traceId) {

    CfpCalcRequestModel cfpCalcRequestModel = new CfpCalcRequestModel();
    
    // 製品情報の取得
    String sql = "SELECT "
        + "producttraceid, "
        + "productitem, "
        + "supplyitemno, "
        + "supplyfuctory, "
        + "fuctoryaddress, "
        + "responceinfo "
        + "FROM public.product "
        + "WHERE operatorid = ? "
        + "AND producttraceid = ? "
        + "AND deleteflag IS NOT True;";
    
    Map<String, Object> resultMap = jdbc.queryForMap(sql, operatorId, traceId);
    ProductModel productModel = new ProductModel();
    productModel.setProductTraceId((String) resultMap.get("producttraceid"));
    productModel.setProductItem((String) resultMap.get("productitem"));
    productModel.setSupplyItemNo((String) resultMap.get("supplyitemno"));
    productModel.setSupplyFuctory((String) resultMap.get("supplyfuctory"));
    productModel.setFuctoryAddress((String) resultMap.get("fuctoryaddress"));
    productModel.setResponceInfo((String) resultMap.get("responceinfo"));
    
    cfpCalcRequestModel.setProductModel(productModel);
    
    // 依頼リスト情報の取得
    String sql2 = "SELECT "
        + "ps.traceid, "
        + "CASE "
        + "WHEN rt.requeststatus IS NULL THEN '' "
        + "WHEN rs.requestid IS NULL THEN '01' "
        + "ELSE '02' "
        + "END AS requeststatus, "
        + "ps.partsname, "
        + "ps.partslabelname, "
        + "ps.supportpartsname, "
        + "rt.requestedtooperatorid, "
        + "op.operatorname, "
        + "rt.responseunit, "
        + "rt.requestmessage "
        + "FROM public.product pt "
        + "LEFT JOIN public.lcapartsstructure ps "
        + "ON pt.producttraceid = ps.producttraceid "
        + "LEFT JOIN  public.request rt "
        + "ON ps.traceid = rt.requestedfromtraceid "
        + "LEFT JOIN public.operator op "
        + "ON op.operatorid = rt.requestedtooperatorid "
        + "LEFT JOIN public.response rs "
        + "ON rt.requestid = rs.requestid "
        + "WHERE pt.operatorid = ? "
        + "AND pt.producttraceid = ? "
        + "AND ps.requesttargetflag IS TRUE "
        + "ORDER BY ps.rowno;";

    List<Map<String, Object>> resultList = jdbc.queryForList(sql2, operatorId, traceId);
    
    List<CalcRequestModel> entityList = new ArrayList<CalcRequestModel>();
    
    for (Map<String, Object> map : resultList) {
      CalcRequestModel model = new CalcRequestModel(); 
      model.setTraceId((String) map.get("traceid"));
      model.setRequestStatus((String) map.get("requeststatus"));
      model.setPartsName((String) map.get("partsname"));
      model.setPartsLabelName((String) map.get("partslabelname"));
      model.setSupportPartsName((String) map.get("supportpartsname"));
      model.setRequestedToOperatorId((String) map.get("requestedtooperatorid"));
      model.setRequestedToOperatorName((String) map.get("operatorname"));
      model.setResponseUnit((String) map.get("responseunit"));
      model.setRequestMessage((String) map.get("requestmessage"));
      
      entityList.add(model);
    }
    cfpCalcRequestModel.setCalcRequestModel(entityList);
    
    return cfpCalcRequestModel;
  }

}
