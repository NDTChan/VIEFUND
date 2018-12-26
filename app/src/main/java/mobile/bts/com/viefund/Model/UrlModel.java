package mobile.bts.com.viefund.Model;

/**
 * Created by Administrator on 3/8/2018.
 */

public class UrlModel {
   public static String domain = "http://67.55.26.45/VApp/WebClient.asmx/";
//    public static String domain = "https://viefund.partnercenter.ca/VClient/VClient.asmx/";

    public static String Msg_DieSession = "Session expired";
    // link dang nhap vaf dang ki
    public static String url_login = domain + "VALoginJSON";// dang nhap
    public static String url_renew_session = domain + "VASessionRenewJSON";// dang nhap

    //link man hinh main
    public static String url_Portfolio_Plan_List = domain + "VAPortfolioPlanListJSON";// dang nhap

    // link screen plan account
    public static String url_PlanAccount_List = domain + "VAPlanAccountListJSON";

    // link screen plan InFO
    public static String url_PlanInfo_List = domain + "VAPlanInfoJSON";


    // link screen account detail
    public static String url_AccountDetail_List = domain + "VAAccountDetailJSON";

    // link screen barchart
    public static String url_BarChart_Data = domain + "VABarChartDataJSON";

    //link screen piechart
    public static String url_PieChart_Data = domain + "VAPlanPieChartJSON";

    //link screen trxdetail
    public static String url_TrxDetail_Data = domain + "VAFundTrxDetailJSON";

    //link quick access
    public static String url_Quick_Access_Set = domain + "VAQuickAccessSetJSON";
    public static String url_Quick_Access_List = domain + "VAQuickAccessListJSON";

    // link notification
    public static String url_Notification_List = domain + "VANotificationListJSON";
    public static String url_Notification_Info = domain + "VANotificationInfoJSON";
    public static String getUrl_Notification_Send = domain + "VANotificationSendJSON";
    // link setting
    public static String url_setting = domain + "VASettingsJSON";
}
