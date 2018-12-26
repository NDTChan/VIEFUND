package mobile.bts.com.viefund.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mobile.bts.com.viefund.Holder.NotificationHolder;
import mobile.bts.com.viefund.Holder.SettingHolder;
import mobile.bts.com.viefund.MainActivity;
import mobile.bts.com.viefund.Model.Constants;
import mobile.bts.com.viefund.Model.NotificationModel;
import mobile.bts.com.viefund.Model.SettingModel;
import mobile.bts.com.viefund.MultiLanguage.ChangeLanguageActivity;
import mobile.bts.com.viefund.R;

/**
 * Created by THANH on 4/13/2018.
 */

public class SettingAdapter  extends RecyclerView.Adapter<SettingHolder> {
    public static String TAG = SettingAdapter.class.getSimpleName();
    public ArrayList<SettingModel> data = new ArrayList<>();
    public Context context;
    public Class<?> desClass;
    View v, view;
    public SettingAdapter(ArrayList<SettingModel> data, Context context,Class cls) {
        this.data = data;
        this.context = context;
        this.desClass = cls;
    }

    @Override
    public SettingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings, parent, false);
        SettingHolder vh = new SettingHolder(v);
        Log.d(TAG, "that context is: " + context);
        Log.d(TAG, "that view is: " + v);
        return vh;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(SettingHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: " + data.get(position).getText());
        holder.tv_setting.setText(data.get(position).getText());
        holder.iv_setting.setImageResource(data.get(position).getImage());
        int myColor = Color.parseColor(data.get(position).getCircle_image());
       // Log.d(TAG, "onBindViewHolder: "+myColor);
        Bitmap image = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        image.eraseColor(myColor);
        if(data.get(position).getText() != R.string.nav_change_theme)
        {
            holder.civ_setting.setImageResource(myColor);
        }
        else {
            holder.civ_setting.setImageBitmap(image);
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (data.get(position).getText()){
                    case R.string.changelanguage:
                        Intent intent2 = new Intent(context, ChangeLanguageActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent2);
                        break;
                    case R.string.nav_change_theme:
                        Intent intent = new Intent(context, desClass);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
