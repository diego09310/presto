function fetchAndRefreshTeams() {
    fetch(`/common/teams`)
        .then(response => response.text())
        .then(text => {
            const teams = document.getElementById("teams");
            teams.outerHTML = text;
        });
}

const buzzResults = [];

function displayBuzzResults(buzzResult) {
    buzzResults.push(buzzResult);
    buzzResults.sort((a, b) => a.position - b.position);

    const buzzersEl = document.getElementById("buzzers");
    buzzersEl.innerHTML = "";
    buzzResults.forEach(result => buzzersEl.innerHTML += `<p><strong>${result.position}</strong>. ${result.player} (${result.team})</p>`);
}

function clearBuzzers() {
    document.getElementById("buzzers").innerHTML = "";
    buzzResults.length = 0;
}
