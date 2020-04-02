package aidl

import android.os.Parcel
import android.os.Parcelable

/**
 * @author https://github.com/103style
 * @date 2020/4/2 22:09
 */
class Book(var bookId: Int = 0, var bookName: String? = null) : Parcelable {

    constructor(parcel: Parcel) : this() {
        bookId = parcel.readInt()
        bookName = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(bookId)
        parcel.writeString(bookName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Book{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                '}'
    }


}