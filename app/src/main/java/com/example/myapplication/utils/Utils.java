package com.example.myapplication.utils;

import com.example.myapplication.model.GioHang;

import java.util.ArrayList;
import java.util.List;
import com.example.myapplication.model.User;
public class Utils {
    public static final String BASE_URL= "http://172.20.10.10/";
    //Đây là URL cơ sở của máy chủ hoặc API mà ứng dụng của bạn sẽ giao tiếp vớ
    public static List<GioHang> manggiohang;
    // danh sách (list) các mục trong giỏ hàng. Là 1 biến tĩnh ,có thể được truy cập từ bất kỳ đâu trong ứng dụng mà không cần tạo ra một đối tượng Utils mới.
    public static List<GioHang> mangmuahang = new ArrayList<>();
    //danh sách để lưu trữ các mục mà người dùng đã mua
    public  static User user_current = new User();
// Đối tượng User đại diện cho thông tin của người dùng hiện tại đang đăng nhập vào ứng dụng
}
