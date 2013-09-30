package com.mobpro.wolfpack.lab3;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by rachel on 9/18/13.
 */
public class NotesListCursorAdapter extends CursorAdapter {
    private final LayoutInflater inflater;
    private final NotesDbAdapter dbAdapter;

    public NotesListCursorAdapter(Context context, Cursor c, NotesDbAdapter dbAdapter) {
        super(context, c, true);
        inflater= LayoutInflater.from(context);
        this.dbAdapter = dbAdapter;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.note_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder holder;
        holder = new ViewHolder(NotesDbAdapter.noteFromCursor(cursor), (TextView) view.findViewById(R.id.titleTextView), (ImageButton) view.findViewById(R.id.deleteButton));
        view.setTag(holder);

        holder.titleTextView.setText(holder.note.getName());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbAdapter.deleteNote(holder.note);
                NotesListCursorAdapter.this.changeCursor(dbAdapter.getAllNotes());
            }
        });
    }

    public class ViewHolder{
        Note note;
        TextView titleTextView;
        ImageButton deleteButton;

        private ViewHolder(Note note, TextView titleTextView, ImageButton deleteButton) {
            this.note = note;
            this.titleTextView = titleTextView;
            this.deleteButton = deleteButton;
        }
    }
}