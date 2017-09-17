var getsheet = function(){
	var tiles = {
		BACKGROUND: new NormalTile(0, 51, 16, 16),
		WATER1: new NormalTile( 128, 80, 16, 16),
		WATER2: new NormalTile( 128, 96, 16, 16),
		
		TOP_LEFT_DIRT:	new NormalTile( 160, 0, 16, 16, 0, 0, 16, 3, true),
		TOP_CENTER_DIRT: new NormalTile( 176, 0, 16, 16, 0, 0, 16, 3, true),
		TOP_RIGHT_DIRT: new NormalTile( 192, 0, 16, 16, 0, 0, 16, 3, true),
		BOTTOM_RIGHT_DIRT: new NormalTile( 192, 16, 16, 16),
		BOTTOM_CENTER: new NormalTile( 176, 16, 16, 16),
		BOTTOM_LEFT_DIRT: new NormalTile( 160, 16, 16, 16),
		BOTTOM_CENTER_DIRT: new NormalTile( 112, 16, 16, 16),
		
		LADDER_TOP: new NormalTile( 112, 64, 16, 16, 2, 0, 10, 16, false),
		LADDER_CENTER: new NormalTile( 112, 80, 16, 16, 2, 0, 10, 16, false),
		LADDER_BOTTOM: new NormalTile( 112, 96, 16, 16, 2, 0, 10, 16, false),
		
		BACKGROUND_TOP_RIGHT_GRASS: new NormalTile( 176, 64, 16, 16),
		BACKGROUND_TOP_CENTER_GRASS: new NormalTile( 192, 64, 16, 16),
		BACKGROUND_TOP_LEFT_GRASS: new NormalTile( 208, 64, 16, 16),
		BACKGROUND_MIDDLE_LEFT_GRASS: new NormalTile( 176, 80, 16, 16),
		BACKGROUND_MIDDLE_RIGHT_GRASS: new NormalTile( 208, 80, 16, 16),
		BACKGROUND_BOTTOM_LEFT_GRASS: new NormalTile( 176, 96, 16, 16),
		BACKGROUND_BOTTOM_CENTER_GRASS: new NormalTile( 192, 96, 16,16),
		BACKGROUND_BOTTOM_RIGHT_GRASS: new NormalTile( 208, 96, 16, 16),
		BACKGROUND_CENTER_GRASS: new NormalTile(160, 96, 16,16),
		
		
		PLAYER_LADDER_STILL: new NormalTile(221, 115, 17, 24, 0, 0, 17, 23, true),
		PLAYER_LADDER_MOVING: new AnimatedTile(10),
		PLAYER_STILL: new NormalTile(187, 140, 18, 24, 0, 0, 17, 23, true),
		PLAYER_WALKING: new AnimatedTile(15),
		PLAYER_RUNNING: new AnimatedTile(15),
		PLAYER_JUMP_START: new NormalTile(157, 197, 18, 22, 0, 0, 17, 23, true),
		PLAYER_JUMP_UP: new NormalTile(188, 195, 18, 23, 0, 0, 17, 23, true),
		PLAYER_JUMP_DOWN: new NormalTile(220, 195, 18, 23, 0, 0, 17, 23, true),
		PLAYER_JUMP_LAND: new NormalTile(252, 197, 19, 22, 0, 0, 17, 23, true)
		
	}
	
	tiles.PLAYER_LADDER_MOVING.addTile(
		tiles.PLAYER_LADDER_STILL,
		new NormalTile(189, 115, 17, 24, 0, 0, 17, 23, true),
		tiles.PLAYER_LADDER_STILL,
		new NormalTile(253, 115, 17, 24, 0, 0, 17, 23, true));
	tiles.PLAYER_WALKING.addTile(
		new NormalTile(155, 141, 18, 23, 0, 0, 17, 23, true),
		tiles.PLAYER_STILL,
		new NormalTile(219, 141, 18, 23, 0, 0, 17, 23, true),
		new NormalTile(251, 140, 19, 23, 0, 0, 17, 23, true));
	tiles.PLAYER_RUNNING.addTile(
		tiles.PLAYER_STILL,
		new NormalTile(188, 170, 18, 23, 0, 0, 17, 23, true),
		new NormalTile(220, 169, 19, 23, 0, 0, 17, 23, true),
		new NormalTile(252, 170, 18, 23, 0, 0, 17, 23, true));
	

	var graphicsmanager = new GraphicsManager("spritesheet.png", tiles);
	var keys = Object.keys(tiles);
	for(i = 0; i<keys.length; i++){
		level.Tiles[keys[i]] = i;
	};
	return graphicsmanager;
}

level.GraphicsManager = getsheet();
level.SheetPath = "spritesheet.js";