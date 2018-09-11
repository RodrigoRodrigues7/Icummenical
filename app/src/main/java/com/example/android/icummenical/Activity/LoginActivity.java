package com.example.android.icummenical.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.icummenical.Classes.Usuario;
import com.example.android.icummenical.DAO.ConfigFirebase;
import com.example.android.icummenical.Helper.Preferences;
import com.example.android.icummenical.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnLogin;
    private ProgressBar progressBar;
    private ImageView login_Facebook;

    private FirebaseAuth mAuth;
    private Usuario usuario;

    private final static String TAG = "FACELOG";
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        login_Facebook = findViewById(R.id.img_login_Facebook);
        login_Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        showSnackbar(exception.getMessage());
                    }
                });

            }
        });

//--------------------------------------------------------------------------------------------------

        edtEmail = findViewById(R.id.edt_emailLogin);
        edtSenha = findViewById(R.id.edt_senhaLogin);
        btnLogin = findViewById(R.id.btn_loginUsuario);

        progressBar = findViewById(R.id.progressBarID);
        progressBar.setVisibility(View.GONE);

        // Verificando se o usuário já está logado
        if (verificarUsuarioLogado()) {

            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
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
                        btnLogin.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(LoginActivity.this, "Por Favor, Preencha os Campos de E-mail e Senha!", Toast.LENGTH_SHORT).show();
                        edtEmail.requestFocus();
                    }

                }
            });


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI();
        }
    }

//------------------------------------------------------------------------------------------------------------

    public void updateUI() {
        showSnackbar("Você Entrou no Icummenical");
        abrirTelaPrincipal();
    }

    // Método para Gerar uma Credencial o 'Token' de Acesso do Facebook para o Firebase
    private void handleFacebookAccessToken(AccessToken accessToken) {
        Log.d(TAG, "handleFacebookAccessToken:" + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            updateUI();
                        }

                        // ...
                    }
                });
    }


//------------------------------------------------------------------------------------------------------------


    //Método para autenticar o login do usuário
    private void validarLogin() {

        mAuth = ConfigFirebase.getFirebaseAuth();
        mAuth.signInWithEmailAndPassword(usuario.getEmail().toString(), usuario.getSenha().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    abrirTelaPrincipal();

                    //Salvando as Preferencias do Usuário Logado
                    Preferences preferences = new Preferences(LoginActivity.this);
                    preferences.saveUserPreferences(usuario.getEmail(), usuario.getSenha());

                    Toast.makeText(LoginActivity.this, "Usuário Logado com Sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Senha ou E-mail Inválido, tente Novamente!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
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

    public void showSnackbar(String message) {
        Snackbar.make(progressBar,
                message,
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}
