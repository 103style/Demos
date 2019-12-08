package aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import aidl.Book;
import aidl.IBookAddListener;
import aidl.IBookManager;

/**
 * @author https://github.com/103style
 * @date 2019/10/26 15:55
 */
public class BookManagerService extends Service {

    private static final String TAG = "BookManagerService";
    //服务是否已经销毁
    private AtomicBoolean destroyed = new AtomicBoolean(false);
    private CopyOnWriteArrayList<Book> bookList = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IBookAddListener> listeners = new RemoteCallbackList<>();

    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int check = checkCallingOrSelfPermission("com.aidl.test.permission.ACCESS_BOOK_SERVICE");
            if (check == PackageManager.PERMISSION_DENIED) {
                Log.e(TAG, "PERMISSION_DENIED");
                return false;
            }

            String packageName = null;
            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0) {
                packageName = packages[0];
            }
            if (!packageName.startsWith("com.aidl")) {
                Log.e(TAG, "packageName is illeagl = " + packageName);
                return false;
            }
            return super.onTransact(code, data, reply, flags);
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return bookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            bookList.add(book);
        }

        @Override
        public void registerListener(IBookAddListener listener) throws RemoteException {
            listeners.register(listener);
            final int N = listeners.beginBroadcast();
            Log.e(TAG, "registerListener: size = " + N);
            listeners.finishBroadcast();
        }

        @Override
        public void unregisterListener(IBookAddListener listener) throws RemoteException {
            listeners.unregister(listener);

            final int N = listeners.beginBroadcast();
            Log.e(TAG, "unregisterListener: size = " + N);
            listeners.finishBroadcast();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        bookList.add(new Book(1, "Android艺术开发探索"));
        bookList.add(new Book(2, "Java并发编程指南"));
        new Thread(new Worker()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission("com.aidl.test.permission.ACCESS_BOOK_SERVICE");
        if (check == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG, "PERMISSION_DENIED");
            return null;
        }
        return mBinder;
    }

    @Override
    public void onDestroy() {
        destroyed.set(true);
        super.onDestroy();
    }

    private void onBookAdd(Book book) throws RemoteException {
        bookList.add(book);
        final int N = listeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IBookAddListener listener = listeners.getBroadcastItem(i);
            if (listener != null) {
                listener.onBookArrived(book);
            }
        }
        listeners.finishBroadcast();
    }

    private class Worker implements Runnable {

        @Override
        public void run() {
            while (!destroyed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = bookList.size() + 1;
                Book book = new Book(bookId, "new book#" + bookId);
                try {
                    onBookAdd(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
