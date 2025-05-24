package baguchi.bagus_lib.register;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.client.dialog.DialogType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class DialogRegister {
    public static final ResourceKey<Registry<DialogType>> REGISTRY_KEY = ResourceKey
            .createRegistryKey(ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "dialog"));
    public static ResourceKey<DialogType> key(ResourceLocation name) {
        return ResourceKey.create(REGISTRY_KEY, name);
    }

}
