package com.sahil.shopcart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sahil.shopcart.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ArrayList<SetGet> csg = new ArrayList<>();
    public static int count=0;
    RecyclerView RvCustomList;
    CardAdapter cAdapter;
    DatabaseHelper dbhelper;
    ImageView imgcart;
    static TextView txtc;
    Spinner spinner;
    String item;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Paint p = new Paint();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RvCustomList = (RecyclerView) findViewById(R.id.lv1);
        imgcart = (ImageView) findViewById(R.id.CartimgV);
        txtc = (TextView)findViewById(R.id.textViewC);
        spinner = (Spinner) findViewById(R.id.spinner1);

        List<String> categories = new ArrayList<String>();
        categories.add("ALL PRODUCTS");
        categories.add("Cases");
        categories.add("Phones");
        categories.add("Others");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        dbhelper = new DatabaseHelper(this);

       // csg = dbhelper.getData();

        DatabaseReference allBooksRef = mDatabase.child("shop");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<SetGet> shoplist = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    SetGet book = ds.getValue(SetGet.class);
                    shoplist.add(book);
                }

                cAdapter = new CardAdapter(MainActivity.this, shoplist);
                RvCustomList.setAdapter(cAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        allBooksRef.addListenerForSingleValueEvent(valueEventListener);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("ALL PRODUCTS"))
                {
                    csg = dbhelper.getData();

                    cAdapter = new CardAdapter(getApplicationContext(),csg);
                    RecyclerView.LayoutManager mLaM = new LinearLayoutManager(getApplicationContext());
                    RvCustomList.setLayoutManager(mLaM);
                    RvCustomList.setItemAnimator(new DefaultItemAnimator());
                    RvCustomList.setAdapter(cAdapter);

                    cAdapter.notifyDataSetChanged();
                    cAdapter.notifyItemChanged(position);
                    //do nothing.
                }
                else
                {
                    item = parent.getItemAtPosition(position).toString();
                    csg = new ArrayList<SetGet>();
                    csg = dbhelper.getAllData(item);

                    cAdapter = new CardAdapter(getApplicationContext(),csg);
                    RecyclerView.LayoutManager mLaM = new LinearLayoutManager(getApplicationContext());
                    // mLaM.setOrientation(LinearLayoutManager.VERTICAL);
                    RvCustomList.setLayoutManager(mLaM);
                    RvCustomList.setItemAnimator(new DefaultItemAnimator());
                    RvCustomList.setAdapter(cAdapter);

                    cAdapter.notifyDataSetChanged();
                    cAdapter.notifyItemChanged(position);
                    // Showing selected spinner item
                    Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
                    // write code on what you want to do with the item selection
                }
                // On selecting a spinner item


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }
        });



        SwipeToCart();
        GoToCart();
        getCount();
//        cAdapter.notifyDataSetChanged();
    }


    public void SwipeToCart(){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){

                    dbhelper.setbool1(csg.get(position).getId());
                    //csg.remove(position);
                    cAdapter.notifyItemChanged(position);
                    getCount();

                    Snackbar snackbar = Snackbar.make(RvCustomList, "OK!", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener(){
                                @Override
                                public void onClick(View view){

                                    dbhelper.setbool0(csg.get(position).getId());

                                    cAdapter.notifyItemChanged(position);
                                    getCount();

                                    Snackbar snackbar1 = Snackbar.make(RvCustomList, "Restored!", Snackbar.LENGTH_SHORT);
                                    snackbar1.show();
                                }

                            });

                    snackbar.show();



                }
                else{
                    dbhelper.setbool1(csg.get(position).getId());

                    cAdapter.notifyItemChanged(position);
                    getCount();

                    Snackbar snackbar = Snackbar.make(RvCustomList, "OK!", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener(){
                                @Override
                                public void onClick(View view){

                                    dbhelper.setbool0(csg.get(position).getId());
                                    cAdapter.notifyItemChanged(position);
                                    getCount();

                                    Snackbar snackbar1 = Snackbar.make(RvCustomList, "Restored!", Snackbar.LENGTH_SHORT);
                                    snackbar1.show();
                                }

                            });

                    snackbar.show();
                }

            }

            @Override
            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0){
                        p.setColor(Color.parseColor("#B2FF59"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_add_shopping_cart_black_24dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                    else {
                        p.setColor(Color.parseColor("#B2FF59"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_add_shopping_cart_black_24dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(RvCustomList);
    }


    public void GoToCart(){
        imgcart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,Cart.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public int DisCount(){

        int i = dbhelper.getTaskCount();
        txtc.setText(Integer.toString(i));
        return i;
    }
    public void getCount(){
        count= DisCount();
        if(count>0){
            txtc.setText(Integer.toString(count));
            txtc.setVisibility(View.VISIBLE);
        }
        else
            txtc.setVisibility(View.GONE);
    }
    public static void dodecrease() {
        count--;
        if (count <= 0) {
            txtc.setVisibility(View.GONE);
        } else {
            txtc.setText(Integer.toString(count));
            txtc.setVisibility(View.VISIBLE);
        }
    }

}