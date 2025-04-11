package net.ouranos_ecos.domain.repository;

import java.text.ParseException;
import java.util.List;

import net.ouranos_ecos.domain.model.ProductModel;

public interface ProductRepository {

  /**
   * 製品情報を取得
   * 
   * @param operatorid
   * @param limit
   * @param after
   * @return List
   * @throws ParseException
   */
  List<ProductModel> select(String operatorid, int limit, String after)
      throws ParseException;

  /**
   * 製品情報を登録
   * 
   * @param productModel
   * @return int
   */
  int insert(ProductModel productModel);

  /**
   * 製品情報のCFP情報(合計値)を更新
   * 
   * @param productModel
   * @return int
   */
  int updateForCfpInfo(ProductModel productModel);
  
  /**
   * 製品情報を更新
   * 
   * @param productModel
   * @return int
   */
  int update(ProductModel productModel);

  /**
   * 製品情報を取得
   * 
   * @param operatorId
   * @param productTraceId
   * @return ProductModel
   * @throws ParseException
   */
  ProductModel select(String operatorId, String productTraceId) throws ParseException;

}
