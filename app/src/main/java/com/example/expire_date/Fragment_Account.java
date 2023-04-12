package com.example.expire_date;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Account extends Fragment {

    View Fragment_Account;

    TextView fname, fadd, fphone, fmail;

    DatabaseReference db;

    FirebaseAuth mauth;

    public Fragment_Account() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fragment_Account = inflater.inflate(R.layout.fragment_account, container, false);

        init();

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (mauth.getCurrentUser().getEmail().endsWith(ds.getValue(UserInfo.class).getU_email())) {
                        fname.setText(fname.getText() + ds.getValue(UserInfo.class).getU_Name());
                        fadd.setText(fadd.getText()+ds.getValue(UserInfo.class).getU_Address());
                        fphone.setText(fphone.getText()+ds.getValue(UserInfo.class).getU_Phno());
                        fmail.setText(fmail.getText()+ds.getValue(UserInfo.class).getU_email());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return Fragment_Account;
    }

    private void init() {
        fname = Fragment_Account.findViewById(R.id.F_TV_Name);
        fadd = Fragment_Account.findViewById(R.id.F_TV_Address);
        fphone = Fragment_Account.findViewById(R.id.F_TV_Phone);
        fmail = Fragment_Account.findViewById(R.id.F_TV_Email);

        db = FirebaseDatabase.getInstance().getReference("User");

        mauth = FirebaseAuth.getInstance();
    }
}