package ru.vizzi.Utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.*;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

import java.util.Set;

public class NBTTagCompoundToJsonConverter {

    public static JsonObject convertToJson(NBTTagCompound compound) {
        JsonObject json = new JsonObject();
        Set<String> stringSet = compound.func_150296_c();
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
            return new JsonPrimitive(((NBTTagString) tag).func_150285_a_());
        } else if (tag instanceof NBTTagDouble) {
            return new JsonPrimitive(((NBTTagDouble) tag).func_150286_g());
        } else if (tag instanceof NBTTagFloat) {
            return new JsonPrimitive(((NBTTagFloat) tag).func_150288_h());
        } else if (tag instanceof NBTTagLong) {
            return new JsonPrimitive(((NBTTagLong) tag).func_150291_c());
        } else if (tag instanceof NBTTagInt) {
            return new JsonPrimitive(((NBTTagInt) tag).func_150287_d());
        } else if (tag instanceof NBTTagShort) {
            return new JsonPrimitive(((NBTTagShort) tag).func_150289_e());
        } else if (tag instanceof NBTTagByte) {
            return new JsonPrimitive(((NBTTagByte) tag).func_150290_f());
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
        for (byte b : byteArray.func_150292_c()) {
            jsonArray.add(new JsonPrimitive(b));
        }
        return jsonArray;
    }

    private static JsonElement convertIntArrayToJson(NBTTagIntArray intArray) {
        JsonArray jsonArray = new JsonArray();
        for (int i : intArray.func_150302_c()) {
            jsonArray.add(new JsonPrimitive(i));
        }
        return jsonArray;
    }
}
