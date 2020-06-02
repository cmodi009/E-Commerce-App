package com.example.e_commerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e_commerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SellerLoginActivity extends AppCompatActivity
{
    private EditText emailInput,passInput;
    private Button loginSelBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        loadingbar = new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();

        loginSelBtn=findViewById(R.id.seller_logbtn);
        emailInput=findViewById(R.id.seller_log_email);
        passInput=findViewById(R.id.seller_log_password);

        loginSelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSeller();
            }
        });
    }

    private void loginSeller()
    {
        final String email = emailInput.getText().toString();
        String password = passInput.getText().toString();

        if(!email.equals("") && !password.equals("")) {
            loadingbar.setTitle("Seller Account Login");
            loadingbar.setMessage("Please wait, while we are checking the credentials.");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
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
