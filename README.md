# SIW movie - La vendetta 2

Progetto assegnato dal professore per il corso di Sistemi Informativi su Web.


UC1: Inserimento dati di un nuovo film da parte dell'amministratore

	1. L'amministratore accede all'interfaccia di amministrazione
	2. L'amministratore seleziona l'opzione per l'inserimento di un nuovo film
	3. Il sistema mostra l'elenco di dati da inserire (titolo, anno, immagine)
	4. L'amministratore inserisce i dati e preme il bottone "conferma"
	5. Il sistema verifica e li memorizza nel database
	6. Il sistema mostra un messaggio di conferma dell'avvenuto inserimento
	

UC2: Aggiornamento delle informazioni di un film da parte dell'amministratore

	1. L'amministratore accede all'interfaccia di amministrazione
	2. L'amministratore seleziona l'opzione per l'aggiornamento di un film
	3. L'amministrazione apporta le modifiche desiderate alle informazioni del film (titolo, anno di uscita, regista, attori, recensioni)
	4. Il sistema verifica e aggiorna le informazioni del database
	5. Il sistema mostra un messaggio di conferma dell'avvenuto aggiornamento


UC3: Cancellazione di un artista da parte dell'amministratore

    1. L'amministratore accede all'interfaccia di amministrazione
    2. L'amministratore seleziona l'opzione per la cancellazione di un artista
    3. Il sistema mostra l'elenco degli artisti presenti nel database
    4. L'amministratore seleziona l'artista da cancellare
    5. Il sistema verifica e cancella l'artista dal database, insieme a tutte le sue partecipazioni ai film
    6. Il sistema mostra un messaggio di conferma dell'avvenuta cancellazione

UC4: Inserimento di una recensione da parte di un utente registrato

	1. L'utente registrato accede all'interfaccia di visualizzazione dei dettagli di un film
	2. L'utente seleziona l'opzione per l'inserimento di una recensione
	3. L'utente inserisce il titolo, la valutazione complessiva(da 1 a 5) e il testo della recensione
	4. Il sistema verifica i dati inseriti e li associa al film nel database
	5. Il sistema mostra un messaggio di conferma dell'avvenuto inserimento della recensione

UC5: Consultazione di informazioni dei film nel database da parte di un utente generico

    1. L'utente generico accede all'interfaccia generale del sistema
    2. L'utente seleziona l'opzione per la visualizzazione dei dettagli di un film
    3. Il sistema mostra l'elenco dei film presenti nel database
    4. L'utente seleziona il film di cui vuole visualizzare i dettagli
    5. Il sistema mostra le informazioni del film (titolo, anno di uscita, regista, attori, recensioni)

UC6: Consultazione di informazioni degli artisti nel database da parte di un utente generico

    1. L'utente generico accede all'interfaccia generale del sistema
    2. L'utente seleziona l'opzione per la visualizzazione dei dettagli di un artista
    3. Il sistema mostra l'elenco degli artisti presenti nel database
    4. L'utente seleziona l'artista di cui vuole visualizzare i dettagli
    5. Il sistema mostra le informazioni dell'artista (nome, cognome, data di nascita, data di morte, film in cui ha recitato)