package com.example.android.icummenical.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.icummenical.Helper.CommonActivity;
import com.example.android.icummenical.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class PrincipalActivity extends CommonActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView nomeUsuario, emailUsuario;
    private ImageView imgUsuario;
    private String emailUsuarioLogado;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        emailUsuarioLogado = mAuth.getCurrentUser().getEmail();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios");

        nomeUsuario = findViewById(R.id.txt_nomeUsuarioHeader);
        emailUsuario = findViewById(R.id.txt_emailUsuarioHeader);
        imgUsuario = findViewById(R.id.img_fotoUsuarioHeader);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

//--------------------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_SignOut) {
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    // Método que controla os clicks no 'Navigation Drawer'
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_mapa) {
            Intent i = new Intent(PrincipalActivity.this, MapsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_listarEventos) {
            Intent i = new Intent(PrincipalActivity.this, EventoActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_cadastrarEvento) {
            Intent i = new Intent(PrincipalActivity.this, CadastrarEventoActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_abrirPerfilUsuario) {
            Intent i = new Intent(PrincipalActivity.this, PerfilUsuarioActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Método para deslogar do aplicativo
    public void signOut() {

        mAuth.signOut();

       // Intent intent = new Intent(PrincipalActivity.this, LoginActivity.class);
        //startActivity(intent);
       // finish();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setTitle("iCummenical");
        builder.setMessage("Deseja sair?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent intent = new Intent(PrincipalActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Não",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
            }
        });

        AlertDialog alerta = builder.create();
        builder.show();


}

    //Método para Carregar Imagem do Usuário
//    private void carregarFotoPadrao() {
//
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        final StorageReference storageReference = storage.getReferenceFromUrl("gs://icummenical.appspot.com/fotoPerfilUsuario-" + emailUsuarioLogado + "/" + emailUsuarioLogado + ".jpg");
//
//        final int width = 300;
//        final int height = 300;
//
//        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.with(PrincipalActivity.this).load(uri.toString()).resize(width, height).centerCrop().into(imgUsuario);
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                showToast("Imagem Não Encontrada");
//            }
//        });
//
//
//    }

}
