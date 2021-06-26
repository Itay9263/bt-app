package defpackage;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.syu.app.App;

/* renamed from: by  reason: default package */
public class by extends Dialog {
    Activity a;
    LinearLayout b;
    AnimationDrawable c;
    TextView d;

    public by(Activity activity, int i, int i2) {
        super(activity, App.getApp().getIdStyle("Dialog"));
        this.a = activity;
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.dimAmount = 0.3f;
        attributes.gravity = 1;
        attributes.x = i;
        attributes.y = i2;
        attributes.width = -2;
        attributes.height = -2;
        getWindow().setAttributes(attributes);
        getWindow().setBackgroundDrawableResource(App.getApp().getIdDrawable("loading_dialog_bg"));
        getWindow().setWindowAnimations(App.getApp().getIdStyle("fade_anim_style"));
    }

    public String a() {
        if (this.d != null) {
            return this.d.getText().toString();
        }
        return null;
    }

    public void a(CharSequence charSequence) {
        if (this.d != null) {
            this.d.setText(charSequence);
        }
    }

    public void a(CharSequence charSequence, int i, int i2, Typeface typeface) {
        boolean z;
        if (this.b != null) {
            this.b.removeAllViews();
            this.b = null;
        }
        this.b = new LinearLayout(this.a);
        this.b.setOrientation(1);
        this.b.setGravity(1);
        this.b.removeAllViews();
        ImageView imageView = new ImageView(this.a);
        if (!App.mStrCustomer.equalsIgnoreCase("TZY_NEW") || !charSequence.equals(App.getApp().getString("bt_download"))) {
            imageView.setBackgroundResource(App.getApp().getIdAnim("loading_anim_list"));
            z = true;
        } else {
            imageView.setBackgroundResource(App.getApp().getIdAnim("loading_anim"));
            z = false;
        }
        imageView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        if (imageView.getBackground() != null) {
            this.c = (AnimationDrawable) imageView.getBackground();
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.leftMargin = 20;
        layoutParams.rightMargin = 20;
        layoutParams.bottomMargin = 5;
        this.b.addView(imageView, layoutParams);
        if (!App.mStrCustomer.equalsIgnoreCase("TZY_UI5") && z) {
            this.d = new TextView(this.a);
            if (typeface != null) {
                this.d.setTypeface(typeface);
            }
            this.d.setSingleLine();
            this.d.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.d.setFocusableInTouchMode(true);
            this.d.requestFocus();
            this.d.setTextColor(i);
            this.d.setTypeface(Typeface.MONOSPACE);
            this.d.setTextSize(0, (float) i2);
            this.d.setGravity(17);
            this.d.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            this.d.setText(charSequence);
            if (App.mStrCustomer.equalsIgnoreCase("TZY_NEW")) {
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams2.topMargin = -65;
                this.b.addView(this.d, layoutParams2);
            } else {
                this.b.addView(this.d);
            }
        }
        if (this.c != null) {
            this.c.start();
        }
        setContentView(this.b);
        setOwnerActivity(this.a);
        setCanceledOnTouchOutside(false);
        setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                if (by.this.b != null) {
                    by.this.b.removeAllViews();
                    by.this.b = null;
                }
                if (by.this.c != null) {
                    by.this.c.stop();
                    by.this.c = null;
                }
            }
        });
    }
}
