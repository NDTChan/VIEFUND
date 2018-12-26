package mobile.bts.com.viefund.Entities;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * BT Company
 * Created by Administrator on 3/12/2018.
 */
@DatabaseTable
public class AccountFundPerformance {
    public static final String ID_FIELD_NAME = "ID";
    public static final String AccountID_FIELD_NAME = "AccountID";
    public static final String InfoLabel_FIELD_NAME = "Date";
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

    public AccountFundPerformance() {
    }

    public AccountFundPerformance(String ID, String accountID, String infoLabel, String infoContent) {
        this.ID = ID;
        AccountID = accountID;
        InfoLabel = infoLabel;
        InfoContent = infoContent;
    }

    private static List<AccountFundPerformance> _DATA_AccountFundPerformance = null;
    public static List<AccountFundPerformance> Load_AccountFundPerformance_By_AccountID(Dao<AccountFundPerformance,String> _planAccountFundPerformance, String AccountID){
        try {
            Log.d("AccountFundPerformance", "Load_FundPerformance_By_PlanID: "+AccountID);
            QueryBuilder<AccountFundPerformance, String> queryBuilder = _planAccountFundPerformance.queryBuilder();
            queryBuilder.where().eq(AccountFundPerformance.AccountID_FIELD_NAME,AccountID);
            PreparedQuery<AccountFundPerformance> preparedQuery = queryBuilder.prepare();
            _DATA_AccountFundPerformance = _planAccountFundPerformance.query(preparedQuery);
        }catch (Exception ex){
            _DATA_AccountFundPerformance = null;
            ex.printStackTrace();
        }
        return _DATA_AccountFundPerformance;
    }

}
