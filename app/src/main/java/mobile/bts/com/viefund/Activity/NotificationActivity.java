package mobile.bts.com.viefund.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mobile.bts.com.viefund.Adapter.NotificationAdapter;
import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.CustomConnectApi.RenewSession;
import mobile.bts.com.viefund.CustomFont.CustomColor;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.Model.Constants;
import mobile.bts.com.viefund.Model.NotificationModel;
import mobile.bts.com.viefund.Model.UrlModel;
import mobile.bts.com.viefund.MultiLanguage.ChangeLanguageActivity;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;
import mobile.bts.com.viefund.R;

public class NotificationActivity extends CustomColor {
    public static int navItemIndex = 0;
    public static String TAG = NotificationActivity.class.getSimpleName();
    public static final String url = UrlModel.url_Notification_List;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    private ImageView imageHeader;
    private LinearLayout headerLayout;
    TextView txtName;
    View navHeader;
    public RecyclerView _rwNotification;
    private Toolbar toolbar;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        toolbar = (Toolbar) findViewById(R.id.toolbar_notification);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tw = (TextView) toolbar.findViewById(R.id.app_bar_notification_title);
        tw.setText(getString(R.string.la_mess_noti));
        _rwNotification = (RecyclerView) findViewById(R.id.rwNotification);
        _rwNotification.setLayoutManager(new LinearLayoutManager(getApplicationContext())); // set dạng list. Ở đây dạng dọc

        drawer = (DrawerLayout) findViewById(R.id.drawer_noti_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_noti_view);
        fab = (FloatingActionButton) findViewById(R.id.fab_notification);

        navHeader = navigationView.getHeaderView(0);
        headerLayout = navHeader.findViewById(R.id.view_container);
        imageHeader = navHeader.findViewById(R.id.imageVie);

        headerLayout.setBackgroundColor(Color.parseColor(trans));
        Picasso.get().load(R.drawable.left_menu).into(imageHeader);
//        txtName = (TextView) navHeader.findViewById(R.id.name);
//        txtName.setText(BTApplication.getInstance().getPrefManager().getUser().getName().toString().trim());
       // txtName.setTextColor(Color.parseColor("1321"));
        ChangeThemeToolbar(toolbar,null,tw);
        ChangeBackground();
        setUpNavigationView();
        SynchData();

    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_noti_inbox:
                        Intent intent = new Intent(NotificationActivity.this, NotificationActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.nav_noti_sent:
                        Intent inten2 = new Intent(NotificationActivity.this, SentRepNotificationActivity.class);
                        startActivity(inten2);
                        return true;
                    case R.id.nav_noti_back_home:
                        Intent inten3 = new Intent(NotificationActivity.this, MainActivity.class);
                        startActivity(inten3);
                        return true;
                    case R.id.nav_noti_new_mess:
                        Intent inten4 = new Intent(NotificationActivity.this, NewMessageNotificationActivity.class);
                        startActivity(inten4);
                        return true;
                    default:
                        navItemIndex = 0;
                }
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                return true;
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }
    public void SynchData(){
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse StringRequest: "+response);
                try {
                    ArrayList<NotificationModel> data = new ArrayList<>();
                    JSONObject jObj = new JSONObject(response);
                    int result = Integer.parseInt(jObj.getString("RtnCode"));
                    if (result == 0) {
                        JSONObject jOnjNotification = jObj.getJSONObject("NotificationList");
                        JSONArray lstNotification = jOnjNotification.getJSONArray("List");
                        if(lstNotification.length()>0){
                            for(int i =0;i<lstNotification.length();i++){
                                NotificationModel obj = new NotificationModel();
                                JSONObject objJson = lstNotification.getJSONObject(i);
                                obj.setId(objJson.getString("ID"));
                                obj.setDateSend(objJson.getString("Date"));
                                obj.setSenderID(objJson.getString("SenderID"));
                                obj.setFrom(objJson.getString("From"));
                                obj.setSubject(objJson.getString("Subject"));
                                obj.setContent(objJson.getString("Content"));
                                data.add(obj);
                            }
                            _rwNotification.setAdapter(new NotificationAdapter(data,getApplicationContext(),NotificationDetailsActivity.class));
                        }
                    }

                    else if (result == 1){
                        if (jObj.getString("ErrorMsg").startsWith(UrlModel.Msg_DieSession)) {
                            RenewSession rns = new RenewSession(NotificationActivity.class, getApplicationContext());
                            rns.synchReNew();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    jObj.getString("ErrorMsg"), Toast.LENGTH_LONG).show();
                        }

                    }

                    } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error + "", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onErrorResponse() returned: " + error);
            }
        } ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d(TAG, "getParams: " + BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("iOptions", "2");
                params.put("lg",BTApplication.getInstance().getPrefManager().getUser().getLang());
                return params;
            }

            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("iOptions", "2");
                params.put("lg",BTApplication.getInstance().getPrefManager().getUser().getLang());
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        BTApplication.getInstance().addToRequestQueue(request, TAG);
    }
}
