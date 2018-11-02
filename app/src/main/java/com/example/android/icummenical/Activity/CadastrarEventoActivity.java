package com.example.android.icummenical.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.Calendar;

public class CadastrarEventoActivity extends CommonActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final int GALLERY_CODE = 2444;
    private ImageView imgFotoEvento, imgDatePicker, imgTimePicker;
    private EditText edtTitulo, edtData, edtHorario, edtLocal, edtDescricao, edtAtividades;
    private Button btnRegistrarEvento, btnMenuPrincipal;

    private Evento evento;
    private String tituloEvento;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseUser usuarioAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_evento);

        databaseReference = ConfigFirebase.getDatabaseReference();
        storageReference = ConfigFirebase.getStorageReference();
        mAuth = ConfigFirebase.getFirebaseAuth();
        usuarioAtual = mAuth.getCurrentUser();

        imgFotoEvento = findViewById(R.id.img_FotoEvento);
        imgDatePicker = findViewById(R.id.imgView_datePickerAtualizar);
        imgTimePicker = findViewById(R.id.imgView_timePickerAtualizar);

        edtTitulo = findViewById(R.id.edt_tituloEvento);
        edtData = findViewById(R.id.edt_dataAtualizarEvento);
        edtHorario = findViewById(R.id.edt_horarioEvento);
        edtLocal = findViewById(R.id.edt_localEvento);
        edtDescricao = findViewById(R.id.edt_descricaoEvento);
        edtAtividades = findViewById(R.id.edt_atividadeEvento);

        btnRegistrarEvento = findViewById(R.id.btn_salvarEvento);
        btnMenuPrincipal = findViewById(R.id.btn_voltarMenuPrincipal);


        imgFotoEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarFotoEvento();
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

        btnMenuPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMenuPrincipal();
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

//        final int width = 300;
//        final int height = 300;
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == GALLERY_CODE) {
//                Uri imagemSelecionada = data.getData();
//                Picasso.with(CadastrarEventoActivity.this).load(imagemSelecionada.toString()).resize(width, height).centerCrop().into(imgFotoEvento);
//            }
//        }

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

    private void abrirMenuPrincipal() {
        Intent menuPrincipal = new Intent(CadastrarEventoActivity.this, PrincipalActivity.class);
        startActivity(menuPrincipal);
        finish();
    }

    private void selecionarFotoEvento() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_CODE);
    }

    private void carregarFotoEvento() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReferenceFromUrl("gs://icummenical.appspot.com/fotoEvento-" + tituloEvento + "/" + tituloEvento + ".jpg");

        final int width = 300;
        final int height = 300;

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(CadastrarEventoActivity.this).load(uri.toString()).resize(width, height).centerCrop().into(imgFotoEvento);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast("Imagem Não Encontrada");
            }
        });

    }

//--------------------------------------------------------------------------------------------------

    private void salvarFoto() {

        tituloEvento = evento.getTitulo();
        StorageReference imageReference = storageReference.child("fotoEvento-" + tituloEvento + "/" + tituloEvento + ".jpg");

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
                carregarFotoEvento();
            }
        });

    }

    private void registrarEvento() {

        evento = new Evento();
        evento.setTitulo(edtTitulo.getText().toString());
        evento.setData(edtData.getText().toString());
        evento.setHorario(edtHorario.getText().toString());
        evento.setLocal(edtLocal.getText().toString());
        evento.setDescricao(edtDescricao.getText().toString());
        evento.setAtividades(edtAtividades.getText().toString());
        evento.setUid(usuarioAtual.getUid());

        salvarFoto();
        insertEvento(evento);
        abrirMenuPrincipal();

    }

    private boolean insertEvento(Evento evento) {

        try {
            databaseReference = ConfigFirebase.getDatabaseReference().child("Eventos");
            String key = databaseReference.push().getKey();
            evento.setKeyEvento(key);

            databaseReference.child(key).setValue(evento);
//            databaseReference.push().setValue(evento);
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
        } else if (edtData.getText().toString().trim().equals("") || edtHorario.getText().toString().trim().equals("")) {
            showToast("Preencha os Campos de Data e Horario!");
            return false;
        } else if (edtLocal.getText().toString().trim().equals("")) {
            showToast("Preencha o Campo do Local!");
            return false;
        } else if (edtDescricao.getText().toString().trim().equals("")) {
            showToast("Preencha o Campo da Descrição!");
            return false;
        } else if (edtAtividades.getText().toString().trim().equals("")) {
            showToast("Preencha o Campo das Atividades!");
            return false;
        }

        return true;
    }

}
