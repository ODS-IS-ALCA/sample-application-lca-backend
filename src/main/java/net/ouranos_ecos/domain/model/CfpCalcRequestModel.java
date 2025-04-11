package net.ouranos_ecos.domain.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CFP算出依頼情報
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CfpCalcRequestModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 製品情報
  private ProductModel productModel;

  // 依頼リスト情報
  private List<CalcRequestModel> calcRequestModel;

}
