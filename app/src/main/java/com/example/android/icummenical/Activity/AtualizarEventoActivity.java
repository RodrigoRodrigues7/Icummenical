package com.example.android.icummenical.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.Calendar;

public class AtualizarEventoActivity extends CommonActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final int GALLERY_CODE = 2111;
    private ImageView imgFotoEvento, imgDatePicker, imgTimePicker;

    private EditText edtTitulo, edtData, edtHorario, edtLocal, edtDescricao, edtAtividades;
    private Button btnSalvarEvento, btnVoltarMenu;
    private String titulo, keyEvento;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_evento);

        databaseReference = ConfigFirebase.getDatabaseReference();
        storageReference = ConfigFirebase.getStorageReference();

        imgFotoEvento = findViewById(R.id.img_fotoAtualizarEvento);
        imgDatePicker = findViewById(R.id.imgView_datePickerAtualizar);
        imgTimePicker = findViewById(R.id.imgView_timePickerAtualizar);

        btnSalvarEvento = findViewById(R.id.btn_salvarEvento);
        btnVoltarMenu = findViewById(R.id.btn_voltarMenu);

        edtTitulo = findViewById(R.id.edt_tituloAtualizarEvento);
        edtData = findViewById(R.id.edt_dataAtualizarEvento);
        edtHorario = findViewById(R.id.edt_horarioAtualizarEvento);
        edtLocal = findViewById(R.id.edt_localAtualizarEvento);
        edtDescricao = findViewById(R.id.edt_descricaoAtualizarEvento);
        edtAtividades = findViewById(R.id.edt_atividadeAtualizarEvento);

        carregarDadosEvento();
        carregarFotoOriginalEvento();

        btnSalvarEvento.setOnClickListener(new View.OnClickListener() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            Uri uriTarget = data.getData();
            Bitmap bitmap;

            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriTarget));
                imgFotoEvento.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
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

    protected void carregarFotoOriginalEvento() {

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
                            Picasso.with(AtualizarEventoActivity.this).load(uri.toString()).resize(width, height).centerCrop().into(imgFotoEvento);

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

    private void atualizarEvento() {

        final String titulo = edtTitulo.getText().toString().trim();
        final String data = edtData.getText().toString().trim();
        final String horario = edtHorario.getText().toString().trim();
        final String local = edtLocal.getText().toString().trim();
        final String descricao = edtDescricao.getText().toString().trim();
        final String atividades = edtAtividades.getText().toString().trim();

        if (TextUtils.isEmpty(titulo)) {
            edtTitulo.setError("Informe o Titulo");
        }
        if (TextUtils.isEmpty(data)) {
            edtData.setError("Informe a Data");
        }
        if (TextUtils.isEmpty(horario)) {
            edtData.setError("Informe o Horario");
        }
        if (TextUtils.isEmpty(local)) {
            edtData.setError("Informe o Local");
        }
        if (TextUtils.isEmpty(descricao)) {
            edtData.setError("Informe a Descrição");
        }
        if (TextUtils.isEmpty(atividades)) {
            edtData.setError("Informe as Atividades");
        } else {

            updateEvento(titulo, data, horario, local, descricao, atividades, keyEvento);
            showToast("Evento Atualizado!");
            voltarMenuPrincipal();

        }

    }

    private boolean updateEvento(String titulo, String data, String horario, String local, String descricao, String atividades, String key_Evento) {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Eventos").child(key_Evento);

        Evento evento = new Evento(titulo, data, horario, local, descricao, atividades, key_Evento);
        dR.setValue(evento);
        salvarFoto();
        return true;
    }

    private void selecionarFotoEvento() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_CODE);
    }

    private void salvarFoto() {

        StorageReference imageReference = storageReference.child("fotoEvento-" + titulo + "/" + titulo + ".jpg");

        imgFotoEvento.setDrawingCacheEnabled(true);
        imgFotoEvento.buildDrawingCache();

        Bitmap bitmap = imgFotoEvento.getDrawingCache();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        byte[] data = outputStream.toByteArray();
        UploadTask uploadTask = imageReference.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
//                carregarFotoEvento();
            }
        });

    }

    private void voltarMenuPrincipal() {
        Intent voltarMenu = new Intent(AtualizarEventoActivity.this, PrincipalActivity.class);
        startActivity(voltarMenu);
        finish();
    }

    private void carregarDadosEvento() {

        String origem, local, data, horario, descricao, atividades;

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
            keyEvento = bundle.getString("keyEvento");

            edtTitulo.setText(titulo);
            edtData.setText(data);
            edtHorario.setText(horario);
            edtLocal.setText(local);
            edtDescricao.setText(descricao);
            edtAtividades.setText(atividades);
        }
    }

}
