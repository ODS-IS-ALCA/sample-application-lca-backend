package net.ouranos_ecos.domain.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LCA材料情報
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LcaMaterialModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // LCA材料番号
  private int lcaMaterialNo;

  // LCA材料コード
  private String lcaMaterialCd;

  // LCA材料名称
  private String lcaMaterialName;
}