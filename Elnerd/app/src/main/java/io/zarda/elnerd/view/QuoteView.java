package io.zarda.elnerd.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;

import io.zarda.elnerd.R;

/**
 * Created by ahmed on 5/12/2015.
 */
public class QuoteView implements Viewable , Game{
    Context context;

    Bitmap bitmap;

    FrameLayout mainLayout;
    RelativeLayout displayLayout;
    TableLayout layout;

    private TextView quote;
    private TextView bookName;
    private int time = 6000;

    private int screenWidth;
    private int screenHeight;

    private float degree = 0;
    private int randomIndex = 0;

    RelativeLayout barLayout;
    View bar;
    View barBackground;

    int [] colors = {R.drawable.display , R.drawable.display1 , R.drawable.display2 ,
            R.drawable.display3};


    @Override
    public void initializeView(Context context, List<View> views) {
        this.context = context;
        quote = (TextView) views.get(0);
        bookName = (TextView) views.get(1);
        mainLayout = new FrameLayout(context);
        layout = new TableLayout(context);
        setScreenDimention();
        bar = new View(context);
        barBackground = new View(context);

        barLayout = new RelativeLayout(context);
        barBackground.setBackgroundColor(Color.DKGRAY);
        barBackground.setMinimumWidth(screenWidth);
        bar.setBackgroundColor(Color.GRAY);
        bar.setMinimumWidth(screenWidth);
        barLayout.addView(barBackground);
        barLayout.addView(bar);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = 50;
        params.width = screenWidth;
        barLayout.setLayoutParams(params);
        layout.addView(barLayout);
    }


    @Override
    public void startView() {
        mainLayout.addView(layout);
        setMainLayout();
        loadBitmap(R.drawable.gpbg, bitmap);
    }

    @Override
    public void endView() {
        mainLayout.removeAllViews();
    }

    @Override
    public void showSuccess(Button correctButton) {

    }

    @Override
    public void showFailure(Button correctButton, Button wrongButton) {

    }

    @Override
    public void showNextQuestion() {

    }

    //    public QuoteView(){
//
//    }
    @Override
    public void setTime(int time){
        this.time = time;
    }

    private void setScreenDimention(){
        Display screen = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        screen.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    private void setMainLayout(){
        displayLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams displayParams = new RelativeLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        displayLayout.setLayoutParams(displayParams);
        displayLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.addView(displayLayout);
    }

    public void showQuote(){
        getRandomIndex(3);
        getRandomDegree(10);

        quote.setWidth(screenWidth);
        quote.setTextDirection(View.TEXT_DIRECTION_RTL);
        quote.setBackground(context.getResources().getDrawable(colors[randomIndex]));
        quote.setGravity(Gravity.CENTER);
        quote.setTypeface(Typeface.createFromAsset(
                context.getAssets() ,"fonts/DroidKufi-Regular.ttf"));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        quote.setLayoutParams(params);
        quote.setTranslationX(new Random().nextFloat() * 20 - 10);
        quote.setTranslationY(new Random().nextFloat() * 20 - 10);

        AnimationSet dropAnimation = new AnimationSet(false);

        Animation rotateAnimation = new RotateAnimation(0.0f , degree , screenWidth/2 ,
                screenHeight/4);
        rotateAnimation.setDuration(100);

        Animation scaleAnimation = new ScaleAnimation(1.0f , 0.85f , 1f , 0.7f , screenWidth / 2 ,
                screenHeight / 2);
        scaleAnimation.setDuration(100);

        dropAnimation.addAnimation(rotateAnimation);
        dropAnimation.addAnimation(scaleAnimation);
        dropAnimation.setFillAfter(true);

        displayLayout.addView(quote);
        quote.startAnimation(dropAnimation);
        timeAnimation();

        bookName.setWidth(screenWidth);
        bookName.setTextDirection(View.TEXT_DIRECTION_RTL);
        bookName.setBackground(context.getResources().getDrawable(colors[randomIndex]));
        bookName.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams paramsBook = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        bookName.setLayoutParams(paramsBook);
        bookName.setTranslationX(new Random().nextFloat() * 20 - 10);
        bookName.setTranslationY(new Random().nextFloat() * 20 - 10);
        bookName.setTypeface(Typeface.createFromAsset(
                context.getAssets() ,"fonts/DroidKufi-Regular.ttf"));
        layout.addView(bookName);
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

    public void loadBitmap(int resId, Bitmap bitmap) {
        BitmapWorkerTask task = new BitmapWorkerTask(bitmap);
        task.execute(resId);
    }

    private void timeAnimation(){
        Animation move = new TranslateAnimation(0 , -screenWidth , 0 , 0);
        move.setDuration(time);
        bar.startAnimation(move);
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
            }
        }
    }

}
