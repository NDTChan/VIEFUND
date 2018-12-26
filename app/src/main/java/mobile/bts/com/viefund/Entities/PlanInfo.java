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
public class PlanInfo {
    public static final String ID_FIELD_NAME = "ID";
    public static final String PlanID_FIELD_NAME = "PlanID";
    public static final String InfoLabel_FIELD_NAME = "InfoLabel";
    public static final String InfoContent_FIELD_NAME = "InfoContent";

    @DatabaseField(id = true, canBeNull = false, columnName = ID_FIELD_NAME)
    private String ID;

    @DatabaseField(columnName = PlanID_FIELD_NAME)
    private String PlanID;

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

    public String getPlanID() {
        return PlanID;
    }

    public void setPlanID(String planID) {
        PlanID = planID;
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

    public PlanInfo() {
    }

    public PlanInfo(String ID, String planID, String infoLabel, String infoContent) {
        this.ID = ID;
        PlanID = planID;
        InfoLabel = infoLabel;
        InfoContent = infoContent;
    }

    private static List<PlanInfo> _DATA_Plan_Info = null;
    public static List<PlanInfo> Load_PlanInfo_By_PlanID(Dao<PlanInfo,String> _planInfo, String PlanID){
        try {
            QueryBuilder<PlanInfo, String> queryBuilder = _planInfo.queryBuilder();
            queryBuilder.where().eq(PlantAccount.PlanID_FIELD_NAME,PlanID);
            PreparedQuery<PlanInfo> preparedQuery = queryBuilder.prepare();
            _DATA_Plan_Info = _planInfo.query(preparedQuery);
        }catch (Exception ex){
            _DATA_Plan_Info = null;
            ex.printStackTrace();
        }
        return _DATA_Plan_Info;
    }


}
