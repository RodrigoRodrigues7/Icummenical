package com.example.android.icummenical.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.icummenical.Helper.CommonActivity;
import com.example.android.icummenical.R;

public class DetalhesEventoActivity extends CommonActivity {

    private TextView tituloEvento, localEvento, dataEvento;
    private TextView horarioEvento, descricaoEvento, atividadesEvento;
    private Button btnVoltarMenu, btnAtualizarEvento;

    private String origem, titulo, local, data, horario, descricao, atividades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_detalhes);

        btnAtualizarEvento = findViewById(R.id.btn_atualizarEvento);
        btnVoltarMenu = findViewById(R.id.btn_voltarMenuPrincipal);

        tituloEvento = findViewById(R.id.txt_detalheTituloEvento);
        localEvento = findViewById(R.id.txt_detalheLocalEvento);
        dataEvento = findViewById(R.id.txt_detalheDataEvento);
        horarioEvento = findViewById(R.id.txt_detalheHorarioEvento);
        descricaoEvento = findViewById(R.id.txt_detalheDescricaoEvento);
        atividadesEvento = findViewById(R.id.txt_detalheAtividadesEvento);

        carregarDadosEvento();

        btnAtualizarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarEvento();
            }
        });

        btnVoltarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarMenuPrincipal();
            }
        });

    }

//--------------------------------------------------------------------------------------------------

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

        atualizarEvento.putExtras(bundle);
        startActivity(atualizarEvento);
        finish();
    }

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

            tituloEvento.setText(titulo);
            localEvento.setText(local);
            dataEvento.setText(data);
            horarioEvento.setText(horario);
            descricaoEvento.setText(descricao);
            atividadesEvento.setText(atividades);
        }
    }

    private void voltarMenuPrincipal() {
        Intent menuPrincipal = new Intent(DetalhesEventoActivity.this, PrincipalActivity.class);
        startActivity(menuPrincipal);
        finish();
    }

}
