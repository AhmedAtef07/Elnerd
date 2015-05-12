package io.zarda.elnerd.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Random;

import io.zarda.elnerd.model.Constants.DB;

/**
 * Created by atef & emad on 4 May, 2015.
 */

public class QuestionsDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = DB.NAME.toString();

    private static final String QUESTIONS_TABLE_NAME = DB.Questions.NAME.toString();
    private static final String QUESTIONS_ID = DB.Questions.ID.toString();
    private static final String QUESTIONS_HEADER = DB.Questions.HEADER.toString();
    private static final String QUESTIONS_VIEW_COUNTER = DB.Questions.VIEW_COUNTER.toString();
    private static final String QUESTIONS_QUOTE_ID = DB.Questions.QUOTE_ID.toString();
    private static final String QUESTIONS_MODE_ID = DB.Questions.MODE_ID.toString();
    private static final String QUESTIONS_ANSWER_ID = DB.Questions.ANSWER_ID.toString();

    private static final String CHOICES_TABLE_NAME = DB.Choices.NAME.toString();
    private static final String CHOICES_ID = DB.Choices.ID.toString();
    private static final String CHOICES_HEADER = DB.Choices.HEADER.toString();
    private static final String CHOICES_QUESTION_ID = DB.Choices.QUESTION_ID.toString();

    private static final String MODES_TABLE_NAME = DB.Modes.NAME.toString();
    private static final String MODES_ID = DB.Modes.ID.toString();
    private static final String MODES_TITLE = DB.Modes.TITLE.toString();

    private static final String QUOTES_TABLE_NAME = DB.Quotes.NAME.toString();
    private static final String QUOTES_ID = DB.Quotes.ID.toString();
    private static final String QUOTES_CONTENT = DB.Quotes.CONTENT.toString();
    private static final String QUOTES_BOOK = DB.Quotes.BOOK.toString();
    private static final String QUOTES_USER_ID = DB.Quotes.USER_ID.toString();

    private static QuestionsDB ourInstance;
    private static Context context;
    private int currentViewCounter;

    private QuestionsDB() {
        super(context, DATABASE_NAME, null, 1);
    }

    public static void initialize(Context context) {
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
                        + QUESTIONS_ID + " INTEGER PRIMARY KEY, "
                        + QUESTIONS_HEADER + " TEXT NOT NULL, "
                        + QUESTIONS_ANSWER_ID + " INTEGER, "
                        + QUESTIONS_QUOTE_ID + " INTEGER, "
                        + QUESTIONS_MODE_ID + " INTEGER, "
                        + QUESTIONS_VIEW_COUNTER + " INTEGER)"
        );

        db.execSQL(
                "CREATE TABLE "
                        + CHOICES_TABLE_NAME + " ("
                        + CHOICES_ID + " INTEGER PRIMARY KEY, "
                        + CHOICES_HEADER + " TEXT NOT NULL, "
                        + CHOICES_QUESTION_ID + " INTEGER NOT NULL)"
        );

        db.execSQL(
                "CREATE TABLE "
                        + MODES_TABLE_NAME + " ("
                        + MODES_ID + " INTEGER PRIMARY KEY, "
                        + MODES_TITLE + " TEXT NOT NULL)"
        );

        db.execSQL(
                "CREATE TABLE "
                        + QUOTES_TABLE_NAME + " ("
                        + QUOTES_ID + " INTEGER PRIMARY KEY, "
                        + QUOTES_CONTENT + " TEXT NOT NULL, "
                        + QUOTES_BOOK + " TEXT NOT NULL, "
                        + QUOTES_USER_ID + " INTEGER NOT NULL)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QUESTIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CHOICES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MODES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QUOTES_TABLE_NAME);
        onCreate(db);
    }

    public void addQuote(Quote quote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(QUOTES_CONTENT, quote.getContent());
        contentValues.put(QUOTES_BOOK, quote.getBook());
        contentValues.put(QUOTES_USER_ID, quote.getUserId());

        int quoteId = (int) db.insert(QUOTES_TABLE_NAME, null, contentValues);
        addQuestion(quote.getQuestion(), quoteId);
    }

    public int addQuestion(Question question, int quoteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(QUESTIONS_HEADER, question.getHeader());
        contentValues.put(QUESTIONS_QUOTE_ID, quoteId);
        contentValues.put(QUESTIONS_MODE_ID, question.getModeId());
        contentValues.put(QUESTIONS_VIEW_COUNTER, 0);

        int questionId = (int) db.insert(QUESTIONS_TABLE_NAME, null, contentValues);

        int answerId = 0;
        for (int i = 0; i < question.getChoices().size(); ++i) {
            if (i != question.getCorrectIndex()) {
                addChoice(question.getChoices().get(i), questionId);
            } else {
                answerId = addChoice(question.getChoices().get(i), questionId);
                addAnswer(answerId, questionId);
            }
        }

        contentValues.put(QUESTIONS_ANSWER_ID, answerId);
        db.update(QUESTIONS_TABLE_NAME, contentValues, QUESTIONS_ID + " = ? ",
                new String[]{Integer.toString(questionId)});
        return questionId;
    }

    private int addChoice(String choice, int questionId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CHOICES_HEADER, choice);
        contentValues.put(CHOICES_QUESTION_ID, questionId);

        return (int) db.insert(CHOICES_TABLE_NAME, null, contentValues);
    }

    private void addAnswer(int choiceId, int questionId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(QUESTIONS_ANSWER_ID, choiceId);

        db.update(QUESTIONS_TABLE_NAME, contentValues, QUESTIONS_ID + " = ? ",
                new String[]{Integer.toString(questionId)});
    }

    private int addMode(String title) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor modesCursor = db.rawQuery("SELECT * FROM " + MODES_TABLE_NAME + " WHERE "
                + MODES_TITLE + " = " + title, null);

        if (modesCursor.getCount() == 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MODES_TITLE, title);
            return (int) db.insert(MODES_TABLE_NAME, null, contentValues);
        } else {
            modesCursor.moveToFirst();
            return modesCursor.getInt(modesCursor.getColumnIndex(MODES_ID));
        }
    }

    public int getNumberOfQuestions() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, QUESTIONS_TABLE_NAME);
        return numRows;
    }

    public boolean updateQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(QUESTIONS_HEADER, question.getHeader());

        db.update(QUESTIONS_TABLE_NAME, contentValues, QUESTIONS_ID + " = ? ",
                new String[]{Integer.toString(question.getId())});
        return true;
    }

    public boolean updateViewCounter(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor questionsCursor = db.rawQuery("SELECT * FROM " + QUESTIONS_TABLE_NAME, null);
        questionsCursor.moveToFirst();

        int viewCounter = questionsCursor.getInt(questionsCursor.getColumnIndex(
                QUESTIONS_VIEW_COUNTER));

        contentValues.put(QUESTIONS_VIEW_COUNTER, ++viewCounter);

        db.update(QUESTIONS_TABLE_NAME, contentValues, QUESTIONS_ID + " = ? ",
                new String[]{Integer.toString(question.getId())});
        return true;
    }

    public ArrayList getRandomQuestions(int limit) {
        ArrayList<Quote> quotes = new ArrayList<>();

        if (getNumberOfQuestions() > 0) {
            SQLiteDatabase db = this.getReadableDatabase();
            currentViewCounter = 0;
            while (limit > 0) {
                Cursor questionsCursor = db.rawQuery("SELECT * FROM " + QUESTIONS_TABLE_NAME + " WHERE "
                        + QUESTIONS_VIEW_COUNTER + " = " + currentViewCounter
                        + " ORDER BY RANDOM() LIMIT " + limit, null);

                if (questionsCursor.getCount() < limit) {
                    ++currentViewCounter;
                }

                questionsCursor.moveToFirst();
                while (!questionsCursor.isAfterLast()) {
                    int id = questionsCursor.getInt(questionsCursor.getColumnIndex(
                            QUESTIONS_ID));
                    int quoteId = questionsCursor.getInt(questionsCursor.getColumnIndex(
                            QUESTIONS_QUOTE_ID));
                    int modeId = questionsCursor.getInt(questionsCursor.getColumnIndex(
                            QUESTIONS_MODE_ID));

                    int correctChoiceId = questionsCursor.getInt(questionsCursor.getColumnIndex(
                            QUESTIONS_ANSWER_ID));
                    String header = questionsCursor.getString(questionsCursor.getColumnIndex(
                            QUESTIONS_HEADER));

                    Cursor CorrectChoiceCursor = db.rawQuery("SELECT * FROM " + CHOICES_TABLE_NAME +
                            " WHERE " + CHOICES_ID + " = " + correctChoiceId, null);
                    CorrectChoiceCursor.moveToFirst();
                    String correctChoice = CorrectChoiceCursor.getString(
                            CorrectChoiceCursor.getColumnIndex(CHOICES_HEADER));

                    ArrayList<String> choices = new ArrayList<>();
                    Random rand = new Random();
                    int correctIndex = rand.nextInt(4), counter = 0;
                    Cursor choicesCursor = db.rawQuery("SELECT * FROM " + CHOICES_TABLE_NAME + " WHERE "
                                    + CHOICES_QUESTION_ID + " = " + id
                                    + " ORDER BY RANDOM() LIMIT " + 4,
                            null);
                    choicesCursor.moveToFirst();
                    while (!choicesCursor.isAfterLast()) {
                        if (choicesCursor.getInt(choicesCursor.getColumnIndex(
                                CHOICES_ID)) == correctChoiceId) {
                            choicesCursor.moveToNext();
                            continue;
                        }
                        choices.add(choicesCursor.getString(choicesCursor.getColumnIndex(
                                CHOICES_HEADER)));
                        ++counter;
                        choicesCursor.moveToNext();
                    }
                    choices.add(correctIndex, correctChoice);

                    Question question = new Question(
                            header, choices, correctIndex, id, quoteId, modeId);

                    Cursor quotesCursor = db.rawQuery("SELECT * FROM " + QUOTES_TABLE_NAME + " WHERE "
                            + QUOTES_ID + " = " + quoteId, null);
                    String content = quotesCursor.getString(questionsCursor.getColumnIndex(
                            QUOTES_CONTENT));
                    String book = quotesCursor.getString(questionsCursor.getColumnIndex(
                            QUOTES_BOOK));
                    int userId = questionsCursor.getInt(questionsCursor.getColumnIndex(
                            QUOTES_USER_ID));

                    Quote quote = new Quote(content, question, book, quoteId, userId, 0);
                    quotes.add(quote);

                    questionsCursor.moveToNext();
                }
                limit -= questionsCursor.getCount();
                questionsCursor.close();
            }
        }

        return quotes;
    }

}
