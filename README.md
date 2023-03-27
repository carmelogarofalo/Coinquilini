# Coinquilini

## Introduzione
Quest’app è pensata per tutte quelle persone che devono confrontarsi con un’esperienza di home sharing.
Permette di creare una casa virtuale oppure nel caso in cui qualcun altro lo abbia già fatto, di loggarvici. 
Una volta dentro sarà possibile effettura delle semplici operazioni come quella di aggiungere 
una bolletta oppure mostrare dove poterle eventualmente pagare. 

## Funzionalità
Inizialmente l’app si apre sulla MainActivity che permette di interagire con 2 
button: uno che permettte di creare una nuova casa inserendo nel Database il 
nome, l’altro permette di effetture il login oppure di spostarsi nell’Activity di 
registrazione. Quest’ultima permette di registrare un’utente inserendo 
all’interno del db il nome utente e la password. Nella fase di login 
successivamente inserendo nome utente, password e nome della casa il 
database estrarrà l’utente, se presente, e setterà l’id della casa con quello 
associato al nome inserito. Una volta effettuato il login l’app ti porterà nell 
HomeActivity che tramite un’interrogazione al db mostrerà a schermo il nome 
della casa ed i coinquilini presenti. Da questa activity si potrà scegliere di 
effettuare 2 operazioni: passare alla FotoActivity, dove si potranno visualizzare 
tutte le bollette inserite oppure inserirne una nuova, oppure passare alla 
MapsActivity dove si potrà visualizzare la posizione corrente e poter 
visualizzare le poste,banche o tabacchini vicini.

## Database
L’app si collegherà ad un database esterno (utilizzando phpmyadmin), situato sulla macchina stessa, per 
poter permettere a chiunque di potersi collegare. Esso è composto da 3 tabelle: utenti, casa e info_casa.

## Foto
Le activity dedicate alle foto sfruttano il TextRecognizer dell’ML kit di google. 
Per prima cosa viene scattata una foto, avendo precedentemente acconsentito 
I permessi alla fotocamera e allo storage, per poi chiamare il metodo recognizeTextFromImage().

## Mappa
La MapsActivity sfrutta le api di google maps per poter visualizzare la mappa. 
Viene effettuata anche una richiesta https per poter ricevere, sotto forma di 
json, le posizioni dei luoghi richieste.
