package org.example.util;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class JsonUtil {
    public static JsonPath rawStringToJson(String response) {
        return new JsonPath(response);
    }

    public static String getJsonPathValue(Response response, String key) {
        String resp = response.asString();
        JsonPath js = rawStringToJson(resp);
        return js.get(key).toString();
    }

}
