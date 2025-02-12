package com.example.myapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.DonHangAdapter;
import com.example.myapplication.retrofit.ApiBanHang;
import com.example.myapplication.retrofit.RetrofitClient;
import com.example.myapplication.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    ApiBanHang apiBanHang;
    RecyclerView redonhang;

    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don);
        initView();
        initToolbar();
        getOrder();


    }

    private void getOrder() {
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        compositeDisposable.add(apiBanHang.xemDonHang(String.valueOf(Utils.user_current.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel ->{
                            if (donHangModel.isSuccess()){
                                DonHangAdapter adapter=new DonHangAdapter(getApplicationContext(),donHangModel.getResult());
                                redonhang.setAdapter(adapter);
                            }else{
                                Log.d("loggg", "khong get ");
                            }


                        },
                        throwable ->{
                            Log.d("loggg", throwable.getMessage());

                        }
                )
        );
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {
        apiBanHang=RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        redonhang=findViewById(R.id.recycleview_donhang);
        toolbar=findViewById(R.id.toobar);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        redonhang.setLayoutManager(layoutManager);
    }
    protected void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }

}
