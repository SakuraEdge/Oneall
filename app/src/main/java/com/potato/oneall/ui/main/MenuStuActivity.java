package com.potato.oneall.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.potato.oneall.ui.course.ClassInfoActivity;
import com.potato.oneall.ui.course.ScoreStuInfoActivity;
import com.potato.timetable.R;
import com.potato.oneall.ui.param.DBHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.potato.oneall.ui.web.WebActivity;
import com.potato.oneall.ui.web.WebActivity3;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MenuStuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_student);

        List<Integer> images = new ArrayList<>();
        images.add(R.mipmap.banner_1);
        images.add(R.mipmap.banner_2);

        Banner banner = findViewById(R.id.banner);
        //设置内置样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new MenuStuActivity.GlideImageLoader());
        //设置图片网址或地址的集合
        banner.setImages(images);
        //设置轮播间隔时间
        banner.setDelayTime(2000);
        //设置是否为自动轮播 默认是 “是”
        banner.isAutoPlay(true);
        //设置显示器的位置   小点点 左中右
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start(); //一定不能缺start

        ImageButton schoolButton = findViewById(R.id.schoolButton);
        //跳转至官网
        ImageButton classInfoButton = findViewById(R.id.classInfoButton);
        //跳转至班级信息页面
        ImageButton scoreInfoButton = findViewById(R.id.scoreInfoButton);
        //跳转至成绩查询页面
        ImageButton tableButton = findViewById(R.id.tableButton);

        //登出
        ImageButton LoginOutButton = findViewById(R.id.login_out);


        //设置年月日
        TextView getTime = findViewById(R.id.getTime);
        String get_time = get_Time();
        getTime.setText(get_time);


        DBHelper dbHelper = new DBHelper(MenuStuActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        LoginOutButton.setOnClickListener(v -> {
            db.execSQL("update login_info set name = null");
            db.execSQL("update login_info set pwd = null");
            Toast.makeText(MenuStuActivity.this,"登出成功!",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(MenuStuActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        });

        tableButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MenuStuActivity.this,MainActivity.class);
            //跳转至课表界面
            startActivity(intent);
        });



        schoolButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MenuStuActivity.this, WebActivity.class);
            //跳转至官网界面
            startActivity(intent);
        });

        classInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MenuStuActivity.this, ClassInfoActivity.class);
            //跳转至班级信息界面
            startActivity(intent);
        });

        scoreInfoButton.setOnClickListener(v -> {
                Intent intent = new Intent();
                intent.setClass(MenuStuActivity.this, ScoreStuInfoActivity.class);
                //跳转至DIY界面
                startActivity(intent);
        });
    }


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setImageResource((Integer) path);
        }

        //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
        @Override
        public ImageView createImageView(Context context) {
            //使用fresco，需要创建它提供的ImageView，当然你也可以用自己自定义的具有图片加载功能的ImageView
            return new ImageView(context);
        }
    }


    /**
     * 获取当前时间
     */
    private String get_Time(){
        String times = "";
        Calendar i = Calendar.getInstance();
        times += i.get(Calendar.MONTH)+1+"月";
        times += i.get(Calendar.DAY_OF_MONTH)+"日 | ";
        int week = i.get(Calendar.DAY_OF_WEEK)-1;
        String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六","出错"};
        times += weeks[week];


        return times;
    }

//    /**
//     * 检测是否第一次启动
//     */
//    private void first_load(){
//        DBHelper dbHelper = new DBHelper(Main0Activity.this);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from param",null);
//        cursor.moveToFirst();
//        String first = cursor.getString(cursor.getColumnIndex("firstload"));
//        if (first.equals("1")) {
//            new AlertDialog.Builder(Main0Activity.this)
//                    .setTitle("系统提示:")
//                    .setMessage("你当前为首次使用壹软通，是否前往设置自定义网页内容？")
//                    .setPositiveButton("立即前往", (dialog, which) -> {
//                        Intent intent = new Intent();
//                        intent.setClass(Main0Activity.this, MenuSetting.class);
//                        //跳转至设置
//                        startActivity(intent);
//                    })
//                    .setNegativeButton("稍后设置", (dialog, which) ->
//                            Toast.makeText(Main0Activity.this,"以后可点击左下角进行设置",Toast.LENGTH_SHORT).show()).create().show();
//        }
//        db.execSQL("update param set firstload = '0' where param.id = '1'");
//    }


}
