package com.example.expire_date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    TextInputLayout name, address, phone, mail, password, C_password;

    RadioButton m, f, o;

    RadioGroup rbg;

    Button btnSub;

    DatabaseReference db1;

    FirebaseAuth mauth;

    UserInfo uinfo;

    boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

    }

    @Override
    protected void onStart() {
        super.onStart();

        btnSub.setOnClickListener(v -> {
            if (Valid()) {
                StoretoDB();
            }
        });

    }

    private void init() {

        name = findViewById(R.id.FULL_NAME);
        address = findViewById(R.id.ADDRESS);
        phone = findViewById(R.id.NUMBER);
        mail = findViewById(R.id.RE_Email);
        password = findViewById(R.id.RE_Password);
        C_password = findViewById(R.id.Conform_Password);

        btnSub = findViewById(R.id.Btn_Register);

        db1 =  FirebaseDatabase.getInstance().getReference("User");

        mauth = FirebaseAuth.getInstance();

        uinfo = new UserInfo();

        m = findViewById(R.id.MALE);
        f = findViewById(R.id.FEMALE);
        o = findViewById(R.id.OTHER);

        uinfo.setU_Gender("Male");

        rbg = findViewById(R.id.RBG_RE);

        rbg.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.MALE:
                    uinfo.setU_Gender("Male");
                    break;
                case R.id.FEMALE:
                    uinfo.setU_Gender("Female");
                    break;
                case R.id.OTHER:
                    uinfo.setU_Gender("Other");
                    break;
            }
        });
    }

    private boolean Valid() {
        valid= true;
        String cname, cadd, cphno, cmail, cpass, ccpass;

        cname = name.getEditText().getText().toString();
        cadd = address.getEditText().getText().toString();
        cphno = phone.getEditText().getText().toString();
        cmail = mail.getEditText().getText().toString();
        cpass = password.getEditText().getText().toString();
        ccpass = C_password.getEditText().getText().toString();

        if (cname.isEmpty() && cadd.isEmpty() && cphno.isEmpty() && cmail.isEmpty() && cpass.isEmpty() && ccpass.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle(Html.fromHtml("<font color='#ff0f0f'>Error</font>"));
            builder.setMessage("All Field are Required");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
            valid = false;
        } else {
            //name
            if (valid) {
                if (cname.isEmpty()) {
                    name.setError("Please Enter Name");
                    name.setErrorEnabled(true);
                    valid = false;
                } else {
                    Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
                    Matcher ms = ps.matcher(name.getEditText().getText().toString());
                    if (!ms.matches()) {
                        name.setError("Please Enter Valid First Name");
                        name.setErrorEnabled(true);
                        valid = false;
                    } else {
                        name.setErrorEnabled(false);
                        valid = true;
                    }
                }
            }
            //phone
            if (valid) {
                if (phone.getEditText().getText().toString().isEmpty()) {
                    phone.setError("Please Enter Phone number");
                    phone.setErrorEnabled(true);
                    valid = false;
                } else {
                    Pattern ps = Pattern.compile("^[0-9]+$");
                    Matcher ms = ps.matcher(phone.getEditText().getText().toString());
                    if (!ms.matches()) {
                        phone.setError("Please Enter Phone Number In Digit Only");
                        phone.setErrorEnabled(true);
                        valid = false;
                    } else {
                        if (phone.getEditText().getText().toString().length() < 10 | phone.getEditText().getText().toString().length() > 10) {
                            phone.setError("Please Enter Phone Number In 10 Digit Only");
                            phone.setErrorEnabled(true);
                            valid = false;
                        } else {
                            phone.setErrorEnabled(false);
                            valid = true;
                        }
                    }
                }
            }
            //address
            if (valid) {
                if (address.getEditText().getText().toString().isEmpty()) {
                    address.setError("Please Enter This Field");
                    address.setErrorEnabled(true);
                    valid = false;
                } else {
                    Pattern ps = Pattern.compile("^[a-zA-Z0-9./, ]+$");
                    Matcher ms = ps.matcher(address.getEditText().getText().toString());
                    if (!ms.matches()) {
                        String temp1 = address.getEditText().getText().toString();
                        String temp2 = String.valueOf(temp1.charAt(temp1.indexOf("-")));

                        int i = temp2.compareTo("-");
                        if (i == 0) {
                            valid = true;
                        } else {
                            address.setError("Please Enter Address in character only");
                            address.setErrorEnabled(true);
                            address.findFocus();
                            valid = false;
                        }
                    } else {
                        address.setErrorEnabled(false);
                        valid = true;
                    }
                }
            }
            //mail
            if (valid) {
                if (mail.getEditText().getText().toString().isEmpty()) {
                    mail.setError("Please Enter This Field");
                    mail.setErrorEnabled(true);
                    valid = false;
                } else {
                    mail.setErrorEnabled(false);
                    valid = true;
                }
            }
            //passsword
            if (valid) {
                if (password.getEditText().getText().toString().isEmpty()) {
                    password.setError("Please Enter This Field");
                    password.setErrorEnabled(true);
                    valid = false;
                } else {
                    Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
                    Matcher ms = ps.matcher(password.getEditText().getText().toString());
                    if (!ms.matches()) {
                        password.setError("Please Enter Password In Digit & NUmber  Only");
                        password.setErrorEnabled(true);
                        valid = false;
                    } else {
                        if (password.getEditText().getText().toString().length() < 8 | password.getEditText().getText().toString().length() > 8) {
                            password.setError("Please Enter Password In 8 Digit Only");
                            password.setErrorEnabled(true);
                            valid = false;
                        } else {
                            password.setErrorEnabled(false);
                            valid = true;
                        }
                    }
                }
            }
            //conform Password
            if (valid) {
                if (C_password.getEditText().getText().toString().isEmpty()) {
                    C_password.setError("Please Enter This Field");
                    C_password.setErrorEnabled(true);
                    valid = false;
                } else {
                    Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
                    Matcher ms = ps.matcher(C_password.getEditText().getText().toString());
                    if (!ms.matches()) {
                        C_password.setError("Please Enter Password In Digit & NUmber  Only");
                        C_password.setErrorEnabled(true);
                        valid = false;
                    } else {
                        if (C_password.getEditText().getText().toString().length() < 8 | C_password.getEditText().getText().toString().length() > 8) {
                            C_password.setError("Please Enter Password In 8 Digit Only");
                            C_password.setErrorEnabled(true);
                            valid = false;
                        } else {
                            C_password.setErrorEnabled(false);
                            valid = true;
                        }
                    }
                }
            }
            //Compare pass with conf pass
            if (valid) {
                if(!cpass.endsWith(ccpass)){
                    Toast.makeText(this, "Password not match", Toast.LENGTH_LONG).show();
                    valid= false;
                }else{
                    valid = true;
                }
            }
        }
        return valid;
    }

    private void StoretoDB(){
        uinfo.setU_Name(name.getEditText().getText().toString().trim());
        uinfo.setU_Address(address.getEditText().getText().toString().trim());
        uinfo.setU_Phno(phone.getEditText().getText().toString().trim());
        uinfo.setU_email(mail.getEditText().getText().toString().trim());
        uinfo.setU_Password(password.getEditText().getText().toString().trim());

        db1.child(uinfo.getU_Name()).setValue(uinfo).addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               mauth.createUserWithEmailAndPassword(uinfo.getU_email(), uinfo.getU_Password()).addOnCompleteListener(task1->{
                   if(task1.isSuccessful()){
                       Toast.makeText(this, "Register Successfully", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(this,Login.class));
                       finish();
                   }else{
                       Toast.makeText(this, task1.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                   }
               });
           }else{
               Toast.makeText(this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
           }
        });
    }
}