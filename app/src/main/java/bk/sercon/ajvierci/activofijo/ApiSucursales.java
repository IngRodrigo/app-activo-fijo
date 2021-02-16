package bk.sercon.ajvierci.activofijo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiSucursales {

    String BASE_URL_SUCU = "http://webapp2.ajvierci.com.py/appactivofijo/";

    @GET("api/sucursales.php")
    Call<List<Sucursal>> getLocales(@Query("cia") int cia);
}
