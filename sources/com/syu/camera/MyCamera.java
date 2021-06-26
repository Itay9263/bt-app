package com.syu.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.os.SystemProperties;
import android.view.SurfaceHolder;
import com.syu.app.MyApplication;
import com.syu.ipcself.Conn;
import com.syu.ipcself.module.main.Main;
import com.syu.share.ShareHandler;
import java.util.List;
import java.util.Locale;

public class MyCamera {
    public static int QUALITY_CVBSN;
    public static int QUALITY_CVBSP;
    public static boolean bBackCar = false;
    public static boolean bDvd9853 = false;
    public static MyCamera mCameraInst = null;
    public static int mVideoFrameHeight = 0;
    public static int mVideoFrameWidth = 0;
    private boolean bIsPreview = false;
    public boolean bShutDown = false;
    public Camera mCamera;
    public Camera.Parameters mParameters;
    public CamcorderProfile mProfile;
    Runnable_setPreviewSurfHolder runnable_setPreviewSurfHolder = null;
    Runnable_setPreviewSurfTexture runnable_setPreviewSurfTexture = null;

    public class Runnable_setPreviewSurfHolder implements Runnable {
        public boolean bRunning = true;
        SurfaceHolder holder;

        Runnable_setPreviewSurfHolder(SurfaceHolder surfaceHolder) {
            this.holder = surfaceHolder;
        }

        public void run() {
            if (!MyCamera.this.bShutDown) {
                MyCamera.this.open(MyCamera.this.getCameraId());
                if (MyCamera.this.mCamera != null) {
                    try {
                        MyCamera.this.mCamera.setPreviewDisplay(this.holder);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    boolean access$0 = MyCamera.this.initCamera(MyCamera.this.getCameraId());
                    boolean startPreView = access$0 ? MyCamera.this.startPreView() : false;
                    if (!access$0 || !startPreView) {
                        Main.postRunnable_Ui(false, new Runnable() {
                            public void run() {
                                if (!MyCamera.this.bShutDown) {
                                    MyCamera.this.setPreview(Runnable_setPreviewSurfHolder.this.holder);
                                }
                            }
                        }, 1000);
                    }
                } else if (this.bRunning) {
                    Main.postRunnable_Ui(true, this, 1000);
                }
            }
        }

        public void stopRun() {
            this.bRunning = false;
        }
    }

    public class Runnable_setPreviewSurfTexture implements Runnable {
        public boolean bRunning = true;
        SurfaceTexture surface;

        Runnable_setPreviewSurfTexture(SurfaceTexture surfaceTexture) {
            this.surface = surfaceTexture;
        }

        public void run() {
            if (!MyCamera.this.bShutDown) {
                MyCamera.this.open(MyCamera.this.getCameraId());
                if (MyCamera.this.mCamera != null) {
                    try {
                        MyCamera.this.mCamera.setPreviewTexture(this.surface);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    boolean access$0 = MyCamera.this.initCamera(MyCamera.this.getCameraId());
                    boolean startPreView = access$0 ? MyCamera.this.startPreView() : false;
                    if (!access$0 || !startPreView) {
                        Main.postRunnable_Ui(false, new Runnable() {
                            public void run() {
                                if (!MyCamera.this.bShutDown) {
                                    MyCamera.this.setPreview(Runnable_setPreviewSurfTexture.this.surface);
                                }
                            }
                        }, 500);
                    }
                } else if (this.bRunning) {
                    Main.postRunnable_Ui(true, this, 1000);
                }
            }
        }

        public void stopRun() {
            this.bRunning = false;
        }
    }

    protected MyCamera() {
    }

    public static synchronized MyCamera getInstance() {
        MyCamera myCamera;
        synchronized (MyCamera.class) {
            if (mCameraInst == null) {
                mCameraInst = new MyCamera();
            }
            myCamera = mCameraInst;
        }
        return myCamera;
    }

    /* access modifiers changed from: private */
    public boolean initCamera(int i) {
        try {
            if (this.mCamera != null) {
                this.mParameters = this.mCamera.getParameters();
                if (this.mParameters == null) {
                    return false;
                }
                this.mProfile = getCamcorderProfile(i, this.mParameters);
                if (this.mProfile == null) {
                    return false;
                }
                int i2 = 0;
                for (Integer next : this.mParameters.getSupportedPreviewFrameRates()) {
                    if (next.intValue() > i2) {
                        int intValue = next.intValue();
                        this.mProfile.videoFrameRate = intValue;
                        i2 = intValue;
                    }
                }
                if (this.mProfile.videoFrameRate == 0) {
                    this.mProfile.videoFrameRate = 20;
                }
                this.mParameters.setPreviewFrameRate(this.mProfile.videoFrameRate);
                if (Main.mConf_PlatForm == 7 && bBackCar) {
                    this.mParameters.set("mirror-preview", Main.DATA[24] == 1 ? "true" : "false");
                }
                this.mParameters.setPreviewSize(this.mProfile.videoFrameWidth, this.mProfile.videoFrameHeight);
                if (Conn.mInterfaceApp != null) {
                    Conn.mInterfaceApp.setPreviewFormat();
                }
                this.mCamera.setParameters(this.mParameters);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isVideoQualityExists(int i, int i2, List<Camera.Size> list) {
        if (list == null || !CamcorderProfile.hasProfile(i, i2)) {
            return false;
        }
        CamcorderProfile camcorderProfile = CamcorderProfile.get(i, i2);
        for (int i3 = 0; i3 < list.size(); i3++) {
            if (camcorderProfile.videoFrameWidth == list.get(i3).width && camcorderProfile.videoFrameHeight == list.get(i3).height) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x008b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.media.CamcorderProfile getCamcorderProfile(int r18, android.hardware.Camera.Parameters r19) {
        /*
            r17 = this;
            r4 = 0
            if (r19 == 0) goto L_0x00a4
            r3 = 1
            android.media.CamcorderProfile r4 = android.media.CamcorderProfile.get(r3)
            java.util.List r12 = r19.getSupportedPreviewSizes()
            r3 = 0
            int r5 = com.syu.ipcself.module.main.Main.mConf_PlatForm
            r6 = 5
            if (r5 != r6) goto L_0x0036
            int[] r5 = com.syu.ipcself.module.main.Main.DATA
            r6 = 12
            r5 = r5[r6]
            r6 = 1
            if (r5 != r6) goto L_0x0022
            int r5 = com.syu.ipcself.module.main.Main.getBackCameraType()
            r6 = 1
            if (r5 != r6) goto L_0x0036
        L_0x0022:
            int[] r5 = com.syu.ipcself.module.main.Main.DATA     // Catch:{ Exception -> 0x00d2 }
            r6 = 70
            r5 = r5[r6]     // Catch:{ Exception -> 0x00d2 }
            if (r5 != 0) goto L_0x00b1
            int r5 = QUALITY_CVBSN     // Catch:{ Exception -> 0x00d2 }
            r0 = r18
            android.media.CamcorderProfile r5 = android.media.CamcorderProfile.get(r0, r5)     // Catch:{ Exception -> 0x00d2 }
        L_0x0032:
            if (r5 == 0) goto L_0x0036
            r3 = 1
            r4 = r5
        L_0x0036:
            if (r3 != 0) goto L_0x0047
            r3 = 1080(0x438, float:1.513E-42)
            r0 = r17
            r1 = r18
            r2 = r19
            android.media.CamcorderProfile r3 = r0.getCamcorderProfileByQuality(r1, r3, r2, r12)
            if (r3 == 0) goto L_0x0047
            r4 = r3
        L_0x0047:
            r10 = 704(0x2c0, float:9.87E-43)
            r9 = 468(0x1d4, float:6.56E-43)
            r8 = 704(0x2c0, float:9.87E-43)
            r7 = 564(0x234, float:7.9E-43)
            r6 = 0
            r3 = 0
            java.util.Iterator r11 = r12.iterator()
            r5 = r3
        L_0x0056:
            boolean r3 = r11.hasNext()
            if (r3 != 0) goto L_0x00dc
            r11 = 0
            android.hardware.Camera$Size r13 = r19.getPreferredPreviewSizeForVideo()
            if (r13 == 0) goto L_0x0075
            int r3 = r13.width
            if (r3 <= 0) goto L_0x0075
            int r3 = r13.height
            if (r3 <= 0) goto L_0x0075
            java.util.Iterator r14 = r12.iterator()
        L_0x006f:
            boolean r3 = r14.hasNext()
            if (r3 != 0) goto L_0x0129
        L_0x0075:
            r3 = r11
        L_0x0076:
            if (r3 != 0) goto L_0x007c
            r4.videoFrameWidth = r6
            r4.videoFrameHeight = r5
        L_0x007c:
            boolean r3 = com.syu.share.ShareHandler.isTp2825()
            boolean r11 = com.syu.share.ShareHandler.isTp2850()
            int r13 = com.syu.ipcself.module.main.Main.getBackCameraType()
            r14 = 1
            if (r13 != r14) goto L_0x0098
            if (r3 != 0) goto L_0x008f
            if (r11 == 0) goto L_0x01fd
        L_0x008f:
            int[] r3 = com.syu.ipcself.module.main.Main.DATA
            r7 = 70
            r3 = r3[r7]
            switch(r3) {
                case 0: goto L_0x0145;
                case 1: goto L_0x016b;
                case 2: goto L_0x017f;
                case 3: goto L_0x017f;
                case 4: goto L_0x017f;
                case 5: goto L_0x017f;
                case 6: goto L_0x01a3;
                case 7: goto L_0x01a3;
                case 8: goto L_0x01bc;
                case 9: goto L_0x01bc;
                case 10: goto L_0x01d5;
                case 11: goto L_0x01e9;
                default: goto L_0x0098;
            }
        L_0x0098:
            int r3 = r4.videoFrameWidth
            if (r3 > r6) goto L_0x00a0
            int r3 = r4.videoFrameHeight
            if (r3 <= r5) goto L_0x00a4
        L_0x00a0:
            r4.videoFrameWidth = r6
            r4.videoFrameHeight = r5
        L_0x00a4:
            boolean r3 = bDvd9853
            if (r3 == 0) goto L_0x00b0
            r3 = 720(0x2d0, float:1.009E-42)
            r4.videoFrameWidth = r3
            r3 = 480(0x1e0, float:6.73E-43)
            r4.videoFrameHeight = r3
        L_0x00b0:
            return r4
        L_0x00b1:
            int[] r5 = com.syu.ipcself.module.main.Main.DATA     // Catch:{ Exception -> 0x00d2 }
            r6 = 70
            r5 = r5[r6]     // Catch:{ Exception -> 0x00d2 }
            r6 = 1
            if (r5 != r6) goto L_0x00c4
            int r5 = QUALITY_CVBSP     // Catch:{ Exception -> 0x00d2 }
            r0 = r18
            android.media.CamcorderProfile r5 = android.media.CamcorderProfile.get(r0, r5)     // Catch:{ Exception -> 0x00d2 }
            goto L_0x0032
        L_0x00c4:
            r5 = 1080(0x438, float:1.513E-42)
            r0 = r17
            r1 = r18
            r2 = r19
            android.media.CamcorderProfile r5 = r0.getCamcorderProfileByQuality(r1, r5, r2, r12)     // Catch:{ Exception -> 0x00d2 }
            goto L_0x0032
        L_0x00d2:
            r5 = move-exception
            r5 = 4
            r0 = r18
            android.media.CamcorderProfile r5 = android.media.CamcorderProfile.get(r0, r5)
            goto L_0x0032
        L_0x00dc:
            java.lang.Object r3 = r11.next()
            android.hardware.Camera$Size r3 = (android.hardware.Camera.Size) r3
            int r13 = r3.height
            if (r13 <= r5) goto L_0x0109
            int r6 = r3.width
            int r5 = r3.height
        L_0x00ea:
            int r13 = r3.width
            r14 = 740(0x2e4, float:1.037E-42)
            if (r13 >= r14) goto L_0x0056
            int r13 = r3.width
            r14 = 700(0x2bc, float:9.81E-43)
            if (r13 <= r14) goto L_0x0056
            int r13 = r3.height
            r14 = 500(0x1f4, float:7.0E-43)
            if (r13 >= r14) goto L_0x0116
            int r13 = r3.height
            r14 = 460(0x1cc, float:6.45E-43)
            if (r13 <= r14) goto L_0x0116
            int r10 = r3.width
            int r3 = r3.height
            r9 = r3
            goto L_0x0056
        L_0x0109:
            int r13 = r3.height
            if (r13 != r5) goto L_0x00ea
            int r13 = r3.width
            if (r13 <= r6) goto L_0x00ea
            int r6 = r3.width
            int r5 = r3.height
            goto L_0x00ea
        L_0x0116:
            int r13 = r3.height
            r14 = 600(0x258, float:8.41E-43)
            if (r13 >= r14) goto L_0x0056
            int r13 = r3.height
            r14 = 560(0x230, float:7.85E-43)
            if (r13 <= r14) goto L_0x0056
            int r8 = r3.width
            int r3 = r3.height
            r7 = r3
            goto L_0x0056
        L_0x0129:
            java.lang.Object r3 = r14.next()
            android.hardware.Camera$Size r3 = (android.hardware.Camera.Size) r3
            int r15 = r13.height
            int r0 = r3.height
            r16 = r0
            r0 = r16
            if (r15 != r0) goto L_0x006f
            r11 = 1
            int r13 = r3.width
            r4.videoFrameWidth = r13
            int r3 = r3.height
            r4.videoFrameHeight = r3
            r3 = r11
            goto L_0x0076
        L_0x0145:
            r3 = 4
            r0 = r18
            android.media.CamcorderProfile r3 = android.media.CamcorderProfile.get(r0, r3)
            if (r3 == 0) goto L_0x014f
            r4 = r3
        L_0x014f:
            int r3 = mVideoFrameWidth
            if (r3 <= 0) goto L_0x0161
            int r3 = mVideoFrameHeight
            if (r3 <= 0) goto L_0x0161
            int r3 = mVideoFrameHeight
            r4.videoFrameHeight = r3
            int r3 = mVideoFrameWidth
            r4.videoFrameWidth = r3
            goto L_0x0098
        L_0x0161:
            r3 = 480(0x1e0, float:6.73E-43)
            r4.videoFrameHeight = r3
            r3 = 960(0x3c0, float:1.345E-42)
            r4.videoFrameWidth = r3
            goto L_0x0098
        L_0x016b:
            r3 = 4
            r0 = r18
            android.media.CamcorderProfile r3 = android.media.CamcorderProfile.get(r0, r3)
            if (r3 == 0) goto L_0x0175
            r4 = r3
        L_0x0175:
            r3 = 576(0x240, float:8.07E-43)
            r4.videoFrameHeight = r3
            r3 = 960(0x3c0, float:1.345E-42)
            r4.videoFrameWidth = r3
            goto L_0x0098
        L_0x017f:
            r3 = 720(0x2d0, float:1.009E-42)
            r0 = r17
            r1 = r18
            r2 = r19
            android.media.CamcorderProfile r3 = r0.getCamcorderProfileByQuality(r1, r3, r2, r12)
            if (r3 == 0) goto L_0x018e
            r4 = r3
        L_0x018e:
            r3 = 1280(0x500, float:1.794E-42)
            r4.videoFrameWidth = r3
            r3 = 720(0x2d0, float:1.009E-42)
            r4.videoFrameHeight = r3
            int r3 = com.syu.ipcself.module.main.Main.mConf_PlatForm
            switch(r3) {
                case 6: goto L_0x0098;
                case 7: goto L_0x0098;
                case 8: goto L_0x0098;
                default: goto L_0x019b;
            }
        L_0x019b:
            int r3 = r4.videoFrameWidth
            int r3 = r3 + -16
            r4.videoFrameWidth = r3
            goto L_0x0098
        L_0x01a3:
            r3 = 1080(0x438, float:1.513E-42)
            r0 = r17
            r1 = r18
            r2 = r19
            android.media.CamcorderProfile r3 = r0.getCamcorderProfileByQuality(r1, r3, r2, r12)
            if (r3 == 0) goto L_0x01b2
            r4 = r3
        L_0x01b2:
            r3 = 1920(0x780, float:2.69E-42)
            r4.videoFrameWidth = r3
            r3 = 1080(0x438, float:1.513E-42)
            r4.videoFrameHeight = r3
            goto L_0x0098
        L_0x01bc:
            r3 = 720(0x2d0, float:1.009E-42)
            r0 = r17
            r1 = r18
            r2 = r19
            android.media.CamcorderProfile r3 = r0.getCamcorderProfileByQuality(r1, r3, r2, r12)
            if (r3 == 0) goto L_0x01cb
            r4 = r3
        L_0x01cb:
            r3 = 1280(0x500, float:1.794E-42)
            r4.videoFrameWidth = r3
            r3 = 720(0x2d0, float:1.009E-42)
            r4.videoFrameHeight = r3
            goto L_0x0098
        L_0x01d5:
            r3 = 4
            r0 = r18
            android.media.CamcorderProfile r3 = android.media.CamcorderProfile.get(r0, r3)
            if (r3 == 0) goto L_0x01df
            r4 = r3
        L_0x01df:
            r3 = 720(0x2d0, float:1.009E-42)
            r4.videoFrameWidth = r3
            r3 = 240(0xf0, float:3.36E-43)
            r4.videoFrameHeight = r3
            goto L_0x0098
        L_0x01e9:
            r3 = 4
            r0 = r18
            android.media.CamcorderProfile r3 = android.media.CamcorderProfile.get(r0, r3)
            if (r3 == 0) goto L_0x01f3
            r4 = r3
        L_0x01f3:
            r3 = 720(0x2d0, float:1.009E-42)
            r4.videoFrameWidth = r3
            r3 = 288(0x120, float:4.04E-43)
            r4.videoFrameHeight = r3
            goto L_0x0098
        L_0x01fd:
            int r3 = com.syu.ipcself.module.main.Main.mConf_PlatForm
            switch(r3) {
                case 6: goto L_0x0210;
                case 7: goto L_0x0202;
                case 8: goto L_0x0210;
                default: goto L_0x0202;
            }
        L_0x0202:
            int[] r3 = com.syu.ipcself.module.main.Main.DATA
            r11 = 70
            r3 = r3[r11]
            if (r3 != 0) goto L_0x02b8
            r4.videoFrameWidth = r10
            r4.videoFrameHeight = r9
            goto L_0x0098
        L_0x0210:
            int[] r3 = com.syu.ipcself.module.main.Main.DATA
            r7 = 70
            r3 = r3[r7]
            switch(r3) {
                case 0: goto L_0x021b;
                case 1: goto L_0x0249;
                case 2: goto L_0x0219;
                case 3: goto L_0x0219;
                case 4: goto L_0x0219;
                case 5: goto L_0x0219;
                case 6: goto L_0x0219;
                case 7: goto L_0x0219;
                case 8: goto L_0x0277;
                case 9: goto L_0x0277;
                case 10: goto L_0x0290;
                case 11: goto L_0x02a4;
                default: goto L_0x0219;
            }
        L_0x0219:
            goto L_0x0098
        L_0x021b:
            boolean r3 = com.syu.share.ShareHandler.is6322()
            if (r3 == 0) goto L_0x0235
            r3 = 4
            r0 = r18
            android.media.CamcorderProfile r3 = android.media.CamcorderProfile.get(r0, r3)
            if (r3 == 0) goto L_0x022b
            r4 = r3
        L_0x022b:
            r3 = 720(0x2d0, float:1.009E-42)
            r4.videoFrameWidth = r3
            r3 = 240(0xf0, float:3.36E-43)
            r4.videoFrameHeight = r3
            goto L_0x0098
        L_0x0235:
            r3 = 4
            r0 = r18
            android.media.CamcorderProfile r3 = android.media.CamcorderProfile.get(r0, r3)
            if (r3 == 0) goto L_0x023f
            r4 = r3
        L_0x023f:
            r3 = 720(0x2d0, float:1.009E-42)
            r4.videoFrameWidth = r3
            r3 = 480(0x1e0, float:6.73E-43)
            r4.videoFrameHeight = r3
            goto L_0x0098
        L_0x0249:
            boolean r3 = com.syu.share.ShareHandler.is6322()
            if (r3 == 0) goto L_0x0263
            r3 = 4
            r0 = r18
            android.media.CamcorderProfile r3 = android.media.CamcorderProfile.get(r0, r3)
            if (r3 == 0) goto L_0x0259
            r4 = r3
        L_0x0259:
            r3 = 720(0x2d0, float:1.009E-42)
            r4.videoFrameWidth = r3
            r3 = 288(0x120, float:4.04E-43)
            r4.videoFrameHeight = r3
            goto L_0x0098
        L_0x0263:
            r3 = 4
            r0 = r18
            android.media.CamcorderProfile r3 = android.media.CamcorderProfile.get(r0, r3)
            if (r3 == 0) goto L_0x026d
            r4 = r3
        L_0x026d:
            r3 = 720(0x2d0, float:1.009E-42)
            r4.videoFrameWidth = r3
            r3 = 576(0x240, float:8.07E-43)
            r4.videoFrameHeight = r3
            goto L_0x0098
        L_0x0277:
            r3 = 720(0x2d0, float:1.009E-42)
            r0 = r17
            r1 = r18
            r2 = r19
            android.media.CamcorderProfile r3 = r0.getCamcorderProfileByQuality(r1, r3, r2, r12)
            if (r3 == 0) goto L_0x0286
            r4 = r3
        L_0x0286:
            r3 = 1280(0x500, float:1.794E-42)
            r4.videoFrameWidth = r3
            r3 = 720(0x2d0, float:1.009E-42)
            r4.videoFrameHeight = r3
            goto L_0x0098
        L_0x0290:
            r3 = 4
            r0 = r18
            android.media.CamcorderProfile r3 = android.media.CamcorderProfile.get(r0, r3)
            if (r3 == 0) goto L_0x029a
            r4 = r3
        L_0x029a:
            r3 = 720(0x2d0, float:1.009E-42)
            r4.videoFrameWidth = r3
            r3 = 240(0xf0, float:3.36E-43)
            r4.videoFrameHeight = r3
            goto L_0x0098
        L_0x02a4:
            r3 = 4
            r0 = r18
            android.media.CamcorderProfile r3 = android.media.CamcorderProfile.get(r0, r3)
            if (r3 == 0) goto L_0x02ae
            r4 = r3
        L_0x02ae:
            r3 = 720(0x2d0, float:1.009E-42)
            r4.videoFrameWidth = r3
            r3 = 288(0x120, float:4.04E-43)
            r4.videoFrameHeight = r3
            goto L_0x0098
        L_0x02b8:
            r4.videoFrameWidth = r8
            r4.videoFrameHeight = r7
            goto L_0x0098
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.camera.MyCamera.getCamcorderProfile(int, android.hardware.Camera$Parameters):android.media.CamcorderProfile");
    }

    public CamcorderProfile getCamcorderProfileByQuality(int i, int i2, Camera.Parameters parameters, List<Camera.Size> list) {
        CamcorderProfile camcorderProfile = null;
        if (i2 >= 1080 && isVideoQualityExists(i, 6, list)) {
            camcorderProfile = CamcorderProfile.get(i, 6);
        }
        if (camcorderProfile == null && i2 >= 720 && isVideoQualityExists(i, 5, list)) {
            camcorderProfile = CamcorderProfile.get(i, 5);
        }
        return (camcorderProfile != null || !isVideoQualityExists(i, 4, list)) ? camcorderProfile : CamcorderProfile.get(i, 4);
    }

    public int getCameraId() {
        int cameraId;
        if (Conn.mInterfaceApp != null && (cameraId = Conn.mInterfaceApp.getCameraId()) >= 0) {
            return cameraId;
        }
        if (MyApplication.mIdChip == 8) {
            return 0;
        }
        if (Main.mConf_PlatForm == 8) {
            return 1;
        }
        if (bBackCar) {
            return Main.getBackCameraId(MyApplication.mIdPlatForm);
        }
        return 0;
    }

    public int getFixWidth() {
        switch (Main.mConf_PlatForm) {
            case 8:
                return 990;
            default:
                return 960;
        }
    }

    public boolean isCameraOpen() {
        return this.mCamera != null;
    }

    public boolean isCameraPreview() {
        return this.bIsPreview;
    }

    public void open(int i) {
        if (this.mCamera == null) {
            setSystemProp();
            this.mCamera = open_actual(i);
        }
    }

    public Camera open_actual(int i) {
        try {
            switch (Main.mConf_PlatForm) {
                case 7:
                case 8:
                    return Camera.open(i);
                default:
                    int numberOfCameras = Camera.getNumberOfCameras();
                    Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                    for (int i2 = 0; i2 < numberOfCameras; i2++) {
                        Camera.getCameraInfo(i2, cameraInfo);
                        if (cameraInfo.facing == i) {
                            return Camera.open(i);
                        }
                    }
                    break;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public void setPreview(SurfaceTexture surfaceTexture) {
        this.bShutDown = false;
        try {
            if (this.runnable_setPreviewSurfTexture != null) {
                this.runnable_setPreviewSurfTexture.stopRun();
            }
            this.runnable_setPreviewSurfTexture = new Runnable_setPreviewSurfTexture(surfaceTexture);
            this.runnable_setPreviewSurfTexture.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPreview(SurfaceHolder surfaceHolder) {
        this.bShutDown = false;
        try {
            if (this.runnable_setPreviewSurfHolder != null) {
                this.runnable_setPreviewSurfHolder.stopRun();
            }
            this.runnable_setPreviewSurfHolder = new Runnable_setPreviewSurfHolder(surfaceHolder);
            this.runnable_setPreviewSurfHolder.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSystemProp() {
        int i;
        int i2 = 288;
        int i3 = 480;
        switch (Main.mConf_PlatForm) {
            case 6:
            case 8:
                boolean isTp2825 = ShareHandler.isTp2825();
                boolean isTp2850 = ShareHandler.isTp2850();
                if (bDvd9853) {
                    i = 720;
                } else {
                    if (Main.getBackCameraType() == 1) {
                        if (!isTp2825 && !isTp2850) {
                            switch (Main.DATA[70]) {
                                case 0:
                                    if (!ShareHandler.is6322()) {
                                        i = 720;
                                        break;
                                    } else {
                                        i3 = 240;
                                        i = 720;
                                        break;
                                    }
                                case 1:
                                    if (!ShareHandler.is6322()) {
                                        i3 = 576;
                                        i = 720;
                                        break;
                                    } else {
                                        i3 = 288;
                                        i = 720;
                                        break;
                                    }
                                case 8:
                                case 9:
                                    i3 = 720;
                                    i = 1280;
                                    break;
                                case 10:
                                    i3 = 240;
                                    i = 720;
                                    break;
                                case 11:
                                    i3 = 288;
                                    i = 720;
                                    break;
                            }
                        } else {
                            switch (Main.DATA[70]) {
                                case 0:
                                    i = getFixWidth();
                                    break;
                                case 1:
                                    i3 = 576;
                                    i = getFixWidth();
                                    break;
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                    i3 = 720;
                                    i = 1280;
                                    break;
                                case 6:
                                case 7:
                                    i3 = 1080;
                                    i = 1920;
                                    break;
                                case 8:
                                case 9:
                                    i3 = 720;
                                    i = 1280;
                                    break;
                                case 10:
                                    i3 = 240;
                                    i = 720;
                                    break;
                                case 11:
                                    break;
                                default:
                                    i2 = 480;
                                    break;
                            }
                            i3 = i2;
                            i = 720;
                        }
                    }
                    i = 720;
                }
                String format = String.format(Locale.US, "%d", new Object[]{Integer.valueOf(i)});
                String format2 = String.format(Locale.US, "%d", new Object[]{Integer.valueOf(i3)});
                SystemProperties.set("sys.fyt.cvbs.width", format);
                SystemProperties.set("sys.fyt.cvbs.height", format2);
                SystemProperties.set("sys.fyt.cvbs.trim_width", format);
                SystemProperties.set("sys.fyt.cvbs.trim_height", format2);
                SystemProperties.set("sys.fyt.cvbs.scaler_trim.w", format);
                SystemProperties.set("sys.fyt.cvbs.scaler_trim.h", format2);
                SystemProperties.set("sys.fyt.cvbs.scaler_trim.x", "0");
                SystemProperties.set("sys.fyt.cvbs.scaler_trim.y", "0");
                return;
            default:
                return;
        }
    }

    public void shutDown() {
        this.bShutDown = true;
        if (this.mCamera != null) {
            if (this.bIsPreview) {
                this.mCamera.stopPreview();
                this.bIsPreview = false;
            }
            this.mCamera.release();
            this.mCamera = null;
            if (Conn.mInterfaceApp != null) {
                Conn.mInterfaceApp.notify_stopCamera();
            }
        }
    }

    public boolean startPreView() {
        if (this.mCamera == null || this.bIsPreview) {
            return true;
        }
        try {
            if (Conn.mInterfaceApp != null) {
                Conn.mInterfaceApp.setCameraCallBack();
            }
            this.mCamera.startPreview();
            if (Conn.mInterfaceApp != null) {
                Conn.mInterfaceApp.notify_startCamera();
            }
            this.bIsPreview = true;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
