package com.huashanlunjian.script.inventory.container.backpack;

import com.huashanlunjian.script.api.backpack.IMaidBackpack;
import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.item.BackpackLevel;
import com.huashanlunjian.script.script;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class EmptyBackpack extends IMaidBackpack {
    public static final ResourceLocation ID = new ResourceLocation(script.MOD_ID, "empty");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Item getItem() {
        return Items.AIR;
    }

    @Override
    public void onPutOn(ItemStack stack, PlayerEntity player, AnEntity maid) {
    }

    @Override
    public void onTakeOff(ItemStack stack, PlayerEntity player, AnEntity maid) {
    }

    //@Override
    //public void onSpawnTombstone(EntityMaid maid, EntityTombstone tombstone) {
    //}

    @Override
    public INamedContainerProvider getGuiProvider(int entityId) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return ITextComponent.getTextComponentOrEmpty("Maid Empty Container");
            }

            @Override
            public Container createMenu(int index, PlayerInventory playerInventory, PlayerEntity player) {
                return new EmptyBackpackContainer(index, playerInventory, entityId);
            }
        };
    }


    @Override
    public int getAvailableMaxContainerIndex() {
        return BackpackLevel.EMPTY_CAPACITY;
    }

    //@Nullable
    //@Override
    //@OnlyIn(Dist.CLIENT)
    //public EntityModel<AnEntity> getBackpackModel(EntityModelSet modelSet) {
    //    return null;
    //}

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getBackpackTexture() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public void offsetBackpackItem(MatrixStack poseStack) {
        poseStack.translate(0, 0.625, 0.2);
    }

    @Nullable
    @Override
    public EntityModel<AnEntity> getBackpackModel(ModelManager modelSet) {
        return null;
    }
}

