$('.button').click(function(){
  $('.photo-wrap').addClass('love');
  $('.photo').addClass('match');

  
setTimeout(function () { 
  $('.photo').removeClass('match');
  $('.photo-wrap').removeClass('love');
}, 2000);
});

function redirectToLogIn() {
    window.location.href = "../login/index.html";
}

function redirectToChat() {
    window.location.href = "../chat/chat.html";
}