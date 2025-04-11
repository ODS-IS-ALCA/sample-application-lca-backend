package net.ouranos_ecos.domain.repository.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.ouranos_ecos.domain.model.CfpCalcRequestRegistModel;
import net.ouranos_ecos.domain.model.CfpResponseTransModel;
import net.ouranos_ecos.domain.repository.RequestRepository;

/**
 * RequestRepository実装クラス。
 */
@Repository
public class RequestJdbc implements RequestRepository {

	@Autowired
	private JdbcTemplate jdbc;

  @Override
  public CfpResponseTransModel select(String operatorId, String requestId) {

    String sql = "SELECT request.requestid, request.requestedfromoperatorid, operator.operatorname, "
        + "lcapartsstructure.partsname, lcapartsstructure.partslabelname, lcapartsstructure.supportpartsname, "
        + "request.responseunit, request.requestmessage, request.requestedfromtraceId " 
        + "FROM public.request JOIN public.operator ON request.requestedfromoperatorId = operator.operatorid  "
        + "JOIN public.lcapartsstructure ON request.requestedfromtraceid = lcapartsstructure.traceid "
        + "WHERE request.requestedtooperatorid = ? AND request.requestid = ? AND request.deleteflag IS NOT True";

    Map<String, Object> resultMap = jdbc.queryForMap(sql, operatorId, requestId);

    CfpResponseTransModel model = new CfpResponseTransModel();
    model.setRequestId((String) resultMap.get("requestid"));
    model.setRequestedFromOperatorId((String) resultMap.get("requestedfromoperatorid"));
    model.setRequestedFromOperatorName((String) resultMap.get("operatorname"));
    model.setPartsName((String) resultMap.get("partsname"));
    model.setPartsLabelName((String) resultMap.get("partslabelname"));
    model.setSupportPartsName((String) resultMap.get("supportpartsname"));
    model.setResponseUnit((String) resultMap.get("responseunit"));
    model.setRequestMessage((String) resultMap.get("requestmessage"));
    model.setRequestedFromTraceId((String) resultMap.get("requestedfromtraceId"));

    return model;
  }
  
  @Override
  public int updateAcceptedFlag(String operatorId, String requestId) {

    Date nowDate = new Date();

    String sql = "UPDATE public.request "
        + "SET acceptedflag = ? , modifiedat = ? "
        + "WHERE operatorid = ? and requestid = ?";
    
    int result = jdbc.update(sql, true, nowDate, operatorId, requestId);

    return result;
  }
  
  @Override
  public int insertRequest(CfpCalcRequestRegistModel cfpCalcRequestModel) {

    Date nowDate = new Date();

    String sql = "INSERT "
        + "INTO public.request(operatorid, requestid, requestedat, requeststatus, requestedfromoperatorid"
        + ", requestedfromtraceid, requestedtooperatorid, requestmessage, acceptedflag, createdat, "
        + "modifiedat, deleteflag) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    int result = jdbc.update(sql, cfpCalcRequestModel.getOperatorId(), UUID.randomUUID().toString(), nowDate, "01",
        cfpCalcRequestModel.getRequestedFromOperatorId(), cfpCalcRequestModel.getRequestedFromTraceId(),
        cfpCalcRequestModel.getRequestedToOperatorId(), cfpCalcRequestModel.getRequestMessage(), false, nowDate,
        nowDate, false);

    return result;
  }

  @Override
  public List<CfpResponseTransModel> selectRequestIdList(String operatorId) {

    String sql = "SELECT requestid FROM request WHERE requestedtooperatorid = ? ORDER BY modifiedat DESC";
    List<Map<String, Object>> resultList = null;
    List<CfpResponseTransModel> entityList = new ArrayList<CfpResponseTransModel>();
    resultList = jdbc.queryForList(sql, operatorId);

    for (Map<String, Object> resultMap : resultList) {
      CfpResponseTransModel model = new CfpResponseTransModel();
      model.setRequestId((String) resultMap.get("requestid"));
      entityList.add(model);
    }

    return entityList;
  }

}