package net.ouranos_ecos.exception_handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * REST APIのエラーレスポンスのクラス。
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiDataDistributionErrorResponse {

    public String code;
    public String message;
    public String detail;
  }
