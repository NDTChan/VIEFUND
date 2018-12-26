package mobile.bts.com.viefund.Entities;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.Date;
import java.util.List;

/**
 * BT Company
 * Created by Administrator on 3/12/2018.
 */

public class AccountFundPrice {
    public static final String ID_FIELD_NAME = "ID";
    public static final String AccountID_FIELD_NAME = "AccountID";
    public static final String Date_FIELD_NAME = "Date";
    public static final String NAV_FIELD_NAME = "NAV";
    public static final String Date101_FIELD_NAME="Date101";
    public static final String PriceValue_FIELD_NAME="PriceValue";

    @DatabaseField(id = true, canBeNull = false, columnName = ID_FIELD_NAME)
    private String ID;

    @DatabaseField(columnName = AccountID_FIELD_NAME)
    private String AccountID;

    @DatabaseField(columnName = Date_FIELD_NAME)
    private String Date;

    @DatabaseField(columnName = NAV_FIELD_NAME)
    private String NAV;

    @DatabaseField(columnName = Date101_FIELD_NAME)
    private  String Date101;

    @DatabaseField(columnName = PriceValue_FIELD_NAME)
    private  double PriceValue;

    public double getPriceValue() {
        return PriceValue;
    }

    public void setPriceValue(double priceValue) {
        PriceValue = priceValue;
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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getNAV() {
        return NAV;
    }

    public void setNAV(String NAV) {
        this.NAV = NAV;
    }

    public AccountFundPrice() {
    }

    public String getDate101() {
        return Date101;
    }

    public void setDate101(String date101) {
        Date101 = date101;
    }

    public AccountFundPrice(String ID, String accountID, String date, String NAV,String date101) {
        this.ID = ID;
        this.AccountID = accountID;
        this.Date = date;
        this.NAV = NAV;
        this.Date101=date101;
    }


    private static List<AccountFundPrice> _DATA_AccountFundPrice = null;
    public static List<AccountFundPrice> Load_AccountFundPrice_By_AccountID(Dao<AccountFundPrice,String> _planAccountFundPrice, String AccountID){
        try {
            Log.d("AccountFundPrice", "Load_FundPrice_By_PlanID: "+AccountID);
            QueryBuilder<AccountFundPrice, String> queryBuilder = _planAccountFundPrice.queryBuilder();
            queryBuilder.where().eq(AccountFundPrice.AccountID_FIELD_NAME,AccountID);
            PreparedQuery<AccountFundPrice> preparedQuery = queryBuilder.prepare();
            _DATA_AccountFundPrice = _planAccountFundPrice.query(preparedQuery);
        }catch (Exception ex){
            _DATA_AccountFundPrice = null;
            ex.printStackTrace();
        }
        return _DATA_AccountFundPrice;
    }

}
