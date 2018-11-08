package com.sahil.shopcart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    private ArrayList<SetGet> csg = new ArrayList<>();
    public static int count=0;
    CardAdapter cAdapter;
    DatabaseHelper dbhelper;
    String item;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Paint p = new Paint();
    public List<String> categories = new ArrayList<String>();

    @BindView(R.id.CartimgV) ImageView imgcart;
    @BindView(R.id.progressbar) ProgressBar progressBar;
    @BindView(R.id.textViewC) TextView txtc;
    @BindView(R.id.spinner1) Spinner spinner;
    @BindView(R.id.lv1) RecyclerView RvCustomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        dbhelper = new DatabaseHelper(getApplicationContext());

        fetchFirestoreData();
        SwipeToCart();
        //getCount();
    }

    private void fetchFirestoreData() {
        Toast.makeText(getApplicationContext(),"Fetching Data...",Toast.LENGTH_SHORT).show();
        //Firestore to get Data
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){

            db.collection("shop").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                            csg = dbhelper.getData(1,"");
                            ArrayList<String> idlist = new ArrayList<>();
                            for (int i = 0; i<csg.size() ; i++){
                                idlist.add(csg.get(i).getId());
                            }

                            if(idlist.contains(Objects.requireNonNull(document.get("id")).toString())){
                                dbhelper.updatedata(Objects.requireNonNull(document.get("id")).toString(),
                                        Objects.requireNonNull(document.get("name")).toString(),
                                        Objects.requireNonNull(document.get("price")).toString(),
                                        Objects.requireNonNull(document.get("image")).toString(),
                                        Objects.requireNonNull(document.get("category")).toString(),
                                        Objects.requireNonNull(document.get("description")).toString());
                            }
                            else {
                                dbhelper.insertdata(Objects.requireNonNull(document.get("id")).toString(),
                                        Objects.requireNonNull(document.get("name")).toString(),
                                        Objects.requireNonNull(document.get("price")).toString(),
                                        Objects.requireNonNull(document.get("image")).toString(),
                                        Objects.requireNonNull(document.get("category")).toString(),
                                        Objects.requireNonNull(document.get("description")).toString());
                            }


                            cAdapter = new CardAdapter(getApplicationContext(),csg);
                            RecyclerView.LayoutManager mLaM = new LinearLayoutManager(getApplicationContext());
                            RvCustomList.setLayoutManager(mLaM);
                            RvCustomList.setItemAnimator(new DefaultItemAnimator());
                            RvCustomList.setAdapter(cAdapter);
                            cAdapter.notifyDataSetChanged();

                            if (csg.size()>0){
                                categories = dbhelper.getCategoryData();
                                if(!categories.contains("ALL PRODUCTS")){
                                    categories.add("ALL PRODUCTS");
                                }
                                //sorting arraylist
                                Collections.sort(categories);
                                // Creating adapter for spinner
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);
                                // Drop down layout style - list view with radio button
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // attaching data adapter to spinner
                                spinner.setAdapter(dataAdapter);
                                dataAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });

            //animation for recycler view
            int resId = R.anim.layout_animation_fall_down;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
            RvCustomList.setLayoutAnimation(animation);
            progressBar.setVisibility(View.GONE);
        }
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Alert!");
            alertDialog.setMessage("No internet connectivity.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            progressBar.setVisibility(View.GONE);

        }
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

                if (direction == ItemTouchHelper.RIGHT || direction == ItemTouchHelper.LEFT){
                    dbhelper.setboolean(csg.get(position).getId(),1);
                    cAdapter.notifyItemChanged(position);
                    getCount();

                    Snackbar snackbar = Snackbar.make(RvCustomList, "OK!", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    dbhelper.setboolean(csg.get(position).getId(),0);
                                    cAdapter.notifyItemChanged(position);
                                    getCount();
                                    //undo added item
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

    public void getCount(){
        count=dbhelper.getTaskCount();
        if(count>0){
            txtc.setText(Integer.toString(count));
            txtc.setVisibility(View.VISIBLE);
        }
        else
            txtc.setVisibility(View.GONE);
    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...

        try {
            getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.CartimgV)
    void buttonClick() {
        Intent intent = new Intent(MainActivity.this,Cart.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @OnItemSelected(R.id.spinner1)
    public void spinnerItemSelected(Spinner spinner, int position) {
        if (spinner.getItemAtPosition(position).equals("ALL PRODUCTS"))
        {
            csg = dbhelper.getData(1,"");

            cAdapter = new CardAdapter(getApplicationContext(),csg);
            RecyclerView.LayoutManager mLaM = new LinearLayoutManager(getApplicationContext());
            RvCustomList.setLayoutManager(mLaM);
            RvCustomList.setItemAnimator(new DefaultItemAnimator());
            RvCustomList.setAdapter(cAdapter);

            cAdapter.notifyDataSetChanged();
            cAdapter.notifyItemChanged(position);

            int resId = R.anim.layout_animation_fall_down;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
            RvCustomList.setLayoutAnimation(animation);
        }
        else
        {
            item = spinner.getItemAtPosition(position).toString();
            csg = new ArrayList<SetGet>();
            //get selelcted data in arraylist
            csg = dbhelper.getData(3,item);

            cAdapter = new CardAdapter(getApplicationContext(),csg);
            RecyclerView.LayoutManager mLaM = new LinearLayoutManager(getApplicationContext());
            RvCustomList.setLayoutManager(mLaM);
            RvCustomList.setItemAnimator(new DefaultItemAnimator());
            RvCustomList.setAdapter(cAdapter);
            cAdapter.notifyDataSetChanged();
            cAdapter.notifyItemChanged(position);
            // Showing selected spinner item
            Toast.makeText(getApplicationContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();

            //animation for recycler view
            int resId = R.anim.layout_animation_fall_down;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
            RvCustomList.setLayoutAnimation(animation);
        }
    }
}

