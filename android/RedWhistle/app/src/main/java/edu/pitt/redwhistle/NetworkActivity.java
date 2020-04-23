package edu.pitt.redwhistle;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class NetworkActivity {

    RequestQueue queue;

    public NetworkActivity(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public void sendRequest(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("Response is: "
                                + response.substring(0,Math.min(500, response.length())));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
                System.out.println(error);
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
