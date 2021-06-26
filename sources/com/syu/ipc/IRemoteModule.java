package com.syu.ipc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.syu.ipc.IModuleCallback;

public interface IRemoteModule extends IInterface {

    public static abstract class Stub extends Binder implements IRemoteModule {
        private static final String DESCRIPTOR = "com.syu.ipc.IRemoteModule";
        static final int TRANSACTION_cmd = 1;
        static final int TRANSACTION_get = 2;
        static final int TRANSACTION_register = 3;
        static final int TRANSACTION_unregister = 4;

        private static class Proxy implements IRemoteModule {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void cmd(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeIntArray(iArr);
                    obtain.writeFloatArray(fArr);
                    obtain.writeStringArray(strArr);
                    this.mRemote.transact(1, obtain, obtain2, 1);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public ModuleObject get(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException {
                ModuleObject moduleObject;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeIntArray(iArr);
                    obtain.writeFloatArray(fArr);
                    obtain.writeStringArray(strArr);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        moduleObject = new ModuleObject();
                        moduleObject.ints = obtain2.createIntArray();
                        moduleObject.flts = obtain2.createFloatArray();
                        moduleObject.strs = obtain2.createStringArray();
                    } else {
                        moduleObject = null;
                    }
                    return moduleObject;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void register(IModuleCallback iModuleCallback, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iModuleCallback != null ? iModuleCallback.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(3, obtain, obtain2, 1);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregister(IModuleCallback iModuleCallback, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iModuleCallback != null ? iModuleCallback.asBinder() : null);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, obtain2, 1);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRemoteModule asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IRemoteModule)) ? new Proxy(iBinder) : (IRemoteModule) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    cmd(parcel.readInt(), parcel.createIntArray(), parcel.createFloatArray(), parcel.createStringArray());
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    ModuleObject moduleObject = get(parcel.readInt(), parcel.createIntArray(), parcel.createFloatArray(), parcel.createStringArray());
                    parcel2.writeNoException();
                    if (moduleObject != null) {
                        parcel2.writeInt(1);
                        parcel2.writeIntArray(moduleObject.ints);
                        parcel2.writeFloatArray(moduleObject.flts);
                        parcel2.writeStringArray(moduleObject.strs);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    register(IModuleCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt());
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregister(IModuleCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void cmd(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException;

    ModuleObject get(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException;

    void register(IModuleCallback iModuleCallback, int i, int i2) throws RemoteException;

    void unregister(IModuleCallback iModuleCallback, int i) throws RemoteException;
}
