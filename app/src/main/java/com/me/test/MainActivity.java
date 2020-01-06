package com.me.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.widget.SearchView;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecycleViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Exercise> ListExcercises;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListExcercises = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        searchView = findViewById(R.id.searchView);
        mAdapter = new RecycleViewAdapter(ListExcercises, new RecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Exercise item) {
                OpenVideoPage(item);
            }
        });
        recyclerView.setAdapter(mAdapter);

        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setFocusable(true);
        searchView.setFocusableInTouchMode(true);
        searchView.requestFocus();
        searchView.requestFocusFromTouch();
        searchView.setQueryHint("Cerca un esercizio");
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });

        // specify an adapter (see also next example)
        LoadListExcercises();
    }
    private void OpenVideoPage(Exercise ex){
        Intent i = new Intent(this, VideoActivity.class);
        i.putExtra("urlVideo",ex.videoUrl);
        startActivity(i);
    }
    private void LoadListExcercises(){
        String url = "https://api.buddyfit.it/api/exercises";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<Exercise> tmp = new ArrayList<Exercise>();
                        try {
                            JSONObject data = response.getJSONObject("data");
                            JSONArray objs = data.getJSONArray("exercises");
                            for(int i = 0; i<objs.length();i++){
                                JSONObject e = objs.getJSONObject(i);
                                Exercise exc = new Exercise(e);
                                tmp.add(exc);
                            }
                        }
                        catch (Exception ex){
                            Log.e("ERRORE PARSE JSON", Arrays.toString(ex.getStackTrace()));
                        }
                        ListExcercises.clear();
                        ListExcercises.addAll(tmp);
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        NetworkSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    class Exercise
    {

        public String getExerciseId() {
            return exerciseId;
        }

        public String getName() {
            return name;
        }

        public String getShortName() {
            return shortName;
        }

        public String getArea() {
            return area;
        }

        public String getDifficultyLevel() {
            return difficultyLevel;
        }

        public int getSeries() {
            return series;
        }

        public int getReps() {
            return reps;
        }

        public int getRepsTime() {
            return repsTime;
        }

        public String getRepsVerbose() {
            return repsVerbose;
        }

        public String getRepsVerboseTop() {
            return repsVerboseTop;
        }

        public String getRepsVerboseBottom() {
            return repsVerboseBottom;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public int getRecoveryAfter() {
            return recoveryAfter;
        }

        public int getRecoveryAfterSeries() {
            return recoveryAfterSeries;
        }

        public List<String> getAdvice() {
            return advice;
        }
        private String exerciseId ;
        private String name ;
        private String shortName ;
        private String area ;
        private String difficultyLevel ;
        private int series ;
        private int reps ;
        private int repsTime ;
        private String repsVerbose ;
        private String repsVerboseTop ;
        private String repsVerboseBottom ;
        private String pictureUrl ;
        private String videoUrl ;
        private int recoveryAfter ;
        private int recoveryAfterSeries;
        private List<String>advice;

        Exercise(JSONObject e) {
            try {
                this.exerciseId = e.getString("exerciseId");
            } catch (JSONException ex) {
                this.exerciseId = "";
            }
            try {
                this.name = e.getString("name");
            } catch (JSONException ex) {
                this.name = "";
            }
            try {
                this.shortName = e.getString("shortName");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            try {
                this.area = e.getString("area");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            try {
                this.difficultyLevel = e.getString("difficultyLevel");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            try {
                this.series = e.getInt("series");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            try {
                this.reps = e.getInt("reps");
            } catch (JSONException ex) {
                this.reps = -1;
            }
            try {
                this.repsTime = e.getInt("repsTime");
            } catch (JSONException ex) {
                this.repsTime = -1;
            }
            try {
                this.repsVerbose = e.getString("repsVerbose");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            try {
                this.repsVerboseTop = e.getString("repsVerboseTop");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            try {
                this.repsVerboseBottom = e.getString("repsVerboseBottom");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            try {
                this.pictureUrl = e.getString("pictureUrl");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            try {
                this.videoUrl = e.getString("videoUrl");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            try {
                this.recoveryAfter = e.getInt("recoveryAfter");
            } catch (JSONException ex) {
                this.recoveryAfter = -1;
            }
            try {
                this.recoveryAfterSeries = e.getInt("recoveryAfterSeries");
            } catch (JSONException ex) {
                this.recoveryAfterSeries = -1;
            }

        }
    }

}

