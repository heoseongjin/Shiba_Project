package com.example.shiba;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.os.StrictMode.setThreadPolicy;


public class pet_MainActivity extends AppCompatActivity{


    SocketChannel socketChannel = null;
    LinearLayout auto_layout;
    TextView auto_text;
    ArrayList<String> cart_name_list, cart_price_list, cart_count_list;
    final long INTERVAL_TIME = 1000;
    long previousTime = 0;
    Integer temp = 0;

    int auto_cnt = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.pet_activity_main);

            auto_layout = (LinearLayout)findViewById(R.id.auto_layout);
            auto_text = (TextView)findViewById(R.id.auto_text);

            cart_name_list = new ArrayList<>();
            cart_price_list = new ArrayList<>();
            cart_count_list = new ArrayList<>();

            // 스레드 생성하고 시작(자동 모드)
            //AutoThread automode = new AutoThread();
            //automode.setDaemon(true);
            //automode.start();

            // 쓰레드 규칙정의
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            setThreadPolicy(policy);
            socketConnect("192.168.13.17", 8402);

            //WebView 선언
            WebView webView = (WebView) findViewById(R.id.webView);

            webView.setPadding(0, 0, 0, 0);
            //webView.setInitialScale(30);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setSupportZoom(true);
            webView.setHorizontalScrollBarEnabled(false);
            webView.setVerticalScrollBarEnabled(true);
            webView.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return (event.getAction() == MotionEvent.ACTION_MOVE);
                }
            });
            webView.getSettings().setJavaScriptEnabled(true);   //자바스크립트사용
            webView.getSettings().setLoadWithOverviewMode(true);// 컨텐츠크기가 너메크면 스크린크기에 맞춤
            webView.getSettings().setUseWideViewPort(true);     //meta viewport 사용허가
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

            String url = "http://192.168.13.13/5/dogvideo.html";
            webView.loadUrl(url);
            //String imgSrcHtml = "<html><img src='" + url + "' /></html>";
            // String imgSrcHtml = url;
            //webView.loadData(imgSrcHtml, "text/html", "UTF-8");
        }


        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.auto_button:  //Auto 버튼을 눌렀을 때
                    //auto_layout.setVisibility(View.VISIBLE);
                    sendData("a");
                    break;

                case R.id.ryo_button:    //사료모양 버튼을 눌렀을 때 (간식을 던져줌)
                    sendData("1");
                    break;

                case R.id.left_button:      // '<' 버튼을 눌렀을 때
                    sendData("<");
                    break;

                case R.id.right_button:     // '>' 버튼을 눌렀을 때
                    sendData(">");
                    break;

                case R.id.market_button:   //M(마켓)버튼을 눌렀을 때
                    Intent intent4 = new Intent(pet_MainActivity.this, pet_MarketActivity.class);
                    intent4.putExtra("cart_name_list", cart_name_list);
                    intent4.putExtra("cart_count_list", cart_count_list);
                    intent4.putExtra("cart_price_list", cart_price_list);
                    startActivity(intent4);  //마켓 액티비티로 넘어감
                    finish();
                    break;

                case R.id.camera_button:  //카메라 버튼을 눌렀을 때
                    Intent intent = new Intent(pet_MainActivity.this, pet_ShotActivity.class);
                    temp += 1;
                    intent.putExtra("temp", temp);
                    startActivity(intent);  //SHOT액티비티로 넘어감

                    break;

                case R.id.web_btn:
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.13.13/5/"));
                    startActivity(webIntent);


            }
        }

        //헿 내가 만들었당
        public void sendData(String data){
            Log.d("Send", data);

            try {
                ByteBuffer buffer = null;
                Charset charset = Charset.forName("UTF-8");
                buffer = charset.encode(data);

                socketChannel.write(buffer);

                buffer = ByteBuffer.allocate(15000);
                int len = socketChannel.read(buffer);
                buffer.flip();

                String recieve = charset.decode(buffer).toString();

            } catch (Exception e){
                e.printStackTrace();
            }
            //socketTask.SendDataToNetwork(data);
        }

/*
        private class AutoThread extends Thread{
        @Override
        public void run(){
            while(true){
                try {

                    //handler.sendEmptyMessage(0);

                    ByteBuffer buffer = null;
                    Charset charset = Charset.forName("UTF-8");
                    buffer = charset.encode("c");

                    socketChannel.write(buffer);

                    buffer = ByteBuffer.allocate(15000);
                    int len = socketChannel.read(buffer);
                    buffer.flip();

                    final String recive = charset.decode(buffer).toString();

                    if (recive == "o")
                        if (auto_cnt == 4)
                            break;
                        auto_cnt++;
                    // 사용하고자 하는 코드

                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what ==0){
                switch (auto_cnt) {
                    case 0:
                        auto_text.setText("Auto Mode\n○ ○ ○");
                        break;
                    case 1:
                        auto_text.setText("Auto Mode\n● ○ ○");
                        break;
                    case 2:
                        auto_text.setText("Auto Mode\n● ● ○");
                        break;
                    case 3:
                        auto_text.setText("Auto Mode\n● ● ●\nEnd");
                        break;
                }
                if (auto_cnt == 3)
                    auto_layout.setVisibility(View.INVISIBLE);
            }
        }
    };
*/

    //메인 깔끔해지라고 이것도 함수로 만들어버림 ㅎ(socketchannel 연결 부분)
    public void socketConnect(String host, int port){
        //연결 부분
        try{
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            socketChannel.connect(new InetSocketAddress(host, port));

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();

        if((currentTime - previousTime) <= INTERVAL_TIME) {
            super.onBackPressed();
        } else {
            previousTime = currentTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

}