package com.example.android.icummenical.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

public class Preferences {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private Context context;
    private static final String FILE_NAME = "app.preferences";
    private int MODE = 0;

    private final String LOGGED_USER_EMAIL = "userEmail";
    private final String LOGGED_USER_PASSWORD = "userPassword";

    public Preferences(Context parameterContext) {
        context = parameterContext;
        preferences = context.getSharedPreferences(FILE_NAME, MODE);

        editor = preferences.edit();
    }

    public void saveUserPreferences(String email, String password) {

        editor.putString(LOGGED_USER_EMAIL, email);
        editor.putString(LOGGED_USER_PASSWORD, password);
        editor.commit();
    }


}
