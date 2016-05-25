package com.ht.aidldemo.client;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.ht.aidldemo.Book;

import java.util.List;

/**
 * Created by niehongtao on 16/5/25.
 * 暂时不用
 */
public interface IBookManager2 extends IInterface {
    /**
     * binder的唯一标识
     */
    static final String DESCRIPTOR = "com.ryg.chapter_2.manualbinder.IBookManager";

    static final int TRANSACTION_getBookList = (IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_addBook = (IBinder.FIRST_CALL_TRANSACTION + 1);

    public List<Book> getBookList() throws RemoteException;

    public void addBook(Book book) throws RemoteException;


}
