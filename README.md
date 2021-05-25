# Mobil alkalmazásfejlesztés beadandó

Ez egy egyszerű üzenetküldő applikáció, Firestore adatbázissal.
Regisztrációt és bejelentkezést követően a *Contacts* menüben látható a többi regisztrált felhasználó.
Bármelyikre kattintva üzenet küldhető neki. A nekünk küldött üzeneteket a *Received messages* oldalon láthatjuk,
az általunk küldött üzeneteket a *Sent messages* oldalon.

Az applikáció megvalósítja a `CommunicationMessage` adatstruktúra CRUD műveleteit.
Frissítés akkor történik, amikor a felhasználó először megnyitja a neki küldött üzenetet. Ekkor frissül az üzenet státusza.

Néhány teszt fiók:

+ Név: **Anthony Burgess**\
  Email: **test1@email.com**

+ Név: **Neumann János**\
  Email: **test2@email.com**

+ Név: **Tarr Béla**\
  Email: **test3@email.com**
  
Jelszó minden esetben: **123456**
