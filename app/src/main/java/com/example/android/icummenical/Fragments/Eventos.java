package com.example.android.icummenical.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.icummenical.Classes.Adapter;
import com.example.android.icummenical.Classes.Evento;
import com.example.android.icummenical.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;


public class Eventos extends Fragment {

    private RecyclerView eventosList;
    private Adapter adapter;
    private List<Evento> mListEventos;

    private DatabaseReference databaseReference;
    private Evento todosEventos;

    public Eventos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eventos, container, false);
eventosList =(RecyclerView)view.findViewById(R.id.enventoListId);
eventosList.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }


}
