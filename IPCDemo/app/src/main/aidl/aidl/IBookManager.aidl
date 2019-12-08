package aidl;

import aidl.Book;
import aidl.IBookAddListener;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IBookAddListener listener);
    void unregisterListener(IBookAddListener listener);
}