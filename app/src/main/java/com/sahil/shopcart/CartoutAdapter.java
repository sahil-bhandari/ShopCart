package com.sahil.shopcart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
            //GoToPlus();
            //GoToMi();

        }
       /* public void GoToPlus(){
            final int count[] = {0};
            plus.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            cartQ.setText(Integer.toString(a));
                            a =count[0]++;

                        }
                    }
            );
        }

        public void GoToMi(){
            //final int count[] = {a};
            minus.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                             a=b;
                             b = count[a]--;
                            cartQ.setText(String.valueOf(b));
                            //a--;

                            cartQ.setText(Integer.toString(a));
                            if (a<=0){
                                 a = 0;
                                cartQ.setText(String.valueOf(a));
                            }
                            else
                            {
                                a--;
                                cartQ.setText(String.valueOf(a));
                            }

                        }
                    }
            );
            }*/
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
        Picasso.with(ctx).load(listsg1.getImage()).fit().into(holder.cartImg);

        //Glide.with(context).load(csg.get(position).getImage()).into(mholder.cImg);


    }




    @Override
    public int getItemCount() {
        return csg1.size();
    }





}
