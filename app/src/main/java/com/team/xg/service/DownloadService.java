package com.team.xg.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.team.xg.book.greendao.entity.INoteManager;
import com.team.xg.book.greendao.entity.Note;

import java.util.List;

public class DownloadService extends Service {


    private Binder mNoteBinder = new INoteManager.Stub() {
        @Override
        public void addNote(Note note) throws RemoteException {
            com.team.xg.book.manager.GreenManager.getInstance().addNote(note);
        }

        @Override
        public void updateNote(Note note) throws RemoteException {
            com.team.xg.book.manager.GreenManager.getInstance().updateNote(note);
        }

        @Override
        public void deleteNote(Note note) throws RemoteException {
            com.team.xg.book.manager.GreenManager.getInstance().delNote(note);
        }

        @Override
        public List<Note> getNotes() throws RemoteException {
            return com.team.xg.book.manager.GreenManager.getInstance().getNotes();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return null;
        }

        if (bundle.getString("ServiceKey").equals("Note")) {
            return mNoteBinder;
        } else if (bundle.getString("ServiceKey").equals("Book")) {
            return null;
        } else {
            return null;
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();

        com.team.xg.book.manager.GreenManager.getInstance().initGreenDao(getApplicationContext(), "GreenTest.db");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
