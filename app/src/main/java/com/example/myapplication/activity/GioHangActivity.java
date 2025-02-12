package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import com.example.myapplication.R;
import com.example.myapplication.adapter.GioHangAdapter;
import com.example.myapplication.model.EventBus.TinhTongEvent;
import com.example.myapplication.model.GioHang;
import com.example.myapplication.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {
    TextView giohangtrong,tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnmuahang;
    GioHangAdapter adapter;
    //chuyển thành biến toàn cục
    long tongtiensp = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        tinhTongTien();
        initControl();
    }
//    private void tinhTongTien() {
//        long tongtiensp = 0;
//        if (Utils.manggiohang != null) { // Kiểm tra xem manggiohang đã được khởi tạo chưa
//            for(int i = 0; i < Utils.manggiohang.size(); i++) {
//                tongtiensp += Utils.manggiohang.get(i).getGiasp() * Utils.manggiohang.get(i).getSoluong();
//            }
//        }
//        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
//        tongtien.setText(decimalFormat.format(tongtiensp));
//    }


//    private void tinhTongTien() {
//        long tongtiensp = 0;
//        for(int i = 0; i<Utils.manggiohang.size();i++){
//            tongtiensp = tongtiensp+(Utils.manggiohang.get(i).getGiasp()*Utils.manggiohang.get(i).getSoluong());
//        }
//        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
//
//        tongtien.setText(decimalFormat.format(tongtiensp));
//    }
private void tinhTongTien() {
    long tongtien1 = 0;
    for(int i = 0; i < Utils.mangmuahang.size(); i++){
        tongtien1 += Utils.mangmuahang.get(i).getGiasp() * Utils.mangmuahang.get(i).getSoluong();
    }

    double thueVAT = tongtien1 * 0.1;

    tongtiensp = tongtien1 + (long) thueVAT;

    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    tongtien.setText(decimalFormat.format(tongtiensp));
}



private void initControl() {
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
        Log.e("GioHangActivity", "ActionBar is null");
    }

    recyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);

    if (Utils.manggiohang.size() == 0) {
        giohangtrong.setVisibility(View.VISIBLE);
    } else {
        adapter = new GioHangAdapter(getApplicationContext(), Utils.manggiohang);
        recyclerView.setAdapter(adapter);
    }

    btnmuahang.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), ThanhToanActivity.class);
            //truyền vào tổng tiền
            intent.putExtra("tongtien", tongtiensp);
            Utils.manggiohang.clear();
            startActivity(intent);
        }
    });
}

    private void initView(){
        giohangtrong =findViewById(R.id.txtgiohangtrong);
        tongtien=findViewById(R.id.txttongtien);
        toolbar=findViewById(R.id.toobar);
        recyclerView=findViewById(R.id.recycleviewgiohang);
        btnmuahang=findViewById(R.id.btnmuahang);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent event){
        if(event != null){
            tinhTongTien();
        }
    }
}

