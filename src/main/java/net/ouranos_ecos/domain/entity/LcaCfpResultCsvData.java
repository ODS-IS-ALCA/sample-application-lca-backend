package net.ouranos_ecos.domain.entity;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LcaCfpResultCsvData implements Serializable {

  private static final long serialVersionUID = 1L;

  private CsvProductData csvProductData;

  private CsvInputMaterialData csvInputMaterialData;

  private List<CsvInputEnegryData> csvInputEnegryData;

  private CsvOutputData csvOutputData;

  private List<CsvCfpInfoData> csvCfpInfoData;
}
