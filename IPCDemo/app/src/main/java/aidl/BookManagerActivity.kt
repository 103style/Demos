package aidl

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lxk.ipcdemo.R

/**
 * @author https://github.com/103style
 * @date 2020/4/2 23:02
 */

class BookManagerActivity : AppCompatActivity() {

    companion object {
        private val TAG = BookManagerActivity::class.java.simpleName
    }

    private val BOOK_ADD_MSG = 0x001

    private var remoteBookManager: IBookManager? = null

    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                BOOK_ADD_MSG -> Log.e(TAG, "a new book add ：" + msg.obj)
                else -> super.handleMessage(msg)
            }
        }
    }
    private val bookAddListener = object : IBookAddListener.Stub() {
        @Throws(RemoteException::class)
        override fun onBookArrived(newBook: Book) {
            handler.obtainMessage(BOOK_ADD_MSG, newBook).sendToTarget()
        }
    }

    private val deathRecipient: IBinder.DeathRecipient = object : IBinder.DeathRecipient {
        override fun binderDied() {
            if (remoteBookManager == null) {
                return
            }
            remoteBookManager!!.asBinder().unlinkToDeath(this, 0)
            remoteBookManager = null
            val intent = Intent(this@BookManagerActivity, BookManagerService::class.java)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val iBookManager = IBookManager.Stub.asInterface(service)
            try {
                service.linkToDeath(deathRecipient, 0)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            try {
                remoteBookManager = iBookManager
                val list = iBookManager.bookList
                Log.e(TAG, "query book list, type = " + list::class.java.canonicalName)
                Log.e(TAG, list.toString())
                val book = Book(3, "Android软件安全指南")
                remoteBookManager!!.addBook(book)
                remoteBookManager!!.registerListener(bookAddListener)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            remoteBookManager = null
            Log.e(TAG, "binder died ")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_manager)
        val intent = Intent(this, BookManagerService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        unregisterListener()
        unbindService(connection)
        super.onDestroy()
    }

    fun unregisterListener() {
        if (remoteBookManager != null && remoteBookManager!!.asBinder().isBinderAlive) {
            try {
                remoteBookManager!!.unregisterListener(bookAddListener)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }
}

