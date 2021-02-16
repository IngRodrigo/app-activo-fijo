package bk.sercon.ajvierci.activofijo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateColector extends AppCompatActivity {
    private TextView lbMensaje, lbOtrasOpciones;
    private Button btnActualizar, btnVaciar, btnVolver;
    private EditText txt_NumCompanhia;
    private SQLiteDatabase BaseDeDatos;
    private String numCom;
    private AdminSQLiteOpenHelper admin;

    //rodrigo_dev
    private ProgressBar progreso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        //******************************************************************************
        lbMensaje = (TextView) findViewById(R.id.lbActualizando);
        lbOtrasOpciones = (TextView) findViewById(R.id.tvOtrasopciones);
        txt_NumCompanhia = (EditText) findViewById(R.id.txtNumCompanhia);

        //rodrigo_dev
        this.progreso=findViewById(R.id.progressBar);
        //

        lbMensaje.setVisibility(View.INVISIBLE);

        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        btnVaciar = (Button) findViewById(R.id.btnVaciar);
        btnVolver = (Button) findViewById(R.id.btnVolver);
        comprobarCompania();
    }


    public void Actualizar(View view){
        numCom = txt_NumCompanhia.getText().toString().trim();
        int num = Integer.parseInt(numCom);

        if (numCom.isEmpty()) {
            Toast.makeText(this, "Ingrese el Nº de Compañía", Toast.LENGTH_SHORT).show();
        }
        else{
            lbMensaje.setText("");
            lbMensaje.setText("Actualizando Colector, la operación puede durar unos segundos..");

            lbMensaje.setVisibility(View.VISIBLE);
            lbOtrasOpciones.setVisibility(View.GONE);
            txt_NumCompanhia.setVisibility(View.GONE);
            btnActualizar.setVisibility(View.GONE);
            btnVaciar.setVisibility(View.GONE);
            btnVolver.setVisibility(View.GONE);


            //actualizando las sucursales
          // updateSucursales(num);
            new TareaCompanias(true).execute(numCom);
            //Actuliza los Activos en el Dispositivo
           //updateActivos(num);
        }
    }



    public void updateSucursales(int num){
        int cia = num;

        admin = new AdminSQLiteOpenHelper(this, "activo_fijo", null, 1);
        BaseDeDatos = admin.getWritableDatabase(); // open database writting and reading*/

        Retrofit retrofitSu = new Retrofit.Builder()
                .baseUrl(ApiSucursales.BASE_URL_SUCU)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiSucursales apiSu = retrofitSu.create(ApiSucursales.class);
        Call<List<Sucursal>> call = apiSu.getLocales(cia);

        call.enqueue(new Callback<List<Sucursal>>() {
            @Override
            public void onResponse(Call<List<Sucursal>> call, Response<List<Sucursal>> response) {
                List<Sucursal> sucursalList = response.body();
                System.out.println(response);
                //SQLite
                ContentValues registroSu = new ContentValues();

                for (int i = 0; i < sucursalList.size(); i++) {
                    registroSu.put("local", sucursalList.get(i).getLOCAL());
                    BaseDeDatos.insert("sucursales", null, registroSu);
                    System.out.println("se inserto: "+sucursalList.get(i).getLOCAL());
                }

                //no cerrar BD para que funcionen ambas funciones de actulizacion...
                //BaseDeDatos.close();
            }

            @Override
            public void onFailure(Call<List<Sucursal>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void updateActivos(int num){
        int cia = num;

        admin = new AdminSQLiteOpenHelper(this, "activo_fijo", null, 1);
        BaseDeDatos = admin.getWritableDatabase(); // open database writting and reading

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Activo>> call = api.getActivos(cia);

        call.enqueue(new Callback<List<Activo>>() {
            @Override
            public void onResponse(Call<List<Activo>> call, Response<List<Activo>> response) {
                if(response.isSuccessful()){
                    List<Activo> activoList = response.body();
                    System.out.println(response.body().get(0).getNUMCHAPA());
                    if(activoList!=null){
                        //Creating an String array for the ListView
                        //String[] activos = new String[activoList.size()];

                        //SQLite
                        ContentValues registro = new ContentValues();

                        for (int i = 0; i < activoList.size(); i++) {
                            registro.put("numChapa", activoList.get(i).getNUMCHAPA());
                            registro.put("numActivo", activoList.get(i).getNUMACTIVO());
                            registro.put("descripcion", activoList.get(i).getDESCRIPCION());
                            registro.put("ubicacion", activoList.get(i).getUBICACION());
                            registro.put("descUbicacion", activoList.get(i).getDESCUBICACION());
                            registro.put("estado", activoList.get(i).getESTADO());
                            registro.put("descEstado", activoList.get(i).getDESCESTADO());
                            registro.put("cat7", activoList.get(i).getCAT7());
                            registro.put("numCompa", activoList.get(i).getNUMCOMPA());
                            registro.put("userAsignado", activoList.get(i).getUSUASIGNADO());
                            registro.put("costo", activoList.get(i).getCOSTO());
                            registro.put("depAcu", activoList.get(i).getDEPACU());

                            BaseDeDatos.insert("activos", null, registro);
                            System.out.println("activo: "+activoList.get(i).getDESCRIPCION());
                        }

                        Toast.makeText(getApplicationContext(), "Colector Actualizado Correctamente..", Toast.LENGTH_SHORT).show();
                        BaseDeDatos.close();

                        lbMensaje.setText("");
                        lbMensaje.setText("Colector Actualizado Correctamente..");
                        btnVolver.setVisibility(View.VISIBLE);
                    }else{
                        System.out.println("Lista vacia");
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Error al intentar conectarse", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Activo>> call, Throwable t) {
                System.out.println(t.toString());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void vaciarColector(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Quieres Vaciar el Colector??..");
        builder.setTitle("AVISO");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                vaciar();
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



    public void vaciar(){
        admin = new AdminSQLiteOpenHelper(this, "activo_fijo", null, 1);
        BaseDeDatos = admin.getWritableDatabase(); // open database writting and reading

        //int cantidad = BaseDeDatos.delete("activos", null, null);

        BaseDeDatos.delete("activos", null, null);
        BaseDeDatos.delete("sucursales", null, null);

        Toast.makeText(this, "Colector Vaciado Correctamente", Toast.LENGTH_SHORT).show();

        BaseDeDatos.close();
    }


    public void comprobarCompania(){

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "activo_fijo", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase(); // open database writting and reading
        Cursor cursor;
        cursor = BaseDeDatos.query("sucursales", new String[]{"local"},
                "local='BKING  MRA'",
                null, null, null, null);

            if(cursor.getCount()!=0){
                System.out.println("existe");
                while (cursor.moveToNext()){
                    System.out.println(cursor.getString(0));
                }
            }else{
                System.out.println("No existe");
            }

    }
    public void Volver(View view){
        Intent principal = new Intent(this, Principal.class);
        startActivity(principal);
        finish();
    }




    //rodrigo_dev
    class TareaActivos extends AsyncTask<String, Void, String>{

        public TareaActivos(boolean showLoading){
            super();

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progreso.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            int contador=0;
            String respuesta="";
            String cia=String.valueOf(strings[0]);
            StringBuilder texto;
            ArrayList<String>lista= new ArrayList<>();
            try {
                    String resultado=traerActivos(cia);
                    JSONArray jsonArray= new JSONArray(resultado);

                for (int i = 0; i < jsonArray.length(); i++) {
                    texto=new StringBuilder();
                    texto.append(jsonArray.getJSONObject(i));
                    lista.add(texto.toString());
                }
                for (int i = 0; i < lista.size(); i++) {
                    JSONObject jsonObject= new JSONObject(lista.get(i));
                    System.out.println(jsonObject.toString());
                    respuesta=actualizarActivos(jsonObject);
                    if(respuesta.equals("exito")){
                        contador++;
                    }

                }
                System.out.println("se inseratorn: "+contador);
            //

            }catch (Exception e){
                System.out.println(e);
            }



            return respuesta;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("exito")){
                progreso.setVisibility(View.INVISIBLE);
                lbMensaje.setText("");
                lbMensaje.setText("Colector Actualizado Correctamente..");
                btnVolver.setVisibility(View.VISIBLE);
            }else{
                progreso.setVisibility(View.INVISIBLE);
                System.out.println(s);
            }
        }
    }
    class TareaCompanias extends AsyncTask<String, Void, String>{

        public TareaCompanias(boolean showLoading){
            super();

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progreso.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            int contador=0;
            String respuesta="";
            String cia=String.valueOf(strings[0]);
            StringBuilder texto;
            ArrayList<String>lista= new ArrayList<>();
            try {
                String resultado=traerCompanias(cia);
                JSONArray jsonArray= new JSONArray(resultado);

                for (int i = 0; i < jsonArray.length(); i++) {
                    texto=new StringBuilder();
                    texto.append(jsonArray.getJSONObject(i));
                    lista.add(texto.toString());
                }
                for (int i = 0; i < lista.size(); i++) {
                    JSONObject jsonObject= new JSONObject(lista.get(i));
                    System.out.println(jsonObject.toString());
                    respuesta=actualizarCompanias(jsonObject);
                    if(respuesta.equals("exito")){
                        contador++;
                    }

                }
                System.out.println("se insertaron: "+contador);
                //

            }catch (Exception e){
                System.out.println(e);
            }



            return respuesta;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("exito")){
                numCom = txt_NumCompanhia.getText().toString().trim();
                int num = Integer.parseInt(numCom);
                new TareaActivos(true).execute(numCom);
            }else{
                progreso.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Ocurrio un error inesperado, al importar las sucursales", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String actualizarActivos(JSONObject json) throws JSONException {
        admin = new AdminSQLiteOpenHelper(this, "activo_fijo", null, 1);
        BaseDeDatos = admin.getWritableDatabase(); // open database writting and reading
        String resultado="";
       // int contador=0;
        ContentValues registro = new ContentValues();

            registro.put("numChapa", json.getString("NUMCHAPA"));
            registro.put("numActivo", json.getString("NUMACTIVO"));
            registro.put("descripcion", json.getString("DESCRIPCION"));
            registro.put("ubicacion", json.getString("UBICACION"));
            registro.put("descUbicacion",json.getString("DESCUBICACION"));
            registro.put("estado", json.getString("ESTADO"));
            registro.put("descEstado", json.getString("DESCESTADO"));
            registro.put("cat7", json.getString("CAT7"));
            registro.put("numCompa", json.getString("NUMCOMPA"));
            registro.put("userAsignado", json.getString("USUASIGNADO"));
            registro.put("costo", json.getString("COSTO"));
            registro.put("depAcu", json.getString("DEPACU"));
            BaseDeDatos.insert("activos", null, registro);

        resultado="exito";
        return resultado;
    }
    private String actualizarCompanias(JSONObject json)throws JSONException{
        admin = new AdminSQLiteOpenHelper(this, "activo_fijo", null, 1);
        BaseDeDatos = admin.getWritableDatabase(); // open database writting and reading
        String resultado="";
        // int contador=0;
        ContentValues registro = new ContentValues();
        registro.put("local",json.getString("LOCAL"));
        BaseDeDatos.insert("sucursales", null, registro);
        System.out.println("se inserto: "+json.getString("LOCAL"));
        resultado="exito";
        return resultado;
    }
    private String traerActivos(String cia) {

    String resultado="error";
        URL url= null;
        String linea="";
        StringBuilder resul=null;
        int respuesta=0;
        ArrayList<String>jsonR= new ArrayList<>();
        try{
              //  url=new URL("http://webapp2.ajvierci.com.py/appactivofijo/api/sucursales.php?cia="+cia);
            url=new URL("http://webapp2.ajvierci.com.py/appactivofijo/api/activos.php?cia="+cia);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            respuesta=connection.getResponseCode();
            resul=new StringBuilder();
            if(respuesta==200){
                InputStream in=new BufferedInputStream(connection.getInputStream());
                BufferedReader reader= new BufferedReader(new InputStreamReader(in));

                while ((linea=reader.readLine())!=null){
                    resul.append(linea);
                }

            }else{
                System.out.println("Error codigo respuesta: "+respuesta);
            }
        }catch (Exception e){
            System.out.println("Error= " + e.toString());
        }
    return resul.toString();
    }



    private String traerCompanias(String cia) {

        String resultado="error";
        URL url= null;
        String linea="";
        StringBuilder resul=null;
        int respuesta=0;
        ArrayList<String>jsonR= new ArrayList<>();
        try{
              url=new URL("http://webapp2.ajvierci.com.py/appactivofijo/api/sucursales.php?cia="+cia);
            //url=new URL("http://webapp2.ajvierci.com.py/appactivofijo/api/activos.php?cia="+cia);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            respuesta=connection.getResponseCode();
            resul=new StringBuilder();
            if(respuesta==200){
                InputStream in=new BufferedInputStream(connection.getInputStream());
                BufferedReader reader= new BufferedReader(new InputStreamReader(in));

                while ((linea=reader.readLine())!=null){
                    resul.append(linea);
                }

            }else{
                System.out.println("Error codigo respuesta: "+respuesta);
            }
        }catch (Exception e){
            System.out.println("Error= " + e.toString());
        }
        return resul.toString();
    }
}
