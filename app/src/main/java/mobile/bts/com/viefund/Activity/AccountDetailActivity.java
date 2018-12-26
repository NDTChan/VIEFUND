package mobile.bts.com.viefund.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobile.bts.com.viefund.ConnSqlite.DatabaseHelper;
import mobile.bts.com.viefund.ConnSqlite.OrmLiteBaseActivity;
import mobile.bts.com.viefund.CustomFont.CustomColor;
import mobile.bts.com.viefund.Entities.CashAccTrx;
import mobile.bts.com.viefund.Fragment.AccountInfoFragment;
import mobile.bts.com.viefund.Fragment.CashAccDetailFragment;
import mobile.bts.com.viefund.Fragment.CashAccTrxFragment;
import mobile.bts.com.viefund.Fragment.FundPerformanceFragment;
import mobile.bts.com.viefund.Fragment.FundPriceFragment;
import mobile.bts.com.viefund.Fragment.GICAccountFragment;
import mobile.bts.com.viefund.Fragment.TransactionsFragment;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.MultiLanguage.LanguageUtils;
import mobile.bts.com.viefund.R;

public class AccountDetailActivity extends CustomColor {
    public static String TAG = AccountDetailActivity.class.getSimpleName();

    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private String AccountID = "";
    private String AccountType ="";
    private  TextView tw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        //set property for toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarAccountDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tw =toolbar.findViewById(R.id.app_bar_title) ;
//        tw.setText(getString(R.string.Account_Details));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
      //  TableLayout tableLayout= (TableLayout) findViewById(R.id.tabsAccountDetail);

        ChangeThemeToolbar(toolbar,null,(TextView)findViewById(R.id.app_bar_title));
        ChangeBackground();
        //get Param from bundle
        Bundle b = getIntent().getExtras();
        if(b!= null){
            AccountID = b.getString("AccountID");
            AccountType = b.getString("AccountType");
        }
        // viewpager
        mViewPager = (ViewPager) findViewById(R.id.pager);
        //set tab
        tabLayout = (TabLayout) findViewById(R.id.tabsAccountDetail);
        tabLayout.setBackgroundColor(Color.parseColor(trans));
        tabLayout.setupWithViewPager(mViewPager);
        setUpViewPager(mViewPager);
        ImageView imageView = findViewById(R.id.app_bar_home);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.onAttach(base));
    }
    private void setUpViewPager(ViewPager viewPager)
    {
        Log.d(TAG, "setUpViewPager: AccountType"+AccountType);
        Bundle bundle = new Bundle();
        String myMessage = "Stackoverflow is cool!";
        bundle.putString("AccountID", AccountID); //Your id
        bundle.putString("AccountType", AccountType); //Your id

        TransactionsFragment fragTran = new TransactionsFragment();
        AccountInfoFragment fragAccount = new AccountInfoFragment();
        FundPriceFragment fragFundPrice = new FundPriceFragment();
        FundPerformanceFragment FundPerformance = new FundPerformanceFragment();
        GICAccountFragment gicAccountFragment = new GICAccountFragment();
        CashAccTrxFragment cashAccTrxFragment = new CashAccTrxFragment();
        CashAccDetailFragment cashAccDetailFragment = new CashAccDetailFragment();

        fragTran.setArguments(bundle);
        fragAccount.setArguments(bundle);
        fragFundPrice.setArguments(bundle);
        FundPerformance.setArguments(bundle);
        gicAccountFragment.setArguments(bundle);
        cashAccTrxFragment.setArguments(bundle);
        cashAccDetailFragment.setArguments(bundle);


        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        if(AccountType.equals("1"))
        {
            tw.setText(getString(R.string.GIC_Account));
            mViewPagerAdapter.addFragment(gicAccountFragment,getString(R.string.gic_details));
            //To hide the first tab
            tabLayout.setVisibility(View.GONE);
        }
        else if(AccountType.equals("2")){
            tw.setText(getString(R.string.Cash_Account));
            mViewPagerAdapter.addFragment(cashAccTrxFragment,getString(R.string.cash_transactions));
            mViewPagerAdapter.addFragment(cashAccDetailFragment,getString(R.string.Details));
        }
        else {
            tw.setText(getString(R.string.Account_Details));
            mViewPagerAdapter.addFragment(fragTran,getString(R.string.Trades));
            mViewPagerAdapter.addFragment(fragAccount,getString(R.string.Account)); //getString(R.string.acc_info));
            mViewPagerAdapter.addFragment(fragFundPrice,getString(R.string.Prices));//getString(R.string.fund_price));
            mViewPagerAdapter.addFragment(FundPerformance,"Stats");
        }

        viewPager.setAdapter(mViewPagerAdapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        public void addFragmentGIC(Fragment fragment) {
            mFragmentList.add(fragment);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
