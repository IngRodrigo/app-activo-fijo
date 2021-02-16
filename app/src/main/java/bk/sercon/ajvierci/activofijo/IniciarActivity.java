package bk.sercon.ajvierci.activofijo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class IniciarActivity extends AppCompatActivity {

    private EditText txt_NomAuditoria;
    private Spinner spinner;
    private SQLiteDatabase BaseDeDatos;
    private AdminSQLiteOpenHelper admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar);

        txt_NomAuditoria = (EditText) findViewById(R.id.txt_NombreAuditoria);
        spinner = (Spinner) findViewById(R.id.spinner);

        cargarSpinner();
    }

    //carga el La lista de sucursales para seleccionar.
    private void cargarSpinner() {

        admin = new AdminSQLiteOpenHelper(this, "activo_fijo", null, 1);
        BaseDeDatos = admin.getReadableDatabase();

        List<String> opciones = new ArrayList<String>();
        opciones.add("Selecciona una opción");

        String selectQuery = "SELECT  * FROM sucursales" ;
        Cursor cursor = BaseDeDatos.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // adding to tags list
                opciones.add(cursor.getString(cursor.getColumnIndex("local")));
            } while (cursor.moveToNext());
        }

        BaseDeDatos.close();

        //spiner personalizado
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(adapter);

    }

    //Comprueba los datos sean correctos para iniciar la auditoria. Al dar iniciar cambia a otro activity
    public void Iniciar(View view){
        String auditoria = txt_NomAuditoria.getText().toString().trim();
        String sucursal = spinner.getSelectedItem().toString(); //obteniendo los valores del Spinner

        if (auditoria.isEmpty()){
            Toast.makeText(this, "El Nombre de la Auditoria es Requerido", Toast.LENGTH_SHORT).show();
        }
        else{
            if (sucursal.equals("Selecciona una opción")){
                Toast.makeText(this, "Selecciona una opción", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent colectar = new Intent(this, Colectar.class);
                colectar.putExtra("auditoria", auditoria);
                colectar.putExtra("sucursal", sucursal);
                startActivity(colectar);
                finish();
            }
        }
    }

    //metodo para volver al activity Principal
    public void Volver(View view){
        Intent principal = new Intent(this, Principal.class);
        startActivity(principal);
        finish();
    }
}
