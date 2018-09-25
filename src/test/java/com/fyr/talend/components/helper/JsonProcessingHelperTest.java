package com.fyr.talend.components.helper;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue.ValueType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonProcessingHelperTest {

    private final String key = "key";

    @Test
    public void getJavaTypeFromJsonJsonValueTest() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        JsonObject jsonObject = jsonObjectBuilder.addNull(key).build();
        Assertions.assertEquals(null, JsonProcessingHelper.getJavaTypeFromJsonJsonValue(jsonObject.get(key)));

        jsonObject = jsonObjectBuilder.add(key, "123").build();
        Assertions.assertEquals("123", JsonProcessingHelper.getJavaTypeFromJsonJsonValue(jsonObject.get(key)));

        jsonObject = jsonObjectBuilder.add(key, new Integer(123)).build();
        Assertions.assertEquals(true, jsonObject.get(key).getValueType() == ValueType.NUMBER);
        Assertions.assertEquals(true, jsonObject.getJsonNumber(key).isIntegral());

        jsonObject = jsonObjectBuilder.add(key, 2.4).build();
        Assertions.assertEquals(2.4, JsonProcessingHelper.getJavaTypeFromJsonJsonValue(jsonObject.get(key)));
    }
}