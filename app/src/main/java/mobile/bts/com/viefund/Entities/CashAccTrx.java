package mobile.bts.com.viefund.Entities;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by Administrator on 3/26/2018.
 */

public class CashAccTrx {
    public static final String ID_FIELD_NAME = "ID";
    public static final String AccountID_FIELD_NAME = "AccountID";
    public static final String ID_CHILD_FIELD_NAME = "IDChild";
    public static final String Amount_FIELD_NAME = "Amount";
    public static final String Description_FIELD_NAME = "Description";
    public static final String TradeDate_FIELD_NAME = "TradeDate";

    @DatabaseField(id = true, canBeNull = false, columnName = ID_FIELD_NAME)
    private String ID;

    @DatabaseField(columnName = AccountID_FIELD_NAME)
    private String AccountID;

    @DatabaseField(columnName = ID_CHILD_FIELD_NAME)
    private String IDChild;

    @DatabaseField(columnName = Amount_FIELD_NAME)
    private String Amount;

    @DatabaseField(columnName = Description_FIELD_NAME)
    private String Description;

    @DatabaseField(columnName = TradeDate_FIELD_NAME)
    private String TradeDate;


    public CashAccTrx() {
    }

    public CashAccTrx(String ID, String accountID, String IDChild, String amount, String description, String tradeDate) {
        this.ID = ID;
        AccountID = accountID;
        this.IDChild = IDChild;
        Amount = amount;
        Description = description;
        TradeDate = tradeDate;
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

    public String getIDChild() {
        return IDChild;
    }

    public void setIDChild(String IDChild) {
        this.IDChild = IDChild;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTradeDate() {
        return TradeDate;
    }

    public void setTradeDate(String tradeDate) {
        TradeDate = tradeDate;
    }

    private static List<CashAccTrx> _DATA_Cash_Account_Trx = null;
    public static List<CashAccTrx> Load_Cash_Account_Trx_By_AccountID(Dao<CashAccTrx,String> _planCashAccTrx, String AccountID){
        try {
            Log.d("Cash Account Trx", "Load_Cash_Account_Trx_By_PlanID: "+AccountID);
            QueryBuilder<CashAccTrx, String> queryBuilder = _planCashAccTrx.queryBuilder();
            queryBuilder.where().eq(AccountInfo.AccountID_FIELD_NAME,AccountID);
            PreparedQuery<CashAccTrx> preparedQuery = queryBuilder.prepare();
            _DATA_Cash_Account_Trx = _planCashAccTrx.query(preparedQuery);
        }catch (Exception ex){
            _DATA_Cash_Account_Trx = null;
            ex.printStackTrace();
        }
        return _DATA_Cash_Account_Trx;
    }
}
