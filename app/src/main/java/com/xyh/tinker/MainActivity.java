package com.xyh.tinker;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xyh.annotation.BindPath;
import com.xyh.tinker.bug.Bug;
import com.xyh.tinker.utils.ChannelUtil;
import com.xyh.tinker.utils.FixManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@BindPath("app/main")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //打印渠道提示
        Toast.makeText(this, "" + ChannelUtil.getChannel(this), Toast.LENGTH_SHORT).show();
    }


    public void fix(View view) {
        File odex = getDir("odex", Context.MODE_PRIVATE);
        String name = "output.dex";
        String filePath = new File(odex, name).getAbsolutePath();

        File file = new File(filePath);

        if (file.exists()) {
            file.delete();
        }

        //Input
        InputStream is = null;
        FileOutputStream os = null;


        try {
            is = new FileInputStream(new File(Environment.getExternalStorageDirectory(), name));
            os = new FileOutputStream(filePath);

            int len = 0;
            byte[] buffer = new byte[1024];

            while ((len = is.read(buffer)) > -1) {
                os.write(buffer, 0, len);
            }

            File f = new File(filePath);


            FixManager.loadDex(getApplicationContext());


            Toast.makeText(this, "修复成功！", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert os != null;
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void calc(View view) {
        Bug.fix();
    }
}
