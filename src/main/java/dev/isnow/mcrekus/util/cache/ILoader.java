package dev.isnow.mcrekus.util.cache;

public interface ILoader<K, V> {
    V load(K key);
}
