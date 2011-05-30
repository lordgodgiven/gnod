
function translateToFr() {
	alert('translate in Fr');
		$.ajax({
		url: '/passerellegoogle/translateinfrench',
		context: document.body,
		success: function(){
		//$(this).addClass("done");
		alert('Fonction appelée');
	  }
	});
}

function translateToEn() {
	alert('translate in En @{PasserelleGoogle.translateInEnglish()}');
	$.ajax({
		url: '/passerellegoogle/translateinenglish',
		context: document.body,
		success: function(){
		//$(this).addClass("done");
		alert('Fonction appelée');
	  }
	});
}

function translateToDe() {
	alert('translate in De');
	$.ajax({
		url: '/passerellegoogle/translateindeutsch',
		context: document.body,
		success: function(){
		//$(this).addClass("done");
		alert('Fonction appelée');
	  }
	});
}

function translateToEs() {
	alert('translate in Es');
	$.ajax({
		url: '/passerellegoogle/translateinspanish',
		context: document.body,
		success: function(){
		//$(this).addClass("done");
		alert('Fonction appelée');
	  }
	});
}

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
 
 function googleTranslateElementInit() {
	new google.translate.TranslateElement({
		pageLanguage: 'fr'
		}, 'translation');
}

$("#frenchTranslator").live("click", function(){ translateToFr();});
$("#englishTranslator").live("click", function(){ translateToEn();});
$("#spanishTranslator").live("click", function(){ translateToEs();});
$("#germanTranslator").live("click", function(){ translateToDe();});
