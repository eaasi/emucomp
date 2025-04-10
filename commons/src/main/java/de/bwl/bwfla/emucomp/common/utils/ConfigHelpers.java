package de.bwl.bwfla.emucomp.common.utils;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.config.spi.Converter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConfigHelpers {

	public static <T> T configure(T object, Config config) {
		Class<?> clazz = object.getClass();

		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(ConfigProperty.class)) {
				ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
				String key = annotation.name();
				String defaultValue = annotation.defaultValue();
				field.setAccessible(true);

				try {
					String value = config.getOptionalValue(key, String.class).orElse(defaultValue);

					Object convertedValue = convertValue(value, field.getType());
					field.set(object, convertedValue);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Failed to inject config value into " + field.getName(), e);
				}
			}
		}

		return object;
	}

	public static String anonymize(String value, char replacement) {
		return anonymize(value, replacement, 0, 0, -1);
	}

	public static String anonymize(String value, char replacement, int prelen, int suflen, int maxrlen) {
		if (prelen < 0 || suflen < 0) {
			throw new IllegalArgumentException("Invalid prefix or suffix length");
		}

		if (maxrlen < 0) maxrlen = Integer.MAX_VALUE;
		int length = value.length();
		final StringBuilder sb = new StringBuilder(length);

		if (prelen > 0) {
			prelen = Math.min(prelen, length);
			sb.append(value, 0, prelen);
			length -= prelen;
		}

		if (suflen > length) suflen = length;
		length -= suflen;

		while (length > 0 && maxrlen > 0) {
			sb.append(replacement);
			length--;
			maxrlen--;
		}

		if (maxrlen == 0) sb.append("...");
		if (suflen > 0) sb.append(value.substring(value.length() - suflen));

		return sb.toString();
	}

	public static String toListKey(String key, int index) {
		return key + "[" + index + "]";
	}

	public static String toListKey(String key, int index, String suffix)
	{
		return (ConfigHelpers.toListKey(key, index) + suffix);
	}

	public static List<String> getAsList(Config config, String key) {
		List<String> entries = new ArrayList<>();
		int index = 0;
		while (config.getOptionalValue(toListKey(key, index), String.class).isPresent()) {
			entries.add(config.getValue(toListKey(key, index), String.class));
			index++;
		}
		return entries;
	}

	public static Config filter(Config config, String prefix) {
		return new Config() {
			@Override
			public <T> T getValue(String name, Class<T> type) {
				return config.getValue(prefix + name, type);
			}

			@Override
			public ConfigValue getConfigValue(String propertyName) {
				return config.getValue(prefix + propertyName, ConfigValue.class);
			}

			@Override
			public <T> java.util.Optional<T> getOptionalValue(String name, Class<T> type) {
				return config.getOptionalValue(prefix + name, type);
			}

			@Override
			public Iterable<String> getPropertyNames() {
				return config.getPropertyNames();
			}

			@Override
			public Iterable<org.eclipse.microprofile.config.spi.ConfigSource> getConfigSources() {
				return config.getConfigSources();
			}

			@Override
			public <T> Optional<Converter<T>> getConverter(Class<T> forType) {
				return Optional.empty();
			}

			@Override
			public <T> T unwrap(Class<T> type) {
				return null;
			}
		};
	}

	public static boolean isEmpty(Config config) {
		return !config.getPropertyNames().iterator().hasNext();
	}

	public static void check(Object arg, String message) {
		if (arg == null) throw new IllegalArgumentException(message);
	}

	public static void check(String arg, String message) {
		if (arg == null || arg.isEmpty()) throw new IllegalArgumentException(message);
	}

	public static void check(int arg, int min, int max, String message) {
		if (arg < min || arg > max) {
			throw new IllegalArgumentException(message + " Current: " + arg + ", Expected: [" + min + ", " + max + "]");
		}
	}

	private static Object convertValue(String value, Class<?> targetType) {
		if (targetType == String.class) return value;
		if (targetType == int.class || targetType == Integer.class) return Integer.parseInt(value);
		if (targetType == long.class || targetType == Long.class) return Long.parseLong(value);
		if (targetType == double.class || targetType == Double.class) return Double.parseDouble(value);
		if (targetType == boolean.class || targetType == Boolean.class) return Boolean.parseBoolean(value);

		throw new IllegalArgumentException("Unsupported config field type: " + targetType.getName());
	}
}
