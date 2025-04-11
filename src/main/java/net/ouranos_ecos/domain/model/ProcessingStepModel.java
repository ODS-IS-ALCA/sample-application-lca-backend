package net.ouranos_ecos.domain.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 加工工程モデル
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessingStepModel implements Serializable {

	private static final long serialVersionUID = 1L;

	// 加工工程コード
	private String processingStepCd;

	// 加工工程名称
	private String processingStepName;
}
