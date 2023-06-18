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
            NBTBase tag = convertJsonToTag(value);
            compound.setTag(key, tag);
        }
        return compound;
    }

    private static NBTBase convertJsonToTag(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            return convertToNBT(jsonElement.getAsJsonObject());
        } else if (jsonElement.isJsonArray()) {
            return convertJsonToList(jsonElement.getAsJsonArray());
        } else if (jsonElement.isJsonPrimitive()) {
            return convertJsonToPrimitive(jsonElement.getAsJsonPrimitive());
        }
        return null;
    }

    private static NBTTagList convertJsonToList(JsonArray jsonArray) {
        NBTTagList list = new NBTTagList();
        for (JsonElement element : jsonArray) {
            NBTBase tag = convertJsonToTag(element);
            list.appendTag(tag);
        }
        return list;
    }

    private static NBTBase convertJsonToPrimitive(JsonPrimitive jsonPrimitive) {
        if (jsonPrimitive.isString()) {
            return new NBTTagString(jsonPrimitive.getAsString());
        } else if (jsonPrimitive.isNumber()) {
            return convertJsonToNumber(jsonPrimitive.getAsNumber());
        } else if (jsonPrimitive.isBoolean()) {
            return new NBTTagByte((byte) (jsonPrimitive.getAsBoolean() ? 1 : 0));
        }
        return null;
    }

    private static NBTBase convertJsonToNumber(Number number) {
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