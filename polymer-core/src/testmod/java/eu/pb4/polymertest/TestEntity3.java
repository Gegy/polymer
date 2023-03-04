package eu.pb4.polymertest;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.core.api.utils.PolymerUtils;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.VirtualEntityUtils;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.api.elements.InteractionElement;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import eu.pb4.polymer.virtualentity.api.tracker.EntityTrackedData;
import eu.pb4.polymertest.mixin.EntityAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Matrix4x3fStack;

import java.util.List;
import java.util.function.Consumer;

public class TestEntity3 extends CreeperEntity implements PolymerEntity {
    private final ElementHolder holder;
    private final EntityAttachment attachment;
    private final ItemDisplayElement leftLeg = new ItemDisplayElement(Items.RED_WOOL);
    private final ItemDisplayElement rightLeg = new ItemDisplayElement(Items.RED_WOOL);
    private final ItemDisplayElement torso = new ItemDisplayElement(PolymerUtils.createPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjYyYzQ4NWIxODg2ZGJjZTZjMWNhZDE0MGMwZWY4NzYzNTU5ZDQzYTc4NTY0NDY2NGM2ZDVmMzZlMjc1NGVlOCJ9fX0="));
    private final InteractionElement interaction = InteractionElement.redirect(this);
    private Matrix4x3fStack stack = new Matrix4x3fStack(8);
    private float previousSpeed = Float.MIN_NORMAL;
    private float previousLimbPos = Float.MIN_NORMAL;

    public TestEntity3(EntityType<TestEntity3> entityEntityType, World world) {
        super(entityEntityType, world);
        this.holder = new ElementHolder() {
            @Override
            protected void startWatchingExtraPackets(ServerPlayNetworkHandler player, Consumer<Packet<ClientPlayPacketListener>> packetConsumer) {
                packetConsumer.accept(VirtualEntityUtils.createRidePacket(TestEntity3.this.getId(), this.getEntityIds()));
            }

            @Override
            protected void updatePosition() {
                this.currentPos = this.getAttachment().getPos();
            }

            @Override
            public Vec3d getPos() {
                return this.getAttachment().getPos();
            }
        };

        this.holder.addElement(interaction);
        this.holder.addElement(leftLeg);
        this.holder.addElement(rightLeg);
        this.holder.addElement(torso);
        leftLeg.setInterpolationDuration(2);
        rightLeg.setInterpolationDuration(2);
        torso.setInterpolationDuration(2);
        this.updateAnimation();
        this.attachment = new EntityAttachment(this.holder, this, false);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.world.getTime() % 2 == 1) {
            return;
        }

        this.updateAnimation();

        this.holder.tick();
    }

    private void updateAnimation() {
        var speed = this.limbAnimator.getSpeed();
        var limbPos = this.limbAnimator.getPos();

        if (speed == this.previousSpeed && limbPos == this.previousLimbPos) {
            return;
        }

        this.previousSpeed = speed;
        this.previousLimbPos = limbPos;

        this.leftLeg.setInterpolationStart(this.world.getTime());
        this.rightLeg.setInterpolationStart(this.world.getTime());
        this.torso.setInterpolationStart(this.world.getTime());

        stack.clear();

        stack.rotateY((float) Math.toRadians(180.0F * 3 - MathHelper.lerpAngleDegrees(0.5f, this.prevBodyYaw, this.bodyYaw)) + (float) (0.00001f * Math.random()));

        stack.pushMatrix();

        stack.translate(0, 0.5f, 0);
        torso.setTransformation(stack);

        stack.popMatrix();

        stack.pushMatrix();
        stack.translate(0.15f, 0.4f, 0).rotateX(MathHelper.cos(limbPos * 0.6662F) * 1.4F * speed).translate(0, -0.125f, 0).scale(0.5f, 0.8f, 0.5f);
        leftLeg.setTransformation(stack);
        stack.popMatrix();

        stack.pushMatrix();
        stack.translate(-0.15f, 0.4f, 0).rotateX(MathHelper.cos(limbPos * 0.6662F + 3.1415927F) * 1.4F * speed).translate(0, -0.125f, 0).scale(0.5f, 0.8f, 0.5f);
        rightLeg.setTransformation(stack);
        stack.popMatrix();
    }

    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
        return EntityType.ARMOR_STAND;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
    
    @Override
    public void modifyRawTrackedData(List<DataTracker.SerializedEntry<?>> data, ServerPlayerEntity player, boolean initial) {
        data.add(DataTracker.SerializedEntry.of(EntityTrackedData.FLAGS, (byte) (1 << EntityTrackedData.INVISIBLE_FLAG_INDEX)));
        data.add(new DataTracker.SerializedEntry(EntityAccessor.getNO_GRAVITY().getId(), EntityAccessor.getNO_GRAVITY().getType(), true));
        data.add(DataTracker.SerializedEntry.of(ArmorStandEntity.ARMOR_STAND_FLAGS, (byte) (ArmorStandEntity.MARKER_FLAG | ArmorStandEntity.SMALL_FLAG)));
    }

}
