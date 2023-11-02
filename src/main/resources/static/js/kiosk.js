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
            fetchAndRefreshTeams();
            break;
    }
}
