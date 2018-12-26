package mobile.bts.com.viefund.Application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;


import java.util.List;

import mobile.bts.com.viefund.Activity.FirstActivity;
import mobile.bts.com.viefund.Activity.LoginActivity;
import mobile.bts.com.viefund.Activity.QuickAccess;
import mobile.bts.com.viefund.Authentication.SessionManager;
import mobile.bts.com.viefund.ConnSqlite.SQLiteHandler;
import mobile.bts.com.viefund.CustomColor.Utility;
import mobile.bts.com.viefund.MethodSupport.StyleLoader;
import mobile.bts.com.viefund.Model.Language;
import mobile.bts.com.viefund.Model.UserModel;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;
import mobile.bts.com.viefund.R;
import mobile.bts.com.viefund.Service.MyService;

/**
 * Created by VUDQ on 3/8/2018.
 * APPlication Custom for Session login
 */
public class BTApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static Context context;
    public static final String TAG = BTApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static BTApplication mInstance;
    private SQLiteHandler db;
    private int userId;
    private SessionManager pref;
    private Gson mGSon;
    private StyleLoader styleLoader ;

    //@Override
    //protected void attachBaseContext(Context base) {
    //    super.attachBaseContext(LanguageUtils.onAttach(base));
    //}

    @Override
    public void onTerminate() {
        super.onTerminate();
        Intent serviceIntent = new Intent(this, MyService.class);
        stopService(serviceIntent);
        Log.d(TAG, "onTerminate() returned: ");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = new SQLiteHandler(getApplicationContext());
        mInstance = this;
        mGSon = new Gson();
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);

    }

    public static boolean isApplicationSentToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    /**
     *VUDQ
     *09/03/2018
     *get current instance
     * **/
    public static synchronized BTApplication getInstance() {
        return mInstance;
    }

    /**
     *VUDQ
     *09/03/2018
     *get queue by request
     * **/
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    /**
     *VUDQ
     *09/03/2018
     *get current PrefManager
     * **/
    public SessionManager getPrefManager() {
        if (pref == null) {
            pref = new SessionManager(this);
        }

        return pref;
    }

    /**
     *VUDQ
     *09/03/2018
     *add queue to request call api
     * **/
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }


    /**
     *VUDQ
     *09/03/2018
     *camcle queue to request call api
     * **/
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    //function login
    public void logout(SQLiteHandler conn) {
        //Log.d(TAG, "logout: "+pref.getUser().getName());
        UserModel obj = new UserModel();
        obj = conn.getUserByUserName(pref.getUser().getName());
        Log.d(TAG, "logout: "+obj.getName());
        conn.setDefaulAllUser();
        conn.setLoginUser(obj,db.STATUS_LOGOUT);
        conn.close();
        pref.clear();
        Intent intent = new Intent(this, FirstActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //function login
    public void logoutNoActivity(SQLiteHandler conn) {
        if(pref.getUser()!=null) {
            UserModel obj = new UserModel();
            obj = conn.getUserByUserName(pref.getUser().getName());

            Log.d(TAG, "logout: " + obj.getName());
            conn.setDefaulAllUser();
            conn.setLoginUser(obj, db.STATUS_LOGOUT);
            conn.close();
            pref.clear();
        }
    }

    public Gson getGSon() {
        return mGSon;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d(TAG, "onPause: "+BTApplication.isApplicationSentToBackground(getApplicationContext()));

            if (BTApplication.isApplicationSentToBackground(getApplicationContext())){
                BTApplication.getInstance().logout(db);
                Intent intent2 = new Intent(getApplicationContext(), QuickAccess.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
            }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
