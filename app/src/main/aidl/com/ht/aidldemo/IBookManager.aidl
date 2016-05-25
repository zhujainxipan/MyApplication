package com.ht.aidldemo;
import com.ht.aidldemo.Book;
import com.ht.aidldemo.IOnNewBookArrivedListener;

interface IBookManager {
List<Book> getBookList();
void addBook(in Book book);
void registerListener(IOnNewBookArrivedListener listener);
void unregisterListener(IOnNewBookArrivedListener listener);
}