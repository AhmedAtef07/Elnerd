package io.zarda.elnerd.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by atef & emad on 4 May, 2015.
 */

public class QuestionsDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "elnerd.db";

    private static final String QUESTIONS_TABLE_NAME = "questionsTable";
    private static final String CHOICES_TABLE_NAME = "choicesTable";
    private static final String ANSWERS_TABLE_NAME = "answersTable";

    private static final String QUESTIONS_COLUMN_ID = "id";
    private static final String QUESTIONS_COLUMN_HEADER = "header";
    private static final String QUESTIONS_COLUMN_ANSWER_ID = "answer_id";
    private static final String QUESTIONS_COLUMN_VIEW_COUNTER = "view_counter";

    private static final String CHOICES_COLUMN_ID = "id";
    private static final String CHOICES_COLUMN_CHOICE = "choice";
    private static final String CHOICES_COLUMN_QUESTION_ID = "question_id";

    private static final String ANSWERS_COLUMN_ID = "id";
    private static final String ANSWERS_COLUMN_CHOICE_ID = "choice_id";
    private static final String ANSWERS_COLUMN_QUESTION_ID = "question_id";
    private static QuestionsDB ourInstance;
    private static Context context;
    private int currentViewCounter;

    private QuestionsDB() {
        super(context, DATABASE_NAME, null, 1);
    }

    public static void initializeDB(Context context) {
        QuestionsDB.context = context;
        ourInstance = new QuestionsDB();
    }

    public static QuestionsDB getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "
                        + QUESTIONS_TABLE_NAME + " ("
                        + QUESTIONS_COLUMN_ID + " INTEGER PRIMARY KEY, "
                        + QUESTIONS_COLUMN_HEADER + " TEXT NOT NULL, "
                        + QUESTIONS_COLUMN_ANSWER_ID + " INTEGER, "
                        + QUESTIONS_COLUMN_VIEW_COUNTER + " INTEGER)"
        );

        db.execSQL(
                "CREATE TABLE "
                        + CHOICES_TABLE_NAME + " ("
                        + CHOICES_COLUMN_ID + " INTEGER PRIMARY KEY, "
                        + CHOICES_COLUMN_CHOICE + " TEXT NOT NULL, "
                        + CHOICES_COLUMN_QUESTION_ID + " INTEGER NOT NULL)"
        );

        db.execSQL(
                "CREATE TABLE "
                        + ANSWERS_TABLE_NAME + " ("
                        + ANSWERS_COLUMN_ID + " INTEGER PRIMARY KEY, "
                        + ANSWERS_COLUMN_CHOICE_ID + " INTEGER NOT NULL, "
                        + ANSWERS_COLUMN_QUESTION_ID + " INTEGER NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QUESTIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CHOICES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ANSWERS_TABLE_NAME);
        onCreate(db);
    }

    public void addQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(QUESTIONS_COLUMN_HEADER, question.getHeader());
        contentValues.put(QUESTIONS_COLUMN_VIEW_COUNTER, 0);

        int questionId = (int) db.insert(QUESTIONS_TABLE_NAME, null, contentValues);

        int answerId = 0, correctChoiceId;

        for (int i = 0; i < question.getChoices().size(); ++i) {
            if (i != question.getCorrectIndex()) {
                addChoice(question.getChoices().get(i), questionId);
            } else {
                correctChoiceId = addChoice(question.getChoices().get(i), questionId);
                answerId = addAnswer(correctChoiceId, questionId);
            }
        }

        contentValues.put(QUESTIONS_COLUMN_ANSWER_ID, answerId);
        db.update(QUESTIONS_TABLE_NAME, contentValues, QUESTIONS_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(questionId)});
    }

    private int addChoice(String choice, int questionId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CHOICES_COLUMN_CHOICE, choice);
        contentValues.put(CHOICES_COLUMN_QUESTION_ID, questionId);

        return (int) db.insert(CHOICES_TABLE_NAME, null, contentValues);
    }

    private int addAnswer(int choiceId, int questionId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ANSWERS_COLUMN_CHOICE_ID, choiceId);
        contentValues.put(ANSWERS_COLUMN_QUESTION_ID, questionId);

        return (int) db.insert(ANSWERS_TABLE_NAME, null, contentValues);
    }

//    public Cursor getData(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "SELECT * FROM " + QUESTIONS_TABLE_NAME + " WHERE "
//        + QUESTIONS_COLUMN_ID + " = " + id, null );
//        return res;
//    }

    public int numberOfQuestions() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, QUESTIONS_TABLE_NAME);
        return numRows;
    }

    public boolean updateQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(QUESTIONS_COLUMN_HEADER, question.getHeader());

        db.update(QUESTIONS_TABLE_NAME, contentValues, QUESTIONS_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(question.getId())});
        return true;
    }

    public boolean updateViewCounter(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor questionsCursor = db.rawQuery("SELECT * FROM " + QUESTIONS_TABLE_NAME, null);
        questionsCursor.moveToFirst();

        int viewCounter = questionsCursor.getInt(questionsCursor.getColumnIndex(
                QUESTIONS_COLUMN_VIEW_COUNTER));

        contentValues.put(QUESTIONS_COLUMN_VIEW_COUNTER, ++viewCounter);

        db.update(QUESTIONS_TABLE_NAME, contentValues, QUESTIONS_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(question.getId())});
        return true;
    }

    public ArrayList getQuestions() {
        ArrayList<Question> questions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor questionsCursor = db.rawQuery("SELECT * FROM " + QUESTIONS_TABLE_NAME, null);
        questionsCursor.moveToFirst();
        while (!questionsCursor.isAfterLast()) {
            int id = questionsCursor.getInt(questionsCursor.getColumnIndex(QUESTIONS_COLUMN_ID));
            String header = questionsCursor.getString(questionsCursor.getColumnIndex(
                    QUESTIONS_COLUMN_HEADER));

            int correctChoiceId = 0;
            Cursor answersCursor = db.rawQuery("SELECT * FROM " + ANSWERS_TABLE_NAME + " WHERE "
                    + ANSWERS_COLUMN_QUESTION_ID + " = " + id, null);
            answersCursor.moveToFirst();
            while (!answersCursor.isAfterLast()) {
                correctChoiceId = answersCursor.getInt(answersCursor.getColumnIndex(
                        ANSWERS_COLUMN_CHOICE_ID));
                answersCursor.moveToNext();
            }

            ArrayList<String> choices = new ArrayList<>();
            int correctIndex = 0, counter = 0;
            Cursor choicesCursor = db.rawQuery("SELECT * FROM " + CHOICES_TABLE_NAME + " WHERE "
                    + CHOICES_COLUMN_QUESTION_ID + " = " + id, null);
            choicesCursor.moveToFirst();
            while (!choicesCursor.isAfterLast()) {
                choices.add(choicesCursor.getString(choicesCursor.getColumnIndex(
                        CHOICES_COLUMN_CHOICE)));
                if (choicesCursor.getInt(choicesCursor.getColumnIndex(
                        CHOICES_COLUMN_ID)) == correctChoiceId) {
                    correctIndex = counter;
                }
                ++counter;
                choicesCursor.moveToNext();
            }

            Question question = new Question(header, choices, correctIndex, id);
            questions.add(question);

            questionsCursor.moveToNext();
        }

        return questions;
    }

    public ArrayList getRandomQuestions(int limit) {
        ArrayList<Question> questions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        currentViewCounter = 0;

        while (limit > 0) {
            Cursor questionsCursor = db.rawQuery("SELECT * FROM " + QUESTIONS_TABLE_NAME + " WHERE "
                    + QUESTIONS_COLUMN_VIEW_COUNTER + " = " + currentViewCounter
                    + " ORDER BY RANDOM() LIMIT " + limit, null);
            if (questionsCursor.getCount() < limit) {
                ++currentViewCounter;
            }

            questionsCursor.moveToFirst();
            while (!questionsCursor.isAfterLast()) {
                int id = questionsCursor.getInt(questionsCursor.getColumnIndex(QUESTIONS_COLUMN_ID));
                String header = questionsCursor.getString(questionsCursor.getColumnIndex(
                        QUESTIONS_COLUMN_HEADER));

                Cursor answersCursor = db.rawQuery("SELECT * FROM " + ANSWERS_TABLE_NAME + " WHERE "
                        + ANSWERS_COLUMN_QUESTION_ID + " = " + id, null);
                answersCursor.moveToFirst();
                int correctChoiceId = answersCursor.getInt(answersCursor.getColumnIndex(
                        ANSWERS_COLUMN_CHOICE_ID));

                Cursor CorrectChoiceCursor = db.rawQuery("SELECT * FROM " + CHOICES_TABLE_NAME +
                        " WHERE " + CHOICES_COLUMN_ID + " = " + correctChoiceId, null);
                CorrectChoiceCursor.moveToFirst();
                String correctChoice = CorrectChoiceCursor.getString(
                        CorrectChoiceCursor.getColumnIndex(CHOICES_COLUMN_CHOICE));

                ArrayList<String> choices = new ArrayList<>();
                Random rand = new Random();
                int correctIndex = rand.nextInt(4), counter = 0;
                Cursor choicesCursor = db.rawQuery("SELECT * FROM " + CHOICES_TABLE_NAME + " WHERE "
                                + CHOICES_COLUMN_QUESTION_ID + " = " + id
                                + " ORDER BY RANDOM() LIMIT " + 4,
                        null);
                choicesCursor.moveToFirst();
                while (!choicesCursor.isAfterLast()) {
                    if (choicesCursor.getInt(choicesCursor.getColumnIndex(
                            CHOICES_COLUMN_ID)) == correctChoiceId) {
                        choicesCursor.moveToNext();
                        continue;
                    }
                    choices.add(choicesCursor.getString(choicesCursor.getColumnIndex(
                            CHOICES_COLUMN_CHOICE)));
                    ++counter;
                    choicesCursor.moveToNext();
                }
                choices.add(correctIndex, correctChoice);

                Question question = new Question(header, choices, correctIndex, id);
                questions.add(question);

                questionsCursor.moveToNext();
            }
            limit -= questionsCursor.getCount();
        }

        return questions;
    }

}
