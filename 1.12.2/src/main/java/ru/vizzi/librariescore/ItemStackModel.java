package ru.vizzi.librariescore;

import lombok.Data;
import net.minecraft.nbt.NBTTagCompound;
import ru.vizzi.librariescore.config.Configurable;

@Data
@Configurable
public class ItemStackModel {
    private final String name;
    private final int count;
    private final int damage;
    private final NBTTagCompound stringedNbtTagCompound;
}
