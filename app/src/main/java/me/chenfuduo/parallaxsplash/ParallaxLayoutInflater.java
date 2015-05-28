package me.chenfuduo.parallaxsplash;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ParallaxLayoutInflater extends LayoutInflater {

    private ParallaxFragment fragment;

    protected ParallaxLayoutInflater(LayoutInflater original, Context newContext,ParallaxFragment fragment) {
        super(original, newContext);
        this.fragment = fragment;
        //重新设置布局加载器的工厂

        //工厂用于创建文件中所有的视图


        setFactory(new ParallaxFactory(this));

    }

    /**
     * Create a copy of the existing LayoutInflater object, with the copy
     * pointing to a different Context than the original.  This is used by
     * {@link ContextThemeWrapper} to create a new LayoutInflater to go along
     * with the new Context theme.
     *
     * @param newContext The new Context to associate with the new LayoutInflater.
     *                   May be the same as the original Context if desired.
     * @return Returns a brand spanking new LayoutInflater object associated with
     * the given Context.
     */
    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new ParallaxLayoutInflater(this, newContext,fragment);
    }

    class ParallaxFactory implements LayoutInflater.Factory {

        private LayoutInflater inflater;

        private final String[] sClassPrefix = {
                "android.widget.",
                "android.view."
        };

        public ParallaxFactory(LayoutInflater layoutInflater) {
            this.inflater = layoutInflater;
        }

        /**
         * 自定义视图创建的过程
         *
         * @param name
         * @param context
         * @param attrs
         * @return
         */
        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            //android.widget.TextView;  TextView的prefix就是android.widget
            //当然在xml文件譬如ImageView和TextView等等有prefix，而其他的可能没有
            //没有的情况：比如我们自定义View，然后我们拷贝全路径到xml文件中
            //这种情况下没有prefix
            //==============================
            //总结
            //1.自定义控件不需要前缀
            //2.系统视图需要加上前缀(主要是两个，一个是android.view一个是android.widget)

            View view = null;

            if (view == null) {
                view = createViewOrFailQuitely(name, context, attrs);
            }

            //实例化完成
            if (view != null) {
                //获取自定义属性，通过标签关联到视图上
                setViewTag(view, context, attrs);
                fragment.getParallaxViews().add(view);

            }

            return view;
        }

        private void setViewTag(View view, Context context, AttributeSet attrs) {
            //所有自定义的属性
            int[] attrIds = {
                    R.attr.a_in,
                    R.attr.a_out,
                    R.attr.x_in,
                    R.attr.x_out,
                    R.attr.y_in,
                    R.attr.y_out
            };

            //获取(这种方式是不是和前面我们获取自定义属性的方式不同！！！)
            TypedArray array = context.obtainStyledAttributes(attrs, attrIds);
            if (array != null && array.length() > 0) {
                //获取自定义属性的值
                ParallaxViewTag tag = new ParallaxViewTag();

                tag.alphaIn = array.getFloat(0, 0f);
                tag.alphaOut = array.getFloat(1, 0f);
                tag.xIn = array.getFloat(2, 0f);
                tag.xOut = array.getFloat(3, 0f);
                tag.yIn = array.getFloat(4, 0f);
                tag.yOut = array.getFloat(5, 0f);
               // tag.index
                view.setTag(R.id.parallax_view_tag,tag);
            }

            array.recycle();

        }

        private View createViewOrFailQuitely(String name, String prefix, Context context, AttributeSet attrs) {

            try {
                return inflater.createView(name, prefix, attrs);
            } catch (Exception e) {
                return null;
            }

        }

        private View createViewOrFailQuitely(String name, Context context, AttributeSet attrs) {
            //自定义控件不需要前缀
            if (name.contains(".")) {
                createViewOrFailQuitely(name, null, context, attrs);
            }
            //系统视图需要加上前缀
            for (String prefix : sClassPrefix) {
                View view = createViewOrFailQuitely(name, prefix, context, attrs);
                if (view != null) {
                    return view;
                }
            }
            return null;
        }
    }

}
