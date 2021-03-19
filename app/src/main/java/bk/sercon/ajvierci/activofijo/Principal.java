package bk.sercon.ajvierci.activofijo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import bk.sercon.ajvierci.activofijo.globales.Globales;


public class Principal extends AppCompatActivity {

    // User Session Manager Class
    UserSessionManager session;
    private TextView lbName;

    private Button btn_csv;

    private SharedPreferences prefrences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setPreferences();
        // Show user data on activity
        String name= Globales.cargarUsuarioPreference(setPreferences());
        this.lbName=findViewById(R.id.lbName);
        this.lbName.setText(Html.fromHtml("Hola: <b>" + name + "</b>"));



        //Boton Exportar a CSV
        //******************************************************************************
        btn_csv = (Button) findViewById(R.id.btnReportes);
        btn_csv.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         try {
                                             generarInforme();
                                         } catch (IOException e) {
                                             e.printStackTrace();
                                         }
                                     }
                                 }
        );
    }
    private SharedPreferences setPreferences(){
        return prefrences=getSharedPreferences("usuario", Context.MODE_PRIVATE);
    }


    public void actualizarColector(View view){
        Intent actualizar = new Intent(this, UpdateColector.class);
        startActivity(actualizar);
        finish();
    }



    public void iniciarInventario(View view){
        Intent iniciar = new Intent(this, IniciarActivity.class);
        startActivity(iniciar);
        finish();
    }


    public void generarInforme()throws IOException
    {
        Toast.makeText(this, "Exportando Archivo CSV", Toast.LENGTH_SHORT).show();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "activo_fijo", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getReadableDatabase();

        String date = new SimpleDateFormat("yyyy-MM-dd_HH_mm", Locale.getDefault()).format(new Date());

        File dbFile = getDatabasePath("activo_fijo.db");
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) { exportDir.mkdirs(); }

        File file = new File(exportDir, "activos_" + date + ".csv");

        try{
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

            String query = "SELECT nombreAuditoria AS 'Auditoria Nombre', " +
                    "numChapa AS 'N de Chapa', numActivo AS 'N de Activo', descripcion AS 'Descripcion', " +
                    "estado AS 'Estado', descEstado AS 'Desc del Estado', ubicacion AS 'N Ubicacion', " +
                    "descUbicacion AS 'Ubicacion', cat7 AS 'Cat 7', costo AS 'Costo', " +
                    "depAcu AS 'Dep Acum', userAsignado AS 'Usuario Asignado', " +
                    "registrado AS 'Alta S/N', sucursal AS 'Sucursal', pick AS 'Pick', fechaPick AS 'Fecha Pick', " +
                    "user AS 'Usuario Pick' " +
                    "FROM activos" +
                    " where estado ='WK' and nombreAuditoria NOTNULL";


            Cursor curCSV = BaseDeDatos.rawQuery(query,null);
            csvWrite.writeNext(curCSV.getColumnNames());

            while(curCSV.moveToNext()) {
                String arrStr[] = {
                        curCSV.getString(0),
                        curCSV.getString(1),
                        curCSV.getString(2),
                        curCSV.getString(3),
                        curCSV.getString(4),
                        curCSV.getString(5),
                        curCSV.getString(6),
                        curCSV.getString(7),
                        curCSV.getString(8),
                        curCSV.getString(9),
                        curCSV.getString(10),
                        curCSV.getString(11),
                        curCSV.getString(12),
                        curCSV.getString(13),
                        curCSV.getString(14),
                        curCSV.getString(15),
                        curCSV.getString(16)

                };

                csvWrite.writeNext(arrStr);
            }

            csvWrite.close();
            curCSV.close();

            Toast.makeText(getApplicationContext(),"Archivo CSV generando correctamente",Toast.LENGTH_LONG).show();

        }
        catch (SQLiteException se)
        {
            Toast.makeText(getApplicationContext(),"Error al generar el archivo CSV",Toast.LENGTH_LONG).show();

        }
        finally
        {
            BaseDeDatos.close();
        }
    }



    public void cerrarSesion(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseas Cerrar tu Sesion??..");
        builder.setTitle("AVISO");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                        if(Globales.cerrarSesion(setPreferences())){
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
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
    }

    private void enviarAlLogin() {
        // Starting Principal
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
