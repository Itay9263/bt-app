package com.syu.ctrl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import com.syu.app.MyApplication;
import com.syu.app.MyUi;
import com.syu.page.IPageNotify;
import com.syu.util.FuncUtils;

public class JSeekBar extends View {
    public boolean bCanSeekAble = true;
    public boolean bClickDown = false;
    public boolean bDrawMidTxt = false;
    private Bitmap bmp_seekbar;
    private Bitmap bmp_seekbar_focus;
    private Bitmap bmp_thumb;
    private Bitmap bmp_txtBK;
    Drawable drawable_seekbar;
    Drawable drawable_seekbar_focus;
    Drawable drawable_thumb;
    Drawable drawable_txtBk;
    private Paint.FontMetricsInt fontMetrics;
    public int hExtraTxt = 0;
    public int hExtraTxtBk = 0;
    public int hLine = 5;
    private long mDrawingTime;
    public int mMax = 1000000;
    public int mMin = -1;
    public int mMinDisp = 0;
    public int mPosMin = 0;
    public int mPosStart = 0;
    public int mPosTarget = 0;
    public int mProgress = 0;
    private float mScaleX = 1.0f;
    private float mScaleY = 1.0f;
    public float mStep = 0.0f;
    private float mTextSize = 18.0f;
    public int[] mValDisp;
    public int mValue = 0;
    private boolean mVertical = false;
    private JPage page;
    private Paint paint;
    public Rect rec_seekBar;
    public Rect rec_thumb;
    public Rect rec_txtMax;
    public Rect rec_txtMin;
    public int szThumb = 0;
    private MyUi ui;

    public JSeekBar(Context context, JPage jPage, MyUi myUi) {
        super(context);
        this.page = jPage;
        this.ui = myUi;
        calcStep();
        init();
    }

    public void calcStep() {
        if (this.rec_seekBar != null) {
            this.mStep = (((float) ((this.rec_seekBar.right - this.rec_seekBar.left) - this.szThumb)) * 1.0f) / ((float) this.mMax);
        }
    }

    public void calcValue(int i, int i2) {
        if (this.mVertical) {
            this.mValue = (int) (((double) (((float) ((this.rec_seekBar.bottom - this.rec_seekBar.top) - (i2 - this.rec_seekBar.top))) / this.mStep)) + 0.5d);
        } else {
            this.mValue = (int) (((double) (((float) (i - this.rec_seekBar.left)) / this.mStep)) + 0.5d);
        }
        if (this.mValue > this.mMax) {
            this.mValue = this.mMax;
        }
        if (this.mValue < 0) {
            this.mValue = 0;
        }
        setProgress(this.mValue);
    }

    public Bitmap getBitmapFromDrawable(Drawable drawable, float f, float f2) {
        if (drawable == null) {
            return null;
        }
        return FuncUtils.getBitmapFromDrawable(drawable, (int) (((float) drawable.getIntrinsicWidth()) * f), (int) (((float) drawable.getIntrinsicHeight()) * f2));
    }

    public int getValue() {
        return this.mValue;
    }

    public void init() {
        this.paint = new Paint();
        this.paint.setSubpixelText(true);
        this.paint.setAntiAlias(true);
        this.paint.setFilterBitmap(true);
        this.paint.setTextAlign(Paint.Align.CENTER);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.bmp_seekbar != null) {
            if (this.mMin >= 0) {
                this.mPosStart = this.mPosTarget;
            }
            if (this.mPosStart != this.mPosTarget) {
                long drawingTime = getDrawingTime();
                if (this.mDrawingTime == 0) {
                    this.mDrawingTime = drawingTime;
                }
                int i = this.mVertical ? (int) (((drawingTime - this.mDrawingTime) * ((long) ((this.rec_seekBar.bottom - this.rec_seekBar.top) - this.szThumb))) / 300) : (int) (((drawingTime - this.mDrawingTime) * ((long) ((this.rec_seekBar.right - this.rec_seekBar.left) - this.szThumb))) / 300);
                this.mDrawingTime = drawingTime;
                if (Math.abs(this.mPosTarget - this.mPosStart) < i) {
                    this.mPosStart = this.mPosTarget;
                } else if (this.mPosTarget > this.mPosStart) {
                    this.mPosStart = i + this.mPosStart;
                } else {
                    this.mPosStart -= i;
                }
                invalidate();
            } else {
                this.mDrawingTime = 0;
            }
            if (!(this.rec_txtMin == null || this.rec_txtMax == null)) {
                int i2 = (this.rec_txtMin.top + ((((this.rec_txtMin.bottom - this.rec_txtMin.top) - this.fontMetrics.bottom) + this.fontMetrics.top) / 2)) - this.fontMetrics.top;
                if (this.mValDisp == null || this.mValDisp.length <= 0) {
                    if (!this.mVertical) {
                        canvas.drawText(new StringBuilder().append(this.mMinDisp).toString(), (this.paint.measureText(new StringBuilder().append(this.mMinDisp).toString()) / 2.0f) + ((float) this.rec_txtMin.left) + 5.0f, (float) i2, this.paint);
                    } else {
                        canvas.drawText(new StringBuilder().append(this.mMinDisp).toString(), (float) this.rec_txtMin.centerX(), (float) i2, this.paint);
                    }
                    int i3 = (this.rec_txtMax.top + ((((this.rec_txtMax.bottom - this.rec_txtMax.top) - this.fontMetrics.bottom) + this.fontMetrics.top) / 2)) - this.fontMetrics.top;
                    if (!this.mVertical) {
                        canvas.drawText(new StringBuilder().append(this.mMax + this.mMinDisp).toString(), (((float) this.rec_txtMax.right) - (this.paint.measureText(new StringBuilder().append(this.mMax + this.mMinDisp).toString()) / 2.0f)) - 5.0f, (float) i3, this.paint);
                        if (this.bDrawMidTxt) {
                            canvas.drawText(new StringBuilder().append((this.mMax / 2) + this.mMinDisp).toString(), ((float) ((this.rec_txtMin.right + this.rec_txtMax.right) / 2)) - (this.paint.measureText(new StringBuilder().append((this.mMax / 2) + this.mMinDisp).toString()) / 2.0f), (float) i3, this.paint);
                        }
                    } else {
                        canvas.drawText(new StringBuilder().append(this.mMax + this.mMinDisp).toString(), (float) this.rec_txtMax.centerX(), (float) i3, this.paint);
                        if (this.bDrawMidTxt) {
                            canvas.drawText(new StringBuilder().append((this.mMax / 2) + this.mMinDisp).toString(), (float) this.rec_txtMax.centerX(), (float) ((((this.rec_txtMax.top + this.rec_txtMin.top) / 2) + ((((this.rec_txtMax.bottom - this.rec_txtMax.top) - this.fontMetrics.bottom) + this.fontMetrics.top) / 2)) - this.fontMetrics.top), this.paint);
                        }
                    }
                } else {
                    for (int i4 : this.mValDisp) {
                        canvas.drawText(new StringBuilder().append(this.mMinDisp + i4).toString(), (float) (this.rec_seekBar.left + ((int) (((float) i4) * this.mStep)) + (this.szThumb / 2)), (float) i2, this.paint);
                        int i5 = ((int) (((float) i4) * this.mStep)) + this.rec_seekBar.left + (this.szThumb / 2);
                        int i6 = this.rec_txtMin.bottom;
                        canvas.drawRect((float) i5, (float) i6, (float) (i5 + 2), (float) (this.hLine + i6), this.paint);
                    }
                }
            }
            canvas.save();
            canvas.clipRect(this.rec_seekBar.left, this.rec_seekBar.top, this.rec_seekBar.right, this.rec_seekBar.bottom);
            canvas.drawBitmap(this.bmp_seekbar, (float) this.rec_seekBar.left, (float) this.rec_seekBar.top, this.paint);
            canvas.restore();
            canvas.save();
            if (this.mVertical) {
                if (this.mMin >= 0) {
                    int i7 = this.mPosMin;
                    int i8 = this.mPosStart;
                    if (i7 > i8) {
                        i7 = this.mPosStart;
                        i8 = this.mPosMin;
                    }
                    canvas.clipRect(this.rec_seekBar.left, (this.rec_seekBar.bottom - i8) - (this.szThumb / 2), this.rec_seekBar.right, (this.rec_seekBar.bottom - i7) - (this.szThumb / 2));
                } else {
                    canvas.clipRect(this.rec_seekBar.left, (this.rec_seekBar.bottom - this.mPosStart) - (this.szThumb / 2), this.rec_seekBar.right, this.rec_seekBar.bottom);
                }
            } else if (this.mMin >= 0) {
                int i9 = this.mPosMin;
                int i10 = this.mPosStart;
                if (i9 > i10) {
                    i9 = this.mPosStart;
                    i10 = this.mPosMin;
                }
                canvas.clipRect(i9 + this.rec_seekBar.left + (this.szThumb / 2), this.rec_seekBar.top, i10 + this.rec_seekBar.left + (this.szThumb / 2), this.rec_seekBar.bottom);
            } else {
                canvas.clipRect(this.rec_seekBar.left, this.rec_seekBar.top, this.rec_seekBar.left + this.mPosStart + (this.szThumb / 2), this.rec_seekBar.bottom);
            }
            canvas.drawBitmap(this.bmp_seekbar_focus, (float) this.rec_seekBar.left, (float) this.rec_seekBar.top, this.paint);
            canvas.restore();
            canvas.save();
            if (this.mVertical) {
                if (this.bmp_thumb != null) {
                    canvas.drawBitmap(this.bmp_thumb, (float) this.rec_thumb.left, (float) ((this.rec_seekBar.bottom - this.mPosStart) - (this.rec_thumb.bottom - this.rec_thumb.top)), this.paint);
                    if (this.mMin >= 0) {
                        canvas.drawBitmap(this.bmp_thumb, (float) this.rec_thumb.left, (float) ((this.rec_seekBar.bottom - this.mPosMin) - (this.rec_thumb.bottom - this.rec_thumb.top)), this.paint);
                    }
                }
                if (this.rec_txtMin != null) {
                    int i11 = (this.rec_seekBar.bottom - this.mPosStart) - (((this.szThumb + this.rec_txtMin.bottom) - this.rec_txtMin.top) / 2);
                    if (this.bmp_txtBK != null) {
                        canvas.drawBitmap(this.bmp_txtBK, (float) this.rec_txtMin.left, (float) i11, this.paint);
                    }
                    canvas.drawText(new StringBuilder().append(this.mProgress + this.mMinDisp).toString(), (float) this.rec_txtMin.centerX(), (float) ((i11 + ((((this.rec_txtMin.bottom - this.rec_txtMin.top) - this.fontMetrics.bottom) + this.fontMetrics.top) / 2)) - this.fontMetrics.top), this.paint);
                    if (this.mMin >= 0) {
                        canvas.drawText(new StringBuilder().append(this.mMin + this.mMinDisp).toString(), (float) this.rec_txtMin.centerX(), (float) ((((this.rec_seekBar.bottom - this.mPosMin) - (((this.szThumb + this.rec_txtMin.bottom) - this.rec_txtMin.top) / 2)) + ((((this.rec_txtMin.bottom - this.rec_txtMin.top) - this.fontMetrics.bottom) + this.fontMetrics.top) / 2)) - this.fontMetrics.top), this.paint);
                    }
                }
            } else {
                if (this.bmp_thumb != null) {
                    canvas.drawBitmap(this.bmp_thumb, (float) (this.rec_seekBar.left + this.mPosStart), (float) this.rec_thumb.top, this.paint);
                    if (this.mMin >= 0) {
                        canvas.drawBitmap(this.bmp_thumb, (float) (this.rec_seekBar.left + this.mPosMin), (float) this.rec_thumb.top, this.paint);
                    }
                }
                if (this.rec_txtMin != null) {
                    if (this.bmp_txtBK != null) {
                        canvas.drawBitmap(this.bmp_txtBK, (float) (this.rec_seekBar.left + this.mPosStart + ((this.szThumb - this.rec_txtMin.width()) / 2)), (float) (this.rec_txtMin.top + this.hExtraTxtBk), this.paint);
                        if (this.mMin >= 0) {
                            canvas.drawBitmap(this.bmp_txtBK, (float) (this.rec_seekBar.left + this.mPosMin + ((this.szThumb - this.rec_txtMin.width()) / 2)), (float) (this.rec_txtMin.top + this.hExtraTxtBk), this.paint);
                        }
                    }
                    int i12 = ((this.rec_txtMin.top + this.hExtraTxt) + ((((this.rec_txtMin.bottom - this.rec_txtMin.top) - this.fontMetrics.bottom) + this.fontMetrics.top) / 2)) - this.fontMetrics.top;
                    canvas.drawText(new StringBuilder().append(this.mProgress + this.mMinDisp).toString(), (float) (this.rec_seekBar.left + this.mPosStart + (this.szThumb / 2)), (float) i12, this.paint);
                    if (this.mMin >= 0) {
                        canvas.drawText(new StringBuilder().append(this.mMin + this.mMinDisp).toString(), (float) (this.rec_seekBar.left + this.mPosMin + (this.szThumb / 2)), (float) i12, this.paint);
                    }
                }
            }
            canvas.restore();
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        if (i > 0 && i2 > 0) {
            super.onSizeChanged(i, i2, i3, i4);
            if (this.drawable_seekbar != null) {
                if (this.drawable_seekbar.getIntrinsicWidth() < this.drawable_seekbar.getIntrinsicHeight()) {
                    this.mVertical = true;
                }
                this.mScaleX = (((float) (this.rec_seekBar.right - this.rec_seekBar.left)) * 1.0f) / ((float) this.drawable_seekbar.getIntrinsicWidth());
                this.mScaleY = (((float) (this.rec_seekBar.bottom - this.rec_seekBar.top)) * 1.0f) / ((float) this.drawable_seekbar.getIntrinsicHeight());
                this.bmp_seekbar = getBitmapFromDrawable(this.drawable_seekbar, this.mScaleX, this.mScaleY);
                this.bmp_seekbar_focus = getBitmapFromDrawable(this.drawable_seekbar_focus, this.mScaleX, this.mScaleY);
                this.bmp_thumb = getBitmapFromDrawable(this.drawable_thumb, MyApplication.mScale, MyApplication.mScale);
                this.bmp_txtBK = getBitmapFromDrawable(this.drawable_txtBk, MyApplication.mScale, MyApplication.mScale);
                if (this.mVertical) {
                    if (this.bmp_thumb != null) {
                        this.szThumb = this.bmp_thumb.getHeight();
                    }
                    this.mStep = (((float) ((this.rec_seekBar.bottom - this.rec_seekBar.top) - this.szThumb)) * 1.0f) / ((float) this.mMax);
                } else {
                    if (this.bmp_thumb != null) {
                        this.szThumb = this.bmp_thumb.getWidth();
                    }
                    this.mStep = (((float) ((this.rec_seekBar.right - this.rec_seekBar.left) - this.szThumb)) * 1.0f) / ((float) this.mMax);
                }
                this.mPosTarget = (int) (((float) this.mProgress) * this.mStep);
                this.mPosStart = this.mPosTarget;
                this.mTextSize *= this.mScaleY;
                this.paint.setTextSize(this.mTextSize);
                this.fontMetrics = this.paint.getFontMetricsInt();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.bCanSeekAble) {
            return super.onTouchEvent(motionEvent);
        }
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        switch (motionEvent.getAction()) {
            case 0:
                getParent().requestDisallowInterceptTouchEvent(true);
                this.bClickDown = true;
                calcValue(x, y);
                IPageNotify notify = this.page.getNotify();
                if (notify == null) {
                    return true;
                }
                notify.ResponseClick(this);
                return true;
            case 1:
            case 3:
                this.bClickDown = false;
                calcValue(x, y);
                IPageNotify notify2 = this.page.getNotify();
                if (notify2 == null) {
                    return true;
                }
                notify2.ResponseClick(this);
                return true;
            case 2:
                if (!this.bClickDown) {
                    return true;
                }
                calcValue(x, y);
                IPageNotify notify3 = this.page.getNotify();
                if (notify3 == null) {
                    return true;
                }
                notify3.ResponseClick(this);
                return true;
            default:
                return true;
        }
    }

    public void setIconName(String[] strArr) {
        if (strArr != null) {
            this.drawable_seekbar = this.ui.getDrawableFromPath(strArr[0]);
            this.drawable_seekbar_focus = this.ui.getDrawableFromPath(strArr[1]);
            if (strArr.length > 2) {
                this.drawable_thumb = this.ui.getDrawableFromPath(strArr[2]);
            }
            if (strArr.length > 3) {
                this.drawable_txtBk = this.ui.getDrawableFromPath(strArr[3]);
            }
        }
    }

    public void setProgress(int i) {
        int i2 = this.mPosTarget;
        if (i < 0) {
            i2 = 0;
            i = 0;
        }
        if (i > this.mMax) {
            i = this.mMax;
        }
        this.mProgress = i;
        if (this.mStep >= 0.0f) {
            i2 = (int) (((float) i) * this.mStep);
        }
        if (this.mPosTarget != i2) {
            this.mPosTarget = i2;
            invalidate();
        }
    }

    public void setProgressMax(int i) {
        if (i > 0) {
            this.mMax = i;
            calcStep();
        }
    }

    public void setProgressMin(int i) {
        this.mMin = i;
        int i2 = (int) (((float) i) * this.mStep);
        if (this.mPosMin != i2) {
            this.mPosMin = i2;
            invalidate();
        }
    }

    public void setProgressMinDisp(int i) {
        this.mMinDisp = i;
    }

    public void setTextColor(int i) {
        this.paint.setColor(i);
    }

    public void setTextSize(float f) {
        this.mTextSize = f;
    }
}
