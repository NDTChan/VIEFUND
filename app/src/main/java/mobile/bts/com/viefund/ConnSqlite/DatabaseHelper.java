package mobile.bts.com.viefund.ConnSqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.toolbox.StringRequest;
import com.j256.ormlite.android.apptools.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import mobile.bts.com.viefund.Entities.AccountFundPerformance;
import mobile.bts.com.viefund.Entities.AccountFundPrice;
import mobile.bts.com.viefund.Entities.AccountInfo;
import mobile.bts.com.viefund.Entities.BarChart;
import mobile.bts.com.viefund.Entities.CashAccDetail;
import mobile.bts.com.viefund.Entities.CashAccTrx;
import mobile.bts.com.viefund.Entities.GICAccDetail;
import mobile.bts.com.viefund.Entities.PieChart;
import mobile.bts.com.viefund.Entities.Plan;
import mobile.bts.com.viefund.Entities.PlanAsset;
import mobile.bts.com.viefund.Entities.PlanGroup;
import mobile.bts.com.viefund.Entities.PlanInfo;
import mobile.bts.com.viefund.Entities.PlantAccount;
import mobile.bts.com.viefund.Entities.TransactionAcount;
import mobile.bts.com.viefund.Entities.TrxDetail;

/**
 * BT Company
 * Created by Administrator on 3/13/2018.
 */

public class DatabaseHelper extends com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper {

    private SQLiteDatabase mDB;
    private Dao<Plan, String> PlanDao;
    private Dao<PlanGroup, String> PlanGroupDao;
    private Dao<AccountFundPerformance, String> AccountFundPerformanceDao;
    private Dao<AccountFundPrice, String> AccountFundPriceDao;
    private Dao<AccountInfo, String> AccountInfoDao;
    private Dao<PlanInfo, String> PlanInfoDao;
    private Dao<PlantAccount, String> PlantAccountDao;
    private Dao<TransactionAcount, String> TransactionAcountDao;
    private Dao<PlanAsset, String> PlanAssetDao;
    private Dao<BarChart, String> BarChartDao;
    private Dao<PieChart,String> PieChartDao;
    private Dao<GICAccDetail,String> gicAccDetailDao;
    private Dao<CashAccDetail,String> cashAccDetailDao;
    private Dao<CashAccTrx,String> cashAccTrxDao;
    private Dao<TrxDetail,String> TrxDetailDao;
    public static String TAG = DatabaseHelper.class.getSimpleName();

    public DatabaseHelper(Context context) {
        super(context,"VIEFUNDV2.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase var1, ConnectionSource connectionSource) {
        Log.d(TAG, "onCreate: START");
        try {
            TableUtils.createTable(connectionSource, Plan.class);
            TableUtils.createTable(connectionSource, PlanGroup.class);
            TableUtils.createTable(connectionSource, AccountFundPerformance.class);
            TableUtils.createTable(connectionSource, AccountFundPrice.class);
            TableUtils.createTable(connectionSource, AccountInfo.class);
            TableUtils.createTable(connectionSource, PlanInfo.class);
            TableUtils.createTable(connectionSource, PlantAccount.class);
            TableUtils.createTable(connectionSource, TransactionAcount.class);
            TableUtils.createTable(connectionSource, PlanAsset.class);
            TableUtils.createTable(connectionSource,PieChart.class);
            TableUtils.createTable(connectionSource, BarChart.class);
            TableUtils.createTable(connectionSource, TrxDetail.class);
            TableUtils.createTable(connectionSource, GICAccDetail.class);
            TableUtils.createTable(connectionSource, CashAccDetail.class);
            TableUtils.createTable(connectionSource, CashAccTrx.class);
            Log.d(TAG, "onCreate:  SUCCES");
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            TableUtils.dropTable(connectionSource, Plan.class,true);
            TableUtils.dropTable(connectionSource, PlanGroup.class,true);
            TableUtils.dropTable(connectionSource, AccountFundPerformance.class,true);
            TableUtils.dropTable(connectionSource, AccountFundPrice.class,true);
            TableUtils.dropTable(connectionSource, AccountInfo.class,true);
            TableUtils.dropTable(connectionSource, PlanInfo.class,true);
            TableUtils.dropTable(connectionSource, PlantAccount.class,true);
            TableUtils.dropTable(connectionSource, TransactionAcount.class,true);
            TableUtils.dropTable(connectionSource, PlanAsset.class,true);
            TableUtils.dropTable(connectionSource, BarChart.class,true);
            TableUtils.dropTable(connectionSource, PieChart.class,true);
            TableUtils.dropTable(connectionSource, TrxDetail.class,true);
            TableUtils.dropTable(connectionSource, GICAccDetail.class,true);
            TableUtils.dropTable(connectionSource, CashAccDetail.class,true);
            TableUtils.dropTable(connectionSource, CashAccTrx.class,true);
            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }
    }

    public DatabaseHelper open()
    {
        mDB  =DatabaseHelper.this.getWritableDatabase();
        return this;
    }

//    public void close()
//    {
//        mDB.close();
//    }

    /**
     * FUNCTION delete data tale
     */

    public void Delete_Plan_All()
    {
        mDB.execSQL("delete from Plan");
    }
    public void Delete_Plan_All_By_Plan_ID(String _planID)
    {
        mDB.execSQL("delete from plan where "+Plan.PlanId_FIELD_NAME+" = "+_planID);
    }

    public void Delete_Plan_Account_All()
    {
        mDB.execSQL("delete from PlantAccount");
    }
    public void Delete_Plan_Group_All()
    {
        mDB.execSQL("delete from PlanGroup");
    }
    public void Delete_Plan_Account_By_Account_ID(String _accountID)
    {
        mDB.execSQL("delete from PlantAccount where "+PlantAccount.ID_FIELD_NAME+" = "+_accountID);
    }

    public void Delete_Transaction_Account_All()
    {
        mDB.execSQL("delete from TransactionAcount");
    }

    public void Delete_Plan_Info_All()
    {
        mDB.execSQL("delete from PlanInfo");
    }

    public void Delete_Account_Info_All()
    {
        mDB.execSQL("delete from AccountInfo");
    }

    public void Delete_Plan_Asset_All()
    {
        mDB.execSQL("delete from PlanAsset");
    }

    public void Delete_AccountFundPerformance_All()
    {
        mDB.execSQL("delete from AccountFundPerformance");
    }

    public void Delete_AccountFundPrice_All()
    {
        mDB.execSQL("delete from AccountFundPrice");
    }
    public void Delete_BarChart_All(){mDB.execSQL("delete from BarChart");}
    public void Delete_PieChart_All(){mDB.execSQL("delete from PieChart");}
    public  void Delete_TrxDetail_All(){mDB.execSQL("delete from TrxDetail");}

    public void Delete_GIC_Account_All()
    {
        mDB.execSQL("delete from GICAccDetail");
    }
    public void Delete_Cash_AccDetail_All()
    {
        mDB.execSQL("delete from CashAccDetail");
    }
    public void Delete_Cash_AccTrx_All()
    {
        mDB.execSQL("delete from CashAccTrx");
    }
    /**
     * VUDQ FUNCTION GAT DAO
     * @return
     * @throws SQLException
     */
    public Dao<Plan, String> getPlanDao() throws SQLException {
        if (PlanDao == null) {
            PlanDao = getDao(Plan.class);
        }
        return PlanDao;
    }
    public Dao<PlanGroup, String> getPlanGroupDao() throws SQLException {
        if (PlanGroupDao == null) {
            PlanGroupDao = getDao(PlanGroup.class);
        }
        return PlanGroupDao;
    }

    public Dao<PlantAccount, String> getPlanAccountDao() throws SQLException {
        if (PlantAccountDao == null) {
            PlantAccountDao = getDao(PlantAccount.class);
        }
        return PlantAccountDao;
    }

    public Dao<PlanInfo, String> getPlanInfoDao() throws SQLException {
        if (PlanInfoDao == null) {
            PlanInfoDao = getDao(PlanInfo.class);
        }
        return PlanInfoDao;
    }

    public Dao<TransactionAcount, String> getTransactionAcountDao() throws SQLException {
        if (TransactionAcountDao == null) {
            TransactionAcountDao = getDao(TransactionAcount.class);
        }
        return TransactionAcountDao;
    }
    public Dao<AccountInfo, String> getAccountInfoDao() throws SQLException {
        if (AccountInfoDao == null) {
            AccountInfoDao = getDao(AccountInfo.class);
        }
        return AccountInfoDao;
    }

    public Dao<AccountFundPerformance, String> getAccountFundPerformanceDao() throws SQLException {
        if (AccountFundPerformanceDao == null) {
            AccountFundPerformanceDao = getDao(AccountFundPerformance.class);
        }
        return AccountFundPerformanceDao;
    }

    public Dao<AccountFundPrice, String> getAccountFundPriceDao() throws SQLException {
        if (AccountFundPriceDao == null) {
            AccountFundPriceDao = getDao(AccountFundPrice.class);
        }
        return AccountFundPriceDao;
    }

    public Dao<PlanAsset, String> getPlanAssetDao() throws SQLException {
        if (PlanAssetDao == null) {
            PlanAssetDao = getDao(PlanAsset.class);
        }
        return PlanAssetDao;
    }

    public Dao<BarChart, String> getBarChartDao() throws SQLException {
        if (BarChartDao == null) {
            BarChartDao = getDao(BarChart.class);
        }
        return BarChartDao;
    }
    public Dao<PieChart, String> getPieChartDao() throws SQLException {
        if (PieChartDao == null) {
            PieChartDao = getDao(PieChart.class);
        }
        return PieChartDao;

    }
    public Dao<TrxDetail,String> getTrxDetailDao() throws SQLException{
        if(TrxDetailDao==null)
        {
            TrxDetailDao=getDao(TrxDetail.class);
        }
        return TrxDetailDao;
    }

    public Dao<GICAccDetail, String> getGicAccDetailDao() throws SQLException {
        if (gicAccDetailDao == null) {
            gicAccDetailDao = getDao(GICAccDetail.class);
        }
        return gicAccDetailDao;

    }
    public Dao<CashAccDetail, String> getCashAccDetailDao() throws SQLException {
        if (cashAccDetailDao == null) {
            cashAccDetailDao = getDao(CashAccDetail.class);
        }
        return cashAccDetailDao;

    }
    public Dao<CashAccTrx, String> getCashAccTrxDao() throws SQLException {
        if (cashAccTrxDao == null) {
            cashAccTrxDao = getDao(CashAccTrx.class);
        }
        return cashAccTrxDao;

    }

}
