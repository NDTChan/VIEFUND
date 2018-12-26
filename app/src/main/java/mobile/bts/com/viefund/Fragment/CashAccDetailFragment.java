package mobile.bts.com.viefund.Fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import mobile.bts.com.viefund.CustomFont.CustomColorFrag;
import mobile.bts.com.viefund.Entities.CashAccDetail;
import mobile.bts.com.viefund.Entities.Plan;
import mobile.bts.com.viefund.Model.UrlModel;
import mobile.bts.com.viefund.R;

/**
 * Created by Administrator on 3/26/2018.
 */

public class CashAccDetailFragment extends CustomColorFrag {
    public static final String TAG = FundPriceFragment.class.getSimpleName();
    private String AccountID = "";
    private String AccountType = "";
    private TableLayout mTableLayout;
    public static final String url = UrlModel.url_AccountDetail_List;

    private String fund_accID="";
    //sqliteconn



    private DatabaseHelper mDB; // Helper for connect sqlite
    private Dao<CashAccDetail, String> _CashAccDetailFragmentDao = null; // param dao for connet entities PlanAccount

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cash_account_details, container, false);
        if (this.getArguments() != null) {
            AccountID = this.getArguments().getString("AccountID");
            AccountType = this.getArguments().getString("AccountType");
            Log.d(TAG, "onCreateView: AccountID" + AccountID);
        }
        Log.d(TAG, "This is account id: " + AccountID);
        mDB = new DatabaseHelper(getContext());
        PreferenceClass(getContext());
        try {
            _CashAccDetailFragmentDao = mDB.getCashAccDetailDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mTableLayout = (TableLayout) v.findViewById(R.id.fragTableCashAccDetails);
        mTableLayout.removeAllViews();
        SynchroData();

        return v;
    }

    public void LoadTableLayout(List<CashAccDetail> obj) {
        ArrayList<CashAccDetail> data = new ArrayList<>();
        data.addAll(obj);
        int rows = obj.size();
        int leftRowMargin = 1;
        int topRowMargin = 1;
        int rightRowMargin = 1;
        int bottomRowMargin = 1;
        int idTable = 0;
        TableRow tableRow = new TableRow(getContext());
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        TextView tv_Label = new TextView(getContext());
        tv_Label.setText(fund_accID);
        tv_Label.setTextColor(Color.WHITE);
        tv_Label.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_Label.setTextSize(15);
        tv_Label.setPadding(40, 15, 0, 15);
        tableRow.addView(tv_Label);

        tableRow.setBackgroundColor(Color.parseColor(trans));
        mTableLayout.addView(tableRow,new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < rows; i++) {
            CashAccDetail rowItem = obj.get(i);

            final TextView tv6 = new TextView(getContext());
            tv6.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tv6.setGravity(Gravity.LEFT);
            tv6.setPadding(8, 15, 0, 15);
            tv6.setBackgroundColor(Color.parseColor("#f8f8f8"));
            tv6.setText(String.valueOf(rowItem.getInfoLabel()));
            final LinearLayout layCustomer6 = new LinearLayout(getContext());
            layCustomer6.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layParams6 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 5);
//            layParams6.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
            layCustomer6.setLayoutParams(layParams6);

            if (idTable != rows) {
                layCustomer6.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
            }
            else{
                layCustomer6.setBackgroundColor(Color.parseColor("#f8f8f8"));
            }
            layCustomer6.setPadding(8, 10, 0, 10);
//            layCustomer6.setBackgroundColor(Color.parseColor("#f8f8f8"));
            layCustomer6.addView(tv6);

            final TextView tv5 = new TextView(getContext());
            tv5.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tv5.setGravity(Gravity.LEFT);
            tv5.setPadding(8, 15, 8, 15);
            tv5.setBackgroundColor(Color.parseColor("#f8f8f8"));
            tv5.setText(rowItem.getInfoContent());
            tv5.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

            final LinearLayout layCustomer5 = new LinearLayout(getContext());
            layCustomer5.setOrientation(LinearLayout.VERTICAL);
            layCustomer5.setPadding(0, 10, 0, 10);
//            layCustomer5.setBackgroundColor(Color.parseColor("#f8f8f8"));
            LinearLayout.LayoutParams layParams5 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 5);
//            tv5.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
            if (idTable != rows) {
                layCustomer5.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
            }
            else{
                layCustomer5.setBackgroundColor(Color.parseColor("#f8f8f8"));
            }
//            layParams5.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
            layCustomer5.setLayoutParams(layParams5);
            layCustomer5.addView(tv5);

            // add table row
            final TableRow tr = new TableRow(getContext());
            tr.setId(idTable);
            TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
//            trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
            tr.setWeightSum(10);
            tr.setPadding(0, 0, 0, 0);
            tr.setLayoutParams(trParams);
            tr.addView(layCustomer6);
            tr.addView(layCustomer5);
            idTable++;
            //add vao table lay out
            mTableLayout.addView(tr, trParams);
            idTable++;
        }
    }

    public void SynchroData() {
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.loading_data));
        pDialog.show();
        pDialog.setCanceledOnTouchOutside(false);
        mDB.open();
        mDB.Delete_Cash_AccDetail_All();
        mDB.close();
        RequestQueue mRequest = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse() returned: " + response);
                try {
                    Plan obj = new Plan();
                    List<CashAccDetail> obj2 = new ArrayList<CashAccDetail>();
                    JSONObject jObj = new JSONObject(response);
                    int result = Integer.parseInt(jObj.getString("RtnCode"));
                    if (result == 0) {
                        JSONObject objAccountDetail = new JSONObject(jObj.getString("AccountDetail"));
                        fund_accID=objAccountDetail.getString("AccountID");
                        JSONArray arrayInfoList = objAccountDetail.getJSONArray("InfoList");
                        String planID = "";

                        if (arrayInfoList.length() > 0) {
                            for (int i = 0; i < arrayInfoList.length(); i++) {
                                JSONObject objTemp = arrayInfoList.getJSONObject(i);
                                CashAccDetail objEntities = new CashAccDetail();
                                objEntities.setAccountID(objAccountDetail.getString("ID"));
                                String uniqueID = UUID.randomUUID().toString();
                                objEntities.setID(uniqueID.toString());
                                objEntities.setInfoLabel(objTemp.getString("InfoLabel"));
                                objEntities.setInfoContent(objTemp.getString("InfoContent"));
                                _CashAccDetailFragmentDao.create(objEntities);
                            }
                        }

                        obj2 = CashAccDetail.Load_Cash_Account_Detail_By_AccountID(_CashAccDetailFragmentDao, AccountID);
                        LoadTableLayout(obj2);
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
                params.put("iOptions", "1");
                params.put("iAccountType", AccountType);
                params.put("iAccountID", AccountID);
                params.put("lg",BTApplication.getInstance().getPrefManager().getUser().getLang());
                return params;
            }

            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("iOptions", "1");
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
