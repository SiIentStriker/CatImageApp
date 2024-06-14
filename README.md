# Random Cat Image App

## Übersicht

Random Cat Image App ist eine Android-Anwendung, die zufällige Bilder von Katzen anzeigt. Zusätzlich kann die App Informationen über Katzenrassen anzeigen und einen QR-Code mit diesen Informationen generieren. 

## Features

- Anzeige eines zufälligen Katzenbildes.
- Anzeige von Informationen über die Katzenrasse, einschließlich Name, Rasse, Temperament, Herkunft, Alter und Lebensdauer.
- Generierung eines QR-Codes mit den Informationen über die Katzenrasse.
- Scannen eines QR-Codes, um die Informationen über eine Katze anzuzeigen.

## Umgesetzte Features

- Anzeige von Katzenbildern und Informationen.
- Generierung von QR-Codes mit Katzeninformationen.
- Scannen von QR-Codes, um die Katzeninformationen anzuzeigen.

## Nicht umgesetzte Features

- Augmented Reality (AR) Funktionalitäten.

## Voraussetzungen

- Android Studio (empfohlene Version 4.1 oder höher)
- Kotlin 1.4 oder höher
- Internetverbindung (für API-Abfragen)

## Abhängigkeiten

Die folgenden Abhängigkeiten werden für das Projekt benötigt:

- Retrofit: für Netzwerkoperationen
- Gson: für die JSON-Verarbeitung
- Glide: für das Laden von Bildern
- ZXing: für die QR-Code-Generierung und -Scannen
- Room: für die lokale Datenbank
  
## Projektstruktur

- **manifests**
  - `AndroidManifest.xml`: Manifest-Datei für die App.

- **kotlin-java**
  - `com.example.randomcatimageapp`
    - `ui.theme`: Theme-Dateien für die App.
    - `ApiService.kt`: Definiert die Schnittstelle für die API-Aufrufe.
    - `CameraActivity.kt`: Aktivität zum Scannen von QR-Codes.
    - `Cat.kt`: Datenklasse für Katzeninformationen.
    - `CatAdapter.kt`: Adapter für die RecyclerView.
    - `CatDao.kt`: Datenzugriffsobjekt für die Cat-Datenbank.
    - `CatDatabase.kt`: Datenbankkonfiguration.
    - `CatDetailActivity.kt`: Aktivität zur Anzeige der Details einer Katze.
    - `CatImageResponse.kt`: Datenklasse für das API-Antwortformat.
    - `MainActivity.kt`: Hauptaktivität der App.
    - `RandomCatInfoGenerator.kt`: Generiert zufällige Katzeninformationen.
    - `RetrofitClient.kt`: Konfiguriert Retrofit für API-Aufrufe.

- **res**
  - `drawable`: Enthält Zeichnungsressourcen.
  - `layout`: Enthält Layout-Dateien.
  - `menu`: Enthält Menü-Ressourcen.
  - `values`: Enthält Werte-Ressourcen wie Farben, Strings und Themes.
  - 
## Verwendete API und Mobilgerät in Android Studio
  - Pixel 8 Pro
  - API Level 31 (Release Name S)
    
## Bekannte Fehler
  - Nach dem Generieren einer Katze und danach die Katzendetails anzuschauen kann zum Crashen der App führen (Nur 1 mal nach dem start der App)
