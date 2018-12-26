package mobile.bts.com.viefund.ConnSqlite;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.j256.ormlite.android.apptools.*;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

/**
 * BT Company
 * Created by Administrator on 3/13/2018.
 */

public abstract class OrmLiteBaseActivity<H extends OrmLiteSqliteOpenHelper> extends AppCompatActivity {
    private volatile H helper;
    private volatile boolean created = false;
    private volatile boolean destroyed = false;

    public OrmLiteBaseActivity() {
    }

    public H getHelper() {
        if(this.helper == null) {
            if(!this.created) {
                throw new IllegalStateException("A call has not been made to onCreate() yet so the helper is null");
            } else if(this.destroyed) {
                throw new IllegalStateException("A call to onDestroy has already been made and the helper cannot be used after that point");
            } else {
                throw new IllegalStateException("Helper is null for some unknown reason");
            }
        } else {
            return this.helper;
        }
    }

    public ConnectionSource getConnectionSource() {
        return this.getHelper().getConnectionSource();
    }

    protected void onCreate(Bundle savedInstanceState) {
        if(this.helper == null) {
            this.helper = this.getHelperInternal(this);
            this.created = true;
        }

        super.onCreate(savedInstanceState);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.releaseHelper(this.helper);
        this.destroyed = true;
    }

    protected H getHelperInternal(Context context) {
        OrmLiteSqliteOpenHelper newHelper = OpenHelperManager.getHelper(context);
        return (H) newHelper;
    }

    protected void releaseHelper(H helper) {
        OpenHelperManager.releaseHelper();
        helper = null;
    }

}
