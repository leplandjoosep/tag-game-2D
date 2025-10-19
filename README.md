# java kull



## Alustuseks

1. Alla tõmmata projekt
2. Intellij peaks ise pakkuma paremal all nuppu load, millega ta laeb kõik gradled ära, kuid kui millegi pärast seda ei tule, siis vajutada Intellj's paremal 'Notifications' nuppu ja seal peaks olema ka nupp 'Load'
3. Mängu käivitamiseks tuleb minna faili Client/desktop/src/com.javakull.main/DesktopLauncher.java ja seda runida
4. Mängu avades on kaks nuppu, 'quit' ja 'start', kui vajutada starti küsib ta nime ja tekib paremale kõrvale lobby nupp, millega saab lobbysse minna
Kui ka teised mängijad on lobbys saab, lobby tegija (ehk siis esimene inimene, kes lobbysse jõudis) mängu käima panna vajutades 'start' nuppu
5. Kui lobby on täis või mäng käib on võimalik ka mängu vaadata. Kõige alguses starti vajutades, küsib ta jällegi nime, aga 'lobby' nupu asemel on 'spectate'

- Kui tuleb kiri 'Could not connect to server', siis võib meiega ühendust võtta ja paneme serveri uuesti tööle või saab ka mängida lokaalselt enda arvutis

- Et mängida lokaalselt tuleb vahetada IP failis Client/core/src/NewClientConnection/NewClientConnection.java
- Konstruktoris on selline asi nagu: String ip = "193.40.156.195"; see IP tuleks siis panna 127.0.0.1 ehk siis String ip = "127.0.0.1";

## Mängust
Mäng on lihtne tagaajamismäng, kus kull on roosa karakter ja teised on valged karakterid.
Punkti saab siis, kui kull läheb kellelegi vastu ehk saab kellegi kätte ja annab kulli teisele mängijale
Mängu võidab see, kellel on kõige rohkem punkte

## Nupud
Karakter liigub ringi 'wasd' nuppudega ja 's' nuppu vajutades kukub mängija kiiremini alla.
'esc' nupust läheb mäng kinni.
