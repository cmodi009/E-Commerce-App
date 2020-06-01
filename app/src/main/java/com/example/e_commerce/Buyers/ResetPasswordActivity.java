package com.example.e_commerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Prevalent.Prevalent;
import com.example.e_commerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pagetitle,titleques;
    private EditText phoneno,ques1,ques2;
    private Button verifybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");

        pagetitle=findViewById(R.id.reset_pass);
        titleques=findViewById(R.id.title_questions);
        phoneno=findViewById(R.id.find_phoneno);
        ques1=findViewById(R.id.question_1);
        ques2=findViewById(R.id.question_2);
        verifybtn=findViewById(R.id.verify_btn);

    }

    @Override
    protected void onStart() {
        super.onStart();

        phoneno.setVisibility(View.GONE);

        if(check.equals("settings"))
        {
            pagetitle.setText("Set Questions");
            titleques.setText("Please set Answers for the Following Security Questions ?");
            verifybtn.setText("Set");

            DisplayPrevAns();

            verifybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetAnswers();

                }
            });
        }
        else if(check.equals("login"))
        {
            phoneno.setVisibility(View.VISIBLE);
            verifybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VerifyUser();
                }
            });
        }
    }



    private void SetAnswers(){
        String ans1=ques1.getText().toString().toLowerCase();
        String ans2 = ques2.getText().toString().toLowerCase();

        if(ques1.equals("") && ques2.equals(""))
        {
            Toast.makeText(ResetPasswordActivity.this, "Please Answer Both Questions.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(Prevalent.currentuser.getPhone());

            HashMap<String,Object > userdataMap = new HashMap<>();
            userdataMap.put("Answer1",ans1);
            userdataMap.put("Answer2",ans2);

            ref.child("Security Questions").updateChildren(userdataMap).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ResetPasswordActivity.this,
                                        "You have answered security questions.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
        }
    }

    private void DisplayPrevAns()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(Prevalent.currentuser.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String ans1 = dataSnapshot.child("Answer1").getValue().toString();
                    String ans2 = dataSnapshot.child("Answer2").getValue().toString();

                    ques1.setText(ans1);
                    ques2.setText(ans2);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void VerifyUser() {
        final String phone = phoneno.getText().toString();
        final String answer1 = ques1.getText().toString().toLowerCase();
        final String answer2 = ques2.getText().toString().toLowerCase();

        if (!phone.equals("") && !answer1.equals("") && !answer2.equals("")) {

            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String mPhone = dataSnapshot.child("phone").getValue().toString();

                        if (dataSnapshot.hasChild("Security Questions"))
                        {
                            String ans1 = dataSnapshot.child("Security Questions").child("Answer1").getValue().toString();
                            String ans2 = dataSnapshot.child("Security Questions").child("Answer2").getValue().toString();

                            if (!ans1.equals(answer1)) {
                                Toast.makeText(ResetPasswordActivity.this, "Your First Answer " +
                                        "is incorrect", Toast.LENGTH_SHORT).show();
                            } else if (!ans2.equals(answer2)) {
                                Toast.makeText(ResetPasswordActivity.this, "Your Second " +
                                        "Answer is incorrect", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");

                                final EditText newpass = new EditText(ResetPasswordActivity.this);
                                newpass.setHint("Write Password here..");
                                builder.setView(newpass);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!newpass.getText().toString().equals("")) {
                                            ref.child("password").setValue(newpass.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ResetPasswordActivity.this,
                                                                        "Password changed successfully", Toast.LENGTH_SHORT).show();
                                                                Intent intent =new Intent(ResetPasswordActivity.this,LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "You have not set the security questions.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(ResetPasswordActivity.this, "This phone no not exists", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else
        {
            Toast.makeText(this, "Please complete the form", Toast.LENGTH_SHORT).show();
        }
    }

}
