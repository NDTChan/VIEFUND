package mobile.bts.com.viefund.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.thebluealliance.spectrum.SpectrumPalette;

import mobile.bts.com.viefund.ConnSqlite.SQLiteHandler;
import mobile.bts.com.viefund.CustomColor.BaseActivity;
import mobile.bts.com.viefund.CustomColor.Utility;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;
import mobile.bts.com.viefund.R;

public class ThemeActivity extends BaseActivity implements SpectrumPalette.OnColorSelectedListener {
    private Toolbar toolbar;
    private static final String TABLE_COLOR = "Custom_Color";
    private static final String TABLE_ID = "Color_Id";
    private static final String COLUMN_COLOR_CONTENT ="Color_Content";
    private static final String SHARED_PREFERENCES_NAME="Color";
    private static final String EDIT_PREFERENCES = "VALUE";
    SQLiteHandler mDB;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        toolbar = (Toolbar) findViewById(R.id.toolbarTheme);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tw = toolbar.findViewById(R.id.app_bar_title) ;
        tw.setText(R.string.nav_change_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ChangeThemeToolbar(toolbar,null,tw);
        ChangeBackground();
        SpectrumPalette spectrumPalette = (SpectrumPalette) findViewById(R.id.palette);
        spectrumPalette.setOnColorSelectedListener(this);
 //       mDB=new SQLiteHandler(getApplicationContext());
//        Log.d("AAA",mDB.getColor());

    }
    @Override public void onColorSelected(@ColorInt int color) {
       // Log.d("BBB", String.valueOf(color));

       String trans=Integer.toHexString(color).toUpperCase();
      // trans.re
       String replace="#"+trans;
     //  Log.d("DDD",color+"=="+getResources().getColor(R.color.colorPrimary));

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EDIT_PREFERENCES,replace);
        editor.commit();
        /// Toast.makeText(this, "Color selected: " + replace, Toast.LENGTH_SHORT).show();
//       if(color==getResources().getColor(R.color.colorPrimary)){
//           Utility.setTheme(getApplicationContext(), 1);
//           Log.d("CCC","if");
//           refeshLayout();
//       }
//       else if(color==getResources().getColor(R.color.md_red_500)){
//            Utility.setTheme(getApplicationContext(), 2);
//           Log.d("CCC","else");
//           refeshLayout();
//        }
  //      startActivity(new Intent(getApplicationContext(),getIntent()));
 //       setChangeThemeAll(trans);
//       setChangeThemeAll(trans);
//       finish();
//       startActivity(getIntent());
        refeshLayout();
    }
    public void setChangeThemeAll( String trans)
    {
        mDB = new SQLiteHandler(getApplicationContext());
        mDB.UpdateColor(trans);
        refeshLayout();

    }
    private void refeshLayout() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);

    }
    public void recreateActivity() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
