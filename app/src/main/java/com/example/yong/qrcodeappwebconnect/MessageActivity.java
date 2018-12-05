package com.example.yong.qrcodeappwebconnect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
    private ArrayList<String> message_arraylist;
    private Button button, btnSend, btnRefresh;
    private EditText editText;
    private TextView nameText;
    private ListView listView;
    int user_id;
    String name, message;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        message_arraylist = new ArrayList<String>();
        user_id = intent.getIntExtra("user_id",1);

        nameText = (TextView) findViewById(R.id.name);
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        listView = (ListView) findViewById(R.id.list_view);

        getMessageList();
        adapter = new ArrayAdapter(this, R.layout.message_layout, message_arraylist);
        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textQrcode = "http://211.213.95.132/mobile_web/qrcode_msg_test.php";
                textQrcode = textQrcode + "?user_id=" + user_id;
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(textQrcode, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    Intent intent = new Intent(MessageActivity.this, QRCodeActivity.class);
                    intent.putExtra("pic",bitmap);
                    startActivity(intent);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = editText.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(response);
                            Toast.makeText(getApplicationContext(),"메시지가 전송되었습니다.",Toast.LENGTH_SHORT).show();
                            editText.setText("");
                            getMessageList();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(String.valueOf(user_id), message, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MessageActivity.this);
                queue.add(registerRequest);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList();
            }
        });
    }

    public void getList(){
        adapter = new ArrayAdapter(this, R.layout.message_layout, message_arraylist);
        listView.setAdapter(adapter);
    }

    public void getMessageList(){
        message_arraylist = new ArrayList<String>();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    name = jsonResponse.getString("name");
                    nameText.setText(name);
                    JSONArray message_list = jsonResponse.getJSONArray("message_list");
                    for(int i=0; i<message_list.length(); i++) {
                        message = message_list.getJSONObject(i).getString("message");
                        message_arraylist.add(message);
                    }

                    editText.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        GetMessageRequest getMessageRequest = new GetMessageRequest(String.valueOf(user_id), responseListener);
        RequestQueue queue = Volley.newRequestQueue(MessageActivity.this);
        queue.add(getMessageRequest);
    }
}