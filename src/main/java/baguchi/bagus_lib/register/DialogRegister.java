package baguchi.bagus_lib.register;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.client.dialog.DialogType;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class DialogRegister {
    public static final ResourceKey<Registry<DialogType>> REGISTRY_KEY = ResourceKey
            .createRegistryKey(Identifier.fromNamespaceAndPath(BagusLib.MODID, "dialog"));

    public static ResourceKey<DialogType> key(Identifier name) {
        return ResourceKey.create(REGISTRY_KEY, name);
    }

}
