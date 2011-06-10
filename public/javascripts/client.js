
function translateToFr() {
	alert('translate in Fr');
	$.ajax({
		url: '/passerellegoogle/translateinfrench',
		context: document.body,
		cache:false,
		dataType: "json",
		success: function(){
			alert('Fonction appel�e');
		}
	});
}

function translateToEn() {
	alert('translate in En @{PasserelleGoogle.translateInEnglish()}');
	$.ajax({
		url: '/passerellegoogle/translateinenglish',
		context: document.body,
		cache:false,
		dataType: "json",
		success: function(){
		alert('Fonction appelee');
	  }
	});
}

function translateFFS() {

	//var germanTranslator = #{jsAction @PasserelleGoogle.translateInDeutsch(':doc') /}

	var germanTranslator = function(options) {var pattern = '/passerellegoogle/translateindeutsch?doc=:doc'; for(key in options) { pattern = pattern.replace(':'+key, options[key]); } return pattern }
	
//	$.post(germanTranslator,{'doc': document.body.innerHTML})
//		.success(function(data) {
//			alert(data);
//			$(events).each(function() {
//				alert(this.data);
//			})
//		})
//		.error(function(data) {
//			alert("fucku");
//		});
	var doc = 'salut';
	alert("a envoyer " +doc);
	$.ajax({
		type: 'POST',
		data: doc,
		url: '/passerellegoogle/translateindeutsch',
		success: function(data) {
			alert(data);
			$(events).each(function() {
				alert(this.data);
			})
		},
		error: function(e) {
			alert("ERROR : "+e);
		},
		dataType: 'text'
	});
}

function translateToDe() {
	var doc = document.body.innerHTML;
	alert('youhou ' +doc);
	/*$.ajax({
		type: "POST",
		url: "/passerellegoogle/translateindeutsch",
		data: doc,
		cache:false,
		dataType: "json",
		processData: false,
		success: function(msg){
		   alert( "Reussite. YOUHOUUUUUUUUU " +msg);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
             alert(errorThrown);
             alert(XMLHttpRequest);
             alert(textStatus);
			 }
	});*/
	$.post('translateInDeutsch',{document: doc}, 
			function(ta_variable_de_retour){
				alert('success' +ta_variable_de_retour);
			}
	);
	// Ce qui marche 
/*
$.ajax({type: "POST", url: "/Application/recup_liste_etudiant", data: param, dataType: "json",
               success: function(json){
                       alert('Reussite');
               },
               error: function(XMLHttpRequest, textStatus, errorThrown) {
                       alert('Erreur; ca ne fonctionne pas');
                       alert(errorThrown);
                       alert(XMLHttpRequest);
                       alert(textStatus);
               }
          });*/	
		  
	//$.getJSON("http://localhost:9000/passerellegoogle/translateindeutsch.html",
	/*$.getJSON("@{PasserelleGoogle.translateInDeutsch()}",
	{"action" : "objectifs",
	"page_html" : page_html },
	function() {
		$.each(function() {
			alert('reception ok');
		});
	});*/
	
}

function translateToEs() {
	alert('translate in Es');
	var cpt;
	var racine = document.getElementById("initial");
	var child = racine.childNodes;
	//for (
	$.ajax({
		//url: '/passerellegoogle/translateinspanish',
		//url: '@{PasserelleGoogle.translateInDeutsch()}',
		//url: '/passerellegoogle/translateinspanish.html',
		url: '/PasserelleGoogle/translateInSpanish',
		data: racine,
		//context: document.body,
		cache:false,
		dataType: "json",
		success: function(){
		alert('Fonction appel�e');
	  }
	});
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

