package net.asodev.islandutils;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.MusicUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class IslandutilsClient implements ClientModInitializer {
    public static KeyMapping previewKeyBind;

    @Override
    public void onInitializeClient() {
        previewKeyBind = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.islandutils.preview", // The translation key of the keybinding's name
                InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_P, // The keycode of the key
                "category.islandutils.keys" // The translation key of the keybinding's category.
        ));

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> {
            dispatcher.register(ClientCommandManager.literal("resetmusic").executes(ctx -> {
                MusicUtil.resetMusic(ctx);
                return 1;
            }));
        });
    }

    public static void onJoinMCCI() {
        if (IslandUtils.availableUpdate != null) {
            ChatUtils.send("Hey! Update " + IslandUtils.availableUpdate.title() + " is available for Island Utils!");

            Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, IslandUtils.availableUpdate.releaseUrl()));
            Component link = Component.literal(IslandUtils.availableUpdate.releaseUrl()).setStyle(style);
            Component text = Component.literal(ChatUtils.translate(ChatUtils.prefix + " Download Here: &f")).append(link);

            ChatUtils.send(text);
        }
        DiscordPresenceUpdator.create();
    }
}
