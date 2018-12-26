package mobile.bts.com.viefund.MultiLanguage;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import mobile.bts.com.viefund.Application.BTApplication;
import mobile.bts.com.viefund.Model.Constants;
import mobile.bts.com.viefund.Model.Language;
import mobile.bts.com.viefund.*;

/**
 * BT Company
 * Created by Administrator on 3/15/2018.
 */

public class LanguageUtils {
    public static final String TAG = LanguageUtils.class.getSimpleName();
    private static Language sCurrentLanguage = null;
    public static Language getCurrentLanguage() {
        if (sCurrentLanguage == null) {
            sCurrentLanguage = initCurrentLanguage();
        }
        return sCurrentLanguage;
    }

    public static Context onAttach(Context context) {
        return setLocale(context,initCurrentLanguage());
    }


    /**
     * check language exist in SharedPrefs, if not exist then default language is English
     */
    private static Language initCurrentLanguage() {
        Language currentLanguage =
                SharedPrefsLanguage.getInstance().get(SharedPrefsLanguage.LANGUAGE, Language.class);
        if (currentLanguage != null) {
            Log.d(TAG, "initCurrentLanguage: "+currentLanguage.getName());
            return currentLanguage;
        }
        currentLanguage = new Language(Constants.Value.DEFAULT_LANGUAGE_ID,
                BTApplication.getInstance().getString(R.string.language_english),
                BTApplication.getInstance().getString(R.string.language_english_code));
        SharedPrefsLanguage.getInstance().put(SharedPrefsLanguage.LANGUAGE, currentLanguage);
        return currentLanguage;
    }

    /**
     * return language list from string.xml
     */
    public static List<Language> getLanguageData() {
        List<Language> languageList = new ArrayList<>();
        List<String> languageNames =
                Arrays.asList(BTApplication.getInstance().getResources().getStringArray(R.array.language_names));
        List<String> languageCodes =
                Arrays.asList(BTApplication.getInstance().getResources().getStringArray(R.array.language_codes));
        if (languageNames.size() != languageCodes.size()) {
            // error, make sure these arrays are same size
            return languageList;
        }
        for (int i = 0, size = languageNames.size(); i < size; i++) {
            languageList.add(new Language(i, languageNames.get(i), languageCodes.get(i)));
        }
        Log.d(TAG, "getLanguageData: "+languageList.get(0).getName());
        return languageList;
    }

    /**
     * load current locale and change language
     */
    public static void loadLocale(Context context) {
        setLocale(context,initCurrentLanguage());
    }

    public static Context setLocale(Context context, Language language) {
        SharedPrefsLanguage.getInstance().put(SharedPrefsLanguage.LANGUAGE, language);
        sCurrentLanguage = language;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }

        return updateResourcesLegacy(context, language);
    }

    /**
     * change app language
     */
    @SuppressWarnings("deprecation")
    public static void changeLanguage(Language language) {
        SharedPrefsLanguage.getInstance().put(SharedPrefsLanguage.LANGUAGE, language);
        sCurrentLanguage = language;
        Log.d(TAG, "changeLanguage: "+language.getCode());
        Locale locale = new Locale(language.getCode());
        Resources resources = BTApplication.getInstance().getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, Language language) {
        Locale locale = new Locale(language.getCode());
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, Language language) {
        Locale locale = new Locale(language.getCode());
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }


}
