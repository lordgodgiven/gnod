
function translateToEn() {
	alert('translate in english');
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

$("#translateToEn").live("click", function(){ translateToEn();});
$("#translateToFr").live("click", function(){ translateToFr();});
$("#translateToEs").live("click", function(){ translateToEs();});
$("#translateToDe").live("click", function(){ translateToDe();});