package me.chenfuduo.parallaxsplash;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

/**
 *
 */
public class MainActivity extends FragmentActivity {

    ImageView iv_man;
    ParallaxContainer container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = (ParallaxContainer) findViewById(R.id.parallax_container);
        container.setUp(new int[]{
                R.layout.view_intro_1,
                R.layout.view_intro_2,
                R.layout.view_intro_3,
                R.layout.view_intro_4,
                R.layout.view_intro_5,
                R.layout.view_login,
        });

        iv_man = (ImageView) findViewById(R.id.iv_man);
        container.setImage(iv_man);
        iv_man.setBackgroundResource(R.anim.man_run);
    }


}
