package net.ouranos_ecos.domain.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperatorModel implements Serializable {

  private static final long serialVersionUID = 1L;

  // 内部事業者識別子
  private String operatorId;

  // 公開法人番号
  private String openOperatorId;

  // 事業者名
  private String operatorName;
}
