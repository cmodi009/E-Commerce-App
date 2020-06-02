package com.example.e_commerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.Interface.ItemClickListener;
import com.example.e_commerce.R;

public class SellerProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductDescription, txtProductPrice,txtProductState;
    public ImageView imageView;
    public ItemClickListener listener;


    public SellerProductsViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.product_seller_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_seller_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_seller_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_seller_price);
        txtProductState=(TextView)itemView.findViewById(R.id.product_state);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);
    }
}
