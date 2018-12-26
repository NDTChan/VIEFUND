package mobile.bts.com.viefund.Model;

/**
 * BT Company
 * Created by Administrator on 3/15/2018.
 */

public class Language {
    private int mId;
    private String mName;
    private String mCode;

    public Language() {
    }

    public Language(int id, String name, String code) {
        mId = id;
        mName = name;
        mCode = code;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getCode() {
        return mCode;
    }


}
