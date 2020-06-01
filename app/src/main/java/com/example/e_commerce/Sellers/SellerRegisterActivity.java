package com.example.e_commerce.Sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.e_commerce.R;

public class SellerRegisterActivity extends AppCompatActivity {

    private Button sellerloginbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);

        sellerloginbtn=findViewById(R.id.seller_already_have_act_btn);

        sellerloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegisterActivity.this,SellerLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
