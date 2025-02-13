package eu.pb4.polymer.core.api.other;

import eu.pb4.polymer.core.api.utils.PolymerSyncedObject;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

public interface PolymerPotion extends PolymerSyncedObject<Potion> {
    @Override
    @Nullable
    default Potion getPolymerReplacement(ServerPlayerEntity player) {
        return null;
    }
}
