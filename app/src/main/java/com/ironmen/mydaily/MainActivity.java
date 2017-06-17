package com.ironmen.mydaily;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class MainActivity extends AppCompatActivity {


    private List<CostBean> mCostBeanList = new ArrayList<>();
    CostBean costBean;
    private CostListAdapter adapter;
    private String searchDate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bmob.initialize(this, "8328498fdda0c8dc6c372a5fc00d110c");  //Bmob云服务器初始化
        costBean = new CostBean();   //新建全局bean,为添加数据作准备

        final ListView costList = (ListView) findViewById(R.id.lv_mydaily);
        initCostData(); //进入程序时初始化数据

        adapter = new CostListAdapter(mCostBeanList, this);
        costList.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View viewDialog = inflater.inflate(R.layout.new_cost_record, null);
                final EditText title = (EditText) viewDialog.findViewById(R.id.et_new_costTitle);   //viewDialog.findViewById();一定记得要更换布局！！！
                final EditText money = (EditText) viewDialog.findViewById(R.id.et_new_costMoney);
                final DatePicker date = (DatePicker) viewDialog.findViewById(R.id.dp_new_costDate);
                builder.setView(viewDialog);
                builder.setTitle("New Cost");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {   //点击OK确认添加一条数据到数据库
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        costBean.setCostTitle(title.getText().toString());
                        costBean.setCostMoney(money.getText().toString());
                        costBean.setCostDate(date.getYear() + "-" + (date.getMonth() + 1) + "-" + date.getDayOfMonth());

                        costBean.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {    //add success
                                    //添加成功后通知更新ListView
                                    mCostBeanList.add(costBean);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(MainActivity.this, "add fail:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
                builder.setNegativeButton("CANCEL", null);
                builder.create().show();
            }
        });
    }

    private void initCostData() {     //先查询所有账单数据
        /*for (int i = 0; i < 6; i++) {
            CostBean costBean = new CostBean();
            costBean.setCostTitle("daily" + i);
            costBean.setCostDate("2017-6-13");
            costBean.setCostMoney("20");
            mConstBeanList.add(costBean);
        }*/
        /*BmobQuery<CostBean> costBeanBmobQuery = new BmobQuery<>();
        costBeanBmobQuery.findObjects(new FindListener<CostBean>() {
            @Override
            public void done(List<CostBean> list, BmobException e) {
                *//*for(int i = 0; i<list.size(); i++){
                    CostBean costBean = new CostBean();
                    costBean.setCostTitle(list.get(i).getCostTitle());
                    costBean.setCostDate(list.get(i).getCostDate());
                    costBean.setCostMoney(list.get(i).getCostMoney());
                    mConstBeanList.add(costBean);
                }*//*
                mConstBeanList = list;
                //Log.d("query",list.get(0).getCostTitle().toString());
            }
        });
        //Log.d("query",mConstBeanList.get(0).getCostTitle());*/
        BmobQuery<CostBean> costBeanBmobQuery = new BmobQuery<>();
        costBeanBmobQuery.findObjects(new FindListener<CostBean>() {
            @Override
            public void done(List<CostBean> list, BmobException e) {
                mCostBeanList.addAll(list);     //不能使用mCostBeanList = list;

                /*for (CostBean bean: list) {
                    mCostBeanList.add(bean);
                }*/
                adapter.notifyDataSetChanged();
                /*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("getList");
                String str = "";
                for (CostBean bean: list) {
                    str += bean.getCostTitle()+" "+bean.getCostDate()+" "+bean.getCostMoney()+ "\n";
                }
                builder.setMessage(str);
                builder.setNegativeButton("cancel",null);
                builder.create().show();
*/
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Toast.makeText(this, "click settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_search:
                newSearchView();   //创建一个查询窗口,并完成查询更新操作
                return true;

        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    private void newSearchView() {   //点击查询按钮，新建查询会话窗口
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Search Record");
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View searchView = inflater.inflate(R.layout.date_search, null);
        builder.setView(searchView);
        final DatePicker dp_search_data = (DatePicker) searchView.findViewById(R.id.dp_search_date);

        builder.setPositiveButton("Search!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                searchDate = dp_search_data.getYear() + "-" + (dp_search_data.getMonth() + 1) + "-" + dp_search_data.getDayOfMonth();
                goSearch();
            }
        });
        builder.create().show();

    }

    private void goSearch() {   //查询核心代码

        BmobQuery<CostBean> dateSearchCostBean = new BmobQuery<>();
        dateSearchCostBean.addWhereEqualTo("costDate", searchDate);
        dateSearchCostBean.findObjects(new FindListener<CostBean>() {
            @Override
            public void done(List<CostBean> list, BmobException e) {
                if(e == null) {
                    mCostBeanList.removeAll(mCostBeanList);
                    adapter.notifyDataSetChanged();
                    mCostBeanList.addAll(list);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this,"success",Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(MainActivity.this,"fail",Toast.LENGTH_SHORT).show();
                }

                /*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(list.toString());
                builder.create().show();*/


            }
        });

    }
}
