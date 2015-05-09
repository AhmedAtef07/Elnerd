package io.zarda.elnerd;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import io.zarda.elnerd.model.Question;
import io.zarda.elnerd.model.QuestionsDB;
import io.zarda.elnerd.src.QuestionsManager;
import io.zarda.elnerd.src.ViewManager;


public class MainActivity extends Activity {

    private ViewManager vm;
    QuestionsManager questionsManager;

    private int correctIndex;

    public static final String MyPrefrrencesKEY = "Score" ;
    public static final String LongestPlayedKEY = "LongestPlayed";
    public static final String AllPlayedKEY = "AllPlayed";

    private int lastLongestPlayed;
    private int lastAllPlayed;

    private int currentLongestPlayed;
    private int currentAllPlayed;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        QuestionsDB.initializeDB(this);

        questionsManager = new QuestionsManager(50);

        ArrayList<String> choices = new ArrayList<>();
        choices.add("Choice  0");
        choices.add("Choice  1");
        choices.add("Choice  2");
        choices.add("Choice  3");

//        for (int i = 1; i < 50; ++i)
//            questionsManager.addQuestion(new Question("Question number " + i + "?", choices, i % 4));

        int questionsSize = questionsManager.getRandomQuestions().size();
        System.out.println("Size: " + questionsSize);

        sharedpreferences = getSharedPreferences(MyPrefrrencesKEY, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        lastAllPlayed = sharedpreferences.getInt(AllPlayedKEY, 0);
        lastLongestPlayed = sharedpreferences.getInt(LongestPlayedKEY, 0);

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

    public void setNewQuestion() {
        Question question = questionsManager.getRandomQuestion();
        if (question != null) {
            vm.showQuestion(question);
            correctIndex = question.getCorrectIndex();
        }
        else {
            vm.endGameView();
            vm.startHomeView();
        }
    }

    public void answerClick(View v) {
        ++currentAllPlayed;
        if ((int)v.getTag() == correctIndex) {
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

}
