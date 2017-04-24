package com.wyz.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangyongzheng on 2017/4/13.
 */

public class Book implements Parcelable{
    public int bookId;
    public String bookName;
    public Book(){

    }
    public Book(int id,String name){
        this.bookId = id ;
        this.bookName = name;

    }

    protected Book(Parcel in) {
        bookId = in.readInt();
        bookName = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookId);
        dest.writeString(bookName);
    }

    @Override
    public String toString() {
        return String.format("[bookId:%s, bookName:%s]", bookId, bookName);
    }
}