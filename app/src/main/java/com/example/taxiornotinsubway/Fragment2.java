package com.example.taxiornotinsubway;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxiornotinsubway.database.DatabaseHelper;
import com.example.taxiornotinsubway.database.model.Note;

import java.util.ArrayList;
import java.util.List;


public class Fragment2 extends Fragment implements FragmentLifecycle{

    private NotesAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();
    private RecyclerView recyclerView;
    public TextView noNotesView;
    private DatabaseHelper db;
    private TextView test_btn;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment2 , container, false);
        recyclerView = view.findViewById(R.id.recycler_view2);
         noNotesView = view.findViewById(R.id.empty_notes_view2);
        test_btn = view.findViewById(R.id.test_btn);

        db = new DatabaseHelper(getActivity());
        notesList.addAll(db.getAllHistory());

        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long id = db.insertNote("history","혜화","강남","37.582191", "127.001915", "37.500628","127.036392");
                Note n = db.getNote(id);

                if (n != null) {
                    // adding new note to array list at 0 position
                    notesList.add(0, n);
                }
                toggleEmptyNotes();
                mAdapter.notifyDataSetChanged();
            }
        });

        mAdapter = new NotesAdapter(getActivity(), notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        toggleEmptyNotes();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.d("skku", "Fragment2 is visible.");
            notesList.clear();
            notesList.addAll(db.getAllHistory());
            toggleEmptyNotes();

        }        return;

    }


    public void toggleEmptyNotes() {
        Log.d("skku","toggle History column: "+db.getNotesCountH());
        if (db.getNotesCountH() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onPauseFragment() {
        Log.d("ss", "onPauseFragment()2");
        Toast.makeText(getActivity(), "onPauseFragment():" + "ss", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResumeFragment() {
        Log.d("ss", "onResumeFragment()2");
        Toast.makeText(getActivity(), "onResumeFragment():" + "ss", Toast.LENGTH_SHORT).show();
    }
}
