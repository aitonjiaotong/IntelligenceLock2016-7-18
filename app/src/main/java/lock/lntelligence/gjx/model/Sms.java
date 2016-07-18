package lock.lntelligence.gjx.model;

/**
 * Created by zjb on 2016/6/23.
 */
public class Sms {
    /**
     * status : 0
     * mes : 验证码已发送
     * datas : gwwxsx
     */

    private int status;
    private String mes;
    private String datas;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }
}
