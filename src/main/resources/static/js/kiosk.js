function handleWebSocketMessage(type, data) {
    console.log("type: " + type + ", data: " + data);
    switch (type) {
        case 'buzzResults':
            displayBuzzResults(data);
            break;
        case 'game':
            handleGameMessage(data);
        case 'newTeam':
        case 'newUser':
            fetchAndRefreshTeams();
            break;
    }
}

function handleGameMessage(data) {
    switch (data) {
        case 'next':
        case 'continue':
            clearBuzzers();
            break;
    }
}
