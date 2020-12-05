
$( '.friend-drawer--onhover' ).on( 'click',  function() {
  $( '.chat-bubble' ).hide('slow').show('slow');
  
});

$( '.btn-send' ).on( 'click', ()=>{

    let inputText = $( '#textfield' ).val();
    if( inputText === "" ) {
        console.log("Empty")
        return;
    }
    console.log(inputText);


  let jqxhr = $.post( 'chat/chat.html', inputText, () => {
      console.log( "success" );
  })
      .done(function( data ) {
          $( '#chat-panel' ).append(data);

          console.log( "second success" );
      })
      .fail(function() {
        alert( "error" );
      })
      .always(function() {
          console.log( "finished" );
      });


// Set another completion function for the request above
  jqxhr.always(function() {

      console.log("second finished");
  });
} )


