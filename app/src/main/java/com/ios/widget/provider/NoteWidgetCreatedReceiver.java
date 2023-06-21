package com.ios.widget.provider;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ios.widget.ui.Activity.WidgetItemActivity;
import com.ios.widget.utils.Constants;

public class NoteWidgetCreatedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int i;
        if ((i = Constants.Widget_Id) > 0) {
            System.out.println("******* iii : "+i);
            System.out.println("******* intent: "+ intent.getIntExtra(Constants.TAG_WIDGET_NOTE_ID, -1));
//            NotesDatabaseHelper helper=new NotesDatabaseHelper(context);
//            Note notes = helper.getNoteRecord(intent.getIntExtra(Constant.TAG_WIDGET_NOTE_ID, 0));
//            notes.setCreateWidgetId(i);
//            helper.updateNotes(notes);
            WidgetItemActivity.UpdateWidget(i, context.getPackageName(), context,  intent.getIntExtra(Constants.TAG_WIDGET_NOTE_ID, -1));
//            Toast.makeText(context, context.getResources().getString(R.string.str_widget_notify_success_created_msg), Toast.LENGTH_SHORT).show();
        }
    }
}
