package com.example.evaluacion1;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class activity_user_editor extends AppCompatActivity {
    private ListView userListView;
    private UserAdapter userAdapter;
    private UserDataManager userDataManager;
    private Dialog addUserDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_editor);

        userListView = findViewById(R.id.userListView);
        userDataManager = new UserDataManager(this);

        Cursor userDataCursor = userDataManager.getAllUsers();

        userAdapter = new UserAdapter(this, userDataCursor);
        userListView.setAdapter(userAdapter);

        Button addNewUserButton = findViewById(R.id.btn_add_new_user);
        addNewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddUserDialog();
            }
        });
    }

    private void showAddUserDialog() {
        addUserDialog = new Dialog(this);
        addUserDialog.setContentView(R.layout.agregar);

        Button addUserButton = addUserDialog.findViewById(R.id.btn_add_user);
        Button cancelButton = addUserDialog.findViewById(R.id.btn_cancel);

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameEditText = addUserDialog.findViewById(R.id.usernameEditTextView);
                EditText emailEditText = addUserDialog.findViewById(R.id.emailEditTextView);
                EditText phoneEditText = addUserDialog.findViewById(R.id.txt_SignUp_Edit_Phone);
                EditText passwordEditText = addUserDialog.findViewById(R.id.txt_SignUp_Edit_Password);

                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                long result = userDataManager.insertUser(username, email, phone, password);

                if (result != -1) {
                    addUserDialog.dismiss();
                } else {
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserDialog.dismiss();
            }
        });

        addUserDialog.show();
    }
}