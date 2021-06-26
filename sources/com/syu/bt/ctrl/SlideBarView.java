package com.syu.bt.ctrl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.syu.bt.Bt_Info;

public class SlideBarView extends View {
    private int choose = -1;
    private TextView mTextDialog;
    private OnTouchLettersChangedListener onTouchLettersChangedListener;
    private Paint paint = new Paint();
    public Rect rec_Letterbar;

    public interface OnTouchLettersChangedListener {
        void onTouchLettersChanged(String str);
    }

    public SlideBarView(Context context) {
        super(context);
    }

    public SlideBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SlideBarView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int length = height / Bt_Info.b.length;
        for (int i = 0; i < Bt_Info.b.length; i++) {
            this.paint.setColor(Color.parseColor("#474747"));
            this.paint.setAntiAlias(true);
            this.paint.setTextSize(22.0f);
            if (i == this.choose) {
                this.paint.setColor(Color.parseColor("#0308ff"));
                this.paint.setFakeBoldText(true);
            }
            canvas.drawText(Bt_Info.b[i], ((float) (width / 2)) - (this.paint.measureText(Bt_Info.b[i]) / 2.0f), (float) ((length * i) + length), this.paint);
            this.paint.reset();
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        float y = motionEvent.getY();
        int i = this.choose;
        OnTouchLettersChangedListener onTouchLettersChangedListener2 = this.onTouchLettersChangedListener;
        int height = (int) ((y / ((float) getHeight())) * ((float) Bt_Info.b.length));
        switch (action) {
            case 1:
                this.choose = -1;
                invalidate();
                if (this.mTextDialog == null) {
                    return true;
                }
                this.mTextDialog.setVisibility(4);
                return true;
            default:
                if (i == height || height >= Bt_Info.b.length || height < 0) {
                    return true;
                }
                if (onTouchLettersChangedListener2 != null) {
                    onTouchLettersChangedListener2.onTouchLettersChanged(Bt_Info.b[height]);
                }
                if (this.mTextDialog != null) {
                    this.mTextDialog.setBackgroundColor(Color.parseColor("#0000ff"));
                    this.mTextDialog.setText(Bt_Info.b[height]);
                    this.mTextDialog.setVisibility(0);
                }
                this.choose = height;
                invalidate();
                return true;
        }
    }

    public void setOnTouchLettersChangedListener(OnTouchLettersChangedListener onTouchLettersChangedListener2) {
        this.onTouchLettersChangedListener = onTouchLettersChangedListener2;
    }

    public void setTextView(TextView textView) {
        this.mTextDialog = textView;
    }
}
