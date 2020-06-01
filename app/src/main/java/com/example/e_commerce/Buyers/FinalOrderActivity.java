package com.example.e_commerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_commerce.Prevalent.Prevalent;
import com.example.e_commerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class FinalOrderActivity extends AppCompatActivity
{
    private EditText nameedt,phoneedt,addressedt,cityedt;
    private Button confirmbtn;

    private String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_order);

        totalAmount =getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price = "+totalAmount+ " Rs", Toast.LENGTH_SHORT).show();

        confirmbtn=findViewById(R.id.confirm_btn);
        nameedt=findViewById(R.id.order_name);
        phoneedt=findViewById(R.id.order_phone);
        addressedt=findViewById(R.id.order_address);
        cityedt = findViewById(R.id.order_city);

        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });


    }

    private void Check()
    {
        if(TextUtils.isEmpty(nameedt.getText().toString()))
        {
            Toast.makeText(this, "Please provide your full name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneedt.getText().toString()))
        {
            Toast.makeText(this, "Please provide your phone no", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressedt.getText().toString()))
        {
            Toast.makeText(this, "Please provide your address", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cityedt.getText().toString()))
        {
            Toast.makeText(this, "Please provide your city name", Toast.LENGTH_SHORT).show();
        }
        else {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder()
    {
        final String savecurrentdate,savecurrentime;
        Calendar calendar =Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MM dd, yyyy");
        savecurrentdate = currentdate.format(calendar.getTime());
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        savecurrentime = currenttime.format(calendar.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().
                getReference().child("Orders").child(Prevalent.currentuser.getPhone());

        HashMap<String,Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount",totalAmount);
        ordersMap.put("name",nameedt.getText().toString());
        ordersMap.put("phone",phoneedt.getText().toString());
        ordersMap.put("time",savecurrentime);
        ordersMap.put("date",savecurrentdate);
        ordersMap.put("address",addressedt.getText().toString());
        ordersMap.put("city",cityedt.getText().toString());
        ordersMap.put("state","not shipped");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View").child(Prevalent.currentuser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(FinalOrderActivity.this,
                                                "Your Order has been placed.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(FinalOrderActivity.this,HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }
}
