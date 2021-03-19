package bk.sercon.ajvierci.activofijo.globales;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;

import java.util.ArrayList;

import bk.sercon.ajvierci.activofijo.Activo;
import bk.sercon.ajvierci.activofijo.AdminSQLiteOpenHelper;

public class MovimientosDB {
    private static AdminSQLiteOpenHelper conexion;
    private static SQLiteDatabase bd;
    private static boolean resultado = false;
    private static ArrayList<Activo> listaActivos;

    public static String actualizarActivos(ArrayList<Activo> activo, AdminSQLiteOpenHelper conexion) throws JSONException {
        //conexion = new AdminSQLiteOpenHelper(this, "activo_fijo", null, 1);
        bd = conexion.getWritableDatabase(); // open database writting and reading
        String resultado = "";
        // int contador=0;
        ContentValues registro = new ContentValues();

        for (Activo activoObjeto : activo
        ) {
            registro.put("numChapa", activoObjeto.getNUMCHAPA());
            registro.put("numActivo", activoObjeto.getNUMACTIVO());
            registro.put("descripcion", activoObjeto.getDESCRIPCION());
            registro.put("ubicacion", activoObjeto.getUBICACION());
            registro.put("descUbicacion", activoObjeto.getDESCUBICACION());
            registro.put("estado", activoObjeto.getESTADO());
            registro.put("descEstado", activoObjeto.getDESCESTADO());
            registro.put("cat7", activoObjeto.getCAT7());
            registro.put("numCompa", activoObjeto.getNUMCOMPA());
            registro.put("userAsignado", activoObjeto.getUSUASIGNADO());
            registro.put("costo", activoObjeto.getCOSTO());
            registro.put("depAcu", activoObjeto.getDEPACU());
            registro.put("user", activoObjeto.getUserRegistro());
            registro.put("fechaPick", activoObjeto.getFechaPick());
            registro.put("nombreAuditoria", activoObjeto.getNombreAuditoria());
            registro.put("sucursal", activoObjeto.getSucursal());
            registro.put("pick", activoObjeto.getPick());
            registro.put("registrado", activoObjeto.getRegistrado());
            bd.insert("activosColectados", null, registro);
        }
        resultado = "exito";
        return resultado;
    }

    public static ArrayList<Activo> getActivosColectados(AdminSQLiteOpenHelper conexion) {
        Activo activo = null;
        bd = conexion.getReadableDatabase();
        listaActivos = new ArrayList<>();
        Cursor cursor = bd.rawQuery("Select * from activosColectados where estado='WK'", null);

        while (cursor.moveToNext()) {
            activo = new Activo();
            activo.setNUMCHAPA(cursor.getString(1));
            activo.setNUMACTIVO(cursor.getString(2));
            activo.setDESCRIPCION(cursor.getString(3));
            activo.setUBICACION(cursor.getString(4));
            activo.setDESCUBICACION(cursor.getString(5));
            activo.setESTADO(cursor.getString(6));
            activo.setDESCESTADO(cursor.getString(7));
            activo.setCAT7(cursor.getString(8));
            activo.setNUMCOMPA(cursor.getString(9));
            activo.setUSUASIGNADO(cursor.getString(10));
            activo.setCOSTO(cursor.getString(11));
            activo.setDEPACU(cursor.getString(12));
            activo.setUserRegistro(cursor.getString(13));
            activo.setFechaPick(cursor.getString(14));
            activo.setNombreAuditoria(cursor.getString(15));
            activo.setSucursal(cursor.getString(16));
            activo.setPick(cursor.getString(17));
            activo.setRegistrado(cursor.getString((18)));

            listaActivos.add(activo);
        }
        bd.close();
        return listaActivos;
    }

    public static boolean activoTrabajando(AdminSQLiteOpenHelper conexion, String numActivo) {
        try {
            bd = conexion.getReadableDatabase();
            listaActivos = new ArrayList<>();
            String sql="Select * from activos where estado='WK' and numActivo='"+numActivo+"'";
            Globales.EscribirLog("SQL: ", "Consulta a ejecutar: "+sql+" Time:"+Globales.fechaHoraMinutoSegundoActual());
            Cursor cursor = bd.rawQuery(sql, null);
            if(cursor.moveToFirst()){
                resultado=true;
            }else{
                resultado=false;
            }
        } catch (Exception e) {

        }
        return resultado;
    }
    public static boolean activoRegistradoTrabajando(AdminSQLiteOpenHelper conexion, String numChapa) {
        try {
            bd = conexion.getReadableDatabase();
            listaActivos = new ArrayList<>();
            String sql="Select * from activos where estado='WK' and numChapa='"+numChapa+"'";
            Globales.EscribirLog("SQL: ", "Consulta a ejecutar: "+sql+" Time:"+Globales.fechaHoraMinutoSegundoActual());
            Cursor cursor = bd.rawQuery(sql, null);
            if(cursor.moveToFirst()){
                resultado=true;
            }else{
                resultado=false;
            }
        } catch (Exception e) {

        }
        return resultado;
    }
    public static boolean comprobarEstadoActivo(AdminSQLiteOpenHelper conexion, String chapita) {
        try {
            bd = conexion.getReadableDatabase();
            String sql="SELECT * FROM activos WHERE registrado ='SI' and numChapa='"+chapita+"'";

            Globales.EscribirLog("SQL: ", "Consulta a ejecutar: "+sql+" Time:"+Globales.fechaHoraMinutoSegundoActual());

            Cursor cursor = bd.rawQuery(sql, null);
            if(cursor.moveToFirst()){
                resultado=true;
            }else{
                resultado=false;
            }
        } catch (Exception e) {
            System.out.println("No se pudo realizar la consulta del estado del activo a causa de: "+e);
            Globales.EscribirLog("ERROR","No se pudo realizar la consulta del estado del activo a causa de: "+e);
        }
        return resultado;
    }
}
