package dev.isnow.mcrekus.util.cache;

public interface ISaver<T, Object> {
    void save(T key, Object value);
}
