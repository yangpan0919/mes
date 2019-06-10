package cn.tzauto.mes;

import cn.tzauto.mes.conf.ContextConfig;
import cn.tzauto.mes.conf.SplashScreenImpl;
import cn.tzauto.mes.javafx.view.FirstView;
import cn.tzauto.mes.service.TestService;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class MesApplication extends AbstractJavaFxApplicationSupport {


    public static void main(String[] args) {
        new Thread(()->
        {
            try {
                Thread.currentThread().sleep(10000L);
                TestService testService = ContextConfig.context.getBean("testService", TestService.class);
                String test = testService.test();
                System.out.println(test);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        launch(MesApplication.class, FirstView.class,new SplashScreenImpl(), args);
    }

}

