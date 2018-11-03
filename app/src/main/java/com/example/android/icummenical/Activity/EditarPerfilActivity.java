package com.example.android.icummenical.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.android.icummenical.Classes.Usuario;
import com.example.android.icummenical.DAO.ConfigFirebase;
import com.example.android.icummenical.Helper.CommonActivity;
import com.example.android.icummenical.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class EditarPerfilActivity extends CommonActivity {

    private static final int GALLERY_CODE = 2333;
    private EditText nomeUsuario, senhaUsuario, confirmarSenhaUsuario;
    private ImageView imgFotoPerfil;
    private Button btnSalvarPerfil, btnCancelar;

    private String txtOrigem, txtNome, txtEmail, txtKeyUsuario;
    private String emailUsuarioLogado;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_usuario);

        mAuth = ConfigFirebase.getFirebaseAuth();
        storageReference = ConfigFirebase.getStorageReference();
        emailUsuarioLogado = mAuth.getCurrentUser().getEmail();

        imgFotoPerfil = findViewById(R.id.img_fotoPerfilUsuarioAtualizar);
        nomeUsuario = findViewById(R.id.edt_nomeUsuarioAtualizar);
        senhaUsuario = findViewById(R.id.edt_senhaUsuarioAtualizar);
        confirmarSenhaUsuario = findViewById(R.id.edt_confirmarSenhaUsuarioAtualizar);

        btnSalvarPerfil = findViewById(R.id.btn_salvarPerfil);
        btnCancelar = findViewById(R.id.btn_cancelarAtualizacaoPerfil);

        carregarFotoUsuario();
        carregarDadosUsuario();

        imgFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abrirGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(abrirGaleria, "Selecione uma Imagen: "), GALLERY_CODE);
            }
        });

        btnSalvarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarFoto();
                salvarDadosPerfil();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaPrincipal();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            Uri uriTarget = data.getData();
            Bitmap bitmap;

            try {

                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriTarget));
                imgFotoPerfil.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

//--------------------------------------------------------------------------------------------------

    private void salvarDadosPerfil() {

        closeKeyboard();
        if (senhaUsuario.getText().toString().equals(confirmarSenhaUsuario.getText().toString())) {
            Usuario usuario = new Usuario();
            usuario.setNome(nomeUsuario.getText().toString());
            usuario.setSenha(senhaUsuario.getText().toString());
            usuario.setEmail(txtEmail.toString());
            usuario.setKeyUsuario(txtKeyUsuario);

            updateUsuario(usuario);
            abrirTelaPrincipal();

        } else {
            showToast("Por Favor, Verifique se a 'Senha' está Correta.");
            senhaUsuario.requestFocus();
            senhaUsuario.setText("");
            confirmarSenhaUsuario.setText("");
        }

    }

    private void salvarFoto() {

        StorageReference imageReference = storageReference.child("fotoPerfilUsuario-" + emailUsuarioLogado + "/" + emailUsuarioLogado + ".jpg");

        imgFotoPerfil.setDrawingCacheEnabled(true);
        imgFotoPerfil.buildDrawingCache();

        Bitmap bitmap = imgFotoPerfil.getDrawingCache();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        byte[] data = outputStream.toByteArray();
        UploadTask uploadTask = imageReference.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                carregarFotoUsuario();
            }
        });

    }

    private boolean updateUsuario(final Usuario usuario) {

        btnSalvarPerfil.setEnabled(false);

        try {
            databaseReference = ConfigFirebase.getDatabaseReference().child("usuarios");
            atualizarSenha(usuario.getSenha());

            databaseReference.child(txtKeyUsuario).setValue(usuario);
            showToast("Sua Conta foi Atualizada!" + usuario.getNome());
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private void atualizarSenha(String novaSenha) {

        FirebaseUser user = mAuth.getCurrentUser();
        user.updatePassword(novaSenha).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("NOVA_SENHA_ATUALIZADA", "--------------------> Senha Atualizada Com Sucesso <--------------------");
                }
            }
        });

    }

//--------------------------------------------------------------------------------------------------

    private void carregarFotoUsuario() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReferenceFromUrl("gs://icummenical.appspot.com/fotoPerfilUsuario-" + emailUsuarioLogado + "/" + emailUsuarioLogado + ".jpg");

        final int width = 300;
        final int height = 300;

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(EditarPerfilActivity.this).load(uri.toString()).resize(width, height).centerCrop().into(imgFotoPerfil);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast("Imagem Não Encontrada");
            }
        });

    }

    private void carregarDadosUsuario() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        txtOrigem = bundle.getString("origem");

        if (txtOrigem.equals("editarUsuario")) {
            txtNome = bundle.getString("nome");
            txtEmail = bundle.getString("email");
            txtKeyUsuario = bundle.getString("keyUsuario");

            nomeUsuario.setText(txtNome);
        }
    }

    private void abrirTelaPrincipal() {
        Intent voltarMenu = new Intent(EditarPerfilActivity.this, PrincipalActivity.class);
        startActivity(voltarMenu);
        finish();
    }

}
