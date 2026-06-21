#!/bin/bash

# Перечень пакетов, которые нужно перенести из server в gateway
# Если пакет есть в обоих местах, мы копируем содержимое из server в gateway
BASE_DIR="server/src/main/java/ru/practicum/shareit"
TARGET_DIR="gateway/src/main/java/ru/practicum/shareit"

echo "Начинаем перенос DTO и Exceptions..."

# Список модулей, в которых лежат DTO и Exception
MODULES=("booking" "item" "request" "user" "common")

for module in "${MODULES[@]}"; do
    # Перенос DTO
    if [ -d "$BASE_DIR/$module/model/dto" ]; then
        echo "Копирую DTO из $module..."
        mkdir -p "$TARGET_DIR/$module/model/dto"
        cp -r "$BASE_DIR/$module/model/dto/"* "$TARGET_DIR/$module/model/dto/"
    fi

    # Перенос Exceptions
    if [ -d "$BASE_DIR/$module/exception" ]; then
        echo "Копирую Exceptions из $module..."
        mkdir -p "$TARGET_DIR/$module/exception"
        cp -r "$BASE_DIR/$module/exception/"* "$TARGET_DIR/$module/exception/"
    fi
done

# Перенос специфичных для common папок (например, validation, serializing)
cp -r "$BASE_DIR/common/validation/"* "$TARGET_DIR/common/validation/" 2>/dev/null
cp -r "$BASE_DIR/common/serializing/"* "$TARGET_DIR/common/serializing/" 2>/dev/null

echo "Готово! Не забудь обновить импорты (package) в перенесенных файлах, если они изменились."