package net.ouranos_ecos.domain.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 依頼・回答情報情報
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CfpRequestResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  // 依頼情報
  private CfpResponseTransModel cfpResponseTransModel;
  
  // 回答製品情報
  private List<CfpResponseProductModel> cfpResponseProductModel;
  
}
