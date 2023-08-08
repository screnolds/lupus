package com.test.mylupusproject.ui.data;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.test.mylupusproject.R;
import com.test.mylupusproject.ui.adapters.DocumentValuesAdapter;

import javax.annotation.Nonnull;

public class DocumentValuesFragment extends Fragment {

    private FirestoreRecyclerAdapter<DocumentModel, DocumentValuesAdapter.ViewHolder> recyclerAdapter = null;
    private static final String TAG = "DataListValues";
    private Context context = null;
    private String queryString = null;

    public DocumentValuesFragment(String queryString) {
        this.queryString = queryString;
    }

    public View onCreateView(@Nonnull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getContext();
        View root = inflater.inflate(R.layout.fragment_document_values, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.document_values_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        FragmentActivity fragmentActivity = getActivity();
//        BottomNavigationView navBar = getActivity().findViewById(R.id.nav_view);
//        recyclerAdapter = new DocumentValuesAdapter(root, context, getChildFragmentManager(), queryString, navBar).getAdapter();
        recyclerAdapter = new DocumentValuesAdapter(root, context, getChildFragmentManager(), queryString, fragmentActivity).getAdapter();
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