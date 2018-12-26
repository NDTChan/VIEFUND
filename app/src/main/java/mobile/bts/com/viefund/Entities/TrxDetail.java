package mobile.bts.com.viefund.Entities;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by NDTChan on 3/26/2018.
 */

public class TrxDetail {
    public static final String ID_FIELD_NAME = "ID";
    public static final String InfoLabel_FIELD_NAME = "InfoLabel";
    public static final String InfoContent_FIELD_NAME = "InfoContent";



    @DatabaseField(id = true, canBeNull = false, columnName = ID_FIELD_NAME)

    private String ID;

    @DatabaseField(columnName = InfoLabel_FIELD_NAME)
    private String InfoLabel;

    @DatabaseField(columnName = InfoContent_FIELD_NAME)
    private String InfoContent;

    public TrxDetail(String ID, String infoLabel, String infoContent) {
        this.ID = ID;
        InfoLabel = infoLabel;
        InfoContent = infoContent;
    }

    public TrxDetail() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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
}
