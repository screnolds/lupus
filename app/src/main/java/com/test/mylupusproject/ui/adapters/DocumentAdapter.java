package com.test.mylupusproject.ui.adapters;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

public class DocumentAdapter {
    private static final String TAG = "DocumentAdapter";
    private FirestoreRecyclerAdapter<DocumentModel, DocumentAdapter.ViewHolder> documentAdapter;
    private View root = null;
    private Context context = null;
    private FragmentManager fragmentManager = null;
    private String queryString = null;
    private String docType = null;
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
        FirestoreRecyclerOptions<DocumentModel> options = new FirestoreRecyclerOptions.Builder<DocumentModel>().setQuery(query.orderBy("name"), DocumentModel.class).build();
        documentAdapter = new FirestoreRecyclerAdapter<DocumentModel, DocumentAdapter.ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull DocumentModel documentModel) {
                Log.d(TAG, "Binding: " + documentModel.getName() + " Position: " + position);
                holder.togglePlacholderView(documentModel.getName());
                holder.setListName(documentModel.getName());
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
            listenerHelper = new ListenerHelper(root, view, mainFragmentActivity, documentModel, context, docType, containerId, fragmentManager);
            listenerHelper.addArrowButtonClickListeners();
            listenerHelper.addMoreButtonClickListeners();
            listenerHelper.addLessButtonClickListeners();
            listenerHelper.addEditTextDoneListener();
        }

        void togglePlacholderView(String documentName) {
            TextView textView = view.findViewById(R.id.document_name);
            EditText editText = view.findViewById(R.id.document_name_edit);
            if (documentName.equals("AAAAAAAA")) {
                if (textView.getVisibility() == View.VISIBLE) {
                    textView.setVisibility(View.GONE);
                }
                if (editText.getVisibility() == View.GONE) {
                    editText.setVisibility(View.VISIBLE);
                }
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                if (!imm.isAcceptingText()) {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            } else {
                if (textView.getVisibility() == View.GONE) {
                    textView.setVisibility(View.VISIBLE);
                }
                if (editText.getVisibility() == View.VISIBLE) {
                    editText.setVisibility(View.GONE);
                }
            }
        }
    }
}
