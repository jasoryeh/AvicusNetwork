package net.avicus.libraries.quest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class QuestUtils {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String escape(String string) {
        String data = null;
        if (string != null && string.length() > 0) {
            string = string.replace("\\", "\\\\");
            string = string.replace("'", "\\'");
            string = string.replace("\0", "\\0");
            string = string.replace("\n", "\\n");
            string = string.replace("\r", "\\r");
            string = string.replace("\"", "\\\"");
            string = string.replace("\\x1a", "\\Z");
            data = string;
        }
        return data;
    }

    public static String getField(String field) {
        if (field.contains("SUM")) {
            return field;
        }
        if (field.contains("COUNT")) {
            return field;
        }
        return "`" + field + "`";
    }

    public static String getValue(Object value) {
        if (value instanceof String) {
            return "'" + escape(value.toString()) + "'";
        }
        if (value instanceof Character) {
            return getValue(value.toString());
        } else if (value instanceof Number) {
            return value.toString();
        } else if (value instanceof Date) {
            return "'" + dateFormat.format(value) + "'";
        } else if (value instanceof List) {
            List<String> values = new ArrayList<>(((List) value).size());
            for (Object object : (List) value) {
                values.add(getValue(object));
            }
            return "(" + String.join(",", values) + ")";
        } else if (value instanceof Boolean) {
            return value.equals(true) ? "1" : "0";
        } else if (value == null) {
            return "NULL";
        } else if (value.getClass().isArray()) {
            return getValue(Arrays.asList((Object[]) value));
        }
        throw new IllegalArgumentException(
                "Unknown SQL type: \"" + value.getClass().getSimpleName() + "\".");
    }
}
