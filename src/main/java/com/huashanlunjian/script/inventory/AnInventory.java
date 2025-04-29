package com.huashanlunjian.script.inventory;

import com.google.common.collect.ImmutableList;
import com.huashanlunjian.script.entity.AnEntity;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.tags.ITag;
import net.minecraft.util.INameable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.function.Predicate;

public class AnInventory implements IInventory, INameable {
    public final NonNullList<ItemStack> mainInventory = NonNullList.withSize(36, ItemStack.EMPTY);
    public final NonNullList<ItemStack> armorInventory = NonNullList.withSize(4, ItemStack.EMPTY);
    public final NonNullList<ItemStack> offHandInventory = NonNullList.withSize(1, ItemStack.EMPTY);
    private final List<NonNullList<ItemStack>> allInventories = ImmutableList.of(this.mainInventory, this.armorInventory, this.offHandInventory);
    public int currentItem;
    public final AnEntity an;
    private ItemStack itemStack = ItemStack.EMPTY;
    private int timesChanged;

    public AnInventory(AnEntity anIn) {
        an = anIn;
    }
    //public ItemStack getCurrentItem() {
    //    return isHotbar(this.currentItem) ? this.mainInventory.get(this.currentItem) : ItemStack.EMPTY;
    //}
    //public static boolean isHotbar(int index) {
    //    return index >= 0 && index < 9;
    //}
    private boolean canMergeStacks(ItemStack stack1, ItemStack stack2) {
        return !stack1.isEmpty() && this.stackEqualExact(stack1, stack2) && stack1.isStackable() && stack1.getCount() < stack1.getMaxStackSize() && stack1.getCount() < this.getInventoryStackLimit();
    }
    private boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }
    public int getFirstEmptyStack() {
        for(int i = 0; i < this.mainInventory.size(); ++i) {
            if (this.mainInventory.get(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }/*
    @OnlyIn(Dist.CLIENT)
    public void setPickedItemStack(ItemStack stack) {
        int i = this.getSlotFor(stack);
        if (isHotbar(i)) {
            this.currentItem = i;
        } else {
            if (i == -1) {
                this.currentItem = this.getBestHotbarSlot();
                if (!this.mainInventory.get(this.currentItem).isEmpty()) {
                    int j = this.getFirstEmptyStack();
                    if (j != -1) {
                        this.mainInventory.set(j, this.mainInventory.get(this.currentItem));
                    }
                }

                this.mainInventory.set(this.currentItem, stack);
            } else {
                this.pickItem(i);
            }

        }
    }*/
    public void pickItem(int index) {
        this.currentItem = this.getBestHotbarSlot();
        ItemStack itemstack = this.mainInventory.get(this.currentItem);
        this.mainInventory.set(this.currentItem, this.mainInventory.get(index));
        this.mainInventory.set(index, itemstack);
    }
    public int getBestHotbarSlot() {
        for(int i = 0; i < 9; ++i) {
            int j = (this.currentItem + i) % 9;
            if (this.mainInventory.get(j).isEmpty()) {
                return j;
            }
        }

        for(int k = 0; k < 9; ++k) {
            int l = (this.currentItem + k) % 9;
            if (!this.mainInventory.get(l).isEnchanted()) {
                return l;
            }
        }

        return this.currentItem;
    }

    @OnlyIn(Dist.CLIENT)
    public int getSlotFor(ItemStack stack) {
        for(int i = 0; i < this.mainInventory.size(); ++i) {
            if (!this.mainInventory.get(i).isEmpty() && this.stackEqualExact(stack, this.mainInventory.get(i))) {
                return i;
            }
        }

        return -1;
    }
    public int func_234564_a_(Predicate<ItemStack> p_234564_1_, int p_234564_2_, IInventory p_234564_3_) {
        int i = 0;
        boolean flag = p_234564_2_ == 0;
        i = i + ItemStackHelper.func_233534_a_(this, p_234564_1_, p_234564_2_ - i, flag);
        i = i + ItemStackHelper.func_233534_a_(p_234564_3_, p_234564_1_, p_234564_2_ - i, flag);
        i = i + ItemStackHelper.func_233535_a_(this.itemStack, p_234564_1_, p_234564_2_ - i, flag);
        if (this.itemStack.isEmpty()) {
            this.itemStack = ItemStack.EMPTY;
        }

        return i;
    }
    private int storePartialItemStack(ItemStack itemStackIn) {
        int i = this.storeItemStack(itemStackIn);
        if (i == -1) {
            i = this.getFirstEmptyStack();
        }

        return i == -1 ? itemStackIn.getCount() : this.addResource(i, itemStackIn);
    }
    private int addResource(int p_191973_1_, ItemStack p_191973_2_) {
        Item item = p_191973_2_.getItem();
        int i = p_191973_2_.getCount();
        ItemStack itemstack = this.getStackInSlot(p_191973_1_);
        if (itemstack.isEmpty()) {
            itemstack = p_191973_2_.copy(); // Forge: Replace Item clone above to preserve item capabilities when picking the item up.
            itemstack.setCount(0);
            if (p_191973_2_.hasTag()) {
                itemstack.setTag(p_191973_2_.getTag().copy());
            }

            this.setInventorySlotContents(p_191973_1_, itemstack);
        }

        int j = i;
        if (i > itemstack.getMaxStackSize() - itemstack.getCount()) {
            j = itemstack.getMaxStackSize() - itemstack.getCount();
        }

        if (j > this.getInventoryStackLimit() - itemstack.getCount()) {
            j = this.getInventoryStackLimit() - itemstack.getCount();
        }

        if (j == 0) {
            return i;
        } else {
            i = i - j;
            itemstack.grow(j);
            itemstack.setAnimationsToGo(5);
            return i;
        }
    }

    public int storeItemStack(ItemStack itemStackIn) {
        if (this.canMergeStacks(this.getStackInSlot(this.currentItem), itemStackIn)) {
            return this.currentItem;
        } else if (this.canMergeStacks(this.getStackInSlot(40), itemStackIn)) {
            return 40;
        } else {
            for(int i = 0; i < this.mainInventory.size(); ++i) {
                if (this.canMergeStacks(this.mainInventory.get(i), itemStackIn)) {
                    return i;
                }
            }

            return -1;
        }
    }
    public void tick() {
        for(NonNullList<ItemStack> nonnulllist : this.allInventories) {
            for(int i = 0; i < nonnulllist.size(); ++i) {
                if (!nonnulllist.get(i).isEmpty()) {
                    nonnulllist.get(i).inventoryTick(this.an.world, this.an ,i, this.currentItem == i);
                }
            }
        }
    }

    /**
     * Adds the stack to the first empty slot in the player's inventory. Returns {@code false} if it's not possible to
     * place the entire stack in the inventory.
     */
    public boolean addItemStackToInventory(ItemStack itemStackIn) {
        return this.add(-1, itemStackIn);
    }

    /**
     * Adds the stack to the specified slot in the player's inventory. Returns {@code false} if it's not possible to
     * place the entire stack in the inventory.
     */
    public boolean add(int slotIn, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        } else {
            try {
                if (stack.isDamaged()) {
                    if (slotIn == -1) {
                        slotIn = this.getFirstEmptyStack();
                    }

                    if (slotIn >= 0) {
                        this.mainInventory.set(slotIn, stack.copy());
                        this.mainInventory.get(slotIn).setAnimationsToGo(5);
                        stack.setCount(0);
                        return true;
                    }
                    else {
                        return false;
                    }
                } else {
                    int i;
                    do {
                        i = stack.getCount();
                        if (slotIn == -1) {
                            stack.setCount(this.storePartialItemStack(stack));
                        } else {
                            stack.setCount(this.addResource(slotIn, stack));
                        }
                    } while(!stack.isEmpty() && stack.getCount() < i);
                        return stack.getCount() < i;
                }
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
                crashreportcategory.addDetail("Registry Name", () -> String.valueOf(stack.getItem().getRegistryName()));
                crashreportcategory.addDetail("Item Class", () -> stack.getItem().getClass().getName());
                crashreportcategory.addDetail("Item ID", Item.getIdFromItem(stack.getItem()));
                crashreportcategory.addDetail("Item data", stack.getDamage());
                crashreportcategory.addDetail("Item name", () -> {
                    return stack.getDisplayName().getString();
                });
                throw new ReportedException(crashreport);
            }
        }
    }
    public void placeItemBackInInventory(World worldIn, ItemStack stack) {
        if (!worldIn.isRemote) {
            while(!stack.isEmpty()) {
                int i = this.storeItemStack(stack);
                if (i == -1) {
                    i = this.getFirstEmptyStack();
                }

                int j = stack.getMaxStackSize() - this.getStackInSlot(i).getCount();
            }

        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory() {
        return this.mainInventory.size() + this.armorInventory.size() + this.offHandInventory.size();
    }


    /**
     * Returns the stack in the given slot.
     *
     * @param index
     */
    @Override
    public ItemStack getStackInSlot(int index) {
        List<ItemStack> list = null;

        for(NonNullList<ItemStack> nonnulllist : this.allInventories) {
            if (index < nonnulllist.size()) {
                list = nonnulllist;
                break;
            }

            index -= nonnulllist.size();
        }

        return list == null ? ItemStack.EMPTY : list.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     *
     * @param index
     * @param count
     */
    @Override
    public ItemStack decrStackSize(int index, int count) {
        List<ItemStack> list = null;

        for(NonNullList<ItemStack> nonnulllist : this.allInventories) {
            if (index < nonnulllist.size()) {
                list = nonnulllist;
                break;
            }

            index -= nonnulllist.size();
        }

        return list != null && !list.get(index).isEmpty() ? ItemStackHelper.getAndSplit(list, index, count) : ItemStack.EMPTY;

    }

    /**
     * Removes a stack from the given slot and returns it.
     *
     * @param index
     */
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     *
     * @param index
     * @param stack
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        NonNullList<ItemStack> nonnulllist = null;

        for(NonNullList<ItemStack> nonnulllist1 : this.allInventories) {
            if (index < nonnulllist1.size()) {
                nonnulllist = nonnulllist1;
                break;
            }

            index -= nonnulllist1.size();
        }

        if (nonnulllist != null) {
            nonnulllist.set(index, stack);
        }

    }


    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    @Override
    public void markDirty() {
        ++this.timesChanged;
    }
    @OnlyIn(Dist.CLIENT)
    public int getTimesChanged() {
        return this.timesChanged;
    }
    public void setItemStack(ItemStack itemStackIn) {
        this.itemStack = itemStackIn;
    }

    /**
     * Stack helds by mouse, used in GUI and Containers
     */
    public ItemStack getItemStack() {
        return this.itemStack;
    }


    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     *
     * @param player
     */
    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {

    }

    @Override
    public ITextComponent getName() {
        return null;
    }

    /**
     * Writes the inventory out as a list of compound tags. This is where the slot indices are used (+100 for armor, +80
     * for crafting).
     */
    public ListNBT write(ListNBT nbtTagListIn) {
        for(int i = 0; i < this.mainInventory.size(); ++i) {
            if (!this.mainInventory.get(i).isEmpty()) {
                CompoundNBT compoundnbt = new CompoundNBT();
                compoundnbt.putByte("Slot", (byte)i);
                this.mainInventory.get(i).write(compoundnbt);
                nbtTagListIn.add(compoundnbt);
            }
        }

        for(int j = 0; j < this.armorInventory.size(); ++j) {
            if (!this.armorInventory.get(j).isEmpty()) {
                CompoundNBT compoundnbt1 = new CompoundNBT();
                compoundnbt1.putByte("Slot", (byte)(j + 100));
                this.armorInventory.get(j).write(compoundnbt1);
                nbtTagListIn.add(compoundnbt1);
            }
        }

        for(int k = 0; k < this.offHandInventory.size(); ++k) {
            if (!this.offHandInventory.get(k).isEmpty()) {
                CompoundNBT compoundnbt2 = new CompoundNBT();
                compoundnbt2.putByte("Slot", (byte)(k + 150));
                this.offHandInventory.get(k).write(compoundnbt2);
                nbtTagListIn.add(compoundnbt2);
            }
        }

        return nbtTagListIn;
    }

    /**
     * Reads from the given tag list and fills the slots in the inventory with the correct items.
     */
    public void read(ListNBT nbtTagListIn) {
        this.mainInventory.clear();
        this.armorInventory.clear();
        this.offHandInventory.clear();

        for(int i = 0; i < nbtTagListIn.size(); ++i) {
            CompoundNBT compoundnbt = nbtTagListIn.getCompound(i);
            int j = compoundnbt.getByte("Slot") & 255;
            ItemStack itemstack = ItemStack.read(compoundnbt);
            if (!itemstack.isEmpty()) {
                if (j >= 0 && j < this.mainInventory.size()) {
                    this.mainInventory.set(j, itemstack);
                } else if (j >= 100 && j < this.armorInventory.size() + 100) {
                    this.armorInventory.set(j - 100, itemstack);
                } else if (j >= 150 && j < this.offHandInventory.size() + 150) {
                    this.offHandInventory.set(j - 150, itemstack);
                }
            }
        }

    }
    public boolean isEmpty() {
        for(ItemStack itemstack : this.mainInventory) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        for(ItemStack itemstack1 : this.armorInventory) {
            if (!itemstack1.isEmpty()) {
                return false;
            }
        }

        for(ItemStack itemstack2 : this.offHandInventory) {
            if (!itemstack2.isEmpty()) {
                return false;
            }
        }

        return true;
    }
    public boolean hasItemStack(ItemStack itemStackIn) {
        for(List<ItemStack> list : this.allInventories) {
            for(ItemStack itemstack : list) {
                if (!itemstack.isEmpty() && itemstack.isItemEqual(itemStackIn)) {
                    return true;
                }
            }
        }

        return false;
    }
    @OnlyIn(Dist.CLIENT)
    public boolean hasTag(ITag<Item> itemTag) {
        for(List<ItemStack> list : this.allInventories) {
            for(ItemStack itemstack : list) {
                if (!itemstack.isEmpty() && itemTag.contains(itemstack.getItem())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Copy the ItemStack contents from another InventoryPlayer instance
     */
    public void copyInventory(PlayerInventory playerInventory) {
        for(int i = 0; i < this.getSizeInventory(); ++i) {
            this.setInventorySlotContents(i, playerInventory.getStackInSlot(i));
        }

        this.currentItem = playerInventory.currentItem;
    }
    public void accountStacks(RecipeItemHelper p_201571_1_) {
        for(ItemStack itemstack : this.mainInventory) {
            p_201571_1_.accountPlainStack(itemstack);
        }

    }


}
