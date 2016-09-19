package com.example.lenovo.myfirstapp.model;

import android.content.Context;

import com.example.lenovo.myfirstapp.db.AcgbaseHelper;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by lenovo on 2016/9/14.
 */
public class BookLab {

    private ArrayList<Book> mBooks;
    private Context mContext;
    private AcgbaseHelper mHelper;

    private static BookLab sBookLab;

    private BookLab(Context context) {
        mContext = context;
        mHelper = new AcgbaseHelper(context);
        try {
            mBooks = queryBooks().getBooks();
        }catch (Exception e) {
            mBooks = new ArrayList<Book>();
        }
    }

    public static BookLab get(Context c) {
        if(sBookLab == null) {
            sBookLab = new BookLab(c.getApplicationContext());
        }
        return sBookLab;
    }

    public ArrayList<Book> getBooks() {
        return mBooks;
    }

    public Book getBook(UUID uuid) {
        for(Book book : mBooks) {
            if(book.getmId().equals(uuid)) {
                return book;
            }
        }
        return null;
    }

    public void addBook(Book book) {
        mBooks.add(book);
        mHelper.insertAcg(book);
    }
    public void updateBookLab(Book book) {
        mHelper.updateAcg(book, book.getmId());
    }

    public void deleteBook(Book book) {
        mBooks.remove(book);
        mHelper.deleteAcg(book, book.getmId());
    }

    public AcgbaseHelper.BookCursor queryBooks() {
        return mHelper.queryBook();
    }
}
