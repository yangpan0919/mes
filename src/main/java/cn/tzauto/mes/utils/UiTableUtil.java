package cn.tzauto.mes.utils;

import cn.tzauto.mes.javafx.controller.ViewController;
import cn.tzauto.mes.javafx.view.FirstView;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UiTableUtil {

    @Autowired
    ViewController viewController;


    public void initTable(String msg){
        viewController.initTable("123");

    }
}
