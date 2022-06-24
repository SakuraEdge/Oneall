package com.potato.oneall.ui.main;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.potato.timetable.R;

public class Eng_Chs extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eng_chs);
        EditText search = findViewById(R.id.search);
        TextView result = findViewById(R.id.result);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(v -> {
            String word = search.getText().toString();
            switch (word) {
                case "": {
                    String re_word = "你还未输入任何内容，请输入！";
                    result.setText(re_word);
                    break;
                }
                case "apple": {
                    String re_word = "检测到输入为英文，自动翻译为中文:\n\nn. 苹果";
                    result.setText(re_word);
                    break;
                }
                case "bag": {
                    String re_word = "检测到输入为英文，自动翻译为中文:\n\n" +
                            "n. 袋，包；一袋的量；大量，很多；丑妇；<旧>宽松的裤子；眼袋；一批，一套；爱好，品味；垒，垒包；全部猎物\n\n" +
                            "v. 把……装进袋子；抢占，占有；捕获，猎杀；批评，嘲笑；进球，得分；放弃，离开；给（病人）戴上氧气面罩；松垂，变形";
                    result.setText(re_word);
                    break;
                }
                case "创意": {
                    String re_word = "检测到输入为中文，自动翻译为英文:\n\ncreative\ncreativity\noriginality";
                    result.setText(re_word);
                    break;
                }
                case "人": {
                    String re_word = "检测到输入为中文，自动翻译为英文:\n\nperson\npeople\nhuman being";
                    result.setText(re_word);
                    break;
                }
                default:
                    result.setText("未找到匹配单词，请重试！");
                    break;
            }
        });

    }
}
