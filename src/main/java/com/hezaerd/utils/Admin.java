package com.hezaerd.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
//? if >=1.21.11 {
/*import net.minecraft.server.permissions.Permissions;
 *///?}

import java.util.Collection;

public final class Admin {
    private Admin() {}

    public static Collection<ServerPlayer> getAdminPlayers(MinecraftServer server) {
        //? if <1.21.11 {
        return server.getPlayerList().getPlayers().stream().filter(player -> player.hasPermissions(2)).toList();
        //?} else {
        /*return server.getPlayerList().getPlayers().stream().filter(player -> player.permissions().hasPermission(Permissions.COMMANDS_ADMIN)).toList();
         *///?}


    }
}
