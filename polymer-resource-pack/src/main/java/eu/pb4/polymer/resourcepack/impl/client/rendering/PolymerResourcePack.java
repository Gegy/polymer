package eu.pb4.polymer.resourcepack.impl.client.rendering;

import eu.pb4.polymer.common.impl.client.ClientUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.*;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public class PolymerResourcePack  {
    public static boolean generated = false;

    @Nullable
    public static ResourcePackProfile.PackFactory setup() {
        Path outputPath = PolymerResourcePackUtils.getMainPath();
        if ((outputPath.toFile().exists() && generated) || PolymerResourcePackUtils.buildMain(outputPath)) {
            generated = true;
            return new ZipResourcePack.ZipBackedFactory(outputPath.toFile());
        } else {
            return null;
        }
    }

    public static class Provider implements ResourcePackProvider {
        @Override
        public void register(Consumer<ResourcePackProfile> profileAdder) {
            if (PolymerResourcePackUtils.hasResources()) {
                var pack = PolymerResourcePack.setup();

                if (pack != null) {
                    profileAdder.accept(ResourcePackProfile.create(
                            new ResourcePackInfo(ClientUtils.PACK_ID,
                            Text.translatable("text.polymer.resource_pack.name"), ResourcePackSource.BUILTIN, Optional.empty()),
                            pack,
                            ResourceType.CLIENT_RESOURCES,
                            new ResourcePackPosition(PolymerResourcePackUtils.isRequired(), ResourcePackProfile.InsertionPosition.TOP, false)
                    ));
                }
            }
        }
    }
}
