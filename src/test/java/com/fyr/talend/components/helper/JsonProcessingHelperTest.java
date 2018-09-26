package com.fyr.talend.components.helper;

import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue.ValueType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonProcessingHelperTest {

    private final String key = "key";

    @Test
    public void getJavaTypeFromJsonValueTest() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        JsonObject jsonObject = jsonObjectBuilder.addNull(key).build();
        Assertions.assertEquals(null,
                JsonProcessingHelper.getJavaTypeFromJsonJsonValue(jsonObject.get(key)));

        jsonObject = jsonObjectBuilder.add(key, "123").build();
        Assertions.assertEquals("123",
                JsonProcessingHelper.getJavaTypeFromJsonJsonValue(jsonObject.get(key)));

        jsonObject = jsonObjectBuilder.add(key, new Integer(123)).build();
        Assertions.assertEquals(true, jsonObject.get(key).getValueType() == ValueType.NUMBER);
        Assertions.assertEquals(true, jsonObject.getJsonNumber(key).isIntegral());

        jsonObject = jsonObjectBuilder.add(key, 2.4).build();
        Assertions.assertEquals(2.4,
                JsonProcessingHelper.getJavaTypeFromJsonJsonValue(jsonObject.get(key)));

        jsonObject = jsonObjectBuilder.add(key, true).build();
        Assertions.assertEquals(true,
                JsonProcessingHelper.getJavaTypeFromJsonJsonValue(jsonObject.get(key)));

        jsonObject = jsonObjectBuilder.add(key, false).build();
        Assertions.assertEquals(false,
                JsonProcessingHelper.getJavaTypeFromJsonJsonValue(jsonObject.get(key)));
    }

    @Test
    public void getJavaListTypeFromJsonValueTest() {
        List<String> list = new ArrayList<>();
        list.add("test");
        list.add("test2");
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        JsonObject jsonObject = jsonObjectBuilder
                .add(key, jsonArrayBuilder.add("test").add("test2").build()).build();
        Object obj = JsonProcessingHelper.getJavaTypeFromJsonJsonValue(jsonObject.get(key));
        Assertions.assertEquals(ArrayList.class, obj.getClass());

        @SuppressWarnings("unchecked")
        List<String> outList = (ArrayList<String>) obj;

        Assertions.assertEquals(2, outList.size());
        Assertions.assertEquals(true, outList.contains("test"));
        Assertions.assertEquals(true, outList.contains("test2"));
        Assertions.assertEquals(false, outList.contains("test3"));
    }

    @Test
    public void testUnsupportedOperationException() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        JsonObject jsonObject =
                jsonObjectBuilder.add(key, jsonObjectBuilder.add("key", "value").build()).build();
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> JsonProcessingHelper.getJavaTypeFromJsonJsonValue(jsonObject.get(key)));
    }
}
