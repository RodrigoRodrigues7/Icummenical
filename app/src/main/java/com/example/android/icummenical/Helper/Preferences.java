package com.example.android.icummenical.Helper;

import android.content.Context;
import android.content.SharedPreferences;

//Esta classe irá salvar, no celular, as preferencias do usuário logado no app
//Nome do Arquivo = "app.preferences"
public class Preferences {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private Context context;
    private static final String FILE_NAME = "app.preferences";
    private int MODE = 0; //Para editar o arquivo(FILE_NAME) que ficará salvo no celular

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


    public String getLoggedUserEmail() {
        return preferences.getString(LOGGED_USER_EMAIL, null);
    }

    public String getLoggedUserPassword() {
        return preferences.getString(LOGGED_USER_PASSWORD, null);
    }

}
