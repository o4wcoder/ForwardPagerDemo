package com.fourthwardmobile.forwardpagerdemo;

import android.animation.ArgbEvaluator;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    int mColor1;
    int mColor2;
    int mColor3;
    int[] mColorList;

    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mColor1 = ContextCompat.getColor(this,R.color.cyan);
        mColor2 = ContextCompat.getColor(this,R.color.orange);
        mColor3 = ContextCompat.getColor(this,R.color.green);
        mColorList = new int[]{mColor1,mColor2,mColor3};

        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPager.setAdapter(new ForwardPagerAdapter(this));
        mViewPager.setPageTransformer(true,new DepthPageTransformer());



        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                //Fade between colors
                ArgbEvaluator evaluator = new ArgbEvaluator();
                int colorUpdate = (Integer) evaluator.evaluate(positionOffset,mColorList[position],
                        mColorList[position == 2 ? position : position + 1]);
                mViewPager.setBackgroundColor(colorUpdate);
            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        mViewPager.setBackgroundColor(mColor1);
                        break;
                    case 1:
                        mViewPager.setBackgroundColor(mColor2);
                        break;
                    case 2:
                        mViewPager.setBackgroundColor(mColor3);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
