$("h1").on("click", function(e) {
  if (e.which === 1) {
    $("*").css("font-size", "+=2px");
  } else if (e.which === 3) {
    $("*").css("font-size", "-=2px");
  }
});