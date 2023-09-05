package com.test.mylupusproject.ui.data;

import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.test.mylupusproject.R;
import com.test.mylupusproject.ui.adapters.DocumentAdapter;

import javax.annotation.Nonnull;

public class DocumentGroupsFragment extends Fragment {

    private FirestoreRecyclerAdapter<DocumentModel, DocumentAdapter.ViewHolder> recyclerAdapter = null;
    private static final String TAG = "DataListGroups";
    private Context context = null;
    private String queryString = null;
    private DocumentAdapter documentAdapter = null;

    public DocumentGroupsFragment(String queryString) {
        this.queryString = queryString;
    }

    public View onCreateView(@Nonnull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getContext();
        View root = inflater.inflate(R.layout.fragment_document_groups, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.document_group_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        FragmentActivity fragmentActivity = getActivity();
        ExpandHelper expandHelper = new ExpandHelper();
        documentAdapter = new DocumentAdapter(root, context, getChildFragmentManager(), queryString, fragmentActivity, expandHelper);
        recyclerAdapter = documentAdapter.getAdapter("groups");
        recyclerView.setAdapter(recyclerAdapter);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (recyclerAdapter != null) {
            recyclerAdapter.stopListening();
        }
    }
}