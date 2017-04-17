


/*  詳細条件指定
--------------------------------*/
 $(function(){
    $("#moreMenu dt").on("click", function() {
        $(this).next().slideToggle(250);
    });
});


// Pagetop
$(function() {
  var topBtn = $('#pageTop');
  topBtn.hide();
  $(window).scroll(function () {
    if ($(this).scrollTop() > 100) {
      topBtn.fadeIn();
    } else {
      topBtn.fadeOut();
    }
  });
    topBtn.click(function () {
    $('body,html').animate({
      scrollTop: 0
    }, 400);
    return false;
    });
});


/*  スマホタップ用
--------------------------------*/
$( 'a, input[type="button"], input[type="submit"], button' )
  .bind( 'touchstart', function(){
    $( this ).addClass( 'hover' );
}).bind( 'touchend', function(){
    $( this ).removeClass( 'hover' );
});