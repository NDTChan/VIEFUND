package mobile.bts.com.viefund.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mobile.bts.com.viefund.Activity.NotificationActivity;
import mobile.bts.com.viefund.Activity.NotificationDetailsActivity;
import mobile.bts.com.viefund.Activity.SentRepNotificationActivity;
import mobile.bts.com.viefund.Holder.NotificationHolder;
import mobile.bts.com.viefund.Model.NotificationModel;
import mobile.bts.com.viefund.R;

/**
 * BT Company
 * Created by Administrator on 3/30/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {
    public static String TAG = NotificationAdapter.class.getSimpleName();
    public ArrayList<NotificationModel> data = new ArrayList<>();
    public Context context;
    public Class<?> desClass;
//    public AppCompatActivity toClass;
    View v, view;

    public NotificationAdapter(ArrayList<NotificationModel> data, Context context,Class cls) {
        this.data = data;
        this.context = context;
        this.desClass = cls;
    }

    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        NotificationHolder vh = new NotificationHolder(v);
        Log.d(TAG, "that context is: " + context);
        Log.d(TAG, "that view is: " + v);
        return vh;
    }

    @Override
    public void onBindViewHolder(NotificationHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: " + data.get(position).getFrom());
        holder._twFromToNotidicafion.setText(data.get(position).getFrom());
        holder._twDateNotification.setText(data.get(position).getDateSend());
        holder._twSubjectNotification.setText(data.get(position).getSubject());
        holder._twContentNotification.setText(data.get(position).getContent());
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, desClass);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putString("iMsgID", data.get(position).getId());
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
