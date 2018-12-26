package mobile.bts.com.viefund.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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
import mobile.bts.com.viefund.CustomFont.CustomColor;
import mobile.bts.com.viefund.Entities.Plan;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.Model.UrlModel;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;
import mobile.bts.com.viefund.R;

public class BarChart extends CustomColor {

    public static final String url = UrlModel.url_BarChart_Data;
    public static final String TAG = BarChart.class.getSimpleName();
    List<mobile.bts.com.viefund.Model.BarChart> chartList;

    private DatabaseHelper mDB; // Helper for connect sqlite
    private Dao<mobile.bts.com.viefund.Entities.BarChart, String> _BarChartDao = null; // param dao for connet entities PlanAccount
    private Dao<Plan, String> _Plan = null;// param dao for connet entities Plan

    private ArrayList<String> chartRow;
    private ArrayList<Float> chartColumn;
    private Toolbar toolbar;
    private TableLayout tableLayout;
    private TableLayout tableLayout1;
    private com.github.mikephil.charting.charts.BarChart chart;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        chartRow = new ArrayList<>();
        chartColumn = new ArrayList<>();
        toolbar =  findViewById(R.id.toolbarBarchart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tableLayout = findViewById(R.id.main_barchart);
        tableLayout1 =findViewById(R.id.header_barchart);
        tableLayout.removeAllViews();
        tableLayout1.removeAllViews();
        TextView tw = toolbar.findViewById(R.id.app_bar_title);
        tw.setText(getString(R.string.nav_assett_chart) + "");
        chart = findViewById(R.id.barchart);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        chart.getLayoutParams().height = height/2;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mDB = new DatabaseHelper(this);
        try {
            _BarChartDao=mDB.getBarChartDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ChangeThemeToolbar(toolbar,null,(TextView) findViewById(R.id.app_bar_title));
        ChangeBackground();
        ImageView imageView = findViewById(R.id.app_bar_home);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        SynchroData();
    }
   public void LoadChart(final ArrayList<String> nameDate, ArrayList<Float> fl){

       List<BarEntry> entries = new ArrayList<>();
       for(int i=0;i<fl.size();i++)
       {
           entries.add(new BarEntry(i, fl.get(i)));
       }
        mDB.open();
       BarDataSet set = new BarDataSet(entries, null);
        set.setValueTextSize(12f);
       set.setValueFormatter(new MyValueFormatter());

       set.setValueTextSize(12f);
       set.setColor(Color.parseColor(trans));
       BarData data = new BarData(set);

       data.setBarWidth(0.9f); // set custom bar width
       chart.setData(data);
       chart.setFitBars(true); // make the x-axis fit exactly all bars
       chart.animateXY(2000, 2000);
       chart.setScaleEnabled(false);
       chart.invalidate(); // refresh

       Description description = chart.getDescription();
       description.setEnabled(false);


       XAxis xAxis = chart.getXAxis();
       xAxis.setDrawGridLines(false);
       xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
       xAxis.setDrawGridLines(false);
       xAxis.setGranularity(1f);
      // xAxis.setLabelRotationAngle(45);
       xAxis.setValueFormatter(new IAxisValueFormatter() {
           @Override
           public String getFormattedValue(float value, AxisBase axis) {
               return nameDate.get((int) value);
           }
       });

       YAxis yLabels = chart.getAxisLeft();
       yLabels.setDrawGridLines(false);
       YAxis yLabels1 = chart.getAxisRight();
       yLabels1.setEnabled(false);

       Legend l = chart.getLegend();
       l.setEnabled(false);


   }
    public void SynchroData(){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loading_data));
        pDialog.show();
        pDialog.setCanceledOnTouchOutside(false);
        mDB.open();
        mDB.Delete_BarChart_All();
        mDB.close();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse() returned: " + response);
                try{
                    List<mobile.bts.com.viefund.Model.BarChart> obj2 = new ArrayList<mobile.bts.com.viefund.Model.BarChart>();
                    JSONObject jObj = new JSONObject(response);
                    int result = Integer.parseInt(jObj.getString("RtnCode"));
                    if(result==0)
                    {
                        JSONObject objPortfolio = new JSONObject(jObj.getString("Portfolio"));
                        JSONObject objAssetsArr = objPortfolio.getJSONObject("Assets");
                        JSONArray listArr = objAssetsArr.getJSONArray("Asset");
                        Log.d("Da vao", "vao duoc result >0 roi");

                        if(listArr.length()>0)
                        {
                            for(int i=0;i<listArr.length();i++) {
                                JSONObject objTemp = listArr.getJSONObject(i);
                                mobile.bts.com.viefund.Entities.BarChart barChart = new mobile.bts.com.viefund.Entities.BarChart();
                                String  uniqueID = UUID.randomUUID().toString();
                                barChart.setID(uniqueID);
                              //  barChart.setID(Html.fromHtml(getString(R.id.)));
                                barChart.setMKVstr(objTemp.getString("MKVStr"));
                                barChart.setDate(objTemp.getString("Date"));
                                float temp = Float.parseFloat(objTemp.getString("MKV"));
                                barChart.setMKV(temp);
                                Log.d(TAG, barChart.getDate()+barChart.getMKV()+barChart.getMKVstr());
                                _BarChartDao.create(barChart);
                                chartRow.add(objTemp.getString("Date"));
                                chartColumn.add(temp);
                            }
                        }
//                        Log.d(TAG, chartRow[0]+" "+chartColumn[0]);
                        LoadChart(chartRow,chartColumn);
                        addHeaders();
                        addData(chartRow,chartColumn);
                        EventClick();
                    }
                    else if(result == 1)
                    {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString("ErrorMsg"), Toast.LENGTH_LONG).show();
                        pDialog.hide();
                    }
                    pDialog.dismiss();
                }catch(Exception ex){
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
                params.put("lg",BTApplication.getInstance().getPrefManager().getUser().getLang());
                //params.put("iPlanID", PlanID);
                return params;
            }
            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("iOptions", "0");
                params.put("lg",BTApplication.getInstance().getPrefManager().getUser().getLang());
               // params.put("iPlanID", PlanID);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        BTApplication.getInstance().addToRequestQueue(request,TAG);


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
    private TextView getTextViewFirst( String title, int color, int typeface) {
        TextView tv = new TextView(getApplicationContext());
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(8, 15, 15, 15);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.LEFT);
        return tv;
    }
    private TextView getTextViewSecond( String title, int color, int typeface) {
        TextView tv = new TextView(getApplicationContext());
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(8, 15, 0, 15);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.LEFT);
        return tv;
    }
    private TextView getTextViewThird( String title, int color, int typeface) {
        TextView tv = new TextView(getApplicationContext());
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(8, 15, 0, 15);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.LEFT);
        return tv;
    }
    private LinearLayout layoutCustemFirst(TextView tv)
    {
        final LinearLayout layCustomerTotal = new LinearLayout(this);
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 3);
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
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 4);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);
        layCustomerTotal.setBackgroundColor(Color.parseColor(trans));
        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }
    private LinearLayout layoutCustemFirstData(TextView tv)
    {
        final LinearLayout layCustomerTotal = new LinearLayout(this);
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 3);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);

        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }
    private LinearLayout layoutCustemSecondData(TextView tv)
    {
        final LinearLayout layCustomerTotal = new LinearLayout(this);
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 3);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);

        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }
    private LinearLayout layoutCustemThirdData(TextView tv)
    {
        final LinearLayout layCustomerTotal = new LinearLayout(this);
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 4);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);


        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }
    public void addHeaders(){
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(getLayoutParams());
        tableRow.setBackgroundColor(Color.parseColor(trans));
        tableRow.addView(layoutCustemFirst(getTextViewFirst("No",getResources().getColor(R.color.white),Typeface.BOLD)));
        tableRow.addView(layoutCustemSecond(getTextViewFirst("Date",getResources().getColor(R.color.white),Typeface.BOLD)));
        tableRow.addView(layoutCustemThird(getTextViewFirst(getString(R.string.amount),getResources().getColor(R.color.white),Typeface.BOLD)));
        tableRow.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
        tableLayout1.addView(tableRow,new TableLayout.LayoutParams(

                TableRow.LayoutParams.FILL_PARENT,

                TableRow.LayoutParams.WRAP_CONTENT));
    }
    // function click in chart
    public void EventClick(){
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                    for(int i =0;i<tableLayout.getChildCount();i++)
                    {
                        if(i == (int) e.getX()){
                            Log.d(TAG, "onValueSelected: in if: "+i);
                            tableLayout.getChildAt(i).setBackgroundColor(Color.rgb(212,212,212));
                        }
                        else {
                            Log.d(TAG, "onValueSelected: in else: "+i);

                            tableLayout.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
                            ;
                        }

                    }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }
    public void addData( ArrayList<String> nameDate, ArrayList<Float> fl){

        for(int i=0;i< fl.size();i++)
        {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(getLayoutParams());
            tableRow.addView(layoutCustemFirstData(getTextViewFirst(String.valueOf(i+1),Color.BLACK,Typeface.NORMAL)));
            tableRow.addView(layoutCustemSecondData(getTextViewSecond(String.valueOf(nameDate.get(i)),Color.BLACK,Typeface.NORMAL)));
            tableRow.setId(i);
            String temp=String.valueOf(fl.get(i));
            if(BTApplication.getInstance().getPrefManager().getUser().getLang().equals("E"))
                temp = "$"+temp;
            else
                temp=temp+"$";
            tableRow.addView(layoutCustemThirdData(getTextViewThird(String.valueOf(temp),Color.BLACK,Typeface.NORMAL)));
            tableRow.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
            //tableRow.setBackgroundColor(Color.GRAY);

            tableLayout.addView(tableRow,getTblLayoutParams());
            tableLayout.invalidate();
            tableLayout.refreshDrawableState();
        }
    }
}
