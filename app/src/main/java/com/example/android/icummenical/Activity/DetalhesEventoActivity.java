package com.example.android.icummenical.Activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.android.icummenical.Helper.CommonActivity;
import com.example.android.icummenical.R;

public class DetalhesEventoActivity extends CommonActivity {

    private TextView tituloEvento, localEvento, dataEvento;
    private TextView horarioEvento, descricaoEvento, atividadesEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_detalhes);

        tituloEvento = findViewById(R.id.txt_detalheTituloEvento);
        localEvento = findViewById(R.id.txt_detalheLocalEvento);
        dataEvento = findViewById(R.id.txt_detalheDataEvento);
        horarioEvento = findViewById(R.id.txt_detalheHorarioEvento);
        descricaoEvento = findViewById(R.id.txt_detalheDescricaoEvento);
        atividadesEvento = findViewById(R.id.txt_detalheAtividadesEvento);

        get_TituloEvento();

    }

    private void get_TituloEvento() {
        if (getIntent().hasExtra("tituloEvento")) {
            String titulo = getIntent().getStringExtra("tituloEvento");
            String local = getIntent().getStringExtra("localEvento");
            String data = getIntent().getStringExtra("dataEvento");
            String horario = getIntent().getStringExtra("horarioEvento");
            String descricao = getIntent().getStringExtra("descricaoEvento");
            String atividades = getIntent().getStringExtra("atividadesEvento");

            tituloEvento.setText(titulo);
            localEvento.setText(local);
            dataEvento.setText(data);
            horarioEvento.setText(horario);
            descricaoEvento.setText(descricao);
            atividadesEvento.setText(atividades);
        }
    }
    
}
