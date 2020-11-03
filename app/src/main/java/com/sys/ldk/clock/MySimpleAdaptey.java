package com.sys.ldk.clock;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.sys.ldk.R;

import java.util.List;

public class MySimpleAdaptey extends BaseAdapter {
    private SQLiteDatabase db;
    private MyDatabaseHelper myDatabaseHelper;
    private List<ClockBean> list;

    private Context context;

    private TextView repeat;
    private TextView clockTime;
    private DrawableSwitch drawableSwitch;
    private TextView timedistance;
    private String time_distance;
    private ImageView head;
    private TextView clocktv;


    public MySimpleAdaptey(Context context, List<ClockBean> list) {
        this.context = context;
        this.list = list;
        myDatabaseHelper = new MyDatabaseHelper(context, "Items.db", null, 1);
        db = myDatabaseHelper.getWritableDatabase();

    }

    @Override
    public int getCount() {
        if (list != null && list.size() != 0) {
            return list.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.clockitemslayout, null, true);
        clockTime = convertView.findViewById(R.id.clockTime);
        repeat = convertView.findViewById(R.id.repeat);
        drawableSwitch = convertView.findViewById(R.id.drawableSwitch);
        timedistance = convertView.findViewById(R.id.timedistance);
        head = convertView.findViewById(R.id.head);
        clocktv = convertView.findViewById(R.id.clocktv);

        time_distance = new CalTime().cal(Integer.parseInt(list.get(position).getTime().split(":")[0]),
                Integer.parseInt(list.get(position).getTime().split(":")[1]));
        timedistance.setText(time_distance);
        if (list.size() != 0) {
            ClockBean clock = list.get(position);
            clockTime.setText(clock.getTime());
            repeat.setText(clock.getRepeat());
            if (clock.getIsSwitchOn() == 0) {
                drawableSwitch.setSwitchOn(false);
            } else {
                drawableSwitch.setSwitchOn(true);
            }
            switch (clock.getApptype()) {
                case "今日校园":
                    head.setImageResource(R.drawable.jinri);
                    clocktv.setText("今日校园");
                    break;
                case "学习强国":
                    head.setImageResource(R.drawable.xuexi);
                    clocktv.setText("学习强国");
                    break;
                case "蚂蚁森林":
                    head.setImageResource(R.drawable.mayi);
                    clocktv.setText("蚂蚁森林");
                    break;
            }
        }

        drawableSwitch.setListener(new DrawableSwitch.MySwitchStateChangeListener() {
            @Override
            public void mySwitchStateChanged(boolean isSwitchOn) {
                if (isSwitchOn) {
                    ContentValues cValue = new ContentValues();
                    cValue.put("isSwitchOn", 1);
                    try {
                        db.update("clocks", cValue, "id = ?", new String[]{list.get(position).getId() + ""});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ContentValues cValue = new ContentValues();
                    cValue.put("isSwitchOn", 0);
                    try {
                        db.update("clocks", cValue, "id = ?", new String[]{list.get(position).getId() + ""});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return convertView;
    }
}
