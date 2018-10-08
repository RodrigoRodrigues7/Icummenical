package com.example.android.icummenical.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CriarContaUsuarioActivity extends CommonActivity {

    private Button btnCriarNovaConta, btnRetornarLogin;
    private EditText edtNomeUsuario, edtEmail, edtSenha, edtSenhaConfirm;
    private ProgressDialog mProgress;

    private Usuario usuario;
    private FirebaseAuth mAuth;

    private StorageReference mStorageRef;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta_usuario);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

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
                    showToast("Por Favor, Verifique se a 'Senha' está Correta, e Escolha uma Imagem de Perfil.");
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

                    inserirNovoUsuario();
                    mAuth.signOut();
                    abrirTelaPrincipal();
                    finish();

                } else {

                    String error = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        error = "Senha Muito Fraca! ela Deve ter no Mínimo 8 Caracteres e Conter Letras e Números.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        error = "Email Inválido! Tente Novamente.";
                    } catch (FirebaseAuthUserCollisionException e) {
                        error = "Já Existe uma Conta com Esse Email, Tente Novamente.";
                    } catch (Exception e) {
                        error = "Houve um Erro ao Criar sua Nova Conta, Tente Mais Tarde.";
                        e.printStackTrace();
                    }

                    showToast("Erro: " + error);
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

            showToast("Conta Criada com Sucesso!");
            return true;
        } catch (Exception e) {

            showToast("Erro ao Criar Nova Conta!");
            e.printStackTrace();
            return false;
        }

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
                    showToast("Falha!");
                    mAuth.signOut();

                    Intent intent = new Intent(CriarContaUsuarioActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });

    }


    private boolean verificarCampos() {

        if (!TextUtils.isEmpty(edtNomeUsuario.toString()) || !TextUtils.isEmpty(edtEmail.toString()) || !TextUtils.isEmpty(edtSenha.toString())) {
            showToast("Preencha os Campos de 'Nome', 'Email' e 'Senha'!");
            edtNomeUsuario.requestFocus();
            return false;
        }

        return true;
    }

}