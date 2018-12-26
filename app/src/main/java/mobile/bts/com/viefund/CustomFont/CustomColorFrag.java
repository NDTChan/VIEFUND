package mobile.bts.com.viefund.CustomFont;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ImageView;

import mobile.bts.com.viefund.R;

/**
 * Created by THANH on 4/18/2018.
 */

public class CustomColorFrag extends Fragment {
    private  String SHARED_PREFERENCES_NAME="Color";
    private  String EDIT_PREFERENCES = "VALUE";
    public static String trans;
    public void PreferenceClass (Context context){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_NAME, context.MODE_PRIVATE);
        trans = sharedPreferences.getString(EDIT_PREFERENCES,"#31844c");
    }
    public static Drawable setTint(Drawable d, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(d);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }
    public void ChangeThemeToolbar(Toolbar toolbar, ImageView imageView)
    {
        toolbar.setBackgroundColor(Color.parseColor(trans));
        imageView.setBackgroundColor(Color.parseColor(trans));
    }
    public void ChangeBackground()
    {
        TypedArray ta = this.getResources().obtainTypedArray(R.array.demo_colors);
        int[] colors = new int[ta.length()];

        int a = 0;
        for(int i=0;i<ta.length();i++)
        {
            colors[i]=ta.getColor(i,0);
            if(colors[i]==Color.parseColor(trans))
            {
                a=colors[i];
            }
        }
        this.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        this.getActivity().getWindow().setStatusBarColor(a);
    }

}
