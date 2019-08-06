package com.example.HandlerDemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "MainAcitivity";
    private TextView mTipTv;
    private Button mDownloadBt;
    private  boolean isDownloading = false;
    public final int MSG_DOWN_FAIL = 1;
    public final int MSG_DOWN_SUCCESS = 2;
    public final int MSG_DOWN_START = 3;

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case MSG_DOWN_FAIL:
                    mTipTv.setText("download fial");
                    break;
                case MSG_DOWN_START:
                    mTipTv.setText("download start");
                    break;
                case MSG_DOWN_SUCCESS:
                    mTipTv.setText("doenload success");
                    break;

            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        mTipTv = (TextView)findViewById(R.id.textView);
        mDownloadBt = (Button) findViewById(R.id.buttonSend);
        mDownloadBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSend:
                if (!isDownloading){
                    //new MyThread().start();
//                    new postThread().start();
                    new obtainThread().start();
                }
                break;
            default:
                break;
        }
    }

    class MyThread extends Thread {
        @Override
        public void run(){

            isDownloading = true;
            Log.d(TAG,"Mythread start run");
            //send the message to mHander
            mHandler.sendEmptyMessage(MSG_DOWN_START);
            try {
                //let the thread sleep 3s
                Thread.sleep(3000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            Message msg = new Message();
            msg.what = MSG_DOWN_SUCCESS;
            mHandler.sendMessage(msg);
            isDownloading = false;
            Log.d(TAG,"MyThread stop run");

        }
    }

    class postThread extends Thread{
        @Override
        public void run(){
            isDownloading = true;
            Log.d(TAG,"run threadid = "+Thread.currentThread().getId()+
            ",name="+Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"Runnable threadid = " + Thread.currentThread().getId()+
                            ",name="+Thread.currentThread().getName());

                    //update ui
                    mTipTv.setText("download success");
                }
            });
            isDownloading = false;
        }
    }

    class obtainThread extends Thread{
        @Override
        public void run (){
            isDownloading = true;
            mHandler.obtainMessage(MSG_DOWN_START).sendToTarget();
            try{
                Thread.sleep(3000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            mHandler.obtainMessage(MSG_DOWN_SUCCESS).sendToTarget();
            isDownloading = false;
        }
    }

}
