# Raster Image Editor 📷

## 📌 Описание
Проектът "Raster Image Editor" представлява конзолен редактор за растерни изображения в PPM, PGM и PBM формати. Позволява зареждане, обработка и запазване на изображения чрез команди, изпълнявани в терминала.

## 🧩 Java Класове

### `ImageProcessor.java`
- Основен клас за работа с изображение
- Зарежда, визуализира, обработва и записва `.ppm` изображения
- Поддържа пълна история на промените чрез `undo`
- Включва всички трансформации: геометрични, цветови и формални

### `Session.java`
- Представлява една сесия с едно или повече изображения
- Следи текущото изображение
- Поддържа групови операции (`applyToAll...`)
- Използва се от `SessionManager` за достъп и превключване

### `SessionManager.java`
- Управлява множество сесии
- Създава нови сесии с `open` и `load`
- Позволява `switch <id>`, `add`, `close`, `session info`
- Държи текущата активна сесия

### `RasterImageEditor.java`
- Главен контролер и потребителски интерфейс
- Обработва командите чрез `processCommand()`
- Свързва всички класове в работеща конзолна програма

  
## ⚡ Функционалности
- 📂 **Зареждане** на изображения
- 🔍 **Преглед** на изображения в терминала
- 🎨 **Преобразуване** и редактиране на изображения
- 💾 **Запазване** на редактираните файлове
- 🔄 **Управление на сесии**
- 🆘 **Помощна информация за командите**

## 🚀 Инсталация
1. **Клониране на репозиторито**:
   ```sh
   git clone https://github.com/Kamelia576/my-project-tu.git
   cd my-project-tu
   ```
2. **Компилиране на кода**:
   ```sh
   javac -d out -sourcepath src src/RasterImageEditor.java
   ```
3. **Стартиране**:
   ```sh
   java -cp out RasterImageEditor
   ```

## 🖥️ Команди
| Команда | Описание |
|---------|-------------|
| `load <file>` | Зарежда изображение в сесията |
| `open <file>` | Отваря изображение |
| `view` | Преглежда текущото изображение |
| `session info` | Показва информация за сесията |
| `save`| Записва изображението под текущото му име |
| `saveas <file>`| Записва изображението под ново име (в `src/`, ако не е посочена директория) |
| `invert`| Инвертира цветовете на изображението (само за P3 формат) |
| `crop <x> <y> <width> <height>`| Изрязва част от изображението, започвайки от координати (x, y) с ширина и височина |
| `rotate`| Завърта изображението на 90 градуса по часовниковата стрелка |
| `grayscale`| Преобразува изображението в скала на сивото (само за P3 формати) |
| `monochrome`| – преобразува P2 изображения в черно-бели (0 или 255)|
| `negative`| – инвертира стойностите на пикселите (всички формати)|
| `flipH` | Обръща изображението хоризонтално (ляво-дясно) |
| `flipV` | Обръща изображението вертикално (отгоре-надолу) |
| `convert P1` | Конвертира изображение от формат P3 към черно-бяло (P1) |
| `convert P2` | Конвертира изображение от формат P3 към сиво (grayscale, P2) |
|`undo` – възстановява предишно състояние (вътрешна история)|
|`collage <direction> <image1> <image2> <outimage>` – създава ново изображение от две с еднакви размери и формат|
| `exit` | Излиза от програмата |
| `help` | Показва всички налични команди |


## 🛠️ Разработчици
- 👩‍💻 **Kamelia576** - Основен разработчик

