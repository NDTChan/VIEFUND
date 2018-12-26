package mobile.bts.com.viefund.Entities;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.List;

/**
 * Created by NDTChan on 3/21/2018.
 */

@DatabaseTable
public class BarChart {
    public static final String ID_FIELD_NAME = "ID";
    public static final String Date_FIELD_NAME = "Date";
    public static final String MKV_FIELD_NAME = "MKV";
    public static final String MKVstr_FIELD_NAME = "MKVstr";

    @DatabaseField(id = true, canBeNull = false, columnName = ID_FIELD_NAME)
    private String ID;

    @DatabaseField(columnName = Date_FIELD_NAME)
    private String Date;

    @DatabaseField(columnName = MKV_FIELD_NAME)
    private double MKV;

    @DatabaseField(columnName = MKVstr_FIELD_NAME)
    private String MKVstr;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public double getMKV() {
        return MKV;
    }

    public void setMKV(double MKV) {
        this.MKV = MKV;
    }

    public String getMKVstr() {
        return MKVstr;
    }

    public void setMKVstr(String MKVstr) {
        this.MKVstr = MKVstr;
    }

    public BarChart() {
    }

    public BarChart(String ID, String date, double MKV, String MKVstr) {
        this.ID = ID;
        Date = date;
        this.MKV = MKV;
        this.MKVstr = MKVstr;
    }
    private static List<BarChart> _DATA_Bar_Chart = null;
    public static List<BarChart> Load_Account_Info_By_AccountID(Dao<BarChart,String> _BarChart, String BarChartID){
        try {
            Log.d("TransactionAcount", "Load_Transaction_By_PlanID: "+BarChartID);
            QueryBuilder<BarChart, String> queryBuilder = _BarChart.queryBuilder();
            PreparedQuery<BarChart> preparedQuery = queryBuilder.prepare();
            _DATA_Bar_Chart = _BarChart.query(preparedQuery);
        }catch (Exception ex){
            _DATA_Bar_Chart = null;
            ex.printStackTrace();
        }
        return _DATA_Bar_Chart;
    }
}
