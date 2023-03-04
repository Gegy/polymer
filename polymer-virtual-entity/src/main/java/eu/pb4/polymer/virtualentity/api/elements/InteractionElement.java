package eu.pb4.polymer.virtualentity.api.elements;

import eu.pb4.polymer.virtualentity.api.tracker.InteractionTrackedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class InteractionElement extends GenericEntityElement {
    private InteractionHandler handler = InteractionHandler.EMPTY;

    public InteractionElement() {
    }

    public InteractionElement(InteractionHandler handler) {
        this.setHandler(handler);
    }

    public static InteractionElement redirect(Entity redirectedEntity) {
        return new InteractionElement(new InteractionHandler() {
            @Override
            public void interact(ServerPlayerEntity player, Hand hand) {
                player.networkHandler.onPlayerInteractEntity(PlayerInteractEntityC2SPacket.interact(redirectedEntity, player.isSneaking(), hand));
            }

            @Override
            public void interactAt(ServerPlayerEntity player, Hand hand, Vec3d pos) {
                player.networkHandler.onPlayerInteractEntity(PlayerInteractEntityC2SPacket.interactAt(redirectedEntity, player.isSneaking(), hand, pos));
            }

            @Override
            public void attack(ServerPlayerEntity player) {
                player.networkHandler.onPlayerInteractEntity(PlayerInteractEntityC2SPacket.attack(redirectedEntity, player.isSneaking()));
            }
        });
    }

    public void setHandler(InteractionHandler handler) {
        this.handler = handler;
    }

    @Override
    public InteractionHandler getInteractionHandler(ServerPlayerEntity player) {
        return this.handler;
    }


    @Override
    protected final EntityType<? extends Entity> getEntityType() {
        return EntityType.INTERACTION;
    }

    private float getWidth() {
        return this.dataTracker.get(InteractionTrackedData.WIDTH);
    }

    private void setWidth(float width) {
        this.dataTracker.set(InteractionTrackedData.WIDTH, width);
    }

    private float getHeight() {
        return this.dataTracker.get(InteractionTrackedData.HEIGHT);
    }

    private void setHeight(float height) {
        this.dataTracker.set(InteractionTrackedData.HEIGHT, height);
    }

    private void setResponse(boolean response) {
        this.dataTracker.set(InteractionTrackedData.RESPONSE, response);
    }

    private boolean shouldRespond() {
        return this.dataTracker.get(InteractionTrackedData.RESPONSE);
    }
}
