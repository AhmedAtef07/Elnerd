package io.zarda.elnerd.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.zarda.elnerd.R;
import io.zarda.elnerd.src.GameViewNotifier;

/**
 * Created by atef & emad on 4 May, 2015.
 * Implementing by magdy.
 */
public class GameView implements Viewable , Game{
    Context context;

    int screenWidth;
    int screenHeight;

    boolean isFirst = true;

    FrameLayout mainLayout;
    Bitmap bitmap;
    TableLayout layout;
    Bitmap correctBitmap;
    Bitmap wrongBitmap;
    ImageView correctImage;
    ImageView wrongImage;
    Drawable background;

    AnimationSet reponseAnimation;

    int [] colors = {R.drawable.display , R.drawable.display1 , R.drawable.display2 ,
            R.drawable.display3};

    RelativeLayout displayLayout;
    TextView cardMain;

    Button firstChoice;
    Button secondChoice;
    Button thirdChoice;
    Button forthChoice;

    ArrayList <TextView> cards = new ArrayList<TextView>();
    GameViewNotifier gvn;
    private float degree = 0;
    private int randomIndex = 0;

    public GameView(GameViewNotifier gvn){
        this.gvn = gvn;
    }

    @Override
    public void initializeView(Context context , List<View> views) {
        this.context = context;
        cardMain = (TextView) views.get(0);
        firstChoice = (Button) views.get(1);
        secondChoice = (Button) views.get(2);
        thirdChoice = (Button) views.get(3);
        forthChoice = (Button) views.get(4);

        setDimension();
    }

    @Override
    public void startView() {
//        mainLayout.addView(layout);

        setLayout();
        setDisplayLayout();
        setButtons();

        setBitmapsAndAnimation();
//        ((Activity) context).setContentView(mainLayout);
        buttonsInAnimation(0);
    }

    @Override
    public void endView() {
        layout.removeAllViews();
        mainLayout.removeAllViews();
    }

    @Override
    public void showSuccess(final Button correctButton) {
        correctButton.setBackground(context.getResources().getDrawable(R.drawable.correctbtn));
        buttonsOutAnimation(true);
        correctImage.startAnimation(reponseAnimation);
        gvn.notifyShowSuccessFinished();

        // mainLayout.
//        if (!correctImage.isShown() && !wrongImage.isShown()) {
//            correctImage.startAnimation(reponseAnimation);
//            mainLayout.addView(correctImage);
//
////            correctImage.startAnimation(reponseAnimation);
//            System.out.println("frames 2 " + SystemClock.currentThreadTimeMillis());
//
//            reponseAnimation.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    ((ViewGroup) (correctImage.getParent())).removeView(correctImage);
//                    buttonsOutAnimation();
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//        }
    }

    @Override
    public void showFailure(Button correctButton, Button wrongButton) {

        wrongButton.setBackground(context.getResources().getDrawable(R.drawable.wrongbtn));
        gvn.notifyShowFailureFinished();

//        wrongImage = new ImageView(context);
//        wrongImage.startAnimation(reponseAnimation);
//        reponseAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                gvn.notifyShowFailureFinished();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });

//        buttonsOutAnimation(false);
//        gvn.notifyShowFailureFinished();

//        if (!wrongImage.isShown() && !correctImage.isShown()) {
//            mainLayout.addView(wrongImage);
//            wrongImage.startAnimation(reponseAnimationFail);
//
//            reponseAnimationFail.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    ((ViewGroup) (wrongImage.getParent())).removeView(wrongImage);
//                    gvn.notifyShowFailureFinished();
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//        }
    }

    @Override
    public void showNextQuestion() {
        newQuestion();
    }

    private void setBitmapsAndAnimation(){
//        correctBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.correct);
//        wrongBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.wrong);

        correctImage = new ImageView(context);
//        wrongImage = new ImageView(context);
        mainLayout.addView(correctImage);
//        mainLayout.addView(wrongImage);
        Animation setAnimation = new ScaleAnimation(0 ,0 ,0 ,0);
        setAnimation.setFillAfter(true);
        correctImage.startAnimation(setAnimation);
//        wrongImage.startAnimation(setAnimation);

        correctImage.setLayerType(View.LAYER_TYPE_HARDWARE , null);
//        wrongImage.setLayerType(View.LAYER_TYPE_HARDWARE , null);

        correctImage.setImageBitmap(correctBitmap);
//        wrongImage.setImageBitmap(wrongBitmap);
        FrameLayout.LayoutParams correctParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT , FrameLayout.LayoutParams.WRAP_CONTENT);


        correctParams.height = screenWidth;
        correctParams.width = screenWidth;

        correctImage.setLayoutParams(correctParams);
//        wrongImage.setLayoutParams(correctParams);


        Animation fadeAnimation = new AlphaAnimation(0.9f , 0.0f);
        fadeAnimation.setDuration(1000);

        Animation scaleAnimation = new ScaleAnimation(0.25f , 1f , 0.25f , 1f ,
                screenWidth/2 , screenHeight/2);
        scaleAnimation.setDuration(1000);

        reponseAnimation = new AnimationSet(false);
        reponseAnimation.addAnimation(fadeAnimation);
        reponseAnimation.addAnimation(scaleAnimation);
        reponseAnimation.setDuration(1000);
        reponseAnimation.setFillAfter(true);
    }

    private void setLayout(){
//        background = context.getResources().getDrawable(R.drawable.bg);
        layout = new TableLayout(context);
        layout.setGravity(Gravity.CENTER);
//        layout.setBackground(context.getResources().getDrawable(R.drawable.gpbg));
        loadBitmap(R.drawable.gpbg, bitmap);

        mainLayout = new FrameLayout(context);
//        mainLayout.setBackground(context.getResources().getDrawable(R.drawable.gmbg));
        mainLayout.addView(layout);
    }

    private void setDisplayLayout(){
        displayLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams displayParams = new RelativeLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        displayParams.width = screenWidth;
        displayParams.height = (int)(screenHeight * 0.5);
        displayLayout.setLayoutParams(displayParams);
        displayLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.addView(displayLayout);
    }

    private void setDimension(){
        Display screen = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        screen.getSize(size);
        this.screenWidth = size.x;
        this.screenHeight = size.y;
    }

    private void setButtons(){
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(20 ,20 ,20 ,20);

        firstChoice.setLayoutParams(params);
        secondChoice.setLayoutParams(params);
        thirdChoice.setLayoutParams(params);
        forthChoice.setLayoutParams(params);

        setButtonsDefaultColorAndTextColor();


        layout.addView(firstChoice);
        layout.addView(secondChoice);
        layout.addView(thirdChoice);
        layout.addView(forthChoice);

    }

    private void setButtonsDefaultColorAndTextColor(){
        firstChoice.setBackground(context.getResources().getDrawable(R.drawable.btn));
        secondChoice.setBackground(context.getResources().getDrawable(R.drawable.btn));
        thirdChoice.setBackground(context.getResources().getDrawable(R.drawable.btn));
        forthChoice.setBackground(context.getResources().getDrawable(R.drawable.btn));
        firstChoice.setTextColor(Color.parseColor("#ecf0f1"));
        secondChoice.setTextColor(Color.parseColor("#ecf0f1"));
        thirdChoice.setTextColor(Color.parseColor("#ecf0f1"));
        forthChoice.setTextColor(Color.parseColor("#ecf0f1"));
    }

    private void buttonsInAnimation(int delay){
        firstChoice.startAnimation(fromRight(0 + delay));
        secondChoice.startAnimation(fromRight(75 + delay));
        thirdChoice.startAnimation(fromRight(150 + delay));
        forthChoice.startAnimation(fromRight(225 + delay));
    }

    private void buttonsOutAnimation(final boolean answer){
        forthChoice.startAnimation(toLeft(0));
        thirdChoice.startAnimation(toLeft(75));
        secondChoice.startAnimation(toLeft(150));
        firstChoice.startAnimation(toLeft(225));
    }

    private void getRandomIndex(int x){
        Random random = new Random();
        int randomValue = random.nextInt(x);
        while (randomValue == randomIndex){
            randomValue = random.nextInt(x);
        }
        randomIndex =  randomValue;
    }

    private void getRandomDegree(float x){
        Random random = new Random();
        float randomDegree = random.nextFloat() * x - (x / 2);
        while(randomDegree == degree){
            randomDegree = random.nextFloat() * x - (x / 2);
        }
        degree = randomDegree;
    }

    private void addCard(){
        if(cards.size() == 5){
            displayLayout.removeView(cards.get(0));
            cards.remove(0);
        }
        System.out.println("cards no : "+ cards.size());
        TextView card = new Button(context);
        cards.add(card);
        card.setText(cardMain.getText());
        getRandomIndex(3);
        getRandomDegree(10);

        card.setWidth(screenWidth);
        card.setBackground(context.getResources().getDrawable(colors[randomIndex]));
        card.setGravity(Gravity.CENTER);
        card.setWidth(screenWidth);
        card.setHeight((int) (screenHeight * 0.5));
        card.setTranslationX(new Random().nextFloat() * 20 - 10);
        card.setTranslationY(new Random().nextFloat() * 20 - 10);

        AnimationSet dropAnimation = new AnimationSet(false);

        Animation rotateAnimation = new RotateAnimation(0.0f , degree , screenWidth/2 ,
                screenHeight/4);
        rotateAnimation.setDuration(100);

        Animation scaleAnimation = new ScaleAnimation(1.0f , 0.85f , 1f , 0.7f , screenWidth / 2 ,
                screenHeight / 4);
        scaleAnimation.setDuration(100);

        dropAnimation.addAnimation(rotateAnimation);
        dropAnimation.addAnimation(scaleAnimation);
        dropAnimation.setFillAfter(true);

        buttonsInAnimation(0);

        displayLayout.addView(card);
        card.startAnimation(dropAnimation);
    }

    private void newQuestion(){
        addCard();
        setButtonsDefaultColorAndTextColor();
    }

    private Animation fromRight(int delay){
        Animation fromRight = new TranslateAnimation(screenWidth , 10 , 0 , 0);
        fromRight.setDuration(100);
        fromRight.setStartOffset(delay);

        return fromRight;
    }

    private Animation toLeft(int delay){
        Animation toLeft = new TranslateAnimation(0 , -2*screenWidth - 20 , 0 , 0);
        toLeft.setDuration(100);
        toLeft.setStartOffset(delay);
        toLeft.setFillAfter(true);

        return toLeft;
    }

    private Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                   int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void loadBitmap(int resId, Bitmap bitmap) {
        BitmapWorkerTask task = new BitmapWorkerTask(bitmap);
        task.execute(resId);
    }

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<Bitmap> imageViewReference;
        private int data = 0;
        private Bitmap bitmap;

        public BitmapWorkerTask(Bitmap bitmap) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<Bitmap>(bitmap);
            this.bitmap = bitmap;
        }

        // Decode image in nd.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            return decodeSampledBitmapFromResource(context.getResources(), data, 500,
                    500);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                Drawable drawable = new BitmapDrawable(bitmap);
                mainLayout.setBackground(drawable);
                ((Activity) context).setContentView(mainLayout);
//                System.out.println("thread ! : " + backgroundBitmap);
            }
        }
    }
}
