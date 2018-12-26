package mobile.bts.com.viefund.Entities;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by NDTChan on 3/23/2018.
 */
@DatabaseTable
public class PieChart {
    public static final String ID_FIELD_NAME = "ID";
    public static final String AccountID_FIELD_NAME = "AccountID";
    public static final String InfoLabel_FIELD_NAME = "InfoLabel";
    public static final String InfoContent_FIELD_NAME = "InfoContent";
    public static final String InfoContent2_FIELD_NAME = "InfoContent2";
    public static final String InfoContent2Str_FIELD_NAME = "InfoContent2Str";


    @DatabaseField(id = true, canBeNull = false, columnName = ID_FIELD_NAME)

    private String ID;

    @DatabaseField(columnName = AccountID_FIELD_NAME)
    private String AccountID;

    @DatabaseField(columnName = InfoLabel_FIELD_NAME)
    private String InfoLabel;

    @DatabaseField(columnName = InfoContent_FIELD_NAME)
    private String InfoContent;

    @DatabaseField(columnName = InfoContent2_FIELD_NAME)
    private float InfoContent2;

    @DatabaseField(columnName = InfoContent2Str_FIELD_NAME)
    private String InfoContent2Str;

    public PieChart(String ID, String accountID, String infoLabel, String infoContent, float infoContent2, String infoContent2Str) {
        this.ID = ID;
        AccountID = accountID;
        InfoLabel = infoLabel;
        InfoContent = infoContent;
        InfoContent2 = infoContent2;
        InfoContent2Str = infoContent2Str;
    }

    public PieChart() {
    }

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

    public double getInfoContent2() {
        return InfoContent2;
    }

    public void setInfoContent2(float infoContent2) {
        InfoContent2 = infoContent2;
    }

    public String getInfoContent2Str() {
        return InfoContent2Str;
    }

    public void setInfoContent2Str(String infoContent2Str) {
        InfoContent2Str = infoContent2Str;
    }
}
