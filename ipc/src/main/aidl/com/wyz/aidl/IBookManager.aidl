package com.wyz.aidl;

import com.wyz.aidl.Book;

interface IBookManager{
List<Book> getBookList();
void addBook(in Book book);


}