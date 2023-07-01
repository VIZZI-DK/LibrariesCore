package ru.vizzi.librariescore;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.*;

import java.util.Set;

public class NBTTagCompoundToJsonConverter {

    public static JsonObject convertToJson(NBTTagCompound compound) {
        JsonObject json = new JsonObject();
        Set<String> stringSet = compound.getKeySet();
        for (String key : stringSet) {
            JsonElement value = convertTagToJson(compound.getTag(key));
            json.add(key, value);
        }
        return json;
    }

    private static JsonElement convertTagToJson(NBTBase tag) {
        if (tag instanceof NBTTagCompound) {
            return convertToJson((NBTTagCompound) tag);
        } else if (tag instanceof NBTTagList) {
            return convertListToJson((NBTTagList) tag);
        } else if (tag instanceof NBTTagByteArray) {
            return convertByteArrayToJson((NBTTagByteArray) tag);
        } else if (tag instanceof NBTTagIntArray) {
            return convertIntArrayToJson((NBTTagIntArray) tag);
        } else if (tag instanceof NBTTagString) {
            return new JsonPrimitive(((NBTTagString) tag).getString());
        } else if (tag instanceof NBTTagDouble) {
            return new JsonPrimitive(((NBTTagDouble) tag).getDouble());
        } else if (tag instanceof NBTTagFloat) {
            return new JsonPrimitive(((NBTTagFloat) tag).getFloat());
        } else if (tag instanceof NBTTagLong) {
            return new JsonPrimitive(((NBTTagLong) tag).getLong());
        } else if (tag instanceof NBTTagInt) {
            return new JsonPrimitive(((NBTTagInt) tag).getInt());
        } else if (tag instanceof NBTTagShort) {
            return new JsonPrimitive(((NBTTagShort) tag).getShort());
        } else if (tag instanceof NBTTagByte) {
            return new JsonPrimitive(((NBTTagByte) tag).getByte());
        }
        return null;
    }

    private static JsonElement convertListToJson(NBTTagList list) {
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < list.tagCount(); i++) {
            NBTBase elementTag = list.getCompoundTagAt(i);
            JsonElement element = convertTagToJson(elementTag);
            jsonArray.add(element);
        }
        return jsonArray;
    }

    private static JsonElement convertByteArrayToJson(NBTTagByteArray byteArray) {
        JsonArray jsonArray = new JsonArray();
        for (byte b : byteArray.getByteArray()) {
            jsonArray.add(new JsonPrimitive(b));
        }
        return jsonArray;
    }

    private static JsonElement convertIntArrayToJson(NBTTagIntArray intArray) {
        JsonArray jsonArray = new JsonArray();
        for (int i : intArray.getIntArray()) {
            jsonArray.add(new JsonPrimitive(i));
        }
        return jsonArray;
    }
}
