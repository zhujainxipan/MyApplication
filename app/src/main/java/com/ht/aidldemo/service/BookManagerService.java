package com.ht.aidldemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import com.ht.aidldemo.Book;
import com.ht.aidldemo.IBookManager;
import com.ht.aidldemo.IOnNewBookArrivedListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by niehongtao on 16/5/25.
 */
public class BookManagerService extends Service {
    private static final String TAG = "BMS";
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListeners = new CopyOnWriteArrayList<>();
    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if (!mListeners.contains(listener)) {
                mListeners.add(listener);
            }
            Log.d(TAG, "registerListener,size:" + mListeners.size());
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if (mListeners.contains(listener)) {
                mListeners.remove(listener);
            }
            Log.d(TAG, "registerListener,size:" + mListeners.size());
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "android"));
        mBookList.add(new Book(2, "ios"));
        new Thread(new ServiceWorker()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }



    private class ServiceWorker implements Runnable {

        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new book" + bookId);
                onNewBookArrived(newBook);
            }


        }
    }

    private void onNewBookArrived(Book newBook) {
        mBookList.add(newBook);
        for (int i = 0; i < mBookList.size(); i++) {
            IOnNewBookArrivedListener listener = mListeners.get(i);
            try {
                listener.onNewBookArrived(newBook);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
