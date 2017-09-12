package com.wyz.aidl;

import com.wyz.aidl.Book;
import com.wyz.aidl.IOnNewBookArrivedListener;

interface IBookManager{
    List<Book> getBookList();
    void addBook(in Book book);

    void registerListener(IOnNewBookArrivedListener listener);

    void unregisterListener(IOnNewBookArrivedListener listener);


}