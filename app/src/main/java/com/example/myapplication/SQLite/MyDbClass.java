package com.example.myapplication.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.example.myapplication.RecyclePackage.DbModelClass;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
//tương tác với cơ sở dữ liệu SQLite
//SQLiteAssetHelper, là một lớp hỗ trợ cho việc sử dụng cơ sở dữ liệu SQLite được lưu trữ trong tài nguyên
public class MyDbClass extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "sonsetapp.db";
    private static final int DATABASE_VERSION = 1;
    Context context;
    public MyDbClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    //trả về một danh sách các đối tượng DbModelClass, đại diện cho tất cả dữ liệu trong bảng sonsetdata
    public ArrayList<DbModelClass> getAllData() {
        try {
            ArrayList<DbModelClass> objDbModelClassArrayList = new ArrayList<>();
//Mở cơ sở dữ liệu để đọc
            SQLiteDatabase objSqliteDatabase = getReadableDatabase();
            if (objSqliteDatabase != null) {
                //Cursor di chuyển qua các dòng kết quả của truy vấn và trích xuất dữ liệu từ cơ sở dữ liệu
                Cursor objCursor = objSqliteDatabase.rawQuery("select *from sonsetdata", null);
                if (objCursor.getCount() != 0) {
                    //Lặp qua kết quả của truy vấn và tạo các đối tượng
                    while (objCursor.moveToNext()) {
                        String imageDes = objCursor.getString(0);
                        byte[] imagesByte = objCursor.getBlob(1);
                        Bitmap ourImage = BitmapFactory.decodeByteArray(imagesByte, 0, imagesByte.length);
                        //Thêm vào danh sách
                        objDbModelClassArrayList.add(
                                new DbModelClass(
                                        imageDes, ourImage
                                )
                        );
                    }
                    return objDbModelClassArrayList;
                } else {
                    Toast.makeText(context, "No data...", Toast.LENGTH_SHORT).show();
                    return null;
                }
            } else {
                Toast.makeText(context, "Data is null...", Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (Exception e) {
            Toast.makeText(context, "getAllData:-" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
