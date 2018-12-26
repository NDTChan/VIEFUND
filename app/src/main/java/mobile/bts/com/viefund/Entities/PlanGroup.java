package mobile.bts.com.viefund.Entities;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by Administrator on 4/13/2018.
 */
@DatabaseTable
public class PlanGroup {
    public static final String Id_FIELD_NAME = "ID";
    public static final String PlanID_FIELD_NAME = "PlanID";
    public static final String AccountType_FIELD_NAME = "AccountType";
    public static final String Description_FIELD_NAME = "Description";
    public static final String AccountGroupTotal_FIELD_NAME = "AccountGroupTotal";

    @DatabaseField(id = true, canBeNull = false, columnName = Id_FIELD_NAME)
    private String ID;
    @DatabaseField(columnName = PlanID_FIELD_NAME)
    private String PlanID;
    @DatabaseField(columnName = AccountType_FIELD_NAME)
    private String AccountType;
    @DatabaseField(columnName = Description_FIELD_NAME)
    private String Description;
    @DatabaseField(columnName = AccountGroupTotal_FIELD_NAME)
    private String AccountGroupTotal;

    public PlanGroup() {
    }

    public PlanGroup(String ID, String accountType, String description, String accountGroupTotal) {
        this.ID = ID;
        AccountType = accountType;
        Description = description;
        AccountGroupTotal = accountGroupTotal;
    }

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

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAccountGroupTotal() {
        return AccountGroupTotal;
    }

    public void setAccountGroupTotal(String accountGroupTotal) {
        AccountGroupTotal = accountGroupTotal;
    }

    private static List<PlanGroup> _DATA_PlanGroup = null;

    public static List<PlanGroup> Load_PlanGroup(Dao<PlanGroup, String> _planGroup, String PlanID) {
        try {
            QueryBuilder<PlanGroup, String> queryBuilder = _planGroup.queryBuilder();
            queryBuilder.where().eq(PlanGroup.PlanID_FIELD_NAME, PlanID);
            PreparedQuery<PlanGroup> preparedQuery = queryBuilder.prepare();
            _DATA_PlanGroup = _planGroup.query(preparedQuery);
        } catch (Exception ex) {
            _DATA_PlanGroup = null;
            ex.printStackTrace();
        }
        return _DATA_PlanGroup;
    }
}
