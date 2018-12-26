package mobile.bts.com.viefund.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import mobile.bts.com.viefund.R;

/**
 * BT Company
 * Created by Administrator on 3/30/2018.
 */

public class NotificationHolder extends RecyclerView.ViewHolder {
    public CircleImageView _iwAvaNotification;
    public TextView _twFromToNotidicafion;
    public TextView _twDateNotification;
    public TextView _twSubjectNotification;
    public TextView _twContentNotification;

    public NotificationHolder(View itemView) {
        super(itemView);
        _iwAvaNotification = (CircleImageView) itemView.findViewById(R.id.iwAvaNotification);
        _twFromToNotidicafion = (TextView) itemView.findViewById(R.id.twFromToNotidicafion);
        _twDateNotification = (TextView) itemView.findViewById(R.id.twDateNotification);
        _twSubjectNotification = (TextView) itemView.findViewById(R.id.twSubjectNotification);
        _twContentNotification = (TextView) itemView.findViewById(R.id.twContentNotification);
    }
}
