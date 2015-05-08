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
    private int correctIndex;
    private int questionsSize;
    private int lastQuestion;

    ArrayList<Question> questions;

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
//        setContentView(R.layout.activity_main);
        QuestionsDB.initializeDB(this);

        QuestionsManager questionsManager = new QuestionsManager();

        ArrayList<String> choices = new ArrayList<>();
        choices.add("Not your business");
        choices.add("Again Not your business");
        choices.add("Why do you ask?");
        choices.add("May be yes and may be no");
//        questionsManager.addQuestion(new Question("Are you Magdy?", choices, 3));

        questions = questionsManager.getQuestions();
        questionsSize = questions.size();

        for (int i = 0; i < questionsSize; ++i) {
            System.out.println("DataBase: " + questions.get(i).getHeader() +
                    questions.get(i).getChoices().get(questions.get(i).getCorrectIndex()));
        }

        lastQuestion = 0;


        sharedpreferences = getSharedPreferences(MyPrefrrencesKEY, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        if (sharedpreferences.contains(AllPlayedKEY)) {
            lastAllPlayed = sharedpreferences.getInt(AllPlayedKEY, 0);
        }
        else {
            lastAllPlayed = 0;
        }

        if (sharedpreferences.contains(LongestPlayedKEY)) {
            lastLongestPlayed = sharedpreferences.getInt(LongestPlayedKEY, 0);
        }
        else {
            lastLongestPlayed = 0;
        }

        System.out.println("last Best: " + lastLongestPlayed);
        System.out.println("last All: " + lastAllPlayed);
        currentLongestPlayed = 0;
        currentAllPlayed = 0;

        vm = new ViewManager(this);
        vm.setScores(lastLongestPlayed, lastAllPlayed);
        vm.startHomeView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(currentLongestPlayed > lastLongestPlayed) {
            editor.putInt(LongestPlayedKEY, currentLongestPlayed);
        }
        editor.putInt(AllPlayedKEY, lastAllPlayed + currentAllPlayed);
        editor.commit();
        System.out.println("Best: " + sharedpreferences.getInt(LongestPlayedKEY, 0));
        System.out.println("All: " + sharedpreferences.getInt(AllPlayedKEY, 0));
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
        if(lastQuestion < questionsSize) {
            vm.showQuestion(questions.get(lastQuestion));
            correctIndex = questions.get(lastQuestion).getCorrectIndex();
            ++lastQuestion;
        }
        else {
            if(currentLongestPlayed > lastLongestPlayed) {
                editor.putInt(LongestPlayedKEY, currentLongestPlayed);
            }
            editor.putInt(AllPlayedKEY, lastAllPlayed + currentAllPlayed);
            editor.commit();
            System.out.println("Best: " + sharedpreferences.getInt(LongestPlayedKEY, 0));
            System.out.println("All: " + sharedpreferences.getInt(AllPlayedKEY, 0));
            vm.endGameView();
            vm.setScores(sharedpreferences.getInt(LongestPlayedKEY, 0),
                    sharedpreferences.getInt(AllPlayedKEY, 0));
            currentAllPlayed = 0;
            vm.startHomeView();
        }
    }

    public void answerClick(View v) {
        ++currentAllPlayed;
        if ((int)v.getTag() == correctIndex) {
            System.out.println("True answer Clicked");
            vm.showSuccess(correctIndex);
            ++currentLongestPlayed;
        } else {
            System.out.println("False answer Clicked");
            vm.showFailure(correctIndex, (int) v.getTag());
            if(currentLongestPlayed > lastLongestPlayed) {
                editor.putInt(LongestPlayedKEY, currentLongestPlayed);
            }
            editor.putInt(AllPlayedKEY, lastAllPlayed + currentAllPlayed);
            editor.commit();
            currentLongestPlayed = 0;
            System.out.println("Best: " + sharedpreferences.getInt(LongestPlayedKEY, 0));
            System.out.println("All: " + sharedpreferences.getInt(AllPlayedKEY, 0));
            vm.setScores(sharedpreferences.getInt(LongestPlayedKEY, 0),
                    sharedpreferences.getInt(AllPlayedKEY, 0));
            currentAllPlayed = 0;
        }
    }

    public void playClick(View v) {
        currentLongestPlayed = 0;
        vm.endHomeView();
        vm.startGameView();
    }

}
