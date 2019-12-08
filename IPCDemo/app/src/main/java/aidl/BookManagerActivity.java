package aidl;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.lxk.ipcdemo.R;

import java.util.List;


/**
 * @author https://github.com/103style
 * @date 2019/10/27 14:44
 */
public class BookManagerActivity extends AppCompatActivity {

    private static final String TAG = BookManagerActivity.class.getSimpleName();

    private static final int BOOK_ADD_MSG = 0x001;

    private IBookManager remoteBookManager;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BOOK_ADD_MSG:
                    Log.e(TAG, "a new book add ：" + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
    private IBookAddListener bookAddListener = new IBookAddListener.Stub() {
        @Override
        public void onBookArrived(Book newBook) throws RemoteException {
            handler.obtainMessage(BOOK_ADD_MSG, newBook).sendToTarget();
        }
    };
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager iBookManager = IBookManager.Stub.asInterface(service);
            try {
                service.linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            try {
                remoteBookManager = iBookManager;
                List<Book> list = iBookManager.getBookList();
                Log.e(TAG, "query book list, type = " + list.getClass().getCanonicalName());
                Log.e(TAG, list.toString());
                Book book = new Book(3, "Android软件安全指南");
                remoteBookManager.addBook(book);
                remoteBookManager.registerListener(bookAddListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            remoteBookManager = null;
            Log.e(TAG, "binder died ");
        }
    };
    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (remoteBookManager == null) {
                return;
            }
            remoteBookManager.asBinder().unlinkToDeath(deathRecipient, 0);
            remoteBookManager = null;
            Intent intent = new Intent(BookManagerActivity.this, BookManagerService.class);
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unregisterListener();
        unbindService(connection);
        super.onDestroy();
    }

    private void unregisterListener() {
        if (remoteBookManager != null && remoteBookManager.asBinder().isBinderAlive()) {
            try {
                remoteBookManager.unregisterListener(bookAddListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
