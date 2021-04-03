package com.example.shiba;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class pet_CartActivity extends AppCompatActivity {

    TextView num, total_price;
    Button buy_button;
    EditText pin_num, name;
    ListView cart_list;
    ArrayList<String> cart_items;
    Context context = this;
    Integer total = 0;

    SocketChannel socketChannel = null;
    SocketChannel socketChannel2 = null;

    ArrayList<add_Item> itemArrayList = new ArrayList<>();
    private ListviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.pet_activity_mycart);

        buy_button = (Button)findViewById(R.id.Buy);
        total_price = (TextView)findViewById(R.id.total_price);
        pin_num = (EditText)findViewById(R.id.pin_num);
        name = (EditText)findViewById(R.id.name);
        cart_list = (ListView)findViewById(R.id.cart_list);

        JSONObject jsonObject = new JSONObject();
        final JSONArray jsonArray = new JSONArray();

        ArrayList cart_name_list = getIntent().getStringArrayListExtra("cart_name_list");
        ArrayList cart_count_list = getIntent().getStringArrayListExtra("cart_count_list");
        ArrayList cart_price_list = getIntent().getStringArrayListExtra("cart_price_list");

        for(int i = 0; i < cart_name_list.size(); i++){
            String cart_name = cart_name_list.get(i).toString();
            String cart_price = cart_price_list.get(i).toString();
            String cart_count = cart_count_list.get(i).toString();
            add_Item a = new add_Item(cart_name, cart_price, cart_count);
            itemArrayList.add(a);

            total = total + Integer.parseInt(cart_price);

            try {
                jsonObject.put("name", cart_name);
                jsonObject.put("count", cart_count);
                jsonObject.put("price", cart_price);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String list = jsonObject.toString();
            Log.d("data",list);
            jsonArray.put(list);
        }

        socketConnect2("192.168.13.17", 8402);     //소켓 연결
        socketConnect("192.168.13.15", 8205);     //7조 소켓 연결
        sendData("Shiba connected!");



        adapter = new ListviewAdapter(this, itemArrayList); //Adapter 설정
        cart_list.setAdapter(adapter);

        total_price.setText(total.toString());


        buy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("data2", String.valueOf(jsonArray));
                final String send_name = name.getText().toString();
                final String send_pin = pin_num.getText().toString();
                final String send_price = total_price.getText().toString();

                if(send_name.length() == 0){
                    Toast.makeText(context, "구매자 이름을 확인해주세요.", Toast.LENGTH_LONG).show();
                } else if (send_pin.length() == 0){
                    Toast.makeText(context, "핀 번호을 확인해주세요.", Toast.LENGTH_LONG).show();
                }else {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    // 제목셋팅
                    alertDialogBuilder.setTitle("※주의※");

                    // AlertDialog 셋팅
                    alertDialogBuilder
                            .setMessage("정말 구매하시겠시바?")
                            .setCancelable(false)
                            .setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int id) {

                                            String data = "dog " + send_pin + " " + Integer.parseInt(send_price) + " " + send_name;
                                            sendData(data);
                                            sendData2(String.valueOf(jsonArray));


                                        }
                                    })
                            .setNegativeButton("취소",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // 다이얼로그를 취소한다
                                            dialog.cancel();
                                        }
                                    });

                    // 다이얼로그 생성
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // 다이얼로그 보여주기
                    alertDialog.show();
                }
            }
        });
    }

    public void sendData(String data) {     //데이터 발신
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
            receiveData(recive);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendData2(String data) {     //데이터 발신
        Log.d("Send", data);

        try {
            ByteBuffer buffer = null;
            Charset charset = Charset.forName("UTF-8");
            buffer = charset.encode(data);

            socketChannel2.write(buffer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void socketConnect(String host, int port) {
        //연결 부분
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            socketChannel.connect(new InetSocketAddress(host, port));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void socketConnect2(String host, int port) {
        //연결 부분
        try {
            socketChannel2 = SocketChannel.open();
            socketChannel2.configureBlocking(true);
            socketChannel2.connect(new InetSocketAddress(host, port));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        if (socketChannel.isConnected()) {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }else if(socketChannel2.isConnected()){
            try{
                socketChannel2.close();
            }catch (IOException e){
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        super.finish();
    }

    public void receiveData(String receive){    //확인 데이터를 받으면
        Log.d("receive", receive);

        switch (receive){
            case "nm":
                Toast.makeText(this, "잔액 부족!", Toast.LENGTH_SHORT).show();
            case "ok":
                Toast.makeText(pet_CartActivity.this, "결제 완료!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(pet_CartActivity.this, pet_MainActivity.class);
                startActivity(intent);
                finish();
            case "fail":
                Toast.makeText(this, "전송 에러, 다시 입력!", Toast.LENGTH_SHORT).show();
        }


    }

    //리스트 뷰 설정
    private class ListviewAdapter extends BaseAdapter {
        Context context;
        ArrayList<add_Item> itemlist = new ArrayList<>();

        public ListviewAdapter(Context context, ArrayList<add_Item> itemlist) {
            this.context = context;
            this.itemlist = itemlist;
        }

        @Override
        public int getCount() {
            return itemlist.size();
        }

        @Override
        public Object getItem(int i) {
            return itemlist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) convertView = new ListviewItem(context);
            ((ListviewItem)convertView).setData(itemlist.get(position));
            return convertView;
        }
    }

    //리스트뷰 속 아이템 설정
    private class ListviewItem extends LinearLayout {
        TextView list_select_name, list_select_price, list_select_count;

        public ListviewItem (Context context){
            super(context);
            init(context);
        }

        private void init(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.pet_list_item, this);
            list_select_name = (TextView)findViewById(R.id.select_name);
            list_select_price = (TextView)findViewById(R.id.select_price);
            list_select_count = (TextView)findViewById(R.id.select_count);
        }

        public void setData(add_Item one) {

            list_select_name.setText(one.getName());
            list_select_price.setText(one.getPrice());
            list_select_count.setText(one.getCount());
        }
    }

    private class add_Item {
        private String name, price, count;

        public add_Item(String name, String price, String count) {
            this.name = name;
            this.price = price;
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public String getCount() {
            return count;
        }

    }



    public void onBackPressed(){
        ArrayList cart_name_list = getIntent().getStringArrayListExtra("cart_name_list");
        ArrayList cart_count_list = getIntent().getStringArrayListExtra("cart_count_list");
        ArrayList cart_price_list = getIntent().getStringArrayListExtra("cart_price_list");

        Intent intent2 = new Intent(pet_CartActivity.this, pet_MarketActivity.class);
        intent2.putExtra("cart_name_list", cart_name_list);
        intent2.putExtra("cart_price_list", cart_price_list);
        intent2.putExtra("cart_count_list", cart_count_list);

        startActivity(intent2);     //마켓 화면으로 전환
        finish();
    }
}

