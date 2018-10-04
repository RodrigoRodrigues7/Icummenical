package com.example.android.icummenical.Classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.icummenical.DAO.ConfigFirebase;
import com.example.android.icummenical.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {

    private Context context;
    private List<Evento> mListEvento;
    private List<Evento> eventos;
    private DatabaseReference databaseReference;
    private Evento todosEventos;

    public Adapter(Context context, List<Evento> mData) {
        this.context = context;
        this.mListEvento = mData;
    }

    @NonNull
    @Override
    public Adapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item, viewGroup, false);
        return new Adapter.myViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter.myViewHolder holder, int position) {

        final Evento eventoItem = mListEvento.get(position);
        eventos = new ArrayList<>();

        databaseReference = ConfigFirebase.getDatabaseReference();
        databaseReference.child("Eventos").orderByChild("keyEvento").equalTo(eventoItem.getKeyEvento()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventos.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    todosEventos = postSnapshot.getValue(Evento.class);
                    eventos.add(todosEventos);

                    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

                    final int height = (displayMetrics.heightPixels / 4);
                    final int width = (displayMetrics.widthPixels / 2);

                    Picasso.with(context).load(todosEventos.getBackground()).resize(width, height).centerCrop().into(holder.imgBackgroundCardEvento);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.txtTituloEvento.setText(eventoItem.getTitulo());
        holder.txtLocalEvento.setText(eventoItem.getLocal());
        holder.txtHorarioEvento.setText(eventoItem.getHorario());
        holder.txtDataEvento.setText(eventoItem.getData());

    }

    @Override
    public int getItemCount() {
        return mListEvento.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        protected ImageView imgBackgroundCardEvento;
        protected TextView txtTituloEvento, txtLocalEvento, txtHorarioEvento, txtDataEvento;
        protected Button btnDetalhesEvento;

        public myViewHolder(View itemView) {
            super(itemView);

            imgBackgroundCardEvento = itemView.findViewById(R.id.img_backgroundCardEvento);
            txtTituloEvento = itemView.findViewById(R.id.txt_tituloCardEvento);
            txtLocalEvento = itemView.findViewById(R.id.txt_localCardEvento);

            txtHorarioEvento = itemView.findViewById(R.id.txt_horarioCardEvento);
            txtDataEvento = itemView.findViewById(R.id.txt_dataCardEvento);
            btnDetalhesEvento = itemView.findViewById(R.id.btn_detalhesCardEvento);

        }
    }

}
