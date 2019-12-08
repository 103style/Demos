package aidl;

import aidl.Book;

interface IBookAddListener{
    void onBookArrived(in Book newBook);
}