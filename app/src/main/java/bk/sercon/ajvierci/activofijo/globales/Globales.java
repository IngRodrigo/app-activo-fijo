package bk.sercon.ajvierci.activofijo.globales;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import bk.sercon.ajvierci.activofijo.LoginActivity;

//rodDevCode</>
//Utilidades globales para la app
public class Globales {

    public static String cargarUsuarioPreference(SharedPreferences preferences){
        String usuario=preferences.getString("name_user","Desconocido");
        return usuario;
    }
    public static boolean cerrarSesion(SharedPreferences preferences){
        boolean respuesta=false;
        try{
            preferences.edit().clear().apply();
            respuesta=true;
        }catch (Exception e){
            respuesta=false;
        }
        return  respuesta;
    }
    public static void EscribirLog(String tipo, String mensaje) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            File nuevaCarpeta = new File(Environment.getExternalStorageDirectory(), "Logs");
            if (!nuevaCarpeta.exists()) {
                nuevaCarpeta.mkdir();
            }
            String data = "Tipo: "+tipo + " Detalle: " + mensaje + "\n";
            File file = new File(nuevaCarpeta, "log_" + Globales.fechaActual() + ".txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            // flag true, indica adjuntar información al archivo.
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            bw.write(data);
            System.out.println("información agregada!");

        } catch (IOException e) {
            System.out.println("No se pudo crear el archivo");
            e.printStackTrace();
        } finally {
            try {
                //Cierra instancias de FileWriter y BufferedWriter
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public static String fechaActual() {
        String fecha = "";
        Calendar calendario = Calendar.getInstance();

        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH) + 1;
        int anio = calendario.get(Calendar.YEAR);

        String hora = String.format("%02d", calendario.get(Calendar.HOUR_OF_DAY));
        String minuto = String.format("%02d", calendario.get(Calendar.MINUTE));
        String segundo = String.format("%02d", calendario.get(Calendar.SECOND));

        fecha = anio + "-" + mes + "-" + dia;

        return fecha;
    }

    public static String fechaHoraMinutoSegundoActual() {
        String fecha = "";
        Calendar calendario = Calendar.getInstance();

        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH) + 1;
        int anio = calendario.get(Calendar.YEAR);

        String hora = String.format("%02d", calendario.get(Calendar.HOUR_OF_DAY));
        String minuto = String.format("%02d", calendario.get(Calendar.MINUTE));
        String segundo = String.format("%02d", calendario.get(Calendar.SECOND));

        fecha = anio + "-" + mes + "-" + dia + " " + hora + ":" + minuto+":"+segundo;

        return fecha;
    }
}


