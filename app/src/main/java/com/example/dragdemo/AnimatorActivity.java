package com.example.dragdemo;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AnimatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn01, btn02, btn03, btn04, btn05, btn07, btn08, btn09, btn10, btn11, btn12, btn13, btn14;
    private TextView tv01;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);
        initView();
    }

    private void initView() {
        btn01 = findViewById(R.id.btn01);
        btn02 = findViewById(R.id.btn02);
        btn03 = findViewById(R.id.btn03);
        btn04 = findViewById(R.id.btn04);
        btn05 = findViewById(R.id.btn05);
        btn07 = findViewById(R.id.btn07);
        btn08 = findViewById(R.id.btn08);
        btn09 = findViewById(R.id.btn09);
        btn10 = findViewById(R.id.btn10);
        btn11 = findViewById(R.id.btn11);
        btn12 = findViewById(R.id.btn12);
        btn13 = findViewById(R.id.btn13);
        btn14 = findViewById(R.id.btn14);
        ll = findViewById(R.id.ll);
        tv01 = findViewById(R.id.tv01);
        btn01.setOnClickListener(this);
        btn02.setOnClickListener(this);
        btn03.setOnClickListener(this);
        btn04.setOnClickListener(this);
        btn05.setOnClickListener(this);
        btn07.setOnClickListener(this);
        btn08.setOnClickListener(this);
        btn09.setOnClickListener(this);
        btn10.setOnClickListener(this);
        btn11.setOnClickListener(this);
        btn12.setOnClickListener(this);
        btn13.setOnClickListener(this);
        btn14.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn01://补间Tween/视图动画==透明
                //===代码实现
                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(2000);//默认0
                alphaAnimation.setInterpolator(new LinearInterpolator());//线性匀速插值器
                alphaAnimation.setRepeatCount(0);//动画重复次数 n+1 默认0,ValueAnimator.INFINITE=-1,INFINITE无线循环
                alphaAnimation.setRepeatMode(ValueAnimator.RESTART);//RESTART连续重复==1,REVERSE逆向重复==2
                tv01.startAnimation(alphaAnimation);//===对应的清除动画代码 tv01.clearAnimation(); 所以在 Activity的onDestroy中记得清除不然用以内存泄漏
                //===xml实现 ==res/anim/
//                Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha_ani);
//                tv01.startAnimation(animation);
                break;
            case R.id.btn02://补间==旋转
                //===代码,  参数1，2表旋转角度；参数3，4标识旋转中心坐标
//                RotateAnimation rotateAnimation = new RotateAnimation(0, 360, tv01.getWidth() / 2, tv01.getHeight() / 2);
//                rotateAnimation.setDuration(2000);
//                rotateAnimation.setInterpolator(new LinearInterpolator());
//                tv01.startAnimation(rotateAnimation);
                //===xml
                Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.rotate_ani);
                tv01.startAnimation(animation1);
                break;
            case R.id.btn03://补间==缩放
                //===代码
                ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, tv01.getWidth() / 2, tv01.getHeight() / 2);
                scaleAnimation.setDuration(2000);
                tv01.startAnimation(scaleAnimation);
                //===xml
                break;
            case R.id.btn04://补间==位移
                //===代码
                TranslateAnimation translateAnimation = new TranslateAnimation(0, 300, 0, 300);
                translateAnimation.setDuration(2000);
                translateAnimation.setFillAfter(true);//动画后view是否停留在结束位置
//                translateAnimation.setFillBefore(true);//动画后view是否还原到开始位置
//                translateAnimation.setFillEnabled(true);//动画后view是否还原到开始位置
                translateAnimation.setStartOffset(1000);//开始动画之前等待的时间
                tv01.startAnimation(translateAnimation);

                tv01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //验证了补间动画只是View映射/影像，并未改变View的真实布局属性值==解决方法用：属性动画Android3.0推出=修改控件的属性值实现的动画
                        Toast.makeText(AnimatorActivity.this, "View的位置", Toast.LENGTH_SHORT).show();
                    }
                });
                //===xml
                break;
            case R.id.btn05://补间==AnimationSet
                AlphaAnimation alphaAnimation1 = new AlphaAnimation(0, 1);
                alphaAnimation1.setDuration(2000);//默认0

                RotateAnimation rotateAnimation = new RotateAnimation(0, 360, tv01.getWidth() / 2, tv01.getHeight() / 2);
                rotateAnimation.setDuration(2000);

                ScaleAnimation scaleAnimation1 = new ScaleAnimation(0, 1, 0, 1, tv01.getWidth() / 2, tv01.getHeight() / 2);
                scaleAnimation1.setDuration(2000);

                TranslateAnimation translateAnimation1 = new TranslateAnimation(0, 300, 0, 300);
                translateAnimation1.setDuration(2000);

                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(alphaAnimation1);
                animationSet.addAnimation(rotateAnimation);
                animationSet.addAnimation(scaleAnimation1);
                animationSet.addAnimation(translateAnimation1);

//                animationSet.setDuration(8000);
                tv01.startAnimation(animationSet);
                break;

            //补间动画还可以实现如下，
            // 1、PopupWindow淡入弹出动画==mImgPopupWindow.setAnimationStyle(R.style.popup_animation_style);
            //2、LayoutAnimation属性+标签实现布局动画==为ViewGroup指定 android:layoutAnimation = "@anim/layout_anim" 或者代码也可实现
            //3、Activity启动动画

            /**
             * 属性动画Android3.0 = API 11加入的，可用nineoldandroids兼容低版本来使用属性动画
             * Animator --> AnimatorSet + ValueAnimator + StateListAnimator
             * ValueAnimator --> ObjectAnimator + TimeAnimator
             */
            case R.id.btn07://属性动画==ValueAnimator.ofFloat==缩放
                //===代码
//                ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f, 2f);
//                valueAnimator.setDuration(2000);
//                valueAnimator.setRepeatCount(1);//n+1，ValueAnimator.Infinite无线循环
//                valueAnimator.setRepeatMode(ValueAnimator.REVERSE);//循环模式,顺序/逆向
//                valueAnimator.setStartDelay(1000);//延迟动画播放时间
//                valueAnimator.start();
//
//                //监听动画更新
//                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//
//                        float currentValue = (float) animation.getAnimatedValue();
//                        Log.e("---MainActivity", "currentValue = " + currentValue);
//
//                        //手动修改缩放参数
//                        tv01.setScaleX(currentValue);
//                        tv01.setScaleY(currentValue);
//
//                        //重绘view==相当于执行了view的onDraw方法
//                        tv01.requestLayout();
//                    }
//                });

                //===xml
                ValueAnimator animator = (ValueAnimator) AnimatorInflater.loadAnimator(this, R.animator.set_animator);
                animator.setTarget(tv01);
                animator.start();

                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float currentValue = (float) animation.getAnimatedValue();
                        tv01.setScaleX(currentValue);
                        tv01.setScaleY(currentValue);
                        tv01.requestLayout();
                    }
                });

                break;
            case R.id.btn08://属性动画==ValueAnimator.ofInt
                ValueAnimator valueAnimator1 = ValueAnimator.ofInt(0, 200, 0, 200);
                valueAnimator1.setDuration(2000);
                valueAnimator1.start();
                valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int currentValue = (int) animation.getAnimatedValue();
                        Log.e("---MainActivity", "currentValue = " + currentValue);
                        tv01.setTranslationX(currentValue);
                        tv01.setTranslationY(currentValue);
                        tv01.requestLayout();
                    }
                });
                break;
            case R.id.btn09://属性动画==ObjectAnimator 是ValueAnimator的子类
                /**
                 * 区别：
                 * ValueAnimator 通过监听属性值的变化addUpdateListener，并手动修改view的属性；
                 * ObjectAnimator 设置属性名称propertyName，自动修改view的属性；== "translationX" 相当于 tv01.setTranslationX();
                 */
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tv01, "translationX", tv01.getWidth());
//                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tv01, "translationY", -tv01.getHeight());
//                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tv01, "rotationX", 0,360);
//                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tv01, "rotationY", 0,360);
//                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tv01, "rotation", 0,360);
                objectAnimator.setDuration(2000);
                objectAnimator.start();

                tv01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //解决了补间动画的view影像问题
                        Toast.makeText(AnimatorActivity.this, "View的位置", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn10://属性动画==
                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(tv01, "translationX", tv01.getWidth() * 3);
                objectAnimator1.setDuration(2000);
                ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(tv01, "translationY", -tv01.getHeight());
                objectAnimator2.setDuration(2000);

                AnimatorSet animatorSet = new AnimatorSet();

                //2个动画有先后顺序 串行 执行
//                animatorSet.play(objectAnimator1).before(objectAnimator2);//效果等同于 animatorSet.play(objectAnimator2).after(objectAnimator1);
                //2个动画一起 并行 执行
//                animatorSet.play(objectAnimator1).with(objectAnimator2);
                //多个动画 串行
//                animatorSet.playSequentially(objectAnimator1,objectAnimator2);//依次添加动画，或者传入动画集合List<Animator>
                //多个动画 并行
                animatorSet.playTogether(objectAnimator1, objectAnimator2);//依次添加动画，或者传入动画集合List<Animator>

                animatorSet.start();
                break;
            case R.id.btn11://属性动画==ViewPropertyAnimator动画 == api 12 view添加了animate 方法
                //链式 串行
//                tv01.animate().setDuration(2000).translationX(tv01.getWidth()).alpha(0.5f).start();
                tv01.animate().setDuration(2000).translationX(tv01.getWidth()).translationXBy(tv01.getWidth() * 2).alpha(0.5f).start();
//                tv01.animate().setDuration(2000).translationXBy(tv01.getWidth()).alpha(0.5f).start();
                break;

            case R.id.btn12://属性动画==LayoutTransition布局动画
                /**
                 * 属性动画==LayoutTransition 布局动画
                 * xml: ViewGroup标签中只加 android:animateLayoutChanges="true" 属性，系统提供的默认ViewGroup的LayoutTransition动画，addView时会自带系统动画
                 * java：LayoutTransition
                 */
                //===代码 通过LayoutTransition类来自定义动画效果
                /**
                 *  LayoutTransition layoutTransition = new LayoutTransition();
                 *  layoutTransition.setAnimator(LayoutTransition.APPEARING, layoutTransition.getAnimator(LayoutTransition.APPEARING));
                 *  ll_root.setLayoutTransition(layoutTransition);//为根布局设置LayoutTransition
                 */
                //===xml
                Button btn = new Button(this);
                btn.setText("数据");
                ll.addView(btn);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ll.removeView(v);
                    }
                });
                break;

            case R.id.btn13://属性动画==StateListAnimator 给按钮添加点击效果 类似selector+shape
                /**
                 * 低版本按钮点击事件没有效果，5.0高版本SDk默认添加了水波纹的点击效果
                 * StateListAnimator 可以让按钮真正的 动 起来
                 * 给按钮设置 android:stateListAnimator="@animator/bg_select" 属性即可
                 * 实现按钮点击上下起伏的效果
                 */
                break;
            case R.id.btn14://按钮触摸反馈动画，5.0默认是水波纹
                /**
                 * 系统自带的Ripple 水波纹 有2钟效果
                 * 1、?android:attr/selectableItemBackground  有边界
                 * 2、?android:attr/selectableItemBackgroundBorderless  无边界 =要求API21以上 == 无边界的ripple会覆盖按钮原本的背景颜色
                 * 一、可在AppTheme该变 水波纹 颜色值 == <item name="android:colorControlHighlight">#B8BD8B</item> 要求API21以上
                 * 二、自定义 Ripple 波纹效果，drawable-v21下添加<ripple>为根标签的动画资源文件--》给按钮设为背景bg
                 */
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tv01.clearAnimation();//销毁Activity后，动画还是在一直运行，这样会造成内存泄露，所以要在onDestroy中清除view的动画
    }
}
