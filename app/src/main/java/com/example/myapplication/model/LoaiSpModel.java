package com.example.myapplication.model;

import com.example.myapplication.model.LoaiSp;

import java.util.List;
//đại diện cho dữ liệu trả về từ một API hoặc từ cơ sở dữ liệu
public class LoaiSpModel {
    boolean success;//Đánh dấu liệu kết quả có thành công hay không
    String message;//Thông điệp liên quan đến kết quả
    List<LoaiSp> result; //Danh sách các đối tượng

    public boolean isSuccess() {
        return success;
    }

    public void setSucces(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LoaiSp> getResult() {
        return result;
    }

    public void setResult(List<LoaiSp> result) {
        this.result = result;
    }
}
