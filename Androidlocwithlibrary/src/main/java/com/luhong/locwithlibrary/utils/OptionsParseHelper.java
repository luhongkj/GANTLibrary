package com.luhong.locwithlibrary.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.luhong.locwithlibrary.entity.OptionsMultipleEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zyyoona7
 * @version v1.0
 * @since 2018/8/21.
 */
public class OptionsParseHelper {
    private static final String TAG = "OptionsParseHelper";

    public static final String NAME_AREA_LEVEL_3 = "AreaLevel_3.json";

    private OptionsParseHelper() {
    }

    public static void initThreeLevelCityList(Context context, List<OptionsMultipleEntity> pList, List<List<OptionsMultipleEntity>> cList, List<List<List<OptionsMultipleEntity>>> aList) {
        List<OptionsMultipleEntity> threeLevelCityList = parseThreeLevelCityList(context);
        pList.addAll(threeLevelCityList);
        for (OptionsMultipleEntity cityEntity : threeLevelCityList) {
            List<OptionsMultipleEntity> cityList = new ArrayList<>(1);
            List<List<OptionsMultipleEntity>> areaList = new ArrayList<>(1);
            List<OptionsMultipleEntity> subCityList = cityEntity.getDistricts();
            for (OptionsMultipleEntity entity : subCityList) {
                cityList.add(entity);
                areaList.add(entity.getDistricts());
            }
            cList.add(cityList);
            aList.add(areaList);
        }
    }

    private static void initCityList(List<OptionsMultipleEntity> parseList, List<OptionsMultipleEntity> pList, List<List<OptionsMultipleEntity>> cList, List<List<List<OptionsMultipleEntity>>> aList, boolean isThreeLevel) {
        if (parseList == null) {
            return;
        }
        pList = parseList;
        for (OptionsMultipleEntity cityEntity : parseList) {
            List<OptionsMultipleEntity> cityList = new ArrayList<>(1);
            if (isThreeLevel) {
                List<List<OptionsMultipleEntity>> areaList = new ArrayList<>(1);
            }
            List<OptionsMultipleEntity> subCityList = cityEntity.getDistricts();
            cityList.addAll(subCityList);
        }
    }

    /**
     * 解析出三级城市列表
     *
     * @param context
     * @return
     */
    public static List<OptionsMultipleEntity> parseThreeLevelCityList(Context context) {
        return parseCityList(context, NAME_AREA_LEVEL_3);
    }

    /**
     * 解析城市数据
     *
     * @param context
     * @param assetFileName
     * @return
     */
    private static List<OptionsMultipleEntity> parseCityList(Context context, String assetFileName) {
        JSONObject jsonObject = AssetsUtils.loadJSONAsset(context, assetFileName);
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("country");
            JSONObject countryJobj = jsonArray.getJSONObject(0);
            String contentString = countryJobj.getString("districts");
            Gson gson = new GsonBuilder().registerTypeAdapter(String.class, new CityEntityStringJsonDeserializer()).create();
            return gson.fromJson(contentString, new TypeToken<List<OptionsMultipleEntity>>() {
            }.getType());
        } catch (JSONException e) {
            Log.d(TAG, "城市列表 JSON 数据解析异常：" + e.getMessage());
        }
        return new ArrayList<>(1);
    }

    public static class CityEntityStringJsonDeserializer implements JsonDeserializer<String> {
        @Override
        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsString();
            } catch (Exception e) {
                return "";
            }
        }
    }
}
