package mobile.bts.com.viefund.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.j256.ormlite.dao.Dao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.ConnSqlite.DatabaseHelper;
import mobile.bts.com.viefund.CustomConnectApi.RenewSession;
import mobile.bts.com.viefund.CustomFont.CustomColor;
import mobile.bts.com.viefund.Entities.Plan;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.Model.PlanDetail;
import mobile.bts.com.viefund.Model.PlanModel;
import mobile.bts.com.viefund.Model.UrlModel;
import mobile.bts.com.viefund.Model.UserModel;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;
import mobile.bts.com.viefund.R;

public class QuickAccess extends CustomColor {
    public static String TAG = QuickAccess.class.getSimpleName();
    private Toolbar toolbar;
    private TableLayout mTableLayout;
    private TableLayout row;
    public static final String url = UrlModel.url_Portfolio_Plan_List;
    public static final String urlQuickAccessList = UrlModel.url_Quick_Access_List;

    ArrayList<String>  plan_group_total = new ArrayList<>();
    private String objPort;
    private DatabaseHelper mDB;
    private int countGreen=0;
    private int countWhite=0;
    private Dao<Plan, String> _PlanDao = null;
    private  String KEY_USER_LAST_TOKEN = "";
    private  String KEY_USER_LAST_LANGUAGE="";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_access);
        toolbar = (Toolbar) findViewById(R.id.toolbarPlanAccount);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tw = toolbar.findViewById(R.id.app_bar_title);
        tw.setText(getString(R.string.quick_access));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ChangeThemeToolbar(toolbar,null,tw);
        ChangeBackground();
        row = findViewById(R.id.row_bottom);
        mDB = new DatabaseHelper(this);
        ImageView imageView = findViewById(R.id.app_bar_home);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        try {
            _PlanDao = getHelper().getPlanDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mTableLayout = findViewById(R.id.tablePlan);
        mTableLayout.removeAllViews();
        UserModel userModel = new UserModel();
        userModel = BTApplication.getInstance().getPrefManager().getLastUser();
        Log.d(TAG, "onCreate: userModel: "+userModel);
        if(userModel != null) {
            KEY_USER_LAST_TOKEN = userModel.getToken();
            KEY_USER_LAST_LANGUAGE = userModel.getLang();
            SynchroData(mTableLayout);
        }
        else {
            UserModel userModel2 = BTApplication.getInstance().getPrefManager().getUser();
            if(userModel2 != null)
            {
                KEY_USER_LAST_TOKEN = userModel2.getToken();
                KEY_USER_LAST_LANGUAGE = userModel2.getLang();
                SynchroData(mTableLayout);
            }
        }
        Log.d(TAG, "onCreate: KEY_USER_LAST_TOKEN: "+KEY_USER_LAST_TOKEN);


    }
    @NonNull
    private TableRow.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }
    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        TableLayout.LayoutParams layoutParams =  new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        return layoutParams;
    }
    private TextView getTextViewFirst(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(getApplicationContext());
        tv.setId(id);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(8, 15, 0, 15);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.LEFT);
        return tv;
    }
    private TextView getTextViewFirstCapTitle(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(getApplicationContext());
        tv.setId(id);
        tv.setAllCaps(true);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(8, 15, 0, 15);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.LEFT);
        return tv;
    }
    private TextView getTextViewSecond(int bgColor) {
        TextView tv = new TextView(getApplicationContext());
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.LEFT);
        tv.setPadding(5, 0, 0, 15);
        tv.setBackgroundColor(bgColor);
        return tv;
    }
    private TextView getTextViewThird(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(getApplicationContext());
        tv.setId(id);
        tv.setText(title);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.LEFT);
        tv.setPadding(10, 15, 0, 15);
        tv.setTextColor(color);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        return tv;
    }
    private TextView getTextViewFourth(int id, int image,int bgColor) {
        TextView tv = new TextView(getApplicationContext());
        tv.setId(id);
        tv.setCompoundDrawablesWithIntrinsicBounds(image,0,0,0);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setPadding(5, 10, 0, 10);
        tv.setBackgroundColor(bgColor);
        return tv;
    }
    private LinearLayout layoutCustemFirst(TextView tv)
    {
        final LinearLayout layCustomerTotal = new LinearLayout(this);
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 5);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);
        layCustomerTotal.setBackgroundColor(Color.parseColor(trans));
        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }
    private LinearLayout layoutCustemSecond(TextView tv)
    {
        final LinearLayout layCustomerTotal = new LinearLayout(this);
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 3);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);

        layCustomerTotal.setBackgroundColor(Color.parseColor(trans));
        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }
    private LinearLayout layoutCustemThird(TextView tv)
    {
        final LinearLayout layCustomerTotal = new LinearLayout(this);
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 2);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);
        layCustomerTotal.setBackgroundColor(Color.parseColor(trans));
        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }
    String temp =" ";
    public void addData(ArrayList<PlanModel> obj) {
        int numCompanies = obj.size();
        Log.d(TAG, "addData: "+numCompanies);
        TableLayout.LayoutParams layoutParams=getTblLayoutParams();
        Log.d("Color",trans);
        for (int i = 0; i < numCompanies; i++) {
            TableRow tr = new TableRow(getApplicationContext());
            tr.setLayoutParams(getLayoutParams());
            tr.setBackgroundColor(Color.parseColor(trans));
            tr.addView(layoutCustemFirst(getTextViewFirst(i, obj.get(i).getName(), Color.WHITE, Typeface.NORMAL,Color.parseColor(trans))));
            tr.addView(layoutCustemSecond(getTextViewSecond(Color.parseColor(trans))));
            tr.addView(layoutCustemThird(getTextViewThird(i,plan_group_total.get(i), Color.WHITE, Typeface.BOLD,Color.parseColor(trans))));
            mTableLayout.addView(tr, getTblLayoutParams() );
            for(int j =0;j<obj.get(i).DataDetails.size();j++){
                PlanDetail detail=new PlanDetail();
                detail = obj.get(i).DataDetails.get(j);
                TableRow tr2 = new TableRow(getApplicationContext());
                TableLayout.LayoutParams trParamsTotal = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
                tr2.setWeightSum(10);
                tr2.setPadding(0, 0, 0, 0);
                tr2.setLayoutParams(trParamsTotal);
                ImageView iw ;
                final PlanDetail finalDetail = detail;
                String s = obj.get(i).DataDetails.get(j).getOwner();
                SpannableString sb = new SpannableString(s);
                sb.setSpan( new StyleSpan(Typeface.BOLD), 0,4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if(j%2==0){
                    TextView  viewText = getTextViewThird(i + numCompanies, obj.get(i).DataDetails.get(j).getMKV(),Color.parseColor(trans), Typeface.NORMAL,Color.parseColor("#f8f8f8"));
                    TextView textView1=getTextViewFourth(i + numCompanies,R.drawable.ic_keyboard_arrow_right_green_700_24dp,Color.parseColor("#f8f8f8"));
                    tr2.addView(layoutCustemFirst(getTextViewFirst(i, sb+" \n"+obj.get(i).DataDetails.get(j).getDescription(), Color.BLACK, Typeface.NORMAL,Color.parseColor("#f8f8f8"))));
                    tr2.addView(layoutCustemSecond(getTextViewSecond(Color.parseColor("#f8f8f8"))));
                    tr2.addView(layoutCustemThird(viewText));
                    tr2.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
                }
                else{
                    TextView textView1=getTextViewFourth(i + numCompanies,R.drawable.ic_keyboard_arrow_right_green_700_24dp,Color.parseColor("#f8f8f8"));
                    TextView viewText=getTextViewThird(i + numCompanies, obj.get(i).DataDetails.get(j).getMKV(),Color.parseColor(trans), Typeface.NORMAL,Color.parseColor("#f8f8f8"));
                    temp = obj.get(i).DataDetails.get(j).getDescription();
                    tr2.addView(layoutCustemFirst(getTextViewFirst(i, sb+" \n"+obj.get(i).DataDetails.get(j).getDescription(), Color.BLACK, Typeface.NORMAL, Color.parseColor("#f8f8f8"))));
                    tr2.addView(layoutCustemSecond(getTextViewSecond(Color.parseColor("#f8f8f8"))));
                    tr2.addView(layoutCustemThird(viewText));
                    tr2.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
                }
                mTableLayout.addView(tr2, getTblLayoutParams());
            }
        }



    }
    public void SynchroData(final TableLayout a) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loading_data));
        pDialog.show();
        pDialog.setCanceledOnTouchOutside(false);
        //BTApplication.getInstance().getPrefManager().reNewSession("20CABD61EE7DB5D2094B678E9A92C8003C63C3A4BDA68B2D45035BBDB3009F0BA560AD9D27C3F20C2E370510216A6C87F2B6613F8F3D4B4971F6BAC629F7B3273F21386A39D7DBAF");
        mDB.open();
        mDB.Delete_Plan_All();

//        mDB.close();
//        RequestQueue mRequest = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, urlQuickAccessList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse() returned: " + response);
                try {
                    ArrayList<PlanModel> obj = new ArrayList<PlanModel>();
                    List<Plan> obj2 = new ArrayList<Plan>();
                    JSONObject jObj = new JSONObject(response);
                    int result = Integer.parseInt(jObj.getString("RtnCode"));
                    if (result == 0) {
                        JSONObject objLogin = new JSONObject(jObj.getString("Portfolio"));
                        objPort = objLogin.getString("PortfolioTotal");
                        JSONArray arrList = objLogin.getJSONArray("PlanGroupList");
                        if (arrList.length() > 0) {
                            Log.d(TAG, "onResponse: arrList.length(): " + arrList.length());
                            for (int i = 0; i < arrList.length(); i++) {
                                countGreen++;
                                Log.d(TAG, "onResponse: PlanGroupList: " + i);
                                JSONObject objTemp = arrList.getJSONObject(i);
                                JSONArray arrayChild = objTemp.getJSONArray("PlanList");
                                plan_group_total.add(objTemp.getString("PlanGroupTotal"));
                                if (arrayChild.length() > 0) {
                                    Log.d(TAG, "onResponse: " + i + " PlanList: " + arrayChild.length());
                                    for (int j = 0; j < arrayChild.length(); j++) {
                                        countWhite++;
                                        Log.d(TAG, "onResponse: PlanList: " + j);

                                        JSONObject objTemp2 = arrayChild.getJSONObject(j);
                                        Plan objEntities = new Plan();
                                        objEntities.setPlanType(objTemp.getString("PlanType"));
                                        objEntities.setPlanID(objTemp2.getString("ID"));
                                        objEntities.setID(objTemp2.getString("ID"));
                                        objEntities.setDescription(objTemp2.getString("Description"));
                                        objEntities.setOwner(objTemp2.getString("Owner"));
                                        objEntities.setMKV(objTemp2.getString("MKV"));
//                                        mDB.Delete_Plan_All_By_Plan_ID(objEntities.getPlanID());
                                        _PlanDao.create(objEntities);
                                    }
                                }
                            }
                        }
                        List<String> lstPlanTypeStr;
                        obj2 = Plan.Load_Plan(_PlanDao);
                        lstPlanTypeStr = new ArrayList<>();
                        if (obj2.size() > 0) {
                            for (int i = 0; i < obj2.size(); i++) {
                                Plan tempObj = obj2.get(i);
                                if (lstPlanTypeStr.size() <= 0) {
                                    lstPlanTypeStr.add(tempObj.getPlanType());
                                } else {
                                    if (lstPlanTypeStr.indexOf(tempObj.getPlanType()) < 0) {
                                        lstPlanTypeStr.add(tempObj.getPlanType());
                                    } else {
                                        continue;
                                    }
                                }
                            }

                            for (int i = 0; i < lstPlanTypeStr.size(); i++) {
                                PlanModel model = new PlanModel();
                                model.setName(lstPlanTypeStr.get(i));
                                double taotalPlan = 0;
                                Log.d(TAG, "onResponse: qua day roi " + i + " : " + obj2.size());

                                for (int j = 0; j < obj2.size(); j++) {
                                    Plan tempObj2 = obj2.get(j);
                                    PlanDetail tempDeial = new PlanDetail();
                                    if (tempObj2.getPlanType().equals(lstPlanTypeStr.get(i))) {
                                        tempDeial.setDescription(tempObj2.getDescription());
                                        tempDeial.setID(tempObj2.getID());
                                        tempDeial.setPlanID(tempObj2.getPlanID());
                                        tempDeial.setMKV((tempObj2.getMKV() + ""));
                                        tempDeial.setOwner(tempObj2.getOwner());
                                        model.DataDetails.add(tempDeial);
                                        model.setPlanGroupTotal(taotalPlan);

                                    }
                                }

                                obj.add(model);
                            }
                        }
                        addData(obj);
                        // LoadTableLayout(obj, a);
                        pDialog.hide();
                    } else if (result == 1) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    pDialog.dismiss();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(),
                            ex + "", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse() returned: " + ex);
                    pDialog.dismiss();
                    pDialog.hide();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error + "", Toast.LENGTH_LONG).show();
                pDialog.hide();
                Log.d(TAG, "onErrorResponse() returned: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("TokenStr", KEY_USER_LAST_TOKEN);
                params.put("lg",KEY_USER_LAST_LANGUAGE);
                Log.d(TAG, "getParams: params: "+params.toString());
                return params;
            }

            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("TokenStr", KEY_USER_LAST_TOKEN);
                params.put("lg",KEY_USER_LAST_LANGUAGE);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        BTApplication.getInstance().addToRequestQueue(request, TAG);
    }
}
