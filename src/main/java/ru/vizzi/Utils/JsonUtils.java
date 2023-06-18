package ru.vizzi.Utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.SneakyThrows;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.vizzi.Utils.databases.DatabaseAdaptedBy;
import ru.vizzi.Utils.databases.DatabaseValue;
import java.io.IOException;
import java.lang.reflect.Field;

import java.util.HashMap;
import java.util.Map;

@GradleSideOnly(GradleSide.SERVER)
public class JsonUtils {



	
	public static final Gson DB_GSON;


	    
	    public static final TypeAdapter<ItemStack> ITEM_STACK_TYPE_ADAPTER = new TypeAdapter<ItemStack>() {
	        @Override
	        public void write(JsonWriter out, ItemStack value) throws IOException {
	            if(value == null) {
	                return;
	            }
	            out.beginArray();
	            Item item = value.getItem();
	            if(item != null) {
	                out.value(Item.itemRegistry.getNameForObject(item));
	                out.value(value.stackSize);
	                out.value(value.getItemDamage());
					DB_GSON.toJson(DB_GSON.toJsonTree(value.getTagCompound(), NBTTagCompound.class), out);
	            } else {
//	                out.value("null");
//	                out.value(1);
//	                out.value(0);
//	                out.value("");
	                throw new IOException("Cannot save an itemStack " + value);
	            }
	            out.endArray();
	        }

	        @Override
	        public ItemStack read(JsonReader in) {
	            try {
	                in.beginArray();
	                String unlocalizedName = in.nextString();
	                int amount = in.nextInt();
	                int damage = in.nextInt();
					NBTTagCompound tagCompound = DB_GSON.fromJson(in, NBTTagCompound.class);
	                //String jsonNbt = in.nextString();
	                ItemStack itemStack = ItemStackFactory.create(unlocalizedName, amount, damage, tagCompound);
	                if(itemStack == null) {
	                    throw new IOException("item with unlocalized name " + unlocalizedName + " was not found.");
	                }
	                in.endArray();
	                return itemStack;
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            return null;
	        }
	    };

	public static final TypeAdapter<NBTTagCompound> nbtTagCompoundTypeAdapter = new TypeAdapter<NBTTagCompound>() {
		@Override
		public void write(JsonWriter out, NBTTagCompound value) throws IOException {
			if(value == null) {
				out.beginObject();
				out.endObject();
				return;
			}

			JsonObject jsonObject = NBTTagCompoundToJsonConverter.convertToJson(value);
			writeObject(out, jsonObject);

		}

		public void writeObject(JsonWriter out, JsonObject jsonObject) throws IOException {
			out.beginObject();
			for (Map.Entry<String, JsonElement> key : jsonObject.entrySet()) {
				String name = key.getKey();
				if(key.getValue().isJsonPrimitive() && key.getValue().getAsJsonPrimitive().isNumber()) {
					Number number = key.getValue().getAsJsonPrimitive().getAsNumber();
					if (number instanceof Double) {
						name += "@d";
					} else if (number instanceof Float) {
						name += "@f";
					} else if (number instanceof Long) {
						name += "@l";
					} else if (number instanceof Integer) {
						name += "@i";
					} else if (number instanceof Short) {
						name += "@s";
					} else if (number instanceof Byte) {
						name += "@b";
					}
					out.name(name);
					out.value(number);
					continue;
				}
				if(key.getValue().isJsonArray()){
					if(key.getValue().getAsJsonArray().get(0).isJsonPrimitive() && key.getValue().getAsJsonArray().get(0).getAsJsonPrimitive().isNumber()){
						Number number = key.getValue().getAsJsonArray().get(0).getAsJsonPrimitive().getAsNumber();
						if(number instanceof Integer){
							name +="@I";
						} else if(number instanceof Byte){
							name+="@B";
						}
						out.name(name);
						out.beginArray();
						for(JsonElement jsonElement : key.getValue().getAsJsonArray()){
							JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
							out.value(jsonPrimitive.getAsNumber());
						}
						out.endArray();
						continue;
					}
				}
				if(key.getValue().isJsonPrimitive() && key.getValue().getAsJsonPrimitive().isString()){
					out.name(name);
					out.value(key.getValue().getAsJsonPrimitive().getAsString());
					continue;
				}
				if(key.getValue().isJsonObject()){
					JsonObject jsonObject1 = key.getValue().getAsJsonObject();
					out.name(name);
					writeObject(out, jsonObject1);
					continue;
				}
				if(key.getValue().isJsonArray()){
					JsonArray jsonArray = key.getValue().getAsJsonArray();
					out.name(name);
					writeArray(out, jsonArray);
					continue;
				}
				out.name(name);
				out.value(key.getValue().toString());
			}
			out.endObject();
		}
		public void writeArray(JsonWriter out, JsonArray jsonArray) throws IOException {
			out.beginArray();
			for (JsonElement value : jsonArray) {
				if(value.isJsonObject()){
					JsonObject jsonObject1 = value.getAsJsonObject();
					writeObject(out, jsonObject1);
				} else {
					System.out.println(value);
				}
			}
			out.endArray();
		}

		@Override
		public NBTTagCompound read(JsonReader in) {
			try {
				JsonObject jsonObject = readJsonObject(in);
				NBTTagCompound tagCompound = JsonToNBTTagCompoundConverter.convertToNBT(jsonObject);
				return tagCompound;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	};

	private static JsonObject readJsonObject(JsonReader reader) throws IOException {
		JsonObject jsonObject = new JsonObject();

		reader.beginObject();
		while (reader.hasNext()) {
			String key = reader.nextName();
			JsonElement value = isNumber(key, reader);
			if(value != null){
				key = key.substring(0, key.length()-2);
			} else {
				value = readJsonValue(reader);
			}
			jsonObject.add(key, value);
		}
		reader.endObject();
		return jsonObject;
	}
	private static JsonArray readJsonArray(JsonReader reader) throws IOException {
		JsonArray jsonArray = new JsonArray();

		reader.beginArray();
		while (reader.hasNext()) {
			JsonElement value;
			value = readJsonValue(reader);
			jsonArray.add(value);
		}
		reader.endArray();
		return jsonArray;
	}

	public static JsonElement isNumber(String key, JsonReader reader) throws IOException {
		if(key.endsWith("@i")){
			return new JsonPrimitive(Integer.parseInt(reader.nextString()));
		} else if(key.endsWith("@f")){
			return new JsonPrimitive(Float.parseFloat(reader.nextString()));
		} else if(key.endsWith("@d")){
			return new JsonPrimitive(Double.parseDouble(reader.nextString()));
		} else if(key.endsWith("@l")){
			return new JsonPrimitive(Long.parseLong(reader.nextString()));
		} else if (key.endsWith("@b")) {
			return new JsonPrimitive(Byte.parseByte(reader.nextString()));
		} else if (key.endsWith("@s")) {
			return new JsonPrimitive(Short.parseShort(reader.nextString()));
		} else if (key.endsWith("@I")) {
			reader.beginArray();
			JsonArray jsonArray = new JsonArray();
			while(reader.hasNext()){
				jsonArray.add(new JsonPrimitive(reader.nextInt()));
			}
			reader.endArray();
			return jsonArray;
		}
		else if (key.endsWith("@B")) {
			reader.beginArray();
			JsonArray jsonArray = new JsonArray();
			while(reader.hasNext()){
				jsonArray.add(new JsonPrimitive(Byte.parseByte(reader.nextString())));
			}
			reader.endArray();
			return jsonArray;
		}
		return null;
	}

	private static JsonElement readJsonValue(JsonReader reader) throws IOException {
		JsonElement value = null;
		switch (reader.peek()) {
			case BEGIN_OBJECT:
				value = readJsonObject(reader);
				break;
			case BEGIN_ARRAY:
				value = readJsonArray(reader);
				break;
			case STRING:
				value = new JsonPrimitive(reader.nextString());
				break;
			case BOOLEAN:
				value = new JsonPrimitive(reader.nextBoolean());
				break;
			case NULL:
				reader.nextNull();
				break;
		}
		return value;
	}




	    
	    static {
	        DB_GSON = new GsonBuilder()
	        	//	.registerTypeAdapter(HashMap.class, HASHMAP_COST_ADAPTER)
	        		.registerTypeAdapter(ItemStack.class, ITEM_STACK_TYPE_ADAPTER)
					.registerTypeAdapter(NBTTagCompound.class, nbtTagCompoundTypeAdapter)
	                .registerTypeAdapterFactory(new TypeAdapterFactory() {
	                    private final Map<Class<?>, Object> fieldTypeAdapters = new HashMap<>();

	                    @SneakyThrows
	                    @Override
	                    @SuppressWarnings("unchecked")
	                    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
	                        DatabaseAdaptedBy annotation = type.getRawType().getAnnotation(DatabaseAdaptedBy.class);
	                        if(annotation != null) {
//	                                System.out.println(type.getRawType());
	                            return (TypeAdapter<T>) annotation.value().newInstance();
	                        }
	                        Object o = fieldTypeAdapters.get(type.getRawType());
	                        if(o != null) {
	                            return (TypeAdapter<T>) fieldTypeAdapters.remove(type.getRawType());
	                        }
	                        for (Field field : type.getRawType().getDeclaredFields()) {
	                            annotation = field.getAnnotation(DatabaseAdaptedBy.class);
	                            if(annotation != null) {
	                                o = fieldTypeAdapters.get(field.getType());
	                                if (o != null) {
	                                    System.out.println("field serialize adapter for " + field + " already registered. This one will be ignored.");
	                                } else {
//	                                        System.out.println("found serialize adapter for " + field.getType());
	                                    fieldTypeAdapters.put(field.getType(), annotation.value().newInstance());
	                                }
	                            }
	                        }
	                        return null;
	                    }
	                })
	                .setExclusionStrategies(new ExclusionStrategy() {
	                    @Override
	                    public boolean shouldSkipField(FieldAttributes f) {
	                        return f.getDeclaringClass().getAnnotation(DatabaseValue.class) == null && f.getAnnotation(DatabaseValue.class) == null;
	                    }

	                    @Override
	                    public boolean shouldSkipClass(Class<?> clazz) {
	                        return false;
	                    }
	                })
	                .setPrettyPrinting()
	                .serializeNulls()
	                .create();
	    }
	    
//	    @Nonnull
//	    public static String nbtToJson(@Nonnull NBTTagCompound nbtTagCompound) {
//	        return JsonUtilsMinecraft.getJsonFromNbt(nbtTagCompound);
//	    }
//
//	    //не конвертит адекватно массивы
//	    @Nullable
//	    public static NBTTagCompound jsonToNBT(@Nullable String json) {
//	        if(json != null) {
//				return JsonUtilsMinecraft.getNbtFromJson(json);
//			}
//	        return null;
//	    }

//	    public static String itemStackToJson(ItemStack itemStack) {
//	        String stringedNbtTagCompound = JsonUtilsMinecraft.getJsonFromNbt(itemStack.getTagCompound() != null ? itemStack.getTagCompound() : new NBTTagCompound());
//	        ItemStackModel itemStackModel = new ItemStackModel(
//	                Item.itemRegistry.getNameForObject(itemStack.getItem()),
//	                itemStack.stackSize,
//	                itemStack.getItemDamage(),
//	                stringedNbtTagCompound
//	        );
//	        return DB_GSON.toJson(itemStackModel);
//	    }
//
//	    public static ItemStack jsonToItemStack(String json) {
//	        ItemStackModel itemStackModel = DB_GSON.fromJson(json, ItemStackModel.class);
//	        ItemStack itemStack = new ItemStack((Item)Item.itemRegistry.getObject(itemStackModel.getName()), itemStackModel.getCount(), itemStackModel.getDamage());
//	        itemStack.setItemDamage(itemStackModel.getDamage());
//	        itemStack.setTagCompound(JsonUtilsMinecraft.getNbtFromJson(itemStackModel.getStringedNbtTagCompound()));
//	        return itemStack;
//	    }

}
