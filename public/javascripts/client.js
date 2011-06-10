
function translateToFr() {
	var doc = document.body.innerHTML;
	$.post('translateInFrench',{document: doc}, 
			function(data){
				//alert('success' +data);
				document.body.innerHTML = data;
			}
	);
}

function translateToEn() {
	var doc = document.body.innerHTML;
	$.post('translateInEnglish',{document: doc}, 
			function(data){
				//alert('success' +data);
				document.body.innerHTML = data;
			}
	);	
}

function translateToDe() {
	var doc = document.body.innerHTML;
	$.post('translateInDeutsch',{document: doc}, 
			function(data){
				//alert('success' +data);
				document.body.innerHTML = data;
			}
	);	
}

function translateToEs() {
	var doc = document.body.innerHTML;
	$.post('translateInSpanisch',{document: doc}, 
			function(data){
				//alert('success' +data);
				document.body.innerHTML = data;
			}
	);	
}
 
$("#frenchTranslator").live("click", function(){ translateToFr();});
$("#englishTranslator").live("click", function(){ translateToEn();});
$("#spanishTranslator").live("click", function(){ translateToEs();});
$("#germanTranslator").live("click", function(){ translateToDe();});

