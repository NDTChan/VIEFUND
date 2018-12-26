package mobile.bts.com.viefund.Entities;

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
public class PlantAccount  {
    public static final String PlanID_FIELD_NAME = "PlanID";
    public static final String Type_FIELD_NAME = "Type";
    public static final String ID_FIELD_NAME = "ID";
    public static final String AccountID_FIELD_NAME = "AccountID";
    public static final String AccountDesc_FIELD_NAME = "AccountDesc";
    public static final String MKV_FIELD_NAME = "MKV";

    @DatabaseField(id = true,canBeNull = false,columnName = ID_FIELD_NAME)
    private String ID;

    @DatabaseField(columnName = PlanID_FIELD_NAME)
    private String PlanID;

    @DatabaseField(columnName = Type_FIELD_NAME)
    private String Type;
    @DatabaseField(columnName = AccountID_FIELD_NAME)
    private String AccountID;
    @DatabaseField(columnName = AccountDesc_FIELD_NAME)
    private String AccountDesc;
    @DatabaseField(columnName = MKV_FIELD_NAME)
    private String MKV;


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPlanID() {
        return PlanID;
    }

    public void setPlanID(String planID) {
        PlanID = planID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getAccountID() {
        return AccountID;
    }

    public void setAccountID(String accountID) {
        AccountID = accountID;
    }

    public String getAccountDesc() {
        return AccountDesc;
    }

    public void setAccountDesc(String accountDesc) {
        AccountDesc = accountDesc;
    }

    public String getMKV() {
        return MKV;
    }

    public void setMKV(String MKV) {
        this.MKV = MKV;
    }

    public PlantAccount() {
    }

    public PlantAccount(String ID, String planID, String type, String accountID, String accountDesc, String MKV) {
        this.ID = ID;
        PlanID = planID;
        Type = type;
        AccountID = accountID;
        AccountDesc = accountDesc;
        this.MKV = MKV;
    }

    private static List<PlantAccount> _DATA_Plan_Acount = null;
    public static List<PlantAccount> Load_Plan_By_PlanID(Dao<PlantAccount,String> _planAcount,String PlanID){
        try {
            QueryBuilder<PlantAccount, String> queryBuilder = _planAcount.queryBuilder();
            queryBuilder.where().eq(PlantAccount.PlanID_FIELD_NAME,PlanID);
            PreparedQuery<PlantAccount> preparedQuery = queryBuilder.prepare();
            _DATA_Plan_Acount = _planAcount.query(preparedQuery);
        }catch (Exception ex){
            _DATA_Plan_Acount = null;
            ex.printStackTrace();
        }
        return _DATA_Plan_Acount;
    }


}
