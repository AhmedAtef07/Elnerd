package io.zarda.elnerd.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import io.zarda.elnerd.R;

/**
 * Created by atef & emad on 4 May, 2015.
 * Implementing by magdy.
 */
public class HomeView implements Viewable {

    private Context context;

    private FrameLayout mainLayout;
    private TableLayout layout;
    private Bitmap backgroundBitmap;

    int screenWidth;
    int screenHeight;

    int bitmapWidth;
    int bitmapHeight;

    private Button playButton;
    private TextView bestScore;
    private TextView allPlayed;
    private Button facebook;

    private RelativeLayout relativeLayout;

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
//        mainLayout.addView(layout);
        ((Activity)context).setContentView(mainLayout);
    }

    @Override
    public void endView() {
        ((ViewGroup) mainLayout.getParent()).removeAllViews();
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
//        ImageView imageView = new ImageView(context);
//        imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bg));
//        relativeLayout.addView(imageView);
//        mainLayout.addView(imageView);
        ImageView imageView = new ImageView(context);
        bitmapHeight = screenHeight;
        bitmapWidth = screenWidth;
        loadBitmap(R.drawable.bgim, backgroundBitmap);
        mainLayout.setBackground(context.getResources().getDrawable(R.drawable.bgim));
        mainLayout.addView(setLayoutComponents());
//        mainLayout.addView(setLayoutComponents());

    }

    private TableLayout setLayoutComponents(){
        layout = new TableLayout(context);
        layout.addView(bestScoreView());
        layout.addView(allPlayedView());
        layout.addView(playButtonView());
        layout.addView(facebookView());
        layout.setGravity(Gravity.CENTER);
        return layout;
    }

    private TextView bestScoreView(){
//        TO-DO
        bestScore.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-ThinItalic.ttf"));
        bestScore.setTextColor(Color.parseColor("#ecf0f1"));
        bestScore.setText(bestScore.getText().toString().replace("Best:" , " "));
//        bestScore.setBackground(context.getResources().getDrawable(R.drawable.score));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(-screenWidth/10 , screenHeight/2 + 350 , 0 , 0);
        bestScore.setLayoutParams(params);
        bestScore.setGravity(Gravity.CENTER);
        return bestScore;
    }

    private TextView allPlayedView(){
//        TO-DO
        allPlayed.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-ThinItalic.ttf"));
        allPlayed.setTextColor(Color.parseColor("#ecf0f1"));
        allPlayed.setText(bestScore.getText().toString().replace("Best:" , " "));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(screenWidth/10 , screenHeight/2 + 350 , 0 , 0);
        allPlayed.setLayoutParams(params);
        allPlayed.setGravity(Gravity.CENTER);
        return allPlayed;
    }

    private Button playButtonView(){
        playButton.setBackground(context.getResources().getDrawable(R.drawable.playbutton));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0 , 520 , 0 , 50);
        playButton.setText("PLAY");
        playButton.setLayoutParams(params);
        playButton.setGravity(Gravity.CENTER);
        playButton.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/CANDY___.otf"));
        playButton.setTextColor(Color.parseColor("#e5e6c9"));
        playButton.setTextSize(50);
        return playButton;
    }

    private Button facebookView(){
//        facebook.setBackground(context.getResources().getDrawable(R.drawable.fb));
        return facebook;
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
//                    backgroundBitmap = bitmap;
            }
        }
    }
}
