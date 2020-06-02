package com.example.e_commerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.e_commerce.R;
import com.example.e_commerce.Sellers.SellerCategoryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainActivity extends AppCompatActivity {

    private Button applychangesbtn,deletebtn;
    private EditText name,price,desc;
    private ImageView imageView;
    private String productId = "";
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain);

        productId = getIntent().getStringExtra("pid");

        productsRef= FirebaseDatabase.getInstance().getReference().child("Products").child(productId);

        applychangesbtn=findViewById(R.id.apply_changes_btn);
        deletebtn=findViewById(R.id.delete_btn);
        name=findViewById(R.id.product_name_maintain);
        price=findViewById(R.id.product_price_maintain);
        desc=findViewById(R.id.product_description_maintain);
        imageView=findViewById(R.id.product_image_maintain);

        DisplaySpecificProductInfo();

        applychangesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyChanges();
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });
    }


    private void DisplaySpecificProductInfo()
    {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String Pname=dataSnapshot.child("pname").getValue().toString();
                    String Pprice=dataSnapshot.child("price").getValue().toString();
                    String Pdesc=dataSnapshot.child("description").getValue().toString();
                    String Pimage=dataSnapshot.child("image").getValue().toString();

                    name.setText(Pname);
                    price.setText(Pprice);
                    desc.setText(Pdesc);

                    Picasso.get().load(Pimage).into(imageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ApplyChanges()
    {
        String pname = name.getText().toString();
        String pprice = price.getText().toString();
        String pdesc = desc.getText().toString();

        if(pname.equals(""))
        {
            Toast.makeText(this, "Please write product name", Toast.LENGTH_SHORT).show();
        }
        else if(pprice.equals(""))
        {
            Toast.makeText(this, "Please write product price", Toast.LENGTH_SHORT).show();
        }
        else if(pdesc.equals(""))
        {
            Toast.makeText(this, "Please write product description", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String,Object> productMap = new HashMap<>();
            productMap.put("pid", productId);
            productMap.put("description", pdesc);
            productMap.put("price", pprice);
            productMap.put("pname", pname);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainActivity.this,
                                "Changes Applied successfully", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(AdminMaintainActivity.this, SellerCategoryActivity.class));
                        finish();
                    }
                }
            });
        }
    }

    private void deleteProduct()
    {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent =new Intent(AdminMaintainActivity.this, SellerCategoryActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(AdminMaintainActivity.this, "Product is deleted.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
