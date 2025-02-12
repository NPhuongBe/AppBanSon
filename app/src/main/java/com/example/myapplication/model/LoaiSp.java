package com.example.myapplication.model;
//đại diện cho một loại sản phẩm
public class LoaiSp {
    //3 thuộc tính
    int id;
    String tensanpham;
    String hinhanh;

    public LoaiSp(String tensanpham, String hinhanh) {
        //khởi tạo một đối tượng LoaiSp với các thông tin cần thiết, đó là tên sản phẩm và đường dẫn hình ảnh
        this.tensanpham = tensanpham;
        this.hinhanh = hinhanh;
    }
// getter và setter cho mỗi thuộc tính để bạn có thể truy cập và thay đổi dữ liệu của mỗi đối tượng
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTensanpham() {
        return tensanpham;
    }

    public void setTensanpham(String tensanpham) {
        this.tensanpham = tensanpham;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }
}
