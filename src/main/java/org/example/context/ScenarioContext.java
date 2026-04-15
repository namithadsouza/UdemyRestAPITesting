package org.example.context;


import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class ScenarioContext {
    private Map<String, Object> data = new HashMap<>();

    public void setData(String key, Object value) {
        data.put(key, value);
    }

    public Object getData(String key) {
        return data.get(key);
    }
}