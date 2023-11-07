package com.example.evaluacion1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;
    private UserDataManager dataManager;

    public UserAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        dataManager = new UserDataManager(context);
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        cursor.moveToPosition(position);
        return cursor.getLong(cursor.getColumnIndex(UserSTDataBaseHelper.COLUMN_ID));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item, null);
        }

        TextView usernameTextView = convertView.findViewById(R.id.usernameEditTextView);
        TextView emailTextView = convertView.findViewById(R.id.emailEditTextView);
        Button deleteButton = convertView.findViewById(R.id.btn_delete);

        cursor.moveToPosition(position);
        final int userId = cursor.getInt(cursor.getColumnIndex(UserSTDataBaseHelper.COLUMN_ID));
        String username = cursor.getString(cursor.getColumnIndex(UserSTDataBaseHelper.COLUMN_USERNAME));
        String email = cursor.getString(cursor.getColumnIndex(UserSTDataBaseHelper.COLUMN_EMAIL));

        usernameTextView.setText(username);
        emailTextView.setText(email);
        Button editButton = convertView.findViewById(R.id.btn_edit);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí debes mostrar el cuadro de diálogo de edición
                showEditUserDialog(userId);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("¿Seguro que quieres eliminar este usuario?");
                alertDialogBuilder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataManager.deleteUser(userId);
                        cursor.requery();
                        notifyDataSetChanged();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        return convertView;
    }

    private void showEditUserDialog(int userId) {
        // Crea el cuadro de diálogo de edición similar al de agregar
        final Dialog editUserDialog = new Dialog(context);
        editUserDialog.setContentView(R.layout.editar);

        // Obtén referencias a las vistas dentro del cuadro de diálogo
        EditText usernameEditText = editUserDialog.findViewById(R.id.usernameEditTextView);
        EditText emailEditText = editUserDialog.findViewById(R.id.emailEditTextView);
        EditText phoneEditText = editUserDialog.findViewById(R.id.txt_SignUp_Edit_Phone);
        EditText passwordEditText = editUserDialog.findViewById(R.id.txt_SignUp_Edit_Password);
        Button saveButton = editUserDialog.findViewById(R.id.btn_edit_user);
        Button cancelButton = editUserDialog.findViewById(R.id.btn_cancel);

        // Aquí deberías cargar la información actual del usuario a editar y mostrarla en las vistas EditText

        // Configura un OnClickListener para el botón "Save"
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí obtén los valores editados de las vistas EditText y actualiza el usuario en la base de datos
                String newUsername = usernameEditText.getText().toString();
                String newEmail = emailEditText.getText().toString();
                String newPhone = phoneEditText.getText().toString();
                String newPassword = passwordEditText.getText().toString();

                // Actualiza los datos del usuario en la base de datos
                int result = dataManager.updateUser(userId, newUsername, newEmail, newPhone, newPassword);

                if (result > 0) {
                    // Si la actualización fue exitosa, cierra el cuadro de diálogo de edición
                    editUserDialog.dismiss();

                    // Actualiza la vista
                    cursor.requery();
                    notifyDataSetChanged();
                }
            }
        });

        // Configura un OnClickListener para el botón "Cancel"
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra el cuadro de diálogo de edición
                editUserDialog.dismiss();
            }
        });

        // Muestra el cuadro de diálogo de edición
        editUserDialog.show();
    }
}