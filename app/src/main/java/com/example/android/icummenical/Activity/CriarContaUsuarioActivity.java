package com.example.android.icummenical.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.icummenical.Classes.Usuario;
import com.example.android.icummenical.DAO.ConfigFirebase;
import com.example.android.icummenical.Helper.CommonActivity;
import com.example.android.icummenical.Helper.Preferences;
import com.example.android.icummenical.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CriarContaUsuarioActivity extends CommonActivity {

    private Button btnCriarNovaConta, btnRetornarLogin;
    private EditText edtNomeUsuario, edtEmail, edtSenha, edtSenhaConfirm;
    private ProgressDialog mProgress;

    private Usuario usuario;

    private FirebaseAuth mAuth;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta_usuario);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();

        edtNomeUsuario = findViewById(R.id.edt_usernameCadastro);
        edtEmail = findViewById(R.id.edt_emailCadastro);
        edtSenha = findViewById(R.id.edt_senhaCadastro);
        edtSenhaConfirm = findViewById(R.id.edt_senhaCadastroConfirmar);

        mProgress = new ProgressDialog(this);

        btnCriarNovaConta = findViewById(R.id.btn_proximoPasso);
        btnRetornarLogin = findViewById(R.id.btn_retornarLogin);

        // Botão para Criar Nova Conta
        btnCriarNovaConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeKeyboard();
                if (edtSenha.getText().toString().equals(edtSenhaConfirm.getText().toString())) {

                    mProgress.setTitle("Criando Nova Conta");
                    mProgress.setMessage("Estamos Registrando sua Conta, Aguarde um Momento ...");
                    mProgress.show();

                    usuario = new Usuario();
                    usuario.setNome(edtNomeUsuario.getText().toString());
                    usuario.setEmail(edtEmail.getText().toString());
                    usuario.setSenha(edtSenha.getText().toString());

                    registrarNovaConta();

                } else {
                    showToastShort("Por Favor, Verifique se a 'Senha' está Correta.");
                    edtSenha.requestFocus();
                    edtSenha.setText("");
                    edtSenhaConfirm.setText("");
                }

            }
        });

        // Botão para Voltar a Tela de Login
        btnRetornarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CriarContaUsuarioActivity.this, LoginActivity.class);
                startActivity(intent);
                closeKeyboard();
                finish();

            }
        });

    }

//--------------------------------------------------------------------------------------------------

    private void registrarNovaConta() {

        mAuth = ConfigFirebase.getFirebaseAuth();
        mAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(CriarContaUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    enviarVerificacaoEmail();
                    inserirNovoUsuario();
                    mAuth.signOut();

                    voltarTelaLogin();
                    finish();

                } else {

                    String error = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        error = "Senha Muito Fraca! ela Deve ter no Mínimo 8 Caracteres e Conter Letras e Números.";
                        mProgress.dismiss();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        error = "Email Inválido! Tente Novamente.";
                        mProgress.dismiss();
                    } catch (FirebaseAuthUserCollisionException e) {
                        error = "Já Existe uma Conta com Esse Email, Tente Novamente.";
                        mProgress.dismiss();
                    } catch (Exception e) {
                        error = "Houve um Erro ao Criar sua Nova Conta, Tente Mais Tarde.";
                        mProgress.dismiss();
                        e.printStackTrace();
                    }

                    showToastLong("Erro: " + error);
                }

            }
        });

    }

    private boolean inserirNovoUsuario() {

        try {

            mReference = ConfigFirebase.getDatabaseReference().child("usuarios");
            String key = mReference.push().getKey();
            usuario.setKeyUsuario(key);

            mReference.child(key).setValue(usuario);
            mProgress.dismiss();
            return true;
        } catch (Exception e) {

            showToastShort("Erro ao Criar Nova Conta!");
            e.printStackTrace();
            return false;
        }

    }

    private void enviarVerificacaoEmail() {

        FirebaseAuth.getInstance().getCurrentUser()
                .sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            showToastLong("Enviamos um Email para: " + edtEmail.getText().toString() + ". Verifique sua Caixa de Entrada.");
                        else
                            showToastLong("Falha ao Enviar o Email de Verificação.");
                    }
                });

    }

    private void abrirTelaPrincipal() {

        mAuth = ConfigFirebase.getFirebaseAuth();
        Preferences preferences = new Preferences(CriarContaUsuarioActivity.this);

        mAuth.signInWithEmailAndPassword(preferences.getLoggedUserEmail(), preferences.getLoggedUserPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Intent intent = new Intent(CriarContaUsuarioActivity.this, PrincipalActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    showToastShort("Falha!");
                    mAuth.signOut();

                    Intent intent = new Intent(CriarContaUsuarioActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });

    }

    private void voltarTelaLogin() {

        Intent intent = new Intent(CriarContaUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

}