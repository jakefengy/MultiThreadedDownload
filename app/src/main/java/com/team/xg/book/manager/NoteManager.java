package com.team.xg.book.manager;

import com.team.xg.book.greendao.dao.NoteDao;
import com.team.xg.book.greendao.entity.Note;

import java.util.List;

/**
 * NoteManager
 */
public class NoteManager {

    public static boolean addNote(NoteDao noteDao, Note note) {

        try {

            noteDao.insert(note);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    public static boolean updateNote(NoteDao noteDao, Note note) {
        try {

            noteDao.update(note);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean deleteNote(NoteDao noteDao, Note note) {
        try {

            noteDao.delete(note);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static List<Note> getNotes(NoteDao noteDao) {

        return noteDao.loadAll();
    }

}
