package bk.sercon.ajvierci.activofijo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;

import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class Colectar extends AppCompatActivity {
    private String auditoria;
    private String sucursal;
    private String numChapa, auditoria_par, sucursal_par; //primeros parametros
    private String num, audi, sucu; //segundo parametros
    private EditText et_NumChapa, txt_auditoria, txt_susursal;
    private ScrollView vistaExistente;
    private EditText txtNumCompa, txtUser, txtNumChapa, txtNumActivo, txtDescripcion, txtUbicacion;

    // User Session Manager Class
    UserSessionManager session;

    SoundPool sp; // audio file
    int sonido_rep = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colectar);
        conectarConVista();
        //Variables para los EditText
        //******************************************************************************
        txtNumCompa = (EditText) findViewById(R.id.txtExisteCom);
        txtUser = (EditText) findViewById(R.id.txtExisteUsuario);
        txtNumChapa = (EditText) findViewById(R.id.txtExisteChapa);
        txtNumActivo = (EditText) findViewById(R.id.txtExisteNumActivo);
        txtDescripcion = (EditText) findViewById(R.id.txtExisteDescripcion);
        txtUbicacion = (EditText) findViewById(R.id.txtExisteUbicacion);


        txtNumCompa.setEnabled(false);
        txtUser.setEnabled(false);
        txtNumChapa.setEnabled(false);
        txtNumActivo.setEnabled(false);
        txtDescripcion.setEnabled(false);
        txtUbicacion.setEnabled(false);


        //******************************************************************************
        vistaExistente = (ScrollView) findViewById(R.id.vistaExistente);
        vistaExistente.setVisibility(View.INVISIBLE);

        et_NumChapa = (EditText) findViewById(R.id.NumChapa);

        //verifica y mantiene el focus en el EditText
        et_NumChapa.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            numChapa = et_NumChapa.getText().toString().trim();
                            auditoria_par = txt_auditoria.getText().toString().trim();
                            sucursal_par = txt_susursal.getText().toString().trim();

                            Verificar(numChapa, auditoria_par, sucursal_par);

                            //establece foco nuevamente
                            et_NumChapa.requestFocus();
                            et_NumChapa.setText("");

                        }
                    }, 10);

                    return true;
                } else {
                    // If any other key was pressed, do not consume the event.
                    return false;
                }

            }
        });


        //Reproduciendo Audio
        //******************************************************************************
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        sonido_rep = sp.load(this, R.raw.noencontrado, 1);

    }

    private void conectarConVista() {
        capturarParametrosExtra();
        txt_auditoria = (EditText) findViewById(R.id.txt_auditoria);
        txt_susursal = (EditText) findViewById(R.id.txt_sucursal);

        txt_auditoria.setText(auditoria);
        txt_susursal.setText(sucursal);

        txt_auditoria.setVisibility(View.INVISIBLE);
        txt_susursal.setVisibility(View.INVISIBLE);
    }

    private void capturarParametrosExtra() {
        this.auditoria = getIntent().getStringExtra("auditoria");
        this.sucursal = getIntent().getStringExtra("sucursal");
    }

    //Procesos para colectar
    public void Verificar(String numChapa, String auditoria, String sucursal) {
        num = numChapa;
        audi = auditoria;
        sucu = sucursal;

        vistaExistente.setVisibility(View.INVISIBLE);
        //conectarse a la bd
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "activo_fijo", null, 1);
        //abrir la bd en modo lectura y escritura
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase(); // open database writting and reading

        Cursor cursor;
        cursor = BaseDeDatos.query("activos",
                new String[]{"numChapa", "numActivo", "descripcion", "ubicacion", "numCompa",
                        "userAsignado", "user"},
                "numChapa LIKE '%" + numChapa + "%'",
                null, null, null, null);


        if (cursor.getCount() == 0) {
            //Reproduciendo Audio
            //******************************************************************************
            sp.play(sonido_rep, 1, 1, 1, 1, 0); //reproducinedo pista..


            //******************************************************************************
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Deseas Registrarlo??..");
            builder.setTitle("Activo NO Registrado");

            builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    registrarActivo(num, audi, sucu);
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            txtNumCompa.setText("");
            txtUser.setText("");
            txtNumChapa.setText("");
            txtNumActivo.setText("");
            txtDescripcion.setText("");
            txtUbicacion.setText("");

            Toast.makeText(this, "El Activo ya se encuentra Registrado", Toast.LENGTH_SHORT).show();

            String chapita = numChapa;

            Cursor fila = BaseDeDatos.rawQuery("SELECT numChapa, numActivo, descripcion, descUbicacion, numCompa, userAsignado FROM activos WHERE numChapa = '" + chapita + "'", null);
            String numActivo;
            if (fila.moveToFirst()) {
                numActivo=fila.getString(1);
                txtNumChapa.setText(fila.getString(0));
                txtNumActivo.setText(fila.getString(1));
                txtDescripcion.setText(fila.getString(2));
                txtUbicacion.setText(fila.getString(3));
                txtNumCompa.setText(fila.getString(4));
                txtUser.setText(fila.getString(5));

                if (MovimientosDB.activoTrabajando(admin, numActivo)) {
                    Toast.makeText(getApplicationContext(), "El activo esta funcionando", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "El activo no se encuentra en funcionamiento", Toast.LENGTH_SHORT).show();
                }
            }

            vistaExistente.setVisibility(View.VISIBLE);


            // Actualizando ACTIVO
            //************************************************************************************************

            // Session class instance
            session = new UserSessionManager(getApplicationContext());
            //TextView lbName = (TextView) findViewById(R.id.lbName);

            // Check user login
            // If User is not logged in , This will redirect user to LoginActivity.
            if (session.checkLogin())
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

            //******************************************************************************
            ContentValues registro = new ContentValues();

            registro.put("user", name);
            registro.put("fechaPick", fecha);
            registro.put("nombreAuditoria", audi);
            registro.put("sucursal", sucu);
            registro.put("pick", pick);


            int cantidad = BaseDeDatos.update("activos", registro, "numChapa=" + chapita, null);

            if (cantidad == 1) {
                Toast.makeText(this, "Activo Leído y Registrado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "El Activo no existe", Toast.LENGTH_SHORT).show();
            }
        }

        BaseDeDatos.close();
    }

    private AdminSQLiteOpenHelper admin;
    private SQLiteDatabase BaseDeDatos;


    public void registrarActivo(String numChapa, String auditoria, String sucursal) {
        Intent registrar = new Intent(this, RegistrarActivo.class);
        registrar.putExtra("codigo", numChapa);
        registrar.putExtra("auditoria", auditoria);
        registrar.putExtra("sucursal", sucursal);
        startActivity(registrar);
        finish();
    }


    public void Volver(View view) {
        Intent iniciar = new Intent(this, IniciarActivity.class);
        startActivity(iniciar);
        finish();
    }

}