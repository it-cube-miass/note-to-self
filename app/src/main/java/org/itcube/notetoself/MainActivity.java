package org.itcube.notetoself;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private JSONSerializer serializer;
    private ArrayList<Note> notes;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private boolean showDividers;
    private SharedPreferences prefs;

    public void createNewNote(Note note) {
        notes.add(note);
        adapter.notifyDataSetChanged();
    }

    public void showNote(int index) {
        DialogShowNote dialog = new DialogShowNote();
        dialog.sendNoteSelected(notes.get(index));
        dialog.show(getSupportFragmentManager(), "");
    }

    public void saveNotes() {
        try {
            serializer.save(notes);
        } catch (Exception e) {
            Log.e("Error saving Notes", "", e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveNotes();
    }

    @Override
    protected void onResume() {
        super.onResume();

        prefs = getSharedPreferences("Note to Self", MODE_PRIVATE);
        showDividers = prefs.getBoolean("dividers", true);

        if (showDividers) {
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        } else {
            if (recyclerView.getItemDecorationCount() > 0) {
                recyclerView.removeItemDecorationAt(0);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogNewNote dialog = new DialogNewNote();
                dialog.show(getSupportFragmentManager(), "");
            }
        });

        serializer = new JSONSerializer("NoteToSelf.json", getApplicationContext());
        try {
            notes = serializer.load();
        } catch (Exception e) {
            notes = new ArrayList<>();
            Log.e("Error loading notes: ", "", e);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new NoteAdapter(this, notes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
