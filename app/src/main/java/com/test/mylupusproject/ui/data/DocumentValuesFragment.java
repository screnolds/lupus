package com.test.mylupusproject.ui.data;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.test.mylupusproject.R;
import com.test.mylupusproject.ui.adapters.DocumentAdapter;
import com.test.mylupusproject.ui.adapters.DocumentValuesAdapter;

import javax.annotation.Nonnull;

public class DocumentValuesFragment extends Fragment {

    private FirestoreRecyclerAdapter<DocumentModel, DocumentValuesAdapter.ViewHolder> recyclerAdapter = null;
    private RecyclerView recyclerView = null;
    private static final String TAG = "DataListValues";
    private Context context = null;
    private String queryString = null;
    private DocumentValuesAdapter documentValuesAdapter = null;

    public DocumentValuesFragment(String queryString) {
        this.queryString = queryString;
    }

    public View onCreateView(@Nonnull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getContext();
        View root = inflater.inflate(R.layout.fragment_document_values, container, false);
        recyclerView = root.findViewById(R.id.document_values_recycler_view);
        ExpandHelper expandHelper = new ExpandHelper();
        //recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setLayoutManager(new LinearLayoutManager(context) {
            @Override
            public void onLayoutCompleted(RecyclerView.State state) {
                //Log.d("DocumentFragment", "onLayoutCompleted: " + state);
                if (expandHelper.getExpandValue() == true) {
                    DocumentValuesAdapter.ViewHolder viewHolder = (DocumentValuesAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(expandHelper.getPosition());
                    Log.d("DocumentFragment", "onLayoutCompleted: ViewHolder ContainerId: " + viewHolder.getContainerId());
                    viewHolder.getListenerHelper().expandCollapseCardView(false);
                    expandHelper.reset();
                }
            }
        });

        FragmentActivity fragmentActivity = getActivity();
        documentValuesAdapter = new DocumentValuesAdapter(root, context, getChildFragmentManager(), queryString, fragmentActivity, expandHelper);
        recyclerAdapter = documentValuesAdapter.getAdapter();
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