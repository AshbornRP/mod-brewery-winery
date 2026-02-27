package net.ruusika.brewerywinery.blocks.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.ruusika.brewerywinery.init.BreweryWineryBlockEntities;
import net.ruusika.brewerywinery.util.NbtKeys;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class ServingTrayBlockEntity extends BlockEntity {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
    private final HashMap<Integer, Float> stackRotation = new HashMap<>();
    private final HashMap<Integer, Vec3d> stackPosition = new HashMap<>();

    public ServingTrayBlockEntity( BlockPos pos, BlockState state) {
        super(BreweryWineryBlockEntities.SERVING_TRAY, pos, state);
    }

    public DefaultedList<ItemStack> getInventory() {
        return inventory;
    }

    @Nullable
    public Float getStackRotation(int index) {
        return stackRotation.get(index);
    }

    @Nullable
    public Vec3d getStackPosition(int index) {
        return stackPosition.get(index);
    }

    @SuppressWarnings("unused")
    public boolean isFull() {
        boolean isFull = true;
        for (ItemStack entry : inventory) {
            if (entry.isEmpty()) {
                isFull = false;
                break;
            }
        }
        return isFull;
    }

    public boolean addToTray(ItemStack stack) {
        boolean changed = false;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack entry = inventory.get(i);
            if (entry.isEmpty()) {
                inventory.set(i, stack.copy());
                if (world != null){
                    stackRotation.put(i, world.getRandom().nextFloat()*360);
                    stackPosition.put(i, new Vec3d(world.getRandom().nextDouble(), 0 , world.getRandom().nextDouble()));
                }
                changed = true;
                break;
            }
        }
        if (changed) {
            markDirty();
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                serverWorld.getChunkManager().markForUpdate(this.getPos());
            }
        }

        return changed;
    }

    @Nullable
    public ItemStack removeFromTray() {
        for (int i = inventory.size()-1; i >= 0; i--) {
            ItemStack entry = inventory.get(i);
            if (!entry.isEmpty()) {
                ItemStack removedStack = entry.copy();
                stackRotation.remove(i);
                stackPosition.remove(i);
                inventory.set(i, ItemStack.EMPTY);
                markDirty();
                if (this.getWorld() instanceof ServerWorld serverWorld) {
                    serverWorld.getChunkManager().markForUpdate(this.getPos());
                }
                return removedStack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);

        NbtCompound stackRotationsNbt = nbt.getCompound(NbtKeys.ROTATION);
        for (String nbtKey : stackRotationsNbt.getKeys()) {
            stackRotation.put(Integer.valueOf(nbtKey),stackRotationsNbt.getFloat(nbtKey));
        }

        NbtCompound stackPositionsNbt = nbt.getCompound(NbtKeys.POSITION);
        for (String nbtKey : stackPositionsNbt.getKeys()) {
            NbtCompound coordinatesNbt = stackPositionsNbt.getCompound(nbtKey);
            Vec3d coordinate = new Vec3d(coordinatesNbt.getDouble("x"), 0 , coordinatesNbt.getDouble("z"));
            stackPosition.put(Integer.valueOf(nbtKey), coordinate);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);

        NbtCompound stackRotationsNbt = new NbtCompound();
        stackRotation.forEach((key, value) -> stackRotationsNbt.putFloat(String.valueOf(key), value));
        nbt.put(NbtKeys.ROTATION, stackRotationsNbt);

        NbtCompound stackPositionsNbt = new NbtCompound();
        stackPosition.forEach((key, value) -> {
            NbtCompound coordinatesNbt = new NbtCompound();
            coordinatesNbt.putDouble("x", value.getX());
            coordinatesNbt.putDouble("z", value.getZ());
            stackPositionsNbt.put(String.valueOf(key), coordinatesNbt);
        });
        nbt.put(NbtKeys.POSITION, stackPositionsNbt);
    }
}
