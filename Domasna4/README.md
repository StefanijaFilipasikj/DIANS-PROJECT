# Линк до хостирана апликација
- https://timeless-travel-microservice.azurewebsites.net
# Линк до docker репозиториуми
- https://hub.docker.com/r/dejanristovski/main-service
- https://hub.docker.com/r/dejanristovski/landmark-service

# Искористени шаблони
1. **MVC**
   - Имплементиран преку Spring MVC во Spring Boot и обезбедува јасна поделба на грижите во апликацијата. Контролерите се справуваат со интеракциите на корисникот, како што се пребарување на знаменитости или интеракција со картата, ажурирање на основниот модел. Моделот, управуван од singleton услуги, ја опфаќа состојбата на апликацијата и податоците поврзани со историските знаменитости. Секоја промена во моделот предизвикува освежување на приказот, осигурувајќи дека корисничкиот интерфејс точно ја одразува моменталната состојба на апликацијата.
2. **Singleton**
   - Искористен за управување со животниот циклус на биновите (@Bean) во Spring рамката. Поточно, компонентите одговорни за интеракција со OpenStreetMap API и преземање податоци за историски знаменитости се имплементирани како singletons. Ова осигурува дека има само едена инстанца од овие компоненти низ апликацијата, промовирајќи ја ефикасноста на ресурсите и постојаното пребарување на податоци.
3. **Iterator**
   - Шаблонот Iterator е имплементиран преку ArrayList за лесно ификасно прегледување на колекциите на податоци. Овој шаблон обезбедува унифициран интерфејс за последователно прегледување на елементите во колекциите како листи, без откривање на основната претстава на колекцијата. Во оваа апликација, Iterator е користен за прегледување на збирките на historic landmarks.
4. **Builder**
   - Шаблонот Builder е користен преку StringBuilder за креирање комплексни текстуални структури. Овој шаблон е особено корисен за составување на сложени низи на текст. Во апликацијата, StringBuilder е користен за креирање на филтер за пребарување низ базата.
5. **Chain of Responsibility**
   - Шаблонот Chain of Responsibility е имплементиран во Spring Security за управување со безбедносните аспекти на апликацијата. Овој шаблон овозможува низа на филтери (filters) да процесираат барања. Секој филтер има можност да го обработи барањето или да го проследи на следниот филтер во синџирот. Во апликацијата, ова обезбедува автентикација и авторизација преку обработка на http барањата(requests).
6. **Facade**
   - Шаблонот Facade е имплементиран за поедноставување на комуникацијата помеѓу различните слоеви на апликацијата. Овој шаблон обезбедува унифициран интерфејс кој ја апстрахира комплексноста на основните системи. Во апликацијата, Facade се користи за интеграција помеѓу слоевите за податочен пристап, бизнис логика и презентирање, овозможувајќи поедноставена и организирана архитектура.
