package mobile.bts.com.viefund.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.ConnSqlite.DatabaseHelper;
import mobile.bts.com.viefund.CustomFont.CustomColorFrag;
import mobile.bts.com.viefund.Entities.AccountInfo;
import mobile.bts.com.viefund.Entities.Plan;
import mobile.bts.com.viefund.Model.UrlModel;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;
import mobile.bts.com.viefund.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class AccountInfoFragment extends CustomColorFrag {
    public static final String TAG = AccountInfoFragment.class.getSimpleName();
    private Context mContext;
    private String AccountType="";
    private String AccountID = "";
    private TableLayout mTableLayout;
    public static final String url = UrlModel.url_AccountDetail_List;
    //sqliteconn
    private DatabaseHelper mDB; // Helper for connect sqlite
    private Dao<AccountInfo, String> _AccountInfoDao = null; // param dao for connet entities PlanAccount

    private String fund_accID="";
    private String fund_Des="";
    @Nullable
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account_info, container, false);
        if(this.getArguments() != null)
        {
            AccountID = this.getArguments().getString("AccountID");
            AccountType = this.getArguments().getString("AccountType");
            Log.d(TAG, "onCreateView: AccountID"+AccountID);
        }
        mDB = new DatabaseHelper(getContext());
        try {
            _AccountInfoDao = mDB.getAccountInfoDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mTableLayout = (TableLayout) v.findViewById(R.id.fragTableAccountInfo);
        mTableLayout.removeAllViews();
        SynchroData();
        return v;
    }

    public void LoadTableLayout(List<AccountInfo> obj){
        ArrayList<AccountInfo> data = new ArrayList<>();
        data.addAll(obj);
        int rows = obj.size();
        int leftRowMargin=1;
        int topRowMargin=1;
        int rightRowMargin=1;
        int bottomRowMargin = 1;
        int idTable =0;


        TableRow tableRow = new TableRow(getContext());
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        TextView tv_Label = new TextView(getContext());
        String[] temp = fund_Des.split("\\s");
        String newObjDes = "";
        for(int i=0;i<temp.length;i++){

            if(i==2) newObjDes+=temp[i]+"\n";
            else newObjDes+=temp[i]+" ";
        }
        tv_Label.setText(fund_accID+"\n"+newObjDes);
        tv_Label.setTextColor(Color.WHITE);
        tv_Label.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_Label.setTextSize(15);
        tv_Label.setPadding(40, 15, 0, 15);
        tableRow.addView(tv_Label);

        tableRow.setBackgroundColor(Color.parseColor(trans));

        mTableLayout.addView(tableRow,new TableLayout.LayoutParams(

                TableRow.LayoutParams.MATCH_PARENT,

                TableRow.LayoutParams.WRAP_CONTENT));

        for (int i = 0;i<rows;i++){
            AccountInfo row = obj.get(i);
            final TextView tv = new TextView(getContext());
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.LEFT);
            tv.setPadding(40, 15, 0, 15);
            tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
            tv.setText(String.valueOf(row.getInfoLabel()));
            final LinearLayout layCustomer = new LinearLayout(getContext());
            layCustomer.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,5);
            if (idTable != rows) {
                layCustomer.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
            }
            else{
                layCustomer.setBackgroundColor(Color.parseColor("#f8f8f8"));
            }
            layCustomer.setLayoutParams(layParams);
            layCustomer.setPadding(0, 10, 0, 10);
            layCustomer.addView(tv);

            final TextView tv4 = new TextView(getContext());
            tv4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            tv4.setGravity(Gravity.LEFT);
            tv4.setPadding(5, 15, 8, 15);
            tv4.setBackgroundColor(Color.parseColor("#f8f8f8"));
            tv4.setTextColor(Color.parseColor(trans));
            tv4.setText(String.valueOf(row.getInfoContent()));
            tv4.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            final LinearLayout layCustomer4 = new LinearLayout(getContext());
            layCustomer4.setOrientation(LinearLayout.VERTICAL);
            layCustomer4.setPadding(0, 10, 0, 10);
            LinearLayout.LayoutParams layParams4 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,5);
            if (idTable != rows) {
                layCustomer4.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
            }
            else{
                layCustomer4.setBackgroundColor(Color.parseColor("#f8f8f8"));
            }
            layCustomer4.setLayoutParams(layParams4);
            layCustomer4.addView(tv4);

            // add table row
            final TableRow tr = new TableRow(getContext());
            tr.setId(idTable);
            TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
            //trParams.setMargins(0, 0, 0, bottomRowMargin);
            tr.setWeightSum(10);
            tr.setPadding(0,0,0,0);
            tr.setLayoutParams(trParams);
            tr.addView(layCustomer);
            tr.addView(layCustomer4);
           // idTable++;
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
        mDB.Delete_Account_Info_All();
        mDB.close();
        RequestQueue mRequest = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse() returned: " + response);
                try {
                    Plan obj = new Plan();
                    List<AccountInfo> obj2 = new ArrayList<AccountInfo>();
                    JSONObject jObj = new JSONObject(response);
                    int result = Integer.parseInt(jObj.getString("RtnCode"));
                    if (result == 0) {
                        JSONObject objAccountDetail = new JSONObject(jObj.getString("AccountDetail"));
                        fund_accID=objAccountDetail.getString("AccountID");
                        fund_Des=objAccountDetail.getString("Description");
                        JSONArray arrayInfoList = objAccountDetail.getJSONArray("InfoList");
                        String planID = "";

                        if (arrayInfoList.length() > 0) {
                            for (int i = 0; i < arrayInfoList.length(); i++) {
                                JSONObject objTemp = arrayInfoList.getJSONObject(i);
                                AccountInfo objEntities = new AccountInfo();
                                objEntities.setAccountID(objAccountDetail.getString("ID"));
                                String  uniqueID = UUID.randomUUID().toString();
                                objEntities.setID(uniqueID.toString());
                                objEntities.setInfoLabel(objTemp.getString("InfoLabel"));
                                objEntities.setInfoContent(objTemp.getString("InfoContent"));
                                _AccountInfoDao.create(objEntities);
                            }
                        }

                        obj2 = AccountInfo.Load_Account_Info_By_AccountID(_AccountInfoDao, AccountID);
                        Log.d(TAG,"AAA"+obj2.size());
                        LoadTableLayout(obj2);
                        pDialog.hide();
                    } else if (result == 1) {
                        Log.d(TAG, "onResponse: "+jObj.getString("ErrorMsg"));
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
