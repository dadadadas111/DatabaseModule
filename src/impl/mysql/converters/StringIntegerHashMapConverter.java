package impl.mysql.converters;

import core.TypeConverter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class StringIntegerHashMapConverter implements TypeConverter<HashMap<String, Integer>, String> {
    @Override
    public String toDb(HashMap<String, Integer> javaValue) {
        if (javaValue == null) return null;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : javaValue.entrySet()) {
            String key = Base64.getEncoder().encodeToString(entry.getKey().getBytes());
            String value = Base64.getEncoder().encodeToString(entry.getValue().toString().getBytes());
            sb.append(key).append(":").append(value).append(",");
        }
        if (!sb.isEmpty()) sb.setLength(sb.length() - 1); // remove last comma
        return sb.toString();
    }

    @Override
    public HashMap<String, Integer> fromDb(String dbValue) {
        HashMap<String, Integer> map = new HashMap<>();
        if (dbValue == null || dbValue.isEmpty()) return map;
        String[] pairs = dbValue.split(",");
        for (String pair : pairs) {
            String[] kv = pair.split(":");
            if (kv.length == 2) {
                String key = new String(Base64.getDecoder().decode(kv[0]));
                String valueStr = new String(Base64.getDecoder().decode(kv[1]));
                map.put(key, Integer.parseInt(valueStr));
            }
        }
        return map;
    }
}
