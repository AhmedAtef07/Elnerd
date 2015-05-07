package io.zarda.elnerd;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        QuestionsDB.initializeDB(this);

        ArrayList<String> choices = new ArrayList<>();
        choices.add("Not your business");
        choices.add("Again Not your business");
        choices.add("Why do you ask?");
        choices.add("May be yes and may be no");

        QuestionsManager questionsManager = new QuestionsManager();
//        questionsManager.addQuestion(new Question("Are you Magdy?", choices, 2));
        questions = questionsManager.getQuestions();
        questionsSize = questions.size();
        for (int i = 0; i < questionsSize; ++i) {
            System.out.println("DataBase: " + questions.get(i).getHeader() +
                    questions.get(i).getChoices().get(questions.get(i).getCorrectIndex()));
        }

        vm = new ViewManager(this);
        vm.showQuestion(questions.get(0));
        correctIndex = questions.get(0).getCorrectIndex();
        lastQuestion = 0;

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

    public void setNextQuestion() {
        if(lastQuestion + 1 < questionsSize) {
            vm.showQuestion(questions.get(++lastQuestion));
            correctIndex = questions.get(lastQuestion).getCorrectIndex();
        }
    }

    public void click(View v) {
        if ((int)v.getTag() == correctIndex) {
            System.out.println("True answer Clicked");
            vm.showSuccess(correctIndex);
        }
        else {
            System.out.println("False answer Clicked");
            vm.showFailure(correctIndex, (int)v.getTag());
        }
    }

}
