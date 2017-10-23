package com.example.wb773.udpsample20171023;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.result_text);

        findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textView.setText("");

                UdpSenderTask udp = new UdpSenderTask();
                udp.setOnCallBack(new UdpSenderTask.CallBackTask(){
                    @Override
                    public void CallBack(String result) {
                        textView.setText(result);
                    }
                });

                udp.execute();
            }
        });

    }



}
