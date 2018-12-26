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
@DatabaseTable(tableName = "plan")
public class Plan {
    public static final String Id_FIELD_NAME = "ID";
    public static final String PlanId_FIELD_NAME = "PlanID";
    public static final String PlanType_FIELD_NAME = "PlanType";
    public static final String Owner_FIELD_NAME = "Owner";
    public static final String Description_FIELD_NAME = "Description";
    public static final String MKV_FIELD_NAME = "MKV";

    @DatabaseField(id = true, canBeNull = false, columnName = Id_FIELD_NAME)
    private String ID;

    @DatabaseField(columnName = PlanId_FIELD_NAME)
    private String PlanID;

    @DatabaseField(columnName = PlanType_FIELD_NAME)
    private String PlanType;

    @DatabaseField(columnName = Owner_FIELD_NAME)
    private String Owner;

    @DatabaseField(columnName = Description_FIELD_NAME)
    private String Description;

    @DatabaseField(columnName = MKV_FIELD_NAME)
    private String MKV;

    public String getMKV() {
        return MKV;
    }

    public void setMKV(String MKV) {
        this.MKV = MKV;
    }

    public String getPlanID() {
        return PlanID;
    }

    public void setPlanID(String planID) {
        PlanID = planID;
    }

    public String getPlanType() {
        return PlanType;
    }

    public void setPlanType(String planType) {
        PlanType = planType;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }



    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Plan() {
    }

    private static List<Plan> _DATA_Plan = null;
    private static Plan _DATA_OBJ_Plan = null;

    public Plan(String planID, String planType, String owner, String description, String MKV) {
        PlanID = planID;
        PlanType = planType;
        Owner = owner;
        Description = description;
        this.MKV = MKV;
    }

    public static List<Plan> Load_Plan(Dao<Plan,String> _plan){
        try {
            QueryBuilder<Plan, String> queryBuilder = _plan.queryBuilder();
//            queryBuilder.where().like(Plan.PlanId_FIELD_NAME,"test");
            PreparedQuery<Plan> preparedQuery = queryBuilder.prepare();
            _DATA_Plan = _plan.query(preparedQuery);
        }catch (Exception ex){
            _DATA_Plan = null;
            ex.printStackTrace();
        }
        return _DATA_Plan;
    }

    public static Plan Load_Plan_By_ID(Dao<Plan,String> _plan,String PlanID){
        try {
            QueryBuilder<Plan, String> queryBuilder = _plan.queryBuilder();
            queryBuilder.where().eq(Plan.PlanId_FIELD_NAME,PlanID);
            PreparedQuery<Plan> preparedQuery = queryBuilder.prepare();
            _DATA_OBJ_Plan = _plan.query(preparedQuery).get(0);
        }catch (Exception ex){
            _DATA_OBJ_Plan = null;
            ex.printStackTrace();
        }
        return _DATA_OBJ_Plan;
    }

}
