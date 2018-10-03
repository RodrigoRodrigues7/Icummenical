package com.example.android.icummenical.Activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.example.android.icummenical.Classes.Adapter;
import com.example.android.icummenical.Classes.cardItem;
import com.example.android.icummenical.Helper.CommonActivity;
import com.example.android.icummenical.R;

import java.util.ArrayList;
import java.util.List;

public class EventoActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

    }





    //    private void listaEventos() {
//        // Setting the status bar background to transparent
//        Window w = getWindow();
//        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//
//        //Setting recyclerview with the Adapter
//        RecyclerView recyclerView = findViewById(R.id.recyclerViewList);
//
//        List<cardItem> mCardList = new ArrayList<>();
//        mCardList.add(new cardItem(R.drawable.img_background1, "Forests", R.drawable.profile_pic1));
//        mCardList.add(new cardItem(R.drawable.img_background2, "Lighting's", R.drawable.profile_pic3));
//        mCardList.add(new cardItem(R.drawable.img_background3, "Lighting's and Cities", R.drawable.profile_pic4));
//        mCardList.add(new cardItem(R.drawable.img_background1, "Forests", R.drawable.profile_pic1));
//        mCardList.add(new cardItem(R.drawable.img_background2, "Lighting's", R.drawable.profile_pic3));
//        mCardList.add(new cardItem(R.drawable.img_background3, "Lighting's and Cities", R.drawable.profile_pic4));
//
//        Adapter adapter = new Adapter(this, mCardList);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//    }
}
