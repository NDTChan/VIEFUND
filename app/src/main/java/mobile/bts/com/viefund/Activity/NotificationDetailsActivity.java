package mobile.bts.com.viefund.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import org.w3c.dom.Text;

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

public class NotificationDetailsActivity extends CustomColor {
    public static String TAG = NotificationDetailsActivity.class.getSimpleName();
    public static final String url = UrlModel.url_Notification_Info;

    public TextView _twSubjectNoticeInfo,_twFomeNoticeInfo,_twToNoticeinfo,_twDateNoticeInfo;
    public WebView _wvContentNoticeInfo;
    public ImageView _iwReplyInfo;
    private Toolbar toolbar;
    public String iMsgID="";
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);
        toolbar = (Toolbar) findViewById(R.id.toolbarNotificationInfo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tw = (TextView) toolbar.findViewById(R.id.app_bar_title);
        tw.setText(getString(R.string.la_mess_noti));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //get PlanId from bundle
        Bundle b = getIntent().getExtras();
        if (b != null) {
            iMsgID = b.getString("iMsgID");
        }

        _twSubjectNoticeInfo = (TextView) findViewById(R.id.twSubjectNoticeInfo);
        _twFomeNoticeInfo = (TextView) findViewById(R.id.twFomeNoticeInfo);
        _twToNoticeinfo = (TextView) findViewById(R.id.twToNoticeinfo);
        _twDateNoticeInfo = (TextView) findViewById(R.id.twDateNoticeInfo);
        _wvContentNoticeInfo = (WebView) findViewById(R.id.wvContentNoticeInfo);
        _iwReplyInfo = (ImageView) findViewById(R.id.iwReplyInfo);
        _wvContentNoticeInfo.getSettings().setJavaScriptEnabled(true);

        ChangeThemeToolbar(toolbar,null,(TextView)findViewById(R.id.app_bar_title));
        ChangeBackground();
        SynchData();
        ImageView imageView = findViewById(R.id.app_bar_home);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }


    public void SynchData(){
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse StringRequest: "+response);
                try {
                    final NotificationModel data = new NotificationModel();
                    JSONObject jObj = new JSONObject(response);
                    int result = Integer.parseInt(jObj.getString("RtnCode"));
                    if (result == 0) {
                        JSONObject jOnjNotification = jObj.getJSONObject("NotificationList");
                        JSONArray lstNotification = jOnjNotification.getJSONArray("List");
                        if(lstNotification.length()>0){
                                NotificationModel obj = new NotificationModel();
                                JSONObject objJson = lstNotification.getJSONObject(0);
                                data.setId(objJson.getString("ID"));
                                data.setDateSend(objJson.getString("Date"));
                                data.setSenderID(objJson.getString("SenderID"));
                                data.setFrom(objJson.getString("From"));
                                data.setSubject(objJson.getString("Subject"));
                                data.setContent(objJson.getString("Content"));
                        }
                        _twSubjectNoticeInfo.setText(data.getSubject());
                        _twFomeNoticeInfo.setText(getString(R.string.la_mess_from)+": "+data.getFrom());
                        _twToNoticeinfo.setText(getString(R.string.la_mess_to)+": Me");
                        _twDateNoticeInfo.setText(getString(R.string.la_mess_time)+" :"+data.getDateSend());
                        Log.d(TAG, "onResponse getContent: "+data.getContent());
                        _wvContentNoticeInfo.loadDataWithBaseURL(null,"<style type=\"text/css\">figure{height: auto; width: 100% !importaint;} figure img{ height: auto; width: 100% !importaint;} img{display: inline; height: auto; max-width: 100%;}</style>"+data.getContent()+"","text/html", "utf-8",null);
                        _iwReplyInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(),ReplyNotificationActivity.class);
                                Bundle b = new Bundle();
                                b.putString("dataSubject",data.getSubject());
                                b.putString("dataFrom",data.getFrom());
                                b.putString("TokenTr",BTApplication.getInstance().getPrefManager().getUser().getToken());
                                intent.putExtra("dataNotification",b);
                                startActivity(intent);
                            }
                        });

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
                params.put("iMsgID", iMsgID);
                params.put("lg",BTApplication.getInstance().getPrefManager().getUser().getLang());
                return params;
            }

            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("iMsgID", iMsgID);
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
