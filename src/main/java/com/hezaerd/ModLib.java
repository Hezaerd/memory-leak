package com.hezaerd;

//? if >=1.21.11 {
/* import net.minecraft.resources.Identifier;
 *///?} else {
import net.minecraft.resources.ResourceLocation;
//?}
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ModLib {
    private ModLib() {}

    public static final String MOD_ID = "ml";

    public static final Logger Logger = LoggerFactory.getLogger(MOD_ID);

    //? if >=1.21.11 {
    /* public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
    *///?} else {
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
    //?}
}