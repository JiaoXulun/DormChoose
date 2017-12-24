package cn.edu.pku.jiaoxulun.dormchoose;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.Callable;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.edu.pku.jiaoxulun.bean.StudentInfo;
import cn.edu.pku.jiaoxulun.util.NetUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int UPDATE_STUDENT_INFO = 1;
    public static String usr = null;
    public static String stringJSON = null;

    TextView txt_info_name;
    TextView txt_info_studentid;
    TextView txt_info_gender;
    TextView txt_info_grade;
    TextView txt_info_location;
    TextView txt_info_vcode;
    TextView txt_info_building;
    TextView txt_info_room;

    Button btn_exit;
    Button btn_choose;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_STUDENT_INFO:
                    updateStudentInfo((StudentInfo) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    void initView() {
        txt_info_name = (TextView) findViewById(R.id.info_name);
        txt_info_studentid = (TextView) findViewById(R.id.info_studentid);
        txt_info_gender = (TextView) findViewById(R.id.info_gender);
        txt_info_grade = (TextView) findViewById(R.id.info_grade);
        txt_info_location = (TextView) findViewById(R.id.info_location);
        txt_info_vcode = (TextView) findViewById(R.id.info_vcode);
        txt_info_building = (TextView) findViewById(R.id.info_building);
        txt_info_room = (TextView) findViewById(R.id.info_room);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_choose = (Button) findViewById(R.id.btn_choose);

        btn_exit.setOnClickListener(this);
        btn_choose.setOnClickListener(this);

        /*StudentInfo studentInfo = decodeJSON(getJSON());

        txt_info_name.setText(studentInfo.getName());
        txt_info_studentid.setText(studentInfo.getStudentid());
        txt_info_gender.setText(studentInfo.getGender());
        txt_info_grade.setText(studentInfo.getGrade());
        txt_info_location.setText(studentInfo.getLocation());
        txt_info_vcode.setText(studentInfo.getVcode());
        txt_info_building.setText(studentInfo.getBuilding());
        txt_info_room.setText(studentInfo.getRoom());*/
        //Log.d("Dorm2",getJSON());
        getJSON();


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_exit) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (view.getId() == R.id.btn_choose) {
            Log.d("Dorm2", "choose");
            Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
            startActivity(intent);
            ChooseActivity.usr = this.usr;
            ChooseActivity.gender = txt_info_gender.getText().toString();
            finish();
        }
    }

    void updateStudentInfo(StudentInfo studentInfo) {
        txt_info_name.setText(studentInfo.getName());
        txt_info_studentid.setText(studentInfo.getStudentid());
        txt_info_gender.setText(studentInfo.getGender());
        txt_info_grade.setText(studentInfo.getGrade());
        txt_info_location.setText(studentInfo.getLocation());
        txt_info_vcode.setText(studentInfo.getVcode());
        txt_info_building.setText(studentInfo.getBuilding());
        txt_info_room.setText(studentInfo.getRoom());

        if (studentInfo.getBuilding() != "未选择") {
            btn_choose.setEnabled(false);
            btn_choose.setText("选择完毕，不能更改");
            btn_choose.setBackgroundColor(getResources().getColor(R.color.grey));
        }
    }

    public void getJSON() {
        final String address = NetUtil.USER_PATH + "getDetail?stuid=" + usr;

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection con = null;
                StudentInfo studentInfo = new StudentInfo();
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
                    studentInfo = decodeJSON(responseStr);
                    if (studentInfo != null) {
                        Log.d("myWeather", studentInfo.toString());

                        Message msg = new Message();
                        msg.what = UPDATE_STUDENT_INFO;
                        msg.obj = studentInfo;
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

    public StudentInfo decodeJSON(String str) {
        StudentInfo studentInfo = new StudentInfo();
        try {
            JSONObject jObj = new JSONObject(str);
            JSONObject jData = jObj.getJSONObject("data");
            int errcode = jObj.getInt("errcode");
            if (errcode == 0) {
                studentInfo.setName(jData.getString("name"));
                studentInfo.setStudentid(jData.getString("studentid"));
                studentInfo.setGender(jData.getString("gender"));
                studentInfo.setGrade(jData.getString("grade"));
                studentInfo.setLocation(jData.getString("location"));
                studentInfo.setVcode(jData.getString("vcode"));

                if (jData.has("building")) {
                    studentInfo.setBuilding(jData.getString("building"));
                    studentInfo.setRoom(jData.getString("room"));
                } else {
                    studentInfo.setBuilding("未选择");
                    studentInfo.setRoom("未选择");
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return studentInfo;
    }
}
