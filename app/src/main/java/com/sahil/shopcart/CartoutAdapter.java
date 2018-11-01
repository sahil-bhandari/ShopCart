package com.sahil.shopcart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.sahil.shopcart.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ACER_SAHIL on 13-02-2017.
 */

public class CartoutAdapter extends RecyclerView.Adapter<CartoutAdapter.MyViewHolder> {

    public ArrayList<SetGet> csg1;
    public Context ctx;
    public  int a=0 ;


    public CartoutAdapter(Context ctx,ArrayList<SetGet> csg1) {

        this.ctx = ctx;
        this.csg1 = csg1;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cartQ,cartname,cartprice;
        public ImageView cartImg;
        public ImageView plus,minus;
        NumberPicker np;

        public MyViewHolder(View itView) {
            super(itView);

            //cartQ = (TextView)itView.findViewById(R.id.editTxtcar);
            cartname = (TextView) itView.findViewById(R.id.textViewame);
            cartprice = (TextView) itView.findViewById(R.id.textViewp);
            cartImg = (ImageView) itView.findViewById(R.id.imageViewCart);
            //plus = (ImageView)itView.findViewById(R.id.imageViewplus);
            //minus = (ImageView) itView.findViewById(R.id.imageViewmi);

            np = (NumberPicker) itView.findViewById(R.id.numberPicker2);

            np.setMinValue(1);
            np.setMaxValue(10);
            np.setWrapSelectorWheel(true);

        }
    }

    @Override
    public CartoutAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cartView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart_layout, parent, false);
        return new CartoutAdapter.MyViewHolder(cartView);
    }

    @Override
    public void onBindViewHolder(final CartoutAdapter.MyViewHolder holder, final int position) {

        final SetGet listsg1 = csg1.get(position);
        holder.cartname.setText(listsg1.getName());
        holder.cartprice.setText(listsg1.getMail());

        byte[] decodedString = Base64.decode(listsg1.getImage(), Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.cartImg.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));


    }




    @Override
    public int getItemCount() {
        return csg1.size();
    }





}
