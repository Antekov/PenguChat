For decades, penguins have complained that that now chatting app is available at the south pole. It now is up to you to provide a solution here.

## PinguChat Vision
The secification of this assignment is much more liberal than all preceeding assignments. In the following, you get a list of *requirements' which your implementation is going to fulfill. It is mostly left to you how the individual components look like - as long as they satisfy the respective needs.

Accordingly, there are no explicit tests for this assignment: it is part of the assignment to provide tests yourself (3 tests are completely sufficient).

## Requirements

### General
you are going to realize a multi-user chat by means of sockets allows an arbitrary (but not too large, i.e., less than 50) number of clients to exchange messages.

your server may be started in two possible ways: `java ChatServer` and `java ChatServer <port>` where in the first case, the port number is set to 3000 by default.

the clients may as well be started in two ways: `java ChatClient` and `java ChatClient [portNumber] [serverAddress]` where in the first case by default, localhost:3000 is used.
The first message after a client has connected to the server, only consists of a String representing the username of this participant.

Altogether there are five kinds of messages:
- `@username<blank>message` sends a DM to the respective client and only this client. When there is no client with this name known to the server, the sender just receives a corresponding error message by the server.
- If the client sends `WHOIS`, (s)he and only (s)he, receives a list of all currently connected clients and since when they are connected.
- If a client sends `LOGOUT`, the connection of this client is closed and all streams and of both sides are also closed.
- If a client sends `PINGU`, all currently connected clients receive an important fact about penguins (what ever that might be :)).

All other messages are considered as ordinary messages and sent to all connected clients.

You are invited to introduce further functionality! If you do this, you may send every client, once (s)he has connected send a welcome message listing all available functionality.

Since you have significantly more freedom here, we will also correct significantly more liberal here.

### Server

You should implement a ChatServer which accepts connections via Sockets.

For each established connection, create an independent Thread which is responsible for the communication with the given client.

For each established connection, a message is sent to all client that a new user has joined the chat.

Your server should maintain a data-structure of all currently connected clients (do not omit synchronization here!).

If the Socket of a client is closed or if an IO error occurred when sending a message, the corresponding client is removed from the data-structure.

### Client
You should implement a ChatClient who by means of a Socket establishes a connection to the ChatServer.

The client is meant to prompt all messages on the console, and at the same time should allow the user to write own messages.
The client may choose a fresh username for each session herself.

### Tipps and hints for the solution and perhaps useful classes

Start with a chat which posts every message to all currently connected clients. For that, you may follow the in-class assignment this weed.

Now write a method for the server for posting messages to specific clients only; if you solve this elegantly, you may reuse this method several times.

The classes ObjectInputStream and ObjectOutputStream allow to send and receive objects of classes which realize the interface Serializable.

In order to access the current time, you may use the method `LocalTime.now()`.

### Better readable execution

Your program need not send the same messages, this is just an example how it might look like.