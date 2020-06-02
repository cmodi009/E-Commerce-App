package com.example.e_commerce.Admin;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commerce.Buyers.HomeActivity;
import com.example.e_commerce.Buyers.MainActivity;
import com.example.e_commerce.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminHomeActivity extends AppCompatActivity {
    private Button logoutbtn,checkordersbtn,maintainbtn,approveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logoutbtn=findViewById(R.id.admin_logout_btn);
        checkordersbtn=findViewById(R.id.check_orders_btn);
        maintainbtn=findViewById(R.id.maintain_btn);
        approveBtn=findViewById(R.id.approve_products_btn);

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkordersbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomeActivity.this,AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

        maintainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);

            }
        });

        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomeActivity.this, AdminApproveProductsActivity.class);
                startActivity(intent);
            }
        });


    }
}
