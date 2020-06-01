package com.example.stcov;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FatalityRateBySex extends AppCompatActivity {

    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fatality_rate_by_sex);

        pieChart = findViewById(R.id.Pchart);

        fetchData();

    }

    private void fetchData() {
        String url = "https://covid19-server.chrismichael.now.sh/api/v1/FatalityRateBySex";

        final StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            JSONArray arr = jsonObject.getJSONArray("table");

                                pieChart.addPieSlice(new PieModel("Male", Integer.parseInt(arr.getJSONObject(0).getString("DeathRateAllCases")), Color.parseColor("#0F96D3")));
                                pieChart.addPieSlice(new PieModel("Female", Integer.parseInt(arr.getJSONObject(1).getString("DeathRateAllCases")), Color.parseColor("#fb7268")));
                                pieChart.startAnimation();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FatalityRateBySex.this, error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
}
