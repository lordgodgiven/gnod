
 function translate(lang) {
   var source = document.getElementById("initial").innerHTML;
   var len = source.length;

   // Google Language API accepts 500 characters per request 
   var words = 500;

   // This is for English pages, you can change the
   // sourcelang variable for other languages
   var sourcelang = "fr";
   document.getElementById("translation").innerHTML = "";

   for(i=0; i<=(len/words); i++) {
     google.language.translate (source.substr(i*words, words),
                 "fr", lang, function (result) {
     if (!result.error) {
     document.getElementById("translation").innerHTML
           = document.getElementById("translation").innerHTML
           + result.translation;
    } }); }  

  // Hide the text written in the original language
  document.getElementById("initial").style.display = 'none';
  return false;
 }
 
function original() {
  document.getElementById("translation").style.display='none';
  document.getElementById("initial").style.display = 'block';
  return false;
 }