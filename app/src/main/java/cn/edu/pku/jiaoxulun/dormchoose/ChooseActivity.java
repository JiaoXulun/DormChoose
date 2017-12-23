package cn.edu.pku.jiaoxulun.dormchoose;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.edu.pku.jiaoxulun.bean.RoomInfo;
import cn.edu.pku.jiaoxulun.bean.StudentInfo;
import cn.edu.pku.jiaoxulun.util.NetUtil;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int UPDATE_ROOM_INFO = 2;
    public static String usr = null;
    public static String gender = null;

    TextView txt_remain_5;
    TextView txt_remain_13;
    TextView txt_remain_14;
    TextView txt_remain_8;
    TextView txt_remain_9;

    Button btn_back;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_ROOM_INFO:
                    updateRoomInfo((RoomInfo) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);


        initView();
    }

    void initView() {
        txt_remain_5 = (TextView) findViewById(R.id.remain_5);
        txt_remain_13 = (TextView) findViewById(R.id.remain_13);
        txt_remain_14 = (TextView) findViewById(R.id.remain_14);
        txt_remain_8 = (TextView) findViewById(R.id.remain_8);
        txt_remain_9 = (TextView) findViewById(R.id.remain_9);

        btn_back = (Button) findViewById(R.id.btn_back);

        btn_back.setOnClickListener(this);

        getJSON();
    }

    @Override
    public void onClick(View view){
        if(view.getId()==R.id.btn_back){
            Intent intent = new Intent(ChooseActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            MainActivity.usr = this.usr;
        }
    }

    void updateRoomInfo(RoomInfo roomInfo) {
        txt_remain_5.setText(String.valueOf(roomInfo.getBuilding_5()));
        txt_remain_13.setText(String.valueOf(roomInfo.getBuilding_13()));
        txt_remain_14.setText(String.valueOf(roomInfo.getBuilding_14()));
        txt_remain_8.setText(String.valueOf(roomInfo.getBuilding_8()));
        txt_remain_9.setText(String.valueOf(roomInfo.getBuilding_9()));
    }

    public void getJSON() {
        int gender = ("男".equals(this.gender)) ? 1 : 2;
        final String address = NetUtil.USER_PATH + "getRoom?gender=" + gender;

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection con = null;
                RoomInfo roomInfo = new RoomInfo();
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
                    Log.d("Dorm2", responseStr);
                    roomInfo = decodeJSON(responseStr);
                    if (roomInfo != null) {
                        Log.d("myWeather", roomInfo.toString());

                        Message msg = new Message();
                        msg.what = UPDATE_ROOM_INFO;
                        msg.obj = roomInfo;
                        mHandler.sendMessage(msg);
                    }
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
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        }};

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

    public RoomInfo decodeJSON(String str) {
        RoomInfo roomInfo = new RoomInfo();
        try {
            JSONObject jObj = new JSONObject(str);
            JSONObject jData = jObj.getJSONObject("data");
            int errcode = jObj.getInt("errcode");
            if (errcode == 0) {
                roomInfo.setBuilding_5(jData.getInt("5"));
                roomInfo.setBuilding_13(jData.getInt("13"));
                roomInfo.setBuilding_14(jData.getInt("14"));
                roomInfo.setBuilding_8(jData.getInt("8"));
                roomInfo.setBuilding_9(jData.getInt("9"));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return roomInfo;
    }
}
