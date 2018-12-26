package mobile.bts.com.viefund.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import mobile.bts.com.viefund.Adapter.SettingAdapter;
import mobile.bts.com.viefund.CustomFont.CustomColor;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.Model.SettingModel;
import mobile.bts.com.viefund.MultiLanguage.ChangeLanguageActivity;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;
import mobile.bts.com.viefund.R;
public class SettingActivity extends CustomColor {
    public RecyclerView _rwSetting;
    private Toolbar toolbar;
    public String iMsgID="";
    public String color="";
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toolbar = (Toolbar) findViewById(R.id.toolbarChangeSetting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tw = toolbar.findViewById(R.id.app_bar_title) ;
       // ImageView imageView=toolbar.findViewById(R.id.app_bar_icon);
        tw.setText(R.string.nav_settings);
       // imageView.setImageResource(R.drawable.ic_settings_black_24dp);
//        imageView.setcol
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ArrayList<SettingModel> data = new ArrayList<>();
        data.add(new SettingModel(R.string.changelanguage,R.drawable.ic_language,"#ffffff"));
//        data.add(new SettingModel(
//                R.string.nav_change_theme,R.drawable.ic_invert_colors_black_18dp,trans));
        _rwSetting = (RecyclerView) findViewById(R.id.recycler_view_setting);
        _rwSetting.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        _rwSetting.setAdapter(new SettingAdapter(data,getApplicationContext(),ThemeActivity.class));
        ChangeThemeToolbar(toolbar,null,tw);
        ChangeBackground();
        ImageView imageView = findViewById(R.id.app_bar_home);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        //_rwSetting.setAdapter(new SettingAdapter(data,getApplicationContext(), ChangeLanguageActivity.class));
    }

}
