package com.test.mylupusproject.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.test.mylupusproject.R;
import com.test.mylupusproject.ui.data.DocumentModel;
import com.test.mylupusproject.ui.utils.ListenerHelper;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Random;

public class DocumentValuesAdapter {
    private static final String TAG = "DocumentValuesAdapter";
    private FirestoreRecyclerAdapter<DocumentModel, DocumentValuesAdapter.ViewHolder> documentAdapter;
    private View root = null;
    private Context context = null;
    private FragmentManager fragmentManager = null;
    private String queryString = null;
    private boolean opened;
    private FragmentActivity mainFragmentActivity = null;

public DocumentValuesAdapter(View root, Context context, FragmentManager fragmentManager, String queryString, FragmentActivity mainFragmentActivity) {
        this.root = root;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.queryString = queryString;
        this.mainFragmentActivity = mainFragmentActivity;
    }

    public FirestoreRecyclerAdapter<DocumentModel, DocumentValuesAdapter.ViewHolder> getAdapter() {
        CollectionReference query = FirebaseFirestore.getInstance().collection(queryString);
        FirestoreRecyclerOptions<DocumentModel> options = new FirestoreRecyclerOptions.Builder<DocumentModel>().setQuery(query, DocumentModel.class).build();
        documentAdapter = new FirestoreRecyclerAdapter<DocumentModel, DocumentValuesAdapter.ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull DocumentModel documentModel) {
                Log.d(TAG, "Binding: " + documentModel.getDocId() + " Position: " + position);
                holder.setItemName(documentModel.getDocId());
                holder.updateContainerId();
                holder.setListeners(documentModel);
            }

            @NonNull
            @Override
            public DocumentValuesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_value_item, parent, false);
                return new DocumentValuesAdapter.ViewHolder(view, context);
            }
        };
        return documentAdapter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private String documentName;
        private Context context;
        private int containerId;
        private Random r;
        private ListenerHelper listenerHelper = null;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            view = itemView;
            this.context = context;
            r = new Random();
        }

        void updateContainerId() {
            FragmentContainerView fragmentContainerView = view.findViewById(R.id.fragment_container);
            if (fragmentContainerView != null) {
                containerId = r.nextInt(1000000);
                while(view.findViewById(containerId) != null) {
                    containerId = r.nextInt(1000000);
                }
                fragmentContainerView.setId(containerId);
                Log.d(TAG, "New ContainerId: " + fragmentContainerView.getId());
            }
        }

        void setItemName(String documentName) {
            this.documentName = documentName;
            TextView textView = view.findViewById(R.id.document_name);
            textView.setText(documentName);
        }

        void setListeners(@NonNull DocumentModel documentModel) {
            listenerHelper = new ListenerHelper(view, mainFragmentActivity, documentModel, context, "Item");
            listenerHelper.addMoreButtonClickListners();
        }
    }
}
