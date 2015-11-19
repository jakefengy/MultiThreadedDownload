// INoteManager.aidl
package com.team.xg.book.greendao.entity;

// Declare any non-default types here with import statements
import com.team.xg.book.greendao.entity.Note;

interface INoteManager {

    void addNote(in Note note);
    void updateNote(in Note note);
    void deleteNote(in Note note);
    List<Note> getNotes();

}
