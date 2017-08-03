package com.example.zlx.zlxbasecv.custom_view.recyclerviewtitle;

import com.example.zlx.zlxbasecv.ChineseToPinyin;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class CityBean extends BaseIndexTagBean implements Comparator<CityBean> {
    private String city;

    public CityBean() {
    }

    public CityBean(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getTag() {
        return super.getTag();
    }

    @Override
    public void setTag(String tag) {
        super.setTag(tag);
    }

    @Override
    public int compare(CityBean o1, CityBean o2) {
        String t1 = o1.getCity();
        String t2 = o2.getCity();
        return Collator.getInstance(Locale.CHINESE).compare(t1, t2);
    }

    private String getFirstSpellFromCh(String text) {
        return ChineseToPinyin.getFirstSpell(text).substring(0, 1).toUpperCase();
    }

    public static List<CityBean> sortList(List<CityBean> datas) {
        CityBean comp = new CityBean();
        Collections.sort(datas, comp);
        return datas;
    }
}
