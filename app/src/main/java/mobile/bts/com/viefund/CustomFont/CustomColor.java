package mobile.bts.com.viefund.CustomFont;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

import mobile.bts.com.viefund.Activity.AboutUsActivity;
import mobile.bts.com.viefund.Activity.TermsOfUseActivity;
import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.ConnSqlite.DatabaseHelper;
import mobile.bts.com.viefund.ConnSqlite.OrmLiteBaseActivity;
import mobile.bts.com.viefund.CustomConnectApi.RenewSession;
import mobile.bts.com.viefund.Model.UrlModel;
import mobile.bts.com.viefund.R;

/**
 * Created by THANH on 4/14/2018.
 */

public class CustomColor extends OrmLiteBaseActivity<DatabaseHelper> {
    private  String SHARED_PREFERENCES_NAME="Color";
    private  String EDIT_PREFERENCES = "VALUE";
    public static String trans;
    public Context mContext;
    public String StartupColor = "#31844c";
    public static String TAG = CustomColor.class.getSimpleName();
    public static final String url = UrlModel.url_setting;
    public void PreferenceClass (Context context){
        mContext = context;
         SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, context.MODE_PRIVATE);
        // getData();
           trans = sharedPreferences.getString(EDIT_PREFERENCES,StartupColor);
    }
    public void PreferenceClassMainActivity (Context context,String StartupColor){
        mContext = context;
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, context.MODE_PRIVATE);
        // getData();
        trans = sharedPreferences.getString(EDIT_PREFERENCES,StartupColor);
    }
    public static Drawable setTint(Drawable d, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(d);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }
    //"#31844c"
    public void ChangeThemeToolbar(Toolbar toolbar, ImageView imageView, TextView textView)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, mContext.MODE_PRIVATE);
        //getData();
        trans = sharedPreferences.getString(EDIT_PREFERENCES,StartupColor);
        toolbar.setBackgroundColor(Color.parseColor(trans));
        if(imageView!=null) imageView.setBackgroundColor(Color.parseColor(trans));
        if(textView!=null) textView.setBackgroundColor(Color.parseColor(trans));
    }
    public void ChangeThemeToolbarMainActivity(Toolbar toolbar, ImageView imageView, TextView textView,  String StartupColor)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, mContext.MODE_PRIVATE);
        //getData();
        trans = sharedPreferences.getString(EDIT_PREFERENCES,StartupColor);
        toolbar.setBackgroundColor(Color.parseColor(trans));
        if(imageView!=null) imageView.setBackgroundColor(Color.parseColor(trans));
        if(textView!=null) textView.setBackgroundColor(Color.parseColor(trans));
    }
    public void ChangeBackground()
    {
        TypedArray ta = this.getResources().obtainTypedArray(R.array.demo_colors);
        int[] colors = new int[ta.length()];

        int a = 0;
        for(int i=0;i<ta.length();i++)
        {
            colors[i]=ta.getColor(i,0);
            if(colors[i]==Color.parseColor(trans))
            {
                a=colors[i];
            }
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(a);
    }


}
