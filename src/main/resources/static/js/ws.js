const stompClient = new StompJs.Client({
    brokerURL: 'ws://' + window.location.host + '/ws'
});

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/all', (message) => {
        console.log(message);
        message = JSON.parse(message.body);
        const type = message.type;
        const data = message.data;
        handleWebSocketMessage(type, data)
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function connect() {
    stompClient.activate();
}

function handleWebSocketMessage(type, data) {
    console.log("Need to implement handler. type: " + type + ", data: " + data);
}

connect();
