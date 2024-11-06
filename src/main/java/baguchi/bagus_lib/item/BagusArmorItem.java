package baguchi.bagus_lib.item;

import baguchi.bagus_lib.client.ModModelLayers;
import baguchi.bagus_lib.client.render.MiniBaguArmorModel;
import baguchi.bagus_lib.client.render.MiniBaguRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentModel;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class BagusArmorItem extends ArmorItem {
    public BagusArmorItem(Properties p_40388_) {
        super(ArmorMaterials.LEATHER, ArmorType.HELMET, p_40388_);
    }

    public static final class ArmorRender implements IClientItemExtensions {
        public static final ArmorRender INSTANCE = new ArmorRender();

        @Override
        public Model getGenericArmorModel(ItemStack itemStack, EquipmentModel.LayerType layerType, Model original) {
            EntityModelSet models = Minecraft.getInstance().getEntityModels();
            ModelPart root = models.bakeLayer(ModModelLayers.MINI_BAGU_ARMOR);

            MiniBaguArmorModel model2 = new MiniBaguArmorModel(root);
            return model2;
        }
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, EquipmentModel.LayerType type, EquipmentModel.Layer layer, ResourceLocation _default) {
        return MiniBaguRenderer.TEXTURE;
    }
}
