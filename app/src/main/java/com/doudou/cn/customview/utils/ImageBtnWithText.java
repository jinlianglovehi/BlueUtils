package com.doudou.cn.customview.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.doudou.cn.customview.R;

/**
 * Created by jinliang on 15/11/15.
 */
public class ImageBtnWithText extends LinearLayout {
    public ImageBtnWithText(Context context) {
        this(context, null);
    }

    public ImageBtnWithText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageBtnWithText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.image_btn_layout, this, true);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageBtnWithText);
//        CharSequence text = a.getText(R.styleable.ImageBtnWithText_android_text);
//        if (text != null) mTv.setText(text);
//        Drawable drawable = a.getDrawable(R.styleable.ImageBtnWithText_android_src);
//        if (drawable != null) mBtn.setImageDrawable(drawable);
//        a.recycle();
        //initUI( context);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
