package mobile.bts.com.viefund.Entities;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.List;

/**
 * BT Company
 * Created by Administrator on 3/12/2018.
 */
@DatabaseTable
public class AccountInfo {
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

    public AccountInfo() {
    }

    public AccountInfo(String ID, String accountID, String infoLabel, String infoContent) {
        this.ID = ID;
        AccountID = accountID;
        InfoLabel = infoLabel;
        InfoContent = infoContent;
    }

    private static List<AccountInfo> _DATA_Account_Info = null;
    public static List<AccountInfo> Load_Account_Info_By_AccountID(Dao<AccountInfo,String> _planAccountInfo, String AccountID){
        try {
            Log.d("AccountInfo", "Load_AccountInfo_By_PlanID: "+AccountID);
            QueryBuilder<AccountInfo, String> queryBuilder = _planAccountInfo.queryBuilder();
            queryBuilder.where().eq(AccountInfo.AccountID_FIELD_NAME,AccountID);
            PreparedQuery<AccountInfo> preparedQuery = queryBuilder.prepare();
            _DATA_Account_Info = _planAccountInfo.query(preparedQuery);
        }catch (Exception ex){
            _DATA_Account_Info = null;
            ex.printStackTrace();
        }
        return _DATA_Account_Info;
    }

}
