package eu.thesystems.cloudnet.discord.json;
/*
 * Created by Mc_Ruben on 05.11.2018
 */

import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleJsonObject {

    public static final JsonParser PARSER = new JsonParser();
    public static final ThreadLocal<Gson> GSON = ThreadLocal.withInitial(() -> new GsonBuilder().setPrettyPrinting().create());


    private JsonObject jsonObject;

    public SimpleJsonObject() {
        this(new JsonObject());
    }

    public SimpleJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public SimpleJsonObject(String input) {
        JsonElement jsonElement;
        try {
            jsonElement = PARSER.parse(input);
        } catch (Exception e) {
            e.printStackTrace();
            jsonElement = new JsonObject();
        }
        if (!jsonElement.isJsonObject()) {
            throw new IllegalArgumentException("JsonInput must be an json object, not " + jsonElement.getClass().getSimpleName());
        }
        this.jsonObject = jsonElement.getAsJsonObject();
    }

    public SimpleJsonObject(Reader reader) {
        JsonElement jsonElement;
        try {
            jsonElement = PARSER.parse(reader);
        } catch (Exception e) {
            e.printStackTrace();
            jsonElement = new JsonObject();
        }
        if (!jsonElement.isJsonObject()) {
            throw new IllegalArgumentException("JsonInput must be an json object, not " + jsonElement.getClass().getSimpleName());
        }
        this.jsonObject = jsonElement.getAsJsonObject();
    }

    public SimpleJsonObject(Object object) {
        JsonElement jsonElement = GSON.get().toJsonTree(object);
        if (!jsonElement.isJsonObject()) {
            throw new IllegalArgumentException("JsonInput must be an json object, not " + jsonElement.getClass().getSimpleName());
        }
        this.jsonObject = jsonElement.getAsJsonObject();
    }

    public SimpleJsonObject append(String key, Object value) {
        if (value == null) {
            this.jsonObject.add(key, JsonNull.INSTANCE);
            return this;
        }

        this.jsonObject.add(key, GSON.get().toJsonTree(value));
        return this;
    }

    public SimpleJsonObject append(String key, SimpleJsonObject value) {
        if (value == null) {
            this.jsonObject.add(key, JsonNull.INSTANCE);
            return this;
        }
        this.append(key, value.jsonObject);
        return this;
    }

    public SimpleJsonObject append(String key, String value) {
        this.jsonObject.addProperty(key, value);
        return this;
    }

    public SimpleJsonObject append(String key, Character value) {
        this.jsonObject.addProperty(key, value);
        return this;
    }

    public SimpleJsonObject append(String key, Boolean value) {
        this.jsonObject.addProperty(key, value);
        return this;
    }

    public SimpleJsonObject append(String key, Number value) {
        this.jsonObject.addProperty(key, value);
        return this;
    }

    public boolean contains(String key) {
        return this.jsonObject.has(key) && !(this.jsonObject.get(key) instanceof JsonNull);
    }

    public JsonArray getJsonArray(String key) {
        JsonElement element = this.get(key);
        return element != null && element.isJsonArray() ? element.getAsJsonArray() : null;
    }

    public SimpleJsonObject getDocument(String key) {
        JsonElement jsonElement = this.get(key);
        return jsonElement != null && jsonElement.isJsonObject() ? new SimpleJsonObject(jsonElement.getAsJsonObject()) : null;
    }

    public JsonElement get(String key) {
        JsonElement jsonElement = this.jsonObject.get(key);
        if (jsonElement instanceof JsonNull)
            return null;
        return jsonElement;
    }

    public String getString(String key) {
        return contains(key) ? get(key).getAsString() : null;
    }

    public boolean getBoolean(String key) {
        return contains(key) && get(key).getAsBoolean();
    }

    public char getCharacter(String key) {
        return contains(key) ? get(key).getAsCharacter() : (char) 0;
    }

    public byte getByte(String key) {
        return contains(key) ? get(key).getAsByte() : -1;
    }

    public short getShort(String key) {
        return contains(key) ? get(key).getAsShort() : -1;
    }

    public int getInt(String key) {
        return contains(key) ? get(key).getAsInt() : -1;
    }

    public long getLong(String key) {
        return contains(key) ? get(key).getAsLong() : -1;
    }

    public double getDouble(String key) {
        return contains(key) ? get(key).getAsDouble() : -1;
    }

    public BigInteger getBigInteger(String key) {
        return contains(key) ? get(key).getAsBigInteger() : null;
    }

    public BigDecimal getBigDecimal(String key) {
        return contains(key) ? get(key).getAsBigDecimal() : null;
    }

    public <T> T getObject(String key, Class<T> tClass) {
        return this.get(key) == null ? null : GSON.get().fromJson(this.get(key), tClass);
    }

    public <T> T getObject(String key, Type type) {
        return this.get(key) == null ? null : GSON.get().fromJson(this.get(key), type);
    }

    public JsonObject getJsonObject(String key) {
        JsonElement jsonElement = this.jsonObject.get(key);
        return jsonElement != null && jsonElement.isJsonObject() ? jsonElement.getAsJsonObject() : null;
    }

    public JsonObject asJsonObject() {
        return jsonObject;
    }

    public void saveAsFile(Path path) {
        if (!Files.exists(path)) {
            if (path.getParent() != null) {
                try {
                    Files.createDirectories(path.getParent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path.toFile()), StandardCharsets.UTF_8)) {
            GSON.get().toJson(this.jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SimpleJsonObject load(String path) {
        return load(Paths.get(path));
    }

    public static SimpleJsonObject load(Path path) {
        if (!Files.exists(path))
            return new SimpleJsonObject();
        try (InputStream inputStream = Files.newInputStream(path)) {
            return load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SimpleJsonObject();
    }

    public static SimpleJsonObject load(InputStream inputStream) {
        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            return new SimpleJsonObject(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SimpleJsonObject();
    }

    public String toJson() {
        return this.jsonObject.toString();
    }

    @Override
    public String toString() {
        return this.toJson();
    }

    public String toPrettyJson() {
        return GSON.get().toJson(this.jsonObject);
    }

    public byte[] toBytes() {
        return toJson().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] toPrettyBytes() {
        return toPrettyJson().getBytes(StandardCharsets.UTF_8);
    }
}
