package com.ironmen.mydaily;

import cn.bmob.v3.BmobObject;

/**
 * Created by logic on 2017/6/13.
 */

public class CostBean extends BmobObject{
    private String costTitle;
    private String costDate;
    private String costMoney;

    public String getCostTitle() {
        return costTitle;
    }

    public void setCostTitle(String costTitle) {
        this.costTitle = costTitle;
    }

    public String getCostDate() {
        return costDate;
    }

    public void setCostDate(String costDate) {
        this.costDate = costDate;
    }

    public String getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(String costMoney) {
        this.costMoney = costMoney;
    }


}
