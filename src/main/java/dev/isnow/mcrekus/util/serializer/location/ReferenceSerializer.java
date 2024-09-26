package dev.isnow.mcrekus.util.serializer.location;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class ReferenceSerializer extends TypeAdapter<Reference<World>> {
    @Override
    public void write(JsonWriter jsonWriter, Reference<World> worldWeakReference) throws IOException {
        if(worldWeakReference == null) {
            jsonWriter.nullValue();
            return;
        }

        jsonWriter.value(worldWeakReference.get().getName());
    }

    @Override
    public Reference<World> read(JsonReader jsonReader) throws IOException {
        return new WeakReference<>(Bukkit.getWorld(jsonReader.nextString()));
    }
}
