package com.example.expire_date;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class List_Add extends Fragment {

    View List_Add;

    EditText etbarcode,etname;

    TextView tv;

    ImageButton imgbtnDate;

    Button scan,getvalue,submite;

    String username;

    FirebaseAuth mauth;

    DatabaseReference db1,db2;

    ProductInfo pinfo;

    public List_Add() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        List_Add= inflater.inflate(R.layout.list__add, container, false);

        imgbtnDate = List_Add.findViewById(R.id.LA_img_btn);

        scan = List_Add.findViewById(R.id.Btn_Scan_Barcode);
        getvalue = List_Add.findViewById(R.id.LA_BTN_GETVALUE);

        tv = List_Add.findViewById(R.id.LA_TV_date);

        etbarcode = List_Add.findViewById(R.id.LA_ET_BARCODE);
        etname = List_Add.findViewById(R.id.LA_ET_PRODUCT);
        submite = List_Add.findViewById(R.id.LA_BTN_SUBMITE);

        mauth =FirebaseAuth.getInstance();

        pinfo = new ProductInfo();

        scan.setOnClickListener(v->{
            ScanBarCode();
        });

        getvalue.setOnClickListener(v->{
            GetProduct();
        });

        imgbtnDate.setOnClickListener(v->{
            Calendar c;
            DatePickerDialog dpg;

            c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);

            //Store selected Date in Text Box
            dpg = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int MYear, int MMonth, int MDay) {
                    tv.setText(MDay + "/" + (MMonth + 1) + "/" + MYear);
                }
            }, day, month, year);
            //show Date picker
            dpg.show();
        });

        submite.setOnClickListener(v->{
            db2 = FirebaseDatabase.getInstance().getReference("User");

            db2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (mauth.getCurrentUser().getEmail().endsWith(ds.getValue(UserInfo.class).getU_email())) {
                            username = ds.getValue(UserInfo.class).getU_Name();
                            break;
                        }
                    }
                    StoretoDB();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });

        return List_Add;
    }

    private void StoretoDB(){
        pinfo.setBarcode(etbarcode.getText().toString());
        pinfo.setName(etname.getText().toString());
        pinfo.setExpireDate(tv.getText().toString());
        db1 = FirebaseDatabase.getInstance().getReference("User").child(username);
        db1.child(pinfo.getBarcode()).setValue(pinfo).addOnCompleteListener(task->{
            if(task.isSuccessful()){
                etbarcode.setText("");
                etname.setText("");
                tv.setText("dd/mm/yyyy");
            }else{
                Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetProduct(){
        String url = "https://dietagram.p.rapidapi.com/apiBarcode.php?name="+etbarcode.getText().toString();
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray dishes = jsonObject.getJSONArray("dishes");
                            JSONObject firstDish = dishes.getJSONObject(0);
                            String dishName = firstDish.getString("name");
                            etname.setText(dishName);
                            // Handle the dish name here
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-RapidAPI-Key", "78031149f5mshc627290f322d1a1p170e51jsn35beb6ca7a0d");
                headers.put("X-RapidAPI-Host", "dietagram.p.rapidapi.com");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    private void ScanBarCode(){
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(integrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult rs = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(rs!=null){
            if(rs.getContents()!= null){
                etbarcode.setText(rs.getContents());
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

/*int notify_ID=1;
                String CHANNEL_ID = "my_channel_id";
                NotificationManager mnotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification;

                notification = new Notification.Builder(getContext(), CHANNEL_ID)
                        .setContentTitle("Barcode Scanned")
                        .setContentText(rs.getContents())
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .build();

                mnotificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"New Channel", NotificationManager.IMPORTANCE_HIGH));

                mnotificationManager.notify(notify_ID, notification);*/