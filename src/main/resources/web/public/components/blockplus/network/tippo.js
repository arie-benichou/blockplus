/*
 * Copyright 2012-2013 ArteFact
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

var main = function() {
	/*-----------------------8<-----------------------*/
	var minRegionSquareSide = 320;
	var maxPieceSize = 5;
	var size = 16;
	var scale = 2.222;
	var rows = 20;
	var columns = 20;
	/*-----------------------8<-----------------------*/
	var offsetToPositionBuilder = new OffsetToPositionBuilder(size, size);
	var boardRendering = new BoardRendering(new CellRendering(document.getElementById('board'), size, size, size - 1, size - 1));
	var data = {
		dimension : {
			rows : rows,
			columns : columns
		},
		cells : {
			Blue : [ 84 ],
			Yellow : [ 315 ],
			Red : [ 216 ],
			Green : [ 128 ]
		}
	};
	var board = new Board(data);
	/*-----------------------8<-----------------------*/
	var offsetToPosition1 = function(targetOffset) {
		event.offsetX = event.pageX - targetOffset.left;
		event.offsetY = event.pageY - targetOffset.top;
		return offsetToPositionBuilder.build(event.offsetX, event.offsetY);
	};
	var offsetToPosition2 = function(targetOffset, scale, referential) {
		event.offsetX = event.pageX - targetOffset.left;
		event.offsetY = event.pageY - targetOffset.top;
		var position = offsetToPositionBuilder.build(event.offsetX / scale, event.offsetY / scale);
		return new Position(position.row + referential.minY, position.column + referential.minX);
	};
	/*-----------------------8<-----------------------*/
	var computeNewReferential = function(position) {
		var minY = position.row - (maxPieceSize - 1);
		var minX = position.column - (maxPieceSize - 1);
		var maxY = position.row + (maxPieceSize - 1);
		var maxX = position.column + (maxPieceSize - 1);
		if (maxY > (rows - 1))
			minY -= (maxY - (rows - 1));
		else if (minY < 0)
			minY = 0;
		if (maxX > (columns - 1))
			minX -= (maxX - (columns - 1));
		else if (minX < 0)
			minX = 0;
		return {
			minX : minX,
			minY : minY
		};
	};
	/*-----------------------8<-----------------------*/
	var clickEventHandler1 = function(event) {
		$('#zoom-out').show();
		$(boardRendering.getCanvas()).unbind('click.1');
		var position = offsetToPosition1($(event.target).offset());
		var referential = computeNewReferential(position);
		var clickEventHandler2 = function(event) {
			var position = offsetToPosition2($(event.target).offset(), scale, referential);
			console.log(position);
			boardRendering.updateCell(position, Colors.Blue);
		};
		$(boardRendering.getCanvas()).bind('click.2', clickEventHandler2);
		var context = boardRendering.getContext();
		context.save();
		var xTranslation = -referential.minX * offsetToPositionBuilder.getOffsetX() * scale;
		var yTranslation = -referential.minY * offsetToPositionBuilder.getOffsetY() * scale;
		context.translate(xTranslation, yTranslation);
		context.scale(scale, scale);
		boardRendering.update(board);
	};
	/*-----------------------8<-----------------------*/
	var initState1 = function() {
		$('#zoom-out').hide();
		$(boardRendering.getCanvas()).unbind('click.2');
		var context = boardRendering.getContext();
		context.restore();
		boardRendering.update(board);
		$(boardRendering.getCanvas()).bind('click.1', clickEventHandler1);
	};
	/*-----------------------8<-----------------------*/
	$(boardRendering.getCanvas()).mousedown(function(event) {
		event.preventDefault();
	});
	$('#zoom-out').bind('click.1', initState1);
	/*-----------------------8<-----------------------*/	
	initState1();
	/*-----------------------8<-----------------------*/
};