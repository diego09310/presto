# Presto
A speed contest to guess the song (using Spotify playlists)

## Implementation details
The game uses the Spotify API to control the music.  
It uses websockets for part of the communication between the users and the server (when a user has clicked the button, when a new game has started...).  

## Endpoints
### Host
- `/host/spotifyLogin`  
![Spotify Login screenshot](images/spotifyLogin.png)
- `/host/dashboard`  
![Dashboard song hidden screenshot](images/dashboardHid.png)  
![Dashboard song displayed screenshot](images/dashboard.png)

### Player
- `/player/login`  
![Player login screenshot](images/login.png)

- `/player/waitingRoom`  
![Player waiting room screenshot](images/waitingRoom.png)

- `/player/button`  
![Player button screenshot](images/button.jpg)  
![Player button clicked screenshot](images/buttonClicked.jpg)

### Kiosk
- `/kiosk`  
![Kiosk screenshot](images/kiosk.png)
