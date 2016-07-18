package lock.lntelligence.gjx.constant;

/**
 * Created by Administrator on 2016/7/16.
 */
public class Constant {
    /**
     * 服务器地址
     */
    public static final String HOST = "http://120.55.166.203:8020/aiton-tickets-app-webapp";

    /**
     * url
     */
    public static class Url {
        public static final String GET_SMS = "http://120.55.166.203:8020/aiton-tickets-app-webapp/public/sendmessage";
        public static final String LOGIN = HOST + "/login";//传入参数：name(名称),phone(电话),deviceId(手机ID)
    }

    /**
     * 权限
     */
    public static class PERMISSION {
        public static final int PERMISSION_READ_SMS = 1;
        public static final int PERMISSION_BLUETOOTH_ADMIN = 2;
        public static final int PERMISSION_BLUETOOTH = 3;
        public static final int PERMISSION_VIBRATE = 4;
    }

    /**
     * 存储
     */
    public static class ACACHE {
        public static final String USER = "user";
        public static final String PAINT_PASSWORD= "paintPassword";
    }

}
