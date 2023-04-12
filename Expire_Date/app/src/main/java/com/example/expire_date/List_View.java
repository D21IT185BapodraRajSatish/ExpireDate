package com.example.expire_date;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class List_View extends Fragment {

    View List_View;

    RecyclerView rcv;

    TextView tv_day;

    DatabaseReference db,db1,db2;

    List<ProductInfo> list;

    String username;

    FirebaseAuth mauth;

    public List_View() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        List_View = inflater.inflate(R.layout.list_view, container, false);

        rcv = List_View.findViewById(R.id.LV_RCV);

        tv_day = List_View.findViewById(R.id.RVA_DATE);

        rcv.setHasFixedSize(true);
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));

        mauth = FirebaseAuth.getInstance();

        list = new ArrayList<>();

        db1 = FirebaseDatabase.getInstance().getReference("User");

        db1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (mauth.getCurrentUser().getEmail().endsWith(ds.getValue(UserInfo.class).getU_email())) {
                        username = ds.getValue(UserInfo.class).getU_Name();
                        break;
                    }
                }
                RetrivefromDB();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rcv);

        return List_View;
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            String delete=list.get(viewHolder.getAdapterPosition()).getBarcode();
            db2=FirebaseDatabase.getInstance().getReference("User").child(username).child(delete);
            db2.removeValue().addOnCompleteListener(task->{
                if(!task.isSuccessful()){
                    Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private  void RetrivefromDB(){

        LocalDateTime currentDateTime = LocalDateTime.now();

        int cmpdate = currentDateTime.getDayOfMonth();
        int cmpmonth = currentDateTime.getMonthValue();
        int cmpyear = currentDateTime.getYear();

        LocalDate date1 = LocalDate.of(cmpyear, cmpmonth, cmpdate);


        db = FirebaseDatabase.getInstance().getReference("User").child(username);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.hasChildren()){
                        String daysleft;
                        String bcode= ds.getValue(ProductInfo.class).getBarcode();
                        String firebasename = ds.getValue(ProductInfo.class).getName();
                        String edate = ds.getValue(ProductInfo.class).getExpireDate();

                        int edate1 = Integer.parseInt(edate.substring(0,edate.indexOf("/")));
                        int emonth = Integer.parseInt(edate.substring(3,4));
                        int eyear = Integer.parseInt(edate.substring(5,9));

                        LocalDate date2 = LocalDate.of(eyear, emonth, edate1);

                        if(Period.between(date1, date2).getDays()<=0){
                            daysleft = "EXPIRED";
                        }else{
                            daysleft=String.valueOf(Period.between(date1, date2).getDays())+" Left";
                        }

                        ProductInfo p = new ProductInfo(bcode,firebasename,daysleft);

                        list.add(p);
                    }else{
                        Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                    rcv.setAdapter(new MyRecyclerViewAdepter(list,getContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}