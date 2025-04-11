package net.ouranos_ecos.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LCA部品構成情報
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LcaPartsStructureModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 内部事業者識別子
  private String operatorId;

  // トレース識別子
  private String traceId;

  // 品番
  private String partsName;

  // 品名
  private String partsLabelName;

  // 補助項目
  private String supportPartsName;

  // 構成品レベル
  private Integer partsStructureLevel;

  // 個数
  private BigDecimal number;

  // 質量
  private BigDecimal mass;

  // 合計質量
  private BigDecimal totalMass;

  // 材料コード
  private String materialCd;

  // 材料規格
  private String materialStandard;

  // 材料分類
  private String materialCategory;

  // LCA材料コード
  private String lcaMaterialCd;

  // 部品調達コード
  private String partsProcurementCd;

  // 材料調達コード
  private String materiaProcurementCd;

  // 終端フラグ
  private Boolean endFlag;

  // 最下層フラグ
  private Boolean bottomLayerFlag;

  // 製品トレース識別子
  private String productTraceId;

  // 行番号
  private Integer rowNo;

  // 登録日時
  private String createDat;

  // 更新日時
  private String modifieDat;

  // 論理削除フラグ
  private boolean deleteFlag;
  
  // 依頼対象フラグ
  private Boolean requestTargetFlag;
  
  // 依頼フラグ
  private Boolean requestFlag;

}
