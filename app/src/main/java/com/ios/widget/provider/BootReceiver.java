package com.ios.widget.provider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
//    private DatabaseHelper helper;

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AllRestartWidgets(context);
        }
    }

    public void AllRestartWidgets(Context context) {
//        helper = new NotesDatabaseHelper(context);
//        Iterator<Note> it =  helper.getAllWidget(0).iterator();
//        while (it.hasNext()) {
//            Note note = (Note) it.next();
//            NewCreateNotesActivity.NoteUpdateWidget(note.getCreateWidgetId(), context.getPackageName(), context, note.getNoteTitle(), note.getNoteContent(), note.getId());
//        }
    }
}
