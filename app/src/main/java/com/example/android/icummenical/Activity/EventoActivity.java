package com.example.android.icummenical.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.icummenical.Classes.Adapter;
import com.example.android.icummenical.Classes.Evento;
import com.example.android.icummenical.DAO.ConfigFirebase;
import com.example.android.icummenical.Helper.CommonActivity;
import com.example.android.icummenical.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventoActivity extends CommonActivity {

    private RecyclerView mRecyclerView;
    private Adapter adapter;
    private List<Evento> mListEventos;

    private DatabaseReference databaseReference;
    private Evento todosEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        mRecyclerView = findViewById(R.id.recyclerView_listaEventos);
        databaseReference = ConfigFirebase.getDatabaseReference();

        carregarRecyclerView();

    }

    private void carregarRecyclerView() {

        try {

            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            mListEventos = new ArrayList<>();
            databaseReference.child("Eventos").orderByChild("keyEvento").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        todosEventos = postSnapshot.getValue(Evento.class);
                        mListEventos.add(todosEventos);

                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            adapter = new Adapter(this, mListEventos);
            mRecyclerView.setAdapter(adapter);

        } catch (Exception e) {
            Log.d("ERRO_LISTA_EVENTOS", "-------------------------> Erro ao Exibir Lista <-------------------------");
            showToastShort("Erro: " + e.getMessage());
        }

    }



}
