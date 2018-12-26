package mobile.bts.com.viefund.Model;

import java.util.Date;

/**
 * Created by NDTChan on 3/11/2018.
 */

public class BarChart {
    private String Date;
    private float MKV;
    private String MKVstr;

    public BarChart(String date, float MKV) {
        Date = date;
        this.MKV = MKV;
    }

    public BarChart() {
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public float getMKV() {
        return MKV;
    }

    public void setMKV(float MKV) {
        this.MKV = MKV;
    }

    public String getMKVstr() {
        return MKVstr;
    }

    public void setMKVstr(String MKVstr) {
        this.MKVstr = MKVstr;
    }
}
