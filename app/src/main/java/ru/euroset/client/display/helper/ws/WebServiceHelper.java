package ru.euroset.client.display.helper.ws;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import mihail.shipulin.videoplayer.R;
import mss.log.L;
import mss.request.data.RequestDataProvider;
import mss.storage.internal.DataStorage;
import ru.euroset.client.display.helper.http.HttpRequestMessage;
import ru.euroset.client.display.helper.json.JsonHelper;

public class WebServiceHelper {
    public L l = new L(this);
    private String WS_URL;
    private RequestQueue httpRequestQueue;
    private RequestDataProvider requestDataGelper;
    private Resources resources;

    public WebServiceHelper(@NonNull Context context){
        resources = context.getResources();
        WS_URL = resources.getString(R.string.WS_URL);
        httpRequestQueue = Volley.newRequestQueue(context);
        requestDataGelper = new RequestDataProvider();
        l.i("WebServiceHelper created WS_URL: "+WS_URL);
    }

    public void sendWsRequest(DataStorage dataStorage) throws Exception {
        HttpRequestMessage requestMessage = requestDataGelper.createRequestObject(dataStorage);
        l.i("sendWsRequest: "+requestMessage.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                WS_URL,
                new JSONObject(JsonHelper.writeValueAsString(requestMessage)),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleWsResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleOnErrorResponse(error);
                    }
                }
        ) {};
        httpRequestQueue.add(jsonObjReq);
    }
    public void handleOnErrorResponse(VolleyError error) {
        l.i("onErrorResponse: "+ error.getMessage());
        //TODO
    }
    public void handleWsResponse(JSONObject response) {
        l.i("handleWsResponse: "+ response.toString());
        //TODO
    }
}
