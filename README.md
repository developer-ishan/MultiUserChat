# MultiUserChat

### Java Socket Programming and Swing

User & Server

1. User --> Server

    * login / logoff
    * status

2. Server --> User

    * online / offline
    * notifications :
         * Group invites

3. User --> User

    * direct messages
    * broadcast messages / group messaging


Commands:

    login <user> <password>
    logoff

    msg <user>  body....
    guest: "msg jim Hello World" <-- sent
    jim: "msg guest Hello World" <-- recv


    #topic <-- chatroom / group chat
    join #topic
    leave #topic
    send: msg #topic body...
    recv: msg #topic:<login> body ...
