package com.lothrazar.examplemod;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Handles the auto-attack keypress on the client Forge event bus. */
@Mod.EventBusSubscriber(modid = ModMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class KeybindTickHandler {

  private KeybindTickHandler() {
    // Utility class; behavior is driven by the Forge event bus.
  }

  /** Toggles auto-attack at the end of the client tick when the key is pressed. */
  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END) {
      return;
    }

    Minecraft minecraft = Minecraft.getInstance();
    while (KeybindHandler.toggleAutoAttackKey.consumeClick()) {
      AutoAttackHandler.enabled = !AutoAttackHandler.enabled;
      boolean enabled = AutoAttackHandler.enabled;
      if (minecraft.player != null) {
        minecraft.player.sendSystemMessage(
            Component.literal("§aAutoAttack: " + (enabled ? "§2ON" : "§cOFF")));
      }
    }
  }
}