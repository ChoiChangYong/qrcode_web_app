package com.example.yong.qrcodeappwebconnect;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class JoinRequest extends StringRequest {

    final static private String URL = "http://211.213.95.132/mybookstore/join.php";
    private Map<String, String> parameters;

    public JoinRequest(String email, String password, String name, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("email",email);
        parameters.put("password",password);
        parameters.put("name",name);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
