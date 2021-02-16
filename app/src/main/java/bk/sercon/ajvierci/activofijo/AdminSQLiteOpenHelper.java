package bk.sercon.ajvierci.activofijo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    String tablaUsuarios = "create table users(id integer PRIMARY KEY AUTOINCREMENT, ci int, user text, password text)";

    String tablaActivos = "create table activos(id integer PRIMARY KEY AUTOINCREMENT, numChapa text, numActivo text,  " +
                                        "descripcion text, estado text, descEstado text, ubicacion text, " +
                                        "descUbicacion text, cat7 text, numCompa text, " +
                                        "userAsignado text, costo text, depAcu text, user text, " +
                                        "fechaPick text, nombreAuditoria text, sucursal text, " +
                                        "pick text default 'NO',  registrado text default 'NO')";

    String tablaActivosColectados = "create table activosColectados(id integer PRIMARY KEY AUTOINCREMENT, numChapa text, numActivo text,  " +
            "descripcion text, estado text, descEstado text, ubicacion text, " +
            "descUbicacion text, cat7 text, numCompa text, " +
            "userAsignado text, costo text, depAcu text, user text, " +
            "fechaPick text, nombreAuditoria text, sucursal text, " +
            "pick text default 'NO',  registrado text default 'NO')";

    String tablaSucursales = "create table sucursales(id integer PRIMARY KEY AUTOINCREMENT, local text)";


    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tablaUsuarios);
        db.execSQL(tablaActivos);
        db.execSQL(tablaSucursales);
        db.execSQL(tablaActivosColectados);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}