package com.example.android.icummenical.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.example.android.icummenical.Classes.DatePickerFragment;
import com.example.android.icummenical.Classes.TimePickerFragment;
import com.example.android.icummenical.DAO.ConfigFirebase;
import com.example.android.icummenical.Helper.CommonActivity;
import com.example.android.icummenical.R;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.util.Calendar;

public class AtualizarEventoActivity extends CommonActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final int GALLERY_CODE = 2111;
    private ImageView imgFotoEvento, imgDatePicker, imgTimePicker;

    private EditText edtTitulo, edtData, edtHorario, edtLocal, edtDescricao, edtAtividades;
    private Button btnSalvarEvento, btnVoltarLista;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_evento);

        databaseReference = ConfigFirebase.getDatabaseReference();

        imgFotoEvento = findViewById(R.id.img_fotoAtualizarEvento);
        imgDatePicker = findViewById(R.id.imgView_datePickerAtualizar);
        imgTimePicker = findViewById(R.id.imgView_timePickerAtualizar);

        btnSalvarEvento = findViewById(R.id.btn_salvarEvento);
        btnVoltarLista = findViewById(R.id.btn_voltarListaEventos);

        edtTitulo = findViewById(R.id.edt_tituloAtualizarEvento);
        edtData = findViewById(R.id.edt_dataAtualizarEvento);
        edtHorario = findViewById(R.id.edt_horarioAtualizarEvento);
        edtLocal = findViewById(R.id.edt_localAtualizarEvento);
        edtDescricao = findViewById(R.id.edt_descricaoAtualizarEvento);
        edtAtividades = findViewById(R.id.edt_atividadeAtualizarEvento);

        carregarDadosEvento();

        btnSalvarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarEvento();
            }
        });

        btnVoltarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarListaEventos();
            }
        });

        imgFotoEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarFotoEvento();
            }
        });
        imgDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDateDialog();
            }
        });
        imgTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTimeDialog();
            }
        });

    }

//--------------------------------------------------------------------------------------------------

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.YEAR, year);
        calendar.set(calendar.MONTH, month);
        calendar.set(calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        edtData.setText(currentDate);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        edtHorario.setText(String.format("%02d:%02d", hourOfDay, minute));
    }

    private void abrirDateDialog() {
        DialogFragment dialog = new DatePickerFragment();
        dialog.show(getSupportFragmentManager(), "Date Picker");
    }

    private void abrirTimeDialog() {
        DialogFragment dialog = new TimePickerFragment();
        dialog.show(getSupportFragmentManager(), "TimePicker");
    }

//--------------------------------------------------------------------------------------------------

    private void atualizarEvento() {

//        databaseReference.child("Eventos");

    }

    private void selecionarFotoEvento() {
        Intent abrirGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(abrirGaleria, "Selecione uma Imagen: "), GALLERY_CODE);
    }

    private void voltarListaEventos() {
        Intent voltarLista = new Intent(AtualizarEventoActivity.this, EventoActivity.class);
        startActivity(voltarLista);
        finish();
    }

    private void carregarDadosEvento() {

        String origem, titulo, local, data, horario, descricao, atividades;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        origem = bundle.getString("atualizarEvento");

        if (origem.equals("atualizarEvento")) {
            titulo = bundle.getString("tituloEvento");
            local = bundle.getString("localEvento");
            data = bundle.getString("dataEvento");
            horario = bundle.getString("horarioEvento");
            descricao = bundle.getString("descricaoEvento");
            atividades = bundle.getString("atividadesEvento");

            edtTitulo.setText(titulo);
            edtData.setText(data);
            edtHorario.setText(horario);
            edtLocal.setText(local);
            edtDescricao.setText(descricao);
            edtAtividades.setText(atividades);
        }
    }

}
