package com.example.sistemas.centralactuarios;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.skyfishjy.library.RippleBackground;



public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    ConnectionClass connectionClass;
    EditText edtuserid,edtpass;
    Button btnlogin;
    ProgressBar pbbar;

    @InjectView(R.id.input_nuempleado) EditText _nuempleadoText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        ImageView imageView = (ImageView) findViewById(R.id.centerImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rippleBackground.startRippleAnimation();
            }
        });


        connectionClass = new ConnectionClass();
        edtuserid = (EditText) findViewById(R.id.input_nuempleado);
        edtpass = (EditText) findViewById(R.id.input_password);
        btnlogin = (Button) findViewById(R.id.btn_login);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        btnlogin.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
            }
        });
    }
    public class DoLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String userid = edtuserid.getText().toString();
        String password = edtpass.getText().toString();

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this,r,Toast.LENGTH_SHORT).show();

            if (!validate()) {
                onLoginFailed();
                return;
            }

            if(isSuccess) {

                _loginButton.setEnabled(false);

                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                        R.style.Base_V7_Theme_AppCompat_Light_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Validando...");
                progressDialog.show();


                Intent i = new Intent(LoginActivity.this, Estado.class);
                startActivity(i);
                finish();
            }
        }

        public void onLoginSuccess() {
            _loginButton.setEnabled(true);
            finish();
        }

        public void onLoginFailed() {
            Toast.makeText(getBaseContext(), "Login fallido", Toast.LENGTH_LONG).show();

            _loginButton.setEnabled(true);
        }


        public boolean validate() {
            boolean valid = true;

            String nempleado = _nuempleadoText.getText().toString();
            String password = _passwordText.getText().toString();

            if (nempleado.isEmpty() || nempleado.length() < 0 || nempleado.length() > 5) {
                _nuempleadoText.setError("Número de Empleado No Registrado");
                valid = false;
            } else {
                _nuempleadoText.setError(null);
            }

            if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
                _passwordText.setError("Entre 4 y 20 Caracteres");
                valid = false;
            } else {
                _passwordText.setError(null);
            }

            return valid;
        }


        @Override
        protected String doInBackground(String... params) {
            {
                /*try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error en conexión con SQL server";
                    } else {
                        String query = "select * from Usertbl where UserId='" + userid + "' and password='" + password + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            z = "Registro Exitoso";
                            isSuccess=true;
                        }
                        else
                        {
                            z = "¡Registro Invalido!";
                            isSuccess = false;
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions";
                }*/
            }
            isSuccess=true;
            return z ;
        }
    }
}
