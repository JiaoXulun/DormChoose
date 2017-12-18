package cn.edu.pku.jiaoxulun.dormchoose;

import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.edu.pku.jiaoxulun.util.NetUtil;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txt_login_usr;
    EditText txt_login_pwd;
    Button btn_login_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_login_usr = (EditText) findViewById(R.id.login_usr);
        txt_login_pwd = (EditText) findViewById(R.id.login_pwd);
        btn_login_submit = (Button) findViewById(R.id.login_submit);

        btn_login_submit.setOnClickListener(this);
        /*
        btn_login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr = txt_login_usr.getText().toString();
                if("".equals(usr)){
                    Toast.makeText(LoginActivity.this,"请输入学号",Toast.LENGTH_LONG).show();
                    return;
                }
                String pwd = txt_login_pwd.getText().toString();
                if("".equals(pwd)){
                    Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_LONG).show();
                    return;
                }
                final String address = NetUtil.USER_PATH + "Login?username=" + usr + "&password=" + pwd;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpsURLConnection con = null;

                        try {
                            URL url = new URL(address);
                            trustAllHosts();
                            con = (HttpsURLConnection) url.openConnection();
                            con.setHostnameVerifier(DO_NOT_VERIFY);
                            con.setRequestMethod("GET");
                            con.setConnectTimeout(8000);
                            con.setReadTimeout(8000);
                            InputStream in = con.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder response = new StringBuilder();
                            String str;
                            Log.d("Dorm", address);
                            while ((str = reader.readLine()) != null) {
                                response.append(str);
                                Log.d("Dorm", str);
                            }
                            String responseStr = response.toString();
                            Log.d("Dorm", responseStr);
                            decodeJSON(responseStr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (con != null) {
                                con.disconnect();
                            }
                        }
                    }
                }).start();
            }
        });*/
    }

    @Override
    public void onClick(View view){
        if(view.getId() == R.id.login_submit){
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE) {
                Log.d("Dorm", "网络O的K");
                onLogin();
            } else {
                Log.d("Dorm", "网络GG");
                Toast.makeText(LoginActivity.this, "网络GG！", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onLogin(){
        String usr = txt_login_usr.getText().toString();
        if("".equals(usr)){
            Toast.makeText(LoginActivity.this,"请输入学号",Toast.LENGTH_LONG).show();
            return;
        }
        String pwd = txt_login_pwd.getText().toString();
        if("".equals(pwd)){
            Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_LONG).show();
            return;
        }
        final String address = NetUtil.USER_PATH + "Login?username=" + usr + "&password=" + pwd;

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection con = null;

                try {
                    URL url = new URL(address);
                    trustAllHosts();
                    con = (HttpsURLConnection) url.openConnection();
                    con.setHostnameVerifier(DO_NOT_VERIFY);
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    Log.d("Dorm", address);
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("Dorm", str);
                    }
                    String responseStr = response.toString();
                    Log.d("Dorm", responseStr);
                    decodeJSON(responseStr);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        // Android use X509 cert
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    public void decodeJSON(String str){
        try{
            JSONObject jObj = new JSONObject(str);
            int errcode = jObj.getInt("errcode");
            if(errcode == 0){
                Log.d("Dorm", "666");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                MainActivity.usr = txt_login_usr.getText().toString();
            } else {
                Looper.prepare();
                Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_LONG).show();
                Looper.loop();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
