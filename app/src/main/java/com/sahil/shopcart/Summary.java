package com.sahil.shopcart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahil.shopcart.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Summary extends Activity {

    private ArrayList<SetGet> csg = new ArrayList<>();
    CardAdapter cAdapter;
    DatabaseHelper dbhelper;
    @BindView(R.id.summarytxt) TextView phone;
    @BindView(R.id.summarypic) ImageView product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        ButterKnife.bind(this);

        Intent i = getIntent();
        String name = i.getStringExtra("partyName");
        String txtdescrip = i.getStringExtra("txt");

        phone.setText(txtdescrip);

        byte[] decodedString = Base64.decode(name, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        product.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
        
    }
}
