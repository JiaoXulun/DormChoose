package cn.edu.pku.jiaoxulun.dormchoose;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import cn.edu.pku.jiaoxulun.bean.ChooseInfo;
import cn.edu.pku.jiaoxulun.bean.RoomInfo;
import cn.edu.pku.jiaoxulun.bean.StudentInfo;
import cn.edu.pku.jiaoxulun.util.NetUtil;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, Spinner.OnItemSelectedListener {
    private static final int UPDATE_ROOM_INFO = 2;
    public static String usr = null;
    public static String gender = null;

    int choose_amount = 1;
    int buildingNo = 5;

    TextView txt_remain_5;
    TextView txt_remain_13;
    TextView txt_remain_14;
    TextView txt_remain_8;
    TextView txt_remain_9;

    Spinner building_choose;

    EditText resident_stu1id;
    EditText resident_v1code;
    EditText resident_stu2id;
    EditText resident_v2code;
    EditText resident_stu3id;
    EditText resident_v3code;

    Button btn_back;
    Button btn_submit;
    Button btn_add;
    Button btn_add2;
    Button btn_remove2;
    Button btn_remove3;

    RadioGroup radio_choose;
    RadioButton radio_personal;
    RadioButton radio_collective;

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
        btn_submit = (Button) findViewById(R.id.btn_submit);

        building_choose = (Spinner)findViewById(R.id.building_choose);

        radio_choose = (RadioGroup) findViewById(R.id.radio_choose);
        radio_personal = (RadioButton) findViewById(R.id.radio_personal);
        radio_collective = (RadioButton) findViewById(R.id.radio_collective);

        btn_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        building_choose.setOnItemSelectedListener(this);

        radio_choose.setOnCheckedChangeListener(this);

        getJSON();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_back:
                Intent intent = new Intent(ChooseActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                MainActivity.usr = this.usr;
                break;
            case R.id.resident_add:
                final LinearLayout container = (LinearLayout) findViewById(R.id.resident_2);
                View child = LayoutInflater.from(ChooseActivity.this).inflate(R.layout.info_resident_2, container, false);
                container.addView(child);
                container.invalidate();
                btn_add.setVisibility(View.INVISIBLE);
                choose_amount++;
                Log.d("DormInt", "" + choose_amount);
                btn_add2 = (Button) findViewById(R.id.resident_add_2);
                btn_remove2 = (Button) findViewById(R.id.resident_remove_2);
                resident_stu2id = (EditText)findViewById(R.id.resident_stu2id);
                resident_v2code = (EditText)findViewById(R.id.resident_v2code);
                btn_add2.setOnClickListener(this);
                btn_remove2.setOnClickListener(this);
                break;
            case R.id.resident_remove_2:
                final LinearLayout container2 = (LinearLayout) findViewById(R.id.resident_2);
                container2.removeAllViews();
                choose_amount--;
                Log.d("DormInt", "" + choose_amount);
                btn_add.setVisibility(View.VISIBLE);
                break;
            case R.id.resident_add_2:
                final LinearLayout container3 = (LinearLayout) findViewById(R.id.resident_3);
                View child3 = LayoutInflater.from(ChooseActivity.this).inflate(R.layout.info_resident_3, container3, false);
                container3.addView(child3);
                container3.invalidate();
                btn_add2.setVisibility(View.INVISIBLE);
                btn_remove2.setVisibility(View.INVISIBLE);
                choose_amount++;
                Log.d("DormInt", "" + choose_amount);
                btn_remove3 = (Button) findViewById(R.id.resident_remove_3);
                resident_stu3id = (EditText)findViewById(R.id.resident_stu3id);
                resident_v3code = (EditText)findViewById(R.id.resident_v3code);
                btn_remove3.setOnClickListener(this);
                break;
            case R.id.resident_remove_3:
                final LinearLayout container4 = (LinearLayout) findViewById(R.id.resident_3);
                container4.removeAllViews();
                choose_amount--;
                Log.d("DormInt", "" + choose_amount);
                btn_add2.setVisibility(View.VISIBLE);
                btn_remove2.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_submit:
                ChooseInfo chooseInfo = new ChooseInfo();
                switch (choose_amount) {
                    case 4:
                        chooseInfo.setStu3id(resident_stu3id.getText().toString());
                        chooseInfo.setV3code(resident_v3code.getText().toString());
                    case 3:
                        chooseInfo.setStu2id(resident_stu2id.getText().toString());
                        chooseInfo.setV2code(resident_v2code.getText().toString());
                    case 2:
                        chooseInfo.setStu1id(resident_stu1id.getText().toString());
                        chooseInfo.setV1code(resident_v1code.getText().toString());
                    case 1:
                        chooseInfo.setNum(choose_amount);
                        chooseInfo.setBuildingNo(buildingNo);
                        chooseInfo.setStuid(usr);
                }
                Log.d("DormPost",chooseInfo.toString());
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        final LinearLayout container = (LinearLayout) findViewById(R.id.info_collective);

        switch (checkedId) {
            case R.id.radio_personal:
                container.removeAllViews();
                choose_amount = 1;
                Log.d("DormRadio", "11111");
                break;
            case R.id.radio_collective:
                Log.d("DormRadio", "22222");
                choose_amount = 2;
                View child = LayoutInflater.from(ChooseActivity.this).inflate(R.layout.info_resident, container, false);
                container.addView(child);
                container.invalidate();
                btn_add = (Button) findViewById(R.id.resident_add);
                resident_stu1id = (EditText)findViewById(R.id.resident_stu1id);
                resident_v1code = (EditText)findViewById(R.id.resident_v1code);
                btn_add.setOnClickListener(this);
                break;
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                buildingNo = 5;
                break;
            case 1:
                buildingNo = 13;
                break;
            case 2:
                buildingNo = 14;
                break;
            case 3:
                buildingNo = 8;
                break;
            case 4:
                buildingNo = 9;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
