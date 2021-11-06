package eu.pb4.polymer.impl.networking;

import eu.pb4.polymer.api.utils.PolymerUtils;
import eu.pb4.polymer.impl.other.ImplPolymerRegistry;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.stream.IntStream;

@ApiStatus.Internal
public class ServerPackets {
    public static final ImplPolymerRegistry<int[]> REGISTRY = new ImplPolymerRegistry<>();
    public static final String HANDSHAKE = "handshake";
    public static final Identifier HANDSHAKE_ID = PolymerUtils.id(HANDSHAKE);
    public static final String SYNC_STARTED = "sync/started";
    public static final Identifier SYNC_STARTED_ID = PolymerUtils.id(SYNC_STARTED);
    public static final String SYNC_FINISHED = "sync/finished";
    public static final Identifier SYNC_FINISHED_ID = PolymerUtils.id(SYNC_FINISHED);
    public static final String SYNC_BLOCK = "sync/blocks";
    public static final Identifier SYNC_BLOCK_ID = PolymerUtils.id(SYNC_BLOCK);
    public static final String SYNC_ITEM = "sync/items";
    public static final Identifier SYNC_ITEM_ID = PolymerUtils.id(SYNC_ITEM);
    public static final String SYNC_ENTITY = "sync/entities";
    public static final Identifier SYNC_ENTITY_ID = PolymerUtils.id(SYNC_ENTITY);
    public static final String SYNC_ITEM_GROUPS = "sync/item_groups";
    public static final Identifier SYNC_ITEM_GROUP_ID = PolymerUtils.id(SYNC_ITEM_GROUPS);
    public static final String SYNC_ITEM_GROUP_REMOVE = "sync/item_group/remove";
    public static final Identifier SYNC_ITEM_GROUP_REMOVE_ID = PolymerUtils.id(SYNC_ITEM_GROUP_REMOVE);
    public static final String SYNC_ITEM_GROUP_VANILLA = "sync/item_group/vanilla";
    public static final Identifier SYNC_ITEM_GROUP_VANILLA_ID = PolymerUtils.id(SYNC_ITEM_GROUP_VANILLA);
    public static final String SYNC_ITEM_GROUP_CLEAR = "sync/item_group/clear";
    public static final Identifier SYNC_ITEM_GROUP_CLEAR_ID = PolymerUtils.id(SYNC_ITEM_GROUP_CLEAR);
    public static final String SYNC_BLOCKSTATE = "sync/blockstate";
    public static final Identifier SYNC_BLOCKSTATE_ID = PolymerUtils.id(SYNC_BLOCKSTATE);
    public static final String SYNC_REBUILD_SEARCH = "sync/rebuild_search";
    public static final Identifier SYNC_REBUILD_SEARCH_ID = PolymerUtils.id(SYNC_REBUILD_SEARCH);
    public static final String SYNC_CLEAR = "sync/clear_all";
    public static final Identifier SYNC_CLEAR_ID = PolymerUtils.id(SYNC_CLEAR);
    public static final String WORLD_SET_BLOCK_UPDATE = "world/set_block";
    public static final Identifier WORLD_SET_BLOCK_UPDATE_ID = PolymerUtils.id(WORLD_SET_BLOCK_UPDATE);
    public static final String WORLD_CHUNK_SECTION_UPDATE = "world/section";
    public static final Identifier WORLD_CHUNK_SECTION_UPDATE_ID = PolymerUtils.id(WORLD_CHUNK_SECTION_UPDATE);
    public static final String WORLD_ENTITY = "world/entity";
    public static final Identifier WORLD_ENTITY_ID = PolymerUtils.id(WORLD_ENTITY);


    public static final int getBestSupported(Identifier identifier, int[] ver) {

        var values = REGISTRY.get(identifier);

        if (values != null) {
            var verSet = new IntArraySet(ver);

            var value = IntStream.of(values).filter((i) -> verSet.contains(i)).max();

            return value.isPresent() ? value.getAsInt() : -1;
        }

        return -1;
    }

    public static final void register(Identifier id, int... ver) {
        REGISTRY.set(id, ver);
    }


    static {
        register(HANDSHAKE_ID, 0);
        register(SYNC_STARTED_ID, 0);
        register(SYNC_FINISHED_ID, 0);
        register(SYNC_BLOCK_ID, 0);
        register(SYNC_BLOCKSTATE_ID, 0);
        register(SYNC_ITEM_ID, 0);
        register(SYNC_ITEM_GROUP_ID, 0);
        register(SYNC_ITEM_GROUP_CLEAR_ID, 0);
        register(SYNC_ITEM_GROUP_REMOVE_ID, 0);
        register(SYNC_ITEM_GROUP_VANILLA_ID, 0);
        register(SYNC_ENTITY_ID, 0);
        register(SYNC_CLEAR_ID, 0);
        register(WORLD_SET_BLOCK_UPDATE_ID, 0);
        register(WORLD_CHUNK_SECTION_UPDATE_ID, 0);
        register(WORLD_ENTITY_ID, 0);
    }
}