package cn.tzauto.mes.javafx.controller;



import cn.tzauto.mes.bean.DataTable;
import cn.tzauto.mes.bean.DataTableProperty;
import cn.tzauto.mes.bean.SimpleRecipeParaProperty;
import cn.tzauto.mes.javafx.view.FirstView;
import cn.tzauto.mes.utils.UiLogUtil;
import com.google.gson.Gson;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

@FXMLController
public class ViewController  implements Initializable {

    public static  ConcurrentHashMap<String,DataTableProperty> dataMap = new ConcurrentHashMap<>();
    @FXML
    public GridPane gridPane;

    @Autowired
    UiLogUtil uiLogUtil;

    public  ObservableList<SimpleRecipeParaProperty> list = FXCollections.observableArrayList();

    @FXML
    private TableView<SimpleRecipeParaProperty> dataTable;     //tableView

    @FXML
    private TableColumn<SimpleRecipeParaProperty, String> lot = new TableColumn<>();
    @FXML
    private TableColumn<SimpleRecipeParaProperty, String> rfid = new TableColumn<>();
    @FXML
    private TableColumn<SimpleRecipeParaProperty, String> shelfLocation = new TableColumn<>();
    @FXML
    private TableColumn<SimpleRecipeParaProperty, String> processStep = new TableColumn<>();
    @FXML
    private TableColumn<SimpleRecipeParaProperty, String> eqp = new TableColumn<>();
    @FXML
    private TableColumn<SimpleRecipeParaProperty, String> ppid = new TableColumn<>();
    @FXML
    private TableColumn<SimpleRecipeParaProperty, String> lotNum = new TableColumn<>();
    @FXML
    private TableColumn<SimpleRecipeParaProperty, String> processState = new TableColumn<>();

    public ObservableList<SimpleRecipeParaProperty> getList() {
        return list;
    }

    public TableView<SimpleRecipeParaProperty> getDataTable() {
        return dataTable;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FirstView.root = gridPane;
        uiLogUtil.appendLog2EventTab(null, "MES系统启动完成");


        initTable("123");
    }

    public void initTable(String msg){
        lot.setCellValueFactory(celldata -> celldata.getValue().lotProperty());
        rfid.setCellValueFactory(celldata -> celldata.getValue().rfidProperty());
        shelfLocation.setCellValueFactory(celldata -> celldata.getValue().shelfLocationProperty());
        processStep.setCellValueFactory(celldata -> celldata.getValue().processStepProperty());
        eqp.setCellValueFactory(celldata -> celldata.getValue().eqpProperty());
        ppid.setCellValueFactory(celldata -> celldata.getValue().ppidProperty());
        lotNum.setCellValueFactory(celldata -> celldata.getValue().lotNumProperty());
        processState.setCellValueFactory(celldata -> celldata.getValue().processStateProperty());

        InputStreamReader inputStream = null;
        try {
            inputStream = new InputStreamReader(ViewController.class.getClassLoader().getResourceAsStream("tableData.txt"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String jsonStr = null;
        if(inputStream !=null) {
            jsonStr = convertStream2Json(inputStream);
        }
        System.out.println(jsonStr);
        Gson gson = new Gson();
        DataTable data = gson.fromJson(jsonStr, DataTable.class);
        List<DataTableProperty> dataList = data.getData();

        for(int i=0,length = dataList.size();i<length;i++){
            DataTableProperty dataTableProperty = dataList.get(i);
            dataMap.put(dataTableProperty.getRfid(),dataTableProperty);
            SimpleRecipeParaProperty property = new SimpleRecipeParaProperty(dataTableProperty.getLot(),dataTableProperty.getRfid(),
                                                                             dataTableProperty.getShelfLocation(),dataTableProperty.getProcessStep(),
                                                                             dataTableProperty.getEqp(),dataTableProperty.getPpid(),
                    dataTableProperty.getLotNum(),dataTableProperty.getProcessState()
            );
            list.add(property);
        }
        dataTable.setItems(list);

    }

    private static String convertStream2Json(InputStreamReader inputStream)
    {
        String jsonStr = "";
        BufferedReader br = new BufferedReader(inputStream);

    // 将输入流转移到内存输出流中
        try
        {
            String len = null;
            while ( (len =br.readLine())!=null)
            {
                jsonStr+=len;
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(inputStream!=null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return jsonStr;
    }
}


