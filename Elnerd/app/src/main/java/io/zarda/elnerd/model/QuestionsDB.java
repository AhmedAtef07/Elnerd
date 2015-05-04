package io.zarda.elnerd.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by atef & emad on 4 May, 2015.
 */

public class QuestionsDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "elnerd.db";

    public static final String QUESTIONS_TABLE_NAME = "questionsTable";
    public static final String CHOICES_TABLE_NAME = "choicesTable";
    public static final String ANSWERS_TABLE_NAME = "answersTable";

    public static final String QUESTIONS_COLUMN_ID = "id";
    public static final String QUESTIONS_COLUMN_HEADER = "header";
    public static final String QUESTIONS_COLUMN_ANSWER_ID = "answer_id";

    public static final String CHOICES_COLUMN_ID = "id";
    public static final String CHOICES_COLUMN_CHOICE = "choice";
    public static final String CHOICES_COLUMN_QUESTION_ID = "question_id";

    public static final String ANSWERS_COLUMN_ID = "id";
    public static final String ANSWERS_COLUMN_CHOICE_ID = "choice_id";
    public static final String ANSWERS_COLUMN_QUESTION_ID = "question_id";


    public QuestionsDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE "
                        + QUESTIONS_TABLE_NAME + " ("
                        + QUESTIONS_COLUMN_ID + " INTEGER PRIMARY KEY, "
                        + QUESTIONS_COLUMN_HEADER + " TEXT, "
                        + QUESTIONS_COLUMN_ANSWER_ID + " INTEGER)"
        );

        db.execSQL(
                "CREATE TABLE "
                        + CHOICES_TABLE_NAME + " ("
                        + CHOICES_COLUMN_ID + " INTEGER PRIMARY KEY, "
                        + CHOICES_COLUMN_CHOICE + " TEXT, "
                        + CHOICES_COLUMN_QUESTION_ID + " INTEGER)"
        );

        db.execSQL(
                "CREATE TABLE "
                        + ANSWERS_TABLE_NAME + " ("
                        + ANSWERS_COLUMN_ID + " INTEGER PRIMARY KEY, "
                        + ANSWERS_COLUMN_CHOICE_ID + " INTEGER, "
                        + ANSWERS_COLUMN_QUESTION_ID + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + QUESTIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CHOICES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ANSWERS_TABLE_NAME);
        onCreate(db);
    }

    public void addQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(QUESTIONS_COLUMN_HEADER, question.getHeader());

        int questionId = (int) db.insert(QUESTIONS_TABLE_NAME, null, contentValues);

        int answerId = 0, correctChoiceId;

        for (int i = 0; i < question.getChoices().size(); ++i) {
            if(i != question.getCorrectIndex()) {
                addChoice(question.getChoices().get(i), questionId);
            }
            else {
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

    public boolean updateQuestion (Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(QUESTIONS_COLUMN_HEADER, question.getHeader());

        db.update(QUESTIONS_TABLE_NAME, contentValues, QUESTIONS_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(question.getId()) } );
        return true;
    }

    public ArrayList getQuestions() {
        ArrayList<Question> questions = new ArrayList<Question>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor questionsCursor =  db.rawQuery("SELECT * FROM " + QUESTIONS_TABLE_NAME, null);
        questionsCursor.moveToFirst();
        while(questionsCursor.isAfterLast() == false){
            int id = questionsCursor.getInt(questionsCursor.getColumnIndex(QUESTIONS_COLUMN_ID));
            String header = questionsCursor.getString(questionsCursor.getColumnIndex(
                    QUESTIONS_COLUMN_HEADER));

//            int answerId = Integer.parseInt(questionsCursor.getString(
//                    questionsCursor.getColumnIndex(QUESTIONS_COLUMN_ANSWER_ID)));
//            System.out.println("Q: " + header + ", id: " + id + ", ansId: " + answerId);

            int correctChoiceId = 0;
            Cursor answersCursor =  db.rawQuery("SELECT * FROM " + ANSWERS_TABLE_NAME + " WHERE "
                    + ANSWERS_COLUMN_QUESTION_ID + " = " + id, null);
            answersCursor.moveToFirst();
            while(answersCursor.isAfterLast() == false) {
                correctChoiceId = answersCursor.getInt(answersCursor.getColumnIndex(
                        ANSWERS_COLUMN_CHOICE_ID));
                answersCursor.moveToNext();
            }

            ArrayList<String> choices = new ArrayList<String>();
            int correctIndex = 0, counter = 0;
            Cursor choicesCursor =  db.rawQuery("SELECT * FROM " + CHOICES_TABLE_NAME + " WHERE "
                    + CHOICES_COLUMN_QUESTION_ID + " = " + id, null);
            choicesCursor.moveToFirst();
            while(choicesCursor.isAfterLast() == false) {
                choices.add(choicesCursor.getString(choicesCursor.getColumnIndex(
                        CHOICES_COLUMN_CHOICE)));
                if(choicesCursor.getInt(choicesCursor.getColumnIndex(
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

}
