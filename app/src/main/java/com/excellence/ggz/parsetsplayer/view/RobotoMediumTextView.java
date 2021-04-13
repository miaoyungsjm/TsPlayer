package com.excellence.ggz.parsetsplayer.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * RobotoMediumTextView
 *
 * @author ggz
 * @date 2018/4/3
 */
public class RobotoMediumTextView extends AppCompatTextView {
    private static final String TYPEFACE_ROBOTO = "Roboto-Medium.ttf";

    public RobotoMediumTextView(Context context) {
        super(context);
        init(context);
    }

    public RobotoMediumTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RobotoMediumTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface typeface = Typeface.createFromAsset(
                context.getAssets(), TYPEFACE_ROBOTO);
        setTypeface(typeface);
    }
}
