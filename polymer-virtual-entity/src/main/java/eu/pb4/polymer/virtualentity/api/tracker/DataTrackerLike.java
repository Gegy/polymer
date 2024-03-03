package eu.pb4.polymer.virtualentity.api.tracker;

import eu.pb4.polymer.common.mixin.DataTrackerAccessor;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public interface DataTrackerLike {
    @Nullable
    <T> T get(TrackedData<T> data);

    @Nullable

    default <T> void set(TrackedData<T> key, T value) {
        set(key, value, false);
    }

    <T> void set(TrackedData<T> key, T value, boolean forceDirty);

    <T> void setDirty(TrackedData<T> key, boolean isDirty);

    boolean isDirty();

    boolean isDirty(TrackedData<?> key);

    @Nullable
    List<DataTracker.SerializedEntry<?>> getDirtyEntries();

    @Nullable
    List<DataTracker.SerializedEntry<?>> getChangedEntries();

    static DataTrackerLike wrap(DataTracker dataTracker) {
        return new DataTrackerLike() {
            @Override
            public <T> @Nullable T get(TrackedData<T> data) {
                return dataTracker.get(data);
            }

            @Override
            public <T> void set(TrackedData<T> key, T value, boolean forceDirty) {
                dataTracker.set(key, value, forceDirty);
            }

            @Override
            public <T> void setDirty(TrackedData<T> key, boolean isDirty) {
                dataTracker.set(key, dataTracker.get(key), isDirty);
            }

            @Override
            public boolean isDirty() {
                return dataTracker.isDirty();
            }

            @Override
            public boolean isDirty(TrackedData<?> key) {
                return ((DataTrackerAccessor) dataTracker).getEntries()[key.id()].isDirty();
            }

            @Override
            public @Nullable List<DataTracker.SerializedEntry<?>> getDirtyEntries() {
                return dataTracker.getDirtyEntries();
            }

            @Override
            public @Nullable List<DataTracker.SerializedEntry<?>> getChangedEntries() {
                return dataTracker.getChangedEntries();
            }
        };
    }
}