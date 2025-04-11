package net.ouranos_ecos.api.lca_csv;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.ouranos_ecos.domain.service.LcaCfpResultCsvService;

/**
 * CSVファイルの受け渡し
 */

@RestController
@RequestMapping("/api/v1/lca_csv")
public class LcaCsvController {

  @Inject
  LcaCfpResultCsvService lcaCfpResultCsvService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<byte[]> get(HttpServletRequest request) {

    String dataTarget = request.getParameter("dataTarget");

    switch (dataTarget) {

      case "lcaCfpResultCsv" :
        // 計算結果DL取得
        return lcaCfpResultCsvService.getRequest(request);

    }
    return null;
  }

}
