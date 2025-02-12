package com.example.myapplication.retrofit;

//import android.database.Observable;

import com.example.myapplication.model.DonHangModel;
import com.example.myapplication.model.LoaiSpModel;
import com.example.myapplication.model.MessageModel;
import com.example.myapplication.model.SanPhamMoiModel;
import com.example.myapplication.model.UserModel;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.POST;
//Đây là một phần quan trọng của việc sử dụng Retrofit để tạo các yêu cầu HTTP
public interface ApiBanHang {
    @GET("getloaisp.php")//Lấy ds các loại sản phẩm
    Observable<LoaiSpModel> getLoaiSp();
    @GET("getspmoi.php")
    Observable<SanPhamMoiModel> getSpMoi();
    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPham(
            @Field("page") int page,
            @Field("loai") int loai
    );
    @POST("dangki.php")
    @FormUrlEncoded
    Observable<UserModel>dangKi(
            @Field("email")String email,
            @Field("pass")String pass,
            @Field("username") String username,
            @Field("mobile") String mobile,
            @Field("uid") String uid

    );
    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel>dangNhap(
            @Field("email")String email,
            @Field("pass")String pass
    );
    @POST("reset.php")
    @FormUrlEncoded
    Observable<UserModel>resetPass(
            @Field("email")String email
    );
    @POST("donhang.php")
    @FormUrlEncoded
    Observable<MessageModel>createOrder(
            @Field("email")String email,
            @Field("sdt")String sdt,
            @Field("tongtien")String tongtien,
            @Field("iduser")int id,
            @Field("diachi")String diachi,
            @Field("soluong")int soluong,
            @Field("chitiet") String chitiet
    );
    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel>xemDonHang(
            @Field("iduser")String id
    );
    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel>search(
            @Field("search")String search
    );
}
