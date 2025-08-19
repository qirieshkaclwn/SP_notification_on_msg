package qir.clwn.chat.info_ls_ms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;
import net.minecraft.SharedConstants;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicSoundPack {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void rebuild() {
        try {
            Path runDir = MinecraftClient.getInstance().runDirectory.toPath();
            Path packsDir = runDir.resolve("resourcepacks");
            Path packRoot = packsDir.resolve("info_ls_ms_dynamic");
            Path assetsRoot = packRoot.resolve("assets").resolve("info_ls_ms");
            Path soundsDirInPack = assetsRoot.resolve("sounds").resolve("custom");

            Files.createDirectories(soundsDirInPack);

            // 1) pack.mcmeta с корректным pack_format
            int packFormat = SharedConstants.getGameVersion().getResourceVersion(ResourceType.CLIENT_RESOURCES);
            JsonObject mcmeta = new JsonObject();
            JsonObject pack = new JsonObject();
            pack.addProperty("pack_format", packFormat);
            pack.addProperty("description", "Info_ls_ms dynamic sounds");
            mcmeta.add("pack", pack);
            writeJson(packRoot.resolve("pack.mcmeta"), mcmeta);

            // 2) Сканируем config/info_ls_ms/sounds на .mp3 и .ogg
            Path configSounds = FabricLoader.getInstance().getConfigDir().resolve("info_ls_ms").resolve("sounds");
            Files.createDirectories(configSounds);

            List<Path> audioFiles = new ArrayList<>();
            try (var stream = Files.list(configSounds)) {
                audioFiles = stream
                        .filter(Files::isRegularFile)
                        .filter(p -> {
                            String name = p.getFileName().toString().toLowerCase();
                            return name.endsWith(".mp3") || name.endsWith(".ogg");
                        })
                        .collect(Collectors.toList());
            }

            JsonObject soundsJson = new JsonObject();

            for (Path src : audioFiles) {
                String fileName = src.getFileName().toString();
                String base = fileName.substring(0, fileName.lastIndexOf('.'));
                String extension = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();

                Path dst = soundsDirInPack.resolve(base + ".ogg");

                try {
                    Files.createDirectories(dst.getParent());

                    if (extension.equals(".mp3")) {
                        // Пытаемся конвертировать MP3 в WAV, затем переименовываем в OGG
                        convertMp3ToWav(src, dst);
                    } else {
                        // Просто копируем OGG
                        Files.copy(src, dst, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    System.err.println("Failed to process audio file: " + fileName + " - " + e.getMessage());
                    // Если не удалось обработать, пропускаем файл
                    continue;
                }

                // Добавляем запись: info_ls_ms:custom/<base>
                JsonObject def = new JsonObject();
                def.addProperty("category", "master");
                JsonObject soundEntry = new JsonObject();
                soundEntry.addProperty("name", "info_ls_ms:custom/" + base);
                soundEntry.addProperty("stream", false);
                def.add("sounds", GSON.toJsonTree(List.of(soundEntry)));
                soundsJson.add("custom/" + base, def);
            }

            // 3) Пишем sounds.json
            writeJson(assetsRoot.resolve("sounds.json"), soundsJson);
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null) {
                client.reloadResources();
            }
        } catch (Exception e) {
            System.err.println("Failed to rebuild dynamic sound pack: " + e.getMessage());
        }
    }

    private static void convertMp3ToWav(Path mp3File, Path oggFile) throws IOException {
        try {
            // Пытаемся загрузить MP3 через Java Sound API
            AudioInputStream mp3Stream = AudioSystem.getAudioInputStream(mp3File.toFile());

            // Конвертируем в PCM формат
            AudioFormat targetFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                44100, 16, 2, 4, 44100, false
            );

            AudioInputStream convertedStream = AudioSystem.getAudioInputStream(targetFormat, mp3Stream);

            // Сохраняем как WAV
            Path tempWav = oggFile.resolveSibling(oggFile.getFileName().toString().replace(".ogg", ".wav"));
            AudioSystem.write(convertedStream, AudioFileFormat.Type.WAVE, tempWav.toFile());

            // Переименовываем в .ogg (для совместимости с Minecraft)
            Files.move(tempWav, oggFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            mp3Stream.close();
            convertedStream.close();

        } catch (UnsupportedAudioFileException e) {
            // Если MP3 не поддерживается, просто копируем файл и переименовываем
            System.err.println("MP3 format not supported, copying as-is: " + mp3File.getFileName());
            Files.copy(mp3File, oggFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            // При любой ошибке конвертации просто копируем файл
            System.err.println("Conversion failed, copying as-is: " + mp3File.getFileName());
            Files.copy(mp3File, oggFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void writeJson(Path path, JsonElement json) throws IOException {
        Files.createDirectories(path.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write(GSON.toJson(json));
        }
    }
}


