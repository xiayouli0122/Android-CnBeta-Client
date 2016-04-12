package com.yuri.cnbeta.view.widgets.AVLoadingIndicatorView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 2015/10/19.
 */
public class LineScaleIndicator extends BaseIndicatorController {

    public static final float SCALE=1.0f;
    public static final float START_SCALE=0.3f;

    float[] scaleYFloats=new float[]{START_SCALE,
            START_SCALE,
            SCALE,
            START_SCALE,
            START_SCALE};

    @Override
    public void draw(Canvas canvas, Paint paint) {
        float translateX=getWidth()/11;
        float translateY=getHeight()/2;
        for (int i = 0; i < 5; i++) {
            canvas.save();
            canvas.translate((2 + i * 2) * translateX - translateX / 2, translateY);
            canvas.scale(SCALE, scaleYFloats[i]);
            RectF rectF=new RectF(-translateX/2,-getHeight()/2.5f,translateX/2,getHeight()/2.5f);
            canvas.drawRoundRect(rectF, 5, 5, paint);
            canvas.restore();
        }
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators=new ArrayList<>();
        long[] delays=new long[]{0,250,500,250,0};
        for (int i = 0; i < 5; i++) {
            final int index=i;
            ValueAnimator scaleAnim= ValueAnimator.ofFloat(1,0.3f,1);
            scaleAnim.setDuration(900);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);
            scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleYFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            scaleAnim.start();
            animators.add(scaleAnim);
        }
        return animators;
    }

}
