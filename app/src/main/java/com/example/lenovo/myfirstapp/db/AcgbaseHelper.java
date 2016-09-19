package com.example.lenovo.myfirstapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lenovo.myfirstapp.model.Acg;
import com.example.lenovo.myfirstapp.model.Book;
import com.example.lenovo.myfirstapp.model.Pvc;
import com.example.lenovo.myfirstapp.model.UseDate;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by lenovo on 2016/9/4.
 */
public class AcgbaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_PVC = "create table pvc ("
            + "id text primary key, "
            + "name text, "
            + "startmoney real, "
            + "finalmoney real, "
            + "factory text, "
            + "explain text, "
            + "type integer, "
            + "ispay integer, "
            + "havaimage integer,"
            + "imagepath text, "
            + "year integer, "
            + "month integer)";

    private static final String CREATE_BOOK = "create table book ("
            + "id text primary key, "
            + "name text, "
            + "startmoney real, "
            + "finalmoney real, "
            + "factory text, "
            + "type integer, "
            + "ispay integer, "
            + "havaimage integer,"
            + "imagepath text, "
            + "year integer, "
            + "month integer)";

    private static final String DB_NAME = "acgs.sqlite";
    private static final int VERSION = 1;
    
    private static final String  TABLE_PVC = "pvc";
    private static final String  TABLE_BOOK = "book";
    
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_STARTMONEY = "startmoney";
    private static final String COLUMN_FINALMONEY = "finalmoney";
    private static final String COLUMN_FACTORY = "factory";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_ISPAY = "ispay";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_MONTH = "month";
    private static final String COLUMN_HAVEIMAGE = "havaimage";
    private static final String COLUMN_IMAGEPATH = "imagepath";

    private static final String COLUMN_PVC_EXPLAIN = "explain";

    
    public AcgbaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PVC);
        sqLiteDatabase.execSQL(CREATE_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long insertAcg(Acg acg) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, acg.getmId().toString());
        cv.put(COLUMN_NAME, acg.getmName());
        
        cv.put(COLUMN_STARTMONEY, acg.getmStartMoney());
        cv.put(COLUMN_FINALMONEY, acg.getmFinalMoney());
        cv.put(COLUMN_FACTORY, acg.getFactory());
        cv.put(COLUMN_TYPE, acg.getmType());
        cv.put(COLUMN_ISPAY, acg.ismIspay());
        cv.put(COLUMN_YEAR, acg.getmDate().getYear());
        cv.put(COLUMN_MONTH, acg.getmDate().getMonth());
        cv.put(COLUMN_HAVEIMAGE, acg.getmHaveImage());
        cv.put(COLUMN_IMAGEPATH, acg.getmImagePath());
        
        switch (acg.getTag()) {
            case "Book" :
                return getWritableDatabase().insert(TABLE_BOOK, null, cv);
            case "Pvc" :
                Pvc c = (Pvc) acg;
                cv.put(COLUMN_PVC_EXPLAIN, c.getmExplain());
                return getWritableDatabase().insert(TABLE_PVC, null, cv);
        }
        return 0;
    }

    public  int updateAcg(Acg acg , UUID uuid) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, acg.getmId().toString());
        cv.put(COLUMN_NAME, acg.getmName());
        cv.put(COLUMN_STARTMONEY, acg.getmStartMoney());
        cv.put(COLUMN_FINALMONEY, acg.getmFinalMoney());
        cv.put(COLUMN_FACTORY, acg.getFactory());
        cv.put(COLUMN_TYPE, acg.getmType());
        cv.put(COLUMN_ISPAY, acg.ismIspay());
        cv.put(COLUMN_YEAR, acg.getmDate().getYear());
        cv.put(COLUMN_MONTH, acg.getmDate().getMonth());
        cv.put(COLUMN_HAVEIMAGE, acg.getmHaveImage());
        cv.put(COLUMN_IMAGEPATH, acg.getmImagePath());

        switch (acg.getTag()) {
            case "Book" :
                return getWritableDatabase().update(TABLE_BOOK, cv, "id = ?", new String[] {uuid.toString()});
            case "Pvc" :
                Pvc c = (Pvc) acg;
                cv.put(COLUMN_PVC_EXPLAIN, c.getmExplain());
                return getWritableDatabase().update(TABLE_PVC, cv, "id = ?", new String[] {uuid.toString()});
        }
       return 0;
    }

    public  int deleteAcg(Acg acg, UUID uuid) {
        switch (acg.getTag()) {
            case "Book" :
                return getWritableDatabase().delete(TABLE_BOOK, "id = ?", new String[] {uuid.toString()});
            case "Pvc" :
                return getWritableDatabase().delete(TABLE_PVC, "id = ?", new String[] {uuid.toString()});
        }
        return 0;
    }

    public PvcCursor queryPvc() {
        Cursor wrapped1 = getReadableDatabase().query(TABLE_PVC, null, null, null, null, null, null);
        return new PvcCursor(wrapped1);
        }

    public BookCursor queryBook() {
        Cursor wrapped = getReadableDatabase().query(TABLE_BOOK, null, null, null, null, null, null);
        return new BookCursor(wrapped);
    }

    public static class PvcCursor extends CursorWrapper {

        public PvcCursor(Cursor cursor) {
            super(cursor);
        }

        public ArrayList<Pvc> getPvc() {
            ArrayList<Pvc> list = new ArrayList<Pvc>();
            if (moveToFirst()) {
                do {
                    Pvc pvc = new Pvc();
                    pvc.setmId(UUID.fromString(getString(getColumnIndex(COLUMN_ID))));
                    pvc.setmName(getString(getColumnIndex(COLUMN_NAME)));
                    pvc.setmStartMoney(getFloat(getColumnIndex(COLUMN_STARTMONEY)));
                    pvc.setmFinalMoney(getFloat(getColumnIndex(COLUMN_FINALMONEY)));
                    pvc.setmType(getInt(getColumnIndex(COLUMN_TYPE)));
                    pvc.setFactory(getString(getColumnIndex(COLUMN_FACTORY)));
                    pvc.setmExplain(getString(getColumnIndex(COLUMN_PVC_EXPLAIN)));
                    pvc.setmIspay(getInt(getColumnIndex(COLUMN_ISPAY)) == 1);
                    UseDate useDate = new UseDate();
                    useDate.setYear(getInt(getColumnIndex(COLUMN_YEAR)));
                    useDate.setMonth(getInt(getColumnIndex(COLUMN_MONTH)));
                    pvc.setmDate(useDate);
                    pvc.setmHaveImage(getInt(getColumnIndex(COLUMN_HAVEIMAGE)) == 1);
                    pvc.setmImagePath(getString(getColumnIndex(COLUMN_IMAGEPATH)));
                    list.add(pvc);
                }while (moveToNext());
            }
            return list;
        }
    }

    public static class BookCursor extends CursorWrapper {

        public BookCursor(Cursor cursor) {
            super(cursor);
        }

        public ArrayList<Book> getBooks() {
            ArrayList<Book> list = new ArrayList<Book>();
            if (moveToFirst()) {
                do {
                    Book book = new Book();
                    book.setmId(UUID.fromString(getString(getColumnIndex(COLUMN_ID))));
                    book.setmName(getString(getColumnIndex(COLUMN_NAME)));
                    book.setmStartMoney(getFloat(getColumnIndex(COLUMN_STARTMONEY)));
                    book.setmFinalMoney(getFloat(getColumnIndex(COLUMN_FINALMONEY)));
                    book.setmType(getInt(getColumnIndex(COLUMN_TYPE)));
                    book.setFactory(getString(getColumnIndex(COLUMN_FACTORY)));
                    book.setmIspay(getInt(getColumnIndex(COLUMN_ISPAY)) == 1);
                    UseDate useDate = new UseDate();
                    useDate.setYear(getInt(getColumnIndex(COLUMN_YEAR)));
                    useDate.setMonth(getInt(getColumnIndex(COLUMN_MONTH)));
                    book.setmDate(useDate);
                    book.setmHaveImage(getInt(getColumnIndex(COLUMN_HAVEIMAGE)) == 1);
                    book.setmImagePath(getString(getColumnIndex(COLUMN_IMAGEPATH)));
                    list.add(book);
                }while (moveToNext());
            }
            return list;
        }
    }
}
