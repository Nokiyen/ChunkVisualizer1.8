package noki.chunkvisualizer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBeacon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.lwjgl.input.Keyboard;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;


/**********
 * @class EventKeyInput
 *
 * @description
 * @description_en
 */
public class EventKeyInput {
	
	//******************************//
	// define member variables.
	//******************************//
	public static int confKeyNum;
	public static int keyNum;
	public static KeyBinding key;
	public static KeyBinding keyB;
	
	private Thread thread;


	//******************************//
	// define member methods.
	//******************************//
	//----------
	//Static Method
	//----------
	public static void setData(FMLPreInitializationEvent event) {
		
		Property prop;
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		cfg.load();
		prop = cfg.get("Key", "keyNumber", 9);
		confKeyNum = MathHelper.clamp_int(prop.getInt(), 1, 12);
		cfg.save();
		
		switch(confKeyNum) {
			case 1:
			case 2:
			case 3:
			case 5:
			case 8:
			case 9:	//	default.
			case 10:
			case 11:
				keyNum = Keyboard.KEY_F9;
				break;
			case 4:
				keyNum = Keyboard.KEY_F4;
				break;
			case 6:
				keyNum = Keyboard.KEY_F6;
				break;
			case 7:
				keyNum = Keyboard.KEY_F7;
				break;
			case 12:
				keyNum = Keyboard.KEY_F12;
				break;
			default:
				keyNum = Keyboard.KEY_F9;
				break;
		}
		
		key = new KeyBinding("chunkvisualizer.key.on", keyNum, "System");
		keyB = new KeyBinding("chunkvisualizer.key.b", Keyboard.KEY_B, "System");
		
	}
	
	private static void sendMessage(String label) {
		
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(I18n.format(label, new Object[0])));
		
	}


	//----------
	//Method for Event
	//-----
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		
		//	render beacon effective area.
		if(keyB.isKeyDown() && key.isPressed()) {
			
			if(this.thread != null && this.thread.getState() != Thread.State.TERMINATED) {
				sendMessage("chunkvisualizer.event.beaconsearching");
				return;
			}

			EventWorldRender.activatedBeacon = !EventWorldRender.activatedBeacon;
			
			if(EventWorldRender.activatedBeacon) {
				this.thread = new ThreadKeyInput();
				this.thread.start();
			}
			else {
				EventWorldRender.position = null;
				EventWorldRender.level = 0;
				sendMessage("chunkvisualizer.event.beaconoff");
			}
			return;
		}
		
		//	render chunks.
		if(key.isPressed()) {
			EventWorldRender.activatedChunk = !EventWorldRender.activatedChunk;
			if(EventWorldRender.activatedChunk == true) {
				sendMessage("chunkvisualizer.event.chunkon");
			}
			else {
				sendMessage("chunkvisualizer.event.chunkoff");
			}
		}
		
	}

	
	//----------
	//Inner Class
	//----------
	private class ThreadKeyInput extends Thread {
		
		@Override
		public void run() {
			
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayer entity = mc.thePlayer;
			World world = entity.worldObj;
			
			int posX = (int)entity.posX;
			int posZ = (int)entity.posZ;
			
			TileEntityBeacon tile = null;
			double dist = 99999;
			for(int i = posX-100; i <= posX+100; i++) {
				for(int j = posZ-100; j <= posZ+100; j++) {
					for(int k = 0; k <= 255; k++) {
						Block currentBlock = world.getBlockState(new BlockPos(i, k, j)).getBlock();
						if(!(currentBlock instanceof BlockBeacon)) {
							continue;
						}
						TileEntity currentTile = world.getTileEntity(new BlockPos(i, k, j));
						double currentDist = currentTile.getDistanceSq(entity.posX, entity.posY, entity.posZ);
						if(currentDist < dist) {
							tile = (TileEntityBeacon)currentTile;
							dist = currentDist;
						}
					}
				}
			}
			
			if(tile != null && tile.getField(0) > 0) {
				EventWorldRender.position = new Vec3(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
				EventWorldRender.level = tile.getField(0);
				sendMessage("chunkvisualizer.event.beaconon");
			}
			else {
				EventWorldRender.activatedBeacon = false;
				EventWorldRender.position = null;
				EventWorldRender.level = 0;
				sendMessage("chunkvisualizer.event.beaconno");
			}
			
		}
		
	}
	
}
