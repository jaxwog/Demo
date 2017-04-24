package com.wyz.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangyongzheng on 2017/4/13.
 */

public class UserAndBook implements Parcelable{
    public int userId;
    public String userName;
    public boolean isMale;

    Book book;

    public UserAndBook(int id,String name,boolean b){
        this.userId = id;
        this.userName = name;
        this.isMale = b;
    }





    protected UserAndBook(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        isMale = in.readInt()==1;
        book = in.readParcelable(Thread.currentThread().getContextClassLoader());
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(userId);
        out.writeString(userName);
        out.writeInt(isMale ? 1 : 0);
        out.writeParcelable(book,0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserAndBook> CREATOR = new Creator<UserAndBook>() {
        @Override
        public UserAndBook createFromParcel(Parcel in) {
            return new UserAndBook(in);
        }

        @Override
        public UserAndBook[] newArray(int size) {
            return new UserAndBook[size];
        }
    };




    @Override
    public String toString() {
        return String.format(
                "User:{userId:%s, userName:%s, isMale:%s}, with child:{%s}",
                userId, userName, isMale, book);

    }
}
