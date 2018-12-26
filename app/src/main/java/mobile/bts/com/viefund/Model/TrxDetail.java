package mobile.bts.com.viefund.Model;

/**
 * Created by NDTChan on 3/26/2018.
 */

public class TrxDetail {
    private String InfoLabel;
    private String InfoContent;

    public TrxDetail(String infoLabel, String infoContent) {
        InfoLabel = infoLabel;
        InfoContent = infoContent;
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
