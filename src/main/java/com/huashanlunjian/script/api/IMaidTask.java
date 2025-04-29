package com.huashanlunjian.script.api;

import com.google.common.collect.Lists;
import com.huashanlunjian.script.entity.AnEntity;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public interface IMaidTask {
    /**
     * 模式 ID，用于后续模式的判断，也用于本地化的 key
     *
     * @return 用 ResourceLocation 类描述的模式 ID
     */
    ResourceLocation getUid();

    /**
     * 该模式的图标
     *
     * @return 用 ItemStack 类描述的图标
     */
    ItemStack getIcon();

    /**
     * 该模式的环境音效
     *
     * @param an 女仆对象
     * @return 对应的环境音效
     */
    @Nullable
    SoundEvent getAmbientSound(AnEntity an);

    /**
     * 该模式下调用的 AI
     *
     * @param an 女仆对象
     * @return 如果什么都不做，请返回空集合
     */
    List<Pair<Integer, Task<? super AnEntity>>> createBrainTasks(AnEntity an);

    /**
     * 当前 Task 是否可使用
     *
     * @param maid 女仆对象
     * @return 当前 Task 是否可以用
     */
    default boolean isEnable(AnEntity maid) {
        return true;
    }

    /**
     * 获取当前模式名称
     *
     * @return 当前模式名称
     */
    default ITextComponent getName() {
        return ITextComponent.getTextComponentOrEmpty(String.format("task.%s.%s", getUid().getNamespace(), getUid().getPath()));
    }

    /**
     * 获取额外的条件提示文本
     *
     * @param maid 女仆对象
     * @return 条件名（用于自动生成对应的 key）和对应条件布尔值的组合列表
     */
    default List<Pair<String, Predicate<AnEntity>>> getConditionDescription(AnEntity maid) {
        return Collections.emptyList();
    }

    /**
     * 获取当前模式的描述文本的 key
     *
     * @param maid 女仆对象
     * @return 模式的描述文本，可以多行<br>如果没有文本描述，请返回空集合
     */
    default List<String> getDescription(AnEntity maid) {
        String key = String.format("task.%s.%s.desc", getUid().getNamespace(), getUid().getPath());
        return Lists.newArrayList(key);
    }
}
