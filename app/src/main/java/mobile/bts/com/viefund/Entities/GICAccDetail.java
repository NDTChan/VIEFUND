package mobile.bts.com.viefund.Entities;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 3/26/2018.
 */

public class GICAccDetail {
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

    public GICAccDetail() {
    }

    public GICAccDetail(String ID, String accountID, String infoLabel, String infoContent) {
        this.ID = ID;
        AccountID = accountID;
        InfoLabel = infoLabel;
        InfoContent = infoContent;
    }
    private static List<GICAccDetail> _DATA_GIC_Account_Detail = null;
    public static List<GICAccDetail> Load_GIC_Account_Detail_By_AccountID(Dao<GICAccDetail,String> _planGICAccDetail, String AccountID){
        try {
            Log.d("GIC Account Detail", "Load_GIC_Account_Detail_By_PlanID: "+AccountID);
            QueryBuilder<GICAccDetail, String> queryBuilder = _planGICAccDetail.queryBuilder();
            queryBuilder.where().eq(AccountInfo.AccountID_FIELD_NAME,AccountID);
            PreparedQuery<GICAccDetail> preparedQuery = queryBuilder.prepare();
            _DATA_GIC_Account_Detail = _planGICAccDetail.query(preparedQuery);
        }catch (Exception ex){
            _DATA_GIC_Account_Detail = null;
            ex.printStackTrace();
        }
        return _DATA_GIC_Account_Detail;
    }
}
