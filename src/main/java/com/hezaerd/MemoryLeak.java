package com.hezaerd;

import com.hezaerd.commands.MemoryLeakCommandManager;
import com.hezaerd.utils.Leak;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemoryLeak implements ModInitializer {
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
			MemoryLeakCommandManager.registerCommands(dispatcher);
		}));

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			Leak.processTick();
		});

		ModLib.Logger.info("MemoryLeak initialized successfully");
	}
}