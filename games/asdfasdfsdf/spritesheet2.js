var getsheet = function(){
	var tiles = {
	GRASS: new NormalTile(0,0, window.base, window.base),
	DIRT: new NormalTile(window.base, 0, window.base, window.base),
	WATER: new AnimatedTile(35),
	COBBLE: new NormalTile(window.base*3, 0, window.base, window.base),
	BOLDER: new NormalTile(window.base*4, 0, window.base, window.base),
	TREE: new NormalTile(80,0,17,24, 4, 19, 8, 3, true),
	FIREBALL: new NormalTile(0,128, window.base, window.base),
	PLAYER_FRONT_STILL: new NormalTile(120, 135, 29, 34,6, 28, 17, 6, true),
	PLAYER_FRONT_WALKING: new AnimatedTile(15),
	PLAYER_SIDE_STILL: new NormalTile(121, 170, 30, 34, 7, 29, 14, 5,true),
	PLAYER_SIDE_WALKING: new AnimatedTile(15),
	PLAYER_BACK_STILL: new NormalTile(120, 206, 29, 34, 6, 28, 17, 6,true),
	PLAYER_BACK_WALKING: new AnimatedTile(15),
	PLAYER_BACK_SWIMMING: new AnimatedTile(20),
	PLAYER_FRONT_SWIMMING: new AnimatedTile(20),
	PLAYER_SIDE_SWIMMING: new AnimatedTile(20)
	}

	tiles.FIREBALL.addHitbox(0,6,16,9, false);
	tiles.BOLDER.addHitbox(1,1,window.base-2, window.base-2, true);
	
	var temp = new NormalTile(window.base*2, window.base, window.base, window.base,0,0,window.base,window.base,false);
	var temp2 = new NormalTile(window.base*2, window.base*2, window.base, window.base,0,0,window.base,window.base,false);
	tiles.WATER.addTile(new NormalTile(window.base*2, 0, window.base, window.base,0,0,window.base,window.base,false));
	tiles.WATER.addTile(temp);
	tiles.WATER.addTile(temp2);
	tiles.WATER.addTile(new NormalTile(window.base*2, window.base*3, window.base, window.base,0,0,window.base,window.base,false));
	tiles.WATER.addTile(temp2);
	tiles.WATER.addTile(temp);
	
	tiles.PLAYER_FRONT_SWIMMING.addTile(new NormalTile(75,135,27,22,6, 27, 17, 7, true));
	tiles.PLAYER_FRONT_SWIMMING.addTile(new NormalTile(42,135,28,25,6, 27, 17, 7, true));
	tiles.PLAYER_FRONT_SWIMMING.addTile(new NormalTile(8,135,32,27,6, 27, 17, 7, true));
	
	tiles.PLAYER_FRONT_WALKING.addTile(tiles.PLAYER_FRONT_STILL);
	tiles.PLAYER_FRONT_WALKING.addTile(new NormalTile(74, 135, 29, 34, 6, 28, 17, 6, true));
	tiles.PLAYER_FRONT_WALKING.addTile(tiles.PLAYER_FRONT_STILL);
	tiles.PLAYER_FRONT_WALKING.addTile(new NormalTile( 158, 135, 29, 34, 6, 28, 17, 6, true));
	
	tiles.PLAYER_SIDE_SWIMMING.addTile(new NormalTile(75, 170, 30, 22, 7, 29, 14, 5, true));
	tiles.PLAYER_SIDE_SWIMMING.addTile(new NormalTile(42, 170, 30, 25, 7, 29, 14, 5, true));
	tiles.PLAYER_SIDE_SWIMMING.addTile(new NormalTile(9, 170, 32, 27, 7, 29, 14, 5, true));
	
	tiles.PLAYER_SIDE_WALKING.addTile(tiles.PLAYER_SIDE_STILL);
	tiles.PLAYER_SIDE_WALKING.addTile(new NormalTile( 75, 170, 30, 34, 7, 29, 14, 5, true));
	tiles.PLAYER_SIDE_WALKING.addTile(tiles.PLAYER_SIDE_STILL);
	tiles.PLAYER_SIDE_WALKING.addTile(new NormalTile( 158, 170, 30, 34, 7, 29, 14, 5, true));
	
	tiles.PLAYER_BACK_SWIMMING.addTile(new NormalTile(75, 206, 27, 22, 6, 28, 16, 6, true));
	tiles.PLAYER_BACK_SWIMMING.addTile(new NormalTile(41, 206, 30, 25, 6, 28, 16, 6, true));
	tiles.PLAYER_BACK_SWIMMING.addTile(new NormalTile(8, 206, 32, 28, 6, 28, 16, 6, true));
	
	tiles.PLAYER_BACK_WALKING.addTile(tiles.PLAYER_BACK_STILL);
	tiles.PLAYER_BACK_WALKING.addTile(new NormalTile(74, 206, 29, 34, 6, 28, 17, 6, true));
	tiles.PLAYER_BACK_WALKING.addTile(tiles.PLAYER_BACK_STILL);
	tiles.PLAYER_BACK_WALKING.addTile(new NormalTile(158, 206, 29, 34, 6, 28, 17, 6, true));

	// makes a tile object in level that has the same keys as the tiles made in this function, but stores the position in the tile array
	// in the graphics manager object, that way the user can refer to all the tiles by name with level.Tiles."tile name"
	var graphicsmanager = new GraphicsManager("spritesheet.png", tiles);
	var keys = Object.keys(tiles);
	for(i = 0; i<keys.length; i++){
		level.Tiles[keys[i]] = i;
	};
	return graphicsmanager;
}

level.GraphicsManager = getsheet();
level.SheetPath = "spritesheet.js";