package com.example.pix.sulmovil.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pix.sulmovil.R;
import com.example.pix.sulmovil.logic.auth.Authenticator;
import com.example.pix.sulmovil.logic.exception.AuthException;
import com.example.pix.sulmovil.logic.models.User;
import com.example.pix.sulmovil.ui.templates.LocationActivity;
import com.example.pix.sulmovil.util.Notifier;

public class LoginActivity extends LocationActivity {

    private Authenticator mAuthenticator;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.mAuthenticator = new Authenticator( this );
        this.mLoginButton = (Button)findViewById(R.id.login_button_login);
        if( this.mAuthenticator.hasSessionOpen() ){
            enterApplication();
        }
    }

    private void enterApplication() {
        Intent intent = new Intent(this, ConsultorActivity.class);
        startActivity(intent);
        finish();
    }

    public void login(View view) {

        if( validateFields() ){
            User user = new User();
            user.setUsername( ((EditText)findViewById(R.id.login_text_user)).getText().toString() );
            user.setPassword( ((EditText)findViewById(R.id.login_text_password)).getText().toString() );
            try {

                this.mAuthenticator.logIn( Authenticator.ORDINARY_ACCOUNT, user );
                enterApplication();

            } catch (AuthException e) {
                Notifier.showMessage( this, e.getMessage() );
            }

        }

    }

    @Override
    protected void onDangerArea(Location currentLocation, float currentDistance) {
        this.mLoginButton.setEnabled(false);
    }

    @Override
    protected void onUsableArea(Location currentLocation, float currentDistance) {
        this.mLoginButton.setEnabled(true);
    }

    private boolean validateFields(){

        boolean areValidFields = false;
        EditText usernameField = (EditText)findViewById(R.id.login_text_user);

        if( ! TextUtils.isEmpty( usernameField.getText().toString() ) ){

            EditText passwordField = (EditText)findViewById(R.id.login_text_password);
            if( ! TextUtils.isEmpty( passwordField.getText().toString() ) ){
                areValidFields = true;
            }else{
                passwordField.setError("El campo contraseña no puede ser vacio");
                passwordField.requestFocus();
            }

        }else{
            usernameField.setError("El campo usuario no puede ser vacio");
            usernameField.requestFocus();
        }

        return areValidFields;
    }
}
