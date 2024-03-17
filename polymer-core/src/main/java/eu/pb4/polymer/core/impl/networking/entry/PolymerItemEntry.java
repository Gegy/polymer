package eu.pb4.polymer.core.impl.networking.entry;

import eu.pb4.polymer.core.impl.PolymerImplUtils;
import eu.pb4.polymer.core.mixin.item.MiningToolItemAccessor;
import eu.pb4.polymer.networking.api.ContextByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

// todo
@ApiStatus.Internal
public record PolymerItemEntry(int numId, Identifier identifier, ItemStack representation, int foodLevels,
                               float saturation, Identifier miningTool, int miningLevel,
                               int stackSize) {
    public static final Identifier NOT_TOOL = PolymerImplUtils.id("not_tool");
    public static final PacketCodec<ContextByteBuf, PolymerItemEntry> CODEC = PacketCodec.of(PolymerItemEntry::write, PolymerItemEntry::read);

    public static PolymerItemEntry of(Item item, ServerPlayNetworkHandler handler, int version) {
        var toolItem = item instanceof MiningToolItem x ? x : null;
        var food = item.getFoodComponent();

        return new PolymerItemEntry(Item.getRawId(item), Registries.ITEM.getId(item), item.getDefaultStack(), food != null ? food.getHunger() : 0, food != null ? food.getSaturationModifier() : 0, toolItem != null ? ((MiningToolItemAccessor) toolItem).getEffectiveBlocks().id() : NOT_TOOL, toolItem != null ? toolItem.getMaterial().getMiningLevel() : 0, item.getMaxCount());
    }

    public static PolymerItemEntry read(ContextByteBuf buf) {
        return new PolymerItemEntry(buf.readVarInt(), buf.readIdentifier(), ItemStack.OPTIONAL_PACKET_CODEC.decode(buf), buf.readVarInt(), buf.readFloat(), buf.readIdentifier(), buf.readVarInt(), buf.readVarInt());
    }

    public void write(ContextByteBuf buf) {
        buf.writeVarInt(this.numId);

        buf.writeIdentifier(this.identifier);
        ItemStack.OPTIONAL_PACKET_CODEC.encode(buf, this.representation);

        buf.writeVarInt(this.foodLevels);
        buf.writeFloat(this.saturation);
        buf.writeIdentifier(this.miningTool);
        buf.writeVarInt(this.miningLevel);

        buf.writeVarInt(this.stackSize);

    }
}
