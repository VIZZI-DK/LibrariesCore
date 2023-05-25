package ru.vizzi.Utils.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import ru.vizzi.Utils.obf.IgnoreObf;

public class AbstractExtendedPlayer implements IExtendedEntityProperties {
	
	private EntityPlayer player;
	
	public AbstractExtendedPlayer(EntityPlayer player) {
		this.player = player;
	}
	
	public AbstractExtendedPlayer get(EntityPlayer player) {
		return (AbstractExtendedPlayer) player.getExtendedProperties(getExtendedName());
	}
	
	public String getExtendedName() {
		return this.getClass().getName();
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@IgnoreObf
	public void loadNBTData(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(Entity entity, World world) {
		// TODO Auto-generated method stub
		
	}
	
	public void copyNBT(AbstractExtendedPlayer props) {
       
    }

}
