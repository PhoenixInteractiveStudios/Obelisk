package org.burrow_studios.obelisk.commons.yaml;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public final class YamlPrimitive extends YamlElement {
    private final Object value;

    public YamlPrimitive(@NotNull Boolean value) {
        this.value = value;
    }

    public YamlPrimitive(@NotNull Number value) {
        this.value = value;
    }

    public YamlPrimitive(@NotNull String value) {
        this.value = value;
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    public boolean getAsBoolean() {
        if (isBoolean())
            return (Boolean) value;
        return Boolean.parseBoolean(getAsString());
    }

    public boolean isNumber() {
        return value instanceof Number;
    }

    public @NotNull Number getAsNumber() {
        if (isNumber())
            return (Number) value;
        throw new YamlFormatException("Not a number: " + value.getClass().getName());
    }

    public byte getAsByte() {
        return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
    }

    public short getAsShort() {
        return isNumber() ? getAsNumber().shortValue() : Short.parseShort(getAsString());
    }

    public int getAsInt() {
        return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
    }

    public float getAsFloat() {
        return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
    }

    public long getAsLong() {
        return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
    }

    public double getAsDouble() {
        return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
    }

    public @NotNull BigInteger getAsBigInteger() {
        return value instanceof BigInteger ? (BigInteger) value : new BigInteger(getAsString());
    }

    public @NotNull BigDecimal getAsBigDecimal() {
        return value instanceof BigDecimal ? (BigDecimal) value : new BigDecimal(getAsString());
    }

    public boolean isString() {
        return value instanceof String;
    }

    public @NotNull String getAsString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof YamlPrimitive yaml)) return false;
        return Objects.equals(this.value, yaml.value);
    }

    @Override
    Object toObject() {
        return value;
    }
}
