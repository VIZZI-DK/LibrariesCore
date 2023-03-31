package ru.vizzi.Utils.config.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.JSONStringer;
import org.json.JSONWriter;

import java.io.*;
import java.util.Map;

public class JsonConfigBuilder {

    private final Map<String, Object> map;
    private final Map<String, String> comments;
    private final File file;
    private final boolean rewritePretty;

    public JsonConfigBuilder(File file, Map<String, Object> map, Map<String, String> comments, boolean rewritePretty) {
        this.file = file;
        this.map = map;
        this.comments = comments;
        this.rewritePretty = rewritePretty;
    }

    public void write() {
        try {
            if(!file.exists()) {
                file.getParentFile().mkdirs();
                if (!file.createNewFile()) {
                    return;
                }
            }
            Writer writer = new FileWriter(file);
            JSONWriter jsonWriter = new JSONWriter(writer).object().key("config").array();
            for (String key : map.keySet()) {
                jsonWriter = writeElement(jsonWriter, key, map.get(key));
            }
            jsonWriter.endArray().endObject();
            writer.flush();
            writer.close();
            if(rewritePretty) {
                rewritePretty();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONWriter writeElement(JSONWriter jsonWriter, String key, Object object) {
        boolean hasComment = comments.containsKey(key);
        if(hasComment) {
            jsonWriter = jsonWriter.array();
        }
        if(object instanceof Map) {
            jsonWriter = jsonWriter.object().key(key).array();
            for(Object k : ((Map) object).keySet()) {
                if(k instanceof String) {
                    jsonWriter = jsonWriter.object().key((String) k).value(((Map) object).get(k)).endObject();
                }
            }
            jsonWriter.endArray().endObject();
        } else {
            jsonWriter = jsonWriter.object().key(key).value(object).endObject();
        }
        if(hasComment) {
            jsonWriter = jsonWriter.object().key("comment").value(convertToJson(comments.get(key), true)).endObject().endArray();
        }
        return jsonWriter;
    }

    private void rewritePretty() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String json = "";
        String line;
        while((line = bufferedReader.readLine()) != null) {
            json = json.concat(line);
        }
        bufferedReader.close();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);
        String prettyJsonString = gson.toJson(jsonElement);
        Writer writer = new FileWriter(file);
        writer.write(prettyJsonString);
        writer.close();
    }

    private static String getPrettyJSON(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);
        return gson.toJson(jsonElement);
    }

    public static String convertToJson(String string, boolean pretty) {
        JSONWriter jsonWriter = new JSONStringer().object().key("config").array();
        jsonWriter.object().key("").value(string).endObject();
        jsonWriter.endArray().endObject();
        return pretty ? getPrettyJSON(jsonWriter.toString()) : jsonWriter.toString();
    }
}
