package mobile.bts.com.viefund.Activity;

import android.content.Context;
import android.content.Intent;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.CustomConnectApi.RenewSession;
import mobile.bts.com.viefund.CustomFont.CustomColor;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.Model.UrlModel;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;
import mobile.bts.com.viefund.R;
import mobile.bts.com.viefund.WebViewClient.MyWebViewClient;

public class HelpActivity extends CustomColor {

    private Toolbar toolbar;
    public static final String url = UrlModel.url_setting;
    public static String TAG = HelpActivity.class.getSimpleName();
    private String AboutURL;
    private String PrivacyURL;
    private String DisclosureURL;
    private String TermsOfUseURL;
    private String HelpURL;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        toolbar = findViewById(R.id.toolbarHelp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tw =toolbar.findViewById(R.id.app_bar_title);
        tw.setText(R.string.setting_help);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ChangeThemeToolbar(toolbar,null,tw);
        ChangeBackground();
        ImageView imageView = findViewById(R.id.app_bar_home);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        getData();
    }
    public void getData(){
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jObj = new JSONObject(response);
                    int result= Integer.parseInt(jObj.getString("RtnCode"));
                    if(result==0){
                        JSONObject settingsObject = jObj.getJSONObject("Settings");
                        AboutURL = settingsObject.getString("AboutURL");
                        PrivacyURL = settingsObject.getString("PrivacyURL");
                        DisclosureURL = settingsObject.getString("DisclosureURL");
                        TermsOfUseURL = settingsObject.getString("TermsOfUseURL");
                        HelpURL = settingsObject.getString("HelpURL");
                        setUpWebView();

                    }
                    else if(result==1)
                    {
                        if(jObj.getString("ErrorMsg").startsWith(UrlModel.Msg_DieSession)){
                            RenewSession rns = new RenewSession(AboutUsActivity.class,getApplicationContext());
                            rns.synchReNew();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),
                                    jObj.getString("ErrorMsg"), Toast.LENGTH_LONG).show();

                        }
                    }
                }catch (Exception ex) {
                    Toast.makeText(getApplicationContext(),
                            ex+"", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse() returned: " + ex);
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Lg", BTApplication.getInstance().getPrefManager().getUser().getLang());
                return  params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        BTApplication.getInstance().addToRequestQueue(request,TAG);
    }
    private void setUpWebView(){
        final WebView webView = findViewById(R.id.wvContentHelp);
        webView.setWebViewClient(new MyWebViewClient(HelpURL));
        String url = HelpURL.trim();
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.loadUrl(url);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        //Combo 3 Zoom
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(true);
    }
}
