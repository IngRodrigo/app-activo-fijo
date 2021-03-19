package bk.sercon.ajvierci.activofijo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Acceso extends AppCompatActivity {
    // User Session Manager Class
    UserSessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceso);

        //cargarPreferencias();
    }

    private void cargarPreferencias() {
        // User Session Manager
        session = new UserSessionManager(getApplicationContext());
        // Check user login
        // If User is not logged in , This will redirect user to LoginActivity.
        if(session.checkLogin())
            finish();
    }

    public void Login(View view) {
    }

    public void Registrar(View view) {
    }
}