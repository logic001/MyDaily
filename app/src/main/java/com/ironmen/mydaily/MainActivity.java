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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bmob.initialize(this,"8328498fdda0c8dc6c372a5fc00d110c");
        costBean = new CostBean();

        final ListView costList = (ListView) findViewById(R.id.lv_mydaily);
        initCostData(); //进入程序时初始化数据************

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
                        costBean.setCostMoney( money.getText().toString());
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
