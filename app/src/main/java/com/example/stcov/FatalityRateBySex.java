package com.example.stcov;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TableRow;
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

import java.math.BigDecimal;
import java.text.NumberFormat;

public class FatalityRateBySex extends AppCompatActivity {

    PieChart pieChart;
    TextView male,female;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fatality_rate_by_sex);

        pieChart = findViewById(R.id.Pchart);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);

        fetchData();

    }

    private void fetchData() {
        String url = "https://covid19-server.chrismichael.now.sh/api/v1/FatalityRateBySex";

        final StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("TAG", "Login Response: " + response.toString());
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            //JSONArray arr = jsonObject.getJSONArray("table");

                            male.setText(jsonObject.getJSONArray("table").getJSONObject(0).getString("DeathRateAllCases"));
                            female.setText(jsonObject.getJSONArray("table").getJSONObject(1).getString("DeathRateAllCases"));

                            String y = female.getText().toString();
                            y = y.replace("%"," ");
                            int yy = (int) Double.parseDouble(y);
                            System.out.println(yy);

                            String x = male.getText().toString();
                            x = x.replace("%"," ");
                            int xx = (int) Double.parseDouble(x);
                            System.out.println(xx);


                            pieChart.addPieSlice(new PieModel("Male",xx, Color.parseColor("#0F96D3")));
                            pieChart.addPieSlice(new PieModel("Female",yy, Color.parseColor("#fb7268")));
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
