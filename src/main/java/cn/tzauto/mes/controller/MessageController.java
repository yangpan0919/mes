package cn.tzauto.mes.controller;


import cn.tzauto.mes.component.QueueSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MessageController {

    @Autowired
    QueueSender queueSender;

//    @GetMapping("/addMessage/{id}")
//    public String activeMqAddMessage(HttpServletRequest request, @PathVariable("id") Integer id){
//        String text = request.getParameter("text");
//        if(id == 1){
//            queueSender.send("mes.track.in",text);
//        }else if(id ==2){
//            queueSender.send("mes.track.in",text);
//        }else if(id ==3){
//            queueSender.send("mes.check.before.starting",text);
//        }else if(id ==4){
//            queueSender.send("mes.check.end",text);
//        }else if(id ==5){
//            queueSender.send("mes.track.out",text);
//        }
//        return "发送成功 :  "+ id;
//    }

//    @GetMapping("/lalala")
//    public String test(){
//        Map<String,String> text = new HashMap<>();
//        text.put("devicecode","qwe");
//        text.put("lot","qwe");
//        text.put("waferNum","qwe");
//        queueSender.sendMap("edc.data",text);
//        return "成功";
//    }
//
//    @GetMapping("/lalala1")
//    public String test1(){
//        Map<String,String> text = new HashMap<>();
//        text.put("devicecode","qwe");
//        text.put("lot","qwe");
//        text.put("waferNum","qwe");
//        text.put("type","end");
//        text.put("time","12121212121");
//        queueSender.sendMap("C2S.Q.EDA_DATA",text);
//        return "成功";
//    }
    @GetMapping("/lalala0")
    public String test0(){
        Map<String,String> text = new HashMap<>();
        text.put("devicecode","qwe");
        text.put("lot","qwe");
        text.put("waferNum","qwe");
        text.put("type","start");
        queueSender.sendMap("edc.data.completed",text);
        return "成功";
    }
    @GetMapping("/mine")
    public String testMy0(){
        Map<String,String> text = new HashMap<>();

        text.put("msgName","mes.trackIn");
        text.put("RFID","TZ20190509.001");
        text.put("waferNum","qwe");
        text.put("type","start");

        queueSender.sendMap("C2S.Q.SPECIFIC_DATA",text);
        return "成功";
    }
    @GetMapping("/mine1")
    public String testMy10(){
        Map<String,String> text = new HashMap<>();

        text.put("msgName","mes.trackOut");
        text.put("mes.trackOut","TZ20190509.001");
        text.put("waferNum","qwe");
        text.put("type","start");

        queueSender.sendMap("C2S.Q.SPECIFIC_DATA",text);
        return "成功";
    }
}
