package com.fyr.talend.components.helper;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonValue;

/**
 * Helper class to assist in converting from javax.json to native Java objects.
 */
public abstract class JsonProcessingHelper {

  /**
   * Transform a JsonValue to a Java Object
   * 
   * @param jsonValue JsonValue
   * @return Java Object of JsonValue
   * @throws Exception When a nested object is included in the JsonValue an
   *                   Exception is thrown. Because it is not supported to have
   *                   nested documents at the moment.
   */
  public static Object getJavaTypeFromJsonJsonValue(JsonValue jsonValue) throws UnsupportedOperationException {
    switch (jsonValue.getValueType()) {
    case ARRAY:
      return getList((JsonArray) jsonValue);
    case NUMBER:
      JsonNumber number = (JsonNumber) jsonValue;
      return number.isIntegral() ? number.longValue() : number.doubleValue();
    case STRING:
      return ((JsonString) jsonValue).getString();
    case TRUE:
      return Boolean.TRUE;
    case FALSE:
      return Boolean.FALSE;
    case OBJECT:
      throw new UnsupportedOperationException("Nested documents not yet supported.");
    case NULL:
    default:
      return null;
    }
  }

  /**
   * Return a Java List based on a JsonArray
   * 
   * @param jsonArray JsonArray
   * @return Java List of objects
   * @throws Exception Whenever the JsonArray includes a nested JsonObject value.
   *                   Because it is not supported at the moment.
   */
  private static List<Object> getList(JsonArray jsonArray) throws UnsupportedOperationException {
    List<Object> list = new ArrayList<Object>(jsonArray.size());
    for (JsonValue value : jsonArray) {
      Object obj = getJavaTypeFromJsonJsonValue(value);
      if (value != null) {
        list.add(obj);
      }
    }
    return list;
  }

}