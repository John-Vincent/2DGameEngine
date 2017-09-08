var EntityFunctions = {
    PlayerFunction: function(){
		var xspeed = 0, yspeed = 0, movespeed = this.moveSpeed(), scale = this.scale(), tile = this.tile(), input = false, 
		updown = false, swimming = false;
		if(this.isIn(level.Tiles.WATER)){
			swimming = true;
			movespeed -= 2;
		}
		if(activeKeys.contains(" W,")){
			yspeed-= movespeed;
			input = true;
			updown = true;
			if(swimming)
				tile = level.Tiles.PLAYER_BACK_SWIMMING;
			else
				tile = level.Tiles.PLAYER_BACK_WALKING;
		}
		if(activeKeys.contains(" S,")){
			input = true;
			updown = true;
			yspeed+= movespeed;
			if(swimming)
				tile = level.Tiles.PLAYER_FRONT_SWIMMING;
			else
				tile = level.Tiles.PLAYER_FRONT_WALKING;
		}
		if(activeKeys.contains(" A,")){
			input = true;
			xspeed -= movespeed;
			this.invertX(true);
                        if(!updown){
							if(swimming)
								tile = level.Tiles.PLAYER_SIDE_SWIMMING;
							else
								tile = level.Tiles.PLAYER_SIDE_WALKING;
						}
		}
		if(activeKeys.contains(" D,")){
			input = true;
			this.invertX(false);
			xspeed += movespeed;
                        if(!updown){
							if(swimming)
								tile = level.Tiles.PLAYER_SIDE_SWIMMING;
							else
								tile = level.Tiles.PLAYER_SIDE_WALKING;
						}
		}
		this.makeMove(xspeed, yspeed, false);
		
		if(!input){
			switch(tile){
			case level.Tiles.PLAYER_BACK_WALKING:
				tile = level.Tiles.PLAYER_BACK_STILL;
				this.invertX(false)
				break;
			case level.Tiles.PLAYER_FRONT_WALKING:
				tile = level.Tiles.PLAYER_FRONT_STILL
				this.invertX(false);
				break;
			case level.Tiles.PLAYER_SIDE_WALKING:
				tile = level.Tiles.PLAYER_SIDE_STILL;
				break;
			}
		}
		this.tile(tile);
	}
}
var level = {
    Map : new Map('map1.png', "map1Layer2.png", "map1Entities.png", 300, 300),
    MapCoords: [0,0],
    SheetPath: {},
    Tiles:{},
    Entities: [],
    tick : function(){
        for(var i = 0; i<this.Entities.length; i++){
            this.Entities[i].tick();
        }
		if(pressedKeys.contains(" Esc,"))
			return "testlevel.js";
    }

}
load(window.gameDirectory+"spritesheet2.js");
level.Entities.push(new Entity(400,400,level.Tiles.PLAYER_FRONT_STILL,window.scale,5,EntityFunctions.PlayerFunction));
level.Entities.push(new Entity(43*window.base*window.scale,38*window.base*window.scale, level.Tiles.TREE, 14));
level.Entities[0].setScreenScrollBox(window.width/4,window.height/4, window.width*2/4, window.height*2/4);
getsheet = null;
