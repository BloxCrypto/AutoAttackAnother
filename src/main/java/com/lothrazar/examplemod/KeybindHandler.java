package com.lothrazar.examplemod;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Registers and handles the client keybind used to toggle auto-attack. */
@Mod.EventBusSubscriber(modid = ModMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class KeybindHandler {

  /** Toggles auto-attack when pressed. The default key is K. */
  public static final KeyMapping toggleAutoAttackKey = new KeyMapping(
      "key.examplemod.toggle_auto_attack",
      InputConstants.Type.KEYSYM,
      InputConstants.KEY_K,
      "key.categories.examplemod");

  private KeybindHandler() {
    // Utility class; all behavior is driven by Forge event buses.
  }

  /** Registers this key mapping on the MOD event bus. */
  @SubscribeEvent
  public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
    event.register(toggleAutoAttackKey);
  }
}