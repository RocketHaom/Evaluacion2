package com.example.evaluacion1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button logInButton;
    private UserSTDataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new UserSTDataBaseHelper(this);

        emailEditText = findViewById(R.id.txt_LogIn_Email);
        passwordEditText = findViewById(R.id.txt_LogIn_Password);
        logInButton = findViewById(R.id.btn_Log_In);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidInput()) {
                    if (isUserRegistered()) {
                        if (isPasswordCorrect()) {
                            Intent intent = new Intent(MainActivity.this, activity_menu.class);
                            startActivity(intent);
                        } else {
                            passwordEditText.setError("Contraseña incorrecta");
                        }
                    } else {
                        emailEditText.setError("Correo no registrado");
                    }
                }
            }
        });
    }

    private boolean isValidInput() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isUserRegistered() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + UserSTDataBaseHelper.TABLE_NAME + " WHERE " + UserSTDataBaseHelper.COLUMN_EMAIL + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{emailEditText.getText().toString()});
        boolean userExists = cursor.getCount() > 0;
        cursor.close();
        return userExists;
    }

    private boolean isPasswordCorrect() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + UserSTDataBaseHelper.COLUMN_PASSWORD + " FROM " + UserSTDataBaseHelper.TABLE_NAME +
                " WHERE " + UserSTDataBaseHelper.COLUMN_EMAIL + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{emailEditText.getText().toString()});
        if (cursor.moveToFirst()) {
            String savedPassword = cursor.getString(cursor.getColumnIndex(UserSTDataBaseHelper.COLUMN_PASSWORD));
            cursor.close();
            return passwordEditText.getText().toString().equals(savedPassword);
        }
        return false;
    }

    public void goToActivitySignUp(View view) {
        Intent intent = new Intent(this, activity_sign_up.class);
        startActivity(intent);
    }
}
