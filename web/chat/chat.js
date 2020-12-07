
// Switching users chat
$( '.friend-drawer--onhover' ).on( 'click', ( event ) => {

    // let jqxhr = $.post( 'chat/chat.html',  )

    let user = event.currentTarget.children[1].children[0].textContent;
    $( '#currentUser' ).empty().append(user);

    $( '.chat-bubble' ).hide('slow').show('slow');
});

// Choosing user
//

// Send button
$( '.btn-send' ).on( 'click', ()=> {

    let inputText = $( '#textfield' ).val();
    if( inputText === "" ) {
        return;
    }

    let user = $( '#currentUser' ).text();

    let jqxhr = $.post( 'chat/chat.html', { User : user, msg : inputText})

        .done( ( data ) => {
            $( '#chat-panel' ).append(data);
        })

        .fail( () => {
            alert( "Failed to send message" );
        })

        .always( () => {
            console.log( "finished" );
        });


        // Set another completion function for the request above
        jqxhr.always( () => {
            console.log("second finished");
        });
})

