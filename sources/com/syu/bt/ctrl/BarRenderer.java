package com.syu.bt.ctrl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import com.syu.app.App;
import java.util.Random;

public class BarRenderer {
    private static final int[][] virtrulFrq = {new int[]{20, 18, 24, 26, 28, 61, 76, 128, 139, 112, 80, 68, 70, 55, 48, 60, 30, 32, 36, 42, 38, 30, 32, 26, 15, 14, 22, 18, 28, 31, 46, 24, 17, 14, 10, 10, 10, 10, 18, 22, 16, 10, 10, 10, 14, 17, 12, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 14, 12, 18, 10, 10, 10}, new int[]{32, 45, 55, 60, 52, 76, 78, 110, 130, 118, 86, 70, 70, 66, 62, 60, 22, 18, 16, 26, 18, 22, 40, 26, 15, 14, 22, 18, 28, 31, 48, 24, 17, 14, 10, 10, 10, 10, 18, 22, 16, 22, 24, 20, 18, 17, 12, 14, 10, 10, 10, 18, 16, 14, 10, 10, 15, 16, 14, 12, 18, 20, 22, 20}, new int[]{45, 39, 36, 38, 48, 61, 76, 128, 139, 112, 72, 75, 70, 55, 48, 60, 22, 18, 16, 26, 18, 22, 40, 26, 15, 14, 22, 18, 28, 31, 38, 24, 17, 14, 16, 18, 24, 10, 18, 22, 16, 14, 12, 10, 14, 17, 12, 10, 10, 10, 14, 16, 18, 14, 10, 12, 14, 12, 10, 10, 18, 17, 20, 10}, new int[]{24, 22, 28, 30, 34, 48, 72, 130, 120, 116, 98, 92, 68, 45, 48, 60, 75, 74, 68, 44, 18, 22, 30, 26, 18, 24, 22, 24, 28, 31, 45, 24, 17, 14, 20, 22, 18, 19, 18, 22, 24, 26, 30, 32, 28, 24, 22, 14, 16, 12, 10, 10, 14, 16, 15, 12, 16, 18, 14, 12, 18, 10, 10, 10}};
    private static final int[][] virtrulFrq_2 = {new int[]{20, 18, 24, 26, 28, 61, 76, 128, 139, 112, 80, 68, 70, 55, 48, 60, 30, 32, 36, 42, 38, 30, 32, 26, 15, 14, 22, 18, 28, 31, 46, 24, 17, 14, 10, 10, 10, 10, 18, 22, 16, 10, 10, 10, 14, 17, 12, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 14, 12, 18, 10, 10, 10, 10, 10, 14, 12, 18, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 16, 10, 10, 10, 14, 17, 12, 10, 17, 14, 10, 10, 10, 10, 18, 22, 15, 14, 22, 18, 28, 31, 46, 24, 30, 32, 36, 42, 38, 30, 32, 26, 139, 112, 80, 68, 70, 55, 48, 60, 20, 18, 24, 26, 28, 61, 76, 128}, new int[]{32, 45, 55, 60, 52, 76, 78, 110, 130, 118, 86, 70, 70, 66, 62, 60, 22, 18, 16, 26, 18, 22, 40, 26, 15, 14, 22, 18, 28, 31, 48, 24, 17, 14, 10, 10, 10, 10, 18, 22, 16, 22, 24, 20, 18, 17, 12, 14, 10, 10, 10, 18, 16, 14, 10, 10, 15, 16, 14, 12, 18, 20, 22, 20, 15, 16, 14, 12, 18, 20, 22, 20, 10, 10, 10, 18, 16, 14, 10, 10, 16, 22, 24, 20, 18, 17, 12, 14, 17, 14, 10, 10, 10, 10, 18, 22, 15, 14, 22, 18, 28, 31, 48, 24, 22, 18, 16, 26, 18, 22, 40, 26, 130, 118, 86, 70, 70, 66, 62, 60, 32, 45, 55, 60, 52, 76, 78, 110}, new int[]{45, 39, 36, 38, 48, 61, 76, 128, 139, 112, 72, 75, 70, 55, 48, 60, 22, 18, 16, 26, 18, 22, 40, 26, 15, 14, 22, 18, 28, 31, 38, 24, 17, 14, 16, 18, 24, 10, 18, 22, 16, 14, 12, 10, 14, 17, 12, 10, 10, 10, 14, 16, 18, 14, 10, 12, 14, 12, 10, 10, 18, 17, 20, 10, 14, 12, 10, 10, 18, 17, 20, 10, 10, 10, 14, 16, 18, 14, 10, 12, 16, 14, 12, 10, 14, 17, 12, 10, 17, 14, 16, 18, 24, 10, 18, 22, 15, 14, 22, 18, 28, 31, 38, 24, 22, 18, 16, 26, 18, 22, 40, 26, 139, 112, 72, 75, 70, 55, 48, 60, 45, 39, 36, 38, 48, 61, 76, 128}, new int[]{24, 22, 28, 30, 34, 48, 72, 130, 120, 116, 98, 92, 68, 45, 48, 60, 75, 74, 68, 44, 18, 22, 30, 26, 18, 24, 22, 24, 28, 31, 45, 24, 17, 14, 20, 22, 18, 19, 18, 22, 24, 26, 30, 32, 28, 24, 22, 14, 16, 12, 10, 10, 14, 16, 15, 12, 16, 18, 14, 12, 18, 10, 10, 10, 16, 18, 14, 12, 18, 10, 10, 10, 16, 12, 10, 10, 14, 16, 15, 12, 24, 26, 30, 32, 28, 24, 22, 14, 17, 14, 20, 22, 18, 19, 18, 22, 18, 24, 22, 24, 28, 31, 45, 24, 75, 74, 68, 44, 18, 22, 30, 26, 120, 116, 98, 92, 68, 45, 48, 60, 24, 22, 28, 30, 34, 48, 72, 130}};
    private boolean autoColor = true;
    int b = 0;
    private float barWidth = 4.0f;
    private float colorCounter = 0.0f;
    private boolean enableLine = true;
    private int f_height = 3;
    int g = 64;
    private int iStyle = 0;
    private boolean isTop = false;
    private int lineCnt = 64;
    protected Paint linePaint = new Paint();
    private Bitmap mBmpBar;
    private Bitmap mBmpBarBk;
    private int mGap = 2;
    private float[] mLasteFft = null;
    private float[] mLasteLine = null;
    protected float[] mLinePoints = null;
    protected float[] mLinePointsDown = null;
    private Paint mPaint = null;
    protected float[] mPoints;
    private Random mRandom;
    private int p_height = 1;
    int r = 0;
    private float[] times = null;
    private int[] virtrulData;
    private float virtrulScale = 0.81764704f;

    public BarRenderer(float f, int i, int i2, int i3, boolean z, boolean z2, boolean z3, Bitmap bitmap, Bitmap bitmap2, int i4) {
        if (f > 0.0f) {
            this.barWidth = f;
        }
        if (i >= 0) {
            this.mGap = i;
        }
        if (i2 >= 0) {
            this.f_height = i2;
        }
        if (i3 >= 0) {
            this.p_height = i3;
        }
        this.enableLine = z;
        this.isTop = z2;
        this.autoColor = z3;
        this.mBmpBar = bitmap;
        this.mBmpBarBk = bitmap2;
        this.lineCnt = i4;
        init(-1);
    }

    private void autoColor() {
        this.r = (int) Math.floor((Math.sin((double) this.colorCounter) + 1.0d) * 128.0d);
        this.g = (int) Math.floor((Math.sin((double) (this.colorCounter + 2.0f)) + 1.0d) * 128.0d);
        this.b = (int) Math.floor((Math.sin((double) (this.colorCounter + 4.0f)) + 1.0d) * 128.0d);
        this.mPaint.setColor(Color.argb(100, this.r, this.g, this.b));
        if (this.enableLine) {
            this.r = this.r + this.g + this.b;
            if (this.r > 255) {
                this.r = 255;
            }
            this.linePaint.setColor(Color.argb(255, this.r, this.r, this.r));
        }
        this.colorCounter = (float) (((double) this.colorCounter) + 0.03d);
    }

    private void init(int i) {
        this.mPaint = new Paint();
        this.mPaint.setStrokeWidth(this.barWidth);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(i);
        this.linePaint.setStrokeWidth((float) this.f_height);
        this.linePaint.setAntiAlias(true);
        this.linePaint.setColor(i);
        this.mRandom = new Random();
    }

    public void onRender(Canvas canvas, float f, Rect rect) {
        int height = rect.height();
        if (this.lineCnt > 64) {
            this.virtrulData = virtrulFrq_2[this.mRandom.nextInt(virtrulFrq_2.length)];
        } else {
            this.virtrulData = virtrulFrq[this.mRandom.nextInt(virtrulFrq.length)];
        }
        int length = this.virtrulData.length;
        if (this.mLasteFft == null) {
            this.mLasteFft = new float[length];
        }
        if (this.mPoints == null || this.mPoints.length < length * 8) {
            this.mPoints = new float[(length * 8)];
        }
        if (this.times == null) {
            this.times = new float[length];
        }
        if (this.enableLine) {
            if (this.mLasteLine == null) {
                this.mLasteLine = new float[length];
            }
            if (this.mLinePoints == null) {
                this.mLinePoints = new float[(length * 8)];
            }
            if (this.iStyle > 0 && this.mLinePointsDown == null) {
                this.mLinePointsDown = new float[(length * 8)];
            }
        }
        float abs = f < 0.008f ? 0.0f : Math.abs(this.virtrulScale - f);
        for (int i = 0; i < length; i++) {
            float f2 = ((float) (this.mGap * i)) + ((((float) ((i * 2) + 1)) * this.barWidth) / 2.0f);
            this.mPoints[(i * 4) + 2] = f2;
            this.mPoints[i * 4] = f2;
            float round = (((float) this.virtrulData[i]) * f) + ((float) Math.round((Math.random() * ((double) ((((float) this.virtrulData[i]) * abs) - ((((float) (-this.virtrulData[i])) * abs) / 3.0f)))) + ((double) ((((float) (-this.virtrulData[i])) * abs) / 3.0f))));
            if (round < 0.0f) {
                round = 0.0f;
            }
            float f3 = ((((round * ((-0.003f * round) + 1.003f)) * 2.0f) * 1.5f) * ((float) height)) / 260.0f;
            if (((float) this.f_height) + f3 + ((float) this.p_height) >= ((float) (height - 10))) {
                f3 = (float) (((height - 10) - this.f_height) - this.p_height);
            }
            if (this.mLasteFft[i] <= 0.0f) {
                this.mLasteFft[i] = f3;
            } else if (this.mLasteFft[i] > f3) {
                this.mLasteFft[i] = (float) (((double) this.mLasteFft[i]) - ((2.4000000953674316d * Math.pow((double) this.times[i], 2.0d)) / 2.0d));
                this.times[i] = this.times[i] <= 0.0f ? 0.5f : this.times[i] + 0.5f;
            } else {
                this.times[i] = 0.0f;
                this.mLasteFft[i] = f3;
            }
            if (this.enableLine) {
                if (this.mLasteLine[i] <= 0.0f) {
                    this.mLasteLine[i] = f3 + 2.0f;
                } else if (this.mLasteLine[i] - 2.0f > f3) {
                    this.mLasteLine[i] = (float) (((double) this.mLasteLine[i]) - ((1.2000000476837158d * Math.pow((double) this.times[i], 2.0d)) / 2.0d));
                } else {
                    this.mLasteLine[i] = f3 + 2.0f;
                }
                this.mLinePoints[i * 4] = this.mPoints[i * 4] - (this.barWidth / 2.0f);
                this.mLinePoints[(i * 4) + 2] = this.mPoints[i * 4] + (this.barWidth / 2.0f);
                if (this.iStyle > 0) {
                    this.mLinePointsDown[i * 4] = this.mPoints[i * 4] - (this.barWidth / 2.0f);
                    this.mLinePointsDown[(i * 4) + 2] = this.mPoints[i * 4] + (this.barWidth / 2.0f);
                }
            }
            if (this.isTop) {
                this.mPoints[(i * 4) + 1] = 0.0f;
                this.mPoints[(i * 4) + 3] = this.mLasteFft[i];
                if (this.enableLine) {
                    float f4 = this.mLasteLine[i];
                    this.mLinePoints[(i * 4) + 3] = f4;
                    this.mLinePoints[(i * 4) + 1] = f4;
                    if (this.iStyle > 0) {
                        float f5 = (((float) height) - this.mLasteLine[i]) / 2.0f;
                        this.mLinePointsDown[(i * 4) + 3] = f5;
                        this.mLinePointsDown[(i * 4) + 1] = f5;
                    }
                }
            } else {
                this.mPoints[(i * 4) + 1] = (float) height;
                this.mPoints[(i * 4) + 3] = ((float) height) - this.mLasteFft[i];
                if (this.enableLine) {
                    if (this.iStyle > 0) {
                        if (bv.h()) {
                            float f6 = this.mLasteLine[i] <= 2.0f ? ((float) (height / 2)) - (this.mLasteLine[i] / 2.0f) : (((float) (height / 2)) - (this.mLasteLine[i] / 2.0f)) - 20.0f;
                            if (f6 < 2.0f) {
                                f6 = 2.0f;
                            }
                            this.mLinePoints[(i * 4) + 3] = f6;
                            this.mLinePoints[(i * 4) + 1] = f6;
                        } else {
                            float f7 = ((float) (height / 2)) - (this.mLasteLine[i] / 2.0f);
                            this.mLinePoints[(i * 4) + 3] = f7;
                            this.mLinePoints[(i * 4) + 1] = f7;
                        }
                        float f8 = ((float) (height / 2)) + (this.mLasteLine[i] / 2.0f);
                        this.mLinePointsDown[(i * 4) + 3] = f8;
                        this.mLinePointsDown[(i * 4) + 1] = f8;
                    } else {
                        float f9 = ((float) height) - this.mLasteLine[i];
                        this.mLinePoints[(i * 4) + 3] = f9;
                        this.mLinePoints[(i * 4) + 1] = f9;
                    }
                }
            }
        }
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        if (this.mBmpBar == null) {
            if (this.autoColor) {
                autoColor();
            }
            for (int i2 = 0; i2 < length; i2++) {
                int i3 = this.iStyle > 0 ? ((int) this.mPoints[(i2 * 4) + 1]) / 2 : (int) this.mPoints[(i2 * 4) + 1];
                int i4 = this.iStyle > 0 ? ((int) this.mPoints[(i2 * 4) + 1]) / 2 : (int) this.mPoints[(i2 * 4) + 1];
                float f10 = this.iStyle > 0 ? this.mPoints[(i2 * 4) + 3] / 2.0f : this.mPoints[(i2 * 4) + 3];
                float min = (((float) i3) == f10 || !bv.h() || ((float) i3) <= f10 || i3 <= 0) ? f10 : f10 - Math.min(20.0f, 20.0f * ((((float) i3) - f10) / 50.0f));
                int i5 = this.r;
                int i6 = this.g;
                int i7 = this.b;
                while (true) {
                    int i8 = i3;
                    if (((float) i8) <= min || i8 <= 0) {
                        int i9 = this.r;
                        int i10 = this.g;
                        int i11 = this.b;
                        int i12 = i4;
                    } else {
                        i6 = i6 + 4 < 255 ? i6 + 4 : 255;
                        i7 = i7 + 2 < 255 ? i7 + 2 : 255;
                        if (i5 >= 30 || i6 >= 30 || i7 >= 30) {
                            this.mPaint.setColor(Color.rgb(i5, i6, i7));
                        } else {
                            this.mPaint.setColor(Color.argb(200, 0, 160, 0));
                        }
                        canvas.drawLine(this.mPoints[i2 * 4], (float) (i8 - this.f_height), this.mPoints[(i2 * 4) + 2], (float) i8, this.mPaint);
                        i3 = i8 - (this.f_height + this.p_height);
                    }
                }
                int i92 = this.r;
                int i102 = this.g;
                int i112 = this.b;
                int i122 = i4;
                while (((float) i122) < this.mPoints[(i2 * 4) + 1] - f10 && this.iStyle > 0) {
                    int i13 = i102 + 4 < 255 ? i102 + 4 : 255;
                    int i14 = i112 + 2 < 255 ? i112 + 2 : 255;
                    if (i92 >= 30 || i13 >= 30 || i14 >= 30) {
                        this.mPaint.setColor(Color.rgb(i92, i13, i14));
                    } else {
                        this.mPaint.setColor(Color.argb(200, 0, 160, 0));
                    }
                    canvas.drawLine(this.mPoints[i2 * 4], (float) i122, this.mPoints[(i2 * 4) + 2], (float) (this.f_height + i122), this.mPaint);
                    i112 = i14;
                    i102 = i13;
                    i122 += this.f_height + this.p_height;
                }
            }
            boolean z = this.enableLine;
            if (bv.h() && !App.bBtAvPlayState) {
                z = false;
            }
            if (z) {
                canvas.drawLines(this.mLinePoints, this.linePaint);
                if (this.iStyle > 0) {
                    canvas.drawLines(this.mLinePointsDown, this.linePaint);
                }
            }
        } else if (this.mBmpBarBk != null) {
            for (int i15 = 0; i15 < length; i15++) {
                int height2 = (((int) this.mPoints[(i15 * 4) + 1]) - ((int) this.mPoints[(i15 * 4) + 3])) / this.mBmpBarBk.getHeight();
                int height3 = height / this.mBmpBarBk.getHeight();
                if (height2 <= 0) {
                    height2 = 1;
                }
                int i16 = height;
                for (int i17 = 0; i17 < height3; i17++) {
                    Rect rect2 = new Rect(0, 0, this.mBmpBar.getWidth(), this.mBmpBar.getHeight());
                    Rect rect3 = new Rect((int) this.mPoints[i15 * 4], i16 - this.mBmpBar.getHeight(), (int) (this.mPoints[i15 * 4] + this.barWidth), i16);
                    if (i17 < height2) {
                        canvas.drawBitmap(this.mBmpBar, rect2, rect3, (Paint) null);
                    } else {
                        canvas.drawBitmap(this.mBmpBarBk, rect2, rect3, (Paint) null);
                    }
                    i16 -= this.mBmpBar.getHeight();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void render(Canvas canvas, float f, Rect rect, int i) {
        onRender(canvas, f, rect);
        this.iStyle = i;
    }

    public void setColor(int i) {
        this.mPaint.setColor(i);
    }
}
