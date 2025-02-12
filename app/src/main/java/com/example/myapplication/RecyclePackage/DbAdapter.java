package com.example.myapplication.RecyclePackage;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import  android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication.R;
import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DbAdapter extends RecyclerView.Adapter<DbAdapter.DbViewHolder> {
    ArrayList<DbModelClass> objDbModelClassArrayList;

    public DbAdapter(ArrayList<DbModelClass> objDbModelClassArrayList) {
        this.objDbModelClassArrayList = objDbModelClassArrayList;
    }

    @NonNull
    @Override
    public DbViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View singleRow = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_row, viewGroup, false);
        return new DbViewHolder(singleRow);

    }

    @Override
    public void onBindViewHolder(@NonNull DbViewHolder dbViewHolder, int i) {
        DbModelClass objDbModelClass = objDbModelClassArrayList.get(i);
        dbViewHolder.imageDesTV.setText(objDbModelClass.getImageDes());
        dbViewHolder.ourImage.setImageBitmap(objDbModelClass.getOurImage());
    }

    @Override
    public int getItemCount() {
        if (objDbModelClassArrayList != null) {
            return objDbModelClassArrayList.size();
        } else {
            return 0; // hoặc số lượng mặc định bạn muốn trả về khi danh sách là null
        }
    }
    public static class DbViewHolder extends  RecyclerView.ViewHolder{
        TextView imageDesTV;
        ImageView ourImage;

        public DbViewHolder(@NonNull View itemView) {
            super(itemView);
            imageDesTV = itemView.findViewById(R.id.sr_imageDes);
            ourImage = itemView.findViewById(R.id.sr_image);
        }
    }
}
