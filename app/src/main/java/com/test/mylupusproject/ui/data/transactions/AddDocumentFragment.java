package com.test.mylupusproject.ui.data.transactions;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.mylupusproject.R;
import com.test.mylupusproject.ui.adapters.DocumentAdapter;
import com.test.mylupusproject.ui.data.DocumentModel;
import com.test.mylupusproject.ui.utils.ListenerHelper;

public class AddDocumentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context = null;

    public AddDocumentFragment() {
        // Required empty public constructor
    }

    public static AddDocumentFragment newInstance() {
        AddDocumentFragment fragment = new AddDocumentFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = this.getContext();
        View root = inflater.inflate(R.layout.fragment_add_document, container, false);
        FragmentActivity fragmentActivity = getActivity();
//        ListenerHelper listenerHelper = new ListenerHelper(root, fragmentActivity, new DocumentModel(), context, "Group");
//        listenerHelper.setAddDocumentFragmentListener(getParentFragmentManager(), this);
        return root;

    }
}