package com.sahil.shopcart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

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
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Paint p = new Paint();
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RvCustomList = (RecyclerView) findViewById(R.id.lv1);
        imgcart = (ImageView) findViewById(R.id.CartimgV);
        txtc = (TextView)findViewById(R.id.textViewC);
        spinner = (Spinner) findViewById(R.id.spinner1);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

//        List<String> categories = new ArrayList<String>();
//        categories = dbhelper.getCategoryData();
//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // attaching data adapter to spinner
//        spinner.setAdapter(dataAdapter);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){


            AsyncTaskRunner myTask = new AsyncTaskRunner();
            myTask.execute();
            progressBar.setVisibility(View.VISIBLE);
        }




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("ALL PRODUCTS"))
                {
                    csg = dbhelper.getData(1,"");

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
                    csg = dbhelper.getData(3,item);

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

                    dbhelper.setboolean(csg.get(position).getId(),1);
                    //csg.remove(position);
                    cAdapter.notifyItemChanged(position);
                    getCount();

                    Snackbar snackbar = Snackbar.make(RvCustomList, "OK!", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener(){
                                @Override
                                public void onClick(View view){

                                    dbhelper.setboolean(csg.get(position).getId(),0);

                                    cAdapter.notifyItemChanged(position);
                                    getCount();

                                    Snackbar snackbar1 = Snackbar.make(RvCustomList, "Restored!", Snackbar.LENGTH_SHORT);
                                    snackbar1.show();
                                }

                            });

                    snackbar.show();



                }
                //uncomment to enable swipe from right
//                else{
//                    dbhelper.setbool1(csg.get(position).getId());
//
//                    cAdapter.notifyItemChanged(position);
//                    getCount();
//
//                    Snackbar snackbar = Snackbar.make(RvCustomList, "OK!", Snackbar.LENGTH_LONG)
//                            .setAction("UNDO", new View.OnClickListener(){
//                                @Override
//                                public void onClick(View view){
//
//                                    dbhelper.setbool0(csg.get(position).getId());
//                                    cAdapter.notifyItemChanged(position);
//                                    getCount();
//
//                                    Snackbar snackbar1 = Snackbar.make(RvCustomList, "Restored!", Snackbar.LENGTH_SHORT);
//                                    snackbar1.show();
//                                }
//
//                            });
//
//                    snackbar.show();
//                }

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
//                    else {
//                        p.setColor(Color.parseColor("#B2FF59"));
//                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
//                        c.drawRect(background,p);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_add_shopping_cart_black_24dp);
//                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
//                        c.drawBitmap(icon,null,icon_dest,p);
//                    }
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

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        String a="0";
        @Override
        protected void onPreExecute() {


        }
        @Override
        protected String doInBackground(String... params) {
            dbhelper = new DatabaseHelper(getApplicationContext());

            db.collection("shop").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
//                                    SetGet r = new SetGet();
//                                    r.setId(Objects.requireNonNull(document.get("id")).toString());
//                                    r.setName(Objects.requireNonNull(document.get("name")).toString());
//                                    r.setMail(Objects.requireNonNull(document.get("price")).toString());
//                                    r.setImage(Objects.requireNonNull(document.get("image")).toString());
//                                    r.setDescrip(Objects.requireNonNull(document.get("description")).toString());
//                                    csg.add(r);

                                    dbhelper.insertdata(Objects.requireNonNull(document.get("name")).toString(),
                                            Objects.requireNonNull(document.get("price")).toString(),
                                            Objects.requireNonNull(document.get("image")).toString(),
                                            Objects.requireNonNull(document.get("description")).toString());


                                    a="1";
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
            });


            return a;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            if(result.equals("0")){
                progressBar.setVisibility(View.GONE);
            }

        }


    }
}

