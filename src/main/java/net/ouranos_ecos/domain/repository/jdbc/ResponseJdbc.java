package net.ouranos_ecos.domain.repository.jdbc;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.ouranos_ecos.domain.common.Constants;
import net.ouranos_ecos.domain.common.ConverterUtil;
import net.ouranos_ecos.domain.model.CfpRequestModel;
import net.ouranos_ecos.domain.model.CfpResponseModel;
import net.ouranos_ecos.domain.model.CfpResponseProductModel;
import net.ouranos_ecos.domain.repository.ResponseRepository;

/**
 * ResponseRepository実装クラス。
 */
@Repository
public class ResponseJdbc implements ResponseRepository {

	@Autowired
	private JdbcTemplate jdbc;

  @Override
  public List<CfpRequestModel> selectCfpRequestList(String operatorid, int limit, String after)
      throws ParseException {

    SimpleDateFormat format = new SimpleDateFormat(Constants.YYYYMMDD_HHMMSS);
    SimpleDateFormat dataformat = new SimpleDateFormat(Constants.YYYYMMDD);
    
	  List<Map<String, Object>> list = null;
	  
    if (after != null) {
      String sql = "SELECT "
          + "   request.operatorid "
          + "  ,request.requestid "
          + "  ,operator.operatorname "
          + "  ,lcapartsstructure.partsname "
          + "  ,lcapartsstructure.partsLabelname "
          + "  ,lcapartsstructure.supportpartsname "
          + "  ,request.requestmessage "
          + "  ,request.requestedat"
          + "  ,response.responsestatus "
          + "  ,request.modifiedat "
          + " FROM  "
          + "  public.request"
          + "  LEFT JOIN public.response"
          + "  ON request.requestedtooperatorid = response.operatorid "
          + "  AND request.requestid = response.requestid"
          + "  LEFT JOIN public.lcapartsstructure"
          + "  ON request.requestedfromtraceid = lcapartsstructure.traceid"
          + "  LEFT JOIN public.operator"
          + "  ON request.requestedfromoperatorid = operator.operatorid"
          + " WHERE "
          + "  request.requestedtooperatorid = ? "
          + "  AND request.modifiedat < ? AND request.deleteflag IS NOT True "
          + " ORDER BY request.modifiedat DESC LIMIT ?";
      
      list = jdbc.queryForList(sql, operatorid, format.parse(after), limit);
      
    } else {
      String sql = "SELECT "
          + "   operator.openoperatorid "
          + "  ,request.requestid "
          + "  ,operator.operatorname "
          + "  ,lcapartsstructure.partsname "
          + "  ,lcapartsstructure.partsLabelname "
          + "  ,lcapartsstructure.supportpartsname "
          + "  ,request.requestmessage "
          + "  ,request.requestedat"
          + "  ,response.responsestatus "
          + "  ,request.modifiedat "
          + " FROM  "
          + "  public.request"
          + "  LEFT JOIN public.response"
          + "  ON request.requestedtooperatorid = response.operatorid "
          + "  AND request.requestid = response.requestid"
          + "  LEFT JOIN public.lcapartsstructure"
          + "  ON request.requestedfromtraceid = lcapartsstructure.traceid"
          + "  LEFT JOIN public.operator"
          + "  ON request.requestedfromoperatorid = operator.operatorid"
          + " WHERE "
          + "  request.requestedtooperatorid = ? "
          + "  AND request.deleteflag IS NOT True "
          + " ORDER BY request.modifiedat DESC LIMIT ?";
      
      list = jdbc.queryForList(sql, operatorid, limit);
    }

	  // 結果返却用の変数
	  List<CfpRequestModel> cfpRequestList = new ArrayList<CfpRequestModel>();

	  for (Map<String, Object> map : list) {
	    CfpRequestModel cfpResponse = new CfpRequestModel();

	    cfpResponse.setRequestedFromOperatorId((String)map.get("openoperatorid"));
	    cfpResponse.setRequestId((String)map.get("requestid"));
	    cfpResponse.setRequestedFromOperatorName((String)map.get("operatorname"));
	    cfpResponse.setPartsName((String)map.get("partsname"));
	    cfpResponse.setPartsLabelName((String)map.get("partsLabelname"));
	    cfpResponse.setSupportPartsName((String)map.get("supportpartsname"));
	    cfpResponse.setRequesteDat(dataformat.format((Date) map.get("requestedat")));
	    cfpResponse.setRequestMessage((String)map.get("requestmessage"));
	    if (map.get("responsestatus") != null) {
	      cfpResponse.setResponseStatus((String)map.get("responsestatus"));
	    }
	    cfpResponse.setModifiedAt(format.format((Date) map.get("modifiedat")));
	    cfpRequestList.add(cfpResponse);
	  }

	  return cfpRequestList;
	}

  @Override
  public List<CfpResponseProductModel> select(String operatorId, String requestId) {

    SimpleDateFormat dataformat = new SimpleDateFormat(Constants.YYYYMMDD);

    String sql = "SELECT responseproduct.operatorid, responseproduct.responseid, "
        + "responseproduct.producttraceid, responseproduct.productitem, responseproduct.supplyitemno, "
        + "responseproduct.supplyfuctory, responseproduct.fuctoryaddress, responseproduct.responceinfo, "
        + "responseproduct.materialsTotal, responseproduct.gco2eqTotal, responseproduct.cfpModifieDat "
        + "FROM public.response JOIN public.responseproduct ON response.operatorid = responseproduct.operatorid  "
        + "AND response.responseid = responseproduct.responseid  "
        + "AND response.requestedtoproducttraceid = responseproduct.producttraceid  "
        + "WHERE response.operatorid = ? AND response.requestid = ? AND response.deleteflag IS NOT True";

    List<Map<String, Object>> resultList = jdbc.queryForList(sql, operatorId, requestId);

    List<CfpResponseProductModel> entityList = new ArrayList<CfpResponseProductModel>();

    for (Map<String, Object> map : resultList) {

      CfpResponseProductModel model = new CfpResponseProductModel();
      model.setOperatorId((String) map.get("operatorid"));
      model.setResponseId((String) map.get("responseid"));
      model.setProductTraceId((String) map.get("producttraceid"));
      model.setProductItem((String) map.get("productitem"));
      model.setSupplyItemNo((String) map.get("supplyitemno"));
      model.setSupplyFuctory((String) map.get("supplyfuctory"));
      model.setFuctoryAddress((String) map.get("fuctoryaddress"));
      model.setResponceInfo((String) map.get("responceinfo"));
      model.setMaterialsTotal(ConverterUtil.formatCommas((BigDecimal) map.get("materialsTotal")));
      model.setGco2eqTotal(ConverterUtil.formatCommas((BigDecimal) map.get("gco2eqTotal")));
      model.setCfpModifieDat(dataformat.format((Date) map.get("cfpModifieDat")));

      entityList.add(model);
    }
    return entityList;

  }
  
  @Override
  public String selectRequestId(String operatorId, String responseId) {

    String sql = "SELECT requestid FROM public.response WHERE requestedfromoperatorid = ? AND responseid = ?  AND deleteflag IS NOT True";

    Map<String, Object> resultMap = jdbc.queryForMap(sql, operatorId, responseId);

    return (String) resultMap.get("requestid");

  }
  
  @Override
  public int insertCfpResponse(CfpResponseModel model, String responseid) {

    String sql = "INSERT INTO public.response(operatorid, responseid, responsedat, responsestatus, "
        + "requestid, requestedfromoperatorid, requestedfromtraceid, requestedtooperatorid, "
        + "requestedtoproducttraceid, createdat, modifiedat, deleteflag) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    Date nowDate = new Date();

    int result = jdbc.update(sql, model.getOperatorId(), responseid, nowDate, "01", model.getRequestId(),
        model.getRequestedFromOperatorId(), model.getRequestedFromTraceId(), model.getOperatorId(),
        model.getProductTraceId(), nowDate, nowDate, false);

    return result;
  }
}