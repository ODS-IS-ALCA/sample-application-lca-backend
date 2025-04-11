package net.ouranos_ecos.domain.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsvInputMaterialData implements Serializable {

  private static final long serialVersionUID = 1L;

  // 鉄
  @JsonProperty("iron")
  private String iron;
  // アルミ
  @JsonProperty("aluminum")
  private String aluminum;
  // 銅
  @JsonProperty("copper")
  private String copper;
  // 非鉄金属
  @JsonProperty("nonFerrousMetals")
  private String nonFerrousMetals;
  // 樹脂
  @JsonProperty("resin")
  private String resin;
  // その他
  @JsonProperty("others")
  private String others;
  // 材料合計
  @JsonProperty("materialsTotal")
  private String materialsTotal;
}
