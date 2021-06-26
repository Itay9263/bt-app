package com.syu.ctrl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.syu.app.MyUi;
import com.syu.page.IPageNotify;
import java.util.ArrayList;
import java.util.List;

public class JViewPager extends ViewPager {
    private static final int Direction_Left = 1;
    private static final int Direction_None = 0;
    private static final int Direction_Right = 2;
    boolean bActionDown;
    boolean bMoveEnd;
    boolean bMoveHead;
    /* access modifiers changed from: private */
    public int iDirection;
    /* access modifiers changed from: private */
    public int itemCur;
    JPagerAdapter mAdapter;
    public int mArg2;
    public int[] mContents;
    private Context mContext;
    ViewPager mPager;
    List<View> mViews;
    List<View> mViewsReal;
    /* access modifiers changed from: private */
    public JPage page;

    public class JPagerAdapter extends PagerAdapter {
        List<View> mViews;

        public JPagerAdapter(List<View> list) {
            this.mViews = list;
        }

        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) obj);
        }

        public int getCount() {
            if (this.mViews != null) {
                return this.mViews.size();
            }
            return 0;
        }

        public Object instantiateItem(ViewGroup viewGroup, int i) {
            int currentItem = JViewPager.this.mPager.getCurrentItem();
            int size = currentItem == 0 ? this.mViews.size() - 2 : currentItem == this.mViews.size() + -1 ? 1 : currentItem;
            if (currentItem != size) {
                JViewPager.this.mPager.setCurrentItem(size, false);
                if (JViewPager.this.bMoveHead) {
                    JViewPager.this.bMoveHead = false;
                    JViewPager.this.AddRealView2Dst(1, 0);
                }
                if (JViewPager.this.bMoveEnd) {
                    JViewPager.this.bMoveEnd = false;
                    JViewPager.this.AddRealView2Dst(this.mViews.size() - 2, JViewPager.this.mViewsReal.size() - 1);
                }
                IPageNotify notify = JViewPager.this.page.getNotify();
                if (notify != null) {
                    notify.viewPageScrolled(JViewPager.this.mPager);
                }
            }
            View view = this.mViews.get(i);
            try {
                viewGroup.addView(view);
            } catch (Exception e) {
            }
            return view;
        }

        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }
    }

    public JViewPager(Context context) {
        super(context);
        this.mArg2 = 0;
        this.bActionDown = false;
        this.bMoveHead = false;
        this.bMoveEnd = false;
        this.itemCur = 0;
        this.iDirection = 0;
    }

    public JViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mArg2 = 0;
        this.bActionDown = false;
        this.bMoveHead = false;
        this.bMoveEnd = false;
        this.itemCur = 0;
        this.iDirection = 0;
    }

    public JViewPager(Context context, JPage jPage) {
        super(context);
        this.mArg2 = 0;
        this.bActionDown = false;
        this.bMoveHead = false;
        this.bMoveEnd = false;
        this.itemCur = 0;
        this.iDirection = 0;
        this.mPager = this;
        this.mContext = context;
        this.iDirection = 0;
        this.page = jPage;
    }

    private View setLayoutParam(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(-1, -1);
        } else {
            layoutParams.width = -1;
            layoutParams.height = -1;
        }
        view.setLayoutParams(layoutParams);
        return view;
    }

    public void AddRealView2Dst(int i, int i2) {
        ViewGroup viewGroup;
        if (i >= 0 && i <= this.mViews.size() - 1 && i2 >= 0 && i2 <= this.mViewsReal.size() - 1) {
            FrameLayout frameLayout = (FrameLayout) this.mViews.get(i);
            View view = this.mViewsReal.get(i2);
            if (view != null && (viewGroup = (ViewGroup) view.getParent()) != frameLayout) {
                if (viewGroup != null) {
                    viewGroup.removeView(view);
                }
                frameLayout.addView(view);
                frameLayout.invalidate();
            }
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                this.bActionDown = true;
                break;
            case 1:
            case 3:
                this.bActionDown = false;
                break;
        }
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (Exception e) {
            return false;
        }
    }

    public int[] getContents() {
        return this.mContents;
    }

    public int getSize() {
        return this.mContents.length;
    }

    public void setCurrentItem(int i) {
        if (this.mViewsReal == null || i <= this.mViewsReal.size()) {
            super.setCurrentItem(i);
        }
    }

    public void setUiLoader(MyUi myUi, int... iArr) {
        JPage loadPage;
        this.mContents = iArr;
        if (this.mContents != null) {
            this.mViews = new ArrayList();
            this.mViewsReal = new ArrayList();
            int length = this.mContents.length;
            if (length > 1) {
                int i = length + 2;
                for (int i2 = 0; i2 < i; i2++) {
                    FrameLayout frameLayout = new FrameLayout(this.mContext);
                    frameLayout.setBackground((Drawable) null);
                    this.mViews.add(setLayoutParam(frameLayout));
                    if (!(i2 == 0 || i2 == i - 1 || (loadPage = myUi.loadPage(true, this.mContents[i2 - 1])) == null)) {
                        this.mViewsReal.add((JPage) setLayoutParam(loadPage));
                        AddRealView2Dst(i2, i2 - 1);
                    }
                }
                if (this.mViews.size() > 0) {
                    this.mAdapter = new JPagerAdapter(this.mViews);
                    setAdapter(this.mAdapter);
                    setCurrentItem(1);
                    setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        public void onPageScrollStateChanged(int i) {
                        }

                        public void onPageScrolled(int i, float f, int i2) {
                            JViewPager.this.mArg2 = i2;
                            IPageNotify notify = JViewPager.this.page.getNotify();
                            if (i2 != 0 && JViewPager.this.bActionDown) {
                                if (JViewPager.this.iDirection == 0) {
                                    JViewPager.this.itemCur = JViewPager.this.mPager.getCurrentItem();
                                }
                                if (JViewPager.this.itemCur > i) {
                                    if (JViewPager.this.iDirection != 2) {
                                        JViewPager.this.iDirection = 2;
                                        if (notify != null) {
                                            notify.viewPagePrev(JViewPager.this.mPager);
                                        }
                                    }
                                } else if (JViewPager.this.iDirection != 1) {
                                    JViewPager.this.iDirection = 1;
                                    if (notify != null) {
                                        notify.viewPageNext(JViewPager.this.mPager);
                                    }
                                }
                                if (i == JViewPager.this.mViews.size() - 2) {
                                    if (!JViewPager.this.bMoveHead) {
                                        JViewPager.this.bMoveHead = true;
                                        JViewPager.this.AddRealView2Dst(JViewPager.this.mViews.size() - 1, 0);
                                    }
                                } else if (i != 0) {
                                    if (JViewPager.this.bMoveHead) {
                                        JViewPager.this.bMoveHead = false;
                                        JViewPager.this.AddRealView2Dst(1, 0);
                                    }
                                    if (JViewPager.this.bMoveEnd) {
                                        JViewPager.this.bMoveEnd = false;
                                        JViewPager.this.AddRealView2Dst(JViewPager.this.mViews.size() - 2, JViewPager.this.mViewsReal.size() - 1);
                                    }
                                } else if (!JViewPager.this.bMoveEnd) {
                                    JViewPager.this.bMoveEnd = true;
                                    JViewPager.this.AddRealView2Dst(0, JViewPager.this.mViewsReal.size() - 1);
                                }
                            }
                            if (i2 == 0) {
                                int currentItem = JViewPager.this.mPager.getCurrentItem();
                                int size = i == 0 ? JViewPager.this.mViews.size() - 2 : i == JViewPager.this.mViews.size() + -1 ? 1 : currentItem;
                                if (currentItem != size || JViewPager.this.iDirection != 0) {
                                    JViewPager.this.iDirection = 0;
                                    JViewPager.this.mPager.setCurrentItem(size, false);
                                    if (JViewPager.this.bMoveHead) {
                                        JViewPager.this.bMoveHead = false;
                                        JViewPager.this.AddRealView2Dst(1, 0);
                                    }
                                    if (JViewPager.this.bMoveEnd) {
                                        JViewPager.this.bMoveEnd = false;
                                        JViewPager.this.AddRealView2Dst(JViewPager.this.mViews.size() - 2, JViewPager.this.mViewsReal.size() - 1);
                                    }
                                    if (notify != null) {
                                        notify.viewPageScrolled(JViewPager.this.mPager);
                                    }
                                }
                            }
                        }

                        public void onPageSelected(int i) {
                        }
                    });
                }
            }
        }
    }
}
