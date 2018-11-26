package com.example.android.icummenical.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.icummenical.DAO.ConfigFirebase;
import com.example.android.icummenical.Helper.CommonActivity;
import com.example.android.icummenical.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DetalhesEventoActivity extends CommonActivity {

    private TextView tituloEvento, localEvento, dataEvento;
    private TextView horarioEvento, descricaoEvento, atividadesEvento;
    private Button btnVoltarMenu, btnAtualizarEvento, btnExcluirEvento;
    private ImageView imgFotoEvento;

    private String origem, titulo, local, data, horario, descricao, atividades, keyEvento;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_detalhes);

        btnAtualizarEvento = findViewById(R.id.btn_atualizarEvento);
        btnVoltarMenu = findViewById(R.id.btn_voltarMenuPrincipal);
        btnExcluirEvento = findViewById(R.id.btn_excluirEvento);
        imgFotoEvento = findViewById(R.id.imgDetalheEvento);

        tituloEvento = findViewById(R.id.txt_detalheTituloEvento);
        localEvento = findViewById(R.id.txt_detalheLocalEvento);
        dataEvento = findViewById(R.id.txt_detalheDataEvento);
        horarioEvento = findViewById(R.id.txt_detalheHorarioEvento);
        descricaoEvento = findViewById(R.id.txt_detalheDescricaoEvento);
        atividadesEvento = findViewById(R.id.txt_detalheAtividadesEvento);

        carregarDadosEvento();
        carregarFotoEvento();

        btnAtualizarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarEvento();

            }
        });

        btnExcluirEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirAlertDialog();
            }
        });

        btnVoltarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarMenuPrincipal();
            }
        });

    }

    protected void carregarFotoEvento() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReferenceFromUrl("gs://icummenical.appspot.com/fotoEvento-" + titulo + "/" + titulo + ".jpg");

        databaseReference = ConfigFirebase.getDatabaseReference();
        databaseReference.child("Eventos").orderByChild("keyEvento").equalTo(keyEvento).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final int height = 300;
                    final int width = 300;
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(DetalhesEventoActivity.this).load(uri.toString()).resize(width, height).centerCrop().into(imgFotoEvento);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ERROR_LOAD_PHOTO", "------------------------> Erro ao Carregar Foto <------------------------");

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

    private void abrirAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(DetalhesEventoActivity.this);

        builder.setMessage("Você quer Mesmo Excluir esse Evento?").setCancelable(false)
                .setPositiveButton("Sim, Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        excluirEvento();
                        excluirFotoEvento();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setTitle("Excluir Evento:");
        dialog.show();

    }

    private void excluirEvento() {

        databaseReference = ConfigFirebase.getDatabaseReference();
        Query excluirQuery = databaseReference.child("Eventos").orderByChild("keyEvento").equalTo(keyEvento);

        excluirQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    postSnapshot.getRef().removeValue();
                    showToastShort("Evento Removido com Sucesso!");
                    voltarMenuPrincipal();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void excluirFotoEvento() {

        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://icummenical.appspot.com");
        StorageReference photoReference = storageReference.child("fotoEvento-" + tituloEvento.getText().toString() + "/" + tituloEvento.getText().toString() + ".jpg");

        photoReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showToastShort("Foto do Evento Removida!");
            }
        });

    }

    private void atualizarEvento() {
        Intent atualizarEvento = new Intent(DetalhesEventoActivity.this, AtualizarEventoActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString("atualizarEvento", "atualizarEvento");
        bundle.putString("tituloEvento", tituloEvento.getText().toString());
        bundle.putString("dataEvento", dataEvento.getText().toString());
        bundle.putString("horarioEvento", horarioEvento.getText().toString());
        bundle.putString("localEvento", localEvento.getText().toString());
        bundle.putString("descricaoEvento", descricaoEvento.getText().toString());
        bundle.putString("atividadesEvento", atividadesEvento.getText().toString());
        bundle.putString("keyEvento", keyEvento);

        atualizarEvento.putExtras(bundle);
        startActivity(atualizarEvento);
        finish();
    }

//--------------------------------------------------------------------------------------------------

    private void carregarDadosEvento() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        origem = bundle.getString("detalhesEvento");

        if (origem.equals("editarEvento")) {
            titulo = bundle.getString("tituloEvento");
            local = bundle.getString("localEvento");
            data = bundle.getString("dataEvento");
            horario = bundle.getString("horarioEvento");
            descricao = bundle.getString("descricaoEvento");
            atividades = bundle.getString("atividadesEvento");
            keyEvento = bundle.getString("keyEvento");

            tituloEvento.setText(titulo);
            localEvento.setText(local);
            dataEvento.setText(data);
            horarioEvento.setText(horario);
            descricaoEvento.setText(descricao);
            atividadesEvento.setText(atividades);
        }
    }

    private void voltarMenuPrincipal() {
        Intent menuPrincipal = new Intent(DetalhesEventoActivity.this, ListaEventoActivity.class);
        startActivity(menuPrincipal);
        finish();
    }

}
