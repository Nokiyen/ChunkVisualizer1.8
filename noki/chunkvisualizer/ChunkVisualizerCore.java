package noki.chunkvisualizer;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


/**********
 * @ModName ChunkVisualizerCore
 * 
 * @description
 * 
 * @caution ここはコアファイルなので、原則、具体的な処理をしないよう気を付ける。
 */
@Mod(modid = "ChunkVisualizer", name = "Chunk Visualizer", version = "1.1.0", useMetadata = true)
public class ChunkVisualizerCore {
	
	//******************************//
	// define member variables.
	//******************************//
	//	core.
	@Instance(value = "ChunkVisualizer")
	public static ChunkVisualizerCore instance;
	@Metadata
	public static ModMetadata metadata;
	@SidedProxy(clientSide = "noki.chunkvisualizer.ProxyClient", serverSide = "noki.chunkvisualizer.ProxyCommon")
	public static ProxyCommon proxy;

	public static VersionInfo versionInfo;
	
	
	//******************************//
	// define member methods.
	//******************************//
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		EventKeyInput.setData(event);
		
		versionInfo = new VersionInfo(metadata.modId.toLowerCase(), metadata.version, metadata.updateUrl);
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
				
		proxy.register();
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		//	nothing to do.
		
	}

}
