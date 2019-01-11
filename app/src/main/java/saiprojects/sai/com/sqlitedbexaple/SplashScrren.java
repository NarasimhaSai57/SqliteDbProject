package saiprojects.sai.com.sqlitedbexaple;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScrren extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_scrren);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScrren.this,MainActivity.class);
                SplashScrren.this.startActivity(mainIntent);
                SplashScrren.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
