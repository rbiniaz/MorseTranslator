package com.mobpro.wolfpack.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView title = (TextView) findViewById(R.id.message_edit_text);
        Button save = (Button)findViewById(R.id.go_button);
        final ListView notes = (ListView) findViewById(R.id.message_list);

        final NotesDbAdapter dbAdapter = new NotesDbAdapter(this);
        dbAdapter.open();

        final NotesListCursorAdapter adapter = new NotesListCursorAdapter(this, dbAdapter.getAllNotes(), dbAdapter);
        notes.setAdapter(adapter);



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = title.getText().toString();
                if (fileName != null){
                    dbAdapter.createNote(fileName);
                    adapter.changeCursor(dbAdapter.getAllNotes());
                    title.setText("");
                }
            }
        });

        save.setFocusable(false);

        notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note note  = ((NotesListCursorAdapter.ViewHolder) view.getTag()).note;
                Intent in = new Intent(getApplicationContext(), NoteDetailActivity.class);
                in.putExtra("name", note.getName());
                startActivity(in);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
