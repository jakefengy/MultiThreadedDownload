package com.team.xg.book.greendao.dao;

import android.database.sqlite.SQLiteDatabase;

import com.team.xg.book.greendao.entity.Note;
import com.team.xg.book.greendao.entity.TimeStamp;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;



// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig noteDaoConfig;
    private final DaoConfig timeStampDaoConfig;

    private final NoteDao noteDao;
    private final TimeStampDao timeStampDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        noteDaoConfig = daoConfigMap.get(NoteDao.class).clone();
        noteDaoConfig.initIdentityScope(type);

        timeStampDaoConfig = daoConfigMap.get(TimeStampDao.class).clone();
        timeStampDaoConfig.initIdentityScope(type);

        noteDao = new NoteDao(noteDaoConfig, this);
        timeStampDao = new TimeStampDao(timeStampDaoConfig, this);

        registerDao(Note.class, noteDao);
        registerDao(TimeStamp.class, timeStampDao);
    }
    
    public void clear() {
        noteDaoConfig.getIdentityScope().clear();
        timeStampDaoConfig.getIdentityScope().clear();
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public TimeStampDao getTimeStampDao() {
        return timeStampDao;
    }

}