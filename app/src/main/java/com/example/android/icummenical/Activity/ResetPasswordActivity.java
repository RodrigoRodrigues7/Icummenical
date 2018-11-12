package com.example.android.icummenical.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.icummenical.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private Button resetButton;
    private EditText resetEmail;
    private FirebaseAuth mauth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mauth = FirebaseAuth.getInstance();

        resetButton = (Button)findViewById(R.id.btnReset);
        resetEmail = (EditText)findViewById(R.id.textReset);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = resetEmail.getText().toString();
                if(TextUtils.isEmpty(userEmail))
                {
                    Toast.makeText(ResetPasswordActivity.this, "Informe o e-mail", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        mauth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(ResetPasswordActivity.this, "E-mail enviado", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                                }
                                else
                                    {

                                     Toast.makeText(ResetPasswordActivity.this, "E-mail não cadastrado", Toast.LENGTH_SHORT).show();
                                     }
                            }
                        });
                    }
            }
        });

    }
}
