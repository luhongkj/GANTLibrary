package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

public class SafeguardEntity implements Serializable {
    public static final int TYPE_FIRST_TRIAL = 1;//1待初审
    public static final int TYPE_FINAL_TRIAL = 2;//2待终审
    public static final int TYPE_FAILED = 3;//3审核未过
    public static final int TYPE_PAY = 4;//4待支付
    public static final int TYPE_GUARANTEED = 5;//5已保障
    public static final int TYPE_EXPIRED = 6;//6已过期
    public static final int TYPE_CLAIMED = 7;//7已理赔
    public static final int TYPE_OVERDUE = 8;//8过期未支付
    //    保障首页
    private String type;
    private String company;
    private String name;
    private String remark;
    private String rate;
    private String typeShow;
    private String companyShow;
    //    private String link;
    private String url;//协议地址
    //保险
    private int size;
    private String id;
    private String userId;
    private String picture;
    private String insureSetId;
    private String insureSetName;
    private float insureFee;//赠送保费
    private String insureType;//":"车辆盗抢险",
    private String insureCompany;//":"中国人民财产保险股份有限公司",
    private String insuranceNo;
    private String realName;
    private String phone;
    private String provice;
    private String city;
    private String area;
    private String address;
    private String idCard;
    private String cardType;
    private String cardFront;
    private String cardBack;
    private String myVehicleId;
    private String nickName;
    private String vehicleType;
    private String vehicleBrand;
    private String vehicleModel;
    private String vin;
    private float price;
    private String sn;//设备号
    private String vehiclePurchase;
    private String vehicleWhole;
    private String vehicleSide;
    private String vinSide;
    private String vinNumber;
    private String recommender;
    private float payMoney;//实际支付金额
    private float cost;//保险费用
    private float insureCost;//保险费用
    private float valuation;//估价
    private int status;//1待初审,2待终审,3审核未过,4待支付,5已保障,6已过期,7已理赔,8过期未支付
    private String statusShow;//":"待初审",
    private String message;//横幅显示信息
    private String buyDate;
    private String payStartTime;//待支付开始时间
    private String payCurrentTime;//当前时间
    private String deadlineStart;//保险有效期
    private String deadlineEnd;
    private String createTime;
    private String createId;
    private int isDel;
    private ClaimDto claimDto;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public static class ClaimDto implements Serializable {
        public static final int TYPE_WAITEVIDENCE = 1;//待取证
        public static final int TYPE_WAITAUDIT = 2;//待审核
        public static final int TYPE_AUDITNOTPASS = 3;//审核未过
        public static final int TYPE_WAITPAYFOR = 4;//待赔付
        public static final int TYPE_ALREADYPAYED = 5;//已理赔
        public static final int TYPE_ALREADYCANCEL = 6;//已取消
        private int status;//"待取证", "1"), "待审核", "2"), "审核未过", "3"), ("待赔付","4"), ("已理赔", "5"), ("已取消", "6");
        private String id;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public ClaimDto getClaimDto() {
        return claimDto;
    }

    public void setClaimDto(ClaimDto claimDto) {
        this.claimDto = claimDto;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    //    public String getLink() {
    //        return link;
    //    }
    //
    //    public void setLink(String link) {
    //        this.link = link;
    //    }

    public String getTypeShow() {
        return typeShow;
    }

    public void setTypeShow(String typeShow) {
        this.typeShow = typeShow;
    }

    public String getCompanyShow() {
        return companyShow;
    }

    public void setCompanyShow(String companyShow) {
        this.companyShow = companyShow;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getInsureSetId() {
        return insureSetId;
    }

    public void setInsureSetId(String insureSetId) {
        this.insureSetId = insureSetId;
    }

    public float getInsureFee() {
        return insureFee;
    }

    public void setInsureFee(float insureFee) {
        this.insureFee = insureFee;
    }

    public String getInsureSetName() {
        return insureSetName;
    }

    public void setInsureSetName(String insureSetName) {
        this.insureSetName = insureSetName;
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

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getCardFront() {
        return cardFront;
    }

    public void setCardFront(String cardFront) {
        this.cardFront = cardFront;
    }

    public String getCardBack() {
        return cardBack;
    }

    public void setCardBack(String cardBack) {
        this.cardBack = cardBack;
    }

    public String getMyVehicleId() {
        return myVehicleId;
    }

    public void setMyVehicleId(String myVehicleId) {
        this.myVehicleId = myVehicleId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getVehiclePurchase() {
        return vehiclePurchase;
    }

    public void setVehiclePurchase(String vehiclePurchase) {
        this.vehiclePurchase = vehiclePurchase;
    }

    public String getVehicleWhole() {
        return vehicleWhole;
    }

    public void setVehicleWhole(String vehicleWhole) {
        this.vehicleWhole = vehicleWhole;
    }

    public String getVehicleSide() {
        return vehicleSide;
    }

    public void setVehicleSide(String vehicleSide) {
        this.vehicleSide = vehicleSide;
    }

    public String getVinSide() {
        return vinSide;
    }

    public void setVinSide(String vinSide) {
        this.vinSide = vinSide;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getRecommender() {
        return recommender;
    }

    public void setRecommender(String recommender) {
        this.recommender = recommender;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getInsureCost() {
        return insureCost;
    }

    public void setInsureCost(float insureCost) {
        this.insureCost = insureCost;
    }

    public float getValuation() {
        return valuation;
    }

    public void setValuation(float valuation) {
        this.valuation = valuation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusShow() {
        return statusShow;
    }

    public void setStatusShow(String statusShow) {
        this.statusShow = statusShow;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayStartTime() {
        return payStartTime;
    }

    public void setPayStartTime(String payStartTime) {
        this.payStartTime = payStartTime;
    }

    public String getPayCurrentTime() {
        return payCurrentTime;
    }

    public void setPayCurrentTime(String payCurrentTime) {
        this.payCurrentTime = payCurrentTime;
    }

    public String getDeadlineStart() {
        return deadlineStart;
    }

    public void setDeadlineStart(String deadlineStart) {
        this.deadlineStart = deadlineStart;
    }

    public String getDeadlineEnd() {
        return deadlineEnd;
    }

    public void setDeadlineEnd(String deadlineEnd) {
        this.deadlineEnd = deadlineEnd;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public float getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(float payMoney) {
        this.payMoney = payMoney;
    }
}
