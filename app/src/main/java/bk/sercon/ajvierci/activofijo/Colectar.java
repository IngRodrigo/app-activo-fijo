package bk.sercon.ajvierci.activofijo;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import bk.sercon.ajvierci.activofijo.globales.Globales;
import bk.sercon.ajvierci.activofijo.globales.MovimientosDB;


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
    private SharedPreferences prefrences;
    private AdminSQLiteOpenHelper admin;
    private SQLiteDatabase BaseDeDatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colectar);
        conectarConVista();
        setPreferences();
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
    private SharedPreferences setPreferences(){
        return prefrences=getSharedPreferences("usuario", Context.MODE_PRIVATE);
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
                    procesar(chapita, audi, sucu);
                } else if(MovimientosDB.activoRegistradoTrabajando(admin, chapita)) {
                        procesar(chapita, audi, sucu);
                }else{
                    Toast.makeText(getApplicationContext(), "El activo no se encuentra en disponible para inventariar", Toast.LENGTH_SHORT).show();

                }
            }

            vistaExistente.setVisibility(View.VISIBLE);

        }

        BaseDeDatos.close();
    }

    private void procesar(String chapita, String audi, String sucu) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "activo_fijo", null, 1);
        BaseDeDatos = admin.getWritableDatabase();
        String name = Globales.cargarUsuarioPreference(setPreferences());

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
            if(MovimientosDB.comprobarEstadoActivo(admin, chapita)){
                Toast.makeText(this, "Activo Leído y Registrado", Toast.LENGTH_SHORT).show();
            }
        }
    }




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