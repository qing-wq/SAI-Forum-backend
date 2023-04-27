package ink.whi.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author: qing
 * @Date: 2023/4/26
 */
public class JsonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> String toStr(T val) {
        try {
            return MAPPER.writeValueAsString(val);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObj(String s, Class<T> clz) {
        try {
            return MAPPER.readValue(s, clz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
