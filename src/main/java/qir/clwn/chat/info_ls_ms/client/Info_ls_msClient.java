package qir.clwn.chat.info_ls_ms.client;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import qir.clwn.chat.info_ls_ms.Info_ls_msModMenuApiImpl;

public class Info_ls_msClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Регистрация событий для получения сообщений чата
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (message.getString().contains("§f>>")) {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.player != null && client.world != null) {
                    String localPlayerName = client.player.getGameProfile().getName();
                    String fullMessage = message.getString();
                    String cleanMessage = fullMessage.replaceAll("§.", "");
                    int arrowIndex = cleanMessage.indexOf(">>");
                    if (arrowIndex > 0) {
                        String senderName = cleanMessage.substring(0, arrowIndex).trim();
                        if (!senderName.equals(localPlayerName)) {
                            Vec3d pos = client.player.getPos();
                            // Используем громкость и pitch из конфига
                            client.world.playSound(
                                    pos.x, pos.y, pos.z,
                                    SoundEvents.BLOCK_NOTE_BLOCK_HARP.value(),
                                    SoundCategory.RECORDS,
                                    Info_ls_msModMenuApiImpl.getSoundVolume(),   // Громкость из конфига
                                    Info_ls_msModMenuApiImpl.getSoundPitch(),    // Pitch из конфига
                                    false
                            );
                        }
                    }
                }
            }
        });
    }
}
