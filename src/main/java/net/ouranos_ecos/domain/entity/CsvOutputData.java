package net.ouranos_ecos.domain.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsvOutputData implements Serializable {

  private static final long serialVersionUID = 1L;

  // 部品加工-内製
  @JsonProperty("partsIn")
  private String partsIn;
  // 部品加工-外製
  @JsonProperty("partsOut")
  private String partsOut;
  // 材料製造-鉄
  @JsonProperty("materialIron")
  private String materialIron;
  // 材料製造-アルミ
  @JsonProperty("materialAluminum")
  private String materialAluminum;
  // 材料製造-銅
  @JsonProperty("materialCopper")
  private String materialCopper;
  // 材料製造-非鉄金属
  @JsonProperty("materialNonFerrousMetals")
  private String materialNonFerrousMetals;
  // 材料製造-樹脂
  @JsonProperty("materialResin")
  private String materialResin;
  // 材料製造-その他
  @JsonProperty("materialOthers")
  private String materialOthers;
  // 計
  @JsonProperty("subTotal")
  private String subTotal;
  // 資材製造
  @JsonProperty("resources")
  private String resources;
  // 輸送-材料輸送
  @JsonProperty("transportMaterial")
  private String transportMaterial;
  // 輸送-部品輸送
  @JsonProperty("transportParts")
  private String transportParts;
  // 廃棄
  @JsonProperty("waste")
  private String waste;
  // 合計
  @JsonProperty("total")
  private String total;

}
