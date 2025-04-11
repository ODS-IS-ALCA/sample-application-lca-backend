package net.ouranos_ecos.domain.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 値変換の共通処理クラス
 */
@Component
public class ConverterUtil {

  /**
   * 3桁ずつカンマで区切る
   * 
   * @param val
   * @return
   */
  public static String formatCommas(BigDecimal val) {
    if (val == null) {
      return "0";
    }
    DecimalFormat decimalFormat = new DecimalFormat("#,###");
    return decimalFormat.format(val);
  }

  /**
   * StringからBigDecimalに変換する
   * 値が無ければ0に変換する
   * 
   * @param val
   * @return
   */
  public static BigDecimal valueOrZero(String val) {
    return StringUtils.hasText(val) ? new BigDecimal(val) : BigDecimal.ZERO;
  }

  /**
   * 値が無ければ0に変換する。
   * 
   * @param val
   * @return
   */
  public static BigDecimal valueOrZero(BigDecimal val) {
    return val != null ? val : BigDecimal.ZERO;
  }

  /**
   * BigDecimalがNULLの場合、ゼロで返す
   * 
   * @param lcaCfpModel
   * @return LcaCfpModel
   * @throws DataAccessException
   */
  public static BigDecimal toNumber(BigDecimal num) {
    return Optional.ofNullable(num).orElse(BigDecimal.ZERO);
  }

  /**
   * BigDecimalをStringに変換する
   * 
   * @param val
   * @return
   */
  public static String toString(BigDecimal val) {
    return val == null ? "" : val.toString();
  }

  /**
   * 別クラスの同名フィールドをコピーする
   * 
   * @param source
   *          コピー元のインスタンス
   * @param target
   *          コピー先のインスタンス
   */
  public static void copyFields(Object source, Object target) {
    final String prefixSet = "set";
    final String prefixGet = "get";

    Set<String> methodSet = new HashSet<String>();
    for (Method method : target.getClass().getMethods())
      if (method.getName().startsWith(prefixSet))
        methodSet.add(method.getName().substring(prefixSet.length()));

    try {
      for (Method method : source.getClass().getMethods()) {
        if (!method.getName().startsWith(prefixGet))
          continue;
        String curName = method.getName().substring(prefixGet.length());
        if (methodSet.contains(curName)) {
          Method newMethod = target.getClass().getMethod(prefixSet + curName, method.getReturnType());
          newMethod.invoke(target, method.invoke(source));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Object型の要素をcsv形式に成型する
   * 
   * @param obj
   * @param headers
   * @return String
   */
  public static <T> String convertObjectToCsv(T obj, List<String> headers) {

    StringBuilder csvBuilder = new StringBuilder();
    Field[] fields = obj.getClass().getDeclaredFields();

    // ヘッダー行を追加
    StringJoiner headerJoiner = new StringJoiner(",");
    for (String header : headers) {
      headerJoiner.add("\"" + header + "\"");
    }
    csvBuilder.append(headerJoiner.toString()).append("\n");

    // データ行の追加
    StringJoiner dataJoiner = new StringJoiner(",");
    for (Field field : fields) {
      if (field.getName() == "serialVersionUID")
        continue;
      field.setAccessible(true);
      try {
        Object value = field.get(obj);
        dataJoiner.add(value != null ? "\"" + value.toString() + "\"" : "");
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    csvBuilder.append(dataJoiner.toString()).append("\n");
    return csvBuilder.toString();
  }

  /**
   * Object型のリストをcsv形式に成型する
   * 
   * @param objs
   * @param headers
   * @return String
   */
  public static <T> String convertObjectToCsv(List<T> objs, List<String> headers) {

    StringBuilder csvBuilder = new StringBuilder();
    Field[] fields = objs.get(0).getClass().getDeclaredFields();

    // ヘッダー行を追加
    StringJoiner headerJoiner = new StringJoiner(",");
    for (String header : headers) {
      headerJoiner.add("\"" + header + "\"");
    }
    csvBuilder.append(headerJoiner.toString()).append("\n");

    // 各オブジェクトのデータ行を追加
    for (T obj : objs) {
      // 文字列の前後に「"」、末尾に「"」を追加
      StringJoiner dataJoiner = new StringJoiner(",");
      for (Field field : fields) {
        if (field.getName() == "serialVersionUID")
          continue;
        field.setAccessible(true);
        try {
          Object value = field.get(obj);
          dataJoiner.add(value != null ? "\"" + value.toString() + "\"" : "");
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
      csvBuilder.append(dataJoiner.toString()).append("\n");
    }
    return csvBuilder.toString();
  }

  /**
   * 小数点6位以降を切り上げ
   * 
   * @param value
   * @return
   */
  public static BigDecimal roundUpToSixDecimalPlaces(BigDecimal value) {

    if (value == null) {
      return value;
    }

    // 小数点6位以降を切り上げ
    return value.setScale(5, RoundingMode.CEILING);

  }
}
