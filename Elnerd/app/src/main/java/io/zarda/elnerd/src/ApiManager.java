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

import java.util.ArrayList;

import io.zarda.elnerd.model.Constants;
import io.zarda.elnerd.model.Question;

/**
 * Created by Ahmed Atef on 11 May, 2015.
 */
public class ApiManager {
    private static final String apiUrl = Constants.API_URL;
    private static ApiManager ourInstance;
    private static Context context;

    public static void initialize(Context context) {
        ApiManager.context = context;
        ourInstance = new ApiManager();
    }

    public static ApiManager getInstance() {
        return ourInstance;
    }

    public void downloadQuestions(long sinceTimeStamp, int count, final Waitable waitable) {
        RequestQueue queue = Volley.newRequestQueue(context);

        String requestUrl = getRequestUrl(CollectionType.QUESTION, sinceTimeStamp, count, 2);
        Log.e("Api Manager", "Sending request to: " + requestUrl);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            waitable.receiveResponse(parseQuestionsFromResponse(response));
                            Log.e("Api Manager", "Response parsed successfully");

                        } catch (JSONException e) {
                            Log.e("Api Manager", "Error parsing response");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Api Manager", "Response parsed successfully");
                    }
                }
        );
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        Log.e("Api Manager", "sent to queue");
    }

    private ArrayList<Question> parseQuestionsFromResponse(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        ArrayList<Question> questionList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
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

    private enum CollectionType {
        QUESTION,
    }
}