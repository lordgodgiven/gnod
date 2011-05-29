
function loadGoogleTranslate() {
	alert('chargement google translate');
	google.load("language", "1");
	alert('chargement ok');
}
function translateToEn() {
	//alert('translate in english');
	google.load("language", "1");
	alert('chargement google translate ok');
	
	$(".translate").each(function(n){
		alert( 'Item #');
		alert( 'Item #'.n );
	 });
	
	google.language.translate($(".translate"), "fr", "en", function(result) {
	  if (!result.error) {
		var container = $(".translate");
		container.innerHTML = result.translation;
		alert('traduction ok');
		alert(result.translation);
	  }
	});
	
	//alert('chargement google translate ok');
	/*google.language.detect(text, function(result) {
	  if (!result.error) {
		var language = 'unknown';
		for (l in google.language.Languages) {
		  if (google.language.Languages[l] == result.language) {
			language = l;
			break;
		  }
		}
		var container = document.getElementById("translate");
		container.innerHTML = text + " is: " + language + "";
	  }
	});
	google.language.detect($('#translate'), function(result));*/
	
	//alert('detection ok');
}

function translateToFr() {
	alert('translate in french');
}

function translateToEs() {
	alert('translate in spanish');
}

function translateToDe() {
	alert('translate in german');
}

/*google.language.translate("Hello world", "en", "es", function(result) {
  if (!result.error) {
    var container = document.getElementById("translation");
    container.innerHTML = result.translation;
  }
});

google.language.detect(text, function(result) {
  if (!result.error) {
    var language = 'unknown';
    for (l in google.language.Languages) {
      if (google.language.Languages[l] == result.language) {
        language = l;
        break;
      }
    }
    var container = document.getElementById("translate");
    container.innerHTML = text + " is: " + language + "";
  }
});*/

//window.addEventListener("load", OnLoad, false);
$("#translateToEn").live("click", function(){ translateToEn();});
$("#translateToFr").live("click", function(){ translateToFr();});
$("#translateToEs").live("click", function(){ translateToEs();});
$("#translateToDe").live("click", function(){ translateToDe();});

function OnLoad(){
	alert('chargement google translate');
	google.load("language", "1");
	alert('chargement ok');
}