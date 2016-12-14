package bd.parvez.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ParveZ on 14-Nov-15.
 *
 */
public class MySharedPreferences {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor sharedPreferenceEditor;

    private static void create(String key, Context context) {
        sharedPreferences = context.getSharedPreferences(key, context.MODE_PRIVATE);
        sharedPreferenceEditor = sharedPreferences.edit();
    }

    /**
     * Save Integer type of data in SharedPreferences
     *
     * @param key     The name of your data
     * @param x       your Integer value
     * @param context passing your current activity or context
     */
    public static void saveInt(String key, int x, Context context) {
        create(key, context);
        sharedPreferenceEditor.putInt(key, x);
        sharedPreferenceEditor.commit();
    }

    /**
     * Save String type of data in SharedPreferences
     *
     * @param key     The name of your data
     * @param x       your String value
     * @param context passing your current activity or context
     */
    public static void saveString(String key, String x, Context context) {
        create(key, context);
        sharedPreferenceEditor.putString(key, x);
        sharedPreferenceEditor.commit();
    }

    /**
     * Save Boolean type of data in SharedPreferences
     *
     * @param key     The name of your data
     * @param x       your Boolean value
     * @param context passing your current activity or context
     */
    public static void saveBoolean(String key, boolean x, Context context) {
        create(key, context);
        sharedPreferenceEditor.putBoolean(key, x);
        sharedPreferenceEditor.commit();
    }

    /**
     * Save Float type of data in SharedPreferences
     *
     * @param key     The name of your data
     * @param x       your Float value
     * @param context passing your current activity or context
     */
    public static void saveFloat(String key, float x, Context context) {
        create(key, context);
        sharedPreferenceEditor.putFloat(key, x);
        sharedPreferenceEditor.commit();
    }

    /**
     * Save Long type of data in SharedPreferences
     *
     * @param key     The name of your data
     * @param x       your Float value
     * @param context passing your current activity or context
     */
    public static void saveLong(String key, long x, Context context) {
        create(key, context);
        sharedPreferenceEditor.putLong(key, x);
        sharedPreferenceEditor.commit();
    }

    /**
     * @param key     The name of your data
     * @param context passing your current activity or context
     * @return Integer value
     */
    public static int loadInt(String key, Context context) {
        create(key, context);
        return sharedPreferences.getInt(key, 0);
    }

    /**
     * @param key     The name of your data
     * @param context passing your current activity or context
     * @return String value
     */
    public static String loadString(String key, Context context) {
        create(key, context);
        return sharedPreferences.getString(key, "Data not found.");
    }

    /**
     * @param key     The name of your data
     * @param context passing your current activity or context
     * @return Boolean value
     */
    public static boolean loadBoolean(String key, Context context) {
        create(key, context);
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * @param key     The name of your data
     * @param context passing your current activity or context
     * @return Float value
     */
    public static float loadFloat(String key, Context context) {
        create(key, context);
        return sharedPreferences.getFloat(key, 0);
    }

    /**
     * @param key     The name of your data
     * @param context passing your current activity or context
     * @return Float value
     */
    public static long loadLong(String key, Context context) {
        create(key, context);
        return sharedPreferences.getLong(key, 0);
    }
}
