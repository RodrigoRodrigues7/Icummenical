package com.example.android.icummenical.Helper;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//Para Fazer uso dos métodos dessa classe, as outras classes tem que extender de 'CommonActivity'
abstract public class CommonActivity extends AppCompatActivity {

    protected void showToastShort(String message) {
        Toast.makeText(this,
                message,
                Toast.LENGTH_SHORT)
                .show();
    }

    protected void showToastLong(String message) {
        Toast.makeText(this,
                message,
                Toast.LENGTH_LONG)
                .show();
    }

    protected void closeKeyboard(){

        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

}
