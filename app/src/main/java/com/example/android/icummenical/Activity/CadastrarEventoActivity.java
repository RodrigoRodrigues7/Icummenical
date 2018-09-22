package com.example.android.icummenical.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.example.android.icummenical.Classes.DatePickerFragment;
import com.example.android.icummenical.Classes.Evento;
import com.example.android.icummenical.Classes.TimePickerFragment;
import com.example.android.icummenical.DAO.ConfigFirebase;
import com.example.android.icummenical.Helper.CommonActivity;
import com.example.android.icummenical.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class CadastrarEventoActivity extends CommonActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private ImageView imgDatePicker, imgTimePicker;
    private EditText edtTitulo, edtData, edtHorario, edtLocal, edtDescricao, edtAtividade;
    private Button btnRegistrarEvento;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mdbReference;
    private Evento evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_evento);

        mdbReference = FirebaseDatabase.getInstance().getReference();

        imgDatePicker = findViewById(R.id.imgView_datePicker);
        imgTimePicker = findViewById(R.id.imgView_timePicker);

        edtTitulo = findViewById(R.id.edt_tituloEvento);
        edtData = findViewById(R.id.edt_dataEvento);
        edtHorario = findViewById(R.id.edt_horarioEvento);
        edtLocal = findViewById(R.id.edt_localEvento);
        edtDescricao = findViewById(R.id.edt_descricaoEvento);
        edtAtividade = findViewById(R.id.edt_atividadeEvento);

        btnRegistrarEvento = findViewById(R.id.btn_registrarEvento);

        btnRegistrarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (verificarCampos() == false) {

                        showToast("Preencha Todos os Campos!");

                    } else {
                        registrarEvento();
                    }

                } catch (Exception error) {
                    showToast("Erro: " + error.getMessage());
                }

            }
        });


        imgDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialog = new DatePickerFragment();
                dialog.show(getSupportFragmentManager(), "Date Picker");

            }
        });

        imgTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialog = new TimePickerFragment();
                dialog.show(getSupportFragmentManager(), "TimePicker");

            }
        });

    }


//------------------------------ REFERENTE AO DATE E TIME PICKER'S ---------------------------------

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

        edtHorario.setText(hourOfDay + ":" + minute);
    }

//--------------------------------------------------------------------------------------------------

    private void registrarEvento() {

        evento = new Evento();
        evento.setTitulo(edtTitulo.getText().toString());
        evento.setData(edtData.getText().toString());
        evento.setHorario(edtHorario.getText().toString());
        evento.setLocal(edtLocal.getText().toString());
        evento.setDescricao(edtDescricao.getText().toString());
        evento.setAtividades(edtAtividade.getText().toString());

        insertEvento(evento);
    }

    private boolean insertEvento(Evento evento) {

        try {

            mdbReference = ConfigFirebase.getDatabaseReference().child("Eventos");
            mdbReference.push().setValue(evento);
            showToast("Evento Registrado com Sucesso!");
            return true;

        } catch (Exception e) {

            showToast("Errro ao Registrar o Evento!");
            e.printStackTrace();
            return false;
        }

    }

    private boolean verificarCampos() {

        if (edtTitulo.getText().toString().trim().equals("")) {
            showToast("Preencha o Campo do Título!");
            return false;
        } else if (edtData.getText().toString().trim().equals("") && edtHorario.getText().toString().trim().equals("")) {
            showToast("Preencha o Campo da Data e Horario!");
            return false;
        } else if (edtLocal.getText().toString().trim().equals("")) {
            showToast("Preencha o Campo do Local!");
            return false;
        } else if (edtDescricao.getText().toString().trim().equals("")) {
            showToast("Preencha o Campo da Descrição!");
            return false;
        } else if (edtAtividade.getText().toString().trim().equals("")) {
            showToast("Preencha o Campo das Atividades!");
            return false;
        }

        return true;
    }

}


//    public void salvarEvento_1(View view) {
//
//        Evento evento = new Evento("Primeiro Evento do Icummenical",
//                "25 de Setembro de 2018",
//                "8:45",
//                "Faculdade Ibratec",
//                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed interdum augue. Cras id porttitor orci. Praesent at scelerisque erat. In aliquet semper arcu id condimentum.",
//                "Apresentar, Discurtir, Debater, Conclusão");
//        mdbReference.child(evento.getTitulo()).setValue(evento);
//    }





