package com.example.lenovo.myfirstapp.model;

import android.content.Context;

import com.example.lenovo.myfirstapp.db.AcgbaseHelper;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by lenovo on 2016/8/24.
 */
public class PvcLab {

    private ArrayList<Pvc> mPvcs;

    private static PvcLab sPvcLab;
    private Context mContext;

    private AcgbaseHelper mHelper;

    private PvcLab(Context Context) {
        mContext = Context;
        mHelper = new AcgbaseHelper(Context);
        try {
            mPvcs = queryPvcs().getPvc();
        }catch (Exception e) {
            mPvcs = new ArrayList<Pvc>();
        }
    }

    public static PvcLab get(Context c) {
        if(sPvcLab == null) {
            sPvcLab = new PvcLab(c.getApplicationContext());
        }
        return sPvcLab;
    }

    public ArrayList<Pvc> getmPvcs() {
        return mPvcs;
    }

    public Pvc getPvc(UUID uuid) {
        for(Pvc c : mPvcs) {
            if(c.getmId().equals(uuid)) {
                return c;
            }
        }
        return null;
    }

    public void addPvc(Pvc pvc) {
        mPvcs.add(pvc);
        mHelper.insertAcg(pvc);
    }

    public void updatePvcLab(Pvc pvc) {
        mHelper.updateAcg(pvc, pvc.getmId());
    }

    public void deletePvcLab(Pvc pvc) {
        mPvcs.remove(pvc);
        mHelper.deleteAcg(pvc, pvc.getmId());
    }

    public AcgbaseHelper.PvcCursor queryPvcs() {
        return mHelper.queryPvc();
    }
}


