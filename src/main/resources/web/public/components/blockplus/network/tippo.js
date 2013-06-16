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
	var boardDimension = {
		rows : 20,
		columns : 20,
		size : function() {
			return this.rows * this.columns;
		}

	};
	/*-----------------------8<-----------------------*/
	var viewPort = {
		minWidth : 320,
		minHeight : 480,
		min : function() {
			return Math.min(this.minWidth, this.minHeight);
		}
	}
	/*-----------------------8<-----------------------*/
	var cellDimension = {
		width : viewPort.min() / boardDimension.rows,
		height : viewPort.min() /boardDimension.columns
	}
	/*-----------------------8<-----------------------*/
	var maxPieceSize = 5;
	var neighbourhood = maxPieceSize - 1;
	/*-----------------------8<-----------------------*/
	var scale = {
		x : boardDimension.columns / (2 * neighbourhood + 1),
		y : boardDimension.rows / (2 * neighbourhood + 1)
	}
	/*-----------------------8<-----------------------*/
	var positionFactory = new PositionFactory(cellDimension);
	var cellRenderer = new CellRenderer(document.getElementById('board'), positionFactory, Colors);
	var boardRenderer = new BoardRenderer(boardDimension, cellRenderer);
	/*-----------------------8<-----------------------*/
	var data = {
		dimension : {
			rows : boardDimension.rows,
			columns : boardDimension.columns
		},
		cells : {
			Blue : [ 0, 21, 42, 63, 84, 105, 126, 147, 168, 189 ],
			Yellow : [ 19, 38, 57, 76, 95, 114, 133, 152, 171, 190 ],
			Red : [ 380, 361, 342, 323, 304, 285, 266, 247, 228, 209 ],
			Green : [ 399, 378, 357, 336, 315, 294, 273, 252, 231, 210 ]
		}
	};
	/*-----------------------8<-----------------------*/	
	// TODO BoardManager et lui injecter le boardRenderer
	var board = new Board(data);

	var offsetToPosition1 = function(event, targetOffset) {
		event.offsetX = event.pageX - targetOffset.left;
		event.offsetY = event.pageY - targetOffset.top;
		return positionFactory.getPositionFromOffset(event.offsetX, event.offsetY);
	};
	var offsetToPosition2 = function(event, targetOffset, scale, referential) {
		event.offsetX = event.pageX - targetOffset.left;
		event.offsetY = event.pageY - targetOffset.top;
		var p = positionFactory.getPositionFromOffset(event.offsetX / scale.x, event.offsetY / scale.y);
		return positionFactory.getPosition(p.row + referential.minY, p.column + referential.minX);
	};
	
	var computeNewReferential = function(position) {
		var minY = position.row - neighbourhood;
		var minX = position.column - neighbourhood;
		var maxY = position.row + neighbourhood;
		var maxX = position.column + neighbourhood;
		if (maxY > (boardDimension.rows - 1))
			minY -= (maxY - (boardDimension.rows - 1));
		else if (minY < 0)
			minY = 0;
		if (maxX > (boardDimension.columns - 1))
			minX -= (maxX - (boardDimension.columns - 1));
		else if (minX < 0)
			minX = 0;
		return {
			minX : minX,
			minY : minY
		};
	};

	var clickEventHandler1 = function(event) {
		$('#zoom-out').show();
		$(boardRenderer.getCanvas()).unbind('click.1');
		var position = offsetToPosition1(event, $(event.target).offset());
		var referential = computeNewReferential(position);
		var clickEventHandler2 = function(event) {
			var position = offsetToPosition2(event, $(event.target).offset(), scale, referential);
			boardRenderer.updateCell(position, "#FFF");
		};
		$(boardRenderer.getCanvas()).bind('click.2', clickEventHandler2);
		var context = boardRenderer.getContext();
		context.save();
		var translation = {
			x : -referential.minX * cellRenderer.width * scale.x,
			y : -referential.minY * cellRenderer.height * scale.y
		}
		context.translate(translation.x, translation.y);
		context.scale(scale.x, scale.y);
		boardRenderer.update(board);
	};

	var initState1 = function() {
		$('#zoom-out').hide();
		$(boardRenderer.getCanvas()).unbind('click.2');
		var context = boardRenderer.getContext();
		context.restore();
		boardRenderer.update(board);
		$(boardRenderer.getCanvas()).bind('click.1', clickEventHandler1);
	};

	$(boardRenderer.getCanvas()).mousedown(function(event) {
		event.preventDefault();
	});
	
	$('#zoom-out').bind('click.1', initState1);
	
	initState1();
	
	/*-----------------------8<-----------------------*/
};