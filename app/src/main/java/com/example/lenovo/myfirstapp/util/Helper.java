package com.example.lenovo.myfirstapp.util;

import com.example.lenovo.myfirstapp.model.Acg;
import com.example.lenovo.myfirstapp.model.Book;
import com.example.lenovo.myfirstapp.model.Pvc;
import com.example.lenovo.myfirstapp.model.UseDate;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/9/9.
 */
public class Helper {

    public static final int PVC = 0;
    public static final int BOOK = 1;
    public static final int OTHER = 2;

    public  <E extends Acg> float getAllMoney(ArrayList<E> arrayList, UseDate date, int TAG) {
        int year = date.getYear();
        int month = date.getMonth();
        float allMoney = 0;
        switch (TAG) {
            case PVC :
                for (E e : arrayList) {
                    Pvc pvc = (Pvc) e;
                    if(year == pvc.getmDate().getYear()
                            && month == pvc.getmDate().getMonth()
                            && (!pvc.ismIspay())) {
                        allMoney = allMoney + pvc.getmFinalMoney();
                    }
                }
                break;
            case BOOK :
                for (E e : arrayList) {
                    Book book = (Book) e;
                    if(year == book.getmDate().getYear()
                            && month == book.getmDate().getMonth()
                            && (!book.ismIspay())) {
                        allMoney = allMoney + book.getmFinalMoney();
                    }
                }
                break;
            case OTHER :
                break;
        }
        return allMoney;
    }

}
