package com.team.xg.book.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.team.xg.Constant;
import com.team.xg.book.greendao.dao.DaoMaster;
import com.team.xg.book.greendao.dao.DaoSession;
import com.team.xg.book.greendao.entity.Note;

import java.io.File;
import java.util.List;

/**
 *
 */
public class GreenManager {

    private static class GreenManagerHolder {

        static final GreenManager INSTANCE = new GreenManager();

    }

    private GreenManager() {

    }

    public static GreenManager getInstance() {

        return GreenManagerHolder.INSTANCE;
    }

    private final static String mDBPath = Constant.appSdPath + "db/";

    private DaoSession mDaoSession;

    public void initGreenDao(Context context, String dbName) {

        if (TextUtils.isEmpty(dbName)) {
            throw new NullPointerException("dbName is empty when GreenManager.initGreenDao");
        }

        File dbFile = new File(mDBPath + dbName);
        if (!dbFile.getParentFile().exists()) {
            dbFile.getParentFile().mkdirs();
        }

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, dbFile.getPath(), null);
        SQLiteDatabase db = helper.getWritableDatabase();

        mDaoSession = new DaoMaster(db).newSession();

    }

    // Note Manager Begin

    public boolean addNote(Note note) {
        return NoteManager.addNote(mDaoSession.getNoteDao(), note);
    }

    public boolean updateNote(Note note) {
        return NoteManager.updateNote(mDaoSession.getNoteDao(), note);
    }

    public boolean delNote(Note note) {
        return NoteManager.deleteNote(mDaoSession.getNoteDao(), note);
    }

    public List<Note> getNotes() {
        return NoteManager.getNotes(mDaoSession.getNoteDao());
    }

    // Note Manager End

}
