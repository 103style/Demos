package aidl

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import androidx.annotation.Nullable
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author https://github.com/103style
 * @date 2020/4/2 22:32
 */
class BookManagerService : Service() {

    companion object {
        private const val TAG = "BookManagerService"
    }

    //服务是否已经销毁
    private val destroyed = AtomicBoolean(false)
    private val bookLists = CopyOnWriteArrayList<Book>()
    private val listeners = RemoteCallbackList<IBookAddListener>()

    private val mBinder: Binder = object : IBookManager.Stub() {

        @Throws(RemoteException::class)
        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
            val check = checkCallingOrSelfPermission("com.aidl.test.permission.ACCESS_BOOK_SERVICE")
            if (check == PackageManager.PERMISSION_DENIED) {
                Log.e(TAG, "PERMISSION_DENIED")
                return false
            }
            var packageName: String? = null
            val packages = packageManager.getPackagesForUid(Binder.getCallingUid())
            if (packages != null && packages.size > 0) {
                packageName = packages[0]
            }
            if (!packageName!!.startsWith("com.aidl")) {
                Log.e(TAG, "packageName is illeagl = " + packageName)
                return false
            }
            return super.onTransact(code, data, reply, flags)
        }

        @Throws(RemoteException::class)
        override fun getBookList(): List<Book> {
            return bookLists
        }

        @Throws(RemoteException::class)
        override fun addBook(book: Book) {
            bookLists.add(book)
        }

        @Throws(RemoteException::class)
        override fun registerListener(listener: IBookAddListener) {
            listeners.register(listener)
            val N = listeners.beginBroadcast()
            Log.e(TAG, "registerListener: size = " + N)
            listeners.finishBroadcast()
        }

        @Throws(RemoteException::class)
        override fun unregisterListener(listener: IBookAddListener) {
            listeners.unregister(listener)
            val N = listeners.beginBroadcast()
            Log.e(TAG, "unregisterListener: size = " + N)
            listeners.finishBroadcast()
        }
    }

    override fun onCreate() {
        super.onCreate()
        bookLists.add(Book(1, "Android艺术开发探索"))
        bookLists.add(Book(2, "Java并发编程指南"))
        Thread(Worker()).start()
    }

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        val check = checkCallingOrSelfPermission("com.aidl.test.permission.ACCESS_BOOK_SERVICE")
        if (check == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG, "PERMISSION_DENIED")
            return null
        }
        return mBinder
    }

    override fun onDestroy() {
        destroyed.set(true)
        super.onDestroy()
    }

    fun onBookAdd(book: Book) {
        bookLists.add(book)
        val N = listeners.beginBroadcast()
        for (i in 0 until N) {
            val listener = listeners.getBroadcastItem(i)
            if (listener != null) {
                listener.onBookArrived(book)
            }
        }
        listeners.finishBroadcast()
    }

    inner class Worker : Runnable {
        override fun run() {
            while (!destroyed.get()) {
                try {
                    Thread.sleep(5000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                val bookId = bookLists.size + 1
                val book = Book(bookId, "new book#" + bookId)
                try {
                    onBookAdd(book)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }

        }
    }
}