function handleWebSocketMessage(type, data) {
    console.log("type: " + type + ", data: " + data);
    switch (type) {
        case 'game':
            if (data === "start") {
                window.location.href = '/player/button';
            }
            break;
        case 'newTeam':
        case 'newUser':
            fetchAndRefreshTeams();
            break;
    }
}

