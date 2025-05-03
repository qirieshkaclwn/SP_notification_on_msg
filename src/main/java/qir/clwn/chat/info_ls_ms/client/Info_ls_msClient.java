package qir.clwn.chat.info_ls_ms.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class Info_ls_msClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, timestamp) -> {
            if (message.getString().contains(">>")) {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.player != null && client.world != null) {
                    Vec3d pos = client.player.getPos();
                    client.world.playSound(
                            pos.x, pos.y, pos.z,
                            SoundEvents.BLOCK_NOTE_BLOCK_HARP.value(),  // Звук музыкального блока (арфа)
                            SoundCategory.RECORDS,                      // Или .PLAYERS / .BLOCKS
                            1.0f,  // Громкость
                            1.0f,  // Питч (высота звука, от 0.5 до 2.0)
                            false
                    );

                }
            }
        });
    }
}
