package me.chenfuduo.parallaxsplash;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 * 引导页的最外层布局
 */
public class ParallaxContainer extends FrameLayout implements ViewPager.OnPageChangeListener {
    private List<ParallaxFragment> fragments;

    private float containerWidth;

    private ParallaxPagerAdapter adapter;

    public ParallaxContainer(Context context) {
        this(context, null);
    }

    public ParallaxContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParallaxContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 指定引导页的所有页面布局文件
     *
     * @param childIds
     */
    public void setUp(int... childIds) {
        //根据布局文件数组，初始化所有的Fragment
        fragments = new ArrayList<>();
        for (int i = 0; i < childIds.length; i++) {
            ParallaxFragment f = new ParallaxFragment();
            Bundle args = new Bundle();
            //页面索引
            args.putInt("index", i);
            //Fragment中需要加载的布局文件id
            args.putInt("layoutId", childIds[i]);
            f.setArguments(args);
            fragments.add(f);
        }
        //实例化适配器
        MainActivity activity = (MainActivity) getContext();

        adapter = new ParallaxPagerAdapter(activity.getSupportFragmentManager(), fragments);
        //实例化ViewPager

        ViewPager vp = new ViewPager(getContext());

        vp.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        vp.setId(R.id.parallax_pager);
        //绑定
        vp.setAdapter(adapter);

        addView(vp, 0);
        //在翻页的过程中，不断根据视图的标签中对应的动画参数，改变视图的位置或者透明度
        vp.setOnPageChangeListener(this);


    }

    ImageView iv;

    public void setImage(ImageView iv) {
        this.iv = iv;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        containerWidth = getWidth();


        //在翻页的过程中，不断根据视图的标签中对应的动画参数，改变视图的位置或者透明度
        //获取到进入的页面
        ParallaxFragment inFragment = null;
        try {
            inFragment = fragments.get(position - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //获取到退出的页面
        ParallaxFragment outFragment = null;
        try {
            outFragment = fragments.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (inFragment != null) {
            //获取Fragment上所有的视图，实现动画效果
            List<View> inViews = inFragment.getParallaxViews();
            if (inViews != null) {
                for (View view : inViews) {
                    //获取标签，从标签上获取所有的动画参数
                    ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view_tag);

                    if (tag == null) {
                        continue;
                    }

                    Log.e("TAG","tag.xIn:" + tag.xIn);


                    // slide in from right
                    ViewHelper.setTranslationX(view, (containerWidth - positionOffsetPixels) * tag.xIn);

                    // slide in from top
                    ViewHelper.setTranslationY(view, (containerWidth - positionOffsetPixels) * tag.yIn);

                }
            }
        }

        if (outFragment != null) {
            //获取Fragment上所有的视图，实现动画效果
            List<View> outViews = outFragment.getParallaxViews();
            if (outViews != null) {
                for (View view : outViews) {
                    //获取标签，从标签上获取所有的动画参数
                    ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view_tag);

                    if (tag == null) {
                        continue;
                    }
                    // slide out to left
                    ViewHelper.setTranslationX(view, 0 - positionOffsetPixels * tag.xOut);

                    // slide out to top
                    ViewHelper.setTranslationY(view, 0 - positionOffsetPixels * tag.yOut);

                }
            }
        }

    }

    @Override
    public void onPageSelected(int position) {

        if (position == adapter.getCount() - 1) {
            iv.setVisibility(INVISIBLE);
        } else {
            iv.setVisibility(VISIBLE);

        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

        final AnimationDrawable animationDrawable = (AnimationDrawable) iv.getBackground();
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                animationDrawable.stop();
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:

                animationDrawable.start();
                break;
           default:
                break;
        }
    }
}
