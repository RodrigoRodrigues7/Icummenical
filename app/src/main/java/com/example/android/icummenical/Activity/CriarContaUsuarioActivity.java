package com.example.android.icummenical.Activity;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.icummenical.Classes.Usuario;
import com.example.android.icummenical.DAO.ConfigFirebase;
import com.example.android.icummenical.Helper.CommonActivity;
import com.example.android.icummenical.Helper.Preferences;
import com.example.android.icummenical.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class CriarContaUsuarioActivity extends CommonActivity {

    private static final int PICK_IMAGE = 2223;
    private Button btnCriarConta, btnRetornarLogin;
    private EditText edtNomeUsuario, edtEmail, edtSenha, edtSenhaConfirm;

    private CircleImageView imgFotoPerfil;
    private Uri imgUri;

    private Usuario usuario;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private FirebaseFirestore mFirestore;

    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta_usuario);

        mStorageRef = FirebaseStorage.getInstance().getReference().child("images");
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        edtNomeUsuario = findViewById(R.id.edt_usernameCadastro);
        edtEmail = findViewById(R.id.edt_emailCadastro);
        edtSenha = findViewById(R.id.edt_senhaCadastro);
        edtSenhaConfirm = findViewById(R.id.edt_senhaCadastroConfirmar);

        imgFotoPerfil = findViewById(R.id.img_profilePicture);
        imgUri = null;

        btnCriarConta = findViewById(R.id.btn_criarNovaConta);
        btnRetornarLogin = findViewById(R.id.btn_retornarLogin);

        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (verificarCampos() && edtSenha.getText().toString().equals(edtSenhaConfirm.getText().toString())) {

                    usuario = new Usuario();

                    usuario.setNome(edtNomeUsuario.getText().toString());
                    usuario.setEmail(edtEmail.getText().toString());
                    usuario.setSenha(edtSenha.getText().toString());

                    registrarNovaConta();
                    showToast("Conta Criada com Sucesso!!!");

                } else {
                    showToast("Por Favor, Verifique se a 'Senha' está Correta, e Escolha uma Imagem de Perfil.");
                    edtSenha.requestFocus();
                }

            }
        });

        imgFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Escolha uma Foto de Perfil"), PICK_IMAGE);
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            imgUri = data.getData();
            imgFotoPerfil.setImageURI(imgUri);
        }

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
            mReference.push().setValue(usuario);
            showToast("Conta Criada com Sucesso!");
            return true;
        } catch (Exception e) {

            showToast("Erro ao Inserir Novo Usuário!");
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
