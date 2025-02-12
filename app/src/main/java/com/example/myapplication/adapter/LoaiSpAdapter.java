package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.LoaiSp;
// Hiển thị danh sách các mục Loại sản phẩm
public class LoaiSpAdapter extends BaseAdapter {
    List<LoaiSp> array;
    Context context; // cung cấp thông tin về môi trường hiện tại của ứng dụng và cung cấp quyền truy cập đến các
    // tài nguyên cần thiết như layout, strings, colors
    // Khơi tạo Adapter với ds các mục Loại sản phẩm context của Activity hoặc Fragment hiện tại
    public  LoaiSpAdapter(Context context,List<LoaiSp> array){
        this.array = array;
        this.context = context;
    }
    //Trả về số lượng phần tử trong dánh sách
    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    //lưu trữ các tham chiếu đến các phần tử giao diện người dùng
    public class ViewHolder{
        // tăng hiệu suất cuộn của ListView bằng cách tái sử dụng các tham chiếu giao diện
        //hay vì phải tìm lại chúng bằng cách gọi findViewById mỗi khi một hàng được vẽ.
        TextView textensp;
        ImageView imhinhanh;

    }
    //được gọi khi mỗi hàng trong ListView được vẽ.
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder =null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_sanpham, null);
            viewHolder.textensp = view.findViewById(R.id.item_tensp);
            viewHolder.imhinhanh = view.findViewById(R.id.item_image);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textensp.setText(array.get(i).getTensanpham());
        Glide.with(context).load(array.get(i).getHinhanh()).into(viewHolder.imhinhanh);
        return view;
    }
}
