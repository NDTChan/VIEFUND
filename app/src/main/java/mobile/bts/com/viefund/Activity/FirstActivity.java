package mobile.bts.com.viefund.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.ConnSqlite.SQLiteHandler;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;
import mobile.bts.com.viefund.R;

public class FirstActivity extends AppCompatActivity {

    private SQLiteHandler db;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        LanguageUtils.loadLocale(getApplicationContext());
        if (BTApplication.getInstance().getPrefManager().getUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else
        {
            if(BTApplication.getInstance().getPrefManager().getLastUser() != null){
                if(BTApplication.getInstance().getPrefManager().getLastUser().getStatus().equals("T"))
                {
                    startActivity(new Intent(this, QuickAccess.class));
                    finish();
                }
                else {
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }
            }
            else {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        }

    }
}
