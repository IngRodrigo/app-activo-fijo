package bk.sercon.ajvierci.activofijo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UserRegister extends AppCompatActivity {

    private EditText et_cedula, et_usuario, et_password1, et_password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        //icono en el action bar
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        et_cedula = (EditText)findViewById(R.id.txt_ciInsert);
        et_usuario = (EditText)findViewById(R.id.txt_userInsert);
        et_password1 = (EditText)findViewById(R.id.txtPassword);
        et_password2 = (EditText)findViewById(R.id.txt_password2Insert);
    }

    public void Registrar (View view){
        String cedula = et_cedula.getText().toString().trim();
        String usuario = et_usuario.getText().toString().trim();
        String password1 = et_password1.getText().toString().trim();
        String password2 = et_password2.getText().toString().trim();


        if (cedula.isEmpty()) {
            Toast.makeText(this, "Nº de Cédula Requerido", Toast.LENGTH_SHORT).show();
        }
        else{
            if (usuario.isEmpty()) {
                Toast.makeText(this, "Nombre de Usuario Requerido", Toast.LENGTH_SHORT).show();
            }
            else{
                if (password1.isEmpty()) {
                    Toast.makeText(this, "El Password es Requerido", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (password2.isEmpty()) {
                        Toast.makeText(this, "Debe Repetir la Contraseña", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (password1.equals(password2)){
                            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "activo_fijo", null, 1);
                            SQLiteDatabase BaseDeDatos = admin.getWritableDatabase(); // open database writting and reading

                            Cursor cursor;
                            cursor = BaseDeDatos.query("users",
                                    new String[]{"ci", "user", "password"},
                                    "ci = "+cedula+" OR user LIKE '"+usuario+"'",
                                    null, null, null, null);

                            if (cursor.getCount() > 0){
                                Toast.makeText(this, "El Usuario ya se encuentra registrado",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                ContentValues registro = new ContentValues();
                                registro.put("ci", cedula);
                                registro.put("user", usuario);
                                registro.put("password", password1);

                                BaseDeDatos.insert("users", null, registro);

                                BaseDeDatos.close();
                                et_cedula.setText("");
                                et_usuario.setText("");
                                et_password1.setText("");
                                et_password2.setText("");

                                Toast.makeText(this, "Usuario Registrado Correctamente", Toast.LENGTH_SHORT).show();

                                Intent login = new Intent(this, LoginActivity.class);
                                startActivity(login);

                                finish();
                            }
                        }
                        else{
                            Toast.makeText(this, "No Coinciden las Contraseñas", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }

    public void Limpiar (View view){
        et_cedula.setText("");
        et_usuario.setText("");
        et_password1.setText("");
        et_password2.setText("");
    }

    public void Volver(View view){
        Intent colectar = new Intent(this, LoginActivity.class);
        startActivity(colectar);
        finish();
    }
}
