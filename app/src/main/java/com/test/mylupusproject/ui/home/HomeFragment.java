    package com.test.mylupusproject.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.test.mylupusproject.R;
import com.test.mylupusproject.ui.bottomsheet.BottomSheet;


import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

    public class HomeFragment extends Fragment {

    private static final String TAG = "HOME";
    //private HomeViewModel homeViewModel;
    Connection connect;
    String ConnectionResult="";
    @SuppressLint("NewApi")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.pain_type);
        AutoCompleteTextView autoCompleteTextView = root.findViewById(R.id.autoCompleteTextView);
        //String[] Subjects = new String[]{"Android", "Flutter", "React Native"};
        List<String> subjects = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), R.layout.dropdown_item, subjects);
        autoCompleteTextView.setAdapter(adapter);
        //GetTextFromSQL(textView);
        //new HttpGetTask("https://us-west4-mylupusproject.cloudfunctions.net/pain?date=today",textView).execute();

        ///////////////////////////////////////////////////////////////////////////////////////////////
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Events").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String data = "";
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        data = document.getString("name");
                        subjects.add(data);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
//        //////////////////////////////////////////////////////////////////////////////////////////////////
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@androidx.annotation.NonNull Menu menu, @androidx.annotation.NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_home,menu);
            }

            @Override
            public boolean onMenuItemSelected(@androidx.annotation.NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.add:
                        BottomSheet BottomSheet = new BottomSheet(false, "Default state");
                        BottomSheet.show(getChildFragmentManager(), "Normal");
                }
                return false;
            }
        },getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}