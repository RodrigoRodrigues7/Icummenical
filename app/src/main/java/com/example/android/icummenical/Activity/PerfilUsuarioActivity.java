package com.example.android.icummenical.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;

public class PerfilUsuarioActivity extends CommonActivity {

    private String emailUsuarioLogado;

    private ImageView imgFotoUsuario;
    private Button btnEditarPerfil, btnExcluirConta, btnVoltarMenu;
    private TextView txtNomeUsuario, txtEmailUsuario;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        storageReference = ConfigFirebase.getStorageReference();
        databaseReference = ConfigFirebase.getDatabaseReference();
        mAuth = ConfigFirebase.getFirebaseAuth();
        emailUsuarioLogado = mAuth.getCurrentUser().getEmail();

        //Operação que seleciona no database o usuario com o 'emailUsuarioLogado' e preenche os dados dele nos TextView's
        databaseReference.child("usuarios").orderByChild("email").equalTo(emailUsuarioLogado).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Usuario usuario = postSnapshot.getValue(Usuario.class);
                    txtNomeUsuario.setText("Nome: " + usuario.getNome());
                    txtEmailUsuario.setText("Email: " + usuario.getEmail());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        carregarFotoUsuario();

        imgFotoUsuario = findViewById(R.id.img_fotoPerfilUsuario);
        txtNomeUsuario = findViewById(R.id.txt_perfilNomeUsuario);
        txtEmailUsuario = findViewById(R.id.txt_perfilEmailUsuario);

        btnEditarPerfil = findViewById(R.id.btn_editarPerfilusuario);
        btnExcluirConta = findViewById(R.id.btn_excluirConta);
        btnVoltarMenu = findViewById(R.id.btn_voltarMenuPrincipal);

        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaEditarPerfil();
            }
        });

        btnExcluirConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirContaDeslogar();
            }
        });

        btnVoltarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarMenuPrincipal();
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
                imgFotoUsuario.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
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
                Picasso.with(PerfilUsuarioActivity.this).load(uri.toString()).resize(width, height).centerCrop().into(imgFotoUsuario);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast("Imagem Não Encontrada");
            }
        });

    }

    private void excluirContaDeslogar() {

        String emailUsuarioLogado = mAuth.getCurrentUser().getEmail();

        databaseReference = ConfigFirebase.getDatabaseReference();
        databaseReference.child("usuarios").orderByChild("email").equalTo(emailUsuarioLogado).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    final Usuario usuario = postSnapshot.getValue(Usuario.class);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    //Removendo o usuário do 'Authentication'
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("USUARIO_EXCLUIDO", "-----------------> Conta de Usuário Excluída <-----------------");
                                showToast("Conta Removida Com Sucesso!!!");

                                databaseReference = ConfigFirebase.getDatabaseReference();
                                databaseReference.child("usuarios").child(usuario.getKeyUsuario()).removeValue();

                                mAuth.signOut();
                                abrirTelaLogin();
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

//--------------------------------------------------------------------------------------------------

    private void voltarMenuPrincipal() {
        Intent voltarMenu = new Intent(PerfilUsuarioActivity.this, PrincipalActivity.class);
        startActivity(voltarMenu);
        finish();
    }

    private void abrirTelaEditarPerfil() {

        String emailUsuarioLogado = mAuth.getCurrentUser().getEmail();

        databaseReference = ConfigFirebase.getDatabaseReference();
        databaseReference.child("usuarios").orderByChild("email").equalTo(emailUsuarioLogado).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Usuario usuario = postSnapshot.getValue(Usuario.class);

                    final Intent intent = new Intent(PerfilUsuarioActivity.this, EditarPerfilActivity.class);
                    final Bundle bundle = new Bundle();

                    bundle.putString("origem", "editarUsuario");
                    bundle.putString("nome", usuario.getNome());
                    bundle.putString("email", usuario.getEmail());
                    bundle.putString("keyUsuario", usuario.getKeyUsuario());

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void abrirTelaLogin() {
        Intent abrirTelaLogin = new Intent(PerfilUsuarioActivity.this, LoginActivity.class);
        startActivity(abrirTelaLogin);
        finish();
    }

    private void abrirAlertdialogConfirmarExclusao() {
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.alert_excluir_personalizado);

        final Button btnConfirmar = dialog.findViewById(R.id.btn_confirmarExclusao);
        final Button btnCancelar = dialog.findViewById(R.id.btn_cancelarExclusao);
        final ImageView imgClose = dialog.findViewById(R.id.img_closeAlertDialog);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirContaDeslogar();
//                dialog.dismiss();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}












