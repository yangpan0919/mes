package cn.tzauto.mes.bean;

import java.util.ArrayList;
import java.util.List;

public class DataTable {

    List<DataTableProperty>  data = new ArrayList<>();


    public List<DataTableProperty> getData() {
        return data;
    }

    public void setData(List<DataTableProperty> data) {
        this.data = data;
    }
}
