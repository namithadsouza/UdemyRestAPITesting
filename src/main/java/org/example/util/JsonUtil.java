package org.example.util;

import io.restassured.path.json.JsonPath;

public class JsonUtil {
    public static JsonPath rawStringToJson(String response) {
        return new JsonPath(response);
    }
}
