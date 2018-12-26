package mobile.bts.com.viefund.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mobile.bts.com.viefund.ConnSqlite.DatabaseHelper;
import mobile.bts.com.viefund.ConnSqlite.OrmLiteBaseActivity;
import mobile.bts.com.viefund.Entities.PlanAsset;
import mobile.bts.com.viefund.Entities.PlanInfo;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;
import mobile.bts.com.viefund.R;

public class PlanAssetActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    public static String TAG = PlanAssetActivity.class.getSimpleName();
    private String PlanID ="";

    //sqliteconn
    private DatabaseHelper mDB; // Helper for connect sqlite
    private Dao<PlanAsset, String> _PlanAssetDao = null; // param dao for connet entities PlanAccount

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_asset);

        Bundle b = getIntent().getExtras();
        PlanID = b != null ? b.getString("PlanID") : "";
        mDB = new DatabaseHelper(this);
        //open dao
        try {
            _PlanAssetDao = getHelper().getPlanAssetDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<PlanAsset> obj2 = new ArrayList<PlanAsset>();

        obj2 = PlanAsset.Load_Plan_Asset_By_PlanID(_PlanAssetDao,PlanID);
        ImageView imageView = findViewById(R.id.app_bar_home);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }
}
