//function init() {
//	var searchInput = document.querySelector("#searchInput");
//	searchInput.addEventListener("input", doAutoCompletion);
//}

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
	var liveSearchObject = document.getElementById("live-search");
	var url = "search?type=autocomplete&input=" + escape(searchInput);
	
	if((searchInput.length == 0)) {
		liveSearchObject.innerHTML = "";
	}
	else {
		request.open("GET", url, true);
		request.onreadystatechange = function() {
			if(request.readyState == 4) {
				liveSearchObject.innerHTML = "";
				console.log(request.responseText);
				liveSearchObject.innerHTML = request.responseText;
			}
		}
		request.send();
	}
}

