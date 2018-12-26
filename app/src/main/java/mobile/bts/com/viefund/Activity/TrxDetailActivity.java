package mobile.bts.com.viefund.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.j256.ormlite.dao.Dao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.ConnSqlite.DatabaseHelper;
import mobile.bts.com.viefund.ConnSqlite.OrmLiteBaseActivity;
import mobile.bts.com.viefund.CustomConnectApi.RenewSession;
import mobile.bts.com.viefund.CustomFont.CustomColor;
import mobile.bts.com.viefund.Entities.*;
import mobile.bts.com.viefund.Entities.TrxDetail;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.Model.UrlModel;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;
import mobile.bts.com.viefund.R;

public class TrxDetailActivity extends CustomColor {

    public static String TAG = TrxDetailActivity.class.getSimpleName();
    public static final String url = UrlModel.url_TrxDetail_Data;

    //sqliteconn
    private DatabaseHelper mDB; // Helper for connect sqlite
    private Dao<mobile.bts.com.viefund.Entities.TrxDetail, String> _TrxDetail = null; // param dao for connet entities TrxDetail
   // private Dao<Plan, String> _Plan = null;// param dao for connet entities Plan
    private String TrxID="";
    private TableLayout mTableLayout;
    private TableRow tableRow;
    private TextView tv_Content,tv_Label;
    private Toolbar toolbar;
    //create and style TrxDetail
    private ArrayList<String> infoLabel;
    private ArrayList<String> infoContent;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trx_detail2);
        toolbar = (Toolbar) findViewById(R.id.toolbarTrxDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tw = (TextView) toolbar.findViewById(R.id.app_bar_title) ;
       // tw.setText("");
        tw.setText(getString(R.string.nav_transaction_detail));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mTableLayout = (TableLayout) findViewById(R.id.tabletrxDetail);
        mTableLayout.removeAllViews();

        ChangeThemeToolbar(toolbar,null,tw);
        ChangeBackground();

        Bundle c = getIntent().getExtras();
        TrxID = c != null ? c.getString("TransactionID") : "";
        Log.d(TAG, "onCreate: TrxID"+TrxID);
        mDB = new DatabaseHelper(this);
        //open dao
        try {
            _TrxDetail = getHelper().getTrxDetailDao();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        infoLabel=new ArrayList<>();
        infoContent = new ArrayList<>();
        ImageView imageView = findViewById(R.id.app_bar_home);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        SynchroData();
    }
    private void SynchroData()
    {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loading_data));
        pDialog.show();
        pDialog.setCanceledOnTouchOutside(false);
        mDB.open();
        mDB.Delete_PieChart_All();
        mDB.close();
        RequestQueue mRequest = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.d(TAG, "onResponse: jObj: "+jObj);

                    int result = Integer.parseInt(jObj.getString("RtnCode"));
                    if(result==0)
                    {
                        Log.d(TAG,"return"+response);
                        TrxDetail trxDetail = new TrxDetail();
                        JSONObject objTrxInfo = new JSONObject(jObj.getString("TrxInfo"));
                        String ID = objTrxInfo.getString("ID");
                        trxDetail.setID(ID);
                        JSONArray objInfoList = objTrxInfo.getJSONArray("InfoList");
                        Log.d(TAG, "onResponse: objInfoList.length() : "+objInfoList.length());
                        if(objInfoList.length()>0)
                        {
                            for(int i=0;i<objInfoList.length();i++)
                            {
                                JSONObject objTemp = objInfoList.getJSONObject(i);
                                trxDetail.setInfoContent(objTemp.getString("InfoContent"));
                                trxDetail.setInfoLabel(objTemp.getString("InfoLabel"));
                                infoContent.add(trxDetail.getInfoContent());
                                infoLabel.add(trxDetail.getInfoLabel());
                            }

                        }
                        tableData();
                    }
                    else if(result==1)
                    {
                        if(jObj.getString("ErrorMsg").startsWith(UrlModel.Msg_DieSession)){
                            RenewSession rns = new RenewSession(TrxDetailActivity.class,getApplicationContext());
                            rns.synchReNew();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),
                                    jObj.getString("ErrorMsg"), Toast.LENGTH_LONG).show();
                            pDialog.hide();
                        }
                    }
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error+"", Toast.LENGTH_LONG).show();
                pDialog.hide();
                Log.d(TAG, "onErrorResponse() returned: " + error);
    }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                Log.d(TAG, "getParams: "+ BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("TokenStr",BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("iOptions", "0");
                params.put("iTrxID", TrxID);
                params.put("lg",BTApplication.getInstance().getPrefManager().getUser().getLang());
                return params;
            }
            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("iOptions", "0");
                params.put("iTrxID", TrxID);
                params.put("lg",BTApplication.getInstance().getPrefManager().getUser().getLang());
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        BTApplication.getInstance().addToRequestQueue(request,TAG);

    }
    private void  tableData()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tv_Label = new TextView(this);
        String[] temp = infoContent.get(1).split("\\s");
        Log.d(TAG, "tableData: temp: "+temp.length);
        String newObjDes = "";
        for(int i=0;i<temp.length;i++){

            if(i==3) newObjDes+=temp[i]+"\n";
            else newObjDes+=temp[i]+" ";
        }
        tv_Label.setText(String.valueOf(infoContent.get(0)+"\n"+String.valueOf(newObjDes+"\n"+String.valueOf(infoContent.get(3)))));
        tv_Label.setTextColor(Color.WHITE);
        tv_Label.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_Label.setTextSize(15);
        tv_Label.setPadding(30, 40, 0, 40);
        tableRow.addView(tv_Label);

        tv_Content = new TextView(this);
        tv_Content.setText("");
        tv_Content.setTextColor(Color.WHITE);
        tv_Content.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tv_Content.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_Content.setPadding(20, 40, 0, 40);
        tv_Content.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

        tableRow.addView(tv_Content);
        tableRow.setBackgroundColor(Color.parseColor(trans));

        TableLayout mTableLayout1 = (TableLayout) findViewById(R.id.trx_header);
        mTableLayout1.addView(tableRow,new TableLayout.LayoutParams(

                TableRow.LayoutParams.FILL_PARENT,

                TableRow.LayoutParams.WRAP_CONTENT));
        for(int i=0;i<infoLabel.size();i++)
        {


            tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.setWeightSum(10);

            tv_Label = new TextView(this);
            tv_Label.setText(String.valueOf(infoLabel.get(i)));
            tv_Label.setTextColor(Color.BLACK);
            tv_Label.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
            tv_Label.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,7));
            tv_Label.setPadding(30, 40, 0, 40);

            tableRow.addView(tv_Label);

            String[] temp1 = infoContent.get(i).split("\\s");
            String newObjDes1 = "";
//            if(temp1.length>3)
//            {
//
//                for(int i1=0;i1<temp1.length;i1++){
//
//                    if(i1==2) newObjDes1+=temp1[i1]+" "+"\n";
//                    else newObjDes1+=temp1[i1]+" ";
//                }
                tv_Label.setText(String.valueOf(infoLabel.get(i)+"\n"+" "));
//            }
             newObjDes1 = infoContent.get(i);
            tv_Content = new TextView(this);
            tv_Content.setSingleLine(false);
            tv_Content.setText(String.valueOf(newObjDes1));
            tv_Content.setTextColor(Color.parseColor(trans));
            tv_Content.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
            tv_Content.setLayoutParams(new TableRow.LayoutParams((mTableLayout.getWidth()/100)*70 , TableRow.LayoutParams.WRAP_CONTENT,3));
            tv_Content.setPadding(20, 40, 8, 40);
            tv_Content.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

            tableRow.addView(tv_Content);


            mTableLayout.addView(tableRow,new TableLayout.LayoutParams(

                    TableRow.LayoutParams.FILL_PARENT,

                    TableRow.LayoutParams.WRAP_CONTENT));
        }
    }
}
