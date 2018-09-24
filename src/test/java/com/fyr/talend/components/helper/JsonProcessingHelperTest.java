package com.fyr.talend.components.helper;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonProcessingHelperTest {

    private final String key = "key";

    @Test
    public void getJavaTypeFromJsonJsonValueTest() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        JsonObject jsonObject = jsonObjectBuilder.addNull(key).build();
        Assertions.assertEquals(null, JsonProcessingHelper.getJavaTypeFromJsonJsonValue(jsonObject.get(key)));

    }
}