package com.team.xg.book.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.team.xg.LogUtils;
import com.team.xg.R;
import com.team.xg.book.greendao.entity.INoteManager;
import com.team.xg.book.greendao.entity.Note;
import com.team.xg.service.DownloadService;

import java.util.Date;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        connService();
        add();
        update();
        del();
        getAll();

    }

    private void connService() {
        Intent intent = new Intent(NoteActivity.this, DownloadService.class);
        Bundle bundle = new Bundle();
        bundle.putString("ServiceKey", "Note");
        intent.putExtras(bundle);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        LogUtils.i("NoteActivity connService . ");
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iNoteManager = INoteManager.Stub.asInterface(service);
            LogUtils.i("NoteActivity onServiceConnected . ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iNoteManager = null;
            LogUtils.i("NoteActivity onServiceDisconnected . ");
        }
    };

    private INoteManager iNoteManager = null;
    long count = 0;
    long time = 1447234069;

    private void add() {
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iNoteManager == null) {
                    return;
                }

                Note note = new Note();
                note.setId((long) 0);
                note.setText("note index " + note.getId());
                note.setComment("remark");
                note.setDate(new Date(time));
                try {
                    iNoteManager.addNote(note);
                    LogUtils.i("NoteActivity add . ");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void update() {
        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iNoteManager == null) {
                    return;
                }
                try {
                    List<Note> notes = iNoteManager.getNotes();
                    if (notes != null && notes.size() > 0) {
                        Note note = notes.get(notes.size() - 1);
                        note.setComment("修改了…………………………");
                        iNoteManager.updateNote(note);
                        LogUtils.i("NoteActivity update . ");
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void del() {
        findViewById(R.id.del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iNoteManager == null) {
                    return;
                }

                try {
                    List<Note> notes = iNoteManager.getNotes();
                    if (notes != null && notes.size() > 0) {
                        Note note = notes.get(notes.size() - 1);
                        iNoteManager.deleteNote(note);
                        LogUtils.i("NoteActivity del . ");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void getAll() {
        findViewById(R.id.get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iNoteManager == null) {
                    return;
                }

                try {
                    List<Note> notes = iNoteManager.getNotes();
                    if (notes != null)
                        LogUtils.i("NoteActivity getAll . result is " + notes.size());

                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        LogUtils.i("NoteActivity onDestroy . ");
        unbindService(conn);
        super.onDestroy();
    }
}
