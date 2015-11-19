package com.team.xg.book.greendao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.team.xg.book.greendao.entity.TimeStamp;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TimeStamp".
*/
public class TimeStampDao extends AbstractDao<TimeStamp, Void> {

    public static final String TABLENAME = "TimeStamp";

    /**
     * Properties of entity TimeStamp.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property ModuleName = new Property(0, String.class, "ModuleName", false, "MODULE_NAME");
        public final static Property TimeStamp = new Property(1, Long.class, "TimeStamp", false, "TIME_STAMP");
    };

    private DaoSession daoSession;


    public TimeStampDao(DaoConfig config) {
        super(config);
    }
    
    public TimeStampDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TimeStamp\" (" + //
                "\"MODULE_NAME\" TEXT," + // 0: ModuleName
                "\"TIME_STAMP\" INTEGER);"); // 1: TimeStamp
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TimeStamp\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, TimeStamp entity) {
        stmt.clearBindings();
 
        String ModuleName = entity.getModuleName();
        if (ModuleName != null) {
            stmt.bindString(1, ModuleName);
        }
 
        Long TimeStamp = entity.getTimeStamp();
        if (TimeStamp != null) {
            stmt.bindLong(2, TimeStamp);
        }
    }

    @Override
    protected void attachEntity(TimeStamp entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public TimeStamp readEntity(Cursor cursor, int offset) {
        TimeStamp entity = new TimeStamp( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // ModuleName
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1) // TimeStamp
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, TimeStamp entity, int offset) {
        entity.setModuleName(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setTimeStamp(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(TimeStamp entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(TimeStamp entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
