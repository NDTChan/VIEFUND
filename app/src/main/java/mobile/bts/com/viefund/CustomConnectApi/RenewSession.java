package mobile.bts.com.viefund.CustomConnectApi;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mobile.bts.com.viefund.Activity.QuickAccess;
import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.ConnSqlite.SQLiteHandler;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.Model.UrlModel;

/**
 * BT Company
 * Created by Administrator on 3/20/2018.
 */

public class RenewSession {
    public static String TAG = RenewSession.class.getSimpleName();
    public String url = UrlModel.url_renew_session;
    public  String token ="";
    private SQLiteHandler db;
    public Context mContext;
    public  Class<?> cls;
    public RenewSession(Class<?> cls,Context mContext){
        token = BTApplication.getInstance().getPrefManager().getUser().getToken();
        this.cls = cls;
        this.mContext = mContext;
        db = new SQLiteHandler(mContext);
    }
    public void synchReNew(){
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int result = Integer.parseInt(jObj.getString("RtnCode"));
                    if(result == 0)
                    {
                        JSONObject objLogin = new JSONObject(jObj.getString("Login"));
                        String token = objLogin.getString("Token");
                        BTApplication.getInstance().getPrefManager().reNewSession(token);
                        Intent intent = new Intent(mContext,cls);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                    else
                    {
                        BTApplication.getInstance().logout(db);
                        Intent intent2 = new Intent(mContext, QuickAccess.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent2);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                Log.d(TAG, "getParams: "+BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("TokenStr",BTApplication.getInstance().getPrefManager().getUser().getToken());
                return params;
            }
            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                return params;
            }
        };
        BTApplication.getInstance().addToRequestQueue(request,TAG);
    }
}
