package mobile.bts.com.viefund.Authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import mobile.bts.com.viefund.Model.UserModel;

/**
 * Created by DQVu on 7/26/2016.
 */
public class SessionManager {
    private String TAG = SessionManager.class.getSimpleName();

    //shared preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AFCVN";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_NOTIFICATIONS = "notifications";
    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_LAST_USER_ID = "last_user_id";
    private static final String KEY_IS_QA = "is_qa";

    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_TOKEN = "token";
    private static final String KEY_USER_LANGUAGE="user_language";
    private static final String KEY_USER_LAST_TOKEN = "last_token";
    private static final String KEY_USER_LAST_LANGUAGE="last_user_language";
    private static final String KEY_LAST_IS_QA = "last_is_qa";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }


    //set current User after Login Success
    public void setLogin(boolean isLoggedIn)
    {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }
    //set current User after Login Success
    public void reNewSession(String token)
    {
        editor.putString(KEY_USER_TOKEN, token);
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }
    //set current User after Login Success
    public void reNewLanguage(String lg)
    {
        editor.putString(KEY_USER_LANGUAGE, lg);
        editor.commit();

        Log.d(TAG, "User login language modified!");
    }

    // save current User after Login Success
    public void storeUser(UserModel user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_TOKEN, user.getToken());
        editor.putString(KEY_USER_LANGUAGE,user.getLang());
        editor.commit();

        Log.e(TAG, "User is stored in shared preferences. " + user.getName() + ", " + user.getLang());
    }

    public void setQA(String isQA) {
        editor.putString(KEY_IS_QA, isQA);
        editor.commit();
    }


    // get current User after Login Success
    public UserModel getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, name, email,token,lang;
            id = pref.getString(KEY_USER_ID, null);
            name = pref.getString(KEY_USER_NAME, null);
            email = pref.getString(KEY_USER_EMAIL, null);
            token = pref.getString(KEY_USER_TOKEN, null);
            lang=pref.getString(KEY_USER_LANGUAGE,null);
            UserModel user = new UserModel(id, name, email);
            user.setLang(lang);
            user.setToken(token);
            return user;
        }
        return null;
    }

    public UserModel getLastUser() {
        if (pref.getString(KEY_LAST_USER_ID, null) != null) {
            String token,lang,lastIsQA;
            token = pref.getString(KEY_USER_LAST_TOKEN, null);
            lang=pref.getString(KEY_USER_LAST_LANGUAGE,null);
            lastIsQA=pref.getString(KEY_LAST_IS_QA,"F");

            UserModel user = new UserModel();
            user.setLang(lang);
            user.setToken(token);
            user.setStatus(lastIsQA);
            return user;
        }
        return null;
    }


    // check current User is Login
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }


    //add notification
    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }


    public void clear() {
        String lastUID = "";
        String lastTkn = "";
        String lastLng = "";
        String lastIsQA = "";

        lastUID = pref.getString(KEY_USER_ID,null);
        lastTkn = pref.getString(KEY_USER_TOKEN, null);
        lastLng = pref.getString(KEY_USER_LANGUAGE,null);
        lastIsQA = pref.getString(KEY_IS_QA,"F");

        Log.d(TAG, "clear: "+lastTkn);
        editor.clear().apply();
        editor.commit();
        editor.putString(KEY_LAST_USER_ID, lastUID);
        editor.putString(KEY_USER_LAST_TOKEN, lastTkn);
        editor.putString(KEY_USER_LAST_LANGUAGE,lastLng);
        editor.putString(KEY_LAST_IS_QA,lastIsQA);

        editor.commit();
        Log.d(TAG, "clear pref login: done");
    }
    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }
}
