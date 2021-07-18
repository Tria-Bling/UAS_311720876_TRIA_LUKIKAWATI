    package com.example.muhammadalfiannovanto_311710549_uas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

    public class MainActivity extends AppCompatActivity {

        EditText username, email, password, confPassword;
        Button login, register;
        ProgressDialog progressDialog;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            username = (EditText) findViewById(R.id.edit_usernameRegister);
            email = (EditText) findViewById(R.id.edit_emailRegister);
            password = (EditText) findViewById(R.id.edit_passwordRegister);
            confPassword = (EditText) findViewById(R.id.edit_confPasswordRegister);
            login = (Button) findViewById(R.id.loginRegister);
            register = (Button) findViewById(R.id.registerRegister);
            progressDialog = new ProgressDialog(MainActivity.this);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("PO.Murni Jaya Cibarusah");
            }
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginIntent = new Intent(MainActivity.this, login.class);
                    startActivity(loginIntent);
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sUsername = username.getText().toString();
                    String sEmail = email.getText().toString();
                    String sPassword = password.getText().toString();
                    String sconfPassword = confPassword.getText().toString();



                    if (sPassword.equals(sconfPassword) && !sPassword.equals("")) {
                        CreateDataToServer(sUsername, sEmail, sPassword);
                        Intent loginIntent = new Intent(MainActivity.this, login.class);
                        startActivity(loginIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), "gagal password tidak cocok", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    public void CreateDataToServer(final String username, final String email, final String password) {
        if (checkNetworkConnection()) {
            progressDialog.show();
            StringRequest stringRequest= new StringRequest(Request.Method.POST, Contract.SERVER_REGISTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if(resp.equals("[{\"status\":\"OK\"}]")){
                                    Toast.makeText(getApplicationContext(), "Registrasi Berhasil", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                   Map<String, String> params = new HashMap<>();
                   params.put("username", username);
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };

            VolleyConnection.getInstance(MainActivity.this).addToReuestQue(stringRequest);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            }, 2000);
        } else {
            Toast.makeText(getApplicationContext(), "Tidak Ada Koneksi Internet ", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
