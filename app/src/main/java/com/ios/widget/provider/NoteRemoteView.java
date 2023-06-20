package com.ios.widget.provider;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ios.widget.R;

public class NoteRemoteView implements RemoteViewsService.RemoteViewsFactory {
//    private final NotesDatabaseHelper helper;
    byte[] bytes;
    int imgOrientionCode;
    private Context context;
    String StrNoteContent;
    private long NoteId;
    String NoteTitle;
    private int TextColor;

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    public NoteRemoteView(Context con, Intent intent) {
//        Note note;
//        NoteId = intent.getLongExtra(Constant.TAG_WIDGET_NOTE_ID, -1);
//        NoteTitle = intent.getStringExtra(Constant.TAG_WIDGET_NOTE_TITLE);
//        StrNoteContent = intent.getStringExtra(Constant.TAG_WIDGET_NOTE_CONTENT);
//        TextColor = intent.getIntExtra(Constant.WIDGET_TEXT_COLOR, -1);
//        context = con;
//        helper = new NotesDatabaseHelper(context);
//        if (NoteId >= 0 && (note = helper.getNoteRecord((int) NoteId)) != null) {
//            bytes = note.getImgByteFormat();
//            imgOrientionCode = note.getImgOrientionCode();
//        }
    }

    @Override
    public void onDestroy() {
        if (context != null) {
            context = null;
        }
    }

    @Override
    public RemoteViews getViewAt(int i) {
//        Note note = helper.getNoteRecord((int) NoteId);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_test);
//        views.removeAllViews(R.id.ll_note_widget_main);
//
//
//        RemoteViews TitleViews = new RemoteViews(context.getPackageName(), R.layout.widget_title_view);
//        if (NoteTitle.equals("")) {
//            NoteTitle = context.getResources().getString(R.string.str_untitled);
//        }
//        SpannableString spanString = new SpannableString(NoteTitle);
//        if (note.isTitleBold()) {
//            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
//        }
//        if (note.isTitleItalic()) {
//            spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
//        }
//        if (note.isTitleUnderline()) {
//            spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
//        }
//        if (note.isTitleStrike()) {
//            spanString.setSpan(new StrikethroughSpan(), 0, spanString.length(), 0);
//        }
//        if (note.getAlign()==1){
//            spanString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL), 0, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }else if (note.getAlign()==2){
//            spanString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }else if (note.getAlign()==3){
//            spanString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE), 0, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        TitleViews.setTextViewText(R.id.tv_note_widget_title, spanString);
//        if (TextColor != -1) {
//            TitleViews.setTextColor(R.id.tv_note_widget_title, context.getResources().getColor(TextColor));
//        }
//        views.addView(R.id.ll_note_widget_main, TitleViews);
//
//
//
//        RemoteViews contentViews = new RemoteViews(context.getPackageName(), R.layout.widget_content_view);
//        SpannableString contentString = new SpannableString(StrNoteContent);
//        if (note.isContentBold()) {
//            contentString.setSpan(new StyleSpan(Typeface.BOLD), 0, contentString.length(), 0);
//        }
//        if (note.isContentItalic()) {
//            contentString.setSpan(new StyleSpan(Typeface.ITALIC), 0, contentString.length(), 0);
//        }
//        if (note.isContentUnderline()) {
//            contentString.setSpan(new UnderlineSpan(), 0, contentString.length(), 0);
//        }
//        if (note.isContentStrike()) {
//            contentString.setSpan(new StrikethroughSpan(), 0, contentString.length(), 0);
//        }
//        if (note.getAlign()==1){
//            contentString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL), 0, contentString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }else if (note.getAlign()==2){
//            contentString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, contentString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }else if (note.getAlign()==3){
//            contentString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE), 0, contentString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        contentViews.setTextViewText(R.id.tv_note_widget_content, contentString);
//        if (TextColor != -1) {
//            contentViews.setTextColor(R.id.tv_note_widget_content, context.getResources().getColor(TextColor));
//        }
//        views.addView(R.id.ll_note_widget_main, contentViews);
//
//
//
//        Bitmap bitmap = Constant.getBitmapOfRotate(bytes, imgOrientionCode);
//        if (bitmap != null) {
//            RemoteViews ImageViews = new RemoteViews(context.getPackageName(), R.layout.widget_image_view);
//            ImageViews.setImageViewBitmap(R.id.img_note_image_attach, Constant.getRoundedCornerOfBitmap(bitmap, 30));
//            views.addView(R.id.ll_note_widget_main, ImageViews);
//        }
//        views.setOnClickFillInIntent(R.id.ll_note_widget_main, new Intent());
        return views;
    }
}
