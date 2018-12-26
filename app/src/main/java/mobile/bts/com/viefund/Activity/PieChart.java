package mobile.bts.com.viefund.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TableLayout;

import android.widget.TableRow;

import android.widget.TextView;

import android.widget.TableRow.LayoutParams;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.j256.ormlite.dao.Dao;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.ConnSqlite.DatabaseHelper;
import mobile.bts.com.viefund.ConnSqlite.OrmLiteBaseActivity;
import mobile.bts.com.viefund.CustomConnectApi.RenewSession;
import mobile.bts.com.viefund.CustomFont.CustomColor;
import mobile.bts.com.viefund.Entities.Plan;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.Model.UrlModel;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;
import mobile.bts.com.viefund.R;


public class PieChart  extends CustomColor {

    public static String TAG = PlanInfoActivity.class.getSimpleName();
    public static final String url = UrlModel.url_PieChart_Data;

    private String PlanID;

    //sqliteconn
    private DatabaseHelper mDB; // Helper for connect sqlite
    private Dao<mobile.bts.com.viefund.Entities.PieChart, String> _PieChartDao = null; // param dao for connet entities PlanAccount
    private Dao<Plan, String> _Plan = null;// param dao for connet entities Plan

    //create and style piechart
    private com.github.mikephil.charting.charts.PieChart mChart;
    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    private ArrayList<String> infoLabel;
    private ArrayList<Float> infoContent2;
    private ArrayList<String> infoContent2Str;
    private ArrayList<String> infoContent;
    //Infomation User
    private String MKV;
    private String Description;
    private String Owner;
    //Create TableLayout Data
    private TableLayout tableLayout;
    private TableRow tableRow;
    private TextView assetPie,percentPie,valuePie,totalPie;
    private Toolbar toolbar;
    float total = 0;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pie_chart);
        toolbar = (Toolbar) findViewById(R.id.toolbarPierchart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tw = toolbar.findViewById(R.id.app_bar_title);
        tw.setText(getString(R.string.nav_plan_asset) + "");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ChangeThemeToolbar(toolbar,null,(TextView)findViewById(R.id.app_bar_title));
        ChangeBackground();


        Bundle b = getIntent().getExtras();
        PlanID = b != null ? b.getString("PlanID") : "";
        Log.d(TAG, "onCreate: PlanID"+PlanID);
        mDB = new DatabaseHelper(this);
        //open dao
        try {
            _PieChartDao = getHelper().getPieChartDao();
            _Plan = getHelper().getPlanDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // setup PieChart
        infoLabel =new ArrayList<>();
        infoContent2 =new ArrayList<>();
        infoContent2Str = new ArrayList<>();
        infoContent=new ArrayList<>();
        SynchroData();
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.cons);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChart.isDrawHoleEnabled())
                { mChart.setDrawHoleEnabled(false);
                    mChart.setDrawCenterText(false);}
                else {
                    mChart.setDrawHoleEnabled(true);
                    mChart.setDrawCenterText(true);
                }
                mChart.invalidate();
            }
        });
        //setup DataTable
        tableLayout = (TableLayout) findViewById(R.id.maintable);
        tableLayout.removeAllViews();
        ImageView imageView = findViewById(R.id.app_bar_home);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }
    public void addHeaders(){
        /** Create a TableRow dynamically **/

        tableRow = new TableRow(this);
        tableRow.setLayoutParams(new LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tableRow.setBackgroundColor(Color.parseColor(trans));
        /** Creating a AssetClass to add to the row **/
        //tableRow.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
        TextView assetPie = new TextView(this);
        assetPie.setText(getString(R.string.Asset_Class));
        assetPie.setTextColor(getResources().getColor(R.color.white));
        assetPie.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        assetPie.setLayoutParams(new LayoutParams(TableRow.LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        assetPie.setPadding(5, 5, 0, 15);

        // Adding textView to tablerow.

        /** Creating a Percent to add to the row **/

        TextView percentPie = new TextView(this);
        percentPie.setText("%");
        percentPie.setTextColor(getResources().getColor(R.color.white));
        percentPie.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        percentPie.setLayoutParams(new LayoutParams(TableRow.LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        percentPie.setPadding(5, 5, 0, 15);

          // Adding textView to tablerow.

        /** Creating a Percent to add to the row **/

        TextView valuePie = new TextView(this);
        valuePie.setText(getString(R.string.Value));
        valuePie.setTextColor(getResources().getColor(R.color.white));
        valuePie.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        valuePie.setLayoutParams(new LayoutParams(TableRow.LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        valuePie.setPadding(5, 5, 0, 15);


        tableRow.addView(assetPie);
        tableRow.addView(valuePie);
        tableRow.addView(percentPie);
        tableLayout.addView(tableRow,new TableLayout.LayoutParams(

                TableRow.LayoutParams.FILL_PARENT,

                TableRow.LayoutParams.WRAP_CONTENT));
    }
    public void addData(){
        for(int i=0;i< infoContent2.size();i++)
        {
            tableRow = new TableRow(this);
            tableRow.setLayoutParams(new LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            Log.d(TAG, "addData: i: "+i);
//            tableRow.setId(i+1);
            String[] temp = String.valueOf(infoLabel.get(i)).split("\\s");
            String newObjDes = "";
            for(int j=0;j<temp.length;j++){

                if(j==3) newObjDes+=temp[j]+"\n";
                else newObjDes+=temp[j]+" ";
            }

            assetPie = new TextView(this);
//            assetPie.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
            assetPie.setText(newObjDes);
            assetPie.setTextColor(Color.BLACK);
            assetPie.setLayoutParams(new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            assetPie.setPadding(5, 15, 0, 15);

            percentPie = new TextView(this);
//            percentPie.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
            percentPie.setText(String.valueOf(infoContent2Str.get(i)));
            percentPie.setTextColor(Color.BLACK);
            percentPie.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            percentPie.setPadding(5, 15, 0, 15);

            valuePie = new TextView(this);
//            valuePie.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
            valuePie.setText(String.valueOf(infoContent.get(i)));
            valuePie.setTextColor(Color.BLACK);
            valuePie.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            valuePie.setPadding(5, 15, 0, 15);

            if(temp.length>3){
                percentPie.setText(String.valueOf(infoContent2Str.get(i))+"\n");
                valuePie.setText(String.valueOf(infoContent.get(i))+"\n");
            }
            tableRow.addView(assetPie);
            tableRow.addView(valuePie);
            tableRow.addView(percentPie);
            tableRow.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
            // Add the TableRow to the TableLayout


            tableLayout.addView(tableRow,new TableLayout.LayoutParams(

                    TableRow.LayoutParams.FILL_PARENT,

                    TableRow.LayoutParams.WRAP_CONTENT));
        }
    }
    private void addFooter(){
        tableRow = new TableRow(this);
        tableRow.setLayoutParams(new LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tableRow.setBackgroundColor(Color.parseColor(trans));
        /** Creating a AssetClass to add to the row **/
        //tableRow.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));

        TextView assetPie = new TextView(this);
        assetPie.setText("Total");
        assetPie.setTextColor(getResources().getColor(R.color.white));
        assetPie.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        assetPie.setLayoutParams(new LayoutParams(TableRow.LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        assetPie.setPadding(5, 15, 0, 15);

        // Adding textView to tablerow.

        /** Creating a Percent to add to the row **/

        TextView percentPie = new TextView(this);
        percentPie.setText("");
        percentPie.setTextColor(getResources().getColor(R.color.white));
        percentPie.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        percentPie.setLayoutParams(new LayoutParams(TableRow.LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        percentPie.setPadding(5, 15, 0, 15);

        // Adding textView to tablerow.

        /** Creating a Percent to add to the row **/

        TextView valuePie = new TextView(this);
        valuePie.setText(MKV);
        valuePie.setTextColor(getResources().getColor(R.color.white));
        valuePie.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        valuePie.setLayoutParams(new LayoutParams(TableRow.LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        valuePie.setPadding(5, 15, 0, 15);


        tableRow.addView(assetPie);
        tableRow.addView(valuePie);  // Adding textView to tablerow.
        tableRow.addView(percentPie);
        // Add the TableRow to the TableLayout
        tableLayout.addView(tableRow,new TableLayout.LayoutParams(

                TableRow.LayoutParams.FILL_PARENT,

                TableRow.LayoutParams.WRAP_CONTENT));
    }
    //funnction SuyncnroData
    public void SynchroData(){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loading_data));
        pDialog.show();
        pDialog.setCanceledOnTouchOutside(false);
        mDB.open();
        mDB.Delete_PieChart_All();
        mDB.close();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int result= Integer.parseInt(jObj.getString("RtnCode"));
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
                                MKV= objTemp.getString("MKV");
                                Description=objTemp.getString("Description");
                                Owner = objTemp.getString("Owner");
                                JSONObject objChild = objTemp.getJSONObject("PieChartData");
                                JSONArray arrayChildChild = objChild.getJSONArray("AssetClass");
                                if(arrayChildChild.length()>0){
                                    for (int n = 0 ; n<arrayChildChild.length();n++){
                                        JSONObject objTempChildChild = arrayChildChild.getJSONObject(n);
                                        Log.d(TAG, "onResponse: "+n+" objTempChildChild: "+objTempChildChild);
                                        mobile.bts.com.viefund.Entities.PieChart objEntities = new mobile.bts.com.viefund.Entities.PieChart();
                                        objEntities.setAccountID(planID);
                                        String  uniqueID = UUID.randomUUID().toString();
                                        Log.d(TAG, "onResponse: uid: "+uniqueID);
                                        objEntities.setID(uniqueID.toString());
                                        //InfoLabel
                                        String objRow = objTempChildChild.getString("InfoLabel");
                                        objEntities.setInfoLabel(objRow);
                                        //InfoContent2 String
                                        String objPercent = objTempChildChild.getString("InfoContent2");
                                        objEntities.setInfoContent2Str(objPercent);
                                        //InfoContent2 String to Float
                                        float objColumn=Float.parseFloat(objPercent.replace("%","").replace(",","."));
                                        objEntities.setInfoContent2(objColumn);

                                        //InfoContent
                                        String objMoney = objTempChildChild.getString("InfoContent");
                                        objEntities.setInfoContent(objMoney);

                                        _PieChartDao.create(objEntities);
                                        infoLabel.add(objRow);
                                        infoContent2.add(objColumn);
                                        infoContent.add(objMoney);
                                        infoContent2Str.add(objPercent);
                                    }
                                }
                            }
                            MainChart();
                            addHeaders();
                            addData();
                            addFooter();
                            pDialog.hide();
                        }
                        else if(result==1)
                        {
                            if(jObj.getString("ErrorMsg").startsWith(UrlModel.Msg_DieSession)){
                                RenewSession rns = new RenewSession(PieChart.class,getApplicationContext());
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
                } catch (Exception ex) {
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

    private void MainChart()
    {
        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        mChart = (com.github.mikephil.charting.charts.PieChart) findViewById(R.id.piechart);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterTextTypeface(mTfLight);
        mChart.setCenterText(generateCenterSpannableText());


        mChart.setDrawHoleEnabled(false);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(false);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);
        setData();

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // phần hiển thị tên label
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setYOffset(0f);
        l.setFormSize(5);
        l.setFormToTextSpace(0f);
        l.setWordWrapEnabled(true);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);

        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);

        //Turn Off xData
        mChart.setDrawEntryLabels(!mChart.isDrawEntryLabelsEnabled());
        mChart.invalidate();
        Log.d(TAG, "onValueSelected: "+infoContent2Str);
        // function click in chart
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                for(int i = 1;i<tableLayout.getChildCount()-1;i++)
                {
                    if(i == infoContent2.indexOf(e.getY())+1){
                        Log.d(TAG, "onValueSelected: in if: "+i);
                        tableLayout.getChildAt(i).setBackgroundColor(Color.rgb(212,212,212));
                    }
                    else {
                        Log.d(TAG, "onValueSelected: in else: "+i);
                        tableLayout.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
//                        tableLayout.getChildAt(i).setBackgroundColor(Color.WHITE);
                    }

                }

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }
    private void setData() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < infoContent2.size() ; i++) {
            entries.add(new PieEntry(infoContent2.get(i),
                    infoLabel.get(i % infoLabel.size())));

        }

        PieDataSet dataSet = new PieDataSet(entries, null);

        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS) colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(mTfLight);
        mChart.getLegend().setWordWrapEnabled(true);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        String centerText= Owner+"\n"+Description+"\n"+MKV;
        TextView text = (TextView) findViewById(R.id.txt_pie);
        text.setTextSize(15);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String red = Owner+" "+Description;
        SpannableString redSpannable= new SpannableString(red);
        redSpannable.setSpan(new ForegroundColorSpan(Color.parseColor(trans)), 0, red.length(), 0);
        builder.append(redSpannable);

        text.setText(builder, TextView.BufferType.SPANNABLE);

        int index = centerText.indexOf("\n");
        SpannableString s = new SpannableString(centerText);
        s.setSpan(new RelativeSizeSpan(2.0f), 0, index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new StyleSpan(Typeface.NORMAL), index, index, 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), index, s.length(), 0);
        return s;
    }
}

