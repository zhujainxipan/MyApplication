package com.ht.aidldemo.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.ht.aidldemo.Book;
import com.ht.aidldemo.IBookManager;
import com.ht.aidldemo.IOnNewBookArrivedListener;
import com.ht.aidldemo.R;
import com.ht.aidldemo.service.BookManagerService;
import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "BookManagerActivity";

    /**
     * 其实这种方式并不能实现解注册
     */
    private IBookManager mRemoteBookManager;

    private final Handler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            switch (msg.what) {
                case 1:
                    Log.d(TAG, "receive new book:" + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private IOnNewBookArrivedListener mOnNewBookArrvedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            mHandler.obtainMessage(1, newBook).sendToTarget();
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            mRemoteBookManager = bookManager;
            try {
                List<Book> list = bookManager.getBookList();
                Log.d(TAG, "query book list:" + list.toString());
                Book book = new Book(3, "android开发艺术探索");
                bookManager.addBook(book);
                List<Book> newList = bookManager.getBookList();
                Log.d(TAG, "query book list:" + newList.toString());
                bookManager.registerListener(mOnNewBookArrvedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteBookManager = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive()) {
            try {
                mRemoteBookManager.unregisterListener(mOnNewBookArrvedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
