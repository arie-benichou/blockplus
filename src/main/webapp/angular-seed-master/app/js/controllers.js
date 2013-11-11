'use strict';
angular.module('myApp.controllers', []).controller('NewGame', [ function() {
	populate();
	$("#board td").bind("click", handleSelection);
	$("#submit").bind("click", handleSubmit);
	$.ajax({
		url : "/context",
		success : update
	});
	$("#board").bind("mousedown", function(event) {
		event.preventDefault();
	});
} ]).controller('Rendering', [ function() {
	populate();
	resetCells();
	var data = window.location.hash.substring(window.location.hash.indexOf('?') + 1).split(",");
	var positions = parse(data);
	for (i in positions) {
		var p = positions[i];
		cell(p.row, p.column).attr('class', p.color);
	}
} ]);