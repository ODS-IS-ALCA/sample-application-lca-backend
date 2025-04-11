package net.ouranos_ecos.domain.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * 定数クラス
 */
@Component
public class Constants {

  // 日付フォーマット_YYYYMMDD
  public static final String YYYYMMDD = "yyyy/MM/dd";
  
  // 日付フォーマット_YYYYMMDD_HHMMSS
  public static final String YYYYMMDD_HHMMSS = "yyyy/MM/dd HH:mm:ss";

  // 登録完了メッセージ
  public static final String SUCSESS_MESSAGE = "登録完了しました。";

  // 登録失敗メッセージ
  public static final String UNSUCSESS_MESSAGE = "登録失敗しました。";

  // 測定方法_実測
  public static final String MEASURE_METHOD_JISOKU = "実測";

  // 測定方法_簡易
  public static final String MEASURE_METHOD_KANI = "簡易";

  // コード01
  public static final String CODE_01 = "01";

  // コード02
  public static final String CODE_02 = "02";

  // LCA材料コード_パターン"1_"
  public static final String LCA_MATERIAL_CD_1 = "1_";

  // LCA材料コード_パターン"2_"
  public static final String LCA_MATERIAL_CD_2 = "2_";

  // LCA材料コード_パターン"3_"
  public static final String LCA_MATERIAL_CD_3 = "3_";

  // LCA材料コード_パターン"4_"
  public static final String LCA_MATERIAL_CD_4 = "4_";

  // LCA材料コード_パターン"5_"
  public static final String LCA_MATERIAL_CD_5 = "5_";

  // 国コード_JAPAN "15"
  public static final String COUNTRY_CD_JAPAN = "15";

  // 改行コード
  public static final String NEW_LINE = "\n";

  // インプット情報
  public static final String INPUT_INFO = "インプット情報";

  // アウトプット情報
  public static final String OUTPUT_INFO = "アウトプット情報(g-CO2eq)";

  // 部品構成・CFP詳細情報
  public static final String CFP_INFO = "部品構成・CFP詳細情報";

  // 部品・材料調達コード
  public static final Map<String, String> PROCURREMENT = Map.of("01", "自給", "02", "支給", "03", "集中購買");

  // 内・外製区分コード
  public static final Map<String, String> MANUFACTURING = Map.of("01", "内製", "02", "外製");

  // 燃料コード
  public static final Map<String, String> FUEL_TYPE = Map.of("01", "ガソリン", "02", "軽油");

  // 燃料コード
  public static final Map<String, String> TONKG_TYPE = Map.of("01", "1000-2000kg(軽油)", "02", "4000-6000kg(軽油)", "03",
      "8000-10000kg(軽油)", "04", "1500kgt(ガソリン)");

  // CFP依頼フラグのチェック失敗メッセージ
  public static final String CFPFLAG_CHECK_NG_MSG = "下記品番行については、LCA材料名称が選択されている、または、途中階層のため、CFP依頼フラグはチェック出来ません。";

  // CsvDL (製品情報ヘッダー)
  public static final List<String> PRODUCT_HEADER = new ArrayList<String>(
      Arrays.asList("事業者識別子", "製品トレース識別子", "製品名", "納入品番", "納入工場", "生産工場所在地", "回答者情報"));

  // CsvDL （インプットヘッダー①)
  public static final List<String> INPUT_MATRIALS_HEADER = new ArrayList<String>(
      Arrays.asList("鉄(g)", "アルミ(g)", "鋼(g)", "非鉄金属(g)", "樹脂(g)", "その他(g)", "材料合計(g)"));

  // CsvDL (インプットヘッダー②)
  public static final List<String> INPUT_ENEGRY_HEADER = new ArrayList<String>(
      Arrays.asList("", "電力(kWh)", "A重油(L)", "C重油(L)", "灯油(L)", "軽油(L)", "ガソリン(L)", "天然ガス液(NGL)(L)", "液化石油ガス(LPG)(L)",
          "天然ガス(LNG)(L)", "都市ガス(㎥)", "追加①(L)", "追加②(kg)"));

  // CsvDL （アウトプットヘッダー)
  public static final List<String> OUTPUT_HEADER = new ArrayList<String>(Arrays.asList("部品加工内製", "部品加工外製", "鉄", "アルミ",
      "鋼", "非鉄金属", "樹脂", "その他", "材料製造合計", "資材製造", "材料輸送", "部品輸送", "廃棄", "Scope1/2/3合計"));

  // CsvDL （CFP情報ヘッダー)
  public static final List<String> CFP_INFO_HEADER = new ArrayList<String>(Arrays.asList("品番", "品名", "補助項目", "構成品レベル",
      "個数", "質量", "合計質量", "材料コード", "材料規格", "材料分類", "LCA材料コード", "注釈", "部品調達コード", "材料調達コード", "製品トレース識別子", "CFP情報識別子",
      "トレース識別子", "素材報告値", "素材測定方法", "素材生産国コード", "素材リサイクル分類PIR", "素材リサイクル分類PCRELV", "素材リサイクル分類PCR他産業", "素材リサイクル分類分類不可",
      "素材リサイクル分類合計", "素材重量計算歩留り率", "素材重量計算投入質量", "素材直接排出原単位排出量", "素材直接排出GHG排出", "素材電力排出エネルギー比率", "素材電力排出電力原単位",
      "素材電力排出消費電力", "素材電力排出電力GHG排出", "加工報告値", "加工測定方法", "加工生産国コード", "加工製造区分", "加工1コード", "加工2コード", "加工3コード", "加工4コード",
      "加工消費電力エネルギー比率", "加工消費電力原単位", "加工消費電力量", "加工消費エネルギー量A重油", "加工消費エネルギー量C重油", "加工消費エネルギー量灯油", "加工消費エネルギー量軽油",
      "加工消費エネルギー量ガソリン", "加工消費エネルギー量NGL", "加工消費エネルギー量LPG", "加工消費エネルギー量LNG", "加工消費エネルギー量都市ガス", "加工消費エネルギー量フリー1",
      "加工消費エネルギー量フリー2", "加工その他", "資材報告値", "資材測定方法", "資材工業用水道", "資材上水道", "資材圧縮空気(15m3/ min)", "資材圧縮空気(90m3/ min)",
      "資材シンナー", "資材アンモニア", "資材硝酸", "資材か性ソーダ", "資材塩酸", "資材アセチレン", "資材その他の無機化学工業製品", "資材硫酸", "資材無水クロム酸", "資材その他の有機化学工業製品",
      "資材その他の洗浄剤", "資材セルロース系接着剤", "資材潤滑油 (グリースを含む)", "資材Free①", "資材Free②", "廃棄物報告値", "廃棄物測定方法", "廃棄物燃え殻", "廃棄物鉱業等無機性汚泥",
      "廃棄物製造業有機性汚泥", "廃棄物製造排出廃プラスチック", "廃棄物金属くず", "廃棄物陶磁器くず", "廃棄物鉱さい", "廃棄物ばいじん", "廃棄物石油由来廃油", "廃棄物天然繊維くず", "廃棄物ゴムくず",
      "廃棄物廃酸", "廃棄物廃アルカリ", "廃棄物フリー1", "廃棄物フリー2", "輸送材料報告値", "輸送部品報告値", "輸送測定方法", "輸送重量材料投入質量", "輸送重量材料排出量",
      "輸送重量部品合計質量", "輸送重量部品排出量", "輸送燃料材料燃料種別", "輸送燃料材料燃料使用量", "輸送燃料材料排出量", "輸送燃料部品燃料種別", "輸送燃料部品燃料使用量", "輸送燃料部品排出量",
      "輸送燃費材料燃料種別", "輸送燃費材料走行距離", "輸送燃費材料燃費", "輸送燃費材料排出量", "輸送燃費部品燃料種別", "輸送燃費部品走行距離", "輸送燃費部品燃費", "輸送燃費部品排出量",
      "輸送トンキロ材料輸送種別", "輸送トンキロ材料輸送距離", "輸送トンキロ材料排出量", "輸送トンキロ部品輸送種別", "輸送トンキロ部品輸送距離", "輸送トンキロ部品排出量"));

  // HTTPステータス 正常
  public static final String HTTP_STATUS_NORMAL = "200";
  
  // HTTPステータス 正常
  public static final String HTTP_STATUS_CREATED = "201";

  // HTTPステータス 約款未同意
  public static final String HTTP_STATUS_NO_CLAUSE = "415";

  // 原単位DB認証処理結果 成功
  public static final String UNITDB_CERTIFICATION_RESULT_SUCCESS = "1";

  // 原単位DB認証処理結果 約款未同意
  public static final String UNITDB_CERTIFICATION_RESULT_CLAUSE_NO_AGREE = "2";

  // 原単位DB認証処理結果 失敗
  public static final String UNITDB_CERTIFICATION_RESULT_FAILURE = "3";

}
