package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

/**
 * 电子围栏半径
 * Created by ITMG on 2020-01-03.
 */
public class PdfEntity implements Serializable {

    /**
     * channelId : 1
     * mainProductId : 1
     * modelId : 1
     * url : http://gis.luhongkj.com:8808/images/202103311010117186.pdf
     */

    private String channelId;
    private String mainProductId;
    private String modelId;
    private String url;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getMainProductId() {
        return mainProductId;
    }

    public void setMainProductId(String mainProductId) {
        this.mainProductId = mainProductId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
