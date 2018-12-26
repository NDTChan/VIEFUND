package mobile.bts.com.viefund.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import mobile.bts.com.viefund.R;

/**
 * Created by THANH on 4/13/2018.
 */

public class SettingHolder extends RecyclerView.ViewHolder  {
    public CircleImageView civ_setting;
    public ImageView iv_setting;
    public TextView tv_setting;
    public SettingHolder(View vItem)
    {
        super(vItem);
        civ_setting=vItem.findViewById(R.id.setting_circle_image);
        iv_setting=vItem.findViewById(R.id.setting_image);
        tv_setting=vItem.findViewById(R.id.setting_text);
    }
}
