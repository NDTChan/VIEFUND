package mobile.bts.com.viefund.Activity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.ConnSqlite.SQLiteHandler;
import mobile.bts.com.viefund.Model.UserModel;
import mobile.bts.com.viefund.MultiLanguage.SharedPrefsLanguage;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by THANH on 4/4/2018.
 */

public class MyValueFormatter implements IValueFormatter {
    private DecimalFormat mFormat;
    private SQLiteHandler db;
    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
    }
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
       String lang = SQLiteHandler.KEY_LANG;
       if (BTApplication.getInstance().getPrefManager().getUser().getLang().equals("E"))  return "$"+mFormat.format(value) ; // e.g. append a dollar-sign
       else return mFormat.format(value)+"$";

    }
}
