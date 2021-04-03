package com.example.shiba;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class pet_LoginActivity extends AppCompatActivity {

    Button btn_login, btn_main;
    EditText edt_id, edt_pw;
    String myJSON;
    JSONArray peoples = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_activity_login);

        btn_login = (Button) findViewById(R.id.login);
        btn_main = (Button) findViewById(R.id.main);
        edt_id = (EditText) findViewById(R.id.id);
        edt_pw = (EditText) findViewById(R.id.password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pet_index.id = edt_id.getText().toString();
                String password = edt_pw.getText().toString();
                LoginData task = new LoginData();
                task.execute(pet_index.id, password);
                edt_pw.setText("");
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
    private class LoginData extends AsyncTask<String, Void, String> {
        // 결과값이 없을 경우 에러 출력
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                myJSON = result;
                try {
                    // JSON 파싱
                    JSONArray jsonArray = new JSONArray(myJSON);
                    JSONObject c = jsonArray.getJSONObject(0);
                    String status = c.getString("status");
                    if (status.equals("success")) {
                        pet_index.name = c.getString("name");
                        Toast.makeText(getApplicationContext(), pet_index.name + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                        pet_index.department = c.getString("department");
                        if (pet_index.department.equals("dog")){
                            Intent intent = new Intent(getApplicationContext(), pet_SplashActivity.class);
                            startActivity(intent);
                        }
                        Log.e("부서 출력", pet_index.department);
                    } else if (status.equals("id_fail")) {
                        Toast.makeText(getApplicationContext(), "아이디를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    } else if (status.equals("pw_fail")) {
                        Toast.makeText(getApplicationContext(), "비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 에러", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        protected String doInBackground(String... params) {
            // 보내줄 데이터를 변수에 담아주는 부분
            String id = (String)params[0];
            String pw = (String)params[1];
            Log.e("비밀번호", pw);
            // 서버 주소 작성
            String serverURL = "http://192.168.13.13/login.php";
            // 보낼 데이터 작성
            String postParameters = "id=" + id + "&pw=" + pw;
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
