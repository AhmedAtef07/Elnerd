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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.List;

import io.zarda.elnerd.R;

/**
 * Created by atef & emad on 4 May, 2015.
 * Implementing by magdy.
 */
public class HomeView implements Viewable {

    int screenWidth;
    int screenHeight;
    TextView transitionView;
    int bitmapWidth;
    int bitmapHeight;
    private Context context;
    private FrameLayout mainLayout;
    private TableLayout layout;
    private Bitmap backgroundBitmap;
    private Button playButton;
    private TextView bestScore;
    private TextView allPlayed;
    private Button facebook;


    @Override
    public void initializeView(Context context , List<View> views) {
        this.context = context;
        playButton = (Button) views.get(0);
        bestScore = (TextView) views.get(1);
        allPlayed = (TextView) views.get(2);
        facebook = (Button) views.get(3);

        setScreenDimention();
        setMainLayout();
    }

    @Override
    public void startView() {
        Animation transitionSet = new AlphaAnimation(0, 0);
        transitionSet.setFillAfter(true);
        transitionView.setAnimation(transitionSet);
        ((Activity)context).setContentView(mainLayout);
        loadBitmap(R.drawable.bgim, backgroundBitmap);


    }

    @Override
    public void endView() {

        ((ViewGroup) mainLayout.getParent()).removeAllViews();
//        goAway();


    }

    private void setScreenDimention(){
        Display screen = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        screen.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    private void setMainLayout(){
        mainLayout = new FrameLayout(context);
        bitmapHeight = screenHeight;
        bitmapWidth = screenWidth;
        mainLayout.addView(setLayoutComponents());
        transitionView = new TextView(context);
        transitionView.setWidth(screenWidth);
        transitionView.setHeight(screenHeight / 2);
        RelativeLayout relativeLayout1 = new RelativeLayout(context);
        transitionView.setBackground(context.getResources().getDrawable(R.drawable.gmbg));
        Animation transitionSet = new AlphaAnimation(0, 0);
        transitionSet.setFillAfter(true);
        transitionView.setAnimation(transitionSet);
//        relativeLayout1.addView(transitionView);
        mainLayout.addView(relativeLayout1);
    }

    private TableLayout setLayoutComponents(){
        layout = new TableLayout(context);
        LinearLayout firstRow = new LinearLayout(context);
        firstRow.addView(playButtonView());
        firstRow.setPadding(0, screenHeight / 2 - 150, 0, 0);
        firstRow.setGravity(Gravity.CENTER);
        layout.addView(firstRow);
        LinearLayout forthRow = new LinearLayout(context);
        forthRow.addView(bestScoreView());
        forthRow.addView(allPlayedView());
//        layout.addView(playButtonView());
//        layout.addView(allPlayedView());
        forthRow.setGravity(Gravity.CENTER);
        forthRow.setPadding(0, 200, 0, 200);
        layout.addView(forthRow);
        layout.addView(facebookView());
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        return layout;
    }

    private TextView bestScoreView(){
//        TO-DO
        bestScore.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-ThinItalic.ttf"));
        bestScore.setTextColor(Color.parseColor("#e5e6c9"));
        bestScore.setText("50");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = 350;
        params.height = 150;
        bestScore.setLayoutParams(params);
        bestScore.setBackground(context.getResources().getDrawable(R.drawable.bestscore));
        bestScore.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        bestScore.setPadding(190, 20, 0, 0);
        bestScore.setTextSize(25);
        return bestScore;
    }

    private TextView allPlayedView(){
//        TO-DO
        allPlayed.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-ThinItalic.ttf"));
        allPlayed.setTextColor(Color.parseColor("#e5e6c9"));
        allPlayed.setText(bestScore.getText().toString().replace("Best:" , " "));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = 350;
        params.height = 150;
        params.setMargins(-20, 0, 0, 0);
        allPlayed.setLayoutParams(params);
        allPlayed.setBackground(context.getResources().getDrawable(R.drawable.allplayed));
        allPlayed.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        allPlayed.setPadding(50, 20, 0, 0);
        allPlayed.setTextSize(25);
        return allPlayed;
    }

    private Button playButtonView(){
        playButton.setBackground(context.getResources().getDrawable(R.drawable.playbutton));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = 300;
        params.height = 300;
        params.setMargins(0 , 520 , 0 , 50);
        playButton.setLayoutParams(params);
        playButton.setGravity(Gravity.CENTER);
        playButton.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/CANDY___.otf"));
        playButton.setTextColor(Color.parseColor("#e5e6c9"));
        playButton.setTextSize(50);
        return playButton;
    }

    private Button facebookView(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        facebook.setLayoutParams(params);
        return facebook;
    }

    private void goAway() {
        playButtonGOAnimation();
        bestScoreGoAnimation();
        allPlayedGoAnimation();
        facebookGoAnimation();
        screenUP();
        ((ViewGroup) mainLayout.getParent()).removeAllViews();
    }

    private void playButtonGOAnimation() {
        Animation playGo = new TranslateAnimation(0, -2 * screenWidth - 20, 0, 0);
        playGo.setDuration(2000);
//        playGo.setFillAfter(true);
        playButton.startAnimation(playGo);
    }

    private void bestScoreGoAnimation() {
        Animation bestScoreGo = new TranslateAnimation(0, -2 * screenWidth - 20, 0, 0);
        bestScoreGo.setDuration(2000);
//        bestScoreGo.setFillAfter(true);
        bestScore.startAnimation(bestScoreGo);
    }

    private void allPlayedGoAnimation() {
        Animation allPlayedGo = new TranslateAnimation(0, -screenWidth, 0, 0);
        allPlayedGo.setDuration(2000);
//        allPlayedGo.setFillAfter(true);
        allPlayed.startAnimation(allPlayedGo);
    }

    private void facebookGoAnimation() {
        Animation facebookGo = new TranslateAnimation(0, 0, 0, 2 * screenHeight);
        facebookGo.setDuration(2000);
//        facebookGo.setFillAfter(true);
        facebook.startAnimation(facebookGo);
    }

    private void screenUP() {
        Animation alpha = new AlphaAnimation(0, 1);
        alpha.setStartOffset(1000);
        alpha.setDuration(1000);
        transitionView.startAnimation(alpha);
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

    public void loadBitmap(int resId , Bitmap bitmap) {
        BitmapWorkerTask task = new BitmapWorkerTask();
        task.execute(resId);
    }

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private int data = 0;

        public BitmapWorkerTask() {
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            return decodeSampledBitmapFromResource(context.getResources(), data, 500,
                    500 );
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                Drawable drawable = new BitmapDrawable(bitmap);
                mainLayout.setBackground(drawable);

//                System.out.println("thread ! : " + backgroundBitmap);
            }
        }
    }
}
