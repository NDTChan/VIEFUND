package mobile.bts.com.viefund.Entities;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by Administrator on 3/26/2018.
 */
@DatabaseTable
public class CashAccDetail {
    public static final String ID_FIELD_NAME = "ID";
    public static final String AccountID_FIELD_NAME = "AccountID";
    public static final String InfoLabel_FIELD_NAME = "InfoLabel";
    public static final String InfoContent_FIELD_NAME = "InfoContent";

    @DatabaseField(id = true, canBeNull = false, columnName = ID_FIELD_NAME)
    private String ID;

    @DatabaseField(columnName = AccountID_FIELD_NAME)
    private String AccountID;

    @DatabaseField(columnName = InfoLabel_FIELD_NAME)
    private String InfoLabel;

    @DatabaseField(columnName = InfoContent_FIELD_NAME)
    private String InfoContent;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAccountID() {
        return AccountID;
    }

    public void setAccountID(String accountID) {
        AccountID = accountID;
    }

    public String getInfoLabel() {
        return InfoLabel;
    }

    public void setInfoLabel(String infoLabel) {
        InfoLabel = infoLabel;
    }

    public String getInfoContent() {
        return InfoContent;
    }

    public void setInfoContent(String infoContent) {
        InfoContent = infoContent;
    }

    public CashAccDetail() {
    }

    public CashAccDetail(String ID, String accountID, String infoLabel, String infoContent) {
        this.ID = ID;
        AccountID = accountID;
        InfoLabel = infoLabel;
        InfoContent = infoContent;
    }
    private static List<CashAccDetail> _DATA_Cash_Account_Detail = null;
    public static List<CashAccDetail> Load_Cash_Account_Detail_By_AccountID(Dao<CashAccDetail,String> _planCashAccDetail, String AccountID){
        try {
            Log.d("Cash Account Detail", "Load_Cash_Account_Detail_By_PlanID: "+AccountID);
            QueryBuilder<CashAccDetail, String> queryBuilder = _planCashAccDetail.queryBuilder();
            queryBuilder.where().eq(AccountInfo.AccountID_FIELD_NAME,AccountID);
            PreparedQuery<CashAccDetail> preparedQuery = queryBuilder.prepare();
            _DATA_Cash_Account_Detail = _planCashAccDetail.query(preparedQuery);
        }catch (Exception ex){
            _DATA_Cash_Account_Detail = null;
            ex.printStackTrace();
        }
        return _DATA_Cash_Account_Detail;
    }
}
