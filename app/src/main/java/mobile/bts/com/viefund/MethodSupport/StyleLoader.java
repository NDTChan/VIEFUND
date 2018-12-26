package mobile.bts.com.viefund.MethodSupport;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;

import mobile.bts.com.viefund.R;

/**
 * BT Company
 * Created by Administrator on 4/14/2018.
 */
public class StyleLoader {
    int color;
    public StyleLoader(int color) {
        this.color = color;
    }

    public static class StyleAttrs {
        public int textColor;
        public int dividerColor;
    }

//    public StyleAttrs load(Context context, @StyleRes int styleResId) {
////        final TypedArray styledAttributes = context.obtainStyledAttributes(styleResId, R.styleable.CustomWidget);
////        return load(styledAttributes);
//    }
//
//    public StyleAttrs load(Context context, AttributeSet attrs) {
////        final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.CustomWidget);
////        return load(styledAttributes);
//    }
//
//    @NonNull
//    private StyleAttrs load(TypedArray styledAttributes) {
//        StyleAttrs styleAttrs = new StyleAttrs();
//        try {
//            styleAttrs.textColor = styledAttributes.getColor(R.styleable.CustomWidget_colorPrimary, 0);
//            styleAttrs.dividerColor = styledAttributes.getColor(R.styleable.CustomWidget_colorPrimaryDark, 0);
//        } finally {
//            styledAttributes.recycle();
//        }
//        return styleAttrs;
//    }

}
