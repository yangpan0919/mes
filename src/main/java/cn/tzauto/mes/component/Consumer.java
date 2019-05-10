package cn.tzauto.mes.component;



import cn.tzauto.mes.bean.DataTableProperty;
import cn.tzauto.mes.bean.SimpleRecipeParaProperty;
import cn.tzauto.mes.javafx.controller.ViewController;
import cn.tzauto.mes.utils.CommonUiUtil;
import cn.tzauto.mes.utils.UiLogUtil;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@Component
public class Consumer {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Autowired
    UiLogUtil uiLogUtil;

    @Autowired
    QueueSender queueSender;

    @Autowired
    ViewController viewController;

    /**
     * 1.入站
     * 2.根据rfid查询lot信息，包括货架位，工艺，机台号，ppid，批号以及数量
     * 3.开机检查，检查人、工、料、机，返回检查结果
     * 4.完工检查，检查lot数量，edc数据完整性
     * 5.出站
     *
     * @param text   接收的rfid信息
     */

    @JmsListener(destination = "edc.lot")
    public void edcLotMessage(Map text) {
        System.out.println("收到队列消息------>" + text);
        if("Y".equals(text.get("result"))){
            uiLogUtil.appendLog2EventTab(null, "数据完整性正常");
        }else if("N".equals(text.get("result"))){
            uiLogUtil.appendLog2EventTab(null, "数据完整性错误");
        }else{
            uiLogUtil.appendLog2EventTab(null, "参数错误");
        }
    }


    public void showView(final  String text){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //update application thread
                CommonUiUtil.alert(Alert.AlertType.INFORMATION,text);
            }
        });
    }
    @JmsListener(destination = "C2S.Q.SPECIFIC_DATA")
    public Map<String,String> mesTrack(Map text) {
        System.out.println("收到信息------>" + text);
        Map<String,String> map = new HashMap<>();
        if(text == null){
            return null;
        }
        String name = (String) text.get("msgName");

        if("mes.trackIn".equals(name)){
            System.out.println("收到入站信息------>" + text);
            String rfid = (String) text.get("RFID");
            DataTableProperty dataTableProperty = ViewController.dataMap.get(rfid==null?"":rfid);
            if(dataTableProperty != null){
                uiLogUtil.appendLog2EventTab(null, rfid+" 入站...");

                ObservableList<SimpleRecipeParaProperty> list = viewController.getList();
                for(int i=0;i<list.size();i++){
                    SimpleRecipeParaProperty property =list.get(i);
                    if(property.getRfid().equals(rfid)){
                        property.setProcessState("生产中（PROCESSING）");
                        property.setInTime(LocalDateTime.now().format(formatter));
                        showView("开始入站");

                    }
                }
                viewController.getDataTable().setItems(list);
                uiLogUtil.appendLog2EventTab(null, dataTableProperty.toString());
                map.put("LOT",dataTableProperty.getLot());
                map.put("DESC","");
//                map.put("SLOT",dataTableProperty.getSlot());
                return map;
            }else{
                uiLogUtil.appendLog2EventTab(null, "未找到批次信息");
                map.put("LOT",null);
                map.put("DESC","未找到  "+rfid+"  对应的批次信息");
                return map;
            }
        }else if("mes.trackOut".equals(name)){
            String out = (String) text.get("mes.trackOut");
            System.out.println("收到出站信息------>" + text);
            uiLogUtil.appendLog2EventTab(null, "出站...");

            ObservableList<SimpleRecipeParaProperty> list = viewController.getList();
            for(int i=0;i<list.size();i++){
                SimpleRecipeParaProperty property =list.get(i);
                if(property.getRfid().equals(out)){
                    property.setProcessState("已完工（COMPLETED）");
                    property.setOutTime(LocalDateTime.now().format(formatter));
                    showView("已出站");
                }
            }

            viewController.getDataTable().setItems(list);
            return map;
        }else if("mes.startCheck".equals(name)){

            String opid = (String) text.get("OPID");
            String lot = (String) text.get("LOT");
            String cassetteMapping = (String) text.get("CassetteMapping");
            String deviceCode = (String) text.get("DeviceCode");
            System.out.println("收到开机检查信息------>" + text);
            showView("开机检查");
            SimpleRecipeParaProperty simpleRecipeParaProperty = null;

            ObservableList<SimpleRecipeParaProperty> list = viewController.getList();
            for(int i=0;i<list.size();i++){
                SimpleRecipeParaProperty property =list.get(i);
                if(property.getLot().equals(lot)){
                    simpleRecipeParaProperty = property;
                }
            }
            uiLogUtil.appendLog2EventTab(null, opid+"  正常");
            uiLogUtil.appendLog2EventTab(null, lot+"  正常");
            uiLogUtil.appendLog2EventTab(null, cassetteMapping+"  正常");
            if(simpleRecipeParaProperty != null){
                if(simpleRecipeParaProperty.getEqp().equals(deviceCode)){
                    uiLogUtil.appendLog2EventTab(null, deviceCode+"  正常");
                    map.put("RESULT","Y");
                    map.put("DESC","");
                    map.put("LOTNUM",simpleRecipeParaProperty.getLotNum());
                    map.put("PPID",simpleRecipeParaProperty.getPpid());
                    return map;
                }else{
                    uiLogUtil.appendLog2EventTab(null, deviceCode+"  出现错误...");
                    map.put("RESULT","N");
                    map.put("DESC","deviceCode不匹配");
                    return map;
                }
            }else{
                uiLogUtil.appendLog2EventTab(null, deviceCode+"  出现错误...");
                map.put("RESULT","N");
                map.put("DESC","无法找到对应批次");
                return map;

            }

        }else if("mes.finishCheck".equals(name)){
            System.out.println("收到完工检查信息------>" + text);
            uiLogUtil.appendLog2EventTab(null, "完工检查");

            //edc数据完整性
            queueSender.sendMap("edc.data",text);
        } else{
            uiLogUtil.appendLog2EventTab(null, "参数错误，请核对参数...");
            map.put("LOT",null);

            map.put("DESC","参数错误");
            return map;
        }

        return map;
    }


    //    @JmsListener(destination = "mes.track.in")
//    public void mesForTrackIn(String text) {
//        System.out.println("收到入站信息------>" + text);
//        DataTableProperty dataTableProperty = ViewController.dataMap.get(text);
//        if(dataTableProperty != null){
//            uiLogUtil.appendLog2EventTab(null, text+" 入站...");
//
//            ObservableList<SimpleRecipeParaProperty> list = viewController.getList();
//            for(int i=0;i<list.size();i++){
//                SimpleRecipeParaProperty property =list.get(i);
//                if(property.getRfid().equals(text)){
//                    property.setProcessState("Track In");
//                }
//            }
//            viewController.getDataTable().setItems(list);
//            uiLogUtil.appendLog2EventTab(null, dataTableProperty.toString());
//
//        }
//    }
//
//    @JmsListener(destination = "mes.check.before.starting")
//    public void checkBeforeStarting(String text) {
//        System.out.println("收到开机检查信息------>" + text);
//        uiLogUtil.appendLog2EventTab(null, "开机检查结束： 人、工、料、机一切正常");
//
//        //返回检查结果
//        queueSender.send("mes.check.start.result","人、工、料、机一切正常");
//    }
//
//    @JmsListener(destination = "mes.check.end")
//    public void endCheck(String text) {
//        System.out.println("收到完工检查信息------>" + text);
//        uiLogUtil.appendLog2EventTab(null, "完工检查");
//
//        //edc数据完整性
//        queueSender.send("edc.data","edc数据完整性");
//    }
//    @JmsListener(destination = "mes.track.out")
//    public void trackOut(String text) {
//        System.out.println("收到出站信息------>" + text);
//        uiLogUtil.appendLog2EventTab(null, "出站...");
//
//        ObservableList<SimpleRecipeParaProperty> list = viewController.getList();
//        for(int i=0;i<list.size();i++){
//            SimpleRecipeParaProperty property =list.get(i);
//            if(property.getRfid().equals(text)){
//                property.setProcessState("Track Out");
//            }
//        }
//        viewController.getDataTable().setItems(list);
//    }


//    {
//        Map mqmap = new HashMap();
//        mqmap.put("msgName", "CassetteMapping");
//        mqmap.put("OPID", cassetteMappingStr);
//        mqmap.put("LOT", lotId);
//        mqmap.put("CassetteMapping", cassetteMappingStr);
//        mqmap.put("DeviceCode", deviceCode);
//    }
//    @JmsListener(destination = "mes.check")
//    public Map<String,String> mesCheck(Map text) {
//        Map<String,String> map = new HashMap<>();
//        if(text == null){
//            return null;
//        }
//        String name = (String) text.get("msgName");
//
//        if("mes.startCheck".equals(name)){
//
//            String opid = (String) text.get("OPID");
//            String lot = (String) text.get("LOT");
//            String cassetteMapping = (String) text.get("CassetteMapping");
//            String deviceCode = (String) text.get("DeviceCode");
//            System.out.println("收到开机检查信息------>" + text);
//
//            SimpleRecipeParaProperty simpleRecipeParaProperty = null;
//
//            ObservableList<SimpleRecipeParaProperty> list = viewController.getList();
//            for(int i=0;i<list.size();i++){
//                SimpleRecipeParaProperty property =list.get(i);
//                if(property.getLot().equals(lot)){
//                    simpleRecipeParaProperty = property;
//                }
//            }
//            uiLogUtil.appendLog2EventTab(null, opid+"  正常");
//            uiLogUtil.appendLog2EventTab(null, lot+"  正常");
//            uiLogUtil.appendLog2EventTab(null, cassetteMapping+"  正常");
//            if(simpleRecipeParaProperty != null){
//                if(simpleRecipeParaProperty.getEqp().equals(deviceCode)){
//                    uiLogUtil.appendLog2EventTab(null, deviceCode+"  正常");
//                    map.put("RESULT","Y");
//                    map.put("DESC","");
//                    return map;
//                }else{
//                    uiLogUtil.appendLog2EventTab(null, deviceCode+"  出现错误...");
//                    map.put("RESULT","N");
//                    map.put("DESC","deviceCode不匹配");
//                    return map;
//                }
//            }else{
//                uiLogUtil.appendLog2EventTab(null, deviceCode+"  出现错误...");
//                map.put("RESULT","N");
//                map.put("DESC","无法找到对应批次");
//                return map;
//
//            }
//
//        }else if("mes.finishCheck".equals(name)){
//            System.out.println("收到完工检查信息------>" + text);
//            uiLogUtil.appendLog2EventTab(null, "完工检查");
//
//            //edc数据完整性
//            queueSender.sendMap("edc.data",text);
//        }
//        return map;
//    }
}