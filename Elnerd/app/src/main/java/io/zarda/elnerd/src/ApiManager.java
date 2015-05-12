package io.zarda.elnerd.src;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import io.zarda.elnerd.model.Question;

/**
 * Created by Ahmed Atef on 11 May, 2015.
 */
public class ApiManager {
    private static ApiManager ourInstance;
    private static Context context;
    private static final String apiUrl = "http://elnerd.zarda.io/api";

    private enum CollectionType {
        QUESTION,
    }

    public static void initialize(Context context) {
        ApiManager.context = context;
        ourInstance = new ApiManager();
    }

    public static ApiManager getInstance() {
        return ourInstance;
    }

    public void downloadQuestions(long sinceTimeStamp, int count, final Waitable waitable) {
        Log.e("downloadQuedstions", "ANOTHER HERE");
        RequestQueue queue = Volley.newRequestQueue(context);

        String requestUrl = getRequestUrl(CollectionType.QUESTION, sinceTimeStamp, count, 2);
        Log.e("download ", requestUrl);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            waitable.receiveResponse(parseQuestionsFromResponse(response));
                            Log.e("downloadQuedstions", "success and sent to waitable");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.err.println("Error while parsing response.");
                            Log.e("downloadQuedstions", "Error while parsing response");

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println("Error while receiving response.");
                        Log.e("downloadQuedstions", "error receiving response");
                    }
                }
        );
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        Log.e("downloadQuedstions", "sent to queue");
    }

    private ArrayList<Question> parseQuestionsFromResponse(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        ArrayList<Question> questionList = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); ++i) {
            questionList.add(makeQuestion(jsonArray.getJSONObject(i)));
        }
        return questionList;
    }

    private Question makeQuestion(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("id");
        String header = jsonObject.getString("header");
        int ansIndex = jsonObject.getInt("answer_index");

        JSONArray choicesArray = jsonObject.getJSONArray("choices");
        ArrayList<String> choices = new ArrayList<>();
        for (int i = 0; i < choicesArray.length(); ++i) {
            choices.add(choicesArray.getString(i));
        }

        return new Question(header, choices, ansIndex, id, 0, 0);
    }

    private String getRequestUrl(CollectionType collectionType, long sinceTimeStamp, int count,
                                 int level) {
        return String.format("%s/%s?timestamp=%d&offset=%d&level=%d", ApiManager.apiUrl,
                collectionType.toString().toLowerCase(), sinceTimeStamp, count, level);
    }
}
