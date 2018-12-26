package mobile.bts.com.viefund.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
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

import mobile.bts.com.viefund.Activity.TrxDetailActivity;
import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.ConnSqlite.DatabaseHelper;
import mobile.bts.com.viefund.CustomFont.CustomColorFrag;
import mobile.bts.com.viefund.Entities.Plan;
import mobile.bts.com.viefund.Entities.TransactionAcount;
import mobile.bts.com.viefund.Model.UrlModel;
import mobile.bts.com.viefund.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class TransactionsFragment extends CustomColorFrag {
    public static final String TAG = TransactionsFragment.class.getSimpleName();
    private Context mContext;
    private String AccountID = "";
    private String AccountType = "";
    private TableLayout mTableLayout;
    public static final String url = UrlModel.url_AccountDetail_List;
    String objAcc;
    String objMkv;
    String objDes;

    TableRow tableRow;
    TextView tv_Label;
    TextView tv_Content;
    TextView tv_func;

    private int width;
    //sqliteconn
    private DatabaseHelper mDB; // Helper for connect sqlite
    private Dao<TransactionAcount, String> _TransactionAcountDao = null; // param dao for connet entities PlanAccount

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
        View v = inflater.inflate(R.layout.fragment_transactions, container, false);
        if (this.getArguments() != null) {
            AccountID = this.getArguments().getString("AccountID");
            AccountType = this.getArguments().getString("AccountType");
            Log.d(TAG, "onCreateView: AccountID" + AccountID);
        }
        PreferenceClass(getContext());
        mDB = new DatabaseHelper(getContext());
        try {
            _TransactionAcountDao = mDB.getTransactionAcountDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mTableLayout = (TableLayout) v.findViewById(R.id.fragTableTransaction);
        mTableLayout.removeAllViews();
//        ChangeBackground();
        SynchroData();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        return v;
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
        TextView tv = new TextView(getContext());
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
        TextView tv = new TextView(getContext());
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
        TextView tv = new TextView(getContext());
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.LEFT);
        tv.setPadding(5, 0, 0, 15);
        tv.setBackgroundColor(bgColor);
        return tv;
    }
    private TextView getTextViewThird(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(getContext());
        tv.setId(id);
        tv.setText(title);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        tv.setPadding(10, 5, 0, 15);
        tv.setTextColor(color);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        return tv;
    }
    private TextView getTextViewFourth(int id, int image,int bgColor,int textColor) {
        TextView tv = new TextView(getContext());
        tv.setId(id);
        tv.setCompoundDrawablesWithIntrinsicBounds(image,0,0,0);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setPadding(5, 10, 0, 10);
        Drawable drawables[] = tv.getCompoundDrawables();
        drawables[0].setColorFilter(new PorterDuffColorFilter(textColor, PorterDuff.Mode.SRC_IN));
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setBackgroundColor(bgColor);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return tv;
    }
    private LinearLayout layoutCustemFirst(TextView tv)
    {
        final LinearLayout layCustomerTotal = new LinearLayout(getContext());
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
        final LinearLayout layCustomerTotal = new LinearLayout(getContext());
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
        final LinearLayout layCustomerTotal = new LinearLayout(getContext());
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);
        layCustomerTotal.setBackgroundColor(Color.parseColor(trans));
        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }
    private LinearLayout layoutCustomFourth(TextView tv){
        final LinearLayout layCustomerTotal = new LinearLayout(getContext());
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);
        layCustomerTotal.setBackgroundColor(Color.parseColor(trans));
        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }

    public void LoadTableLayout(final List<TransactionAcount> obj) {
        ArrayList<TransactionAcount> data = new ArrayList<>();
        data.addAll(obj);
        int rows = obj.size();
        int idTable=0;
        mTableLayout.setBackgroundColor(Color.parseColor(trans));
        tableRow = new TableRow(getContext());
        TableLayout.LayoutParams trParamsTotal = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
        tableRow.setWeightSum(10);
        tableRow.setPadding(0, 0, 0, 0);
        tableRow.setLayoutParams(getLayoutParams());
        tv_Label = new TextView(getContext());
        String newObjDes = "";
        String[] temp = objDes.split("\\s");
        for(int i=0;i<temp.length;i++){

            if(i==2) newObjDes+=temp[i]+"\n";
            else newObjDes+=temp[i]+" ";
        }
        tableRow.addView(layoutCustemFirst(getTextViewFirst(0,objAcc+"\n"+newObjDes,Color.WHITE,Typeface.BOLD,Color.parseColor(trans))));
        tableRow.addView(layoutCustemSecond(getTextViewSecond(Color.parseColor(trans))));
        tableRow.addView(layoutCustemThird(getTextViewThird(0,objMkv,Color.WHITE,Typeface.BOLD,Color.parseColor(trans))));
        tableRow.addView(layoutCustomFourth(getTextViewFourth(0,R.drawable.ic_keyboard_arrow_right_green_700_24dp,Color.parseColor(trans),Color.parseColor(trans))));

        tableRow.setBackgroundColor(Color.parseColor(trans));
        mTableLayout.addView(tableRow, getTblLayoutParams());
        for(int i=0;i<rows;i++)
        {
            final TransactionAcount row = obj.get(i);
            tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            tableRow.setWeightSum(10);
            tableRow.setBackgroundColor(Color.WHITE);
            tableRow.addView(layoutCustemFirst(getTextViewFirst(0,row.getTradeDate()+" \n"+row.getDescription(),Color.BLACK,Typeface.NORMAL,Color.WHITE)));
            tableRow.addView(layoutCustemSecond(getTextViewSecond(Color.WHITE)));
            TextView twNum = getTextViewThird(0,row.getAmount(),Color.parseColor(trans),Typeface.NORMAL,Color.WHITE);
            TextView textView2=getTextViewFourth(0,R.drawable.ic_keyboard_arrow_right_green_700_24dp,Color.WHITE,Color.parseColor(trans));
            tableRow.addView(layoutCustemThird(twNum));
            tableRow.addView(layoutCustomFourth(textView2));
            tableRow.setBackgroundColor(getResources().getColor(R.color.white));
            mTableLayout.addView(tableRow,getTblLayoutParams());
            twNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), TrxDetailActivity.class);
                    Bundle c = new Bundle();
                    c.putString("TransactionID", row.getID());
                    intent.putExtras(c);
                    startActivity(intent);

                }
            });
            textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), TrxDetailActivity.class);
                    Bundle c = new Bundle();
                    c.putString("TransactionID", row.getID());
                    intent.putExtras(c);
                    startActivity(intent);

                }
            });
            tableRow.setId(idTable);
            idTable++;
        }

    }

    public void SynchroData() {
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.loading_data));
        pDialog.show();
        pDialog.setCanceledOnTouchOutside(false);
        mDB.open();
        mDB.Delete_Transaction_Account_All();
        mDB.close();
        RequestQueue mRequest = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse() returned: " + response);
                try {
                    Plan obj = new Plan();
                    List<TransactionAcount> obj2 = new ArrayList<TransactionAcount>();
                    JSONObject jObj = new JSONObject(response);
                    int result = Integer.parseInt(jObj.getString("RtnCode"));
                    if (result == 0) {
                        JSONObject objAccountDetail = new JSONObject(jObj.getString("AccountDetail"));
                         objAcc= objAccountDetail.getString("AccountID");
                         objDes=objAccountDetail.getString("Description");
                         objMkv=objAccountDetail.getString("MKV");
                        JSONArray arrayTrxList = objAccountDetail.getJSONArray("TrxList");
                        String planID = "";

                        if (arrayTrxList.length() > 0) {
                            for (int i = 0; i < arrayTrxList.length(); i++) {
                                JSONObject objTemp = arrayTrxList.getJSONObject(i);
                                TransactionAcount objEntities = new TransactionAcount();
                                objEntities.setAccountID(objAccountDetail.getString("ID"));
                                objEntities.setID(objTemp.getString("ID"));
                                objEntities.setAmount(objTemp.getString("Amount"));
                                objEntities.setPrice(objTemp.getString("Price"));
                                objEntities.setUnit(objTemp.getString("Unit"));
                                objEntities.setDescription(objTemp.getString("Description"));
                                objEntities.setTradeDate(objTemp.getString("TradeDate"));
                                _TransactionAcountDao.create(objEntities);
                            }
                        }

                        obj2 = TransactionAcount.Load_Transaction_By_PlanID(_TransactionAcountDao, AccountID);
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
                params.put("iOptions", "0");
                params.put("iAccountType", AccountType);
                params.put("iAccountID", AccountID);
                params.put("lg",BTApplication.getInstance().getPrefManager().getUser().getLang());
                return params;
            }

            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("iOptions", "0");
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
