package com.example.yong.qrcodeappwebconnect;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class RegisterRequest extends StringRequest{
    final static private String URL = "http://211.213.95.132/mybookstore/qrcode_msg_register.php";
    private Map<String, String> parameters;

    public RegisterRequest(String user_id, String message, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("user_id",user_id);
        parameters.put("message",message);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
