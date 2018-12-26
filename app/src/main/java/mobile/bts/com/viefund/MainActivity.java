package mobile.bts.com.viefund;

import android.app.ProgressDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.j256.ormlite.dao.Dao;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mobile.bts.com.viefund.Activity.AboutUsActivity;
import mobile.bts.com.viefund.Activity.BarChart;
import mobile.bts.com.viefund.Activity.DisclosureActivity;
import mobile.bts.com.viefund.Activity.HelpActivity;
import mobile.bts.com.viefund.Activity.NotificationActivity;
import mobile.bts.com.viefund.Activity.PlanAccountActivity;
import mobile.bts.com.viefund.Activity.PrivacyActivity;
import mobile.bts.com.viefund.Activity.SettingActivity;
import mobile.bts.com.viefund.Activity.TermsOfUseActivity;
import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.ConnSqlite.DatabaseHelper;
import mobile.bts.com.viefund.ConnSqlite.SQLiteHandler;
import mobile.bts.com.viefund.CustomConnectApi.RenewSession;
import mobile.bts.com.viefund.CustomFont.CustomColor;
import mobile.bts.com.viefund.Entities.Plan;
import mobile.bts.com.viefund.Fragment.HomeFragment;
import mobile.bts.com.viefund.Model.Constants;
import mobile.bts.com.viefund.Model.PlanDetail;
import mobile.bts.com.viefund.Model.PlanModel;
import mobile.bts.com.viefund.Model.UrlModel;
import mobile.bts.com.viefund.Model.UserModel;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;


/**
 * Created by DQVu on 8/28/2016.
 */
public class MainActivity extends CustomColor implements ComponentCallbacks2 {
    public static String TAG = MainActivity.class.getSimpleName();
    public static final String url = UrlModel.url_Portfolio_Plan_List;
    public static final String urlQuickAccessSet = UrlModel.url_Quick_Access_Set;
    public static final String urlQuickAccessList = UrlModel.url_Quick_Access_List;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite, twHome;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TableLayout mTableLayout;
    private static final String SHARED_PREFERENCES_NAME = "Color";
    private static final String EDIT_PREFERENCES = "VALUE";
    private RelativeLayout.LayoutParams relativeLayoutParamsRight;
    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;
    public String currentUserName = "";
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    private int countGreen = 0;
    private int countWhite = 0;
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private String objPort;
    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    //sqliteconn
    private SQLiteHandler db;
    private DatabaseHelper mDB;
    private Dao<Plan, String> _PlanDao = null;
    private NumberFormat formater = new DecimalFormat("#,###.##");

    private int width;
    private TableLayout row;

    private String lang = BTApplication.getInstance().getPrefManager().getUser().getLang();
    ArrayList<String> plan_group_total = new ArrayList<>();

    private boolean isChecked2 = false;
    public Switch countTextView;
    private ImageView imageView;
    private ImageView imageHeader;
    private LinearLayout headerLayout;
   // private String StartupColor;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: lang: " + lang);
        StartupColor();
        PreferenceClass(this);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        UserModel currentUser = BTApplication.getInstance().getPrefManager().getUser();
        currentUserName = currentUser.getName();
        db = new SQLiteHandler(getApplicationContext());
        mHandler = new Handler();
        mDB = new DatabaseHelper(this);


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        row = findViewById(R.id.row_bottom);

        navHeader = navigationView.getHeaderView(0);
        headerLayout = navHeader.findViewById(R.id.view_container);
        imageHeader = navHeader.findViewById(R.id.imageVie);
//        txtWebsite = navHeader.findViewById(R.id.website);
//        imgNavHeaderBg = navHeader.findViewById(R.id.img_header_bg);

        for (int i = 1; i < currentUserName.length(); i++) {
            if (i == 4) {
                break;
            }
            Log.d(TAG, "onCreate: " + currentUserName.charAt(i));
            // currentUserName.replace(currentUserName.charAt(i),'*');
            currentUserName = currentUserName.substring(0, i) + '*' + currentUserName.substring(i + 1);

        }
        //twCurrentUserName.setText(currentUserName);
        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        imageView = findViewById(R.id.app_bar_icon);
        imageView.setImageResource(R.drawable.logo1);


        ChangeThemeToolbar(toolbar, imageView, null);
        ChangeBackground();
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        try {
            _PlanDao = getHelper().getPlanDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mTableLayout = findViewById(R.id.tablePlan);
        mTableLayout.removeAllViews();
        if (isOnline()) {
            SynchroData(mTableLayout);
        } else {
        }

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public void StartupColor(){
        StringRequest request = new StringRequest(Request.Method.POST, UrlModel.url_setting, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jObj = new JSONObject(response);
                    int result= Integer.parseInt(jObj.getString("RtnCode"));
                    if(result==0){
                        JSONObject settingsObject = jObj.getJSONObject("Settings");
                        StartupColor = settingsObject.getString("StartupColor");
                        Log.d(TAG, "onResponse:StartupColor : "+StartupColor);
//                        PreferenceClass(MainActivity.this);
//                        ChangeThemeToolbar(toolbar, imageView, null);
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(EDIT_PREFERENCES,StartupColor);
                        editor.commit();

                    }
                    else if(result==1)
                    {
                        if(jObj.getString("ErrorMsg").startsWith(UrlModel.Msg_DieSession)){
                            RenewSession rns = new RenewSession(AboutUsActivity.class,getApplicationContext());
                            rns.synchReNew();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),
                                    jObj.getString("ErrorMsg"), Toast.LENGTH_LONG).show();

                        }

                    }
                    //refeshLayout();
                }catch (Exception ex) {
                    Toast.makeText(getApplicationContext(),
                            ex+"", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse() returned: " + ex);
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Lg", BTApplication.getInstance().getPrefManager().getUser().getLang());
                return  params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        BTApplication.getInstance().addToRequestQueue(request,TAG);
        //refeshLayout();
    }
    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        if (BTApplication.getInstance().getPrefManager().getUser() != null) {
            headerLayout.setBackgroundColor(Color.parseColor(trans));
            Picasso.get().load(R.drawable.left_menu).into(imageHeader);
        }
    }
    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();
        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            // show or hide the fab button
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
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
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_notifications:
                        // launch new intent instead of loading fragment
                        Intent inten2 = new Intent(MainActivity.this, NotificationActivity.class);
                        startActivity(inten2);
                        return true;
                    case R.id.nav_setting:
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        return true;
                    case R.id.nav_logout:
                        logout();
                        return true;
                    case R.id.Privacy:
                        startActivity(new Intent(MainActivity.this, PrivacyActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.Help:
                        startActivity(new Intent(MainActivity.this, HelpActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.Disclosure:
                        startActivity(new Intent(MainActivity.this, DisclosureActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.TermsOfUse:
                        startActivity(new Intent(MainActivity.this, TermsOfUseActivity.class));
                        drawer.closeDrawers();
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
                loadHomeFragment();
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        final MenuItem alertMenuItem = menu.findItem(R.id.checkable_menu);
        StringRequest request2 = new StringRequest(Request.Method.POST, urlQuickAccessList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                countTextView = (Switch) findViewById(R.id.action_switch);
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.d(TAG, "onResponse: response: list qa: " + response);
                    int result = Integer.parseInt(jObj.getString("RtnCode"));
                    if (result == 0) {
                        countTextView.setChecked(true);
                        BTApplication.getInstance().getPrefManager().setQA("T");
                    } else {
                        countTextView.setChecked(false);
                        BTApplication.getInstance().getPrefManager().setQA("F");
                    }
                    countTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            isChecked2 = isChecked;
                            final ProgressDialog pDialog = new ProgressDialog(getApplicationContext());
                            final String bQuickAccess;
                            if (isChecked) {
                                bQuickAccess = "true";
                            } else {
                                bQuickAccess = "false";
                            }
                            Log.d(TAG, "Token: " + BTApplication.getInstance().getPrefManager().getUser().getToken());
                            Log.d(TAG, "bQuickAccess" + bQuickAccess);
                            StringRequest request = new StringRequest(Request.Method.POST, urlQuickAccessSet, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jObj = new JSONObject(response);
                                        int result = Integer.parseInt(jObj.getString("RtnCode"));
                                        if (result == 0) {
                                            if (isChecked2) {
                                                BTApplication.getInstance().getPrefManager().setQA("T");
                                            } else {
                                                BTApplication.getInstance().getPrefManager().setQA("F");
                                            }
                                        } else {
                                        }
                                    } catch (Exception ex) {
                                        pDialog.dismiss();
                                        pDialog.hide();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    pDialog.hide();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    Log.d(TAG, "getParams: " + BTApplication.getInstance().getPrefManager().getUser().getToken());
                                    params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                                    params.put("bQuickAccess", bQuickAccess);
                                    return params;
                                }

                                @Override
                                protected Map<String, String> getPostParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                                    params.put("bQuickAccess", bQuickAccess);
                                    return params;
                                }
                            };
                            BTApplication.getInstance().addToRequestQueue(request, TAG);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d(TAG, "getParams: " + BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("Lg", BTApplication.getInstance().getPrefManager().getUser().getLang());
                return params;
            }

            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("Lg", BTApplication.getInstance().getPrefManager().getUser().getLang());
                return params;
            }
        };

        BTApplication.getInstance().addToRequestQueue(request2, TAG);

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
//            MenuItem itemSwitch = menu.findItem(R.id.my_switch);
//            itemSwitch.setActionView(R.layout.switch_layout);

        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.checkable_menu:
                isChecked2 = !item.isChecked();
                item.setChecked(isChecked2);
                Toast.makeText(getApplicationContext(), "" + isChecked2, Toast.LENGTH_LONG).show();
                return true;
            default:
                return false;

        }
    }
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem checkable = menu.findItem(R.id.checkable_menu);
//        checkable.setChecked(isChecked);
//        return true;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.RequestCode.CHANGE_LANGUAGE:
                if (resultCode == RESULT_OK) {
                    updateViewByLanguage();
                }
                break;
        }
    }

    private void updateViewByLanguage() {
        refeshLayout();
//        twHome.setText(getString(R.string.nav_home));
    }

    private void refeshLayout() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private void logout() {
        BTApplication.getInstance().logout(db);
    }

    /**
     * This function add the data to the table
     **/
    @NonNull
    private TableRow.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        return layoutParams;
    }

    private TextView getTextViewFirst(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(getApplicationContext());
        tv.setId(id);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(8, 15, 0, 15);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.LEFT);
        return tv;
    }

    private TextView getTextViewFirstCapTitle(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(getApplicationContext());
        tv.setId(id);
        tv.setAllCaps(true);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(8, 15, 0, 15);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.LEFT);
        return tv;
    }

    private TextView getTextViewSecond(int bgColor) {
        TextView tv = new TextView(getApplicationContext());
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.LEFT);
        tv.setPadding(5, 0, 0, 15);
        tv.setBackgroundColor(bgColor);
        return tv;
    }

    private TextView getTextViewThird(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(getApplicationContext());
        tv.setId(id);
        tv.setText(title);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.LEFT);
        tv.setPadding(10, 15, 0, 15);
        tv.setTextColor(color);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        return tv;
    }

    private TextView getTextViewFourth(int id, int image, int bgColor, int textColor) {
        TextView tv = new TextView(getApplicationContext());
        tv.setId(id);
        tv.setCompoundDrawablesWithIntrinsicBounds(image, 0, 0, 0);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setPadding(5, 10, 0, 10);
        tv.setBackgroundColor(bgColor);
        Drawable drawables[] = tv.getCompoundDrawables();
        drawables[0].setColorFilter(new PorterDuffColorFilter(textColor, PorterDuff.Mode.SRC_IN));

        return tv;
    }

    private LinearLayout layoutCustemFirst(TextView tv) {
        final LinearLayout layCustomerTotal = new LinearLayout(this);
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 5);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);
        layCustomerTotal.setBackgroundColor(Color.parseColor(trans));
        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }

    private LinearLayout layoutCustemSecond(TextView tv) {
        final LinearLayout layCustomerTotal = new LinearLayout(this);
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 3);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);

        layCustomerTotal.setBackgroundColor(Color.parseColor(trans));
        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }

    private LinearLayout layoutCustemThird(TextView tv) {
        final LinearLayout layCustomerTotal = new LinearLayout(this);
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);
        layCustomerTotal.setBackgroundColor(Color.parseColor(trans));
        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }

    private LinearLayout layoutCustomFourth(TextView tv) {
        final LinearLayout layCustomerTotal = new LinearLayout(this);
        layCustomerTotal.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1);
        layCustomerTotal.setLayoutParams(layParamsTotal);
        layCustomerTotal.setPadding(0, 2, 0, 0);
        layCustomerTotal.setBackgroundColor(Color.parseColor(trans));
        layCustomerTotal.addView(tv);
        return layCustomerTotal;
    }

    String temp = " ";

    public void addData(ArrayList<PlanModel> obj) {
        int numCompanies = obj.size();
        Log.d(TAG, "addData: " + numCompanies);
        TableLayout.LayoutParams layoutParams = getTblLayoutParams();
        Log.d("Color", trans);
        for (int i = 0; i < numCompanies; i++) {
            TableRow tr = new TableRow(getApplicationContext());
            tr.setLayoutParams(getLayoutParams());
            tr.setBackgroundColor(Color.parseColor(trans));
            tr.addView(layoutCustemFirst(getTextViewFirst(i, obj.get(i).getName(), Color.WHITE, Typeface.NORMAL, Color.parseColor(trans))));
            tr.addView(layoutCustemSecond(getTextViewSecond(Color.parseColor(trans))));
            tr.addView(layoutCustemThird(getTextViewThird(i, plan_group_total.get(i), Color.WHITE, Typeface.BOLD, Color.parseColor(trans))));
            tr.addView(layoutCustomFourth(getTextViewFourth(i + numCompanies, R.drawable.ic_keyboard_arrow_right_green_700_24dp, Color.parseColor(trans), Color.parseColor(trans))));

            mTableLayout.addView(tr, getTblLayoutParams());
            Log.d(TAG, "addData obj.get(i).DataDetails.size() : " + obj.get(i).DataDetails.size());
            for (int j = 0; j < obj.get(i).DataDetails.size(); j++) {
                PlanDetail detail = new PlanDetail();
                detail = obj.get(i).DataDetails.get(j);
                TableRow tr2 = new TableRow(getApplicationContext());
                TableLayout.LayoutParams trParamsTotal = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
                tr2.setWeightSum(10);
                tr2.setPadding(0, 0, 0, 0);
                tr2.setLayoutParams(trParamsTotal);
                ImageView iw;
                final PlanDetail finalDetail = detail;
                String s = obj.get(i).DataDetails.get(j).getOwner();
                SpannableString sb = new SpannableString(s);
                sb.setSpan(new StyleSpan(Typeface.BOLD), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (j % 2 == 0) {
//                    tr2.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
                    TextView viewText = getTextViewThird(i + numCompanies, obj.get(i).DataDetails.get(j).getMKV(), Color.parseColor(trans), Typeface.NORMAL, Color.parseColor("#f8f8f8"));
                    TextView textView1 = getTextViewFourth(i + numCompanies, R.drawable.ic_keyboard_arrow_right_green_700_24dp, Color.parseColor("#f8f8f8"), Color.parseColor(trans));
                    tr2.addView(layoutCustemFirst(getTextViewFirst(i, sb + " \n" + obj.get(i).DataDetails.get(j).getDescription(), Color.BLACK, Typeface.NORMAL, Color.parseColor("#f8f8f8"))));
                    tr2.addView(layoutCustemSecond(getTextViewSecond(Color.parseColor("#f8f8f8"))));
                    tr2.addView(layoutCustemThird(viewText));
                    tr2.addView(layoutCustomFourth(textView1));
                    tr2.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
                    viewText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, PlanAccountActivity.class);
                            Bundle b = new Bundle();
                            b.putString("PlanID", finalDetail.getPlanID()); //Your id
                            intent.putExtras(b); //Put your id to your next Intent
                            startActivity(intent);
                        }
                    });
                    textView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, PlanAccountActivity.class);
                            Bundle b = new Bundle();
                            b.putString("PlanID", finalDetail.getPlanID()); //Your id
                            intent.putExtras(b); //Put your id to your next Intent
                            startActivity(intent);
                        }
                    });
                } else {
                    TextView textView1 = getTextViewFourth(i + numCompanies, R.drawable.ic_keyboard_arrow_right_green_700_24dp, Color.parseColor("#f8f8f8"), Color.parseColor(trans));
                    TextView viewText = getTextViewThird(i + numCompanies, obj.get(i).DataDetails.get(j).getMKV(), Color.parseColor(trans), Typeface.NORMAL, Color.parseColor("#f8f8f8"));
                    temp = obj.get(i).DataDetails.get(j).getDescription();
                    tr2.addView(layoutCustemFirst(getTextViewFirst(i, sb + " \n" + obj.get(i).DataDetails.get(j).getDescription(), Color.BLACK, Typeface.NORMAL, Color.parseColor("#f8f8f8"))));
                    tr2.addView(layoutCustemSecond(getTextViewSecond(Color.parseColor("#f8f8f8"))));
                    tr2.addView(layoutCustemThird(viewText));
                    tr2.addView(layoutCustomFourth(textView1));
                    tr2.setBackground(getResources().getDrawable(R.drawable.only_border_bottom_shape));
                    viewText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, PlanAccountActivity.class);
                            Bundle b = new Bundle();
                            b.putString("PlanID", finalDetail.getPlanID()); //Your id
                            intent.putExtras(b); //Put your id to your next Intent
                            startActivity(intent);
                        }
                    });
                    textView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, PlanAccountActivity.class);
                            Bundle b = new Bundle();
                            b.putString("PlanID", finalDetail.getPlanID()); //Your id
                            intent.putExtras(b); //Put your id to your next Intent
                            startActivity(intent);
                        }
                    });
                }
                mTableLayout.addView(tr2, getTblLayoutParams());
            }
        }


        TableRow tr3 = new TableRow(getApplicationContext());

        TableLayout.LayoutParams trParamsTotal = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
        tr3.setLayoutParams(trParamsTotal);
        TextView textView = getTextViewThird(numCompanies + 2, objPort, Color.WHITE, Typeface.NORMAL, Color.parseColor(trans));
        TextView textView1 = getTextViewFourth(numCompanies + 2, R.drawable.bar, Color.parseColor(trans), Color.WHITE);
        tr3.setBackgroundColor(Color.parseColor(trans));
        tr3.addView(layoutCustemFirst(getTextViewFirstCapTitle(numCompanies++, "Total", Color.WHITE, Typeface.BOLD, Color.parseColor(trans))));
        tr3.addView(layoutCustemSecond(getTextViewSecond(Color.parseColor(trans))));
        tr3.addView(layoutCustemThird(textView));
        tr3.addView(layoutCustomFourth(textView1));
        row.addView(tr3, getTblLayoutParams());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BarChart.class);
                startActivity(intent);
            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BarChart.class);
                startActivity(intent);
            }
        });

    }

    public void SynchroData(final TableLayout a) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loading_data));
        pDialog.show();
        pDialog.setCanceledOnTouchOutside(false);
        //BTApplication.getInstance().getPrefManager().reNewSession("20CABD61EE7DB5D2094B678E9A92C8003C63C3A4BDA68B2D45035BBDB3009F0BA560AD9D27C3F20C2E370510216A6C87F2B6613F8F3D4B4971F6BAC629F7B3273F21386A39D7DBAF");
        mDB.open();
        mDB.Delete_Plan_All();

//        mDB.close();
//        RequestQueue mRequest = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse() returned: " + response);
                try {
                    ArrayList<PlanModel> obj = new ArrayList<PlanModel>();
                    List<Plan> obj2 = new ArrayList<Plan>();
                    JSONObject jObj = new JSONObject(response);
                    int result = Integer.parseInt(jObj.getString("RtnCode"));
                    if (result == 0) {
                        JSONObject objLogin = new JSONObject(jObj.getString("Portfolio"));
                        objPort = objLogin.getString("PortfolioTotal");
                        JSONArray arrList = objLogin.getJSONArray("PlanGroupList");
                        if (arrList.length() > 0) {
                            Log.d(TAG, "onResponse: arrList.length(): " + arrList.length());
                            for (int i = 0; i < arrList.length(); i++) {
                                countGreen++;
                                Log.d(TAG, "onResponse: PlanGroupList: " + i);
                                JSONObject objTemp = arrList.getJSONObject(i);
                                JSONArray arrayChild = objTemp.getJSONArray("PlanList");
                                plan_group_total.add(objTemp.getString("PlanGroupTotal"));
                                if (arrayChild.length() > 0) {
                                    Log.d(TAG, "onResponse: " + i + " PlanList: " + arrayChild.length());
                                    for (int j = 0; j < arrayChild.length(); j++) {
                                        countWhite++;
                                        Log.d(TAG, "onResponse: PlanList: " + j);

                                        JSONObject objTemp2 = arrayChild.getJSONObject(j);
                                        Plan objEntities = new Plan();
                                        objEntities.setPlanType(objTemp.getString("PlanType"));
                                        objEntities.setPlanID(objTemp2.getString("ID"));
                                        objEntities.setID(objTemp2.getString("ID"));
                                        objEntities.setDescription(objTemp2.getString("Description"));
                                        objEntities.setOwner(objTemp2.getString("Owner"));
                                        objEntities.setMKV(objTemp2.getString("MKV"));
//                                        mDB.Delete_Plan_All_By_Plan_ID(objEntities.getPlanID());
                                        _PlanDao.create(objEntities);
                                    }
                                }
                            }
                        }
                        List<String> lstPlanTypeStr;
                        obj2 = Plan.Load_Plan(_PlanDao);
                        lstPlanTypeStr = new ArrayList<>();
                        if (obj2.size() > 0) {
                            for (int i = 0; i < obj2.size(); i++) {
                                Plan tempObj = obj2.get(i);
                                if (lstPlanTypeStr.size() <= 0) {
                                    lstPlanTypeStr.add(tempObj.getPlanType());
                                } else {
                                    if (lstPlanTypeStr.indexOf(tempObj.getPlanType()) < 0) {
                                        lstPlanTypeStr.add(tempObj.getPlanType());
                                    } else {
                                        continue;
                                    }
                                }
                            }

                            for (int i = 0; i < lstPlanTypeStr.size(); i++) {
                                PlanModel model = new PlanModel();
                                model.setName(lstPlanTypeStr.get(i));
                                double taotalPlan = 0;
                                Log.d(TAG, "onResponse: qua day roi " + i + " : " + obj2.size());

                                for (int j = 0; j < obj2.size(); j++) {
                                    Plan tempObj2 = obj2.get(j);
                                    PlanDetail tempDeial = new PlanDetail();
                                    if (tempObj2.getPlanType().equals(lstPlanTypeStr.get(i))) {
                                        tempDeial.setDescription(tempObj2.getDescription());
                                        tempDeial.setID(tempObj2.getID());
                                        tempDeial.setPlanID(tempObj2.getPlanID());
                                        tempDeial.setMKV((tempObj2.getMKV() + ""));
                                        tempDeial.setOwner(tempObj2.getOwner());
                                        model.DataDetails.add(tempDeial);
                                        model.setPlanGroupTotal(taotalPlan);

                                    }
                                }

                                obj.add(model);
                            }
                        }
                        addData(obj);
                        // LoadTableLayout(obj, a);
                        pDialog.hide();
                    } else if (result == 1) {
                        if (jObj.getString("ErrorMsg").startsWith(UrlModel.Msg_DieSession)) {
                            RenewSession rns = new RenewSession(MainActivity.class, getApplicationContext());
                            rns.synchReNew();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    jObj.getString("ErrorMsg"), Toast.LENGTH_LONG).show();
                            pDialog.hide();
                        }
                    }
                    pDialog.dismiss();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(),
                            ex + "", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse() returned: " + ex);
                    pDialog.dismiss();
                    pDialog.hide();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error + "", Toast.LENGTH_LONG).show();
                pDialog.hide();
                Log.d(TAG, "onErrorResponse() returned: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d(TAG, "getParams: " + BTApplication.getInstance().getPrefManager().getUser().getLang());
                //               Log.d("LangMap",BTApplication.getInstance().getPrefManager().getUser().getLang());
                params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("iOptions", "0");
                params.put("lg", BTApplication.getInstance().getPrefManager().getUser().getLang());

                return params;
            }

            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("Lang", BTApplication.getInstance().getPrefManager().getUser().getLang());
                params.put("TokenStr", BTApplication.getInstance().getPrefManager().getUser().getToken());
                params.put("iOptions", "0");
                params.put("lg", BTApplication.getInstance().getPrefManager().getUser().getLang());
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
