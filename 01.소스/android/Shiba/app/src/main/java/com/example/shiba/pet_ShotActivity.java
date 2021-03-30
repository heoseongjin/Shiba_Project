package com.example.shiba;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class pet_ShotActivity extends AppCompatActivity {
    ImageView imageView;     //shot 액티비티의 메인 이미지뷰
    Bitmap bitmap;           // 이미지뷰를 bitmap으로 임시저장 하기 위해 썼음

    Button back, download, sns;
    ImageButton web_save_btn;

    private ConnectFtp ConnectFTP_WEB;
    private String currentPath = null;
    private String folder;
    private int temp;


    @SuppressLint("WrongThread")    //이거 지우면 실행이 안됨
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.pet_activity_shot);


        imageView = (ImageView) findViewById(R.id.imageView);


        temp =  getIntent().getExtras().getInt("temp");


        //안드로이드에서 네트워크와 관련된 작업을 할 때,
        //반드시 메인 Thread가 아닌 별도의 작업 Thread를 생성하여 작업해야 한다

        Thread mThread = new Thread() {
            public void run() {
                try {

                  URL url = new URL("http://192.168.13.31:8409/?action=snapshot");
                   // URL url = new URL("https://cdn.pixabay.com/photo/2013/10/28/18/51/brandenburger-tor-201939_960_720.jpg");

                    //web에서 이미지를 가져온 뒤
                    //ImageViwe에 지정할 bitmap을 만든다

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();     //url 개방
                    conn.setDoInput(true);//서버로부터 응답수신
                    conn.connect();    //연결 !

                    InputStream is = conn.getInputStream();//Inputstream값 가져오기
                    bitmap = BitmapFactory.decodeStream(is); //Bitmap으로 변환


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mThread.start();//Thread 실행

        try {
            //메인 스레드는 별도의 작업 스레드가 작업을 완료할 때까지 기다림
            //join()을 호출해 별도의 작업 스레드가 종료될 때까지 메인 스레드가 기다리게 함
            mThread.join();

            //작업 스레드에서 이미지를 불러오는 작업을 완료한 뒤
            //UI 작업을 할 수 있는 메인 스레드에서 이미지뷰에 이미지를 지정함
            imageView.setImageBitmap(bitmap);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        final String image_path = ImageSave();



    }

    private String dateName(long dateTaken){    //시간 저장 함수
        Date date = new Date(dateTaken);
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");  //날짜 시간단위 저장
        return dateFormat.format(date);
    }

    public String ImageSave(){
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/Shiba";  //루트지정
        File myDir = new File(root);  //파일새로 생성위해
        String fname = ("image" + temp);   //shiba_날짜:시간.jpg로 저장됨
        if (!myDir.exists()){
            myDir.mkdirs();
        }
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();              //똑같은 이름이 있으면 지워버림
        String image_path = root + "/" + fname;
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));  //갤러리에 저장되도록 스캔해서 업데이트
        //이걸 안해주면 sd카드에 저장은 되는데
        //갤러리에 안 올라감

        try {
            FileOutputStream out = new FileOutputStream(file);         //마 ! 니가 전학생 파일이가!! 싸우자!! 나와라 마 !!
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);   //퀄 90으로 비트맵을 제피지로 변환
            //여기서 jepg랑 jpg는 같은 형식이란거 안 비밀
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image_path;
    }



    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.picture_button:  //갤러리 버튼을 눌렀을 때

                Intent moonintent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media")); //갤러리 이동
                startActivity(moonintent); //액티비티 바꿈
                break;

        }
    }


}