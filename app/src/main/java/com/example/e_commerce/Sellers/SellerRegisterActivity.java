package com.example.e_commerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_commerce.Buyers.MainActivity;
import com.example.e_commerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegisterActivity extends AppCompatActivity {

    private Button sellerloginbtn,sellerRegBtn;
    private EditText nameInput,phoneInput,emailInput,passInput,addInput;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);

        mAuth=FirebaseAuth.getInstance();
        loadingbar = new ProgressDialog(this);

        sellerloginbtn=findViewById(R.id.seller_already_have_act_btn);
        sellerRegBtn=findViewById(R.id.seller_regbtn);
        nameInput=findViewById(R.id.seller_name);
        phoneInput=findViewById(R.id.seller_phone);
        emailInput=findViewById(R.id.seller_email);
        passInput=findViewById(R.id.seller_password);
        addInput=findViewById(R.id.seller_address);

        sellerloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegisterActivity.this,SellerLoginActivity.class);
                startActivity(intent);
            }
        });


        sellerRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();
            }
        });
    }

    private void registerSeller()
    {
        final String name = nameInput.getText().toString();
        final String phone =phoneInput.getText().toString();
        final String email = emailInput.getText().toString();
        String password = passInput.getText().toString();
        final String address =addInput.getText().toString();

        if(!name.equals("") && !phone.equals("") && !email.equals("")
        && !password.equals("") && !address.equals(""))
        {
            loadingbar.setTitle("Creating Seller Account");
            loadingbar.setMessage("Please wait, while we are checking the credentials.");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                final DatabaseReference Rootref = FirebaseDatabase.getInstance()
                                        .getReference();
                                String sid = mAuth.getCurrentUser().getUid();

                                HashMap<String,Object> sellerMap = new HashMap<>();
                                sellerMap.put("sid",sid);
                                sellerMap.put("phone",phone);
                                sellerMap.put("email",email);
                                sellerMap.put("address",address);
                                sellerMap.put("name",name);

                                Rootref.child("Sellers").child(sid).updateChildren(sellerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                loadingbar.dismiss();
                                                Toast.makeText(SellerRegisterActivity.this,
                                                        "You are registered successfully", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(SellerRegisterActivity.this, SellerHomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Please fill out fields.", Toast.LENGTH_SHORT).show();
        }
    }
}
