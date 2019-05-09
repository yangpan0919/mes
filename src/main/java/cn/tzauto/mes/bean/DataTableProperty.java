package cn.tzauto.mes.bean;



public class DataTableProperty {
    private String lot;
    private String rfid;
    private String shelfLocation;
    private String processStep;
    private String eqp;
    private String ppid;
    private String lotNum;
    private String processState;

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }
//    public String getSlot() {
//        return slot;
//    }
//
//    public void setSlot(String slot) {
//        this.slot = slot;
//    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getShelfLocation() {
        return shelfLocation;
    }

    public void setShelfLocation(String shelfLocation) {
        this.shelfLocation = shelfLocation;
    }

    public String getProcessStep() {
        return processStep;
    }

    public void setProcessStep(String processStep) {
        this.processStep = processStep;
    }

    public String getEqp() {
        return eqp;
    }

    public void setEqp(String eqp) {
        this.eqp = eqp;
    }

    public String getPpid() {
        return ppid;
    }

    public void setPpid(String ppid) {
        this.ppid = ppid;
    }

    public String getProcessState() {
        return processState;
    }

    public void setProcessState(String processState) {
        this.processState = processState;
    }

    @Override
    public String toString() {
        return "相关批次信息->>" +
                "lot:'" + lot + '\'' +
                ", rfid:'" + rfid + '\'' +
                ", shelfLocation:'" + shelfLocation + '\'' +
                ", processStep:'" + processStep + '\'' +
                ", eqp:'" + eqp + '\'' +
                ", ppid:'" + ppid ;
    }
}
