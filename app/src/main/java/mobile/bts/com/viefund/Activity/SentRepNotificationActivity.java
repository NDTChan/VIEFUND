package mobile.bts.com.viefund.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mobile.bts.com.viefund.Adapter.NotificationAdapter;
import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.CustomConnectApi.RenewSession;
import mobile.bts.com.viefund.CustomFont.CustomColor;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.Model.NotificationModel;
import mobile.bts.com.viefund.Model.UrlModel;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;
import mobile.bts.com.viefund.R;

/**
 * Created by Administrator on 4/3/2018.
 */

public class SentRepNotificationActivity extends CustomColor {
    public static final String url = UrlModel.url_Notification_List;
    public static String TAG = NotificationDetailsActivity.class.getSimpleName();
    private Toolbar toolbar;
    RecyclerView _rwSentRepNotification;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_rep_notification);
        toolbar = (Toolbar) findViewById(R.id.toolbarSentRepNotification);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tw = (TextView) toolbar.findViewById(R.id.app_bar_title);
        tw.setText(getString(R.string.la_mess_sent));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ChangeThemeToolbar(toolbar,null,(TextView)findViewById(R.id.app_bar_title));
        ChangeBackground();
        _rwSentRepNotification = (RecyclerView) findViewById(R.id.rwSentRepNotification);
        _rwSentRepNotification.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ImageView imageView = findViewById(R.id.app_bar_home);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        SynchData();
    }

        public void SynchData(){
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse StringRequest: "+response);
                try {
                    ArrayList<NotificationModel> data = new ArrayList<>();
                    JSONObject jObj = new JSONObject(response);
                    int result = Integer.parseInt(jObj.getString("RtnCode"));
                    if (result == 0) {
                        JSONObject jOnjNotification = jObj.getJSONObject("NotificationList");
                        JSONArray lstNotification = jOnjNotification.getJSONArray("List");
                        if(lstNotification.length()>0){
                            Log.d(TAG,"data not null");
                            for(int i =0;i<lstNotification.length();i++){
                                NotificationModel obj = new NotificationModel();
                                JSONObject objJson = lstNotification.getJSONObject(i);
                                obj.setId(objJson.getString("ID"));
                                obj.setDateSend(objJson.getString("Date"));
                                obj.setFrom(getString(R.string.la_mess_to)+": "+objJson.getString("To"));
                                obj.setSubject(objJson.getString("Subject"));
                                data.add(obj);
                            }
                            _rwSentRepNotification.setAdapter(new NotificationAdapter(data,getApplicationContext(),SentNotificationDetailsActivity.class));
                        }
                    }

                    else if (result == 1){
                        if (jObj.getString("ErrorMsg").startsWith(UrlModel.Msg_DieSession)) {
                            RenewSession rns = new RenewSession(NotificationActivity.class, getApplicationContext());
                            rns.synchReNew();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    jObj.getString("ErrorMsg"), Toast.LENGTH_LONG).show();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error + "", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onErrorResponse() returned: " + error);
            }
        } ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d(TAG, "getParams: " + BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("iOptions", "1");
                params.put("lg",BTApplication.getInstance().getPrefManager().getUser().getLang());
                return params;
            }

            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("iOptions", "1");
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
