package com.example.yong.qrcodeappwebconnect;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetMessageRequest extends StringRequest{
    final static private String URL = "http://211.213.95.132/mybookstore/qrcode_msg_list.php";
    private Map<String, String> parameters;

    public GetMessageRequest(String user_id, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("user_id",user_id);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
