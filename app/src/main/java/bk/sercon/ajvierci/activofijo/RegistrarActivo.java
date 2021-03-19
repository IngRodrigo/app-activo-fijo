package bk.sercon.ajvierci.activofijo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class RegistrarActivo extends AppCompatActivity {

    private EditText txtCompanhia, txtNumChapa, txtDescripcion, txtUbicacion, txtUsuarioAsig, txtAuditoria;

    // User Session Manager Class
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_activo);


        //añadiendo datos del Activitie anterior
        //***********************************************************************/
        String codigo = getIntent().getStringExtra("codigo");
        txtNumChapa = (EditText) findViewById(R.id.txtNumChapaAdd);
        txtNumChapa.setText(codigo);
        txtNumChapa.setEnabled(false); //inabilita campo

        String auditoria = getIntent().getStringExtra("auditoria");
        txtAuditoria = (EditText) findViewById(R.id.txtAuditoriaAdd);
        txtAuditoria.setText(auditoria);
        txtAuditoria.setVisibility(View.INVISIBLE); //oculta el campo

        String sucursal = getIntent().getStringExtra("sucursal");
        txtCompanhia = (EditText) findViewById(R.id.txtCompanhiaAdd);
        txtCompanhia.setText(sucursal);
        txtCompanhia.setEnabled(false); //inabilita campo


        //***********************************************************************/
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcionAdd);
        txtUbicacion = (EditText) findViewById(R.id.txtUbicacionAdd);
        txtUsuarioAsig = (EditText) findViewById(R.id.txtUserAsignadoAdd);
    }


    public void Registrar(View view){
        String companhia = txtCompanhia.getText().toString().trim();
        String chapa = txtNumChapa.getText().toString().trim();
        String descripcion = txtDescripcion.getText().toString().trim();
        String ubicacion = txtUbicacion.getText().toString().trim();
        String usuario = txtUsuarioAsig.getText().toString().trim();
        String auditoria = txtAuditoria.getText().toString().trim();


        if (descripcion.isEmpty()){
            Toast.makeText(this, "Ingrese la Descripción.", Toast.LENGTH_SHORT).show();
        }
        else{
            if (ubicacion.isEmpty()){
                    Toast.makeText(this, "Ingrese la Ubicación.", Toast.LENGTH_SHORT).show();
            }
            else{
                if (usuario.isEmpty()){
                    Toast.makeText(this, "Ingrese el Usuario", Toast.LENGTH_SHORT).show();
                }
                else{

                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "activo_fijo", null, 1);
                    SQLiteDatabase BaseDeDatos = admin.getWritableDatabase(); // open database writting and reading

                    //******************************************************************************
                    // Session class instance
                    session = new UserSessionManager(getApplicationContext());
                    //TextView lbName = (TextView) findViewById(R.id.lbName);

                    // Check user login
                    // If User is not logged in , This will redirect user to LoginActivity.
                    if(session.checkLogin())
                        finish();

                    // get user data from session
                    HashMap<String, String> user = session.getUserDetails();
                    // get name
                    String name = user.get(UserSessionManager.KEY_NAME);

                    //fecha del sistema
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy-HH:mm:ss", Locale.getDefault());
                    Date date = new Date();
                    String fecha = dateFormat.format(date);

                    String pick = "SI";
                    String registrado = "SI";

                    ContentValues registro = new ContentValues();
                    registro.put("numChapa", chapa);
                    registro.put("descripcion", descripcion);
                    registro.put("descUbicacion", ubicacion);
                    registro.put("userAsignado", usuario);
                    registro.put("nombreAuditoria", auditoria);
                    registro.put("user", name);
                    registro.put("fechaPick", fecha);
                    registro.put("nombreAuditoria", auditoria);
                    registro.put("sucursal", companhia);
                    registro.put("pick", pick);
                    registro.put("registrado", registrado);
                    registro.put("estado", "WK");
                    registro.put("descEstado","Working");

                    BaseDeDatos.insert("activos", null, registro);
                    BaseDeDatos.close();

                    txtDescripcion.setText("");
                    txtUbicacion.setText("");
                    txtUsuarioAsig.setText("");

                    Toast.makeText(this, "Activo Registrado Correctamente", Toast.LENGTH_SHORT).show();

                    //volviendo al Activitie Anterior
                    Intent colectar = new Intent(this, Colectar.class);
                    colectar.putExtra("auditoria", auditoria);
                    colectar.putExtra("sucursal", companhia);
                    startActivity(colectar);
                    finish();
                }
            }
        }

    }



    public void Volver(View view){
        Intent colectar = new Intent(this, Colectar.class);
        startActivity(colectar);
        finish();
    }
}
