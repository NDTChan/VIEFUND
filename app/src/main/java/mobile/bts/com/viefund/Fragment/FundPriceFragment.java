package mobile.bts.com.viefund.Fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.j256.ormlite.dao.Dao;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kobjects.util.Strings;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mobile.bts.com.viefund.Activity.MyMarkerView;
import mobile.bts.com.viefund.Activity.MyValueFormatter;
import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.ConnSqlite.DatabaseHelper;
import mobile.bts.com.viefund.CustomFont.CustomColor;
import mobile.bts.com.viefund.CustomFont.CustomColorFrag;
import mobile.bts.com.viefund.Entities.AccountFundPrice;
import mobile.bts.com.viefund.Entities.AccountInfo;
import mobile.bts.com.viefund.Entities.Plan;
import mobile.bts.com.viefund.Model.UrlModel;
import mobile.bts.com.viefund.R;

import static android.content.Context.WINDOW_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class FundPriceFragment extends CustomColorFrag {
    public static final String TAG = FundPriceFragment.class.getSimpleName();
    private String AccountID = "";
    private String AccountType = "";
    private TableLayout mTableLayout;
    public static final String url = UrlModel.url_AccountDetail_List;
    private String fund_accID="";
    private String fund_Des="";
    private LineChart mChart;
    //sqliteconn
    private DatabaseHelper mDB; // Helper for connect sqlite
    private Dao<AccountFundPrice, String> _FundPriceFragmentDao = null; // param dao for connet entities PlanAccount

    ///////
    ArrayList<String> xDays = new ArrayList<>();
    ArrayList<Double> yVals = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account_fund_price, container, false);
        if (this.getArguments() != null) {
            AccountID = this.getArguments().getString("AccountID");
            AccountType = this.getArguments().getString("AccountType");
            Log.d(TAG, "onCreateView: AccountID" + AccountID);
        }
        mDB = new DatabaseHelper(getContext());
        try {
            _FundPriceFragmentDao = mDB.getAccountFundPriceDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mTableLayout =v.findViewById(R.id.fragTableAccountPrice);
        mTableLayout.removeAllViews();
        mChart= v.findViewById(R.id.lineChart);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        mChart.getLayoutParams().height = height/3;
        Log.d(TAG, "onCreateView: display"+height+"And height/2="+height/2);
        //set Style
        // add data
        SynchroData();
        return v;
    }
    private void MainChart(){
        mChart.animateY(3000, Easing.EasingOption.EaseInExpo);
        mChart.setDrawGridBackground(false);
        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

//        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
//        mv.setChartView(mChart); // For bounds control
//        mChart.setMarker(mv); // Set the marker to the chart
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setEnabled(false);

        setData();

      //  mChart.invalidate();
    }

    public void EventClick(){
        final ScrollView scrollView = getView().findViewById(R.id.scrollView);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                for(int i =0;i<mTableLayout.getChildCount();i++)
                {
                    if(i == (int) e.getX()){
                        Log.d(TAG, "onValueSelected: in if: "+i);
                        mTableLayout.getChildAt(mTableLayout.getChildCount()-i-1).setBackgroundColor(Color.rgb(212,212,212));
                        scrollView.smoothScrollTo(0, mTableLayout.getChildAt(mTableLayout.getChildCount()-i-1).getTop());
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                try {Thread.sleep(100);} catch (InterruptedException e) {}
                                mChart.requestFocus();
                            }
                        });
                    }


                    else {
                        Log.d(TAG, "onValueSelected: in else: "+i);

                        mTableLayout.getChildAt(mTableLayout.getChildCount()-i-1).setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
                        ;
                    }

                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }
    private void setData() {

        List<Entry> entries = new ArrayList<>();
        Log.d(TAG, "setData: size"+xDays+"and"+yVals);

        Log.d(TAG, "setData(Reverse): "+xDays+"\n"+yVals);

        for (int i = 0; i < yVals.size(); i++) {
            entries.add(new BarEntry(i , Float.parseFloat(String.valueOf(yVals.get(i)))));
        }

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setAxisMinimum(0);
        xAxis.setTextSize(6.5f);
        xAxis.removeAllLimitLines();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xDays.get(((int) value));
            }
        });
        YAxis leftAxis = mChart.getAxisLeft();
       // leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setLabelCount(5,true);
        leftAxis.setGranularity(0.01f);
        leftAxis.setTextSize(6.5f);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
//        // sort by x-value
        Collections.sort(entries, new EntryXComparator());

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(entries, "DataSet 1");
        set1.setValueFormatter(new MyValueFormatter());//Add $ vào  trước leftyAxis
        set1.setValueTextSize(5f);
        set1.setLineWidth(1.5f);
        set1.setCircleRadius(2.5f);
        set1.setColor(Color.parseColor(trans));
        set1.setCircleColor(Color.parseColor(trans));
        // create a data object with the datasets
        LineData data = new LineData(set1);
        data.setValueFormatter(new DefaultValueFormatter(2));

        // set data
        mChart.setData(data);
        mChart.invalidate();
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
        TextView tv = new TextView(getContext());
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(15, 15, 15, 15);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.LEFT);
        return tv;
    }
    private TextView getTextViewSecond( String title, int color, int typeface) {
        TextView tv = new TextView(getContext());
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(15, 15, 0, 15);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.LEFT);
        return tv;
    }
    private TextView getTextViewThird( String title, int color, int typeface) {
        TextView tv = new TextView(getContext());
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(15, 15, 0, 15);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.LEFT);
        return tv;
    }
    private LinearLayout layoutCustemFirst(TextView tv)
    {
        final LinearLayout layCustomerTotal = new LinearLayout(getActivity());
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
        final LinearLayout layCustomerTotal = new LinearLayout(getActivity());
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 2);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);

        layCustomerTotal.setBackgroundColor(Color.parseColor(trans));
        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }
    private LinearLayout layoutCustemThird(TextView tv)
    {
        final LinearLayout layCustomerTotal = new LinearLayout(getActivity());
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 5);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);
        layCustomerTotal.setBackgroundColor(Color.parseColor(trans));
        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }
    private LinearLayout layoutCustemFirstData(TextView tv)
    {
        final LinearLayout layCustomerTotal = new LinearLayout(getActivity());
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 9);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);

        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }
    private LinearLayout layoutCustemSecondData(TextView tv)
    {
        final LinearLayout layCustomerTotal = new LinearLayout(getActivity());
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 3);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);

        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }
    private LinearLayout layoutCustemThirdData(TextView tv)
    {
        final LinearLayout layCustomerTotal = new LinearLayout(getActivity());
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);
        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }
    public void addHeadersVsData(List<AccountFundPrice> obj){
        TableRow tableRow = new TableRow(getContext());
        tableRow.setLayoutParams(getLayoutParams());
        tableRow.setBackgroundColor(Color.parseColor(trans));
        tableRow.addView(layoutCustemFirst(getTextViewFirst(fund_accID+"\n"+fund_Des,getResources().getColor(R.color.white),Typeface.BOLD)));
        tableRow.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
        TableLayout headerLayout = getView().findViewById(R.id.header_linechart);
        headerLayout.addView(tableRow,new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        int rows = obj.size();
        for (int i = rows-1; i >-1; i--) {
            AccountFundPrice row = obj.get(i);
            TableRow tableRow1 = new TableRow(getContext());
            tableRow1.setLayoutParams(getLayoutParams());
            tableRow1.addView(layoutCustemFirstData(getTextViewFirst(String.valueOf(row.getDate().toString()),Color.BLACK,Typeface.NORMAL)));
            tableRow1.addView(layoutCustemThirdData(getTextViewThird(String.valueOf(row.getNAV()),Color.BLACK,Typeface.NORMAL)));
            tableRow1.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));

            mTableLayout.addView(tableRow1,getTblLayoutParams());
            mTableLayout.invalidate();
            mTableLayout.refreshDrawableState();
        }
    }

    public void SynchroData() {
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.loading_data));
        pDialog.show();
        pDialog.setCanceledOnTouchOutside(false);
        mDB.open();
        mDB.Delete_AccountFundPrice_All();
        mDB.close();
        RequestQueue mRequest = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse() returned: " + response);
                try {
                    List<AccountFundPrice> obj2 ;
                    JSONObject jObj = new JSONObject(response);
                    int result = Integer.parseInt(jObj.getString("RtnCode"));
                    if (result == 0) {
                        JSONObject objAccountDetail = new JSONObject(jObj.getString("AccountDetail"));
                        fund_accID=objAccountDetail.getString("AccountID");
                        fund_Des=objAccountDetail.getString("Description");
                        JSONObject objPriceList = new JSONObject(objAccountDetail.getString("PriceList"));
                        JSONArray arrayPriceList = objPriceList.getJSONArray("PriceList");
                        if (arrayPriceList.length() > 0) {
                            for (int i = arrayPriceList.length()-1; i >-1; i--) {
                                JSONObject objTemp = arrayPriceList.getJSONObject(i);
                                AccountFundPrice objEntities = new AccountFundPrice();
                                objEntities.setAccountID(objAccountDetail.getString("ID"));
                                String  uniqueID = UUID.randomUUID().toString();
                                objEntities.setID(uniqueID.toString());
                                objEntities.setDate(objTemp.getString("Date"));
                                objEntities.setDate101(objTemp.getString("Date101"));
                                xDays.add(objTemp.getString("Date101"));
                                objEntities.setNAV(objTemp.getString("Price"));
                                objEntities.setPriceValue(objTemp.getDouble("PriceValue"));
                                yVals.add(objTemp.getDouble("PriceValue"));
                                _FundPriceFragmentDao.create(objEntities);

                            }
                        }

                        obj2 = AccountFundPrice.Load_AccountFundPrice_By_AccountID(_FundPriceFragmentDao, AccountID);
                        MainChart();
                        addHeadersVsData(obj2);
                        EventClick();
                        pDialog.hide();
                    } else if (result == 1) {
                        Toast.makeText(getContext(),
                                jObj.getString("ErrorMsg"), Toast.LENGTH_LONG).show();
                        pDialog.hide();
                    }
                    pDialog.dismiss();
                } catch (Exception ex) {
                    Toast.makeText(getContext(),
                            ex + "", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse() returned: " + ex);
                    pDialog.dismiss();
                    pDialog.hide();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),
                        error + "", Toast.LENGTH_LONG).show();
                pDialog.hide();
                Log.d(TAG, "onErrorResponse() returned: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d(TAG, "getParams: " + BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("iOptions", "2");
                params.put("iAccountType", AccountType);
                params.put("iAccountID", AccountID);
                params.put("lg",BTApplication.getInstance().getPrefManager().getUser().getLang());
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
