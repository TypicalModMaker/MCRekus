package dev.isnow.mcrekus.util.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.util.RekusLogger;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class RekusCache<K, V> {
    private final LoadingCache<K, V> cache;

    @SuppressWarnings("all")
    // Unchecked cast & 'while' statement cannot complete without throwing an exception supress false positive
    public RekusCache(final String name, final ILoader<K, V> loader, final ISaver<K, V> saver, final IRemover<V> remover) {
        cache = Caffeine.newBuilder()
                .expireAfterAccess(300, TimeUnit.SECONDS)
                .removalListener((key, value, cause) -> {
                    if (value != null && !MCRekus.getInstance().isShuttingDown()) {
                        if (cause == RemovalCause.EXPIRED || cause == RemovalCause.SIZE) {
                            saver.save((K) key, (V) value);
                        } else if (cause == RemovalCause.EXPLICIT && remover != null) {
                            remover.remove((V) value);
                        }
                    }
                })
                .initialCapacity(MCRekus.getInstance().getConfigManager().getGeneralConfig().getCacheSizeLimit())
                .build(loader::load);

        MCRekus.getInstance().getThreadPool().execute(() -> {
            while (true) {
                try {
                    cache.cleanUp();

                    TimeUnit.SECONDS.sleep(30);
                } catch (Exception e) {
                    if (MCRekus.getInstance().isShuttingDown()) {
                        return;
                    }
                    RekusLogger.warn("Failed to clean the cache: " + e + ", Cache type: " + name);
                    continue;
                }
            }
        });
    }

    public V get(final K key) {
        return get(key, true);
    }

    public V get(final K key, final boolean runLoader) {
        if (runLoader) {
            return cache.get(key);
        } else {
            return cache.getIfPresent(key);
        }
    }

    public void remove(final K key) {
        cache.invalidate(key);
    }

    public void preload(final K key) {
        cache.get(key);
    }

    public boolean contains(final K key) {
        return cache.getIfPresent(key) != null;
    }

    public void put(final K key, final V value) {
        cache.put(key, value);
    }

    public Collection<V> getAll() {
        return cache.asMap().values();
    }

    public Collection<K> getKeys() {
        return cache.asMap().keySet();
    }
}
