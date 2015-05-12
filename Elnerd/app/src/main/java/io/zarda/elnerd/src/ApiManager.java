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
import io.zarda.elnerd.model.Quote;

/**
 * Created by Ahmed Atef on 11 May, 2015.
 */
public class ApiManager {

    private enum CollectionType {
        QUESTION,
    }

    private static final String apiUrl = Constants.API_URL;
    private static ApiManager ourInstance;
    private static Context context;

    private long maxTimeStamp = 0;

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
                            waitable.receiveResponse(parseQuotesFromResponse(response));
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

private ArrayList<Quote> parseQuotesFromResponse(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        ArrayList<Quote> quotesList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            quotesList.add(makeQuote(jsonArray.getJSONObject(i)));
        }

        // Add last sync time into the shared memory.
        SharedPreferencesManager.getInstance().setKey(Constants.SharedMemory.LAST_SYNC_TIMESTAMP,
            maxTimeStamp);

        return quotesList;
    }

    private Quote makeQuote(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("id");
        String header = jsonObject.getString("header");
        int ansIndex = jsonObject.getInt("answer_index");
        String content = jsonObject.getString("quote");
        String book = jsonObject.getString("book");
        // int tta = jsonObject.getInt("tta");
        String userFullName = jsonObject.getString("user_full_name");
        String mode = jsonObject.getString("mode");

        JSONArray choicesArray = jsonObject.getJSONArray("choices");
        ArrayList<String> choices = new ArrayList<>();
        for (int i = 0; i < choicesArray.length(); ++i) {
            choices.add(choicesArray.getString(i));
        }

        long timeStamp = jsonObject.getLong("timestamp");
        maxTimeStamp = Math.max(maxTimeStamp, timeStamp);

        return new Quote(content, new Question(header, choices, ansIndex, id, 0, mode), book,
                userFullName);
    }

    private String getRequestUrl(CollectionType collectionType, long sinceTimeStamp, int count,
                                 int level) {
        return String.format("%s/%s?timestamp=%d&count=%d", ApiManager.apiUrl,
                collectionType.toString().toLowerCase(), sinceTimeStamp, count);
    }
}