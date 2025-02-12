package com.example.myapplication.retrofit;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//
public class RetrofitClient {
    private static Retrofit instance;
    public static Retrofit getInstance(String baseUrl){
        //Đối số baseUrl là URL cơ sở cho API mà bạn sẽ giao tiếp
        if(instance==null){
            instance = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    //GsonConverterFactory được thêm vào để sử dụng Gson để chuyển đổi dữ liệu JSON thành các đối tượng Java
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    //RxJava3CallAdapterFactory được thêm vào để hỗ trợ sử dụng RxJava cho việc xử lý các cuộc gọi Retrofit bất đồng bộ
                    .build();

        }
        //trả về đối tượng Retrofit đã được cấu hình
        return  instance;
    }

}
