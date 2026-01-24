package com.hezaerd.commands;

import com.hezaerd.ModLib;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public final class MemoryLeakCommandManager {
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(LeakCommand.register());
        ModLib.Logger.info("Successfully register commands");
    }
}
