package com.example.e_commerce.Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.e_commerce.Admin.AdminApproveProductsActivity;
import com.example.e_commerce.Buyers.MainActivity;
import com.example.e_commerce.Model.Products;
import com.example.e_commerce.R;

import com.example.e_commerce.ViewHolder.ProductViewHolder;
import com.example.e_commerce.ViewHolder.SellerProductsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SellerHomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView1;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unaprrovedProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        recyclerView1=findViewById(R.id.seller_home_rec);
        recyclerView1.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager);

        unaprrovedProd = FirebaseDatabase.getInstance().getReference()
                .child("Products");

    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            =new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    Intent intent2 = new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                    startActivity(intent2);
                    return true;

                case R.id.navigation_add:
                    Intent intent = new Intent(SellerHomeActivity.this, SellerCategoryActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.navigation_logout:
                    final FirebaseAuth mAuth;
                    mAuth=FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Intent intent1 = new Intent(SellerHomeActivity.this, MainActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent1);
                    finish();
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(unaprrovedProd.orderByChild("SellerId").
                                equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, SellerProductsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, SellerProductsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SellerProductsViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = "+model.getPrice() + "Rs");
                        holder.txtProductState.setText("State : " +model.getProductstate());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {

                                final String productId =model.getPid();
                                CharSequence options[] =new CharSequence[]{
                                        "Yes",
                                        "No"
                                };
                                AlertDialog.Builder builder=new AlertDialog.Builder(SellerHomeActivity.this);
                                builder.setTitle("Do you want to Delete this Product?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which==0)
                                        {
                                            DeleteProduct(productId);
                                        }
                                        if(which==1)
                                        {

                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public SellerProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_items_layout,parent,false);
                        SellerProductsViewHolder holder = new SellerProductsViewHolder(view);
                        return holder;
                    }
                };
        recyclerView1.setAdapter(adapter);
        adapter.startListening();
    }

    private void DeleteProduct(String productId)
    {
        unaprrovedProd.child(productId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SellerHomeActivity.this,
                                "That item is deleted.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
