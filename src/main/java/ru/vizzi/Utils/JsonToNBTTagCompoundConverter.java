package ru.vizzi.Utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.*;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

import java.util.Map;
@GradleSideOnly(GradleSide.SERVER)
public class JsonToNBTTagCompoundConverter {

    public static NBTTagCompound convertToNBT(JsonObject json) {
        NBTTagCompound compound = new NBTTagCompound();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            NBTBase tag = convertJsonToTag(value, key);
            if(key.endsWith("@I") || key.endsWith("@B")){
                key.substring(0, key.length()-2);
            }
            compound.setTag(key, tag);
        }
        return compound;
    }

    private static NBTBase convertJsonToTag(JsonElement jsonElement, String key) {
        if (jsonElement.isJsonObject()) {
            return convertToNBT(jsonElement.getAsJsonObject());
        } else if (jsonElement.isJsonArray()) {
            return convertJsonToList(jsonElement.getAsJsonArray(), key);
        } else if (jsonElement.isJsonPrimitive()) {
            return convertJsonToPrimitive(jsonElement.getAsJsonPrimitive(), key);
        }
        return null;
    }

    private static NBTBase convertJsonToList(JsonArray jsonArray, String key) {
        if(key.endsWith("@I")){
            int[] array = new int[jsonArray.size()];
            for(int i = 0; i < jsonArray.size(); i++){
                array[i] = jsonArray.get(i).getAsJsonPrimitive().getAsInt();
            }
            return new NBTTagIntArray(array);
        }
        if(key.endsWith("@B")){
            byte[] array = new byte[jsonArray.size()];
            for(int i = 0; i < jsonArray.size(); i++){
                array[i] = jsonArray.get(i).getAsJsonPrimitive().getAsByte();
            }
            return new NBTTagByteArray(array);
        }

        NBTTagList list = new NBTTagList();
        for (JsonElement element : jsonArray) {
            NBTBase tag = convertJsonToTag(element, key);
            list.appendTag(tag);
        }
        return list;
    }

    private static NBTBase convertJsonToPrimitive(JsonPrimitive jsonPrimitive, String key) {
        if (jsonPrimitive.isString()) {
            return new NBTTagString(jsonPrimitive.getAsString());
        } else if (jsonPrimitive.isNumber()) {
            return convertJsonToNumber(jsonPrimitive.getAsNumber(), key);
        } else if (jsonPrimitive.isBoolean()) {
            return new NBTTagByte((byte) (jsonPrimitive.getAsBoolean() ? 1 : 0));
        }
        return null;
    }

    private static NBTBase convertJsonToNumber(Number number, String key) {

        if(key.endsWith("@d")){
            return new NBTTagDouble(number.doubleValue());
        } else if(key.endsWith("@f")){
            return new NBTTagFloat(number.floatValue());
        } else if (key.endsWith("@l")) {
            return new NBTTagLong(number.longValue());
        } else if(key.endsWith("@i")){
            return new NBTTagInt(number.intValue());
        } else if (key.endsWith("@s")) {
            return new NBTTagShort(number.shortValue());
        } else if(key.endsWith("@b")){
            return new NBTTagByte(number.byteValue());
        }


        if (number instanceof Double) {
            return new NBTTagDouble(number.doubleValue());
        } else if (number instanceof Float) {
            return new NBTTagFloat(number.floatValue());
        } else if (number instanceof Long) {
            return new NBTTagLong(number.longValue());
        } else if (number instanceof Integer) {
            return new NBTTagInt(number.intValue());
        } else if (number instanceof Short) {
            return new NBTTagShort(number.shortValue());
        } else if (number instanceof Byte) {
            return new NBTTagByte(number.byteValue());
        }
        return null;
    }
}