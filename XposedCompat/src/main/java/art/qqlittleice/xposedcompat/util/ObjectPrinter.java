package art.qqlittleice.xposedcompat.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Java对象打印器 - 使用反射打印对象的成员属性
 * 支持递归打印嵌套对象、集合和数组
 * 支持自定义格式化和深度控制
 * 
 * @author ObjectPrinter
 * @version 1.0
 */
public class ObjectPrinter {

    // 基本类型和包装类型集合
    private static final Set<Class<?>> SIMPLE_TYPES = new HashSet<>(Arrays.asList(
        Boolean.class, Character.class, Byte.class, Short.class,
        Integer.class, Long.class, Float.class, Double.class,
        String.class, Date.class,
        boolean.class, char.class, byte.class, short.class,
        int.class, long.class, float.class, double.class
    ));

    // 默认配置
    private static final int DEFAULT_MAX_DEPTH = 5;
    private static final String DEFAULT_INDENT = "  ";
    private static final int DEFAULT_MAX_COLLECTION_SIZE = 10;

    // 打印配置类
    public static class PrintConfig {
        private int maxDepth = DEFAULT_MAX_DEPTH;
        private String indent = DEFAULT_INDENT;
        private boolean showNullFields = true;
        private boolean showStaticFields = false;
        private boolean showPrivateFields = true;
        private int maxCollectionSize = DEFAULT_MAX_COLLECTION_SIZE;
        private Set<String> excludeFields = new HashSet<>();
        private boolean prettyFormat = true;

        // Getter和Setter方法
        public PrintConfig maxDepth(int maxDepth) { this.maxDepth = maxDepth; return this; }
        public PrintConfig indent(String indent) { this.indent = indent; return this; }
        public PrintConfig showNullFields(boolean show) { this.showNullFields = show; return this; }
        public PrintConfig showStaticFields(boolean show) { this.showStaticFields = show; return this; }
        public PrintConfig showPrivateFields(boolean show) { this.showPrivateFields = show; return this; }
        public PrintConfig maxCollectionSize(int size) { this.maxCollectionSize = size; return this; }
        public PrintConfig excludeField(String fieldName) { this.excludeFields.add(fieldName); return this; }
        public PrintConfig prettyFormat(boolean pretty) { this.prettyFormat = pretty; return this; }

        // Getter方法
        public int getMaxDepth() { return maxDepth; }
        public String getIndent() { return indent; }
        public boolean isShowNullFields() { return showNullFields; }
        public boolean isShowStaticFields() { return showStaticFields; }
        public boolean isShowPrivateFields() { return showPrivateFields; }
        public int getMaxCollectionSize() { return maxCollectionSize; }
        public Set<String> getExcludeFields() { return excludeFields; }
        public boolean isPrettyFormat() { return prettyFormat; }
    }

    /**
     * 使用默认配置打印对象
     */
    public static String print(Object obj) {
        return print(obj, new PrintConfig());
    }

    /**
     * 使用自定义配置打印对象
     */
    public static String print(Object obj, PrintConfig config) {
        Set<Object> visited = Collections.newSetFromMap(new ConcurrentHashMap<>());
        return printObject(obj, config, 0, visited);
    }

    /**
     * 打印对象到控制台
     */
    public static void println(Object obj) {
        System.out.println(print(obj));
    }

    /**
     * 打印对象到控制台（带配置）
     */
    public static void println(Object obj, PrintConfig config) {
        System.out.println(print(obj, config));
    }

    /**
     * 核心打印方法
     */
    private static String printObject(Object obj, PrintConfig config, int depth, Set<Object> visited) {
        // 检查深度限制
        if (depth > config.getMaxDepth()) {
            return "...";
        }

        // 处理null
        if (obj == null) {
            return "null";
        }

        // 检查循环引用
        if (visited.contains(obj)) {
            return obj.getClass().getSimpleName() + "@" + System.identityHashCode(obj) + " (circular reference)";
        }

        Class<?> clazz = obj.getClass();

        // 处理基本类型
        if (isSimpleType(clazz)) {
            return formatSimpleValue(obj);
        }

        // 处理字符串
        if (obj instanceof String) {
            return "\"" + obj.toString() + "\"";
        }

        // 处理数组
        if (clazz.isArray()) {
            return printArray(obj, config, depth, visited);
        }

        // 处理集合
        if (obj instanceof Collection) {
            return printCollection((Collection<?>) obj, config, depth, visited);
        }

        // 处理Map
        if (obj instanceof Map) {
            return printMap((Map<?, ?>) obj, config, depth, visited);
        }

        // 处理普通对象
        visited.add(obj);
        try {
            return printComplexObject(obj, clazz, config, depth, visited);
        } finally {
            visited.remove(obj);
        }
    }

    /**
     * 打印复杂对象
     */
    private static String printComplexObject(Object obj, Class<?> clazz, PrintConfig config, int depth, Set<Object> visited) {
        StringBuilder sb = new StringBuilder();
        sb.append(clazz.getSimpleName());

        if (config.isPrettyFormat()) {
            sb.append(" {\n");
        } else {
            sb.append("[");
        }

        List<Field> fields = getAllFields(clazz);
        boolean hasContent = false;

        for (Field field : fields) {
            // 跳过不需要的字段
            if (shouldSkipField(field, config)) {
                continue;
            }

            field.setAccessible(true);
            try {
                Object value = field.get(obj);

                // 跳过null值（如果配置要求）
                if (value == null && !config.isShowNullFields()) {
                    continue;
                }

                if (hasContent) {
                    sb.append(config.isPrettyFormat() ? ",\n" : ", ");
                }

                if (config.isPrettyFormat()) {
                    sb.append(getIndent(depth + 1, config.getIndent()));
                }

                sb.append(field.getName()).append(": ");
                sb.append(printObject(value, config, depth + 1, visited));

                hasContent = true;

            } catch (IllegalAccessException e) {
                sb.append(field.getName()).append(": <access denied>");
                hasContent = true;
            }
        }

        if (config.isPrettyFormat()) {
            if (hasContent) {
                sb.append("\n").append(getIndent(depth, config.getIndent()));
            }
            sb.append("}");
        } else {
            sb.append("]");
        }

        return sb.toString();
    }

    /**
     * 打印数组
     */
    private static String printArray(Object array, PrintConfig config, int depth, Set<Object> visited) {
        int length = java.lang.reflect.Array.getLength(array);
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        int maxSize = Math.min(length, config.getMaxCollectionSize());
        for (int i = 0; i < maxSize; i++) {
            if (i > 0) sb.append(", ");
            Object element = java.lang.reflect.Array.get(array, i);
            sb.append(printObject(element, config, depth + 1, visited));
        }

        if (length > config.getMaxCollectionSize()) {
            sb.append(", ... (").append(length - config.getMaxCollectionSize()).append(" more)");
        }

        sb.append("]");
        return sb.toString();
    }

    /**
     * 打印集合
     */
    private static String printCollection(Collection<?> collection, PrintConfig config, int depth, Set<Object> visited) {
        StringBuilder sb = new StringBuilder();
        sb.append(collection.getClass().getSimpleName()).append("[");

        int count = 0;
        for (Object element : collection) {
            if (count >= config.getMaxCollectionSize()) {
                sb.append(", ... (").append(collection.size() - config.getMaxCollectionSize()).append(" more)");
                break;
            }
            if (count > 0) sb.append(", ");
            sb.append(printObject(element, config, depth + 1, visited));
            count++;
        }

        sb.append("]");
        return sb.toString();
    }

    /**
     * 打印Map
     */
    private static String printMap(Map<?, ?> map, PrintConfig config, int depth, Set<Object> visited) {
        StringBuilder sb = new StringBuilder();
        sb.append(map.getClass().getSimpleName()).append("{");

        int count = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (count >= config.getMaxCollectionSize()) {
                sb.append(", ... (").append(map.size() - config.getMaxCollectionSize()).append(" more)");
                break;
            }
            if (count > 0) sb.append(", ");
            sb.append(printObject(entry.getKey(), config, depth + 1, visited));
            sb.append("=");
            sb.append(printObject(entry.getValue(), config, depth + 1, visited));
            count++;
        }

        sb.append("}");
        return sb.toString();
    }

    /**
     * 获取所有字段（包括父类）
     */
    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    /**
     * 判断是否应该跳过字段
     */
    private static boolean shouldSkipField(Field field, PrintConfig config) {
        String fieldName = field.getName();

        // 跳过排除的字段
        if (config.getExcludeFields().contains(fieldName)) {
            return true;
        }

        // 跳过serialVersionUID
        if ("serialVersionUID".equals(fieldName)) {
            return true;
        }

        int modifiers = field.getModifiers();

        // 检查静态字段
        if (Modifier.isStatic(modifiers) && !config.isShowStaticFields()) {
            return true;
        }

        // 检查私有字段
        if (Modifier.isPrivate(modifiers) && !config.isShowPrivateFields()) {
            return true;
        }

        return false;
    }

    /**
     * 判断是否为简单类型
     */
    private static boolean isSimpleType(Class<?> clazz) {
        return SIMPLE_TYPES.contains(clazz) || clazz.isPrimitive() || clazz.isEnum();
    }

    /**
     * 格式化简单值
     */
    private static String formatSimpleValue(Object obj) {
        if (obj instanceof String) {
            return "\"" + obj + "\"";
        }
        if (obj instanceof Character) {
            return "'" + obj + "'";
        }
        return String.valueOf(obj);
    }

    /**
     * 生成缩进
     */
    private static String getIndent(int depth, String indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append(indent);
        }
        return sb.toString();
    }
}
