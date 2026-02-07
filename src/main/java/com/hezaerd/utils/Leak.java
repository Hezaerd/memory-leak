package com.hezaerd.utils;

import com.hezaerd.ModLib;
import net.minecraft.network.chat.Component;
//? if >=1.21.11 {
/* import net.minecraft.resources.Identifier;
 *///?} else {
import net.minecraft.resources.ResourceLocation;
//?}
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.world.BossEvent;

import java.util.ArrayList;
import java.util.List;

public class Leak {

    private static final List<byte[]> LEAK_BUFFER = new ArrayList<>();
    //? if >=1.21.11 {
    /*  private static final Identifier BOSS_BAR_ID = ModLib.id("memory_leak");
     *///?} else {
    private static final ResourceLocation BOSS_BAR_ID = ModLib.id("memory_leak");
    //?}

    private static volatile boolean isLeaking = false;
    private static volatile int targetMegabytes = 0;
    private static volatile int currentMegabytes = 0;
    private static volatile int remainingTicks = 0;
    private static volatile int megabytesPerTick = 0;
    private static volatile int remainderMegabytes = 0;

    private static MinecraftServer serverInstance = null;
    private static CustomBossEvent bossBar = null;

    public static void startGradualLeak(int megabytes, int seconds, MinecraftServer server) {
        synchronized (Leak.class) {
            if (isLeaking) {
                throw new IllegalStateException("A leak is already in progress. Stop it first.");
            }

            isLeaking = true;
            targetMegabytes = megabytes;
            currentMegabytes = 0;

            serverInstance = server;

            int totalTicks = seconds * 20;
            remainingTicks = totalTicks;

            // Calculate how much to leak per tick
            megabytesPerTick = megabytes / totalTicks;
            remainderMegabytes = megabytes % totalTicks;

            createBossBar(megabytes, seconds);
        }
    }

    public static void processTick() {
        synchronized (Leak.class) {
            if (!isLeaking || remainingTicks <= 0) {
                if (isLeaking) {
                    removeBossBar();
                }
                isLeaking = false;
                return;
            }

            int toLeakThisTick = megabytesPerTick;

            if (remainderMegabytes > 0 && remainingTicks <= remainderMegabytes) {
                toLeakThisTick++;
                remainderMegabytes--;
            }

            for (int i = 0; i < toLeakThisTick; i++) {
                LEAK_BUFFER.add(new byte[1024 * 1024]);
                currentMegabytes++;
            }

            remainingTicks--;

            // Update boss bar every 10 ticks (0.5 seconds) for performance
            if (remainingTicks % 10 == 0) {
                updateBossBar();
            }

            if (remainingTicks <= 0) {
                updateBossBar();
                removeBossBar();
                isLeaking = false;
            }

        }
    }

    public static void stop() {
        synchronized (Leak.class) {
            isLeaking = false;
            remainingTicks = 0;
            removeBossBar();
        }
    }

    public static void clear() {
        synchronized (Leak.class) {
            stop();
            LEAK_BUFFER.clear();
            currentMegabytes = 0;
            targetMegabytes = 0;
        }
    }

    private static void createBossBar(int megabytes, int seconds) {
        CustomBossEvents bossEvents = serverInstance.getCustomBossEvents();

        CustomBossEvent existing = bossEvents.get(BOSS_BAR_ID);
        if (existing != null) {
            existing.removeAllPlayers();
            bossEvents.remove(existing);
        }

        bossBar = bossEvents.create(BOSS_BAR_ID,
                Component.literal("Memory Leak: 0/" + megabytes + " MB"));

        bossBar.setMax(targetMegabytes);
        bossBar.setValue(0);
        bossBar.setColor(BossEvent.BossBarColor.RED);
        bossBar.setOverlay(BossEvent.BossBarOverlay.NOTCHED_10);
        bossBar.setVisible(true);

        bossBar.setPlayers(Admin.getAdminPlayers(serverInstance));
    }

    private static void updateBossBar() {
        ModLib.Logger.info("updateBossBar() invoked");
        if (bossBar != null && serverInstance != null) {
            bossBar.setValue(currentMegabytes);
            var temp = String.format("Memory Leak: %d/%d MB (%.1f%%)",
                    currentMegabytes, targetMegabytes,
                    (double) currentMegabytes / targetMegabytes * 100);
            bossBar.setName(Component.literal(temp));

            ModLib.Logger.info(temp);

            double progress = (double) currentMegabytes / targetMegabytes;
            if (progress < 0.33) {
                bossBar.setColor(BossEvent.BossBarColor.GREEN);
            } else if (progress < 0.66) {
                bossBar.setColor(BossEvent.BossBarColor.YELLOW);
            } else {
                bossBar.setColor(BossEvent.BossBarColor.RED);
            }

            bossBar.setPlayers(Admin.getAdminPlayers(serverInstance));
        }
    }

    private static void removeBossBar() {
        if (bossBar != null && serverInstance != null) {
            CustomBossEvents bossEvents = serverInstance.getCustomBossEvents();
            bossBar.removeAllPlayers();
            bossEvents.remove(bossBar);
            bossBar = null;
        }
    }

    public static LeakStatus getStatus() {
        synchronized (Leak.class) {
            return new LeakStatus(isLeaking, currentMegabytes, targetMegabytes, remainingTicks);
        }
    }

    public static class LeakStatus {
        public final boolean isLeaking;
        public final int currentMegabytes;
        public final int targetMegabytes;
        public final int remainingTicks;

        public LeakStatus(boolean isLeaking, int currentMegabytes, int targetMegabytes, int remainingTicks) {
            this.isLeaking = isLeaking;
            this.currentMegabytes = currentMegabytes;
            this.targetMegabytes = targetMegabytes;
            this.remainingTicks = remainingTicks;
        }
    }
}
