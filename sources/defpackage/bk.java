package defpackage;

import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.AsyncTask;
import com.syu.app.App;
import com.syu.bt.act.InterfaceBt;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

/* renamed from: bk  reason: default package */
public class bk {
    /* access modifiers changed from: private */
    public File a;
    private String b = "recording_";
    private b c;
    private a d;
    /* access modifiers changed from: private */
    public boolean e = true;
    /* access modifiers changed from: private */
    public boolean f = false;
    /* access modifiers changed from: private */
    public int g = 8000;
    /* access modifiers changed from: private */
    public int h = 2;
    /* access modifiers changed from: private */
    public int i = 2;

    /* renamed from: bk$a */
    class a extends AsyncTask<Void, Integer, Void> {
        a() {
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Void doInBackground(Void... voidArr) {
            bk.this.f = true;
            int minBufferSize = AudioTrack.getMinBufferSize(bk.this.g, bk.this.h, bk.this.i);
            short[] sArr = new short[(minBufferSize / 4)];
            try {
                DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(bk.this.a)));
                AudioTrack audioTrack = new AudioTrack(0, bk.this.g, bk.this.h, bk.this.i, minBufferSize, 1);
                audioTrack.play();
                while (bk.this.f && dataInputStream.available() > 0) {
                    int i = 0;
                    while (dataInputStream.available() > 0 && i < sArr.length) {
                        sArr[i] = dataInputStream.readShort();
                        i++;
                    }
                    audioTrack.write(sArr, 0, sArr.length);
                }
                audioTrack.stop();
                bk.this.f = false;
                dataInputStream.close();
                Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                while (it.hasNext()) {
                    it.next().dismissRecordDlg();
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        }
    }

    /* renamed from: bk$b */
    class b extends AsyncTask<Void, Integer, Void> {
        b() {
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Void doInBackground(Void... voidArr) {
            bk.this.e = true;
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(bk.this.a)));
                int minBufferSize = AudioRecord.getMinBufferSize(bk.this.g, bk.this.h, bk.this.i);
                AudioRecord audioRecord = new AudioRecord(1, bk.this.g, bk.this.h, bk.this.i, minBufferSize);
                short[] sArr = new short[minBufferSize];
                audioRecord.startRecording();
                int i = 0;
                while (bk.this.e) {
                    int read = audioRecord.read(sArr, 0, sArr.length);
                    for (int i2 = 0; i2 < read; i2++) {
                        dataOutputStream.writeShort(sArr[i2]);
                    }
                    publishProgress(new Integer[]{Integer.valueOf(i)});
                    i++;
                }
                audioRecord.stop();
                dataOutputStream.close();
                return null;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public void a() {
        try {
            if (this.a != null && this.a.exists()) {
                this.a.delete();
            }
            this.a = File.createTempFile(this.b, ".pcm", new File("/mnt/sdcard"));
            this.c = new b();
            this.c.execute(new Void[0]);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void b() {
        try {
            if (this.a != null) {
                this.e = false;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void c() {
        this.d = new a();
        this.d.execute(new Void[0]);
    }
}
