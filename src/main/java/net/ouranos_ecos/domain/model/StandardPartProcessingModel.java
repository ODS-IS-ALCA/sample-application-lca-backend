package net.ouranos_ecos.domain.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 標準工程_部品加工
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardPartProcessingModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 標準工程_部品加工コード
  private String standardPartCd;

  // カテゴリー
  private String standardPartCategory;

  // 名称
  private String standardPartName;

  // 加工1
  private String standardPartProcessing1;

  // 加工2
  private String standardPartProcessing2;

  // 加工3
  private String standardPartProcessing3;

  // 加工4
  private String standardPartProcessing4;

  // 登録日時
  private String createDat;

  // 更新日時
  private String modifieDat;

  // 論理削除フラグ
  private Boolean deleteFlag;

}