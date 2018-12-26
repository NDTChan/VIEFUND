package mobile.bts.com.viefund.Model;

import java.util.Date;

/**
 * Created by NDTChan on 3/11/2018.
 */

public class PieChart {
    private String InfoLabel;
    private String InfoContent;
    private String InfoContent2;

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

    public String getInfoContent2() {
        return InfoContent2;
    }

    public void setInfoContent2(String infoContent2) {
        InfoContent2 = infoContent2;
    }

    public PieChart() {
    }

    public PieChart(String infoLabel, String infoContent, String infoContent2) {
        InfoLabel = infoLabel;
        InfoContent = infoContent;
        InfoContent2 = infoContent2;
    }
}
