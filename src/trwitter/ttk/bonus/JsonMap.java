package trwitter.ttk.bonus;

import java.util.HashMap;

/**
 *
 * @author ctacon
 */
public class JsonMap<K, V> extends HashMap {

    public JsonMap() {
    }

    public JsonMap(String jsonString) throws Exception {
	if (jsonString == null || jsonString.isEmpty()) {
	    throw new Exception("Нуждаюсь в " + jsonString);
	}
	String[] fields = jsonString.split("[,}{]");
	for (String f : fields) {
	    String[] kv = f.replace("\"", "").split(":");
	    if (kv != null && kv.length == 2) {
		this.put(kv[0], kv[1]);
	    }
	}
    }

    @Override
    public V get(Object key) {
	return (V) super.get(key);
    }

    @Override
    public String toString() {
	StringBuilder result = new StringBuilder();
	result.append("{");
	for (Object o : keySet()) {
	    String value = get(o) == null ? "" : String.valueOf(get(o));
	    result.append("\"").append(o.toString()).append("\":").append("\"").append(value).append("\"").append(",");
	}
	result.append("}");
	return result.toString().replace(",}", "}");
    }
}
