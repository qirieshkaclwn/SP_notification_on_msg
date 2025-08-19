package qir.clwn.chat.info_ls_ms;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import qir.clwn.chat.info_ls_ms.DynamicSoundPack;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class Info_ls_msModMenuApiImpl implements ModMenuApi {

    public enum SoundMode {
        CUSTOM_FILE, // Свой звук (из папки sounds)
        NOTE_BLOCK,  // Нотный блок
        PREPACKAGED  // Заготовленные звуки (в комплекте с модом)
    }

    private static SoundMode soundMode = SoundMode.NOTE_BLOCK;

    // Настройки для режима NOTE_BLOCK
    private static float soundVolume = 1.0f;
    private static float soundPitch = 1.0f;

    // Настройки для режима CUSTOM_FILE
    private static String customFileName = ""; // имя файла из папки sounds

    // Настройки для режима PREPACKAGED
    private static String prepackagedSoundId = ""; // идентификатор предустановленного звука

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.of("Настройки Info_ls_ms"))
                    .setSavingRunnable(() -> {
                        System.out.println("Config saved: Volume = " + soundVolume + ", Pitch = " + soundPitch);
                        DynamicSoundPack.rebuild();
                    });

            ConfigCategory general = builder.getOrCreateCategory(Text.of("general"));
            if (general == null) {
                general = builder.getOrCreateCategory(Text.of("Sound Settings"));
            }

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // Режим работы
            general.addEntry(entryBuilder.startEnumSelector(Text.of("Режим звука"), SoundMode.class, soundMode)
                    .setTooltip(Text.of("Выберите, какой тип звука проигрывать при личном сообщении"))
                    .setSaveConsumer(newValue -> soundMode = newValue)
                    .build());

            // Блок настроек для NOTE_BLOCK
            general.addEntry(entryBuilder.startFloatField(Text.of("Громкость звука"), soundVolume)
                    .setDefaultValue(1.0f)
                    .setTooltip(Text.of("Устанавливает громкость воспроизводимого звука"))
                    .setSaveConsumer(newValue -> soundVolume = newValue)
                    .build());

            general.addEntry(entryBuilder.startFloatField(Text.of("Тон звука"), soundPitch)
                    .setDefaultValue(1.0f)
                    .setTooltip(Text.of("Устанавливает тон воспроизводимого звука"))
                    .setSaveConsumer(newValue -> soundPitch = newValue)
                    .build());

            // Блок настроек для CUSTOM_FILE
            List<String> customFiles = getCustomSoundFiles();
            if (customFiles.isEmpty()) {
                customFiles = Collections.singletonList("<файлы не найдены>");
            }
            general.addEntry(entryBuilder.startStringDropdownMenu(Text.of("Свой звук (файл из папки sounds)"), customFileName)
                    .setTooltip(Text.of("Выберите файл из папки config/info_ls_ms/sounds. Поддерживаются .mp3 и .ogg файлы. MP3 автоматически конвертируются в OGG."))
                    .setSelections(customFiles)
                    .setSaveConsumer(newValue -> customFileName = newValue)
                    .build());




            // Блок настроек для PREPACKAGED
            List<String> bundled = getBundledSoundIds();
            general.addEntry(entryBuilder.startStringDropdownMenu(Text.of("Заготовленные звуки"), prepackagedSoundId)
                    .setTooltip(Text.of("Выберите встроенный звук, идущий с модом"))
                    .setSelections(bundled)
                    .setSaveConsumer(newValue -> prepackagedSoundId = newValue)
                    .build());

            return builder.build();
        };
    }

    private static List<String> getCustomSoundFiles() {
        try {
            Path soundsDir = FabricLoader.getInstance().getConfigDir().resolve("info_ls_ms").resolve("sounds");
            if (!Files.exists(soundsDir)) {
                Files.createDirectories(soundsDir);
            }
            List<String> result = new ArrayList<>();
            try (var stream = Files.list(soundsDir)) {
                stream.filter(Files::isRegularFile)
                        .filter(p -> {
                            String name = p.getFileName().toString().toLowerCase();
                            return name.endsWith(".mp3") || name.endsWith(".ogg");
                        })
                        .forEach(p -> result.add(p.getFileName().toString()));
            }
            Collections.sort(result);
            return result;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private static List<String> getBundledSoundIds() {
        List<String> ids = new ArrayList<>();
        ids.add("wood_stick.ogg");//WoodPlankSmack by NightDrawr -- https://freesound.org/s/819536/ -- License: Creative Commons 0
        ids.add("champagne.ogg");//Open or Uncork Champagne Bottle 2 by Centraca -- https://freesound.org/s/818931/ -- License: Creative Commons 0

        return ids;
    }

    public static float getSoundVolume() {
        return soundVolume;
    }

    public static float getSoundPitch() {
        return soundPitch;
    }

    public static SoundMode getSoundMode() {
        return soundMode;
    }

    public static String getCustomFileName() {
        return customFileName;
    }

    public static String getPrepackagedSoundId() {
        return prepackagedSoundId;
    }
}
