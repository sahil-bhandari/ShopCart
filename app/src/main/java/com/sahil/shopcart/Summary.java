package com.sahil.shopcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahil.shopcart.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Summary extends Activity {

    private ArrayList<SetGet> csg = new ArrayList<>();
    CardAdapter cAdapter;
    DatabaseHelper dbhelper;
    TextView phone;
    ImageView product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        phone=(TextView)findViewById(R.id.summarytxt);
        product=(ImageView)findViewById(R.id.summarypic);


        Intent i = getIntent();
        String name = i.getStringExtra("partyName");
        String txtdescrip = i.getStringExtra("txt");

        phone.setText(txtdescrip);
        Picasso.with(this).load(name).fit().into(product);
        
    }
}
