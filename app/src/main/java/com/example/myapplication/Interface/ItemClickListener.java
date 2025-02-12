package com.example.myapplication.Interface;

import android.view.View;
//giúp tăng khả năng tái sử dụng và kiểm thử.
public interface ItemClickListener {
    void onClick(View view, int pos, boolean isLongClick);
    //view : Đối tượng View được click
    // pos: Vị trí của item trong danh sách được click
    //isLongClick: Biến boolean để xác định xem sự kiện click có phải là một click dài hay không
}
