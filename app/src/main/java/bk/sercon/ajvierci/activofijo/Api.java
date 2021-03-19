package bk.sercon.ajvierci.activofijo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    //String BASE_URL = "http://webapp2.ajvierci.com.py/appactivofijo/";
    String BASE_URL ="http://192.168.12.46:8080/api-activo-fijo/activos";


    @GET("api/activos.php")
    Call<List<Activo>> getActivos(@Query("cia") int cia);
}
