package cn.tzauto.mes;

import cn.tzauto.mes.conf.SplashScreenImpl;
import cn.tzauto.mes.javafx.view.FirstView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class MesApplication extends AbstractJavaFxApplicationSupport {


    public static void main(String[] args) {
        launch(MesApplication.class, FirstView.class,new SplashScreenImpl(), args);
    }

}

