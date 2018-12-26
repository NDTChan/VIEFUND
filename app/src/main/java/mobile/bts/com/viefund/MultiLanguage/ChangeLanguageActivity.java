package mobile.bts.com.viefund.MultiLanguage;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.CustomFont.CustomColor;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.Model.Language;
import mobile.bts.com.viefund.Model.UserModel;
import mobile.bts.com.viefund.R;

import static mobile.bts.com.viefund.Application.BTApplication.getInstance;

public class ChangeLanguageActivity extends CustomColor {
    private LanguageAdapter mLanguageAdapter;
    private RecyclerView mRecyclerView;
    private Toolbar toolbar;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        toolbar = (Toolbar) findViewById(R.id.toolbarChangeLanguage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tw = (TextView) toolbar.findViewById(R.id.app_bar_title) ;
        tw.setText(R.string.changelanguage);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mLanguageAdapter = new LanguageAdapter(LanguageUtils.getLanguageData());
        mLanguageAdapter.setListener(new ItemClickListener<Language>() {
            @Override
            public void onClickItem(int position, Language language) {
                if (!language.getCode().equals(LanguageUtils.getCurrentLanguage().getCode())) {
                    onChangeLanguageSuccessfully(language);
                }
            }
        });
        ChangeThemeToolbar(toolbar,null,tw);
        ChangeBackground();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_language) ;
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChangeLanguageActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mLanguageAdapter);

    }

    private void onChangeLanguageSuccessfully(final Language language) {
        mLanguageAdapter.setCurrentLanguage(language);
        LanguageUtils.changeLanguage(language);
        if(language.getCode().equals("en")){
            BTApplication.getInstance().getPrefManager().reNewLanguage("E");
        }else if(language.getCode().equals("fr")){
            BTApplication.getInstance().getPrefManager().reNewLanguage("F");
        }
        refeshLayout();
//        setResult(RESULT_OK, new Intent());
        finish();
    }

    private void refeshLayout() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

}
