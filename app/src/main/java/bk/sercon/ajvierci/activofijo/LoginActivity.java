package bk.sercon.ajvierci.activofijo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText txtUsername, txtPassword;

    // User Session Manager Class
    UserSessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // User Session Manager
        session = new UserSessionManager(getApplicationContext());

        // get Email, Password input text
        txtUsername = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
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

                    // Creating user login session
                    session.createUserLoginSession(username);

                    // Starting Principal
                    Intent i = new Intent(getApplicationContext(), Principal.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // Add new Flag to start new Activity
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
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
