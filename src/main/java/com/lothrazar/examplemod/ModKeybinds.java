package com.lothrazar.examplemod;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Registers client key mappings on the Forge MOD event bus. */
@Mod.EventBusSubscriber(modid = ModMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModKeybinds {

  public static final KeyMapping TOGGLE_KEY = new KeyMapping(
      "key.autoattack.toggle",
      InputConstants.Type.KEYSYM,
      GLFW.GLFW_KEY_K,
      "category.autoattack");

  private ModKeybinds() {
    // Utility class; key mappings are registered through the event bus.
  }

  /** Registers the auto-attack toggle key during client mod initialization. */
  @SubscribeEvent
  public static void onKeyRegister(RegisterKeyMappingsEvent event) {
    event.register(TOGGLE_KEY);
  }
}