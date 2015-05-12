package io.zarda.elnerd.src;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Type;

import io.zarda.elnerd.model.Constants;

/**
 * Created by Ahmed Atef on 12/05/15.
 */
public class SharedPreferencesManager {

    private static SharedPreferencesManager ourInstance;
    private static Context context;
    private static SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    public static void initialize(Context context) {
        SharedPreferencesManager.context = context;
        ourInstance = new SharedPreferencesManager();
        ourInstance.sharedpreferences = context.getSharedPreferences(Constants.SHARED_MEMORY_NAME,
                Context.MODE_PRIVATE);
        ourInstance.editor = sharedpreferences.edit();
    }

    public static SharedPreferencesManager getInstance() {
        return ourInstance;
    }

    public void setKey(Constants.SharedMemory sharedKey, Object value) {
        Type type = sharedKey.getType();
        if (type == int.class) {
            editor.putInt(sharedKey.getName(), (int) value);
        } else if (type == long.class) {
            editor.putLong(sharedKey.getName(), (long) value);
        } else if (type == String.class) {
            editor.putString(sharedKey.getName(), (String) value);
        } else {
            return;
        }
        editor.commit();
    }

    public Object getKey(Constants.SharedMemory sharedKey, Object defaultValue) {
        Type type = sharedKey.getType();
        if (type == int.class) {
            return sharedpreferences.getInt(sharedKey.getName(), (int) defaultValue);
        } else if (type == long.class) {
            return sharedpreferences.getLong(sharedKey.getName(), (long) defaultValue);
        } else if (type == String.class) {
            return sharedpreferences.getString(sharedKey.getName(), (String) defaultValue);
        }
        return null;
    }
}
