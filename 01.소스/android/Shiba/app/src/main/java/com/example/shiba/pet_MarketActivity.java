package com.example.shiba;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.ArrayList;

public class pet_MarketActivity extends AppCompatActivity {

    ImageButton buylist_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.pet_activity_market);

        }

    public void onClick(View v) {
        ArrayList cart_name_list = getIntent().getStringArrayListExtra("cart_name_list");
        ArrayList cart_count_list = getIntent().getStringArrayListExtra("cart_count_list");
        ArrayList cart_price_list = getIntent().getStringArrayListExtra("cart_price_list");

        switch (v.getId()) {

            case R.id.gansick_btn1:   //간식버튼을 눌렀을 때
                Intent i = new Intent(pet_MarketActivity.this, pet_GansickActivity.class);
                i.putExtra("cart_name_list", cart_name_list); //putextra로 간식 액티비티에 넘김
                i.putExtra("cart_price_list", cart_price_list);
                i.putExtra("cart_count_list", cart_count_list);
                startActivity(i);  //간식 액티비티로 넘어감
                finish();   //화면 종료
                break;

            case R.id.saryo_btn1:  //사료버튼을 눌렀을 때
                Intent i1 = new Intent(pet_MarketActivity.this, pet_SaryoActivity.class);
                i1.putExtra("cart_name_list", cart_name_list); //putextra로 사료 액티비티에 넘김
                i1.putExtra("cart_price_list", cart_price_list);
                i1.putExtra("cart_count_list", cart_count_list);
                startActivity(i1);  //사료 액티비티로 넘어감
                finish();   //화면 종료
                break;
        }
    }

    public void onBackPressed(){    //Back 버튼을 누르면
        Intent intent = new Intent(pet_MarketActivity.this, pet_MainActivity.class);
        startActivity(intent);  //메인 화면으로 넘어감
        finish();   //화면 종료
    }
}
