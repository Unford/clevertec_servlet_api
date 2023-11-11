package org.example;


/*
    1. Разработать API для выполнения CRUD операций с пользователями
    2. Разработать API для выполнения CRUD операций с ролями пользователя
    3. Реализовать логирование входящих запросов целиком
    4. Доступ к изменению ролей должен быть только у пользователей с ролью ADMIN
    5. Список ролей пользователя нужно получать из сессии
    6. Если список ролей пользователя изменился во время активной сессии,
    список ролей должен измениться в самой сессии
    7. Если пользователь был удалён админом, его сессия должна стать невалидной
 */

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}