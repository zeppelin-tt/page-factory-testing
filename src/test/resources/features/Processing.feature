# language:ru
Функционал: Тестирование эмулятора процессига

  @ymtest
  Сценарий: Просмотр результатов поиска
    * пользователь переходит на страницу "http://localhost:3000" по ссылке
    * открывается страница "Главная страница процессинга"
    * проверяет содержимое выпадающего списка "Тип операции"
      |Создать счет|
      |Закрыть счет|
      |Заблокировать счет|
      |Снять деньги|
      |Пополнить счет|
      |Перечислить клиенту|
      |Перечислить клиенту|

    * значение поля "Тип операции" равно "Создать счет"
    * пользователь (выбирает тип операции) "Перечислить клиенту"

#    * пользователь (открывает новый счет)

