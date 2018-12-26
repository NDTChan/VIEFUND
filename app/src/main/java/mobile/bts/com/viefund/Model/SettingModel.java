package mobile.bts.com.viefund.Model;

import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by THANH on 4/13/2018.
 */

public class SettingModel {
    private int text;
    private int image;
    private String circle_image;

    public SettingModel(int text, int image, String circle_image) {
        this.text = text;
        this.image = image;
        this.circle_image = circle_image;
    }

    public int getText() {
        return text;
    }

    public void setText(int text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCircle_image() {
        return circle_image;
    }

    public void setCircle_image(String circle_image) {
        this.circle_image = circle_image;
    }
}
