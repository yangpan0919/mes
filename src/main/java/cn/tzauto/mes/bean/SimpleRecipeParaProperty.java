package cn.tzauto.mes.bean;

import javafx.beans.property.SimpleStringProperty;


public class SimpleRecipeParaProperty {

    private SimpleStringProperty lot;
    private SimpleStringProperty rfid;
    private SimpleStringProperty shelfLocation;
    private SimpleStringProperty processStep;
    private SimpleStringProperty eqp;
    private SimpleStringProperty ppid;
    private SimpleStringProperty lotNum;
    private SimpleStringProperty processState;

    public SimpleRecipeParaProperty() {
        this.lot = new SimpleStringProperty("");
        this.rfid = new SimpleStringProperty("");
        this.shelfLocation = new SimpleStringProperty("");
        this.processStep = new SimpleStringProperty("");
        this.eqp = new SimpleStringProperty("");
        this.ppid = new SimpleStringProperty("");
        this.lotNum = new SimpleStringProperty("");
        this.processState = new SimpleStringProperty("");
    }

    public SimpleRecipeParaProperty(String  lot, String rfid, String shelfLocation, String processStep, String eqp, String ppid,String lotNum, String processState) {
        this.lot = new SimpleStringProperty(lot);
        this.rfid = new SimpleStringProperty(rfid);
        this.shelfLocation = new SimpleStringProperty(shelfLocation);
        this.processStep = new SimpleStringProperty(processStep);
        this.eqp = new SimpleStringProperty(eqp);
        this.ppid = new SimpleStringProperty(ppid);
        this.lotNum = new SimpleStringProperty(lotNum);
        this.processState = new SimpleStringProperty(processState);
    }

    public String getLotNum() {
        return lotNum.get();
    }

    public SimpleStringProperty lotNumProperty() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum.set(lotNum);
    }
//    public String getSlot() {
//        return slot.get();
//    }
//
//    public SimpleStringProperty slotProperty() {
//        return slot;
//    }
//
//    public void setSlot(String slot) {
//        this.slot.set(slot);
//    }

    public String getLot() {
        return lot.get();
    }

    public SimpleStringProperty lotProperty() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot.set(lot);
    }

    public String getRfid() {
        return rfid.get();
    }

    public SimpleStringProperty rfidProperty() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid.set(rfid);
    }

    public String getShelfLocation() {
        return shelfLocation.get();
    }

    public SimpleStringProperty shelfLocationProperty() {
        return shelfLocation;
    }

    public void setShelfLocation(String shelfLocation) {
        this.shelfLocation.set(shelfLocation);
    }

    public String getProcessStep() {
        return processStep.get();
    }

    public SimpleStringProperty processStepProperty() {
        return processStep;
    }

    public void setProcessStep(String processStep) {
        this.processStep.set(processStep);
    }

    public String getEqp() {
        return eqp.get();
    }

    public SimpleStringProperty eqpProperty() {
        return eqp;
    }

    public void setEqp(String eqp) {
        this.eqp.set(eqp);
    }

    public String getPpid() {
        return ppid.get();
    }

    public SimpleStringProperty ppidProperty() {
        return ppid;
    }

    public void setPpid(String ppid) {
        this.ppid.set(ppid);
    }

    public String getProcessState() {
        return processState.get();
    }

    public SimpleStringProperty processStateProperty() {
        return processState;
    }

    public void setProcessState(String processState) {
        this.processState.set(processState);
    }
}
