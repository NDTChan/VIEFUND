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
 * BT Company
 * Created by Administrator on 3/12/2018.
 */

@DatabaseTable
public class TransactionAcount {
    public static final String ID_FIELD_NAME = "ID";
    public static final String AccountID_FIELD_NAME = "AccountID";
    public static final String TradeDate_FIELD_NAME = "TradeDate";
    public static final String Amount_FIELD_NAME = "Amount";
    public static final String Unit_FIELD_NAME = "Unit";
    public static final String Price_FIELD_NAME = "Price";
    public static final String Description_FIELD_NAME = "Description";

    @DatabaseField(id = true, canBeNull = false, columnName = ID_FIELD_NAME)
    private String ID;

    @DatabaseField(columnName = AccountID_FIELD_NAME)
    private String AccountID;

    @DatabaseField(columnName = TradeDate_FIELD_NAME)
    private String TradeDate;

    @DatabaseField(columnName = Unit_FIELD_NAME)
    private String Amount;

    @DatabaseField(columnName = Amount_FIELD_NAME)
    private String Unit;

    @DatabaseField(columnName = Price_FIELD_NAME)
    private String Price;

    @DatabaseField(columnName = Description_FIELD_NAME)
    private String Description;

    public String getID() {
        return ID;
    }

    public void setID(String planID) {
        ID = planID;
    }

    public String getAccountID() {
        return AccountID;
    }

    public void setAccountID(String accountID) {
        AccountID = accountID;
    }

    public String getTradeDate() {
        return TradeDate;
    }

    public void setTradeDate(String tradeDate) {
        TradeDate = tradeDate;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public TransactionAcount() {
    }

    public TransactionAcount(String ID, String accountID, String tradeDate, String amount, String unit, String price, String description) {
        this.ID = ID;
        AccountID = accountID;
        TradeDate = tradeDate;
        Amount = amount;
        Unit = unit;
        Price = price;
        Description = description;
    }
    private static List<TransactionAcount> _DATA_Transaction_Acount = null;
    public static List<TransactionAcount> Load_Transaction_By_PlanID(Dao<TransactionAcount,String> _planTransactionAcount, String AccountID){
        try {
            Log.d("TransactionAcount", "Load_Transaction_By_PlanID: "+AccountID);
            QueryBuilder<TransactionAcount, String> queryBuilder = _planTransactionAcount.queryBuilder();
            queryBuilder.where().eq(TransactionAcount.AccountID_FIELD_NAME,AccountID);
            PreparedQuery<TransactionAcount> preparedQuery = queryBuilder.prepare();
            _DATA_Transaction_Acount = _planTransactionAcount.query(preparedQuery);
        }catch (Exception ex){
            _DATA_Transaction_Acount = null;
            ex.printStackTrace();
        }
        return _DATA_Transaction_Acount;
    }

}
