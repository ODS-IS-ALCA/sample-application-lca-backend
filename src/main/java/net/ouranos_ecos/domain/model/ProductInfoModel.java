package net.ouranos_ecos.domain.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 製品情報
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfoModel implements Serializable {

	private static final long serialVersionUID = 1L;

	// 親部品情報
	private ProductModel productModel;

	// 子部品情報
	private List<LcaPartsStructureModel> lcaPartsStructureModel;
}
