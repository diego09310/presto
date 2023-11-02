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

if (document.getElementsByClassName("spotify-wrapper").length > 0) {
    document.getElementById("prev-song").addEventListener('click', () => fetch('/spotify/prev'));
    document.getElementById("play-pause-song").addEventListener('click', () => fetch('/spotify/toggle'));
    document.getElementById("next-song").addEventListener('click', () => fetch('/spotify/next'));
}

document.getElementById("game-start").addEventListener('click', () => {
    fetch('/host/game/start');
});
// document.getElementById("game-pause").addEventListener('click', () => fetch('/host/game/pause'));

function handleWebSocketMessage(type, data) {
    console.log("type: " + type + ", data: " + data);
    switch (type) {
        case 'buzzResults':
            displayBuzzResults(data);
            break;
        case 'game':
            if (data === "start") {
                clearBuzzers();
            }
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