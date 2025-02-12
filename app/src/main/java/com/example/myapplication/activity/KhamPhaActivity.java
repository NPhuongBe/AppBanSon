package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.RecyclePackage.DbAdapter;
import com.example.myapplication.RecyclePackage.DbModelClass;
import com.example.myapplication.SQLite.MyDbClass;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;



public class KhamPhaActivity extends AppCompatActivity {
    MyDbClass objMyDbClass;
    ArrayList<DbModelClass> objDbModelClassArrayList;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kham_pha);
        objMyDbClass = new MyDbClass(this);
        objDbModelClassArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.dataRV);
    }
    public void showData(View view){
        try{
            objDbModelClassArrayList=objMyDbClass.getAllData();
            DbAdapter objDbAdapter = new DbAdapter(objDbModelClassArrayList);
            recyclerView.hasFixedSize();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(objDbAdapter);
        }catch (Exception e){
            Toast.makeText(this, "showData:-" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}