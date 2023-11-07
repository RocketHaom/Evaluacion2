package com.example.evaluacion1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class activity_menu extends AppCompatActivity {

    private Button btnGoToSedes;
    private Button btn_go_to_CRUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnGoToSedes = findViewById(R.id.btn_go_to_SEDES);
        btn_go_to_CRUD = findViewById(R.id.btn_go_to_CRUD);

        btnGoToSedes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_menu.this, activity_SEDE.class);
                startActivity(intent);
            }
        });

        btn_go_to_CRUD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_menu.this, activity_user_editor.class);
                startActivity(intent);
            }
        });
    }
}