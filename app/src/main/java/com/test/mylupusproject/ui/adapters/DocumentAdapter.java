package com.test.mylupusproject.ui.adapters;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.test.mylupusproject.R;
import com.test.mylupusproject.ui.data.DocumentGroupsFragment;
import com.test.mylupusproject.ui.data.DocumentModel;
import com.test.mylupusproject.ui.data.DocumentValuesFragment;
import com.test.mylupusproject.ui.utils.ListenerHelper;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Random;

public class DocumentAdapter {
    private static final String TAG = "DocumentAdapter";
    private FirestoreRecyclerAdapter<DocumentModel, DocumentAdapter.ViewHolder> documentAdapter;
    private View root = null;
    private View dataListView = null;
    private Context context = null;
    private FragmentManager fragmentManager = null;
    private String queryString = null;
    private String docType = null;
    private boolean opened;
    private FragmentActivity mainFragmentActivity = null;

public DocumentAdapter(View root, Context context, FragmentManager fragmentManager, String queryString, FragmentActivity mainFragmentActivity) {
        this.root = root;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.queryString = queryString;
        this.mainFragmentActivity = mainFragmentActivity;
    }

    public FirestoreRecyclerAdapter<DocumentModel, DocumentAdapter.ViewHolder> getAdapter(String docType) {
        this.docType = docType;
        CollectionReference query = FirebaseFirestore.getInstance().collection(queryString);
        FirestoreRecyclerOptions<DocumentModel> options = new FirestoreRecyclerOptions.Builder<DocumentModel>().setQuery(query, DocumentModel.class).build();
        documentAdapter = new FirestoreRecyclerAdapter<DocumentModel, DocumentAdapter.ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull DocumentModel documentModel) {
                Log.d(TAG, "Binding: " + documentModel.getDocId() + " Position: " + position);
                holder.setListName(documentModel.getDocId());
                holder.updateContainerId();
                holder.setListeners(documentModel);
                holder.setCardColor();

            }

            @NonNull
            @Override
            public DocumentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_lists_card_layout, parent, false);
                return new DocumentAdapter.ViewHolder(view, context);
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

        void setListName(String documentName) {
            this.documentName = documentName;
            TextView textView = view.findViewById(R.id.document_name);
            textView.setText(documentName);
        }

        void setCardColor() {
            View cardView = view.findViewById(R.id.document_cardview);
            View arrowImage = view.findViewById(R.id.arrow_button);
            switch(docType) {
                case "root":
//                    cardView.setBackgroundColor(Color.parseColor("#DBE3E5"));
//                    arrowImage.setBackgroundColor(Color.parseColor("#DBE3E5"));
//                    cardView.setBackgroundColor(Color.parseColor("#DBE3E5"));
//                    arrowImage.setBackgroundColor(Color.parseColor("#DBE3E5"));
//                    cardView.setBackgroundColor(Color.parseColor("#D8D5D2"));
//                    arrowImage.setBackgroundColor(Color.parseColor("#D8D5D2"));
                    break;
                case "group":
                    break;
            }
        }

        void setListeners(@NonNull DocumentModel documentModel) {
            ImageButton arrow = view.findViewById(R.id.arrow_button);
            LinearLayout hiddenView = view.findViewById(R.id.hidden_view);
            String docId = documentModel.getDocId();

            arrow.setOnClickListener(view -> {
                // If the CardView is already expanded, set its visibility
                // to gone and change the expand less icon to expand more.
                if (hiddenView.getVisibility() == View.VISIBLE) {
                    // The transition of the hiddenView is carried out by the TransitionManager class.
                    // Here we use an object of the AutoTransition Class to create a default transition
                    //TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    hiddenView.setVisibility(View.GONE);
                    arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
                }

                // If the CardView is not expanded, set its visibility to visible and change the expand more icon to expand less.
                else {
                    Log.d(TAG, "Document: " + docId + " ContainerId: " + containerId);
                    hiddenView.setVisibility(View.VISIBLE);
                    arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    Fragment fragment = fragmentManager.findFragmentByTag(docId);
                    if (fragment == null) {
                        if (documentModel.getChildrenType().equals("groups")) {
                            fragment = new DocumentGroupsFragment(documentModel.getPath() + "/groups");
                            Log.d(TAG, "Adding new group fragment: " + docId + " ContainerId: " + containerId);
                        } else if (documentModel.getChildrenType().equals("values")) {
                            fragment = new DocumentValuesFragment(documentModel.getPath() + "/values");
                            Log.d(TAG, "Adding new value fragment: " + docId + " ContainerId: " + containerId);
                        }
                        fragmentManager.beginTransaction().add(containerId, fragment, docId).commit();
                    } else {
                      if (!fragment.isVisible()) {
                        Log.d(TAG, "Fragment already exists but not visible: " + docId);
                        fragmentManager.beginTransaction().show(fragment).commit();
                      }
                    }
                }
            });

            listenerHelper = new ListenerHelper(view, mainFragmentActivity, documentModel, context, docType);
            listenerHelper.addMoreButtonClickListners();
        }
    }
}
