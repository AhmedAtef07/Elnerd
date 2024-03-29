package io.zarda.elnerd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;

import io.zarda.elnerd.model.Constants;
import io.zarda.elnerd.model.Constants.SharedMemory;
import io.zarda.elnerd.model.Question;
import io.zarda.elnerd.model.QuestionsDB;
import io.zarda.elnerd.model.Quote;
import io.zarda.elnerd.src.ApiManager;
import io.zarda.elnerd.src.QuestionsManager;
import io.zarda.elnerd.src.SharedPreferencesManager;
import io.zarda.elnerd.src.ViewManager;

public class MainActivity extends Activity {

    Question preparedQuestion;
    private QuestionsManager questionsManager;
    private SharedPreferencesManager sharedPreferencesManager;
    private CountDownTimer timer;
    private ViewManager vm;
    private int correctIndex;
    private int lastLongestPlayed;
    private int lastAllPlayed;
    private int currentLongestPlayed;
    private int currentAllPlayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        ApiManager.initialize(this);
        QuestionsDB.initialize(this);
        SharedPreferencesManager.initialize(this);

        questionsManager = new QuestionsManager(this);
        sharedPreferencesManager = sharedPreferencesManager.getInstance();

        questionsManager.downloadQuestions(5);

        sharedPreferencesManager.setKey(SharedMemory.LAST_SYNC_TIMESTAMP, 0L);

        lastAllPlayed = (int) sharedPreferencesManager.getKey(SharedMemory.ALL_PLAYED, 0);
        lastLongestPlayed = (int) sharedPreferencesManager.getKey(SharedMemory.LONGEST_PLAYED, 0);

        currentLongestPlayed = 0;
        currentAllPlayed = 0;

        if (questionsManager.getNumberOfQuestions() == 0) {
            ArrayList<String> choices = new ArrayList<>();
            choices.add("الألم");
            choices.add("السعادة");
            choices.add("الخبرة");
            choices.add("العلم");

            Question question = new Question("إن الحقيقة تأتي فقط مع ؟", choices, 0);
            Quote quote = new Quote("أن تعيش مع المجهول شئ سئ للغاية، و أن تموت من أجل الح" +
                    "قيقة فهذه هي الحياة نفسهافأن الحقيقة لا تأتي إلا من خلال اﻷل" +
                    "م، فكلما كان اﻷلم عميقا، كانت الحقيقة أكثر وضوحا", question, "رواية ٣١٣",
                    "Admin");
            questionsManager.addQuote(quote);

            choices = new ArrayList<>();
            choices.add("بتحريكك حطبك في موقد اﻵخر");
            choices.add("بعدم الإقتراب إليه كثيرا");
            choices.add("بالإقتراب منه كثيرا");
            choices.add("بالبعد عنه");

            question = new Question("كيف تبقى الحب مشتعلا مع اﻵخر ؟", choices, 0);
            quote = new Quote("الحب هو ذكاء المسافة. ألّا تقترب كثيراً فتُلغي اللهفة، ولا تبت" +
                    "عد طويلًا فتُنسى. ألّا تضع حطبك دفعةً واحدةً في موقد من تُحب. أن تُبقيه مشتعل" +
                    "ًا بتحريكك الحطب ليس أكثر، دون أن يلمح الآخر يدك المحرّكة لمشاعره ومسار قدره"
                    , question, "اﻷسود يليق بك",
                    "Admin");
            questionsManager.addQuote(quote);

            questionsManager.getRandomQuotes();
        }

        vm = new ViewManager(this);
        vm.startHomeView(true);

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No Internet Connection\nConnect to the Internet, Please", Toast.LENGTH_LONG).show();
        }
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
        if (!vm.inHome()) {
            vm.cancelTimer();
            if (timer != null) {
                timer.cancel();
            }
            if (vm.inQuote()) {
                vm.endQuoteView();
                vm.startHomeView(true);
            } else {
                vm.endGameView();
                vm.startHomeView(false);
            }
        } else {
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
            vm.startHomeView(false);
        }
    }

    public void setNewQuestion() {
        if (preparedQuestion != null) {
            vm.showQuestion(preparedQuestion);
            correctIndex = preparedQuestion.getCorrectIndex();

            timer = new CountDownTimer(Constants.QUESTION_TIME, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    System.out.println("seconds remaining: " + (double) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    preparedQuestion = null;
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(500);
                    vm.startScoreView();
                }
            };
            timer.start();
        } else {
            vm.endGameView();
            vm.startHomeView(false);
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

    public void adminClick(View v) {
        Intent browserIntent = new Intent(this, WebViewActivity.class);
        startActivity(browserIntent);
    }

    public void playClick(View v) {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No Internet Connection\nConnect to the Internet, Please", Toast.LENGTH_LONG).show();
        }
        currentLongestPlayed = 0;
        vm.endHomeView();
    }

    public void goHome(View v) {
        vm.endScoreView();
        vm.endGameView();
        vm.startHomeView(false);
    }

    public void retry(View v) {
        updatePreferences();
        vm.endScoreView();
        vm.endGameView();
        vm.startQuoteView();
    }

    public int getCurrentPlayed() {
        return currentLongestPlayed;
    }

    public int getBestPlayed() {
        return lastLongestPlayed;
    }


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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
