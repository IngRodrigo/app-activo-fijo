package bk.sercon.ajvierci.activofijo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "http://webapp2.ajvierci.com.py/appactivofijo/";


    @GET("api/activos.php")
    Call<List<Activo>> getActivos(@Query("cia") int cia);
}
