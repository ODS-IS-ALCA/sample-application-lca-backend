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
import net.ouranos_ecos.domain.model.ProductModel;
import net.ouranos_ecos.domain.repository.ProductRepository;

/**
 * ProductRepository実装クラス。
 */
@Repository
public class ProductJdbc implements ProductRepository {

  @Autowired
  private JdbcTemplate jdbc;

  @Override
  public List<ProductModel> select(String operatorid, int limit, String after)
      throws ParseException {

    SimpleDateFormat format = new SimpleDateFormat(Constants.YYYYMMDD_HHMMSS);
    SimpleDateFormat dataformat = new SimpleDateFormat(Constants.YYYYMMDD);
    List<Map<String, Object>> resultList = null;

    if (after != null) {
      // afterがある場合は、after以降を取得する。
      String sql = "SELECT operatorid , producttraceid , productitem , supplyitemno"
          + " , supplyfuctory , fuctoryaddress , responceinfo, modifiedat, materialstotal, gco2eqtotal, cfpmodifiedat "
          + "FROM public.product WHERE operatorid = ? AND modifiedat < ? "
          + "AND deleteflag IS NOT True ORDER BY modifiedat DESC LIMIT ?";

      // 製品情報Tを検索する。
      resultList = jdbc.queryForList(sql, operatorid, format.parse(after), limit);

    } else {
      String sql = "SELECT operatorid , producttraceid , productitem , supplyitemno"
          + " , supplyfuctory , fuctoryaddress , responceinfo, modifiedat, materialstotal, gco2eqtotal, cfpmodifiedat  "
          + "FROM public.product WHERE operatorid = ? "
          + "AND deleteflag IS NOT True ORDER BY modifiedat DESC LIMIT ?";

      // 製品情報Tを検索する。
      resultList = jdbc.queryForList(sql, operatorid, limit);
    }
    List<ProductModel> entityList = new ArrayList<ProductModel>();

    for (Map<String, Object> resultMap : resultList) {

      ProductModel model = new ProductModel();
      model.setOperatorId((String) resultMap.get("operatorid"));
      model.setProductTraceId((String) resultMap.get("producttraceid"));
      model.setProductItem((String) resultMap.get("productitem"));
      model.setSupplyItemNo((String) resultMap.get("supplyitemno"));
      model.setSupplyFuctory((String) resultMap.get("supplyfuctory"));
      model.setFuctoryAddress((String) resultMap.get("fuctoryaddress"));
      model.setResponceInfo((String) resultMap.get("responceinfo"));
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

  @Override
  public ProductModel select(String operatorId, String productTraceId) {

    SimpleDateFormat format = new SimpleDateFormat(Constants.YYYYMMDD_HHMMSS);

    String sql = "SELECT operatorid , producttraceid , productitem , supplyitemno"
        + " , supplyfuctory , fuctoryaddress , responceinfo, modifiedat, materialstotal, gco2eqtotal, cfpmodifiedat  FROM public.product WHERE"
        + " operatorid = ? AND producttraceid = ? AND deleteflag IS NOT True ";

    Map<String, Object> resultMap = jdbc.queryForMap(sql, operatorId, productTraceId);

    ProductModel productModel = new ProductModel();
    productModel.setOperatorId((String) resultMap.get("operatorid"));
    productModel.setProductTraceId((String) resultMap.get("producttraceid"));
    productModel.setProductItem((String) resultMap.get("productitem"));
    productModel.setSupplyItemNo((String) resultMap.get("supplyitemno"));
    productModel.setSupplyFuctory((String) resultMap.get("supplyfuctory"));
    productModel.setFuctoryAddress((String) resultMap.get("fuctoryaddress"));
    productModel.setResponceInfo((String) resultMap.get("responceinfo"));
    productModel.setMaterialsTotal(ConverterUtil.toString((BigDecimal) resultMap.get("materialstotal")));
    productModel.setGco2eqTotal(ConverterUtil.toString((BigDecimal) resultMap.get("gco2eqtotal")));
    if ((Date) resultMap.get("cfpmodifiedat") != null) {
      productModel.setCfpModifieDat(format.format((Date) resultMap.get("cfpmodifiedat")));
    }

    return productModel;

  }

  @Override
  public int insert(ProductModel productModel) {

    Date nowDate = new Date();

    int result = jdbc.update(
        "INSERT INTO product(operatorid,producttraceid,productitem,supplyitemno,supplyfuctory,"
            + "fuctoryaddress,responceinfo,createdat,modifiedat,deleteflag)"
            + "VALUES ( ?,?,?,?,?,?,?,?,?,?)",
        productModel.getOperatorId(), productModel.getProductTraceId(), productModel.getProductItem(),
        productModel.getSupplyItemNo(), productModel.getSupplyFuctory(), productModel.getFuctoryAddress(),
        productModel.getResponceInfo(), nowDate, nowDate, false);

    return result;
  }

  @Override
  public int updateForCfpInfo(ProductModel productModel) {

    String sql = "UPDATE public.product SET materialstotal = ? , gco2eqtotal = ? , cfpmodifiedat = ? ,"
        + "modifiedat = ? WHERE operatorid = ? and producttraceid = ?";

    int result = jdbc.update(sql, new BigDecimal(productModel.getMaterialsTotal()),
        new BigDecimal(productModel.getGco2eqTotal()), new Date(), new Date(), productModel.getOperatorId().trim(),
        productModel.getProductTraceId());

    return result;
  }
  
  @Override
  public int update(ProductModel productModel) {

    String sql = "UPDATE public.product SET productitem = ? , supplyitemno = ? , supplyfuctory = ? ,"
        + "fuctoryaddress = ? , responceinfo = ?, modifiedat = ? WHERE operatorid = ? and producttraceid = ?";

    int result = jdbc.update(sql, productModel.getProductItem(), productModel.getSupplyItemNo(), 
        productModel.getSupplyFuctory(), productModel.getFuctoryAddress(), productModel.getResponceInfo(), 
        new Date(),  productModel.getOperatorId().trim(), productModel.getProductTraceId());

    return result;
  }
}
