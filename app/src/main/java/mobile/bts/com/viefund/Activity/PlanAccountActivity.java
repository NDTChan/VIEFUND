package mobile.bts.com.viefund.Activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import org.w3c.dom.Attr;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import mobile.bts.com.viefund.Entities.PlanAsset;
import mobile.bts.com.viefund.Entities.PlanGroup;
import mobile.bts.com.viefund.Entities.PlantAccount;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.Model.PlanDetail;
import mobile.bts.com.viefund.Model.PlanModel;
import mobile.bts.com.viefund.Model.UrlModel;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;
import mobile.bts.com.viefund.R;

/**
 * create by VUDQ_15/02/2018
 * activity for screen PlanAccount
 */
public class PlanAccountActivity extends CustomColor {
    public static String TAG = PlanAccountActivity.class.getSimpleName();
    public static final String url = UrlModel.url_PlanAccount_List;

    private String PlanID = "";
    private TableLayout mTableLayout;
    private Toolbar toolbar;

    private String[] inves_Des;
    private String[] inves_AccGroup;

    //sqliteconn
    private DatabaseHelper mDB; // Helper for connect sqlite
    private Dao<PlantAccount, String> _PlanAccountDao = null; // param dao for connet entities PlanAccount
    private Dao<PlanGroup, String> _PlanGroupDao = null; // param dao for connet entities PlanAccount
    private Dao<Plan, String> _Plan = null;// param dao for connet entities Plan


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_account);
        //set property for toolbar

        toolbar = (Toolbar) findViewById(R.id.toolbarPlanAccount);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tw = (TextView) toolbar.findViewById(R.id.app_bar_title);
        tw.setText("");
        tw.setText(getString(R.string.nav_plan_account) + "");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mTableLayout = (TableLayout) findViewById(R.id.tablePlanAccount);
        mTableLayout.removeAllViews();
        mDB = new DatabaseHelper(this);
        //get PlanId from bundle
        Bundle b = getIntent().getExtras();
        if (b != null) {
            PlanID = b.getString("PlanID");
        }
        Log.d(TAG, "onCreate: PlanID: " + PlanID);
        try {
            _PlanAccountDao = getHelper().getPlanAccountDao();
            _PlanGroupDao = getHelper().getPlanGroupDao();
            _Plan = getHelper().getPlanDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TextView textView= (TextView) findViewById(R.id.app_bar_title);
        ChangeThemeToolbar(toolbar,null,textView);
        ChangeBackground();
        SynchroData();
        ImageView imageView = findViewById(R.id.app_bar_home);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }


    public void LoadTableLayout(final Plan obj, List<PlantAccount> account, List<PlanGroup> listPlanGroup, String totalMoney) {

//        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
//        int height = display.getHeight();
        ArrayList<PlanGroup> dataPlanGroup = new ArrayList<>();
        dataPlanGroup.addAll(listPlanGroup);
        ArrayList<PlantAccount> data = new ArrayList<>();
        data.addAll(account);
        int rows = data.size();
        int leftRowMargin = 1;
        int topRowMargin = 1;
        int rightRowMargin = 1;
        int bottomRowMargin = 1;
        int textSize = 0, smallTextSize = 0, mediumTextSize = 0;
        NumberFormat formater = new DecimalFormat("#,###.##");

        TextView textSpacer = null;
        int idTable = 0;
        double grandTotal = 0;
//            grandTotal = grandTotal + row.getPlanGroupTotal();
        int rowListPlanGroup = listPlanGroup.size();
        int rowChilds = account.size();
        final TextView tv = new TextView(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv.setGravity(Gravity.LEFT);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tv.setPadding(25, 20, 0, 15);

        tv.setBackgroundColor(Color.parseColor(trans));
        tv.setText(String.valueOf(obj.getPlanType() + " " + obj.getDescription()));
        tv.setTextColor(Color.parseColor("#f8f8f8"));

        final LinearLayout layCustomer = new LinearLayout(this);
        layCustomer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 5);
//        layParams.setMargins(leftRowMargin, topRowMargin, 0, bottomRowMargin);

        layCustomer.setLayoutParams(layParams);
        layCustomer.setPadding(0, 10, 0, 10);
        layCustomer.setBackgroundColor(Color.parseColor(trans));
        layCustomer.addView(tv);

        final TextView tv4 = new TextView(this);
        tv4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv4.setGravity(Gravity.LEFT);
//        tv4.setPadding(5, 15, 0, 15);
        tv4.setBackgroundColor(Color.parseColor(trans));
        tv4.setText("");

        final LinearLayout layCustomer4 = new LinearLayout(this);
        layCustomer4.setOrientation(LinearLayout.VERTICAL);
        layCustomer4.setPadding(0, 10, 0, 10);
        layCustomer4.setBackgroundColor(Color.parseColor(trans));
        LinearLayout.LayoutParams layParams4 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 4);
//        layParams4.setMargins(0, topRowMargin, rightRowMargin, bottomRowMargin);

        layCustomer4.setLayoutParams(layParams4);
        layCustomer4.addView(tv4);

        final TextView tv5 = new TextView(this);
        tv5.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv5.setGravity(Gravity.LEFT);
        tv5.setPadding(5, 25, 0, 15);
        tv5.setBackgroundColor(Color.parseColor(trans));
        tv5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_featured_play_list_white_24dp, 0, 0, 0);
        //tv5.setText("click");
        final LinearLayout layCustomer5 = new LinearLayout(this);
        layCustomer5.setOrientation(LinearLayout.VERTICAL);
        layCustomer5.setPadding(10, 10, 0, 10);
        layCustomer5.setBackgroundColor(Color.parseColor(trans));
        LinearLayout.LayoutParams layParams5 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1);
//        layParams5.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
        layCustomer5.setLayoutParams(layParams5);
        layCustomer5.addView(tv5);
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlanAccountActivity.this, PlanInfoActivity.class);
                Bundle b = new Bundle();
                b.putString("PlanID", obj.getPlanID()); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);

            }
        });

        // add table row
        final TableRow tr = new TableRow(this);
        tr.setId(idTable);
        TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
//        trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
        tr.setWeightSum(10);
        tr.setPadding(0, 0, 0, 0);
        tr.setLayoutParams(trParams);
        tr.addView(layCustomer);
        tr.addView(layCustomer4);
        tr.addView(layCustomer5);


        idTable++;
        //add vao table lay out
       // mTableLayout.addView(tr, trParams);
        TableLayout tableLayout = findViewById(R.id.header);
        tableLayout.addView(tr,trParams);
        for (int i = 0; i < rowListPlanGroup; i++) {
            final PlanGroup itemChild = dataPlanGroup.get(i);
            final TextView tvFirst1 = new TextView(this);
            tvFirst1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tvFirst1.setGravity(Gravity.LEFT);
            tvFirst1.setPadding(5, 20, 0, 15);
            tvFirst1.setBackgroundColor(Color.parseColor("#f8f8f8"));
            tvFirst1.setText(itemChild.getDescription());
            tvFirst1.setTypeface(null,Typeface.BOLD);
            tvFirst1.setTextColor(Color.parseColor(trans));

            final LinearLayout layCustomerFirst1 = new LinearLayout(this);
            layCustomerFirst1.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layParamsFirst1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 7);
                layCustomerFirst1.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape_green));
            layCustomerFirst1.setLayoutParams(layParamsFirst1);
            layCustomerFirst1.setPadding(10, 10, 10, 10);
            layCustomerFirst1.addView(tvFirst1);

            final TextView tvFirst2 = new TextView(this);
            tvFirst2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tvFirst2.setGravity(Gravity.LEFT);
            tvFirst2.setPadding(5, 20, 8, 15);
            tvFirst2.setBackgroundColor(Color.parseColor("#f8f8f8"));
            tvFirst2.setText(String.valueOf(itemChild.getAccountGroupTotal()));
            tvFirst2.setTypeface(null,Typeface.BOLD);
            tvFirst2.setTextColor(Color.parseColor(trans));
            tvFirst2.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            final LinearLayout layCustomerFirst2 = new LinearLayout(this);
            layCustomerFirst2.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layParamsFirst2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 4);
                layCustomerFirst2.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape_green));
            layCustomerFirst2.setLayoutParams(layParamsFirst2);
            layCustomerFirst2.setPadding(10, 10, 8, 10);
           // layCustomerFirst2.addView(tvFirst2);

            //setText 3
            final TextView tvFirst3 = new TextView(this);
            tvFirst3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tvFirst3.setGravity(Gravity.LEFT);
            tvFirst3.setPadding(5, 20, 8, 15);
            tvFirst2.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            tvFirst3.setBackgroundColor(Color.parseColor("#f8f8f8"));
            layCustomerFirst2.addView(tvFirst3);
            final LinearLayout layCustomerFirst3 = new LinearLayout(this);
            TableRow.LayoutParams layParamsFirst3 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 3);
                layCustomerFirst3.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape_green));
            layCustomerFirst3.setLayoutParams(layParamsFirst3);
            layCustomerFirst3.setOrientation(LinearLayout.VERTICAL);
            layCustomerFirst3.setPadding(10, 10, 0, 10);
            layCustomerFirst3.addView(tvFirst2);

            final TableRow trFirst = new TableRow(this);
            trFirst.setId(idTable);
            TableLayout.LayoutParams trParamsFirst = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
            //trParams.setMargins(0, 0, 0, bottomRowMargin);
            trFirst.setPadding(0, 0, 0, 0);
            trFirst.setLayoutParams(trParamsFirst);
            trFirst.setWeightSum(10);

            trFirst.addView(layCustomerFirst1);
            //trFirst.addView(layCustomerFirst2);
            trFirst.addView(layCustomerFirst3);
            mTableLayout.addView(trFirst);
            idTable++;
            for (int j = 0; j < rowChilds; j++) {
                // create row of table
                final TableRow tr2 = new TableRow(this);
                tr2.setId(idTable);
                TableLayout.LayoutParams trParams2 = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
//                tr2.setPadding(0, 0, 0, 0);
                tr2.setLayoutParams(trParams2);
                tr2.setWeightSum(10);
                // end create row of table
                final PlantAccount rowChild = data.get(j);
                // set Text 1
                if (rowChild.getType().equals(itemChild.getAccountType())) {
                    final TextView tv1 = new TextView(this);
                    tv1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    tv1.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    tv1.setGravity(Gravity.LEFT);
                    // tv1.setTypeface(tv.getTypeface(), Typeface.BOLD);
                    tv1.setPadding(5, 15, 0, 15);
                    tv1.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    tv1.setText(String.valueOf(rowChild.getAccountID()));
                    final TextView tv11 = new TextView(this);
                    tv11.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    tv11.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    tv11.setGravity(Gravity.LEFT);
                    // tv1.setTypeface(tv.getTypeface(), Typeface.BOLD);
                    tv11.setPadding(5, 15, 0, 15);
                    tv11.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    tv11.setSingleLine(false);
                    tv11.setEllipsize(TextUtils.TruncateAt.END);
                    //tv11.setLines(2);
                    tv1.setMaxLines(3);
                    int maxLength = 20;
                    InputFilter[] fArray = new InputFilter[1];
                    fArray[0] = new InputFilter.LengthFilter(maxLength);
                   // tv11.setFilters(fArray);
                    String textTV11 = rowChild.getAccountDesc();
                    int lengTextTV11 = textTV11.length();
                    int countrow = lengTextTV11 / 20;
                    tv11.setText(String.valueOf(rowChild.getAccountDesc()));
                    Log.d(TAG, "LoadTableLayout: tr2.getWidth(): "+(mTableLayout.getWidth()/100)*70);
                    final LinearLayout layCustomer1 = new LinearLayout(this);
                    layCustomer1.setOrientation(LinearLayout.VERTICAL);
//                    mTableLayout.getWidth()/100)*70
                    LinearLayout.LayoutParams layParams1 = new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,6);
                        layCustomer1.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
                    layCustomer1.setLayoutParams(layParams1);
                    layCustomer1.setPadding(10, 10, 0, 10);
                    layCustomer1.addView(tv1);
                    layCustomer1.addView(tv11);

                    //setText 3
                    final TextView tv3 = new TextView(this);
                    tv3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));
                    tv3.setPadding(5, 50, 1, 15);
                    //tv3.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                    tv3.setBackgroundColor(Color.parseColor(
                            "#f8f8f8"));
                    tv3.setText(String.valueOf(rowChild.getMKV()));
                    final LinearLayout layCustomer3 = new LinearLayout(this);
                    TableRow.LayoutParams layParams3 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,3);
                        layCustomer3.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
                    layCustomer3.setLayoutParams(layParams3);
                    layCustomer3.setOrientation(LinearLayout.VERTICAL);
                    layCustomer3.setPadding(10, 10, 10, 10);
                    layCustomer3.addView(tv3);

                    final TextView tv6 = new TextView(this);
                    tv6.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    tv6.setPadding(3, 50, 0, 15);
                    tv6.setBackgroundColor(Color.parseColor(
                            "#f8f8f8"));
                    tv6.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_right_green_700_24dp,0,0,0);
                    final LinearLayout layCustomer6 = new LinearLayout(this);
                    TableRow.LayoutParams layParams6 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT,1);
                    layCustomer6.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
                    layCustomer6.setLayoutParams(layParams6);
                    layCustomer6.setOrientation(LinearLayout.VERTICAL);
                    layCustomer6.setPadding(10, 10, 0, 10);
                    layCustomer6.addView(tv6);


                    tr2.addView(layCustomer1);
                    tr2.addView(layCustomer3);
                    tr2.addView(layCustomer6);
                    mTableLayout.addView(tr2);

                    layCustomer6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(PlanAccountActivity.this, AccountDetailActivity.class);
                            Bundle b = new Bundle();
                            b.putString("AccountID", rowChild.getID()); //Your id
                            b.putString("AccountType", rowChild.getType()); //Your id
                            intent.putExtras(b); //Put your id to your next Intent
                            startActivity(intent);
                        }
                    });
                    idTable++;
                }
            }
        }
        final TextView tvTotal = new TextView(this);
        tvTotal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        tvTotal.setGravity(Gravity.LEFT);
        tvTotal.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tvTotal.setPadding(25, 20, 0, 15);

        tvTotal.setBackgroundColor(Color.parseColor(trans));
        tvTotal.setAllCaps(true);
        tvTotal.setText("Total");
        tvTotal.setTextColor(Color.parseColor("#f8f8f8"));

        final LinearLayout layCustomerTotal = new LinearLayout(this);
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 5);
//        layParamsTotal.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);

        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(5, 10, 0, 10);
        layCustomerTotal.setBackgroundColor(Color.parseColor(trans));
        layCustomerTotal.addView(tvTotal);

        final TextView tv4Total = new TextView(this);
        tv4Total.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv4Total.setGravity(Gravity.LEFT);
        tv4Total.setPadding(15, 20, 0, 15);
        tv4Total.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tv4Total.setBackgroundColor(Color.parseColor(trans));
        tv4Total.setTextColor(Color.parseColor("#f8f8f8"));

        final LinearLayout layCustomer4Total = new LinearLayout(this);
        layCustomer4Total.setOrientation(LinearLayout.VERTICAL);
        layCustomer4Total.setPadding(0, 10, 0, 10);
        layCustomer4Total.setBackgroundColor(Color.parseColor(trans));
        LinearLayout.LayoutParams layParams4Total = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 4);
//        layParams4Total.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);

        layCustomer4Total.setLayoutParams(layParams4Total);
        layCustomer4Total.addView(tv4Total);

        final TextView tv5Total = new TextView(this);
        tv5Total.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv5Total.setGravity(Gravity.LEFT);
        tv5Total.setPadding(10, 5, 0, 15);
        tv5Total.setBackgroundColor(Color.parseColor(trans));
        tv5Total.setTextColor(Color.parseColor("#f8f8f8"));
        tv5Total.setText(totalMoney);
        //tv5Total.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pie_chart_white_24dp, 0, 0, 0);
        //tv5Total.setText("click");
        final LinearLayout layCustomer5Total = new LinearLayout(this);
        layCustomer5Total.setOrientation(LinearLayout.VERTICAL);
        layCustomer5Total.setPadding(5, 20, 0, 15);
        layCustomer5Total.setBackgroundColor(Color.parseColor(trans));
        LinearLayout.LayoutParams layParams5Total = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1);
//        layParams5Total.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
        layCustomer5Total.setLayoutParams(layParams5Total);
        layCustomer5Total.addView(tv5Total);

        tv5Total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlanAccountActivity.this, PieChart.class);
                Bundle b = new Bundle();
                b.putString("PlanID", PlanID);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        // add table row
        final TableRow trTotal = new TableRow(this);
        trTotal.setId(idTable);
        TableLayout.LayoutParams trParamsTotal = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
//        trParamsTotal.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
        trTotal.setWeightSum(10);
        trTotal.setPadding(0, 0, 0, 35);
        trTotal.setLayoutParams(trParamsTotal);
        trTotal.addView(layCustomerTotal);
        trTotal.addView(layCustomer4Total);
        trTotal.addView(layCustomer5Total);


        idTable++;
        //add vao table lay out
        mTableLayout.addView(trTotal, trParamsTotal);
//        TableLayout tableLayout1 = (TableLayout)findViewById(R.id.footer);
//        tableLayout1.addView(tr,trParams);
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
        tv.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        tv.setPadding(10, 15, 0, 15);
        tv.setTextColor(color);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        return tv;
    }
    private TextView getTextViewFourth(int id, int image,int bgColor,int textColor) {
        TextView tv = new TextView(getApplicationContext());
        tv.setId(id);
        tv.setCompoundDrawablesWithIntrinsicBounds(image,0,0,0);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setPadding(5, 10, 0, 10);
        tv.setBackgroundColor(bgColor);
        Drawable drawables[] = tv.getCompoundDrawables();
        drawables[0].setColorFilter(new PorterDuffColorFilter(textColor, PorterDuff.Mode.SRC_IN));

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
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);
        layCustomerTotal.setBackgroundColor(Color.parseColor(trans));
        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }
    private LinearLayout layoutCustomFourth(TextView tv){
        final LinearLayout layCustomerTotal = new LinearLayout(this);
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);
        layCustomerTotal.setBackgroundColor(Color.parseColor(trans));
        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }
    public void AddData(final Plan obj, List<PlantAccount> account, List<PlanGroup> listPlanGroup, String totalMoney){
        ArrayList<PlanGroup> dataPlanGroup = new ArrayList<>();
        dataPlanGroup.addAll(listPlanGroup);
        ArrayList<PlantAccount> data = new ArrayList<>();
        data.addAll(account);
        int idTable = 0;
        int rowListPlanGroup = listPlanGroup.size();
        int rowChilds = account.size();
        TableRow tr = new TableRow(getApplicationContext());
        tr.setLayoutParams(getLayoutParams());
        tr.setBackgroundColor(Color.parseColor(trans));
        tr.addView(layoutCustemFirst(getTextViewFirst(0,String.valueOf(obj.getPlanType() + " " + obj.getDescription()), Color.WHITE, Typeface.NORMAL,Color.parseColor(trans))));
        tr.addView(layoutCustemSecond(getTextViewSecond(Color.parseColor(trans))));
        tr.addView(layoutCustemThird(getTextViewThird(0,"aaaaaaaa", Color.parseColor(trans), Typeface.BOLD,Color.parseColor(trans))));
        TextView textView1=getTextViewFourth(0,R.drawable.ic_keyboard_arrow_right_white_24dp,Color.parseColor(trans),Color.WHITE);
        tr.addView(layoutCustomFourth(textView1));
        tr.setId(idTable);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlanAccountActivity.this, PlanInfoActivity.class);
                Bundle b = new Bundle();
                b.putString("PlanID", obj.getPlanID()); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);

            }
        });
        idTable++;
        TableLayout tableLayout = findViewById(R.id.header);
        tableLayout.addView(tr, getTblLayoutParams());

        for (int i = 0; i < rowListPlanGroup; i++) {
            final PlanGroup itemChild = dataPlanGroup.get(i);
            TableRow tr1 = new TableRow(getApplicationContext());
            tr1.setLayoutParams(getLayoutParams());
            tr1.addView(layoutCustemFirst(getTextViewFirst(i, itemChild.getDescription(), Color.WHITE, Typeface.BOLD,Color.parseColor(trans))));
            tr1.addView(layoutCustemSecond(getTextViewSecond(Color.parseColor(trans))));
            tr1.addView(layoutCustemThird(getTextViewThird(i,String.valueOf(itemChild.getAccountGroupTotal()), Color.WHITE, Typeface.BOLD,Color.parseColor(trans))));
            TextView textViewHide=getTextViewFourth(0,R.drawable.ic_keyboard_arrow_right_green_700_24dp,Color.parseColor(trans),Color.parseColor(trans));
            tr1.addView(layoutCustomFourth(textViewHide));
            mTableLayout.addView(tr1, getTblLayoutParams());
            tr1.setId(idTable);
            idTable++;
            for (int j = 0; j < rowChilds; j++) {
                final PlantAccount rowChild = data.get(j);
                TableRow tr2 = new TableRow(getApplicationContext());
                TableLayout.LayoutParams trParamsTotal = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
                tr2.setLayoutParams(trParamsTotal);
                if (rowChild.getType().equals(itemChild.getAccountType())) {

                    TextView  viewText = getTextViewThird(0,String.valueOf(rowChild.getMKV()),Color.parseColor(trans), Typeface.NORMAL,Color.parseColor("#f8f8f8"));
                    TextView textView2=getTextViewFourth(0,R.drawable.ic_keyboard_arrow_right_green_700_24dp,Color.parseColor("#f8f8f8"),Color.parseColor(trans));
                    tr2.addView(layoutCustemFirst(getTextViewFirst(i, String.valueOf(rowChild.getAccountID()), Color.BLACK, Typeface.NORMAL,Color.parseColor("#f8f8f8"))));
                    tr2.addView(layoutCustemSecond(getTextViewSecond(Color.parseColor("#f8f8f8"))));
                    tr2.addView(layoutCustemThird(viewText));
                    tr2.addView(layoutCustomFourth(textView2));
                    tr2.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
                    viewText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(PlanAccountActivity.this, AccountDetailActivity.class);
                            Bundle b = new Bundle();
                            b.putString("AccountID", rowChild.getID()); //Your id
                            b.putString("AccountType", rowChild.getType()); //Your id
                            intent.putExtras(b); //Put your id to your next Intent
                            startActivity(intent);
                        }
                    });
                    textView2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(PlanAccountActivity.this, AccountDetailActivity.class);
                            Bundle b = new Bundle();
                            b.putString("AccountID", rowChild.getID()); //Your id
                            b.putString("AccountType", rowChild.getType()); //Your id
                            intent.putExtras(b); //Put your id to your next Intent
                            startActivity(intent);
                        }
                    });
                    idTable++;
                }
                mTableLayout.addView(tr2, getTblLayoutParams());
            }
        }
        TableRow tr3 = new TableRow(getApplicationContext());
        tr3.setId(idTable);
        TableLayout.LayoutParams trParamsTotal = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
        tr3.setLayoutParams(trParamsTotal);
        TextView textView = getTextViewThird(0,totalMoney, Color.WHITE, Typeface.NORMAL,Color.parseColor(trans));
        TextView textView3=getTextViewFourth(0,R.drawable.ic_pie_chart_white_24dp,Color.parseColor(trans),Color.WHITE);
        tr3.setBackgroundColor(Color.parseColor(trans));
        tr3.addView(layoutCustemFirst(getTextViewFirstCapTitle(0,"Total", Color.WHITE, Typeface.BOLD,Color.parseColor(trans))));
        tr3.addView(layoutCustemSecond(getTextViewSecond(Color.parseColor(trans))));
        tr3.addView(layoutCustemThird(textView));
        tr3.addView(layoutCustomFourth(textView3));
        TableLayout tableLayout1 = findViewById(R.id.footer);
        tableLayout1.addView(tr3, getTblLayoutParams());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlanAccountActivity.this, PieChart.class);
                Bundle b = new Bundle();
                b.putString("PlanID", PlanID);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlanAccountActivity.this, PieChart.class);
                Bundle b = new Bundle();
                b.putString("PlanID", PlanID);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        idTable++;
    }
    private String totalMoney2;

    public void SynchroData() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loading_data));
        pDialog.show();
        pDialog.setCanceledOnTouchOutside(false);
        mDB.open();
//        mDB.Delete_Plan_Account_All();
        mDB.Delete_Plan_Group_All();
//        mDB.close();
        RequestQueue mRequest = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse() returned: " + response);
                try {
                    Plan obj = new Plan();
                    List<PlanGroup> listObjPlanGroup = new ArrayList<PlanGroup>();
                    List<PlantAccount> obj2 = new ArrayList<PlantAccount>();
                    JSONObject jObj = new JSONObject(response);
                    Log.d(TAG, "onResponse: jObj: "+jObj);
                    int result = Integer.parseInt(jObj.getString("RtnCode"));
                    if (result == 0) {
                        JSONObject objPortfolio = new JSONObject(jObj.getString("Portfolio"));
                        JSONArray objPlanGroupArr = objPortfolio.getJSONArray("PlanGroupList");
                        JSONObject objPlanGroupList = objPlanGroupArr.getJSONObject(0);
                        Log.d(TAG, "onResponse: objPlanGroupList: " + objPlanGroupList);
//                        String planType = objPlanGroupList.getString("PlanType");
                        JSONArray arrList = objPlanGroupList.getJSONArray("PlanList");
                        totalMoney2=objPlanGroupList.getString("PlanGroupTotal");
                        String planID = "";

                        if (arrList.length() > 0) {
                            for (int i = 0; i < arrList.length(); i++) {
                                JSONObject objTemp = arrList.getJSONObject(i);
                                planID = objTemp.getString("ID");
                                if(!objTemp.isNull("AccountGroupList")) {
                                    JSONArray arrayChild = objTemp.getJSONArray("AccountGroupList");
                                    if (arrayChild.length() > 0) {
                                        for (int j = 0; j < arrayChild.length(); j++) {
                                            JSONObject objTempChild = arrayChild.getJSONObject(j);
                                            PlanGroup objPlanGroupEntities = new PlanGroup();
                                            String uniqueID = UUID.randomUUID().toString();
                                            objPlanGroupEntities.setID(uniqueID);
                                            objPlanGroupEntities.setPlanID(planID);
                                            objPlanGroupEntities.setAccountType(objTempChild.getString("AccountType"));
                                            objPlanGroupEntities.setAccountGroupTotal(objTempChild.getString("AccountGroupTotal"));
                                            objPlanGroupEntities.setDescription(objTempChild.getString("Description"));
                                            _PlanGroupDao.create(objPlanGroupEntities);
                                            JSONArray arrayChildChild = objTempChild.getJSONArray("AccountList");
                                            if (arrayChildChild.length() > 0) {
                                                for (int n = 0; n < arrayChildChild.length(); n++) {
                                                    JSONObject objTempChildChild = arrayChildChild.getJSONObject(n);
                                                    Log.d(TAG, "onResponse: " + n + " objTempChildChild: " + objTempChildChild);
                                                    PlantAccount objEntities = new PlantAccount();
                                                    objEntities.setPlanID(planID);
                                                    objEntities.setType(objTempChild.getString("AccountType"));
                                                    objEntities.setAccountID(objTempChildChild.getString("AccountID"));
                                                    objEntities.setID(objTempChildChild.getString("ID"));
                                                    if (objTempChildChild.isNull("Description")) {
                                                        objEntities.setAccountDesc(objTempChild.getString("Description"));
                                                    } else {
                                                        objEntities.setAccountDesc(objTempChildChild.getString("Description"));
                                                    }
                                                    objEntities.setMKV(objTempChildChild.getString("MKV"));

                                                    mDB.Delete_Plan_Account_By_Account_ID(objEntities.getID());
                                                    _PlanAccountDao.create(objEntities);
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                        obj2 = PlantAccount.Load_Plan_By_PlanID(_PlanAccountDao, planID);
                        obj = Plan.Load_Plan_By_ID(_Plan, PlanID);
                        listObjPlanGroup = PlanGroup.Load_PlanGroup(_PlanGroupDao, planID);
//                      LoadTableLayout(obj, obj2, listObjPlanGroup, totalMoney2);
                        AddData(obj, obj2, listObjPlanGroup, totalMoney2);

                        pDialog.hide();
                    } else if (result == 1) {
                        if (jObj.getString("ErrorMsg").startsWith(UrlModel.Msg_DieSession)) {
                            RenewSession rns = new RenewSession(PlanAccountActivity.class, getApplicationContext());
                            rns.synchReNew();
                        } else {
                            pDialog.hide();
                        }
                    }
                    pDialog.dismiss();
                } catch (Exception ex) {
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
                Log.d(TAG, "getParams: " + BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("iOptions", "0");
                params.put("iPlanID", PlanID);
                params.put("lg",BTApplication.getInstance().getPrefManager().getUser().getLang());
                return params;
            }

            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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
        BTApplication.getInstance().addToRequestQueue(request, TAG);
    }


}
