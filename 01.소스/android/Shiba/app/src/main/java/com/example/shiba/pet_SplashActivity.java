package com.example.shiba;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;


public class pet_SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.pet_splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashHandler(), 1500); //1.5초 후 hd handler 실행행
    }
    private class splashHandler implements Runnable {
        @Override
        public void run() {
            //로딩이 끝나면 메인화면으로 넘어감
            startActivity(new Intent(getApplicationContext(), pet_MainActivity.class));
            pet_SplashActivity.this.finish();
        }
    }

    public void onBackPressed(){
        //넘어갈 때 뒤로가기버튼 못 누르게 하기 위함
    }

}

