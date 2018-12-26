package mobile.bts.com.viefund.CustomColor;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import mobile.bts.com.viefund.CustomFont.CustomColor;
import mobile.bts.com.viefund.R;

public class BaseActivity extends CustomColor {

    private final static int THEME_DEFAULT = 1;
    private final static int THEME_RED = 2;
    private final static int THEME_PINK = 3;
    private final static int THEME_PURPLE = 4;
    private final static int THEME_DEEP_PURPLE = 5;
    private final static int THEME_INDIGO = 6;
    private final static int THEME_BLUE = 7;
    private final static int THEME_LIGHT_BLUE = 8;
    private final static int THEME_CYAN = 9;
    private final static int THEME_TEAL = 10;
    private final static int THEME_GREEN = 11;
    private final static int THEME_LIGHT_GREEN = 12;
    private final static int THEME_LIME = 13;
    private final static int THEME_YELLOW = 14;
    private final static int THEME_AMBER = 15;
    private final static int THEME_ORANGE = 16;
    private final static int THEME_DEEP_ORANGE = 17;
    private final static int THEME_BROWN = 18;
    private final static int THEME_GREY = 19;
    private final static int THEME_BLUE_GREY = 20;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTheme();
    }
    public void updateTheme() {
        if (Utility.getTheme(getApplicationContext()) <= THEME_DEFAULT) {
            setTheme(R.style.AppTheme_Default);

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        } else if (Utility.getTheme(getApplicationContext()) == THEME_RED) {
            setTheme(R.style.AppTheme_Red);

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_red_500));

        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_PINK) {
            setTheme(R.style.AppTheme_Pink);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_pink_500));
            }
        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_PURPLE) {
            setTheme(R.style.AppTheme_Purple);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_purple_500));
            }
        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_DEEP_PURPLE) {
            setTheme(R.style.AppTheme_DeepPurple);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_deep_purple_500));
            }
        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_INDIGO) {
            setTheme(R.style.AppTheme_Indigo);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_indigo_500));
            }
        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_BLUE) {
            setTheme(R.style.AppTheme_Blue);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_blue_500));
            }
        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_LIGHT_BLUE) {
            setTheme(R.style.AppTheme_LightBlue);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_light_blue_500));
            }
        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_CYAN) {
            setTheme(R.style.AppTheme_Cyan);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_cyan_500));
            }
        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_TEAL) {
            setTheme(R.style.AppTheme_Teal);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_teal_500));
            }
        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_GREEN) {
            setTheme(R.style.AppTheme_Green);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_green_500));
            }
        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_LIGHT_GREEN) {
            setTheme(R.style.AppTheme_LightGreen);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_light_green_500));
            }
        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_LIME) {
            setTheme(R.style.AppTheme_Lime);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_lime_500));
            }
        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_YELLOW) {
            setTheme(R.style.AppTheme_Yellow);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_yellow_500));
            }
        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_AMBER) {
            setTheme(R.style.AppTheme_Amber);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_amber_500));
            }
        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_ORANGE) {
            setTheme(R.style.AppTheme_Orange);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_orange_500));
            }
        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_DEEP_ORANGE) {
            setTheme(R.style.AppTheme_DeepOrange);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_deep_orange_500));
            }
        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_BROWN) {
            setTheme(R.style.AppTheme_Brown);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_brown_500));
            }
        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_GREY) {
            setTheme(R.style.AppTheme_Grey);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_grey_500));
            }
        }
        else if (Utility.getTheme(getApplicationContext()) == THEME_BLUE_GREY) {
            setTheme(R.style.AppTheme_BlueGrey);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.md_blue_grey_500));
            }
        }

    }
}
