package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.retrofit.ApiBanHang;
import com.example.myapplication.retrofit.RetrofitClient;
import com.example.myapplication.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttongtien, txtsodt, txtemail;
    EditText edtdiachi;
    AppCompatButton btndathang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    long tongtien;
    int totalItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        countItem();
        initControl();
    }

    private void countItem() {
        totalItem = 0;
        for(int i = 0; i<Utils.mangmuahang.size(); i++) {
            totalItem = totalItem + Utils.mangmuahang.get(i).getSoluong();
        }
    }


private void initControl() {
    toolbar = findViewById(R.id.toobar); // Khởi tạo toolbar từ layout

    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    } else {
        Log.e("ThanhToanActivity", "ActionBar is null");
    }

    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    tongtien = getIntent().getLongExtra("tongtien", 0);
    txttongtien.setText(decimalFormat.format(tongtien));
    txtemail.setText(Utils.user_current.getEmail()); //lấy email ở bảng user
    txtsodt.setText(Utils.user_current.getMobile());

    // Xu ly nut dat hang
    btndathang.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str_diachi = edtdiachi.getText().toString().trim();
            if (TextUtils.isEmpty(str_diachi)) {
                Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
            } else {
                //post data
                String str_email = Utils.user_current.getEmail();
                String str_sdt = Utils.user_current.getMobile();
                int id = Utils.user_current.getId();
                Log.d("test", new Gson().toJson(Utils.mangmuahang));
                //Goi api
                compositeDisposable.add(apiBanHang.createOrder(str_email, str_sdt, String.valueOf(tongtien), id, str_diachi, totalItem,new Gson().toJson(Utils.mangmuahang) )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                messageModel -> {
                                    if (messageModel.isSuccess()){
                                        Toast.makeText(getApplicationContext(), "Thanh cong", Toast.LENGTH_SHORT).show();
                                        Utils.mangmuahang.clear();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent) ;
                                        finish();

                                    }else {
                                        Log.d("loggg", "onClick: "+messageModel.getMessage());
                                        Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }


                                },
                                throwable ->{
                                    Log.d("loggg", throwable.getMessage());
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                        )
                );
            }
        }
    });
}


    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toobar);
        txttongtien = findViewById(R.id.txttongtien);
        txtsodt = findViewById(R.id.txtsodienthoai);
        txtemail = findViewById(R.id.txtemail);
        edtdiachi =findViewById(R.id.edtdiachi);
        btndathang = findViewById(R.id.btndathang);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();

    }
}