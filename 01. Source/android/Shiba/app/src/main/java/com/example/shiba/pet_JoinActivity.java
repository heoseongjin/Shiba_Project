package com.example.shiba;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class pet_JoinActivity extends AppCompatActivity {

    Button btn_join, btn_main;
    EditText edt_id, edt_name, edt_password, edt_repassword, edt_phone, edt_address;
    Spinner spn_department;
    String myJSON, select_department;
    JSONArray peoples = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_activity_join);

        btn_join = (Button) findViewById(R.id.join);
        btn_main = (Button) findViewById(R.id.main);
        edt_id = (EditText) findViewById(R.id.id);
        edt_name = (EditText) findViewById(R.id.name);
        edt_password = (EditText) findViewById(R.id.password);
        edt_repassword = (EditText) findViewById(R.id.re_password);
        edt_phone = (EditText) findViewById(R.id.phone);
        edt_address = (EditText) findViewById(R.id.address);
        spn_department = (Spinner) findViewById(R.id.department);
        // ArrayAdapter spn_department_adapter = ArrayAdapter.createFromResource(this, R.array.Department, android.R.layout.simple_dropdown_item_1line);
        // spn_department_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // spn_department.setAdapter(spn_department_adapter);

        spn_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    select_department = "home";
                }
                if (position == 2) {
                    select_department = "library";
                }
                if (position == 3) {
                    select_department = "schoolt";
                }
                if (position == 4) {
                    select_department = "schools";
                }
                if (position == 5) {
                    select_department = "mychan";
                }
                if (position == 6) {
                    select_department = "dog";
                }
                if (position == 7) {
                    select_department = "mart";
                }
                if (position == 8) {
                    select_department = "fitness";
                }
                if (position == 9) {
                    select_department = "road";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_password.getText().toString().equals(edt_repassword.getText().toString())) {
                    String id = edt_id.getText().toString();
                    String password = edt_password.getText().toString();
                    String name = edt_name.getText().toString();
                    String phone = edt_phone.getText().toString();
                    String address = edt_address.getText().toString();
                    String department = select_department;
                    JoinData task = new JoinData();
                    task.execute(id, password, name, phone, address, department);
                } else {
                    Toast.makeText(getApplicationContext(), "비밀번호가 서로 다릅니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), pet_Login_MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // 로그인
    private class JoinData extends AsyncTask<String, Void, String> {
        // 결과값이 없을 경우 에러 출력
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                Log.d("asdfas",result);
                myJSON = result;

                try {
                    // JSON 파싱
                    JSONArray jsonArray = new JSONArray(myJSON);
                    JSONObject c = jsonArray.getJSONObject(0);
                    String status = c.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), pet_Login_MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (status.equals("join_fail")) {
                        Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (status.equals("id_already")) {
                        Toast.makeText(getApplicationContext(), "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                    }  else if (status.equals("name_already")) {
                        Toast.makeText(getApplicationContext(), "이미 존재하는 이름 입니다.", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(getApplicationContext(), "회원가입 에러", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        protected String doInBackground(String... params) {
            // 보내줄 데이터를 변수에 담아주는 부분
            String id = params[0];
            String pw = params[1];
            String name = params[2];
            String phone = params[3];
            String address = params[4];
            String department = params[5];
            // 서버 주소 작성
            String serverURL = "http://192.168.0.6/join.php";
            // 보낼 데이터 작성
            String postParameters = "id=" + id + "&pw=" + pw + "&name=" + name + "&phone=" + phone + "&address=" + address + "&department=" + department;
            try {
                // PHP 연결 부분
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST"); // POST로 보낸다는 설정
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                // 데이터를 보내주는 부분
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                // 연결 상태 확인 및 데이터 읽어오기
                int responseStatusCode = httpURLConnection.getResponseCode();
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.e("에러", e.toString());
                return null;
            }
        }
    }
}
