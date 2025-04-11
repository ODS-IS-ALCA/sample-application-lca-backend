package net.ouranos_ecos.domain.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 生産国モデル
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductionCountryModel implements Serializable {

	private static final long serialVersionUID = 1L;

	// 生産国コード
	private String productionCountryCd;

	// 生産国名称
	private String productionCountryName;
}
