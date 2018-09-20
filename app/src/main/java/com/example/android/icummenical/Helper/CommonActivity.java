package com.example.android.icummenical.Helper;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

//Para Fazer uso dos métodos dessa classe, as outras classes tem que extender de 'CommonActivity'
abstract public class CommonActivity extends AppCompatActivity {

    protected ProgressBar progressBar;

    protected void showSnackBar(String message) {
        Snackbar.make(progressBar,
                message,
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    protected void showToast(String message) {
        Toast.makeText(this,
                message,
                Toast.LENGTH_SHORT)
                .show();
    }

}
