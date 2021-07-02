package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

/**
 * 理赔
 * Created by ITMG on 2020-01-04.
 */
public class ClaimEntity implements Serializable {
    public static final int TYPE_WAITEVIDENCE = 1;//待取证
    public static final int TYPE_WAITAUDIT = 2;//审核中
    public static final int TYPE_AUDITNOTPASS = 3;//审核未过
    public static final int TYPE_WAITPAYFOR = 4;//待赔付
    public static final int TYPE_ALREADYPAYED = 5;//已理赔
    public static final int TYPE_ALREADYCANCEL = 6;//已取消
    private int size;//":10;
    private String id;
    private String insuranceId;//":"11297",
    private String claimNo;//理赔单号
    private String beforeEvidence;//"http://zxc.luhongkj.com:8808/images/202004221133357061.jpg",
    private String beforeEvidenceTime;
    private double beforeEvidenceLon;
    private double beforeEvidenceLat;
    private String evidence;//取证图片地址
    private String evidenceTime;//取证时间
    private String filingReceipt;//立案回执图片地址
    private String filingTime;//立案时间
    private double lat;//发生移动的最后位置的纬度
    private double lon;//
    private double evidenceLat;//手机纬度
    private double evidenceLon;
    private String distance;//手机与设备距离
    private int status;//理赔状态
    private String createTime;//":"2020-01-04 20:45:22",
    private int isDel;//":0,
    private String claimStartTime;//":"2020-01-04 20:45:22",
    private String claimCurrentTime;//":"2020-01-04 20:45:22",
    private String insuranceNo;//":"G202001042042120289",
    private String insureType;//":"车辆盗抢险",
    private String insureCompany;//":"中国人民财产保险股份有限公司",
    private String statusShow;//":"待取证",
    private String vehicleId;//":"1192407634052186215"
    private float account;// 理赔金额
    private String auditTime; //审核时间
    private int auditStatus;    //审批状态 0审批中 1审核通过 2审核不通过
    private int cancelType;//取消类型： 1超时未取证 2手动取消
    private String message;    //进度消息提示的内容
    private String order;
    private String qrCode;    //理赔二维码
    private String reason;    //审核不通过的原因


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getClaimNo() {
        return claimNo;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    public String getBeforeEvidence() {
        return beforeEvidence;
    }

    public void setBeforeEvidence(String beforeEvidence) {
        this.beforeEvidence = beforeEvidence;
    }

    public String getBeforeEvidenceTime() {
        return beforeEvidenceTime;
    }

    public void setBeforeEvidenceTime(String beforeEvidenceTime) {
        this.beforeEvidenceTime = beforeEvidenceTime;
    }

    public double getBeforeEvidenceLon() {
        return beforeEvidenceLon;
    }

    public void setBeforeEvidenceLon(double beforeEvidenceLon) {
        this.beforeEvidenceLon = beforeEvidenceLon;
    }

    public double getBeforeEvidenceLat() {
        return beforeEvidenceLat;
    }

    public void setBeforeEvidenceLat(double beforeEvidenceLat) {
        this.beforeEvidenceLat = beforeEvidenceLat;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getEvidenceTime() {
        return evidenceTime;
    }

    public void setEvidenceTime(String evidenceTime) {
        this.evidenceTime = evidenceTime;
    }

    public String getFilingReceipt() {
        return filingReceipt;
    }

    public void setFilingReceipt(String filingReceipt) {
        this.filingReceipt = filingReceipt;
    }

    public String getFilingTime() {
        return filingTime;
    }

    public void setFilingTime(String filingTime) {
        this.filingTime = filingTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public String getClaimStartTime() {
        return claimStartTime;
    }

    public void setClaimStartTime(String claimStartTime) {
        this.claimStartTime = claimStartTime;
    }

    public String getClaimCurrentTime() {
        return claimCurrentTime;
    }

    public void setClaimCurrentTime(String claimCurrentTime) {
        this.claimCurrentTime = claimCurrentTime;
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

    public String getInsureType() {
        return insureType;
    }

    public void setInsureType(String insureType) {
        this.insureType = insureType;
    }

    public String getInsureCompany() {
        return insureCompany;
    }

    public void setInsureCompany(String insureCompany) {
        this.insureCompany = insureCompany;
    }

    public String getStatusShow() {
        return statusShow;
    }

    public void setStatusShow(String statusShow) {
        this.statusShow = statusShow;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public float getAccount() {
        return account;
    }

    public void setAccount(float account) {
        this.account = account;
    }

    public int getCancelType() {
        return cancelType;
    }

    public void setCancelType(int cancelType) {
        this.cancelType = cancelType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getEvidenceLat() {
        return evidenceLat;
    }

    public void setEvidenceLat(double evidenceLat) {
        this.evidenceLat = evidenceLat;
    }

    public double getEvidenceLon() {
        return evidenceLon;
    }

    public void setEvidenceLon(double evidenceLon) {
        this.evidenceLon = evidenceLon;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }
}
