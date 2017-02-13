package com.yinsidh.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yinsidh.bean.ContactBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 快速索引
 */
public class QuickIndexBar extends View {
    private int prePosition = -1;
    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    // 26个字母
    public static String[] b = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private int choose = -1;// 选中
    private List<ContactBean> list;

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        list = new ArrayList<ContactBean>();

    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        list = new ArrayList<ContactBean>();
    }

    public QuickIndexBar(Context context) {
        super(context);
        list = new ArrayList<ContactBean>();
    }

    /**
     * 接口
     *
     * @author coder
     */
    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }

    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener, List<ContactBean> list) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
        this.list = list;


    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        int singleTextHeight = viewHeight / b.length;
        for (int i = 0; i < b.length; i++) {
            if (prePosition == i) {
                paint.setColor(Color.RED);
            } else {
                paint.setColor(Color.parseColor("#48B0ED"));
            }
            int textWidth = (int) paint.measureText(b[i]);
            canvas.drawText(b[i], (viewWidth - textWidth) / 2, singleTextHeight * (i + 1), paint);

        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float y = event.getY();
        int singleTextHeight = getHeight() / b.length;
        int position = (int) (y / singleTextHeight);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.TRANSPARENT);
                break;
            default:
                setBackgroundColor(Color.parseColor("#CCCCCC"));
                Log.e("tag", "Bar");
                Log.e("tag", "Barlist" + list.size());
                if (position >= 0 && position < b.length) {
                    if (this.list.size() > 0) {
                        onTouchingLetterChangedListener.onTouchingLetterChanged(b[position]);
                    } else {
                        //无操作
                    }
                }
                prePosition = position;
                break;
        }
        invalidate();
        return true;

    }
}

