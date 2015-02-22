package noki.chunkvisualizer;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;


/**********
 * @class ProxyClient
 *
 * @description クライアント用proxyクラス。
 * @description_en Proxy class for Client.
 */
public class ProxyClient extends ProxyCommon {
	
	//******************************//
	// define member variables.
	//******************************//


	//******************************//
	// define member methods.
	//******************************//
	@Override
	public void register() {
		
		ClientRegistry.registerKeyBinding(EventKeyInput.key);
		MinecraftForge.EVENT_BUS.register(new EventJoinWorld());
		FMLCommonHandler.instance().bus().register(new EventKeyInput());
		MinecraftForge.EVENT_BUS.register(new EventWorldRender());
		
	}
	
}
