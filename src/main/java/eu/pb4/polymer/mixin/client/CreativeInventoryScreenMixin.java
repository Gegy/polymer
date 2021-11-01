package eu.pb4.polymer.mixin.client;

import eu.pb4.polymer.api.client.registry.ClientPolymerItem;
import eu.pb4.polymer.api.item.PolymerItemUtils;
import eu.pb4.polymer.impl.client.InternalClientItemGroup;
import eu.pb4.polymer.impl.client.InternalClientRegistry;
import eu.pb4.polymer.impl.interfaces.ClientItemGroupExtension;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> {

    @Shadow private TextFieldWidget searchBox;

    public CreativeInventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @ModifyVariable(method = "renderTooltip", at = @At("STORE"), ordinal = 0)
    private ItemGroup polymer_replaceItemGroup(ItemGroup oldGroup, MatrixStack matrices, ItemStack stack) {
        var id = PolymerItemUtils.getPolymerIdentifier(stack);

        if (id != null) {
            var item = ClientPolymerItem.REGISTRY.get(id);

            if (item != null) {
                var groupId = Identifier.tryParse(item.itemGroup());
                if (groupId != null) {
                    var group = InternalClientRegistry.ITEM_GROUPS.get(groupId);

                    if (group != null) {
                        return group;
                    }
                }

                return InternalClientRegistry.VANILLA_ITEM_GROUPS.get(item.itemGroup());
            }
        }
        return oldGroup;
    }

    @Inject(method = "search", at = @At("TAIL"))
    private void polymer_hideServerPolymerItems(CallbackInfo ci) {
        this.handler.itemList.removeIf((i) -> PolymerItemUtils.isPolymerServerItem(i));

        if (this.searchBox.getText().isEmpty()) {
            for (var group : ItemGroup.GROUPS) {
                if (group == ItemGroup.SEARCH) {
                    continue;
                }

                Collection<ItemStack> stacks;

                if (group instanceof InternalClientItemGroup clientItemGroup) {
                    stacks = clientItemGroup.getStacks();
                } else {
                    stacks = ((ClientItemGroupExtension) group).polymer_getStacks();
                }

                if (stacks != null) {
                    for (var stack : stacks) {
                        this.handler.itemList.add(stack);
                    }
                }
            }
        }
    }
}
