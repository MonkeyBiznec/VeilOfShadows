package com.monkeybiznec.veilshadows;

import com.monkeybiznec.veilshadows.client.render.post.PostProcessor;
import com.monkeybiznec.veilshadows.core.proxy.VSClientProxy;
import com.monkeybiznec.veilshadows.core.proxy.VSCommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

@Mod(VeilOfShadows.ID)
public class VeilOfShadows {
    public static final String ID = "veilshadows";
    public static VSCommonProxy PROXY = DistExecutor.safeRunForDist(() -> VSClientProxy::new, () -> VSCommonProxy::new);

    @SuppressWarnings("removal")
    public VeilOfShadows() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onCommonSetup);
        bus.addListener(this::onClientSetup);
        PROXY.commonInitialize();
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void onCommonSetup(@NotNull final FMLCommonSetupEvent event) {

    }

    private void onClientSetup(@NotNull final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            PROXY.clientInitialize();
        });
    }
}
