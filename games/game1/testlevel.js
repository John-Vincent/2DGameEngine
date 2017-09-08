//when making level Nashorn makes Entity first so i cant make tick functions within level then pass them as an arguments
//i also dont want to dump a bunch of functions into the global scope so I made a object for all tick functions that i can 
//garbage collect after the file is loaded
var EntityFunctions = {
    PlayerFunction: function(){
		var xspeed = 0, yspeed = 0, movespeed = this.moveSpeed(), scale = this.scale(), tile = this.tile(), input = false, 
		updown = false, swimming = false;
		if(this.isIn(level.Tiles.WATER))
			swimming = true;
		if(activeKeys.contains(" W,")){
			yspeed-= movespeed*scale;
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
			yspeed+= movespeed*scale;
			if(swimming)
				tile = level.Tiles.PLAYER_FRONT_SWIMMING;
			else
				tile = level.Tiles.PLAYER_FRONT_WALKING;
		}
		if(activeKeys.contains(" A,")){
			input = true;
			xspeed -= movespeed*scale;
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
			xspeed += movespeed*scale;
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
		if(pressedKeys.contains(" Up,"))
			this.scale(scale+=1);
		if(pressedKeys.contains(" Down,")&&scale>1)
			this.scale(scale-=1);
		
		this.tile(tile);
	}
}
var level = {
    Map : new Map('map0.png', "map0Layer2.png", 164, 63),
	MapChanged: true,
	SpriteSheetChanged: true,
    MapCoords: [0,0],
    SheetPath: {},
    Tiles:{},
    Entities: [],
    tick : function(){
        for(var i = 0; i<this.Entities.length; i++){
            this.Entities[i].tick();
        }
		if(pressedKeys.contains(" Esc,"))
			return "level1.js";
    }

}
load(window.gameDirectory+"spritesheet.js");
level.Entities.push(new Entity(400,400,level.Tiles.PLAYER_FRONT_STILL,window.scale,1,EntityFunctions.PlayerFunction));
level.Entities[0].setScreenScrollBox(window.width/4,window.height/4, window.width*2/4, window.height*2/4);
getsheet = null;




