// IOnNewBookArrivedListener.aidl
package com.ht.aidldemo;
import com.ht.aidldemo.Book;

// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
