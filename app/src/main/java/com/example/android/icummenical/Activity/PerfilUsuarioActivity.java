package com.example.android.icummenical.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.android.icummenical.DAO.ConfigFirebase;
import com.example.android.icummenical.Helper.CommonActivity;
import com.example.android.icummenical.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class PerfilUsuarioActivity extends CommonActivity {

    private static final int GALLERY_CODE = 2333;

    private ImageView imgFotoUsuario;
    private Button btnSalvarFoto, btnVoltarMenu;

    private String emailUsuarioLogado;
    private StorageReference mReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        mReference = ConfigFirebase.getStorageReference();
        mAuth = ConfigFirebase.getFirebaseAuth();
        emailUsuarioLogado = mAuth.getCurrentUser().getEmail();

        carregarFotoPadrao();

        imgFotoUsuario = findViewById(R.id.img_fotoPerfilUsuario);
        btnSalvarFoto = findViewById(R.id.btn_salvarFotoPerfil);
        btnVoltarMenu = findViewById(R.id.btn_voltarMenuPrincipal);

        imgFotoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abrirGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(abrirGaleria, "Selecione uma Imagen: "), GALLERY_CODE);

            }
        });

        btnSalvarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Foto de Perfil Salva");
                salvarFoto();
            }
        });

        btnVoltarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMenu();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        final int width = 300;
        final int height = 300;

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_CODE) {
                Uri imagemSelecionada = data.getData();
                Picasso.with(PerfilUsuarioActivity.this).load(imagemSelecionada.toString()).resize(width, height).centerCrop().into(imgFotoUsuario);
            }
        }

    }

//--------------------------------------------------------------------------------------------------

    private void salvarFoto() {

        StorageReference imageReference = mReference.child("fotoPerfilUsuario-" + emailUsuarioLogado + "/" + emailUsuarioLogado + ".jpg");

        imgFotoUsuario.setDrawingCacheEnabled(true);
        imgFotoUsuario.buildDrawingCache();

        Bitmap bitmap = imgFotoUsuario.getDrawingCache();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        byte[] data = outputStream.toByteArray();
        UploadTask uploadTask = imageReference.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                carregarFotoPadrao();
            }
        });

        abrirMenu();

    }

    private void carregarFotoPadrao() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReferenceFromUrl("gs://icummenical.appspot.com/fotoPerfilUsuario-" + emailUsuarioLogado + "/" + emailUsuarioLogado + ".jpg");

        final int width = 300;
        final int height = 300;

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(PerfilUsuarioActivity.this).load(uri.toString()).resize(width, height).centerCrop().into(imgFotoUsuario);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast("Imagem Não Encontrada");
            }
        });


    }

    private void abrirMenu() {
        Intent voltarMenu = new Intent(PerfilUsuarioActivity.this, PrincipalActivity.class);
        startActivity(voltarMenu);
        finish();
    }

}
