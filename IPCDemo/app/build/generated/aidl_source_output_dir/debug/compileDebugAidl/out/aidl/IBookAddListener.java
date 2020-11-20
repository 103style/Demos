/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package aidl;

public interface IBookAddListener extends android.os.IInterface {
    /**
     * Default implementation for IBookAddListener.
     */
    public static class Default implements aidl.IBookAddListener {
        @Override
        public void onBookArrived(aidl.Book newBook) throws android.os.RemoteException {
        }

        @Override
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements aidl.IBookAddListener {
        private static final java.lang.String DESCRIPTOR = "aidl.IBookAddListener";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an aidl.IBookAddListener interface,
         * generating a proxy if needed.
         */
        public static aidl.IBookAddListener asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof aidl.IBookAddListener))) {
                return ((aidl.IBookAddListener) iin);
            }
            return new aidl.IBookAddListener.Stub.Proxy(obj);
        }

        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            java.lang.String descriptor = DESCRIPTOR;
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(descriptor);
                    return true;
                }
                case TRANSACTION_onBookArrived: {
                    data.enforceInterface(descriptor);
                    aidl.Book _arg0;
                    if ((0 != data.readInt())) {
                        _arg0 = aidl.Book.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    this.onBookArrived(_arg0);
                    reply.writeNoException();
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }

        private static class Proxy implements aidl.IBookAddListener {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public void onBookArrived(aidl.Book newBook) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((newBook != null)) {
                        _data.writeInt(1);
                        newBook.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = mRemote.transact(Stub.TRANSACTION_onBookArrived, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        getDefaultImpl().onBookArrived(newBook);
                        return;
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public static aidl.IBookAddListener sDefaultImpl;
        }

        static final int TRANSACTION_onBookArrived = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);

        public static boolean setDefaultImpl(aidl.IBookAddListener impl) {
            if (Stub.Proxy.sDefaultImpl == null && impl != null) {
                Stub.Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static aidl.IBookAddListener getDefaultImpl() {
            return Stub.Proxy.sDefaultImpl;
        }
    }

    public void onBookArrived(aidl.Book newBook) throws android.os.RemoteException;
}
