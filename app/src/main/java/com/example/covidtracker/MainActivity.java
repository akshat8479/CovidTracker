package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtracker.api.ApiUtilities;
import com.example.covidtracker.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView totalConfirm,todayConfirm,totalActive,totalRecovered,todayRecovered,totalDeath;
    private TextView todayDeath,totalTests,date;

    private ProgressDialog dialog;
    private PieChart pieChart;
    private List<CountryData> list;

    String country = "India";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();

        if(getIntent().getStringExtra("country") != null){

            country = getIntent().getStringExtra("country");
        }
        init();

        TextView  cname = findViewById(R.id.cName);
        cname.setText(country);

        cname.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this,CountryActivity.class)));

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);
        dialog.show();

        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {

                        list.addAll(response.body());
                        for (int i = 0; i < list.size(); i++){

                            if(list.get(i).getCountry().equals(country)){

                                int confirm = Integer.parseInt(list.get(i).getCases());
                                int active = Integer.parseInt(list.get(i).getActive());
                                int recovered = Integer.parseInt(list.get(i).getRecovered());
                                int death = Integer.parseInt(list.get(i).getDeaths());

                                int todayCases = Integer.parseInt(list.get(i).getTodayCases());
                                int todayDeaths = Integer.parseInt(list.get(i).getTodayDeaths());
                                int todayRecover = Integer.parseInt(list.get(i).getTodayRecovered());
                                int totalsTest = Integer.parseInt(list.get(i).getTests());

                                setText(list.get(i).getUpdated());


                                totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                                totalActive.setText(NumberFormat.getInstance().format(active));
                                totalRecovered.setText(NumberFormat.getInstance().format(recovered));
                                totalDeath.setText(NumberFormat.getInstance().format(death));
                                totalTests.setText(NumberFormat.getInstance().format(totalsTest));

                                todayConfirm.setText(NumberFormat.getInstance().format(todayCases));
                                todayDeath.setText(NumberFormat.getInstance().format(todayDeaths));
                                todayRecovered.setText(NumberFormat.getInstance().format(todayRecover));



                                pieChart.addPieSlice(new PieModel("Confirm",confirm,getResources().getColor(R.color.yellow)));
                                pieChart.addPieSlice(new PieModel("Active",active,getResources().getColor(R.color.blue_pie)));
                                pieChart.addPieSlice(new PieModel("Recovered",recovered,getResources().getColor(R.color.green_pie)));
                                pieChart.addPieSlice(new PieModel("death",confirm,getResources().getColor(R.color.red_pie)));
                                pieChart.startAnimation();

                                dialog.dismiss();

                            }
                        }
                    }

                    private void setText(String updated) {

                        DateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy");

                        long miliseconds = Long.parseLong(updated);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(miliseconds);

                        date.setText("Updated at "+ dateFormat.format(calendar.getTime()));
                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Error "+t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void init(){
        totalConfirm = findViewById(R.id.totalConfirm);
        todayConfirm = findViewById(R.id.todayConfirm);
        totalActive  = findViewById(R.id.totalActive);
        totalRecovered = findViewById(R.id.totalRecovered);
        todayRecovered = findViewById(R.id.todayRecovered);
        totalDeath = findViewById(R.id.totalDeath);
        todayDeath = findViewById(R.id.todayDeath);
        totalTests = findViewById(R.id.totalTests);
        date = findViewById(R.id.date);
        pieChart = findViewById(R.id.piechart);

    }
}