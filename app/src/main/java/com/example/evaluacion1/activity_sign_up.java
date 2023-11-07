package com.example.evaluacion1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.widget.ProgressBar;

public class activity_sign_up extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, phoneEditText, passwordEditText;
    private Button signUpButton;
    private CheckBox checkBox;
    private TextView checkBoxText;
    private UserSTDataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new UserSTDataBaseHelper(this);

        usernameEditText = findViewById(R.id.usernameEditTextView);
        emailEditText = findViewById(R.id.emailEditTextView);
        phoneEditText = findViewById(R.id.txt_SignUp_Edit_Phone);
        passwordEditText = findViewById(R.id.txt_SignUp_Edit_Password);
        signUpButton = findViewById(R.id.btn_edit_user);
        checkBox = findViewById(R.id.checkBox);
        checkBoxText = findViewById(R.id.checkBox);

        SpannableString spannableString = new SpannableString(checkBoxText.getText());
        int startPos = checkBoxText.getText().toString().indexOf("términos y condiciones");
        int endPos = startPos + "términos y condiciones".length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(activity_sign_up.this, activity_EULA.class);
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan, startPos, endPos, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        checkBoxText.setText(spannableString);
        checkBoxText.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String username = charSequence.toString();
                if (username.length() < 5) {
                    usernameEditText.setError("El nombre de usuario debe tener al menos 5 caracteres.");
                } else {
                    usernameEditText.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = charSequence.toString();
                if (!email.endsWith("@alumnos.cst")) {
                    emailEditText.setError("El correo electrónico debe terminar en @alumnos.cst");
                } else {
                    emailEditText.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        EditText phoneEditText = findViewById(R.id.txt_SignUp_Edit_Phone);

        phoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (phoneEditText.getText().toString().isEmpty()) {
                        phoneEditText.setText("+56 9 ");
                        phoneEditText.setSelection(phoneEditText.getText().length());
                    }
                }
            }
        });

        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String phone = charSequence.toString().replaceAll("\\s", "");
                if (phone.length() != 12) {
                    phoneEditText.setError("Ingresa un número de celular válido.");
                } else {
                    phoneEditText.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = charSequence.toString();
                if (password.length() < 8) {
                    passwordEditText.setError("La contraseña debe tener al menos 8 caracteres.");
                } else {
                    passwordEditText.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidInput() && checkBox.isChecked()) {
                    if (isEmailAlreadyRegistered(emailEditText.getText().toString())) {
                        emailEditText.setError("El correo electrónico ya está registrado.");
                    } else {
                        ProgressBar progressBar = findViewById(R.id.progressBar);
                        progressBar.setVisibility(View.VISIBLE);

                        saveUserData();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(activity_sign_up.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }, 3000); // 1000 = 1s
                    }
                } else {
                    checkBox.setError("Debes aceptar los términos y condiciones para registrarte.");
                }
            }
        });
    }

    private boolean isValidInput() {
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean isUsernameValid = username.length() >= 5;
        boolean isEmailValid = email.endsWith("@alumnos.cst");
        boolean isPhoneValid = phone.startsWith("+56 9 ") && phone.replaceAll("\\s", "").length() == 12;
        boolean isPasswordValid = password.length() >= 8;

        return isUsernameValid && isEmailValid && isPhoneValid && isPasswordValid;
    }

    private boolean isEmailAlreadyRegistered(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + UserSTDataBaseHelper.TABLE_NAME + " WHERE " + UserSTDataBaseHelper.COLUMN_EMAIL + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean emailExists = cursor.getCount() > 0;
        cursor.close();
        return emailExists;
    }

    private void saveUserData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserSTDataBaseHelper.COLUMN_USERNAME, usernameEditText.getText().toString());
        values.put(UserSTDataBaseHelper.COLUMN_EMAIL, emailEditText.getText().toString());
        values.put(UserSTDataBaseHelper.COLUMN_PHONE, phoneEditText.getText().toString());
        values.put(UserSTDataBaseHelper.COLUMN_PASSWORD, passwordEditText.getText().toString());

        long newRowId = db.insert(UserSTDataBaseHelper.TABLE_NAME, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
        }
    }
}
