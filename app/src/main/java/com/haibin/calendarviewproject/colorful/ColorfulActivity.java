package com.haibin.calendarviewproject.colorful;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarviewproject.Article;
import com.haibin.calendarviewproject.ArticleAdapter;
import com.haibin.calendarviewproject.R;
import com.haibin.calendarviewproject.base.activity.BaseActivity;
import com.haibin.calendarviewproject.group.GroupItemDecoration;
import com.haibin.calendarviewproject.group.GroupRecyclerView;
import com.haibin.calendarviewproject.index.IndexActivity;
import com.haibin.calendarviewproject.meizu.MeiZuActivity;
import com.haibin.calendarviewproject.simple.SimpleActivity;

import java.util.HashMap;
import java.util.Map;

public class ColorfulActivity extends BaseActivity implements       //多彩样式活动控件
        CalendarView.OnCalendarSelectListener,      //日历样式选择监听器
        CalendarView.OnYearChangeListener,          //年份变化监听器
        View.OnClickListener {                      //视图点击监听器

    //TextView-文本框
    TextView mTextMonthDay;                         //顶排月与日文本

    TextView mTextYear;                             //顶排年份文本

    TextView mTextLunar;                            //顶排节日文本

    TextView mTextCurrentDay;                       //顶排本日文本

    CalendarView mCalendarView;                     //日历样式

    RelativeLayout mRelativeTool;                   //顶排相关布局组件
    private int mYear;                              //年份
    CalendarLayout mCalendarLayout;                 //日历布局，用基类
    GroupRecyclerView mRecyclerView;                //循环视图组件(用较小的布局视图显示更多的信息，滑动窗口)

    public static void show(Context context) {      //转入该样式，启动
        //Intent是一个将要执行的动作的抽象的描述，一般来说是作为参数来使用，由 Intent来协助完成 Android各个组件之间的通讯
        context.startActivity(new Intent(context, ColorfulActivity.class));
    }

    //获取对应layoutXMLId，为activity_colorful.xml
    @Override
    protected int getLayoutId() {                   //获取布局(样式)ID，此处为多彩样式布局
        return R.layout.activity_colorful;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {                     //初始化视图
        setStatusBarDarkMode();                     //将状态栏设置为暗黑模式(如果必要的话)
        //R.id.xx来自相关的xml文件，此处为activity_colorful.xml，其中可一一对应找到内容，设置对应的引用内容
        mTextMonthDay = findViewById(R.id.tv_month_day);//
        mTextYear = findViewById(R.id.tv_year);
        mTextLunar = findViewById(R.id.tv_lunar);
        mRelativeTool = findViewById(R.id.rl_tool);
        mCalendarView = findViewById(R.id.calendarView);
        mTextCurrentDay =  findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {//月日文本点击触发事件
            @Override
            public void onClick(View v) {                           //设置月日点击监听器
                if (!mCalendarLayout.isExpand()) {                  //当前布局是否展开(可能因查看下方消息部分而折叠)
                    mCalendarLayout.expand();                       //没有展开就展开(展开就是月视图，收缩就是周视图)
                    return;
                }
                //如果已经展开了，则进入年视图
                mCalendarView.showYearSelectLayout(mYear);          //切换视图为年视图
                mTextLunar.setVisibility(View.GONE);                //节日文本消失
                mTextYear.setVisibility(View.GONE);                 //年份文本消失
                mTextMonthDay.setText(String.valueOf(mYear));       //将月日文本设置为当前年份
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {//当日文本点击触发事件
            @Override
            public void onClick(View v) {    //设置回到当日点击监听器
                mCalendarView.scrollToCurrent();                    //滚动到当日
            }
        });
        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);            //切入日历监听器
        mCalendarView.setOnYearChangeListener(this);                //年份变化监听器
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear())); //当前年份文本设置
        mYear = mCalendarView.getCurYear();                            //当前年份引用设置,int型
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");//当前月日文本设置
        mTextLunar.setText("今日");                                    //节日文本设置
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));//当日文本设置
    }

    @Override
    protected void initData() {                                         //初始化数据

        int year = mCalendarView.getCurYear();                          //初始化int型当前年月
        int month = mCalendarView.getCurMonth();

        //设置不同日期的事件
        Map<String, Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "假").toString(),
                getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
        map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "事").toString(),
                getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
        map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
                getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
        map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假").toString(),
                getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
        map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记").toString(),
                getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
        map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假").toString(),
                getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
        map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多").toString(),
                getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);                               //标记日期事件

        //资讯模块加载
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
        mRecyclerView.setAdapter(new ArticleAdapter(this));
        mRecyclerView.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {                                       //点击切换样式？

    }

    //设置每日事件
    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }


    //超出范围时的行为，该样式没有范围相关动作，故留空
    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    //切换至当前日历样式
    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);                             //节日与年份文本显示
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日"); //设置当前月日文本
        mTextYear.setText(String.valueOf(calendar.getYear()));                        //设置当前年份与节日文本
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();                                                   //设置当前年份，int型
    }

    @Override
    public void onYearChange(int year) {                                    //当前年份变化时
        mTextMonthDay.setText(String.valueOf(year));                        //设置年份文本
    }


}
