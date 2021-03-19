package bk.sercon.ajvierci.activofijo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText txtUsername, txtPassword;

    private SharedPreferences prefrences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setPreferences();
        txtUsername = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        cargarPreferencias();
    }
    private void setPreferences(){
        prefrences=getSharedPreferences("usuario", Context.MODE_PRIVATE);
    }
    private void guardarPreferencias(String usuario){
            SharedPreferences.Editor editor= prefrences.edit();
            editor.putString("name_user", usuario);
            editor.putString("recordar","si");
            editor.apply();
            System.out.println("se guardo");
    }
    private void cargarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("usuario", Context.MODE_PRIVATE);

        String recordar=preferences.getString("recordar","no");
        if(recordar.equals("si")){
            ir_Principal();

        }else{
           // EscribirLog.EscribirLog(EscribirLog.fechaHoraMinutoSegundoActual()+": Error de logueo: ", "La sessión del usuario no esta guardada a petición del mismo.");
            System.out.println("no");
        }
    }
    private void ir_Principal() {
        // Starting Principal
        Intent i = new Intent(getApplicationContext(), Principal.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(i);
        finish();
    }

    public void Login (View view){
        // Get username, password from EditText
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        if(username.trim().length() > 0){
            if(password.trim().length() > 0){

                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "activo_fijo", null, 1);
                SQLiteDatabase BaseDeDatos = admin.getWritableDatabase(); // open database writting and reading

                Cursor cursor;
                cursor = BaseDeDatos.query("users",
                        new String[]{"ci", "user", "password"},
                        "user LIKE '"+username+"' AND password LIKE '"+password+"'",
                        null, null, null, null);

                if (cursor.getCount() == 0){
                    Toast.makeText(this, "Usuario no Registrado", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Datos Correctos", Toast.LENGTH_SHORT).show();

                    guardarPreferencias(username);
                    ir_Principal();
                }

            }else{
                Toast.makeText(this, "El Password es Requerido", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "El Usuario es Requerido", Toast.LENGTH_SHORT).show();
        }
    }



    public void Registrar (View view){
        Intent registroUsuario = new Intent(this, UserRegister.class);
        startActivity(registroUsuario);
        finish();
    }

    public void actulizarColector(View view){
        Intent actualizar = new Intent(this, UpdateColector.class);
        startActivity(actualizar);
        //finish();
    }
}
