package qir.clwn.chat.info_ls_ms.client;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import qir.clwn.chat.info_ls_ms.Info_ls_msModMenuApiImpl;
import net.minecraft.util.Identifier;
import net.minecraft.client.sound.SoundManager;
import qir.clwn.chat.info_ls_ms.DynamicSoundPack;
import net.minecraft.sound.SoundEvent;

public class Info_ls_msClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Перестраиваем динамический ресурс-пак на старте клиента (подхватить новые .ogg)
        DynamicSoundPack.rebuild();
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
                            switch (Info_ls_msModMenuApiImpl.getSoundMode()) {
                                case NOTE_BLOCK -> {
                                    client.world.playSound(
                                            pos.x, pos.y, pos.z,
                                            SoundEvents.BLOCK_NOTE_BLOCK_HARP.value(),
                                            SoundCategory.RECORDS,
                                            Info_ls_msModMenuApiImpl.getSoundVolume(),
                                            Info_ls_msModMenuApiImpl.getSoundPitch(),
                                            false
                                    );
                                }
                                case PREPACKAGED -> {
                                    playBundled(client, Info_ls_msModMenuApiImpl.getPrepackagedSoundId(), Info_ls_msModMenuApiImpl.getSoundVolume(), Info_ls_msModMenuApiImpl.getSoundPitch());
                                }
                                case CUSTOM_FILE -> {
                                    playCustomFile(client, Info_ls_msModMenuApiImpl.getCustomFileName(), Info_ls_msModMenuApiImpl.getSoundVolume(), Info_ls_msModMenuApiImpl.getSoundPitch());
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void playBundled(MinecraftClient client, String id, float volume, float pitch) {
        Identifier soundId = Identifier.of("info_ls_ms", "bundled/" + id);
        SoundManager soundManager = client.getSoundManager();
        SoundEvent event = SoundEvent.of(soundId);
        soundManager.play(PositionedSoundInstance.master(event, volume, pitch));
    }

    private void playCustomFile(MinecraftClient client, String fileName, float volume, float pitch) {
        if (fileName == null || fileName.isBlank() || fileName.startsWith("<")) {
            return;
        }
        String base = stripExtension(fileName);
        Identifier soundId = Identifier.of("info_ls_ms", "custom/" + base);
        SoundEvent event = SoundEvent.of(soundId);
        client.getSoundManager().play(PositionedSoundInstance.master(event, volume, pitch));
    }

    private String stripExtension(String name) {
        int dot = name.lastIndexOf('.');
        return dot > 0 ? name.substring(0, dot) : name;
    }
}
