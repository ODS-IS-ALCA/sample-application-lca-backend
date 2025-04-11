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
import net.ouranos_ecos.domain.model.CfpResponseModel;
import net.ouranos_ecos.domain.model.ProductModel;
import net.ouranos_ecos.domain.model.ResponseProductModel;
import net.ouranos_ecos.domain.repository.ResponseProductRepository;

/**
 * ResponseProductRepository実装クラス。
 */
@Repository
public class ResponseProductJdbc implements ResponseProductRepository {

  @Autowired
  private JdbcTemplate jdbc;

  @Override
  public int insertCfpResponse(CfpResponseModel cfpResponseModel, String responseid, ProductModel productModel)
      throws ParseException {

    Date nowDate = new Date();
    SimpleDateFormat format = new SimpleDateFormat(Constants.YYYYMMDD_HHMMSS);

    String sql = "INSERT INTO responseproduct(operatorid, responseid, producttraceid, productitem, "
        + "supplyitemno, supplyfuctory, fuctoryaddress, responceinfo, materialstotal, "
        + "gco2eqtotal, cfpmodifiedat, createdat, modifiedat, deleteflag) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    int result = jdbc.update(sql, cfpResponseModel.getOperatorId(), responseid, cfpResponseModel.getProductTraceId(),
        productModel.getProductItem(), productModel.getSupplyItemNo(), productModel.getSupplyFuctory(),
        productModel.getFuctoryAddress(), productModel.getResponceInfo(), new BigDecimal(productModel.getMaterialsTotal()),
        new BigDecimal(productModel.getGco2eqTotal()), format.parse(productModel.getCfpModifieDat()), nowDate, nowDate, false);

    return result;
  }
  
  @Override
  public List<ResponseProductModel> select(String operatorid, int limit, String after)
      throws ParseException {

    SimpleDateFormat format = new SimpleDateFormat(Constants.YYYYMMDD_HHMMSS);
    SimpleDateFormat dataformat = new SimpleDateFormat(Constants.YYYYMMDD);
    List<Map<String, Object>> resultList = null;

    if (after != null) {
      // afterがある場合は、after以降を取得する。
      String sql = "SELECT responseproduct.operatorid , responseproduct.responseid, responseproduct.producttraceid , responseproduct.productitem , "
          + "responseproduct.supplyitemno, responseproduct.supplyfuctory , responseproduct.fuctoryaddress , responseproduct.responceinfo, "
          + "request.acceptedflag, responseproduct.modifiedat, responseproduct.materialstotal, responseproduct.gco2eqtotal, responseproduct.cfpmodifiedat "
          + "FROM public.request "
          + "JOIN public.response  ON request.requestedfromoperatorid = response.requestedfromoperatorid  AND request.requestid = response.requestid "
          + "JOIN public.responseproduct ON response.responseid = responseproduct.responseid "
          + "WHERE request.operatorid = ? "
          + "AND responseproduct.modifiedat < ? AND responseproduct.deleteflag IS NOT True ORDER BY responseproduct.modifiedat DESC LIMIT ?";

      // 製品情報Tを検索する。
      resultList = jdbc.queryForList(sql, operatorid, format.parse(after), limit);

    } else {
      String sql = "SELECT responseproduct.operatorid , responseproduct.responseid, responseproduct.producttraceid , responseproduct.productitem , "
          + "responseproduct.supplyitemno, responseproduct.supplyfuctory , responseproduct.fuctoryaddress , responseproduct.responceinfo, "
          + "request.acceptedflag, responseproduct.modifiedat, responseproduct.materialstotal, responseproduct.gco2eqtotal, responseproduct.cfpmodifiedat "
          + "FROM public.request "
          + "JOIN public.response  ON request.requestedfromoperatorid = response.requestedfromoperatorid  AND request.requestid = response.requestid "
          + "JOIN public.responseproduct ON response.responseid = responseproduct.responseid "
          + "WHERE request.operatorid = ? AND responseproduct.deleteflag IS NOT True ORDER BY responseproduct.modifiedat DESC LIMIT ?";

      // 製品情報Tを検索する。
      resultList = jdbc.queryForList(sql, operatorid, limit);
    }
    List<ResponseProductModel> entityList = new ArrayList<ResponseProductModel>();

    for (Map<String, Object> resultMap : resultList) {

      ResponseProductModel model = new ResponseProductModel();
      model.setOperatorId((String) resultMap.get("operatorid"));
      model.setResponseId((String) resultMap.get("responseid"));
      model.setProductTraceId((String) resultMap.get("producttraceid"));
      model.setProductItem((String) resultMap.get("productitem"));
      model.setSupplyItemNo((String) resultMap.get("supplyitemno"));
      model.setSupplyFuctory((String) resultMap.get("supplyfuctory"));
      model.setFuctoryAddress((String) resultMap.get("fuctoryaddress"));
      model.setResponceInfo((String) resultMap.get("responceinfo"));
      model.setAcceptedFlag((boolean) resultMap.get("acceptedflag"));
      model.setModifiedAt(format.format((Date) resultMap.get("modifiedat")));
      model.setMaterialsTotal(ConverterUtil.formatCommas((BigDecimal) resultMap.get("materialstotal")));
      model.setGco2eqTotal(ConverterUtil.formatCommas((BigDecimal) resultMap.get("gco2eqtotal")));
      if (resultMap.get("cfpmodifiedat") != null) {
        model.setCfpModifieDat(dataformat.format((Date) resultMap.get("cfpmodifiedat")));
      }
      entityList.add(model);
    }
    return entityList;

  }
}
