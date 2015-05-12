package io.zarda.elnerd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;

import io.zarda.elnerd.model.Constants.SharedMemory;
import io.zarda.elnerd.model.Question;
import io.zarda.elnerd.model.QuestionsDB;
import io.zarda.elnerd.model.Quote;
import io.zarda.elnerd.src.ApiManager;
import io.zarda.elnerd.src.QuestionsManager;
import io.zarda.elnerd.src.SharedPreferencesManager;
import io.zarda.elnerd.src.ViewManager;


public class MainActivity extends FragmentActivity {

    private QuestionsManager questionsManager;
    private SharedPreferencesManager sharedPreferencesManager;

    private CountDownTimer timer;
    private CallbackManager mCallbackManager;
    private ViewManager vm;

    Question preparedQuestion;

    private int correctIndex;
    private int lastLongestPlayed;
    private int lastAllPlayed;
    private int currentLongestPlayed;
    private int currentAllPlayed;

    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            System.out.println("onSuccess");
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            String msg = "Welcome ";
            if (profile != null) {
                msg += profile.getName();
            }
            System.out.println(msg);
        }

        @Override
        public void onCancel() {
            System.out.println("onCancel");
            Toast.makeText(getApplicationContext(), "No Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException e) {
            System.out.println("onError");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        ApiManager.initialize(this);
        QuestionsDB.initialize(this);
        SharedPreferencesManager.initialize(this);

        questionsManager = new QuestionsManager(this);
        sharedPreferencesManager = sharedPreferencesManager.getInstance();

        ArrayList<String> choices = new ArrayList<>();
        choices.add("Choice  0");
        choices.add("Choice  1");
        choices.add("Choice  2");
        choices.add("Choice  3");

//        if (questionsManager.getQuotesSize() == 0) {
//            System.out.println("Getting Data");
//            for (int i = 1; i <= 20; ++i)
//                questionsManager.addQuestion(
//                        new Question("Question number " + i + "?", choices, i % 4));
//            questionsManager.getRandomQuestions();
//        }

//        int questionsSize = questionsManager.getQuestionsSize();
//        System.out.println("Size: " + questionsSize);
        questionsManager.downloadQuestions(3);

//        questionsManager.getRandomQuotes();
//        System.out.println(questionsManager.getRandomQuote().getContent());

        sharedPreferencesManager.setKey(SharedMemory.LAST_SYNC_TIMESTAMP, 0L);


        lastAllPlayed = (int) sharedPreferencesManager.getKey(SharedMemory.ALL_PLAYED, 0);
        lastLongestPlayed = (int) sharedPreferencesManager.getKey(SharedMemory.LONGEST_PLAYED, 0);

        currentLongestPlayed = 0;
        currentAllPlayed = 0;

        vm = new ViewManager(this);
        vm.startHomeView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        updatePreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        updatePreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("OnResume");
        if (vm.inHome()) {
            LoginButton mButtonLogin = (LoginButton) findViewById(R.id.login_button);
            mButtonLogin.setReadPermissions("user_friends");
            mButtonLogin.registerCallback(mCallbackManager, mFacebookCallback);
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if (accessToken == null) {
                System.out.println(">>>" + "Signed Out");
//                mButtonLogin.callOnClick();
            } else {
                System.out.println(">>>" + "Signed In");
                Profile profile = Profile.getCurrentProfile();
                String msg = "Welcome ";
                if (profile != null) {
                    msg += profile.getName();
                }
                System.out.println(msg);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void backPressed() {
        System.out.println("onBackPressed Called");
        if (!vm.inHome()) {
            if (vm.inQuote()) {
                vm.endQuoteView();
                vm.startHomeView();
            } else {
                vm.endGameView();
                vm.startHomeView();
            }
        } else {
//            Toast.makeText(this, "You are currently in home :)", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void setNewQuote() {
        if (questionsManager.containsQuote()) {
            ++currentAllPlayed;
            Quote quote = questionsManager.getRandomQuote();
            vm.showQuote(quote);
            preparedQuestion = quote.getQuestion();
        } else {
            vm.endQuoteView();
            vm.startHomeView();
        }

    }

    public void setNewQuestion() {
        if (preparedQuestion != null) {
            vm.showQuestion(preparedQuestion);
            correctIndex = preparedQuestion.getCorrectIndex();

            timer = new CountDownTimer(6000, 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    System.out.println("seconds remaining: " + (double) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    preparedQuestion = null;
                    vm.endGameView();
                    vm.startHomeView();
                }
            };
            timer.start();
        } else {
            vm.endGameView();
            vm.startHomeView();
        }
    }

    public void answerClick(View v) {
        if ((int) v.getTag() == correctIndex) {
            vm.showSuccess(correctIndex);
            ++currentLongestPlayed;
        } else {
            vm.showFailure(correctIndex, (int) v.getTag());
        }
    }

    public void playClick(View v) {
        currentLongestPlayed = 0;
//        vm.endHomeView();
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//        startActivity(browserIntent);
//        vm.startGameView();

    }

//    public void loginClick(View v) {
//        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//        transaction.replace(android.R.id.content, new FragmentSimpleLoginButton());
//        transaction.commit();
//    }

    public void updatePreferences() {
        if (currentLongestPlayed > lastLongestPlayed) {
            sharedPreferencesManager.setKey(SharedMemory.LONGEST_PLAYED, currentLongestPlayed);
        }
        sharedPreferencesManager.setKey(SharedMemory.ALL_PLAYED, lastAllPlayed + currentAllPlayed);

        currentLongestPlayed = 0;
        System.out.println("Best: " + (int) sharedPreferencesManager.getKey(
                SharedMemory.LONGEST_PLAYED, 0));
        System.out.println("All: " + (int) sharedPreferencesManager.getKey(
                SharedMemory.LONGEST_PLAYED, 0));
        vm.setScores(
                (int) sharedPreferencesManager.getKey(SharedMemory.LONGEST_PLAYED, 0),
                (int) sharedPreferencesManager.getKey(SharedMemory.ALL_PLAYED, 0));
        lastAllPlayed += currentAllPlayed;
        currentAllPlayed = 0;
        currentLongestPlayed = 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
