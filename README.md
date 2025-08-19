# Info_ls_ms – Мод для уведомлений о личных сообщениях
*(Mod for private‑message notifications)*

---

## RU (Русский)

Мод добавляет звуковые уведомления при получении личных сообщений в чате.  
Он написан специально для приватного сервера «СП» и реагирует на команду `/msg`.  
При получении сообщения воспроизводится выбранный звук, громкость и тон которого можно изменить в меню мода.

### 1. Режимы звука

| Режим          | Описание                                                                                 | Настройки                         |
|----------------|------------------------------------------------------------------------------------------|-----------------------------------|
| **NOTE_BLOCK** | Стандартный звук нотного блока Minecraft                                               | Громкость, тон                    |
| **CUSTOM_FILE**| Выберите MP3 или OGG файл из папки `config/info_ls_ms/sounds`<br>MP3 файлы автоматически конвертируются в OGG | Путь к файлу, громкость           |
| **PREPACKAGED**| Встроенные звуки, поставляемые вместе с модом                                            | Громкость                         |

### 2. Предустановленные (пакетные) звуки

| Файл          | Описание                                               | Источник                                               | Лицензия                  |
|---------------|--------------------------------------------------------|--------------------------------------------------------|---------------------------|
| `wood_stick.ogg`   | WoodPlankSmack by NightDrawr                           | https://freesound.org/s/819536/                         | Creative Commons 0        |
| `champagne.ogg`    | Open or Uncork Champagne Bottle 2 by Centraca          | https://freesound.org/s/818931/                         | Creative Commons 0        |


### Установка

1. Поместите файл мода в папку `mods`.
2. Запустите игру.
3. Откройте настройки через ModMenu и выберите режим звука.

### Настройка пользовательских звуков

1. Скопируйте MP3‑ или OGG‑файлы в папку `config/info_ls_ms/sounds`.
2. Выберите режим **CUSTOM_FILE** в настройках мода.
3. В выпадающем списке выберите нужный файл.

### Технические детали

* Динамический ресурс‑пак создаётся автоматически в `resourcepacks/info_ls_ms_dynamic`.
* MP3 файлы конвертируются в WAV (после чего переименовываются в OGG для совместимости).
* Ресурс‑пак пересобирается при запуске клиента и после сохранения настроек.

---

## EN (English)

This mod is written for the private Minecraft server (“СП”) and adds notifications to direct messages (/msg command). When a direct message is received, the notification sound is played. The volume and tone of the sound can be changed in the mod menu.

### 1. Sound Modes

| Mode          | Description                                                                               | Settings                         |
|---------------|-------------------------------------------------------------------------------------------|-----------------------------------|
| **NOTE_BLOCK**| Uses the standard Minecraft note block sound                                              | Volume, pitch                    |
| **CUSTOM_FILE**| Pick an MP3 or OGG file from `config/info_ls_ms/sounds`<br>MP3 files are automatically converted to OGG | File path, volume           |
| **PREPACKAGED**| Built‑in sounds that ship with the mod                                                    | Volume                            |

### 2. Prepackaged (Bundled) Sounds

| File          | Description                                            | Source                                                | License                     |
|---------------|--------------------------------------------------------|-------------------------------------------------------|-----------------------------|
| `wood_stick.ogg`   | WoodPlankSmack by NightDrawr                           | https://freesound.org/s/819536/                         | Creative Commons 0         |
| `champagne.ogg`    | Open or Uncork Champagne Bottle 2 by Centraca          | https://freesound.org/s/818931/                         | Creative Commons 0         |


### Installation

1. Place the mod in the `mods` folder.
2. Launch the game.
3. Open the mod settings via ModMenu and choose a sound mode.

### Configuring Custom Sounds

1. Put MP3 or OGG files into `config/info_ls_ms/sounds`.
2. Select **CUSTOM_FILE** mode in the mod settings.
3. Pick the desired file from the drop‑down list.

### Technical Details

* A dynamic resource pack is automatically created under `resourcepacks/info_ls_ms_dynamic`.
* MP3 files are converted to WAV, then renamed to OGG for compatibility.
* The resource pack is rebuilt at client launch and whenever settings are saved.

---

## Creators / Создатели

![This is an image](https://vzge.me/bust/qirieshka.png)

**qirieshka – Programmer / Программист**

> Specially for [#СП]/Специально для [#СП](https://spworlds.ru/) 
