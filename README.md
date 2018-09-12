# Filespells
Filespells - командный интерпретатор, обеспечивающий простейшие средства взаимодействия с файловой системой и рассчитанный на возможность использования множества действий "за один ввод" для написания простейших мини-скриптов. Большинство команд имеет ряд синонимов для удобства запоминания и интуитивного использования.

Перед исполнением каждого набора команд в консоль выводится полный путь текущей цели (**Target**).
Строкой ниже вы увидите имя "взятого" файла или директории, выбранной для копирования или перемещения (**Picked**).

По-умолчанию и цель, и "взятый" элемент - каталог, из которого был запущен интерпретатор.

Если введенное слово не соответствует ни одной из существующих команд - делается попытка установить файл/директорию с таким именем текущей целью. Если вы хотите уверенности, что ни одна команда не совпадает с именами файлов и каталогов, с которыми вам предстоит работать - используйте слеш ( ***/*** ) перед именами.

"Аргумент" - слово ***после*** команды. Если команда использует аргумент - следующее слово не обрабатывается (пропускается).

Заключайте в двойные кавычки все директории с пробельными символами и аргументы из нескольких слов!

Если команда требует директорию, а цель указывает на файл - используется родительская директория файла-цели.

Ниже приведен список базовых команд с пояснениями.

Команда (+синонимы)      |                        Действие                    |    Использует     |  Изменяет
-------------------------|----------------------------------------------------|-------------------|-----------
**size**                 | Вывести размер цели в байтах                       | Target            | -
**show** (list, dir, look) | Вывести содержимое каталога цели                   | Target            | -
**del** (delete, remove) | Удалить цель. Текущая цель меняется на родительскую папку удаленного элемента | Target   | Target
**return** (back)        | Отменить последнее изменение пути цели             | -                 | Target
**last**                 | Отменить последнее изменение пути выбранного файла | -                 | Picked
**log** (write)          | Дописать строку-аргумент в файл-цель                        | Target, аргумент  | -
**read** (open)          | Выводит содержимое файла-цели (может быть полезно только с текстовыми файлами) | Target | -
**pick** (take, choose)  | Помечает файл-цель как "выбранный"                 | Target            | Picked
**copy** (paste, clone)  | Создает копию "выбранного" файла в директории-цели. Текущая цель меняется на созданный файл | Target, Picked            | Target
**move** (replace)       | Перемещает "выбранный" файл в директорию-цель. Текущая цель меняется на новый путь файла | Target, Picked            | Target
**unzip**       | Распаковывает выбранный zip-файл в директорию-цель | Target, Picked            | -
**rename**               | Изменить имя "выбранного" файла на указанное аргументом. Текущая цель меняется на новый путь файла. | Picked, аргумент | Target
**up** (parent, ..)      | Смена текущей цели на ее родительствую директорию ("переход вверх"). ".." при указании путей также может использоваться для достижения того же результата | Target         | Target
**make** (new, create)   | Создать в директории-цели новый файл/директорию с именем, указанным аргументом. Текущая цель меняется на путь созданного элемента | Target, аргумент | Target
**root**                 | Установка корневого каталога текущей целью | -            | Target
**go** (path)      | Замена текущей цели полным путем, указанным аргументом | Аргумент | Target
**home**       | Установить целью директорию, из которой был запущен данный экземпляр FileSpells | -   | Target
**exit** (quit)          | Завершение работы интерпретатора | -            | -

Filespells выполняет переданные при запуске аргументы как единый набор команд. Если вы не хотите, чтобы по его завершении интерпретатор ожидал от вас нового ввода - не забудьте указать **exit** последней командой!

## Живые Примеры

 - Создание нового текстового файла, помещение в него строки, перенос файла в родительскую директорию и уточнение его размера:

***make file.txt log "Hello World!" pick up paste size***

 - Переход в корневой каталог, выбор целью существующей папки "log", ее перенос и переименование:

***root /log take home move take rename "New Log Folder"***

## Логика

Filespells также предоставляет несколько команд для управляемого перемещения по скрипту. Поскольку скрипт представляет собой одну строку, ветвление достигается "скачками" на конкретные слова или пропуском определенного их количества. Стандартный "скачок" - пропуск двух слов (в дополнении к пропуску аргументов, если таковые используются командой).

Команда (+синонимы)      |                        Действие                    
-------------------------|----------------------------------------------------
**goto**                 | Переход на слово под номером, указанным аргументом
**jump**                 | Выполняет скачок
**x**                    | Слово-балласт, не выполняющее действий. Используется при необходимости заполнения пропусков при использовании скачков
**wait** (sleep)         | Остановка выполнения скрипта на 1 секунду
**print** (msg, console) | Вывести в консоль сообщение, указанное аргументом
**ifexists**             | НЕ выполняет скачок, если файл/директория, указанная аргументом, существует в директории-цели
**ifmissing**            | НЕ выполняет скачок, если файл/директория, указанная аргументом, не существует в директории-цели
**stop**                 | Досрочное завершение исполнения команд, пропуск всех последующих слов

## Живые Примеры

 - Поиск файла, вывод его содержимого, в случае отсутствия файла - его создание и запись строки:
 
 ***ifexists file.txt read stop make file.txt log "Brand new!"***
 
 - Убедительная просьба удалить файл:
 
 ***ifmissing file.txt stop x msg "Please delete it already!" wait goto 1***
