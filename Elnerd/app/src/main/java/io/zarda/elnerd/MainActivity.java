package io.zarda.elnerd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import io.zarda.elnerd.model.Constants;
import io.zarda.elnerd.model.Question;
import io.zarda.elnerd.model.QuestionsDB;
import io.zarda.elnerd.src.ApiManager;
import io.zarda.elnerd.src.QuestionsManager;
import io.zarda.elnerd.src.ViewManager;
import io.zarda.elnerd.view.FragmentSimpleLoginButton;


public class MainActivity extends FragmentActivity {

    public static final String MyPreferencesKEY = Constants.SharedMemory.NAME.toString();
    public static final String LongestPlayedKEY = Constants.SharedMemory.Score.LONGEST_PLAYED.toString();
    public static final String AllPlayedKEY = Constants.SharedMemory.Score.ALL_PLAYED.toString();
    private QuestionsManager questionsManager;
    private CountDownTimer timer;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private ViewManager vm;
    private int correctIndex;
    private int lastLongestPlayed;
    private int lastAllPlayed;
    private int currentLongestPlayed;
    private int currentAllPlayed;
    private FragmentManager mFragmentManager;

    private CallbackManager mCallbackManager;
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

        mFragmentManager = getSupportFragmentManager();

        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        ApiManager.initialize(this);
        QuestionsDB.initialize(this);

        questionsManager = new QuestionsManager(this);

        ArrayList<String> choices = new ArrayList<>();
        choices.add("Choice  0");
        choices.add("Choice  1");
        choices.add("Choice  2");
        choices.add("Choice  3");

        if (questionsManager.getQuestionsSize() == 0) {
            System.out.println("Getting Data");
            for (int i = 1; i <= 20; ++i)
                questionsManager.addQuestion(
                        new Question("Question number " + i + "?", choices, i % 4));
            questionsManager.getRandomQuestions();
        }

        int questionsSize = questionsManager.getQuestionsSize();
        System.out.println("Size: " + questionsSize);

        sharedpreferences = getSharedPreferences(MyPreferencesKEY, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        editor.putLong(Constants.SharedMemory.LAST_SYNC_TIMESTAMP.toString(), 0L);
        editor.commit();

        lastAllPlayed = sharedpreferences.getInt(AllPlayedKEY, 0);
        lastLongestPlayed = sharedpreferences.getInt(LongestPlayedKEY, 0);

        currentLongestPlayed = 0;
        currentAllPlayed = 0;

        vm = new ViewManager(this);
        vm.startHomeView();

        questionsManager.downloadQuestions(2);
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
            vm.endGameView();
            vm.startHomeView();
        } else {
//            Toast.makeText(this, "You are currently in home :)", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void setNewQuestion() {
        if (questionsManager.containsQuestion()) {
            ++currentAllPlayed;
            Question question = questionsManager.getRandomQuestion();
            vm.showQuestion(question);
            correctIndex = question.getCorrectIndex();
            timer = new CountDownTimer(6000, 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    System.out.println("seconds remaining: " + (double) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
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
        timer.cancel();
        if ((int) v.getTag() == correctIndex) {
            vm.showSuccess(correctIndex);
            ++currentLongestPlayed;
        } else {
            vm.showFailure(correctIndex, (int) v.getTag());
        }
    }

    public void playClick(View v) {
        currentLongestPlayed = 0;
        vm.endHomeView();
        vm.startGameView();
    }

    public void loginClick(View v) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(android.R.id.content, new FragmentSimpleLoginButton());
        transaction.commit();
    }

    public void updatePreferences() {
        if (currentLongestPlayed > lastLongestPlayed) {
            editor.putInt(LongestPlayedKEY, currentLongestPlayed);
        }
        editor.putInt(AllPlayedKEY, lastAllPlayed + currentAllPlayed);
        editor.commit();
        currentLongestPlayed = 0;
        System.out.println("Best: " + sharedpreferences.getInt(LongestPlayedKEY, 0));
        System.out.println("All: " + sharedpreferences.getInt(AllPlayedKEY, 0));
        vm.setScores(sharedpreferences.getInt(LongestPlayedKEY, 0),
                sharedpreferences.getInt(AllPlayedKEY, 0));
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
