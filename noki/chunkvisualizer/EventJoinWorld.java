package noki.chunkvisualizer;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;


/**********
 * @class EventJoinWorld
 *
 * @description
 * @description_en
 */
public class EventJoinWorld {
	
	//******************************//
	// define member variables.
	//******************************//


	//******************************//
	// define member methods.
	//******************************//
	
	@SubscribeEvent
	public void onJoinWorld(EntityJoinWorldEvent event) {
		
		if(event.world.isRemote && event.entity instanceof EntityPlayer) {			
			UUID targetID = ((EntityPlayer)event.entity).getGameProfile().getId();
			UUID playerID = Minecraft.getMinecraft().thePlayer.getGameProfile().getId();
			if(targetID.equals(playerID)) {
				ChunkVisualizerCore.versionInfo.notifyUpdate(Side.CLIENT);			
			}
			
			EventWorldRender.activatedChunk = false;
			EventWorldRender.activatedBeacon = false;
			EventWorldRender.position = null;
			EventWorldRender.level = 0;
		}
		
	}
	
}
