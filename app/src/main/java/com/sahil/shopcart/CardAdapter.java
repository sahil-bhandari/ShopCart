package com.sahil.shopcart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sahil.shopcart.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ACER_SAHIL on 10-02-2017.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    public ArrayList<SetGet> csg;
    public Context context;

    public CardAdapter(Context context,ArrayList<SetGet> csg) {
        this.context = context;
        this.csg = csg;
    }


    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView cname,cprice,cdescrip,cimg;
        public ImageView cImg;

        public CardViewHolder(View itemView) {
            super(itemView);

            cname = (TextView) itemView.findViewById(R.id.txtitem);
            cprice = (TextView) itemView.findViewById(R.id.txtprice);
            cImg = (ImageView) itemView.findViewById(R.id.imageViewitem);
            cimg = (TextView) itemView.findViewById(R.id.textViewurl);
            cdescrip = (TextView) itemView.findViewById(R.id.txtdescrip);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            switch (getAdapterPosition()){
                case 0:
                    Intent intent =  new Intent();
                    intent.putExtra("partyName", cimg.getText().toString());
                    intent.putExtra("txt", cdescrip.getText().toString());
                    intent.setClass(v.getContext(), Summary.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    v.getContext().startActivity(intent);
                    break;
                default:
                    intent =  new Intent(v.getContext(), Summary.class);
                    intent.putExtra("partyName", cimg.getText().toString());
                    intent.putExtra("txt", cdescrip.getText().toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    v.getContext().startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public CardAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_main_layout, parent, false);
        return new CardViewHolder(cView);
    }

    @Override
    public void onBindViewHolder(CardAdapter.CardViewHolder mholder, int position) {

        SetGet listsg = csg.get(position);
        mholder.cname.setText(listsg.getName());
        mholder.cprice.setText(listsg.getMail());
        mholder.cimg.setText(listsg.getImage());
        mholder.cdescrip.setText(listsg.getDescrip());
        //
        byte[] decodedString = Base64.decode(listsg.getImage(), Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        mholder.cImg.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));

    }

    @Override
    public int getItemCount() {
        return csg.size();
    }



}
