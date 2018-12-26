package mobile.bts.com.viefund.Entities;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * BT Company
 * Created by Administrator on 3/19/2018.
 */
@DatabaseTable()
public class PlanAsset {
    public static final String Id_FIELD_NAME = "ID";
    public static final String PlanId_FIELD_NAME = "PlanID";
    public static final String Amount_FIELD_NAME = "Amount";
    public static final String Description_FIELD_NAME = "Description";
    public static final String Percentage_FIELD_NAME = "Percentage";

    @DatabaseField(id = true, canBeNull = false, columnName = PlanId_FIELD_NAME)
    private String PlanID;

    @DatabaseField(columnName = Amount_FIELD_NAME)
    private double Amount;

    @DatabaseField(columnName = Description_FIELD_NAME)
    private String Description;

    @DatabaseField(columnName = Percentage_FIELD_NAME)
    private double MKV;

    public String getPlanID() {
        return PlanID;
    }

    public void setPlanID(String planID) {
        PlanID = planID;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getMKV() {
        return MKV;
    }

    public void setMKV(double MKV) {
        this.MKV = MKV;
    }

    public PlanAsset() {
    }

    public PlanAsset(String planID, double amount, String description, double MKV) {
        PlanID = planID;
        Amount = amount;
        Description = description;
        this.MKV = MKV;
    }

    private static List<PlanAsset> _DATA_Plan_Asset = null;

    public static List<PlanAsset> Load_Plan_Asset_By_PlanID(Dao<PlanAsset,String> _planAsset, String PlanID){
        try {
            QueryBuilder<PlanAsset, String> queryBuilder = _planAsset.queryBuilder();
            queryBuilder.where().eq(PlanAsset.PlanId_FIELD_NAME,PlanID);
            PreparedQuery<PlanAsset> preparedQuery = queryBuilder.prepare();
            _DATA_Plan_Asset = _planAsset.query(preparedQuery);
        }catch (Exception ex){
            _DATA_Plan_Asset = null;
            ex.printStackTrace();
        }
        return _DATA_Plan_Asset;
    }

}
