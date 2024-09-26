package dev.isnow.mcrekus.module;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.util.ReflectionUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import lombok.Getter;

@Getter
public class ModuleAccessor<T extends Module> {
    private final T module;

    @SuppressWarnings("unchecked")
    public ModuleAccessor() {
        final Class<T> clazz = ReflectionUtil.getGenericTypeClass(this, 0);

        if(clazz == null) {
            RekusLogger.error("Failed to get generic type class");
            module = null;
            return;
        }

        module = (T) MCRekus.getInstance().getModuleManager().getModuleByClass(clazz);
    }

    @SuppressWarnings("unchecked")
    public ModuleAccessor(final Class<T> moduleClass) {
        this.module = (T) MCRekus.getInstance().getModuleManager().getModuleByClass(moduleClass);
    }
}
