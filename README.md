# solar-test

Веб-сервис сбора статистики по заданным ключевым словам через на StackOverflow.

## Требования

1. Обслуживать HTTP запросы по URL "/search". В параметрах запроса передается параметр "tag", содержащий ключевой тэг для поиска. Параметров может быть несколько, в этом случае мы работаем с несколькими ключевыми тэгами. Пример "http://localhost:8080/search?tag=clojure&tag=scala". Предполагаем, что клиент будет передавать только алфавитно-цифровые запросы в ASCII. Однако, наличие корректной поддержки русского языка в кодировке UTF-8 будет плюсом.
2. Сервис должен обращаться к REST API StackOverflow для поиска [документация по API](https://api.stackexchange.com/docs/search). В случае, если ключевых слов передано больше одного, запросы должны выполняться параллельно (по одному HTTP запросу на ключевое слово). Должно быть ограничение на максимальное количество одновременных HTTP-соединений, это значение нельзя превышать. Если ключевых слов больше, нужно организовать очередь обработки так, чтобы более указанного количество соединений не открывалось.
3. По каждому тэгу ищем только первые 100 записей, отсортированных по дате создания. Пример запроса к API: https://api.stackexchange.com/2.2/search?pagesize=100&order=desc&sort=creation&tagged=clojure&site=stackoverflow. Можно использовать любые дополнительные параметры запроса, если это необходимо.
4. В результатах поиска интересует полный список тегов (поле tags) по каждому вопросу, а также был ли дан на вопрос ответ.
5. В результате работы запроса должна быть возвращена суммарная статистика по всем тэгам - сколько раз встречался тег во всех вопросах и сколько раз на вопрос, содержащий тэг, был дан ответ.
6. Результат должен быть представлен в формате JSON. Выдача ответа с человеко-читаемым форматированием (pretty print) будет рассматриваться как плюс. Пример ответа:
```json
{
  "clojure": { "total": 173, "answered": 54},
  "python": { "total": 100, "answered": 9},
  "clojurescript": { "total": 193, "answered": 193}
}
```

## Использование

Проект можно запустить командой
```
lein run
```

Либо скомпилировать и запустить jar-файл
```
lein deps
lein uberjar
java -jar target/uberjar/solar-test-0.1.0-SNAPSHOT-standalone.jar
```

### Настройки

Конфигурационный файл можно найти здесь: "solar-test/resources/config.edn". Поля в нем следующие:
```edn
{
 :server-port 8080
 :max-connections 42
 :search-timeout 10000
 :worker-timeout 11000
 :target-api {
              :endpoint "https://api.stackexchange.com/2.2/search"
              :pagesize 10
              :sort "creation"
              :order "desc"
              :site "stackoverflow"
             }
}
```

## Что неплохо бы добавить

* Валидацию конфига и параметров запроса по спеке.
* Graceful shutdown.
* Makefile, Dockerfile для удобства.
* Простую веб-морду.
* Тесты.
