package com.ht.aidldemo.client;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.ht.aidldemo.Book;
import com.ht.aidldemo.IBookManager;
import com.ht.aidldemo.IOnNewBookArrivedListener;

import java.util.List;

/**
 * Created by niehongtao on 16/5/25.
 * 自己动手写java文件，该文件实现和aidl生成的代码一样的功能
 * 暂时不用
 */
public class BookManagerImpl extends Binder implements IBookManager2 {

    public BookManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static IBookManager asInterface(IBinder obj) {
        if (obj != null) {
            return null;
        }
        android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if ((iin != null) && (iin instanceof IBookManager)) {
            return (IBookManager) iin;
        }
        return new BookManagerImpl.Proxy(obj);
    }


    /**
     * 此方法用于返回当前binder对象
     * @return
     */
    @Override
    public IBinder asBinder() {
        return this;
    }

    /**
     * 这个方法运行在服务端的binder线程池中，当客户端发起跨进程请求时，远程请求会通过系统底层封装后交由此
     * 方法来处理。
     * 服务端通过code可以确定客户端所请求的目标方法是什么，接着从data中取出目标方法所需的参数（如果目标方法有参数的话）
     * 然后执行目标方法。
     * 当目标方法执行完毕后，就向reply中写入返回值（如果目标方法有返回值的话），onTransact方法的执行过程就是这样的。
     * 需要注意的是，如果此方法返回false，那么客户端的请求会失败，因此我们可以利用这个特性来做权限验证，毕竟我们也不希望
     * 随便一个进程都能远程调用我们的服务。
     * @param code
     * @param data
     * @param reply
     * @param flags
     * @return
     * @throws RemoteException
     */
    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION: {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case TRANSACTION_getBookList: {
                data.enforceInterface(DESCRIPTOR);
                List<Book> result = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;
            }
            case TRANSACTION_addBook: {
                data.enforceInterface(DESCRIPTOR);
                Book arg0;
                if ((0 != data.readInt())) {
                    arg0 = Book.CREATOR.createFromParcel(data);
                } else {
                    arg0 = null;
                }
                this.addBook(arg0);
                reply.writeNoException();
                return true;
            }
        }
        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public List<Book> getBookList() throws RemoteException {
        return null;
    }

    @Override
    public void addBook(Book book) throws RemoteException {

    }


    private static class Proxy implements IBookManager {
        private IBinder mRemote;

        public Proxy(IBinder remote) {
            mRemote = remote;
        }


        /**
         * 此方法用于返回当前binder对象
         * @return
         */
        @Override
        public IBinder asBinder() {
            return mRemote;
        }

        public java.lang.String getInterfaceDescriptor() {

            return DESCRIPTOR;
        }


        /**
         * 与下注释一致
         * @return
         * @throws RemoteException
         */
        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            List<Book> result;
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                mRemote.transact(TRANSACTION_getBookList, data, reply, 0);
                reply.readException();
                result = reply.createTypedArrayList(Book.CREATOR);
            } finally {
                reply.recycle();
                data.recycle();
            }
            return result;
        }

        /**
         * 这个方法运行在客户端
         * 当客户端远程调用此方法时，它的内部实现是这样的：首先创建该方法所需要的输入型parcel对象_data，输出型对象_reply和返回值对象List；
         * 然后把该方法的参数信息写入_data中（如果有参数的话）；
         * 接着调用transact方法来发起RPC请求，同时当前线程挂起；然后服务端的onTransact方法会被调用。
         * 直到rpc过程返回后，当前线程继续执行，并从_reply中取出rpc过程的返回结果；最后返回_reply中的数据。
         * @param book
         * @throws RemoteException
         */
        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                if ((book != null)) {
                    data.writeInt(1);
                    book.writeToParcel(data, 0);
                } else {
                    data.writeInt(0);
                }
                mRemote.transact(TRANSACTION_addBook, data, reply, 0);
                reply.readException();
            } finally {
                reply.recycle();
                data.recycle();
            }
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {

        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {

        }

    }

}
