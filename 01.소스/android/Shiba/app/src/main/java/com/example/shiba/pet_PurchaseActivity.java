package com.example.shiba;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import static android.os.StrictMode.setThreadPolicy;

public class pet_PurchaseActivity extends AppCompatActivity {

    SocketChannel socketChannel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.pet_activity_purchase);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        setThreadPolicy(policy);


    }


    public void sendData(String data){  //데이터 발신
        Log.d("Send", data);

        try {
            ByteBuffer buffer = null;
            Charset charset = Charset.forName("UTF-8");
            buffer = charset.encode(data);

            socketChannel.write(buffer);

            buffer = ByteBuffer.allocate(15000);
            int len = socketChannel.read(buffer);
            buffer.flip();

            String recive = charset.decode(buffer).toString();


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void socketConnect(String host, int port){   //소켓 연결
        try{
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            socketChannel.connect(new InetSocketAddress(host, port));

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {  //종료
        if(socketChannel.isOpen()){
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.finish();
    }

    public void onBackPressed(){
        //마켓 화면 이동
        Intent intent = new Intent(pet_PurchaseActivity.this, pet_MarketActivity.class);
        startActivity(intent);
        finish();
    }
}
