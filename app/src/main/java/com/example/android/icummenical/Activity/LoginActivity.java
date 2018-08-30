package com.example.android.icummenical.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.icummenical.Classes.Usuario;
import com.example.android.icummenical.DAO.ConfigFirebase;
import com.example.android.icummenical.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnLogin;
    private ProgressBar progressBar;

    //Classe do Firebase para autenticação
    private FirebaseAuth auth;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edt_emailLogin);
        edtSenha = findViewById(R.id.edt_senhaLogin);
        btnLogin = findViewById(R.id.btn_loginUsuario);

        progressBar = findViewById(R.id.progressBarID);
        progressBar.setVisibility(View.GONE);

        // Verificando se o usuário já está logado
        if (verificarUsuarioLogado()) {

            Intent intent = new Intent(LoginActivity.this, TelaPrincipalActivity.class);
            startActivity(intent);

        } else {

            // Quando o usuário clicar no botão de Login
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!edtEmail.getText().toString().trim().equals("") && !edtSenha.getText().toString().trim().equals("")) {

                        usuario = new Usuario();
                        usuario.setEmail(edtEmail.getText().toString());
                        usuario.setSenha(edtSenha.getText().toString());

                        validarLogin();
                        progressBar.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(LoginActivity.this, "Por Favor, Preencha os Campos de E-mail e Senha!", Toast.LENGTH_SHORT).show();
                        edtEmail.requestFocus();
                    }

                }
            });

        }

    }

    //Método para autenticar o login do usuário
    private void validarLogin() {

        auth = ConfigFirebase.getFirebaseAuth();
        auth.signInWithEmailAndPassword(usuario.getEmail().toString(), usuario.getSenha().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    abrirTelaPrincipal();

                    Toast.makeText(LoginActivity.this, "Usuário Logado com Sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Senha ou E-mail Inválido, tente Novamente!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, TelaPrincipalActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean verificarUsuarioLogado() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return true;
        } else {
            return false;
        }

    }

}
