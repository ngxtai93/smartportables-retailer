function init() {
	var searchInput = document.querySelector("#searchInput");
	searchInput.addEventListener("input", doAutoCompletion);
}

function getRequestObject() {
	if(window.XMLHttpRequest) {
		return (new XMLHttpRequest());
	}
	else if(window.ActiveXObject) {
		return (new ActiveXObject("Microsoft.XMLHTTP"));
	}
	else {
		return null;
	}
}

function doAutoCompletion() {
	var request = getRequestObject();
	var searchInput = document.getElementById("searchInput").value;
	var url = "search?type=autocomplete&input=" + escape(searchInput);
	
	request.open("GET", url, true);
	request.onreadystatechange = function() {
		if(request.readyState == 4) {
			var liveSearchObject = document.getElementsByClassName("live-search")[0];
			liveSearchObject.innerHtml = request.responseText;
		}
	}
	request.send();
}

