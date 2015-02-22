package noki.chunkvisualizer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


/**********
 * @class EventWorldRender
 *
 * @description
 * @description_en
 */
public class EventWorldRender {
	
	//******************************//
	// define member variables.
	//******************************//
	public static boolean activatedChunk = false;
	
	public static boolean activatedBeacon = false;
	public static Vec3 position;
	public static int level;

	
	//******************************//
	// define member methods.
	//******************************//
	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		
		if(activatedChunk) {
			renderChunks(event);
		}
		if(activatedBeacon) {
			renderBeaconArea(event);
		}
		
	}
	
	private void renderChunks(RenderWorldLastEvent event) {
		
		Minecraft mc = Minecraft.getMinecraft();
		EntityLivingBase entity = mc.thePlayer;
				
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);	// alpha blend.
		GL11.glEnable(GL11.GL_BLEND);
		
		GL11.glPushMatrix();
		GL11.glTranslated(
				-(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.partialTicks),
				-(entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks),
				-(entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.partialTicks));
		
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		
		
		double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks) + 1;
		
		
		renderer.startDrawing(GL11.GL_LINES);
		GlStateManager.color(0F, 1F, 0F, 80F/255F);
		GL11.glLineWidth(3);
		int range = mc.gameSettings.renderDistanceChunks;
		for(int i = entity.chunkCoordX-range; i <= entity.chunkCoordX+range; i++) {
			for(int j = entity.chunkCoordZ-range; j <= entity.chunkCoordZ+range; j++) {
				renderer.addVertex(i*16, 0, j*16);
				renderer.addVertex(i*16, y+30, j*16);
			}
		}
		tessellator.draw();
		
		renderer.startDrawingQuads();
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GlStateManager.color(0F, 0F, 1F, 40F/255F);
		int currentX = entity.chunkCoordX*16;
		int currentZ = entity.chunkCoordZ*16;
		
		renderer.addVertex(currentX, 0, currentZ);
		renderer.addVertex(currentX, y, currentZ);
		renderer.addVertex(currentX+16, y, currentZ);
		renderer.addVertex(currentX+16, 0, currentZ);
		
		renderer.addVertex(currentX+16, 0, currentZ);
		renderer.addVertex(currentX+16, y, currentZ);
		renderer.addVertex(currentX+16, y, currentZ+16);
		renderer.addVertex(currentX+16, 0, currentZ+16);
		
		renderer.addVertex(currentX+16, 0, currentZ+16);
		renderer.addVertex(currentX+16, y, currentZ+16);
		renderer.addVertex(currentX, y, currentZ+16);
		renderer.addVertex(currentX, 0, currentZ+16);
		
		renderer.addVertex(currentX, 0, currentZ+16);
		renderer.addVertex(currentX, y, currentZ+16);
		renderer.addVertex(currentX, y, currentZ);
		renderer.addVertex(currentX, 0, currentZ);
		
		tessellator.draw();
		
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDepthMask(true);
		
	}
	
	private void renderBeaconArea(RenderWorldLastEvent event) {
		
		if(position == null) {
			return;
		}
		
		Minecraft mc = Minecraft.getMinecraft();
		EntityLivingBase entity = mc.thePlayer;
		
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);	// alpha blend.
		GL11.glEnable(GL11.GL_BLEND);
		
		GL11.glPushMatrix();
		GL11.glTranslated(
				-(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.partialTicks),
				-(entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks),
				-(entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.partialTicks));
		
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		
		double range = (double)(level * 10 + 10);
		double bottom = position.yCoord-range < 0 ? 0 : position.yCoord-range;
		double minX = position.xCoord-range;
		double maxX = position.xCoord+range;
		double minZ = position.zCoord-range;
		double maxZ = position.zCoord+range;
		
		//	draw lines.
		renderer.startDrawing(GL11.GL_LINES);
		GlStateManager.color(0F, 1F, 0F, 80F/255F);
		GL11.glLineWidth(3);
		
		renderer.addVertex(minX, bottom, minZ);
		renderer.addVertex(minX, 255, minZ);
		
		renderer.addVertex(maxX, bottom, minZ);
		renderer.addVertex(maxX, 255, minZ);
		
		renderer.addVertex(maxX, bottom, maxZ);
		renderer.addVertex(maxX, 255, maxZ);
		
		renderer.addVertex(minX, bottom, maxZ);
		renderer.addVertex(minX, 255, maxZ);
		
		renderer.addVertex(minX, bottom, minZ);
		renderer.addVertex(maxX, bottom, minZ);
		
		renderer.addVertex(maxX, bottom, minZ);
		renderer.addVertex(maxX, bottom, maxZ);
		
		renderer.addVertex(maxX, bottom, maxZ);
		renderer.addVertex(minX, bottom, maxZ);
		
		renderer.addVertex(minX, bottom, maxZ);
		renderer.addVertex(minX, bottom, minZ);
		
		tessellator.draw();
		
		//	draw rectangles.
		renderer.startDrawingQuads();
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GlStateManager.color(0F, 0F, 1F, 40F/255F);
		
		renderer.addVertex(minX, bottom, minZ);
		renderer.addVertex(minX, 255, minZ);
		renderer.addVertex(maxX, 255, minZ);
		renderer.addVertex(maxX, bottom, minZ);
		
		renderer.addVertex(maxX, bottom, minZ);
		renderer.addVertex(maxX, 255, minZ);
		renderer.addVertex(maxX, 255, maxZ);
		renderer.addVertex(maxX, bottom, maxZ);
		
		renderer.addVertex(maxX, bottom, maxZ);
		renderer.addVertex(maxX, 255, maxZ);
		renderer.addVertex(minX, 255, maxZ);
		renderer.addVertex(minX, bottom, maxZ);
		
		renderer.addVertex(minX, bottom, maxZ);
		renderer.addVertex(minX, 255, maxZ);
		renderer.addVertex(minX, 255, minZ);
		renderer.addVertex(minX, bottom, minZ);

		renderer.addVertex(minX, bottom, minZ);
		renderer.addVertex(maxX, bottom, minZ);
		renderer.addVertex(maxX, bottom, maxZ);
		renderer.addVertex(minX, bottom, maxZ);
		
		tessellator.draw();
		
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDepthMask(true);
		
	}

}
