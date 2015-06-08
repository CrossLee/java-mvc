package org.osgl.mvc.util;

import org.osgl._;
import org.osgl.exception.NotAppliedException;
import org.osgl.util.C;
import org.osgl.util.FastStr;
import org.osgl.util.S;
import org.osgl.util.Str;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * A String value resolver resolves a {@link java.lang.String string value} into
 * a certain type of object instance.
 */
public abstract class StringValueResolver<T> extends _.F1<String, T> {

    public abstract T resolve(String value);

    @Override
    public T apply(String s) throws NotAppliedException, _.Break {
        return resolve(s);
    }

    public static <T> StringValueResolver<T> wrap(final _.Function<String, T> func) {
        if (func instanceof StringValueResolver) {
            Type c = int.class;
            return (StringValueResolver)func;
        } else {
            return new StringValueResolver<T>() {
                @Override
                public T resolve(String value) {
                    return func.apply(value);
                }
            };
        }
    }

    // For primary types
    private static final StringValueResolver<Boolean> _boolean = new StringValueResolver<Boolean>() {
        @Override
        public Boolean resolve(String value) {
            if (S.empty(value)) {
                return Boolean.FALSE;
            }
            return Boolean.parseBoolean(value);
        }
    };
    private static final StringValueResolver<Boolean> _Boolean = new StringValueResolver<Boolean>() {
        @Override
        public Boolean resolve(String value) {
            if (S.empty(value)) {
                return null;
            }
            return Boolean.parseBoolean(value);
        }
    };
    private static final StringValueResolver<Character> _char = new StringValueResolver<Character>() {
        @Override
        public Character resolve(String value) {
            if (S.empty(value)) {
                return ' ';
            }
            return value.charAt(0);
        }
    };
    private static final StringValueResolver<Character> _Char = new StringValueResolver<Character>() {
        @Override
        public Character resolve(String value) {
            if (S.empty(value)) {
                return null;
            }
            return value.charAt(0);
        }
    };
    private static final StringValueResolver<Byte> _byte = new StringValueResolver<Byte>() {
        @Override
        public Byte resolve(String value) {
            if (S.blank(value)) {
                return (byte)0;
            }
            return Byte.parseByte(value);
        }
    };
    private static final StringValueResolver<Byte> _Byte = new StringValueResolver<Byte>() {
        @Override
        public Byte resolve(String value) {
            if (S.blank(value)) {
                return null;
            }
            return Byte.parseByte(value);
        }
    };
    private static final StringValueResolver<Short> _short = new StringValueResolver<Short>() {
        @Override
        public Short resolve(String value) {
            if (S.blank(value)) {
                return (short)0;
            }
            return Short.valueOf(value);
        }
    };
    private static final StringValueResolver<Short> _Short = new StringValueResolver<Short>() {
        @Override
        public Short resolve(String value) {
            if (S.blank(value)) {
                return null;
            }
            return Short.valueOf(value);
        }
    };
    private static int _int(String s) {
        if (s.contains(".")) {
            float f = Float.valueOf(s);
            return Math.round(f);
        } else {
            return Integer.valueOf(s);
        }
    }
    private static final StringValueResolver<Integer> _int = new StringValueResolver<Integer>() {
        @Override
        public Integer resolve(String value) {
            if (S.blank(value)) {
                return 0;
            }
            return _int(value);
        }
    };
    private static final StringValueResolver<Integer> _Integer = new StringValueResolver<Integer>() {
        @Override
        public Integer resolve(String value) {
            if (S.blank(value)) {
                return null;
            }
            return _int(value);
        }
    };
    private static final StringValueResolver<Long> _long = new StringValueResolver<Long>() {
        @Override
        public Long resolve(String value) {
            if (S.blank(value)) {
                return 0l;
            }
            return Long.valueOf(value);
        }
    };
    private static final StringValueResolver<Long> _Long = new StringValueResolver<Long>() {
        @Override
        public Long resolve(String value) {
            if (S.blank(value)) {
                return null;
            }
            return Long.valueOf(value);
        }
    };
    private static final StringValueResolver<Float> _float = new StringValueResolver<Float>() {
        @Override
        public Float resolve(String value) {
            if (S.blank(value)) {
                return 0f;
            }
            return Float.valueOf(value);
        }
    };
    private static final StringValueResolver<Float> _Float = new StringValueResolver<Float>() {
        @Override
        public Float resolve(String value) {
            if (S.blank(value)) {
                return null;
            }
            return Float.valueOf(value);
        }
    };
    private static final StringValueResolver<Double> _double = new StringValueResolver<Double>() {
        @Override
        public Double resolve(String value) {
            if (S.blank(value)) {
                return 0d;
            }
            return Double.valueOf(value);
        }
    };
    private static final StringValueResolver<Double> _Double = new StringValueResolver<Double>() {
        @Override
        public Double resolve(String value) {
            if (S.blank(value)) {
                return null;
            }
            return Double.valueOf(value);
        }
    };
    private static final StringValueResolver<String> _String = wrap(_.F.asString(String.class));
    private static final StringValueResolver<Str> _Str = new StringValueResolver<Str>() {
        @Override
        public Str resolve(String value) {
            return S.str(value);
        }
    };
    private static final StringValueResolver<FastStr> _FastStr = new StringValueResolver<FastStr>() {
        @Override
        public FastStr resolve(String value) {
            return FastStr.of(value);
        }
    };

    private static Map<Class, StringValueResolver> predefined = C.map(
        boolean.class, _boolean,
        Boolean.class, _Boolean,
        char.class, _char,
        Character.class, _Char,
        byte.class, _byte,
        Byte.class, _Byte,
        short.class, _short,
        Short.class, _Short,
        int.class, _int,
        Integer.class, _Integer,
        long.class, _long,
        Long.class, _Long,
        float.class, _float,
        Float.class, _Float,
        double.class, _double,
        Double.class, _Double,
        String.class, _String,
        Str.class, _Str,
        FastStr.class, _FastStr
    );

    public static <T> void addPredefinedResolver(Class<T> type, StringValueResolver<T> resolver) {
        predefined.put(type, resolver);
    }

    @SuppressWarnings("unchecked")
    public static <T> StringValueResolver<T> predefined(Class<T> type) {
        return predefined.get(type);
    }
}
