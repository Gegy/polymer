package eu.pb4.polymer.core.api.other;

import eu.pb4.polymer.core.api.utils.PolymerObject;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public final class PolymerStat extends Identifier implements PolymerObject {
    private static final Map<Identifier, Text> NAMES = new HashMap<>();

    private PolymerStat(String namespace, String path) {
        super(namespace, path);
    }

    /**
     * Register a custom server-compatible statistic.
     * Registering a {@link net.minecraft.stat.Stat} in the vanilla way will cause clients to disconnect when opening the statistics screen.
     *
     * @param id        the Identifier for the stat
     * @param formatter the formatter for the stat to use
     * @return the PolymerStat ({@link Identifier}) for the custom stat
     */
    public static Identifier registerStat(String id, StatFormatter formatter) {
        return registerStat(id, Text.translatable("stat." + id.replace(':', '.')), formatter);
    }

    /**
     * Register a custom server-compatible statistic.
     * Registering a {@link net.minecraft.stat.Stat} in the vanilla way will cause clients to disconnect when opening the statistics screen.
     *
     * @param id        the Identifier for the stat
     * @param name      the name used in /polymer stats
     * @param formatter the formatter for the stat to use
     * @return the PolymerStat ({@link Identifier}) for the custom stat
     */
    public static Identifier registerStat(String id, Text name, StatFormatter formatter) {
        var idx = Identifier.of(id);
        PolymerStat identifier = new PolymerStat(idx.getNamespace(), idx.getPath());
        Registry.register(Registries.CUSTOM_STAT, id, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        NAMES.put(identifier, name);
        return identifier;
    }

    /**
     * Register a custom server-compatible statistic.
     * Registering a {@link net.minecraft.stat.Stat} in the vanilla way will cause clients to disconnect when opening the statistics screen.
     *
     * @param id        the Identifier for the stat
     * @param formatter the formatter for the stat to use
     * @return the PolymerStat ({@link Identifier}) for the custom stat
     */
    public static Identifier registerStat(Identifier id, StatFormatter formatter) {
        return registerStat(id.toString(), formatter);
    }

    /**
     * Register a custom server-compatible statistic.
     * Registering a {@link net.minecraft.stat.Stat} in the vanilla way will cause clients to disconnect when opening the statistics screen.
     *
     * @param id        the Identifier for the stat
     * @param name      the name used in /polymer stats
     * @param formatter the formatter for the stat to use
     * @return the PolymerStat ({@link Identifier}) for the custom stat
     */
    public static Identifier registerStat(Identifier id, Text name, StatFormatter formatter) {
        return registerStat(id.toString(), name, formatter);
    }


    public static Text getName(Identifier identifier) {
        return NAMES.getOrDefault(identifier, Text.empty());
    }
}
