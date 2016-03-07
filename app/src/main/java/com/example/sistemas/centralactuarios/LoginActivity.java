package com.example.sistemas.centralactuarios;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class LoginActivity extends Activity {
    ConnectionClass connectionClass;

    @InjectView(R.id.input_nuempleado) EditText _nuempleadoText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.pbbar) ProgressBar _progressbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

         /*Animacion*/
        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        ImageView imageView = (ImageView) findViewById(R.id.centerImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rippleBackground.startRippleAnimation();
            }
        });

        connectionClass = new ConnectionClass();
        _progressbar.setVisibility(View.GONE);


        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
            }
        });
    }

        public class DoLogin extends AsyncTask<String,String,String> {
            String z = "";
            Boolean isSuccess = false;
            String userid = _nuempleadoText.getText().toString();
            String password = _passwordText.getText().toString();

            @Override
            protected void onPreExecute() {
                _progressbar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String r) {
                _progressbar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this,r,Toast.LENGTH_SHORT).show();

                if (!validate()) {
                    onLoginFailed();
                    return;
                }

                if(isSuccess) {
                    _loginButton.setEnabled(false);
                    onLoginSuccess();
                }
            }


            public void onLoginSuccess() {
                _loginButton.setEnabled(true);
                Intent i = new Intent(LoginActivity.this, Estado.class);
                startActivity(i);
                finish();}


            public void onLoginFailed() {
                Toast.makeText(getBaseContext(), "Login fallido", Toast.LENGTH_LONG).show();
                _loginButton.setEnabled(true);}


            public boolean validate() {
                boolean valid = true;

                String nempleado = _nuempleadoText.getText().toString();
                String password = _passwordText.getText().toString();

                if (nempleado.isEmpty() || nempleado.length() < 0 || nempleado.length() > 5) {
                    _nuempleadoText.setError("Escribe Número de Empleado");
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
                    try {
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
                    }
                }
                return z ;
            }
        }
    }
