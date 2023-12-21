document.getElementById("addTeamForm").addEventListener('submit', (event) => {
    event.preventDefault();
    const teamName = document.getElementById("teamName").value;
    fetch(`/host/addTeam?teamName=${teamName}`);
    event.target.reset();
});

function addDeleteButtonsListeners() {
    const deleteButtons = document.getElementsByClassName("delete-button");
    for (let button of deleteButtons) {
        const teamId = button.getAttribute("data-teamId");
        const playerId = button.getAttribute("data-playerId");
        button.addEventListener('click', () => {
            if (playerId == null) {
                fetch(`/host/deleteTeam?teamId=${teamId}`);
            } else {
                fetch(`/host/deletePlayer?playerId=${playerId}&teamId=${teamId}`);
            }
        });
    }
}

addDeleteButtonsListeners();

let spotifyPlaying = false;

if (document.getElementsByClassName("spotify-wrapper").length > 0) {
    document.getElementById("prev-song").addEventListener('click', () => {
        fetch('/spotify/prev');
        refreshSpotify();
    });
    document.getElementById("play-pause-song").addEventListener('click', () => {
        fetch('/spotify/toggle');
        spotifyPlaying = !spotifyPlaying;
        spotifyPlaying ? setPauseLogo() : setPlayLogo();
    });
    document.getElementById("next-song").addEventListener('click', () => {
        fetch('/spotify/next');
        refreshSpotify();
    });
}

document.getElementById("game-start").addEventListener('click', () => {
    const playlistId = document.getElementById("playlists").value;
    fetch(`/host/game/start?playlist=${playlistId}`);

    document.getElementById("game-start").classList.add("hidden");
    document.getElementById("game-continue").classList.remove("hidden");
    document.getElementById("game-next").classList.remove("hidden");
});
document.getElementById("game-continue").addEventListener('click', () => fetch('/host/game/continue'));
document.getElementById("game-next").addEventListener('click', () => fetch('/host/game/next'));
// document.getElementById("game-pause").addEventListener('click', () => fetch('/host/game/pause'));

function handleWebSocketMessage(type, data) {
    console.log("type: " + type + ", data: " + data);
    switch (type) {
        case 'buzzResults':
            displayBuzzResults(data);
            setPauseLogo();
            break;
        case 'game':
            handleGameMessage(data);
            break;
        case 'newTeam':
        case 'newUser':
            fetch(`/common/teams?host=true`)
                .then(response => response.text())
                .then(text => {
                    const teams = document.getElementById("teams");
                    teams.outerHTML = text;
                    addDeleteButtonsListeners();
                });
            break;
    }
}

function displayBuzzResults(buzzResult) {
    buzzResults.push(buzzResult);
    buzzResults.sort((a, b) => a.position - b.position);

    const buzzersEl = document.getElementById("buzzers");
    buzzersEl.innerHTML = "";
    buzzResults.forEach(result => buzzersEl.innerHTML 
        += `<p class="result"><span><strong>${result.position}</strong>. ${result.player} (${result.team})</span>
            <i class="bi bi-check"></i></p>`);
}

function handleGameMessage(data) {
    switch (data) {
        case 'start':
            refreshSpotify();
            break;
        case 'next':
            refreshSpotify();
            clearBuzzers();
        case 'continue':
            setPauseLogo();
            clearBuzzers();
            break;
    }
}

async function refreshSpotify() {
    await delay(100);
    fetch('/spotify/status')
        .then(response => response.json())
        .then(data => {
            document.getElementById("cover").src = data.cover;
            document.getElementById("song-name").innerHTML = data.song;
            document.getElementById("artist").innerHTML = data.artist;

            spotifyPlaying = data.playState === "PLAYING";
            document.getElementById("play-pause-song").innerHTML = `<i class="${spotifyPlaying ? 'bi-pause' : 'bi-play'}"></i>`;
        });
}

function setPlayLogo() {
    document.getElementById("play-pause-song").innerHTML = '<i class="bi-play"></i>';
}

function setPauseLogo() {
    document.getElementById("play-pause-song").innerHTML = '<i class="bi-pause"></i>';
}
