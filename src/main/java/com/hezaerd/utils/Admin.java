package com.hezaerd.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;

import java.util.Collection;

public final class Admin {
    private Admin() {}

    public static Collection<ServerPlayer> getAdminPlayers(MinecraftServer server) {
        return server.getPlayerList().getPlayers().stream().filter(player -> player.permissions().hasPermission(Permissions.COMMANDS_ADMIN)).toList();
    }
}
