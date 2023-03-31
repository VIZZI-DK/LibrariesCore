package ru.vizzi.Utils.config.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonConfigParser implements IJsonParser<JsonConfigModel> {

    private final JSONObject mainObject;
    private JsonConfigModel jsonConfigModel;

    public JsonConfigParser(String source) {
        this.mainObject = new JSONObject(source);
    }

    @Override
    public void parse() {
        jsonConfigModel = new JsonConfigModel();
        JSONArray jsonArray = mainObject.getJSONArray("config");
        parse(jsonArray);
    }

    private void parse(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            if(jsonArray.optJSONArray(i) != null) {
                JSONArray jsonArray1 = getInnerArray(jsonArray.getJSONArray(i));
                if(jsonArray1 != null) {
                    for (int i1 = 0; jsonArray1.optJSONObject(0) != null && i1 < jsonArray1.optJSONObject(0).names().length(); i1++) {
                        jsonConfigModel.getConfigMap().putAll(jsonArray1.optJSONObject(0).toMap());
                    }
                } else {
                    jsonConfigModel.getConfigMap().putAll(jsonArray.getJSONArray(i).getJSONObject(0).toMap());
                }
            } else {
                JSONArray jsonArray1 = getInnerArray(jsonArray);
                if(jsonArray1 != null) {
                    for (int i1 = 0; jsonArray1.optJSONObject(0) != null && i1 < jsonArray1.optJSONObject(0).names().length(); i1++) {
                        jsonConfigModel.getConfigMap().putAll(jsonArray1.optJSONObject(0).toMap());
                    }
                } else {
                    Map<String, Object> map = parseMap(jsonArray.getJSONObject(i));
                    if (map != null) {
                        jsonConfigModel.getConfigMap().putAll(map);
                    } else {
                        jsonConfigModel.getConfigMap().putAll(jsonArray.getJSONObject(i).toMap());
                    }
                }
            }
        }
    }

    private JSONArray getInnerArray(JSONArray jsonArray) {
        for (Object name : jsonArray.getJSONObject(0).names()) {
            if(jsonArray.getJSONObject(0).optJSONArray((String) name) != null) {
                return jsonArray.getJSONObject(0).optJSONArray((String) name);
            }
        }
        return null;
    }

    private Map<String, Object> parseMap(JSONObject jsonObject) {
        Map<String, Object> map = null;
        for (int j = 0; j < jsonObject.names().length(); j++) {
            if(jsonObject.optJSONArray(jsonObject.names().getString(j)) != null) {
                for (int i = 0; i < jsonObject.getJSONArray(jsonObject.names().getString(j)).length(); i++) {
                    for (Object name : jsonObject.getJSONArray(jsonObject.names().getString(j)).getJSONObject(i).names()) {
                        if(map == null) map = new HashMap<>();
                        map.put((String) name, jsonObject.getJSONArray(jsonObject.names().getString(j)).getJSONObject(i).get((String) name));
                    }
                }
            }
        }
        return map;
    }

    @Override
    public JsonConfigModel getResult() {
        return jsonConfigModel;
    }
}
