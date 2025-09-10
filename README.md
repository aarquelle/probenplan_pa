# Probenplan
Ein kleines Tool, um den Probenplanerstellungs- und -bearbeitungsprozess nicht ganz so ätzend zu machen.

## Voraussetzungen:
Momentan werden nur Linux und Windows unterstützt, eine Version für MacOS kommt sehr bald, ich konnte es aber noch nicht testen.
Ein Java Development Kit Version 21 oder höher wird benötigt, um das Programm auszuführen. 

### Linux
Auf Linux installierst du es am Besten einfach über den Paketmanager deiner Distribution.

### Windows 
Lade den passenden Installer von https://www.oracle.com/java/technologies/downloads/#jdk21-windows herunter.

![Screenshot von vom obigen Link](readme_files/winJava.png)

Achte auf die richtige Version, und das richtige Betriebssystem! Neuere Versionen sind in Ordnung, aber es sollte mindestens JDK 21 sein. Lade den Installer herunter, führe ihn aus und klicke dich durch die Installation.

## Installation und Start

Klicke rechts im Browser auf den neuesten release.

<img width="344" height="569" alt="image" src="https://github.com/user-attachments/assets/9ed9a089-c296-4d07-b015-4a933e9fb912" />

Wenn du Linux benutzt, lade "probenplan_linux.zip" herunter, entpacke das Archiv und führe `run_on_linux.command` aus.
Wenn du Windows benutzt, lade "probenplan_pa.exe" herunter und führe die Datei aus. Beim ersten Mal ausführen kommt wahrscheinlich eine Fehlermeldung wie "Der Computer wurde durch Windows geschützt." Einfach auf "Weitere Informationen", dann auf "Trotzdem ausführen" klicken.

## Verwendung:

Man kann zwischen verschiedenen Tabs wechseln. Im Tab "Termine" können neue Schauspielenden und neue Probentermine erzeugt werden. Klickt man auf die Zellen der Tabelle, die sich dadurch ergibt, lässt sich einstellen, wer an welchen Tagen Zeit hat. Die Tooltips erklären die Bedeutung der Farben. Im Tab "Szenen" können neue Szenen und neue Rollen hinzugefügt werden. In den Zellen kann festgelegt werden, welche Rollen in welchen Szenen vorkommen. Durch Klick auf die Zeilen/Spaltennamen lassen sich die Einträge auch wieder löschen oder bearbeiten. Im Tab "Plan" kann dann ein Probenplan generiert und dann exportiert werden. Die Farben der Zellen stellen hier dar, wie gut sich eine Szene an diesem Tag proben lässt: In den Tooltips kann man sehen, welche benötigten Schauspieler an diesem Termin fehlen würden. Im Tab "Parameters" lassen sich verschiedene Generierungsparameter einstellen. Änderungen treten allerdings erst in Kraft, wenn der Knopf "Save changes" gedrückt wurde.

### Zur "Länge" der Szenen:
Jeder Szene kann eine Dezimalzahl als "Länge" zugewiesen werden. Standardmäßig versucht das Programm, den Probenplan so zu erstellen, dass die Summe aller in einer Probe geprobten Szenen ungefähr `1` ist. Dieser Zielwert kann mit dem Parameter `averageRehearsalLength` verändert werden. Eine gute Möglichkeit ist zum Beispiel, jeder Szene als "Länge" die Anzahl ihrer Seiten zu geben, und dann abzuschätzen, wie viele Seiten man wohl in einer Probe schafft.

<!--
### Zum Einfügen von table-Daten:
Die `import-` Befehle, um schnell größere Datenmengen zu importieren, verlangen "table-Daten" im Zwischenspeicher/Clipboard. Gemeint ist damit, dass Daten in einem Tabellenkalkulationsprogramm wie LibreOffice Calc, Excel oder Google Sheets markiert und kopiert werden. Diese Daten müssen dann **nicht** eingefügt werden, solange sie im Zwischenspeicher sind, könnt ihr den Befehl einfach eingeben und `ENTER` drücken. Also: Um zum Beispiel die Szenen zu importieren: Daten in einer Tabelle wie unten gezeigt auswählen, kopieren, ins Terminal wechseln und `import-scenes` eintippen und `ENTER` drücken.
#### MacOS:
Es kann vorkommen, dass das Terminal dabei minimiert wird. Das ist kein Problem, rufe es einfach wieder auf.

### Die wichtigsten Kommandos:
- `help` --- Zeigt alle Kommandos mit einer kurzen Beschreibung an.
- `exit` --- Beendet das Programm. Hinweis: Das Programm lässt sich auch mit den Standard-"Interrupt"-Tastenkombination `Ctrl+C` beenden, oder indem man einfach das Terminalfenster schließt.
- `import-times` --- Importiert eine Tabelle aus Schauspielenden und Probenterminen, mit Markierungen, an welchen Tagen sie nicht können: "x" für "gar nicht", "?" für "vielleicht", alles andere für ja. Die Daten müssen sich dabei im Zwischenspeicher befinden.
Beispiel: ![Bild einer Tabelle](beispielcsvs/times.png)
- `import-scenes` --- Importiert eine Tabelle aus Rollen und Szenen: "x" für "wichtig", "?" für "weniger wichtig", alles andere für "kommt nicht vor". In der Spalte neben den Szenennamen ist die Länge der Szene notiert. In der Zeile unter den Rollennamen sind die Schauspieler notiert. Die Daten müssen sich dabei im Zwischenspeicher befinden.
Beispiel: ![Bild einer Tabelle](beispielcsvs/scenes.png)
- `import-locks` --- Importiert eine Tabelle aus Rollen und Szene: "x" heißt, dass diese Szene an diesem Tag auf jeden Fall geprobt werden soll. Das ist zum Beispiel nützlich, um bereits vergangene Proben bei der Generierung zu berücksichtigen. In der Spalte neben den Probenterminen kann festgelegt werden, ob die ganze Probe "gelockt" sein soll – bei einem "x" können auch keine Szenen zu dieser Probe hinzugefügt werden. Die Daten müssen sich dabei im Zwischenspeicher befinden.
Beispiel: ![Bild einer Tabelle](beispielcsvs/locks.png)
- `clear-data` --- Löscht alle Daten.
- `show-data` --- Zeigt den aktuellen Stand der Daten, die du eingegeben hast.
- `possible-overview` --- Zeigt eine Liste aller Szenen und Probentermine an, und wie vollständig sie geprobt werden könnten.
- `generate [iterations] [initial_seed]` --- Generiert einen Probenplan basierend auf den Parametern. Je mehr `iterations` gewählt werden, desto länger dauert die Generierung, aber desto höher ist auch die Chance auf einen besseren Plan. Bei leergelassenem Feld ist der Standardwert 10.000. `initial_seed` bestimmt den Zufallsgenerator, bei gleichem seed kommt immer auch das gleiche Ergebnis raus. Wird das Feld leergelassen, wird ein zufälliger seed gewählt.
- `export-to-clipboard [locks]` --- Generiert Tabellendaten aus dem generierten Probenplan, welche dann in ein Tabellenkalkulationsprogramm kopiert werden können. Wenn `locks` `1` ist, wird eine Tabelle generiert, die für `import-locks` genutzt werden könnte. Wenn `locks` fehlt oder einen anderen Wert hat, wird eine Tabelle generiert, die einen für Menschen gut lesbaren Probenplan enthält. Das heißt: Du generierst einen Probenplan mit `generate`, gibst `save-to-clipboard` ein, und drückst `Ctrl+V` (bzw. `Cmd+V` auf MacOS) oder `Rechtsklick→Einfügen` ein einem Tabellenkalkulationsprogramm wie Google Sheets, um diesen generierten Probenplan als Dokument für die Gruppe veröffentlichen zu können.
- `save` --- Speichert die Daten inklusive des aktuell generierten Probenplans in eine Datei in ~/Documents/Probenplan_PA/probenplan.binary ab.
- `load` --- Lädt die Daten aus der Speicherdatei.

<!--
### Für kleinere Änderungen während des Probenprozesses:
- `add-actor <name>` --- Schauspielerin mit angegebenem Namen hinzufügen.
- `add-rehearsal <date>` --- Probendatum hinzufügen. Datum sollte im Format tt.MM.yyyy oder tt.MM.yy sein.
- `add-role <name>` --- Rolle mit angegebenem Namen hinzufügen.
- `add-scene <name> <length> <index>` --- Szene hinzufügen. Die Länge soll eine Vergleichbarkeit zwischen Szenen bieten, `index` bestimmt die Reihenfolge (Szenen mit niedrigerem Index kommen zuerst im Stück vor).
- `assign-actor <role-name> <actor-name>` --- Rolle einer Schauspielerin zuweisen.
- `edit-actor <old-name> <new-name>` --- Namen einer Schauspielerin ändern.
- `edit-role <old-role-name> <new-role-name>` --- Namen einer Rolle ändern.
- `edit-scene-name <old-scene-name> <new-scene-name>` --- Namen einer Szene ändern.
- `edit-scene-length <scene-name> <length>` --- Ändert die Länge einer Szene.
- `edit-scene-index <scene-name> <index>` --- Ändert den Index einer Szene.
- `set-has-time <actor-name> <rehearsal-date> <x|y|?>` --- Legt fest, ob eine Schauspielerin an einem bestimmten Termin Zeit hat. `x` bedeutet, sie hat keine Zeit, `y` bedeutet, sie hat Zeit, `?` bedeutet, sie ist unsicher, oder dass es ungünstig wäre.
- `set-takes-part <role-name> <scene-name> <x|n|?>` --- Legt fest, ob eine Rolle in einer Szene vorkommt. `x`, wenn sie eine große oder normale Rolle spielt, `?`, wenn sie nur am Rand vorkommt, `n`, wenn sie gar nicht vorkommt.
- `delete-actor <actor-name>` --- Löscht eine Schauspielerin. Die zugewiesenen Rollen bleiben erhalten.
- `delete-role <role-name>` --- Löscht eine Rolle. Die zugeschriebene Schauspielerin bleibt erhalten.
- `lock <scene-name> <rehearsal-date>` --- Setzt eine bestimmte Szene auf eine bestimmte Probe. Dies wird bei der Probenplangenerierung immer berücksichtigt.
- `unlock <scene-name> <rehearsal-date>` --- Macht den obigen Befehl wieder rückgängig.
- `unlock-all` --- Entfernt alle locks auf Szenen und Proben.
-->
<!--
### Finetuning der Generierung
- `show-params` --- Zeigt die aktuellen Werte der Generierungsparameter.
- `set-param <param-name> <value>` --- Legt einen Generierungsparameter auf einen bestimmten Wert fest. Die Standardwerte sollten gut funktionieren, aber du kannst auch mit anderen Werten experimentieren. Wenn du mit anderen Werten bessere Ergebnisse erzielst, teile es mir bitte mit! Die Werte werden nach jedem Neustart des Programms auf die Standardwerte zurückgesetzt.-->
#### Parameter:
Indem man die Parameter verändert, lassen sich eventuell Ergebnisse finden, die besser zur eigenen Situation passen. Spielt hier gerne mit rum, und schreibt mir, wenn ihr bessere Ergebnisse bekommt als mit den Standardwerten!

- `averageRehearsalLength` --- Legt fest, wie lang eine Probe sein soll - gemessen als Summe der Längen der einzelnen Szenen.
- `deadline` --- Legt fest, nach wie vielen erfolglosen Verbesserungsversuchen das Programm den generierten Plan akzeptiert.
- `initialSeed` --- Legt den ersten Zufallsseed fest, wenn bei `generate` kein entsprechendes Argument mitgeliefert wird. Bei gleichem Seed kommen immer die gleichen Ergebnisse raus. Ist der seed 0 (default), wird ein zufälliger seed gewählt.
- `completenessWeight` --- Wie viel Wert darauf gelegt wird, dass die einzelnen Szenen möglichst vollständig sind, also dass möglichst wenige Schauspielende fehlen und eingesprochen werden müssen.
- `dlpCompletenessWeight` --- Wie viel Wert darauf gelegt wird, dass die Durchlaufprobe möglichst vollständig gespielt werden kann.
- `completenessBeforeDLPWeight` --- Wie viel Wert darauf gelegt wird, dass vor der Durchlaufprobe möglichst jede Szene schon vollständig gespielt werden konnte.
- `lumpinessWeight` --- Wie viel Wert darauf gelegt wird, dass in einer Probe möglichst zusammenhängende Szenen geprobt werden. **Hinweis:**  Erst ab 3 verschiedenen "Blöcken" hat das einen negativen Einfluss auf das Ergebnis.
- `minimumRepeatsWeight` --- Wie viel Wert darauf gelegt wird, dass die am seltensten geprobte Szene trotzdem oft geprobt wird.
- `medianRepeatsWeight` --- Wie oben, auf für den Median.
- `averageRepeatsWeight` --- Wie oben, nur für den Durchschnitt.
- `overSizeWeight` --- Wie viel Wert darauf gelegt wird, dass Szenen nicht deutlich länger als die `averageRehearsalLength` sind.
- `numberOfRolesWeight` --- Wie viel Wert darauf gelegt wird, dass nicht so viele Rollen pro Probe dran sind. **Hinweis**: Momentan zählt das Programm nur die Rollen, nicht die Schauspielenden. Wenn eine Schauspielerin also mehrere Rollen hat, kann das an dieser Stelle gerade nicht berücksichtigt werden. Das wird sich in Zukunft verbessern. **Anderer Hinweis**: Momentan werden Proben mit 4 oder weniger Rollen als "ideal" angesehen, unterhalb davon wird nicht unterschieden.

## Fehler:
Das Programm ist noch in einem ziemlich unfertigen Stadium. Man kann es zwar benutzen, aber es gibt bestimmt noch viele Situationen, in denen eine leicht unerwartete Bedienung zum Absturz führt. Bitte schreibt mir solche Fehler (inklusive Fehlermeldungen, und was ihr gemacht habt), damit ich sie fixen kann!

## Weiteres:
Wenn Probleme hast oder Ideen, wie man das Programm noch verbessern kann: Teil es mir gerne mit! Per Mail an prott@fim.uni-passau.de, oder am Besten direkt hier auf Github (indem du ein Issue erstellst). Genauso, wenn du selber gerne mitarbeiten möchtest, der Quellcode ist ja hier auf Github. 
