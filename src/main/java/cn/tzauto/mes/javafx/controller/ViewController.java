package cn.tzauto.mes.javafx.controller;



import cn.tzauto.mes.bean.DataTable;
import cn.tzauto.mes.bean.DataTableProperty;
import cn.tzauto.mes.bean.SimpleRecipeParaProperty;
import cn.tzauto.mes.javafx.view.FirstView;
import cn.tzauto.mes.utils.UiLogUtil;
import com.google.gson.Gson;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;
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
    private TableColumn<SimpleRecipeParaProperty, String> inTime = new TableColumn<>();
    @FXML
    private TableColumn<SimpleRecipeParaProperty, String> outTime = new TableColumn<>();
    @FXML
    private TableColumn<SimpleRecipeParaProperty, String> processState = new TableColumn<SimpleRecipeParaProperty, String>();

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
        inTime.setCellValueFactory(celldata -> celldata.getValue().inTimeProperty());
        outTime.setCellValueFactory(celldata -> celldata.getValue().outTimeProperty());
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
        processState.setCellFactory(new Callback<TableColumn<SimpleRecipeParaProperty, String>, TableCell<SimpleRecipeParaProperty, String>>() {

            @Override
            public TableCell call(TableColumn param) {
                TableCell<SimpleRecipeParaProperty, String> tableCell = new TableCell<SimpleRecipeParaProperty, String>(){

                    ObservableValue ov;

                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            ov = getTableColumn().getCellObservableValue(getIndex());
                            if(getTableRow() != null&&!ov.equals("")){

                                SimpleRecipeParaProperty text = (SimpleRecipeParaProperty)this.getTableRow().getItem();

                                if(text.getProcessState().equals("待作业（IDLE）")){
//                                    this.getTableRow().setStyle("-fx-background-color: whitesmoke");
//                                    this.getTableRow().
//                                    this.getTableRow().setStyle("-fx-background-color: green");
                                }else if(text.getProcessState().equals("生产中（PROCESSING）")){
                                    this.getTableRow().setStyle("-fx-background-color: green");
                                }else if(text.getProcessState().equals("已完工（COMPLETED）")){
                                    this.getTableRow().setStyle("-fx-background-color: whitesmoke");
                                }else if(text.getProcessState().equals("扣留（HOLD）")){
                                    this.getTableRow().setStyle("-fx-background-color: yellow");
                                }
                            }
                            setText(item);
                        }
                    }
                };
                return tableCell;
            }
        });

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


