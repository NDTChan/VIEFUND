package mobile.bts.com.viefund.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.ConnSqlite.DatabaseHelper;
import mobile.bts.com.viefund.ConnSqlite.OrmLiteBaseActivity;
import mobile.bts.com.viefund.CustomConnectApi.RenewSession;
import mobile.bts.com.viefund.CustomFont.CustomColor;
import mobile.bts.com.viefund.Entities.Plan;
import mobile.bts.com.viefund.Entities.PlanInfo;
import mobile.bts.com.viefund.Entities.PlantAccount;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.Model.UrlModel;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;
import mobile.bts.com.viefund.R;

/**
 * create by VUDQ_15/02/2018
 * activity for screen PlanInfo
 */
public class PlanInfoActivity extends CustomColor {
    public static String TAG = PlanInfoActivity.class.getSimpleName();
    public static final String url = UrlModel.url_PlanInfo_List;

    private String PlanID;
    private TableLayout mTableLayout;
    private Toolbar toolbar;

    //sqliteconn
    private DatabaseHelper mDB; // Helper for connect sqlite
    private Dao<PlanInfo, String> _PlanInfoDao = null; // param dao for connet entities PlanAccount
    private Dao<Plan, String> _Plan = null;// param dao for connet entities Plan

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_info);
        //set property for toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarPlanInfo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tw = (TextView) toolbar.findViewById(R.id.app_bar_title) ;
        tw.setText("");
        tw.setText(R.string.plan_info_details);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ChangeThemeToolbar(toolbar,null,tw);
        ChangeBackground();
        mTableLayout = (TableLayout) findViewById(R.id.tablePlanInfo);
        mTableLayout.removeAllViews();

        Bundle b = getIntent().getExtras();
        PlanID = b != null ? b.getString("PlanID") : "";
        Log.d(TAG, "onCreate: PlanID"+PlanID);
        mDB = new DatabaseHelper(this);
        //open dao
        try {
            _PlanInfoDao = getHelper().getPlanInfoDao();
            _Plan = getHelper().getPlanDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ImageView imageView = findViewById(R.id.app_bar_home);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        SynchroData();
    }


    public void LoadTableLayout(Plan obj,List<PlanInfo> account){
//        Log.d(TAG, "onCreate: "+obj.get(0).DataDetails.get(0).getOwner()+" - "+obj.get(0).DataDetails.get(1).getOwner());
        ArrayList<PlanInfo> data = new ArrayList<>();
        data.addAll(account);
        int rows = data.size();
        int leftRowMargin=1;
        int topRowMargin=1;
        int rightRowMargin=1;
        int bottomRowMargin = 1;
        int textSize = 0, smallTextSize =0, mediumTextSize = 0;

        TextView textSpacer = null;
        int idTable =0;
        double grandTotal = 0;
//            grandTotal = grandTotal + row.getPlanGroupTotal();
        int rowChilds = account.size();
        final TextView tv = new TextView(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.LEFT);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tv.setPadding(5, 15, 0, 15);

        tv.setBackgroundColor(Color.parseColor(trans));
        tv.setText(String.valueOf(obj.getPlanType()+" "+ obj.getDescription()));
        tv.setTextColor(Color.WHITE);
        final LinearLayout layCustomer = new LinearLayout(this);
        layCustomer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,5);
//        layParams.setMargins(leftRowMargin, topRowMargin, 0, bottomRowMargin);

        layCustomer.setLayoutParams(layParams);
        layCustomer.setPadding(0, 10, 0, 10);
        layCustomer.setBackgroundColor(Color.parseColor(trans));
        layCustomer.addView(tv);

        final TextView tv4 = new TextView(this);
        tv4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        tv4.setGravity(Gravity.LEFT);
        tv4.setPadding(5, 15, 0, 15);
        tv4.setBackgroundColor(Color.parseColor(trans));
        tv4.setText("");
        tv4.setTextColor(Color.WHITE);

        final LinearLayout layCustomer4 = new LinearLayout(this);
        layCustomer4.setOrientation(LinearLayout.VERTICAL);
        layCustomer4.setPadding(0, 10, 0, 10);
        layCustomer4.setBackgroundColor(Color.parseColor(trans));
        LinearLayout.LayoutParams layParams4 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,5);
//        layParams4.setMargins(0, topRowMargin, rightRowMargin, bottomRowMargin);

        layCustomer4.setLayoutParams(layParams4);
        layCustomer4.addView(tv4);

        // add table row
        final TableRow tr = new TableRow(this);
        tr.setBackgroundColor(Color.parseColor(trans));
        tr.setId(idTable);
        TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
//        trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
        tr.setWeightSum(10);
        tr.setPadding(0,0,0,0);
        tr.setLayoutParams(trParams);
        tr.addView(layCustomer);
        tr.addView(layCustomer4);
//        tr.addView(layCustomer5);


        idTable++;
        //add vao table lay out
        mTableLayout.addView(tr, trParams);

        for(int j =0 ; j<rowChilds;j++){
            PlanInfo rowChild = data.get(j);
            Log.d(TAG, "LoadTableLayout: rowChild: "+rowChild.getInfoContent());
            // set Text 1
            final TextView tv1 = new TextView(this);
            tv1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tv1.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            tv1.setGravity(Gravity.LEFT);

            // tv1.setTypeface(tv.getTypeface(), Typeface.BOLD);
            tv1.setPadding(5, 15, 0, 15);

            tv1.setBackgroundColor(Color.parseColor("#f8f8f8"));
            tv1.setText(String.valueOf(rowChild.getInfoLabel()));
            final LinearLayout layCustomer1 = new LinearLayout(this);
            layCustomer1.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layParams1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,5);
//            layParams1.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);

            layCustomer1.setLayoutParams(layParams1);
            layCustomer1.setPadding(10, 10, 0, 10);
//            layCustomer1.setBackgroundColor(Color.parseColor("#f8f8f8"));
            if (idTable != rowChilds) {
                layCustomer1.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
            } else {
                layCustomer1.setBackgroundColor(Color.parseColor("#f8f8f8"));
            }
            layCustomer1.addView(tv1);


            //setText 2
            final TextView tv2 = new TextView(this);
            tv2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tv2.setGravity(Gravity.LEFT);
            tv2.setPadding(5, 15, 0, 15);
            tv2.setBackgroundColor(Color.parseColor("#f8f8f8"));

            DisplayMetrics mDisplayMetrics = getApplicationContext().getResources().getDisplayMetrics();
            tv2.setMaxWidth(mDisplayMetrics.widthPixels/2);
//            tv2.setMaxWidth((getApplicationContext().getResources().getDisplayMetrics()).widthPixels);

            tv2.setText(String.valueOf(rowChild.getInfoContent()));
            tv2.setTextColor(Color.parseColor(trans));
            final LinearLayout layCustomer2 = new LinearLayout(this);
            layCustomer2.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layParams2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,5);
//            layParams2.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
            layCustomer2.setLayoutParams(layParams2);
            layCustomer2.setPadding(10, 10, 0, 10);
//            layCustomer2.setBackgroundColor(Color.parseColor("#f8f8f8"));
            if (idTable != rowChilds) {
                layCustomer2.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
            } else {
                layCustomer2.setBackgroundColor(Color.parseColor("#f8f8f8"));
            }
            layCustomer2.addView(tv2);

            //setText 3
            final TableRow tr2 = new TableRow(this);
            tr2.setId(idTable);
            TableLayout.LayoutParams trParams2 = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT);
//            trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
            tr2.setPadding(0,0,0,0);
            tr2.setLayoutParams(trParams2);
            tr2.setWeightSum(10);

            tr2.addView(layCustomer1);
            tr2.addView(layCustomer2);
//            tr2.addView(layCustomer3);
            mTableLayout.addView(tr2);
            idTable++;
        }

        // add table row
        final TableRow trTotal = new TableRow(this);
        trTotal.setId(idTable);
        TableLayout.LayoutParams trParamsTotal = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
//        trParamsTotal.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
        trTotal.setWeightSum(10);
        trTotal.setPadding(0,0,0,0);
        trTotal.setLayoutParams(trParamsTotal);
//        trTotal.addView(layCustomerTotal);
//        trTotal.addView(layCustomer4Total);
//        trTotal.addView(layCustomer5Total);


        idTable++;
        //add vao table lay out
        mTableLayout.addView(trTotal, trParamsTotal);
    }


    public void SynchroData(){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loading_data));
        pDialog.show();
        pDialog.setCanceledOnTouchOutside(false);
        mDB.open();
        mDB.Delete_Plan_Info_All();
        mDB.close();
        RequestQueue mRequest = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse() returned: " + response);
                try {
                    Plan obj = new Plan();
                    List<PlanInfo> obj2 = new ArrayList<PlanInfo>();
                    JSONObject jObj = new JSONObject(response);
                    int result = Integer.parseInt(jObj.getString("RtnCode"));
                    if(result==0)
                    {
                        JSONObject objPortfolio = new JSONObject(jObj.getString("Portfolio"));
                        JSONArray objPlanGroupArr = objPortfolio.getJSONArray("PlanGroupList");
                        JSONObject objPlanGroupList = objPlanGroupArr.getJSONObject(0);
                        Log.d(TAG, "onResponse: objPlanGroupList: "+objPlanGroupList);
                        String planType = objPlanGroupList.getString("PlanType");
                        JSONArray arrList = objPlanGroupList.getJSONArray("PlanList");
                        String planID ="";

                        if(arrList.length()>0) {
                            for (int i = 0; i < arrList.length(); i++) {
                                JSONObject objTemp = arrList.getJSONObject(i);
                                planID = objTemp.getString("ID");
                                JSONObject objChild = objTemp.getJSONObject("PlanKYC");
                                JSONArray arrayChildChild = objChild.getJSONArray("InfoList");
                                if(arrayChildChild.length()>0){
                                    for (int n = 0 ; n<arrayChildChild.length();n++){
                                        JSONObject objTempChildChild = arrayChildChild.getJSONObject(n);
                                        Log.d(TAG, "onResponse: "+n+" objTempChildChild: "+objTempChildChild);
                                        PlanInfo objEntities = new PlanInfo();
                                        objEntities.setPlanID(planID);
                                        String  uniqueID = UUID.randomUUID().toString();
                                        Log.d(TAG, "onResponse: uid: "+uniqueID);
                                        objEntities.setID(uniqueID.toString());
                                        objEntities.setInfoLabel(objTempChildChild.getString("InfoLabel"));
//                                       objEntities.setInfoContent("v");
                                        //Log.d(TAG, "onResponse: objTempChildChild.isNull(\"InfoContent\"): "+objTempChildChild.isNull("InfoContent"));
                                        if(objTempChildChild.isNull("InfoContent")){
                                            Log.d(TAG, "onResponse: objTempChildChild.isNull(\"InfoContent\"): "+objTempChildChild.isNull("InfoContent"));
                                            objEntities.setInfoContent("");
                                        }else{
                                            Log.d(TAG, "onResponse: objTempChildChild.getString(\"InfoContent\"): "+objTempChildChild.getString("InfoContent"));
                                            objEntities.setInfoContent(objTempChildChild.getString("InfoContent"));
                                        }
                                        _PlanInfoDao.create(objEntities);
                                    }
                                }
                            }
                        }
                        obj2 = PlanInfo.Load_PlanInfo_By_PlanID(_PlanInfoDao,planID);
                        obj = Plan.Load_Plan_By_ID(_Plan,PlanID);
                        LoadTableLayout(obj,obj2);
                        pDialog.hide();
                    }
                    else if(result==1)
                    {
                        if(jObj.getString("ErrorMsg").startsWith(UrlModel.Msg_DieSession)){
                            RenewSession rns = new RenewSession(PlanInfoActivity.class,getApplicationContext());
                            rns.synchReNew();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),
                                    jObj.getString("ErrorMsg"), Toast.LENGTH_LONG).show();
                            pDialog.hide();
                        }
                    }
                    pDialog.dismiss();
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(),
                            ex+"", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse() returned: " + ex);
                    pDialog.dismiss();
                    pDialog.hide();
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
                params.put("iPlanID", PlanID);
                params.put("lg",BTApplication.getInstance().getPrefManager().getUser().getLang());
                return params;
            }
            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("iOptions", "0");
                params.put("iPlanID", PlanID);
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



}
