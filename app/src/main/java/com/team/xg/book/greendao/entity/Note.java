package com.team.xg.book.greendao.entity;


// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.team.xg.book.greendao.dao.DaoSession;
import com.team.xg.book.greendao.dao.NoteDao;

import java.util.Date;

import de.greenrobot.dao.DaoException;

/**
 * Entity mapped to table "NOTE".
 */
public class Note implements Parcelable {

    private Long id;
    /**
     * Not-null value.
     */
    private String text;
    private String comment;
    private java.util.Date date;

    /**
     * Used to resolve relations
     */
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    private transient NoteDao myDao;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Note() {
    }

    public Note(Long id) {
        this.id = id;
    }

    public Note(Long id, String text, String comment, java.util.Date date) {
        this.id = id;
        this.text = text;
        this.comment = comment;
        this.date = date;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNoteDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Not-null value.
     */
    public String getText() {
        return text;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setText(String text) {
        this.text = text;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    /**
     * Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context.
     */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context.
     */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context.
     */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(text);
        dest.writeString(comment);
        dest.writeLong(date.getTime());
    }

    public final static Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    private Note(Parcel source) {
        id = source.readLong();
        text = source.readString();
        comment = source.readString();
        date = new Date(source.readLong());
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    // KEEP METHODS END

}