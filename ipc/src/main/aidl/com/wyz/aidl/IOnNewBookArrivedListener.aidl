package com.wyz.aidl;

import com.wyz.aidl.Book;

interface IOnNewBookArrivedListener{
  void onNewBookArrived(in Book newBook);
}