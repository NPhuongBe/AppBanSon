package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapter.LoaiSpAdapter;
import com.example.myapplication.adapter.SanPhamMoiAdapter;
import com.example.myapplication.model.LoaiSp;
import com.example.myapplication.model.LoaiSpModel;
import com.example.myapplication.model.SanPhamMoi;
import com.example.myapplication.model.SanPhamMoiModel;
import com.example.myapplication.model.User;
import com.example.myapplication.retrofit.ApiBanHang;
import com.example.myapplication.retrofit.RetrofitClient;
import com.example.myapplication.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nex3z.notificationbadge.NotificationBadge;


import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import android.database.sqlite.SQLiteDatabase;

import android.content.ContentValues;

import android.net.Uri;



public class MainActivity extends AppCompatActivity {

    Toolbar toolbar; // tạo thanh công cụ ở đầu màn hình
    ViewFlipper viewFliper; // hiệu ứng chuyển đổi giữa các hình ảnh
    RecyclerView recyclerViewManHinhChinh; // hiển thị danh sách các sản phẩm
    NavigationView navigationView; // tạo menu điều hướng
    ListView listViewManHinhChinh; //Hiển thị danh sách các loại sản phẩm và các điều hướng khác
    DrawerLayout drawerLayout; // tạo thanh điều hướng trượt
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable(); // Quản lý các luồng RxJava
    ApiBanHang apiBanHang; //Api và Retrofit để tương tác với Api từ server
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout; // Hiển thị giỏ hàng
    ImageView imgsearch; //tạo nút tìm kiếm
    @Override
    // Bắt buộc, là 1 phương thức quan trọng trong vòng đời của 1 Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);// Đặt layout cho Activity bằng cách chỉ định tài nguyên layout được định nghĩa trong tệp XML
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        //Tạo đtg RetrofitClient và sử dụng nó để tạo một đối tượng API
        //kiểm tra xem liệu có dữ liệu người dùng đã được lưu trữ trong Paper không
        Paper.init(this);
        if(Paper.book().read("user") != null){
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }
        Anhxa();
        ActionBar();
//Kiểm tra kết nối Internet
        if(isConnected(this)){
            //Hiển thị thông báo
            Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
            ActionViewFlipper();
            getLoaiSanPham();
            getSpMoi();
            getEventClick(); //bắt sự kiện click vào loại sản phẩm
        }
        else{
            Toast.makeText(getApplicationContext(), "Khong co internet", Toast.LENGTH_LONG).show();

        }
    }


    //Bắt sự kiện cho menu chuyển màn hình
    private void getEventClick() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent sonkem = new Intent(getApplicationContext(), SonKemActivity.class);
                        sonkem.putExtra("loai", 1);
                        startActivity(sonkem);
                        break;
                    case 2:
                        Intent sonduong = new Intent(getApplicationContext(), SonKemActivity.class);
                        sonduong.putExtra("loai", 2);
                        startActivity(sonduong);
                        break;
                    case 4:
                        // Mở trang Facebook
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com"));
                        startActivity(intent);
                        break;



                    case 5:
                        Intent donhang = new Intent(getApplicationContext(), XemDonActivity.class);

                        startActivity(donhang);
                        break;

                    case 6:
                        //Xóa key user
                        Paper.book().delete("user");
                        Intent dangnhap = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(dangnhap);
                        finish();
                        break;
                }
            }
        });
    }

    private void getSpMoi() {
        //Gửi yêu cầu đến máy chủ để lấy ds sản phẩm mới bằng Retrofit
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())//Xác định Scheduler mà Observable sẽ sử dụng để thực hiện các hoạt động trên luồng I/O
                //để tránh làm chậm luồng chính của ứng dụng
                .observeOn(AndroidSchedulers.mainThread())//Xác định Scheduler mà Observer sẽ sử dụng để nhận kết quả trên luồng chính (main thread)
                // của ứng dụng để có thể cập nhật giao diện người dùng
                .subscribe(
                        //Đăng ký một Observer để nhận các sự kiện
                        //sự kiện nhận được là một SanPhamMoiModel
                        sanPhamMoiModel -> {
                            //xử lý kết quả trả về từ yêu cầu lấy dữ liệu từ máy chủ
                            if(sanPhamMoiModel.isSuccess()){
                                mangSpMoi = sanPhamMoiModel.getResult();
                 //lấy danh sách sản phẩm mới từ sanPhamMoiModel thông qua phương thức getResult() và gán vào biến mangSpMoi
                                spAdapter = new SanPhamMoiAdapter(getApplicationContext(), mangSpMoi);
                                //Tạo một adapter mới để hiển thị danh sách sản phẩm mới. Constructor của SanPhamMoiAdapter nhận context và danh sách sản phẩm mới làm tham số
                                recyclerViewManHinhChinh.setAdapter(spAdapter);
                               // Gán adapter vừa tạo vào RecyclerView để hiển thị danh sách sản phẩm mới lên giao diện người dùng
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Khong ket noi duoc voi server"+throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                )
        );
    }
//Lấy dữ liệu từ server để load ra navigation
    private void getLoaiSanPham(){
        compositeDisposable.add(apiBanHang.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel ->{
                            if(loaiSpModel.isSuccess()){
                                //Toast.makeText(getApplicationContext(), loaiSpModel.getResult().get(0).getTensanpham(), Toast.LENGTH_LONG).show();
                                mangloaisp = loaiSpModel.getResult();

                                mangloaisp.add(new LoaiSp("Đăng xuất","https://e7.pngegg.com/pngimages/221/392/png-clipart-power-button-logo-computer-icons-button-scalable-graphics-crystal-clear-action-exit-miscellaneous-application-software.png"));
                                loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(), mangloaisp);
                                listViewManHinhChinh.setAdapter(loaiSpAdapter);
                            }
                        }
                )
        );
    }

//Chạy quảng cáo
    private void ActionViewFlipper(){
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://4men.com.vn/attachments/end-of-season-sale.jpg");
        mangquangcao.add("https://caodang.fpt.edu.vn/wp-content/uploads/Screen-Shot-2022-03-04-at-12.20.36.png");
        mangquangcao.add("https://yumeisakura.vn/uploadImages/2021/1000x300.jpg");
        mangquangcao.add("https://png.pngtree.com/thumb_back/fw800/background/20230715/pngtree-d-render-of-lipstick-mockup-on-geometric-pink-background-for-a-image_3889313.jpg");
        for(int i = 0; i<mangquangcao.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFliper.addView(imageView);


        }
        viewFliper.setFlipInterval(3000);
        viewFliper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFliper.setInAnimation(slide_in);
        viewFliper.setInAnimation(slide_out);
    }
   // cài đặt thanh công cụ hành động (Action Bar) cho Activity. Nút quay lại
    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Hiển thị nút back
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    //ánh xạ các thành phần giao diện người dùng từ layout XML vào các biến trong mã nguồn
    private void Anhxa(){
        imgsearch = findViewById(R.id.imgsearch);
        toolbar = findViewById(R.id.toobarmanhinhchinh);
        viewFliper = findViewById(R.id.viewlipper);
        recyclerViewManHinhChinh = findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewManHinhChinh.setLayoutManager(layoutManager);
        recyclerViewManHinhChinh.setHasFixedSize(true);
        listViewManHinhChinh = findViewById(R.id.listviewmanhinhchinh);
        navigationView = findViewById(R.id.navigationview);
        drawerLayout = findViewById(R.id.dramerLayout);
        badge = findViewById(R.id.menu_sl);
        frameLayout= findViewById(R.id.framegiohang);
        //Khởi tạo list
        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
        //Xư lý giỏ hàng
        if(Utils.manggiohang == null){
            Utils.manggiohang = new ArrayList<>();
        }else{
            int totalItem = 0;
            for(int i = 0; i<Utils.manggiohang.size(); i++) {
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

    }
//Hiển thị số lượng của giỏ hàng
    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for(int i = 0; i<Utils.manggiohang.size(); i++) {
            totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }
// Kiểm tra có internet hay không
    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); //Nhớ thêm quyền vào
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi !=null && wifi.isConnected()) ||(mobile != null && mobile.isConnected())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}