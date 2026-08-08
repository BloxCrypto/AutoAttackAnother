package com.lothrazar.examplemod;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Client-side combat handler that attacks eligible entities directly in front
 * of the local player when the attack cooldown is fully charged.
 */
@Mod.EventBusSubscriber(modid = ModMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class AutoAttack {

  /** Controls whether the automatic attack behavior is active. */
  public static boolean enabled = true;

  private static final double REACH = 3.0D;

  private AutoAttack() {
    // Utility class; all behavior is driven by the Forge event bus.
  }

  /**
   * Checks for a target once at the end of every client tick.
   *
   * @param event the client tick event supplied by Forge
   */
  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END || !enabled) {
      return;
    }

    Minecraft minecraft = Minecraft.getInstance();
    LocalPlayer player = minecraft.player;
    if (player == null || minecraft.getConnection() == null) {
      return;
    }

    if (player.getAttackStrengthScale(0.0F) != 1.0F) {
      return;
    }

    LivingEntity target = findTarget(player);
    if (target == null || target.invulnerableTime > 10) {
      return;
    }

    minecraft.getConnection().send(ServerboundInteractPacket.attack(target, player.isShiftKeyDown()));
    minecraft.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
  }

  /**
   * Finds the closest living entity whose bounding box intersects the player's
   * three-block view ray and whose entity type is attackable by this utility.
   */
  private static LivingEntity findTarget(LocalPlayer player) {
    Vec3 eyePosition = player.getEyePosition(0.0F);
    Vec3 rayEnd = eyePosition.add(player.getViewVector(0.0F).scale(REACH));
    AABB searchBox = player.getBoundingBox().inflate(REACH);

    List<LivingEntity> candidates = player.level().getEntitiesOfClass(
        LivingEntity.class,
        searchBox,
        target -> target != player && isAttackable(target));

    return candidates.stream()
        .filter(target -> isInView(target, eyePosition, rayEnd))
        .min(Comparator.comparingDouble(player::distanceToSqr))
        .orElse(null);
  }

  /**
   * Returns true only when the target's AABB is hit by the player's view ray.
   */
  private static boolean isInView(Entity target, Vec3 eyePosition, Vec3 rayEnd) {
    AABB entityBox = target.getBoundingBox();
    Optional<Vec3> hit = entityBox.clip(eyePosition, rayEnd);
    return hit.isPresent();
  }

  /**
   * Restricts attacks to players and hostile monsters, excluding passive
   * animals, armor stands, dropped items, and other entity types.
   */
  private static boolean isAttackable(LivingEntity target) {
    return target.isAlive() && (target instanceof Player || target instanceof Monster);
  }
}