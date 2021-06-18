package com.luhong.locwithlibrary.event;

import java.io.Serializable;
import java.util.List;

/**
 * 推送
 * Created by ITMG on 2019/8/31 0031.
 */
public class PushEvent implements Serializable {

    private String flowId;
    private String imei;
    private int msgId;//消息id
    private boolean msgIdReply;
    private String result;
    private String requestTime;
    //    private int result;
    private List<Integer> status;
    private String time;
    private String userId;
    private String vehicleId;
    private Content content;
    private float accountMoney;//账户金额
    private String deadlineDate;//有效期至

    public PushEvent() {
    }

    public PushEvent(int msgId) {
        this.msgId = msgId;
    }

    public static class Content implements Serializable {
        private Location location;

        public static class Location implements Serializable {
            private int direction;
            private int elevation;
            private double latitude;
            private double longitude;
            private float speed;
            private String time;

            public int getDirection() {
                return direction;
            }

            public void setDirection(int direction) {
                this.direction = direction;
            }

            public int getElevation() {
                return elevation;
            }

            public void setElevation(int elevation) {
                this.elevation = elevation;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public float getSpeed() {
                return speed;
            }

            public void setSpeed(float speed) {
                this.speed = speed;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }


    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public boolean isMsgIdReply() {
        return msgIdReply;
    }

    public void setMsgIdReply(boolean msgIdReply) {
        this.msgIdReply = msgIdReply;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

//    public int getResult() {
//        return result;
//    }
//
//    public void setResult(int result) {
//        this.result = result;
//    }

    public List<Integer> getStatus() {
        return status;
    }

    public void setStatus(List<Integer> status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public float getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(float accountMoney) {
        this.accountMoney = accountMoney;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
}
