package com.haibin.calendarviewproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarview.TrunkBranchAnnals;
import com.haibin.calendarviewproject.base.activity.BaseActivity;
import com.haibin.calendarviewproject.colorful.ColorfulMonthView;
import com.haibin.calendarviewproject.colorful.ColorfulWeekView;
import com.haibin.calendarviewproject.custom.CustomMonthView;
import com.haibin.calendarviewproject.custom.CustomWeekBar;
import com.haibin.calendarviewproject.custom.CustomWeekView;
import com.haibin.calendarviewproject.full.FullMonthView;
import com.haibin.calendarviewproject.full.FullWeekView;
import com.haibin.calendarviewproject.index.IndexMonthView;
import com.haibin.calendarviewproject.index.IndexWeekView;
import com.haibin.calendarviewproject.meizu.MeiZuMonthView;
import com.haibin.calendarviewproject.meizu.MeizuWeekView;
import com.haibin.calendarviewproject.mix.MixMonthView;
import com.haibin.calendarviewproject.mix.MixWeekBar;
import com.haibin.calendarviewproject.mix.MixWeekView;
import com.haibin.calendarviewproject.progress.ProgressMonthView;
import com.haibin.calendarviewproject.progress.ProgressWeekView;
import com.haibin.calendarviewproject.simple.SimpleMonthView;
import com.haibin.calendarviewproject.simple.SimpleWeekView;
import com.haibin.calendarviewproject.solay.SolarMonthView;
import com.haibin.calendarviewproject.solay.SolarWeekBar;
import com.haibin.calendarviewproject.solay.SolarWeekView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//主界面，也是一开始进入的页面
public class MainActivity extends BaseActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnCalendarLongClickListener,
        CalendarView.OnMonthChangeListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnWeekChangeListener,
        CalendarView.OnViewChangeListener,
        CalendarView.OnYearViewChangeListener,
        DialogInterface.OnClickListener {

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;
    CalendarLayout mLayout;
    LinearLayout mRoot;
    RelativeLayout mrl_tool;


    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;
    private int mCalendarHeight;

    //AlertDialog-(弹出式)对话框，置顶于所有界面元素之上，能够屏蔽掉其他控件的交互能力，因此一般用于提示一些非常重要的内容或者警告信息。
    private AlertDialog mMoreDialog;

    private AlertDialog mFuncDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        setStatusBarDarkMode();
        mTextMonthDay = findViewById(R.id.tv_month_day);
        mTextYear = findViewById(R.id.tv_year);
        mTextLunar = findViewById(R.id.tv_lunar);
        mCalendarHeight = dipToPx(this, 46);
        mRelativeTool = findViewById(R.id.rl_tool);
        mCalendarView = findViewById(R.id.calendarView);
        mLayout = findViewById(R.id.calendarLayout);
        mRoot = findViewById(R.id.root);
        mrl_tool = findViewById(R.id.rl_tool);
        mTextCurrentDay = findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        //动态更新点击监听器
        findViewById(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMoreDialog == null) {
                    mMoreDialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle(R.string.list_dialog_title)
                            //此处的响应listener为该类的Onclick函数
                            .setItems(R.array.list_dialog_items, MainActivity.this)
                            .create();
                }
                mMoreDialog.show();
            }
        });

        //功能支持响应
        final DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:         //Meizu
                                mCalendarView.clearSchemeDate();
                                initCustomScheme();
                                resetSettings();
                                mCalendarHeight = dipToPx(MainActivity.this, 55);
                                mCalendarView.setCalendarItemHeight(mCalendarHeight);
                                mCalendarView.setWeekView(MeizuWeekView.class);
                                mCalendarView.setMonthView(MeiZuMonthView.class);
                                mCalendarView.setWeekBar(CustomWeekBar.class);
                                break;
                            case 1:         //Custom
                                mCalendarView.clearSchemeDate();
                                initCustomScheme();
                                resetSettings();
                                mCalendarHeight = dipToPx(MainActivity.this, 55);
                                mCalendarView.setCalendarItemHeight(mCalendarHeight);
                                mCalendarView.setWeekView(CustomWeekView.class);
                                mCalendarView.setMonthView(CustomMonthView.class);
                                mCalendarView.setWeekBar(CustomWeekBar.class);
                                break;
                            case 2:         //Colorful
                                mCalendarView.clearSchemeDate();
                                initNormalScheme();
                                resetSettings();
                                mCalendarHeight = dipToPx(MainActivity.this, 55);
                                mCalendarView.setCalendarItemHeight(mCalendarHeight);
                                mCalendarView.setWeekView(ColorfulWeekView.class);
                                mCalendarView.setMonthView(ColorfulMonthView.class);
                                mCalendarView.setWeekBar(CustomWeekBar.class);
                                break;
                            case 3:         //Full
                                mCalendarView.clearSchemeDate();
                                initFullScheme();
                                resetSettings();
                                mCalendarHeight = dipToPx(MainActivity.this, 85);
                                mCalendarView.setCalendarItemHeight(mCalendarHeight);
                                mCalendarView.setWeekView(FullWeekView.class);
                                mCalendarView.setMonthView(FullMonthView.class);
                                mCalendarView.setWeekBar(EnglishWeekBar.class);
                                break;
                            case 4:         //Mix
                                mCalendarView.clearSchemeDate();
                                initCustomScheme();
                                resetSettings();
                                mCalendarHeight = dipToPx(MainActivity.this, 55);
                                mCalendarView.setCalendarPaddingLeft(100);
                                mCalendarView.setCalendarItemHeight(mCalendarHeight);
                                mCalendarView.setWeekView(MixWeekView.class);
                                mCalendarView.setMonthView(MixMonthView.class);
                                mCalendarView.setWeekBar(MixWeekBar.class);
                                mCalendarView.postInvalidate();
                                break;
                            case 5:         //Index
                                mCalendarView.clearSchemeDate();
                                initNormalScheme();
                                resetSettings();
                                mCalendarHeight = dipToPx(MainActivity.this, 55);
                                mCalendarView.setCalendarItemHeight(mCalendarHeight);
                                mCalendarView.setWeekView(IndexWeekView.class);
                                mCalendarView.setMonthView(IndexMonthView.class);
                                mCalendarView.setWeekBar(CustomWeekBar.class);
                                break;
                            case 6:         //Simple
                                mCalendarView.clearSchemeDate();
                                initNormalScheme();
                                resetSettings();
                                mCalendarHeight = dipToPx(MainActivity.this, 55);
                                mCalendarView.setCalendarItemHeight(mCalendarHeight);
                                mCalendarView.setWeekView(SimpleWeekView.class);
                                mCalendarView.setMonthView(SimpleMonthView.class);
                                mCalendarView.setWeekBar(EnglishWeekBar.class);
                                break;
                            case 7:         //Solar
                                mCalendarView.clearSchemeDate();
                                initSolarScheme();
                                resetSettings();
                                if (Build.VERSION.SDK_INT >= 21) {
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.solar_background));
                                }
                                mCalendarView.setBackgroundResource(R.color.solar_background);
                                mLayout.setBackgroundResource(R.color.solar_background);
                                //mRoot.setBackgroundResource(R.color.solar_background);
                                mrl_tool.setBackgroundResource(R.color.solar_background);
                                mCalendarHeight = dipToPx(MainActivity.this, 55);
                                mCalendarView.setCalendarItemHeight(mCalendarHeight);
                                mCalendarView.setWeekView(SolarWeekView.class);
                                mCalendarView.setMonthView(SolarMonthView.class);
                                mCalendarView.setWeekBar(SolarWeekBar.class);
                                break;
                            case 8:         //Progress
                                mCalendarView.clearSchemeDate();
                                initProgressScheme();
                                resetSettings();
                                mCalendarHeight = dipToPx(MainActivity.this, 55);
                                mCalendarView.setCalendarItemHeight(mCalendarHeight);
                                mCalendarView.setWeekView(ProgressWeekView.class);
                                mCalendarView.setMonthView(ProgressMonthView.class);
                                mCalendarView.setWeekBar(CustomWeekBar.class);
                                break;
                        }
                    }
                };

        //功能支持点击监听器
        findViewById(R.id.iv_func).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFuncDialog == null) {
                    mFuncDialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle(R.string.func_dialog_title)
                            //此处的响应listener为上面定义的listener
                            .setItems(R.array.func_dialog_items, listener)
                            .create();
                }
                mFuncDialog.show();
            }
        });

        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mCalendarView.setOnCalendarLongClickListener(this, true);
        mCalendarView.setOnWeekChangeListener(this);
        mCalendarView.setOnYearViewChangeListener(this);

        mCalendarView.setOnViewChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    @SuppressWarnings("unused")
    @Override
    protected void initData() {

        final int year = mCalendarView.getCurYear();
        final int month = mCalendarView.getCurMonth();

        initNormalScheme();

    }

    //动态更新的响应
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                mCalendarView.setWeekStarWithSun();
                break;
            case 1:
                mCalendarView.setWeekStarWithMon();
                break;
            case 2:
                mCalendarView.setWeekStarWithSat();
                break;
            case 3:
                if (mCalendarView.isSingleSelectMode()) {
                    mCalendarView.setSelectDefaultMode();
                } else {
                    mCalendarView.setSelectSingleMode();
                }
                break;
            case 4:
                mCalendarView.scrollToCurrent(true);
                break;
            case 5:
                mCalendarView.setAllMode();
                break;
            case 6:
                mCalendarView.setOnlyCurrentMode();
                break;
            case 7:
                mCalendarView.setFixMode();
                break;
        }
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }


    @Override
    public void onCalendarOutOfRange(Calendar calendar) {
        //Toast是用于显示消息的一个窗口，会自动淡出
        Toast.makeText(this, String.format("%s : OutOfRange", calendar), Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        //Log.e("onDateSelected", "  -- " + calendar.getYear() + "  --  " + calendar.getMonth() + "  -- " + calendar.getDay());
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        //以下均为日志记录
//        Log.e("lunar "," --  " + calendar.getLunarCalendar().toString() + "\n" +
//        "  --  " + calendar.getLunarCalendar().getYear());
        Log.e("onDateSelected", "  -- " + calendar.getYear() +
                "  --  " + calendar.getMonth() +
                "  -- " + calendar.getDay() +
                "  --  " + isClick + "  --   " + calendar.getScheme());
        Log.e("onDateSelected", "  " + mCalendarView.getSelectedCalendar().getScheme() +
                "  --  " + mCalendarView.getSelectedCalendar().isCurrentDay());
        Log.e("干支年纪 ： ", " -- " + TrunkBranchAnnals.getTrunkBranchYear(calendar.getLunarCalendar().getYear()));
    }

    @Override
    public void onCalendarLongClickOutOfRange(Calendar calendar) {
        Toast.makeText(this, String.format("%s : LongClickOutOfRange", calendar), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCalendarLongClick(Calendar calendar) {
        Toast.makeText(this, "长按显示详情\n" + getCalendarText(calendar), Toast.LENGTH_SHORT).show();
    }

    //点击/长按显示的文本
    private static String getCalendarText(Calendar calendar) {
        return String.format("新历%s \n 农历%s \n 公历节日：%s \n 农历节日：%s \n 节气：%s \n 是否闰月：%s",
                calendar.getMonth() + "月" + calendar.getDay() + "日",
                calendar.getLunarCalendar().getMonth() + "月" + calendar.getLunarCalendar().getDay() + "日",
                TextUtils.isEmpty(calendar.getGregorianFestival()) ? "无" : calendar.getGregorianFestival(),
                TextUtils.isEmpty(calendar.getTraditionFestival()) ? "无" : calendar.getTraditionFestival(),
                TextUtils.isEmpty(calendar.getSolarTerm()) ? "无" : calendar.getSolarTerm(),
                calendar.getLeapMonth() == 0 ? "否" : String.format("闰%s月", calendar.getLeapMonth()));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMonthChange(int year, int month) {
        Log.e("onMonthChange", "  -- " + year + "  --  " + month);
        Calendar calendar = mCalendarView.getSelectedCalendar();
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
    }

    @Override
    public void onViewChange(boolean isMonthView) {
        Log.e("onViewChange", "  ---  " + (isMonthView ? "月视图" : "周视图"));
    }


    @Override
    public void onWeekChange(List<Calendar> weekCalendars) {
        for (Calendar calendar : weekCalendars) {
            Log.e("onWeekChange", calendar.toString());
        }
    }

    @Override
    public void onYearViewChange(boolean isClose) {
        Log.e("onYearViewChange", "年视图 -- " + (isClose ? "关闭" : "打开"));
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
        Log.e("onYearChange", " 年份变化 " + year);
    }

    public void initNormalScheme() {

        Map<String, Calendar> map = new HashMap<>();
        for (int y = 1997; y < 2082; y++) {
            for (int m = 1; m <= 12; m++) {

                map.put(getSchemeCalendar(y, m, 3, 0xFF40db25, "假").toString(),
                        getSchemeCalendar(y, m, 3, 0xFF40db25, "假"));
                map.put(getSchemeCalendar(y, m, 6, 0xFFe69138, "事").toString(),
                        getSchemeCalendar(y, m, 6, 0xFFe69138, "事"));
                map.put(getSchemeCalendar(y, m, 9, 0xFFdf1356, "议").toString(),
                        getSchemeCalendar(y, m, 9, 0xFFdf1356, "议"));
                map.put(getSchemeCalendar(y, m, 13, 0xFFedc56d, "记").toString(),
                        getSchemeCalendar(y, m, 13, 0xFFedc56d, "记"));
                map.put(getSchemeCalendar(y, m, 14, 0xFFedc56d, "记").toString(),
                        getSchemeCalendar(y, m, 14, 0xFFedc56d, "记"));
                map.put(getSchemeCalendar(y, m, 15, 0xFFaacc44, "假").toString(),
                        getSchemeCalendar(y, m, 15, 0xFFaacc44, "假"));
                map.put(getSchemeCalendar(y, m, 18, 0xFFbc13f0, "记").toString(),
                        getSchemeCalendar(y, m, 18, 0xFFbc13f0, "记"));
                map.put(getSchemeCalendar(y, m, 25, 0xFF13acf0, "假").toString(),
                        getSchemeCalendar(y, m, 25, 0xFF13acf0, "假"));
                map.put(getSchemeCalendar(y, m, 27, 0xFF13acf0, "多").toString(),
                        getSchemeCalendar(y, m, 27, 0xFF13acf0, "多"));

            }
        }

        mCalendarView.setSchemeDate(map);
    }

    public void initProgressScheme() {

        Map<String, Calendar> map = new HashMap<>();
        for (int y = 1997; y < 2082; y++) {
            for (int m = 1; m <= 12; m++) {

                map.put(getSchemeCalendar(y, m, 3, 0xFF40db25, "20").toString(),
                        getSchemeCalendar(y, m, 3, 0xFF40db25, "20"));
                map.put(getSchemeCalendar(y, m, 6, 0xFFe69138, "33").toString(),
                        getSchemeCalendar(y, m, 6, 0xFFe69138, "33"));
                map.put(getSchemeCalendar(y, m, 9, 0xFFdf1356, "25").toString(),
                        getSchemeCalendar(y, m, 9, 0xFFdf1356, "25"));
                map.put(getSchemeCalendar(y, m, 13, 0xFFedc56d, "50").toString(),
                        getSchemeCalendar(y, m, 13, 0xFFedc56d, "50"));
                map.put(getSchemeCalendar(y, m, 14, 0xFFedc56d, "80").toString(),
                        getSchemeCalendar(y, m, 14, 0xFFedc56d, "80"));
                map.put(getSchemeCalendar(y, m, 15, 0xFFaacc44, "20").toString(),
                        getSchemeCalendar(y, m, 15, 0xFFaacc44, "20"));
                map.put(getSchemeCalendar(y, m, 18, 0xFFbc13f0, "70").toString(),
                        getSchemeCalendar(y, m, 18, 0xFFbc13f0, "70"));
                map.put(getSchemeCalendar(y, m, 25, 0xFF13acf0, "36").toString(),
                        getSchemeCalendar(y, m, 25, 0xFF13acf0, "36"));
                map.put(getSchemeCalendar(y, m, 27, 0xFF13acf0, "95").toString(),
                        getSchemeCalendar(y, m, 27, 0xFF13acf0, "95"));

            }
        }

        mCalendarView.setSchemeDate(map);
    }

    public void initFullScheme() {

        Map<String, Calendar> map = new HashMap<>();
        for (int y = 1997; y < 2082; y++) {
            for (int m = 1; m <= 12; m++) {

                map.put(getFullSchemeCalendar(y, m, 3, 0xFF40db25, "假").toString(),
                        getFullSchemeCalendar(y, m, 3, 0xFF40db25, "假"));
                map.put(getFullSchemeCalendar(y, m, 6, 0xFFe69138, "事").toString(),
                        getFullSchemeCalendar(y, m, 6, 0xFFe69138, "事"));
                map.put(getFullSchemeCalendar(y, m, 9, 0xFFdf1356, "议").toString(),
                        getFullSchemeCalendar(y, m, 9, 0xFFdf1356, "议"));
                map.put(getFullSchemeCalendar(y, m, 13, 0xFFedc56d, "记").toString(),
                        getFullSchemeCalendar(y, m, 13, 0xFFedc56d, "记"));
                map.put(getFullSchemeCalendar(y, m, 14, 0xFFedc56d, "记").toString(),
                        getFullSchemeCalendar(y, m, 14, 0xFFedc56d, "记"));
                map.put(getFullSchemeCalendar(y, m, 15, 0xFFaacc44, "假").toString(),
                        getFullSchemeCalendar(y, m, 15, 0xFFaacc44, "假"));
                map.put(getFullSchemeCalendar(y, m, 18, 0xFFbc13f0, "记").toString(),
                        getFullSchemeCalendar(y, m, 18, 0xFFbc13f0, "记"));
                map.put(getFullSchemeCalendar(y, m, 25, 0xFF13acf0, "假").toString(),
                        getFullSchemeCalendar(y, m, 25, 0xFF13acf0, "假"));
                map.put(getFullSchemeCalendar(y, m, 27, 0xFF13acf0, "多").toString(),
                        getFullSchemeCalendar(y, m, 27, 0xFF13acf0, "多"));

            }
        }

        mCalendarView.setSchemeDate(map);
    }

    public void initCustomScheme() {

        Map<String, Calendar> map = new HashMap<>();
        for (int y = 1997; y < 2082; y++) {
            for (int m = 1; m <= 12; m++) {

                map.put(getCustomSchemeCalendar(y, m, 3, 0xFF40db25, "假").toString(),
                        getCustomSchemeCalendar(y, m, 3, 0xFF40db25, "假"));
                map.put(getCustomSchemeCalendar(y, m, 6, 0xFFe69138, "事").toString(),
                        getCustomSchemeCalendar(y, m, 6, 0xFFe69138, "事"));
                map.put(getCustomSchemeCalendar(y, m, 9, 0xFFdf1356, "议").toString(),
                        getCustomSchemeCalendar(y, m, 9, 0xFFdf1356, "议"));
                map.put(getCustomSchemeCalendar(y, m, 13, 0xFFedc56d, "记").toString(),
                        getCustomSchemeCalendar(y, m, 13, 0xFFedc56d, "记"));
                map.put(getCustomSchemeCalendar(y, m, 14, 0xFFedc56d, "记").toString(),
                        getCustomSchemeCalendar(y, m, 14, 0xFFedc56d, "记"));
                map.put(getCustomSchemeCalendar(y, m, 15, 0xFFaacc44, "假").toString(),
                        getCustomSchemeCalendar(y, m, 15, 0xFFaacc44, "假"));
                map.put(getCustomSchemeCalendar(y, m, 18, 0xFFbc13f0, "记").toString(),
                        getCustomSchemeCalendar(y, m, 18, 0xFFbc13f0, "记"));
                map.put(getCustomSchemeCalendar(y, m, 25, 0xFF13acf0, "假").toString(),
                        getCustomSchemeCalendar(y, m, 25, 0xFF13acf0, "假"));
                map.put(getCustomSchemeCalendar(y, m, 27, 0xFF13acf0, "多").toString(),
                        getCustomSchemeCalendar(y, m, 27, 0xFF13acf0, "多"));

            }
        }

        mCalendarView.setSchemeDate(map);
    }

    public void initSolarScheme() {

        Map<String, Calendar> map = new HashMap<>();
        for (int y = 1997; y < 2082; y++) {
            for (int m = 1; m <= 12; m++) {

                map.put(getSolarSchemeCalendar(y, m, 3, "假").toString(),
                        getSolarSchemeCalendar(y, m, 3, "假"));
                map.put(getSolarSchemeCalendar(y, m, 6, "事").toString(),
                        getSolarSchemeCalendar(y, m, 6, "事"));
                map.put(getSolarSchemeCalendar(y, m, 9, "议").toString(),
                        getSolarSchemeCalendar(y, m, 9, "议"));
                map.put(getSolarSchemeCalendar(y, m, 13, "记").toString(),
                        getSolarSchemeCalendar(y, m, 13, "记"));
                map.put(getSolarSchemeCalendar(y, m, 14, "记").toString(),
                        getSolarSchemeCalendar(y, m, 14, "记"));
                map.put(getSolarSchemeCalendar(y, m, 15, "假").toString(),
                        getSolarSchemeCalendar(y, m, 15, "假"));
                map.put(getSolarSchemeCalendar(y, m, 18, "记").toString(),
                        getSolarSchemeCalendar(y, m, 18, "记"));
                map.put(getSolarSchemeCalendar(y, m, 25, "假").toString(),
                        getSolarSchemeCalendar(y, m, 25, "假"));
                map.put(getSolarSchemeCalendar(y, m, 27, "多").toString(),
                        getSolarSchemeCalendar(y, m, 27, "多"));

            }
        }

        mCalendarView.setSchemeDate(map);
    }

    private Calendar getFullSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = getSchemeCalendar(year, month, day, color, text);
        calendar.addScheme(color, "假");
        calendar.addScheme(day%2 == 0 ? 0xFF00CD00 : 0xFFD15FEE, "节");
        calendar.addScheme(day%2 == 0 ? 0xFF660000 : 0xFF4169E1, "记");
        return calendar;
    }

    private Calendar getCustomSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = getSchemeCalendar(year, month, day, color, text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }

    private Calendar getSolarSchemeCalendar(int year, int month, int day, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(Color.WHITE);
        calendar.setScheme(text);
        calendar.addScheme(0xFFa8b015, "rightTop");
        calendar.addScheme(0xFF423cb0, "leftTop");
        calendar.addScheme(0xFF643c8c, "bottom");
        return calendar;
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void resetSettings() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        mCalendarView.setBackgroundResource(R.color.colorPrimary);
        mLayout.setBackgroundResource(R.color.colorPrimary);
        //mRoot.setBackgroundResource(R.color.colorPrimary);
        mrl_tool.setBackgroundResource(R.color.colorPrimary);
        mCalendarView.setCalendarPaddingLeft(0);
    }
}