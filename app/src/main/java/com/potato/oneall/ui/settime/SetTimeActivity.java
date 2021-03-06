package com.potato.oneall.ui.settime;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.potato.oneall.bean.Time;
import com.potato.oneall.util.Config;
import com.potato.oneall.util.Utils;
import com.potato.timetable.R;
import com.potato.oneall.ui.main.MainActivity;
import com.potato.oneall.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class SetTimeActivity extends AppCompatActivity {
    private OptionsPickerView<String> mOptionsPv;
    private final TimeAdapter timeAdapter = new TimeAdapter();
    private final List<String> timeList = new ArrayList<>(18 * 12);
    private final Time[] times = new Time[Config.getMaxClassNum()];
    public static final String EXTRA_UPDATE_Time = "time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);
        setActionBar();
        RecyclerView recyclerView = findViewById(R.id.rv_time_list);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(timeAdapter);

        initData();
        ImageView imageView = findViewById(R.id.iv_bg_set_time);
        Utils.setBackGround(this, imageView);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        for (int i = 0, len = times.length; i < len; i++) {
            try {
                if (MainActivity.sTimes == null || MainActivity.sTimes.length == 0) {
                    times[i] = new Time();
                } else {
                    times[i] = (Time) MainActivity.sTimes[i].clone();
                }

            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

        }
        for (int i = 6; i < 24; i++) {
            for (int j = 0; j < 12; j++) {
                timeList.add(Utils.formatTime(i) + ":" + Utils.formatTime(j * 5));
            }
        }
    }

    /**
     * 初始化ActionBar
     */
    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.set_time);
    }

    /**
     * 通知主界面更新
     */
    private void setUpdateResult() {
//        Log.d("settime", "通知更新");
        Intent intent = new Intent();
        intent.putExtra(EXTRA_UPDATE_Time, true);
        setResult(RESULT_OK, intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_set_time, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.menu_save) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("是否保存该时间表并退出?")
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                        new FileUtils<Time[]>().saveToJson(
                                SetTimeActivity.this,
                                times,
                                FileUtils.TIME_FILE_NAME);
                        MainActivity.sTimes = times;
                        //通知主界面更新
                        setUpdateResult();
                        finish();
                    })
                    .setNegativeButton("取消", null)
                    .create();
            alertDialog.show();
        } else if (id == R.id.menu_delete) {
            for (Time time : times) {
                time.setStart("");
                time.setEnd("");
            }
            timeAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showTimeSelectDialog(final TextView textView, final int index) {

        mOptionsPv = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            String start = timeList.get(options1);
            String end = timeList.get(options2);

            times[index].setStart(start);
            times[index].setEnd(end);
            textView.setText(times[index].toString());
        }).setOptionsSelectChangeListener((options1, options2, options3) -> {
            if (options1 >= options2) {
                mOptionsPv.setSelectOptions(options1, options1 + 9);
            }
        }).build();

        mOptionsPv.setTitleText("时间");
        mOptionsPv.setNPicker(timeList, timeList, null);
        if (!times[index].getEnd().isEmpty()) {
            mOptionsPv.setSelectOptions(getOptionsIndex(times[index].getStart()),
                    getOptionsIndex(times[index].getEnd()));
        } else if (index > 0 && !times[index - 1].getEnd().isEmpty()) {
            int option = getOptionsIndex(times[index - 1].getEnd()) + 2;//+2
            mOptionsPv.setSelectOptions(option, option + 9); //option + 9
        } else {
            mOptionsPv.setSelectOptions(0, 1);
        }
        mOptionsPv.show();
    }

    private int getOptionsIndex(String time) {
        String[] strings = time.split(":");
        int hour = Integer.parseInt(strings[0]);
        int minute = Integer.parseInt(strings[1]);
        return (hour - 6) * 12 + minute / 5;
    }

    private class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.classTv.setText("第 " + (position + 1) + " 节");
            holder.timeTv.setText(times[position].toString());
            if (!holder.linearLayout.hasOnClickListeners()) {
                holder.linearLayout.setOnClickListener(view -> showTimeSelectDialog(holder.timeTv, position));
            }
        }

        @Override
        public int getItemCount() {
            return times.length;
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView classTv;
            private final TextView timeTv;
            private final LinearLayout linearLayout;

            public ViewHolder(View view) {
                super(view);
                classTv = view.findViewById(R.id.tv_class_num);
                timeTv = view.findViewById(R.id.tv_time);
                linearLayout = view.findViewById(R.id.ll_set_time);
            }

        }
    }
}
