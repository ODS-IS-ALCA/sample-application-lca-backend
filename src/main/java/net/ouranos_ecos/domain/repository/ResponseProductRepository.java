package net.ouranos_ecos.domain.repository;

import java.text.ParseException;
import java.util.List;

import net.ouranos_ecos.domain.model.CfpResponseModel;
import net.ouranos_ecos.domain.model.ProductModel;
import net.ouranos_ecos.domain.model.ResponseProductModel;

public interface ResponseProductRepository {

  /**
   * 回答製品情報（responseproduct）登録
   * 
   * @param cfpResponseModel
   * @param responseid
   * @param productModel
   * @return int
   * @throws ParseException
   */
  int insertCfpResponse(CfpResponseModel cfpResponseModel, String responseid, ProductModel productModel)
      throws ParseException;
  
  /**
   * 回答製品情報Tを取得
   * 
   * @param operatorid
   * @param limit
   * @param after
   * @return List<ResponseProductModel>
   * @throws ParseException
   */
  List<ResponseProductModel> select(String operatorid, int limit, String after)
      throws ParseException;

}
