package com.minor.project.sm.localizer;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocalizationClient {
    private static String baseUrl = "http://20.204.242.29:8080";
    public static Map<String, Object> languagesMap = new HashMap<>();
    public static void populateSpinnerWithSupportedLanguages(Spinner spinner, Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = baseUrl + "/languages";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            languagesMap = toMap(response);
                            List<String> languages = new ArrayList<>(languagesMap.keySet());
                            Collections.sort(languages);
                            ArrayAdapter arrayAdapter = new ArrayAdapter(context,
                                    android.R.layout.simple_spinner_item,
                                    languages.toArray(new String[languages.size()]));
                            spinner.setAdapter(arrayAdapter);
                        } catch (JSONException e) {
                            Log.e("JsonException", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static void getTranslatedText(Context context,
                                         String textToTranslate,
                                         String toLanguage,
                                         EditText translatedText) {
        String toLang = (String) languagesMap.get(toLanguage);
        Log.i("target Language is", toLang);
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String URL = baseUrl + "/translate";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("text", textToTranslate);
            jsonBody.put("toLang", toLang);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    translatedText.setText(response);
                    translatedText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = new String(response.data);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
