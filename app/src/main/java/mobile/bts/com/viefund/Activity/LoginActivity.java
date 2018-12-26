package mobile.bts.com.viefund.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.ConnSqlite.SQLiteHandler;
import mobile.bts.com.viefund.CustomColor.ColorDatabaseHelper;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.Model.Constants;
import mobile.bts.com.viefund.Model.Language;
import mobile.bts.com.viefund.Model.UrlModel;
import mobile.bts.com.viefund.Model.UserModel;
import mobile.bts.com.viefund.MultiLanguage.ChangeLanguageActivity;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;
import mobile.bts.com.viefund.R;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by VUDQ on 3/8/2018.
 */
public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public String url = UrlModel.url_login;
    public String urlSetting = UrlModel.url_setting;

    //    private String url_changeStatus = UrlModel.url_isonl;
    boolean result;
    EditText mUsername, mPassword;
    private String username,password;
    Button mSubmit;
    public static final String TAG = LoginActivity.class.getSimpleName();
    RequestQueue mRequestQueue;
    TextView mRegister, twQuenMK,twChangeLanguage;
    ImageView imgLogo;
   // Switch switchLogo;
    int user_id;
    private static final int RC_READ_IMEI_PERM = 124;
    private SQLiteHandler db;
    public ColorDatabaseHelper db2222;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LanguageUtils.loadLocale(getApplicationContext());
        //ask permission
        imeiAndContactsTask();
        mUsername = (EditText) findViewById(R.id.usernameET);
        mPassword = (EditText) findViewById(R.id.password_login_ET);
        mSubmit = (Button) findViewById(R.id.Btn_login);

//        switchLogo = findViewById(R.id.switch1);
        imgLogo = (ImageView) findViewById(R.id.logo);
        mSubmit.setText(getString(R.string.login));
        mUsername.setHint(getString(R.string.username));
        mPassword.setHint(getString(R.string.password));

        if (BTApplication.getInstance().getPrefManager().getUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        // create DB
        db = new SQLiteHandler(getApplicationContext());
        UserModel user = new UserModel();
        String username = db.getUserDetailsLogout().get("username");
        Log.d(TAG, "onCreate: username "+username);
       // mDB.close();
        db.close();
        if(username != null)
        {
            mUsername.setText(username);
        }
        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            mUsername.setText(loginPreferences.getString("username", ""));
            mPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
        //using logo switch
//        switchLogo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked==true) {
//                    switchLogo.setTextOn("Logo Off");
//                    switchLogo.setTextColor(Color.RED);
//                    imgLogo.setVisibility(View.INVISIBLE);
//                }
//                else {
//                    switchLogo.setTextOff("Logo On");
//                    switchLogo.setTextColor(Color.parseColor("#31844c"));
//                    imgLogo.setVisibility(View.VISIBLE);
//                }
//            }
//        });
        //click dang nhap
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if (!username.isEmpty() && !password.isEmpty()) {
                    checkBox(username,password);
                    CheckLogin(username, password);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.empty_username)
                            , Toast.LENGTH_LONG).show();
                }
            }
        });

        LoadLogo();

    }
    public void checkBox(String username , String password){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mUsername.getWindowToken(), 0);
        if (saveLoginCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", username);
            loginPrefsEditor.putString("password", password);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }
    }
//    public void onClickCheckBox(View v) {
//        if (v ==mSubmit) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(mUsername.getWindowToken(), 0);
//
//            username = mUsername.getText().toString();
//            password = mPassword.getText().toString();
//
//            if (saveLoginCheckBox.isChecked()) {
//                loginPrefsEditor.putBoolean("saveLogin", true);
//                loginPrefsEditor.putString("username", username);
//                loginPrefsEditor.putString("password", password);
//                loginPrefsEditor.commit();
//            } else {
//                loginPrefsEditor.clear();
//                loginPrefsEditor.commit();
//            }
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.RequestCode.CHANGE_LANGUAGE:
                if (resultCode == RESULT_OK) {
                    refeshLayout();
                }
                break;
        }
    }

    private void refeshLayout(){
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }


    /**
     * Created by VUDQ on 3/8/2018.
     * function check login
     *
     *
     * **/

    public void CheckLogin(final String username, final String pwd) {
        Log.d(TAG, "CheckLogin: "+username+" - "+pwd);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loading_data));
//        pDialog.show();
        pDialog.setCanceledOnTouchOutside(false);
        boolean result = false;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse() returned: " + response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int result = Integer.parseInt(jObj.getString("RtnCode"));
                    if(result==0)
                    {
                        JSONObject objLogin = new JSONObject(jObj.getString("Login"));
                        Log.d(TAG, "onResponse: "+objLogin);
                        String clientID = objLogin.getString("ClientID");
                        String token = objLogin.getString("Token");
                        String expiry = objLogin.getString("Expiry");
                        String lg = objLogin.getString("Lg");
                        Log.d("LANGUAGE",lg);
                        UserModel userTeamp = new UserModel();

                        UserModel username2 = db.getUserByUserName(username);
                        Log.d(TAG, "onResponse: username2: "+username2);
                        if(username2 == null)
                        {
                            userTeamp.setName(username);
                            userTeamp.setId(clientID);
                            userTeamp.setToken(token);
                            userTeamp.setLang(lg);
                            // if sqlite doesn't store user, then add user to sqlite
                            db.setDefaulAllUser();
                            db.addUser(userTeamp);
                            BTApplication.getInstance().getPrefManager().storeUser(userTeamp);
                        }
                        else{
                            username2.setName(username);
                            username2.setId(clientID);
                            username2.setToken(token);
                            username2.setLang(lg);
                            // if sqlite stored user, set status = 1
                            db.setDefaulAllUser();
                            db.setLoginUser(username2,db.STATUS_LOGIN);
                            BTApplication.getInstance().getPrefManager().storeUser(username2);
                        }
                        String lang =lg;
                        if(lang.equals("E")){
                            Log.d(TAG, "onCreate: lang if: "+lang);
                            Language model = new Language(0,getString(R.string.language_english),getString(R.string.language_english_code));
                            LanguageUtils.setLocale(getApplicationContext(),model);
                            LanguageUtils.loadLocale(getApplicationContext());
                        }else if(lang.equals("F")){
                            Log.d(TAG, "onCreate: lang ELSE if: "+lang);
                            Language model = new Language(1,getString(R.string.language_french),getString(R.string.language_french_code));
                            LanguageUtils.setLocale(getApplicationContext(),model);
                            LanguageUtils.loadLocale(getApplicationContext());
                        }

                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        pDialog.hide();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString("ErrorMsg"), Toast.LENGTH_LONG).show();
                        pDialog.hide();
                    }
                    pDialog.dismiss();
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(),
                            ex+"", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse() returned: " + ex);
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
                params.put("LoginID",username);
                params.put("PW", pwd);
                params.put("DeviceInfo", "");
                params.put("SiteID", "");
                return params;
            }
            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("LoginID", username);
                params.put("PW", pwd);
                params.put("DeviceInfo", "");
                params.put("SiteID", "");
                return params;
            }

        };
        BTApplication.getInstance().addToRequestQueue(request,TAG);
//        return result;
    }

    public void LoadLogo (){
        StringRequest request = new StringRequest(Request.Method.POST, urlSetting, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse() returned: " + response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int result = Integer.parseInt(jObj.getString("RtnCode"));
                    if(result==0)
                    {
                        JSONObject jObjData = jObj.getJSONObject("Settings");
                        String imgUrl = jObjData.getString("LogoURL");
                        Log.d(TAG, "onResponse: "+imgUrl);
                        Picasso.get()
                                .load(imgUrl)
                                .fit().centerCrop()
                                .into(imgLogo);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString("ErrorMsg"), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(),
                            ex+"", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse() returned: " + ex);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error+"", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onErrorResponse() returned: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Lg","E");
                return params;
            }

        };
        BTApplication.getInstance().addToRequestQueue(request,TAG);

    }

    /**
     * Created by VUDQ on 3/8/2018.
     * function required Permission
     * **/
    public void imeiAndContactsTask() {
        String[] perms = {Manifest.permission.INTERNET ,Manifest.permission.ACCESS_NETWORK_STATE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Have permissions, do the thing!
//            Toast.makeText(this, "TODO: Location and Contacts things", Toast.LENGTH_LONG).
// ();
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location_contacts),
                    RC_READ_IMEI_PERM, perms);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.

    }



}
