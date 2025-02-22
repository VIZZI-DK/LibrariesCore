package ru.vizzi.Utils;

import lombok.Data;
import net.minecraft.nbt.NBTTagCompound;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.vizzi.Utils.config.Configurable;

@Data
@Configurable
public class ItemStackModel {
    private final String name;
    private final int count;
    private final int damage;
    private final NBTTagCompound stringedNbtTagCompound;
}
