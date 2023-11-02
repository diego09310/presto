function handleWebSocketMessage(type, data) {
    console.log("type: " + type + ", data: " + data);
    switch (type) {
        case 'buzzResults':
            displayBuzzResults(data);
            break;
        case 'game':
            if (data === "start") {
                document.getElementById("buzz").disabled = false;
                document.getElementById("clicked").classList.add("hidden");
                clearBuzzers();
            } else if (data === "pause") {
                document.getElementById("buzz").disabled = true;
            }
            break;
    }
}

function buzz(playerId, teamId) {
    stompClient.publish({
        destination: "/app/buzz",
        body: JSON.stringify({'playerId': playerId, 'teamId': teamId})
    });
    document.getElementById("clicked").classList.remove("hidden");
}

const playerId = document.getElementById("playerId").textContent;
const teamId = document.getElementById("teamId").textContent;
document.getElementById("buzz").addEventListener('click', () => buzz(playerId, teamId));
