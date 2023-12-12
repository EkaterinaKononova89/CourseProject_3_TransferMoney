### Здравствуйте!

#### Команда запуска в терминале: 
- _docker-compose up -d_

#### Запускается на порту 5500.

#### Отправляла следующие запросы:

1. Перевод с карты **1234567890123456**, **01/25**, **123**, на карту **2345678901234567**, сумма **10_000**;\
     _(Изначально баланс карты отправителя 100_000, получателя 150_000. После операции 89_900 и 160_000 соответственно)_\
     **"Успешно"**
2. Перевод с карты **1234567890123456**, **01/25**, **123**, на карту **2345678901234567**, сумма **90_000**;\
   _(Баланс карты отправителя 89_900, получателя 160_000. После операции не изменился)_\
   **"Ошибка"** т.к. недостаточно средств.
3. Перевод с карты **1234567890123456**, **01/25**, **123**, на карту **2345678901234567**, сумма **89_000**;\
   _(Баланс карты отправителя 89_900, получателя 160_000. После операции 10 и 249_000 соответственно)_\
   **"Успешно"**
4. Перевод с карты **2345678901234567**, **02/25**, **234**, на карту **1234567890123456**, сумма **240_000**;\
   _(Баланс карты отправителя 249_000, получателя 10. После операции 6_600 и 240_010 соответственно)_\
   **"Успешно"**
5. Перевод с карты **1234567890123456**, **01/25**, **123**, на карту **3456789012345678**, сумма **10_000**;\
   _(Баланс карты отправителя 240_010, получателя 200_000. После операции не изменился)_\
   **"Ошибка"** т.к. разные валюты.
6. Перевод с карты **1111111111111111**, **01/25**, **123**, на карту **3456789012345678**, сумма **10_000**;\
   **"Ошибка"** т.к. карта отправителя не найдена.
7. Перевод с карты **1234567890123456**, **01/25**, **123**, на карту **2222222222222222**, сумма **10_000**;\
   _(Баланс карты отправителя 240_010. После операции не изменился)_\
   **"Ошибка"** т.к. карта получателя не найдена.