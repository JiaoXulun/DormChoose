package cn.edu.pku.jiaoxulun.dormchoose;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static String usr = null;
    TextView txt_usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_usr = (TextView)findViewById(R.id.usr_show);
        txt_usr.setText(usr);
    }
}
