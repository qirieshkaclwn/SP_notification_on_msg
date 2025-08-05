package qir.clwn.chat.info_ls_ms;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class Info_ls_msModMenuApiImpl implements ModMenuApi {

    private static float soundVolume = 1.0f;
    private static float soundPitch = 1.0f;

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.of("Настройки Info_ls_ms"))
                    .setSavingRunnable(() -> {
                        // Здесь сохраняйте свои значения конфигурации
                        System.out.println("Config saved: Volume = " + soundVolume + ", Pitch = " + soundPitch);
                    });

            ConfigCategory general = builder.getOrCreateCategory(Text.of("general"));
            if (general == null) {
                general = builder.getOrCreateCategory(Text.of("Sound Settings"));
            }

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

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

            return builder.build();
        };
    }

    public static float getSoundVolume() {
        return soundVolume;
    }

    public static float getSoundPitch() {
        return soundPitch;
    }
}