package io.zarda.elnerd;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import io.zarda.elnerd.model.Question;
import io.zarda.elnerd.model.QuestionsDB;
import io.zarda.elnerd.src.QuestionsManager;
import io.zarda.elnerd.src.ViewManager;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        QuestionsDB.initializeDB(this);

        ArrayList<String> choices = new ArrayList<>();
        choices.add("Yes");
        choices.add("No");
        choices.add("May be");
        choices.add("Never");

        QuestionsManager questionsManager = new QuestionsManager();
//        questionsManager.addQuestion(new Question("Is your name Ahmed?", choices, 0));
        ArrayList<Question> questions = questionsManager.getQuestions();
        for (int i = 0; i < questions.size(); ++i) {
            System.out.println("DataBase: " + questions.get(0).getHeader() + questions.get(0).getChoices().get(0));
        }

        ViewManager vm = new ViewManager(this);
        vm.showQuestion(questions.get(0));

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
}
