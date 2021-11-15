package tools;

/**
 * Created by Omid on 3/14/2018.
 */

import android.os.Handler;

import com.dd.processbutton.ProcessButton;

import java.util.Random;

public class ProgressGenerator {

    public interface OnCompleteListener {

        public void onComplete();
    }

    private OnCompleteListener mListener;
    private int mProgress;

    public ProgressGenerator(OnCompleteListener listener) {
        mListener = listener;
    }

    public void start(final ProcessButton button) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgress += 10;
                button.setProgress(mProgress);
                if (mProgress < 100) {
                    handler.postDelayed(this, generateDelay());
                } else {
                    mListener.onComplete();
                }
            }
        }, generateDelay());


    }

    public void startInfinite(final ProcessButton button) {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                button.setProgress(10);
            }
        });
    }

    public void finish(final ProcessButton button) {
        button.setProgress(100);
        mListener.onComplete();
    }

    private Random random = new Random();

    private int generateDelay() {
        return random.nextInt(1000);
    }
}