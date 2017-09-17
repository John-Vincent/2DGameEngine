var EntityFunctions = {
    PlayerFunction: function(){
		var tile= this.tile(), xspeed = 0, yspeed = 8, movespeed = this.moveSpeed(), input = false, 
		onLadder = this.isTouching(level.Tiles.LADDER_TOP)||this.isTouching(level.Tiles.LADDER_CENTER)||this.isTouching(level.Tiles.LADDER_BOTTOM),
		onGround = this.BottomCollision();
		this.BottomCollision(true);
		if(onLadder)
			yspeed = 0;
		
		if(onGround){
			this.jumping = 0;
			this.doubleJump = false;
		}
		if(activeKeys.matches(".*( D,| Right,).*")){
			input = true;
			xspeed += movespeed;
			tile = level.Tiles.PLAYER_WALKING;
			this.invertX(false);
			if(activeKeys.contains(" Shift,")){
				xspeed +=3;
				tile = level.Tiles.PLAYER_RUNNING;
			}
		}
		if(activeKeys.matches(".*( A,| Left,).*")){
			input = true;
			xspeed -= movespeed;
			tile = level.Tiles.PLAYER_WALKING;
			this.invertX(true);
			if(activeKeys.contains(" Shift,")){
				xspeed -=3;
				tile = level.Tiles.PLAYER_RUNNING;
			}
		}
		if(activeKeys.matches(".*( Space,| Up,).*")){
			input = true;	
				if(this.jumping == 0)
					tile = level.Tiles.PLAYER_JUMP_START;
				if(this.jumping<this.jumpTime){
					yspeed = -this.jumpSpeed;
					if(this.jumpTime-this.jumping<this.jumpSpeed)
						yspeed = this.jumping-this.jumpTime;
					tile = level.Tiles.PLAYER_JUMP_UP;
				}
				else{
					if(this.jumping-this.jumpTime<yspeed){
						yspeed = this.jumping-this.jumpTime;
					}
					tile = level.Tiles.PLAYER_JUMP_DOWN;
					if(onGround)
						tile = level.Tiles.PLAYER_JUMP_LAND;
				}
				this.jumping+=1;
		}
		
		if(onLadder && activeKeys.contains(" W,")){
				input = true;
				tile = level.Tiles.PLAYER_LADDER_MOVING;
				yspeed = -3;
		}
		
		if(onLadder && activeKeys.matches(".*( S,| Down,).*")){
			tile = level.Tiles.PLAYER_LADDER_MOVING;
			this.BottomCollision(false);
			input = true;
			yspeed = 3;
		}
		
		if(pressedKeys.matches(".*( W,| Space,| Up,).*")){
			if(!this.doubleJump){
				this.doubleJump = true
				this.jumping = 0;
			}
			else
				this.jumping = this.jumpTime+this.jumpSpeed;
			
		}
		if(!input){
			switch(tile){
				case level.Tiles.PLAYER_LADDER_MOVING:
					tile = level.Tiles.PLAYER_LADDER_STILL;
					break;
				case level.Tiles.PLAYER_RUNNING:
				case level.Tiles.PLAYER_WALKING:
					tile = level.Tiles.PLAYER_STILL;
				break;
				break;
			}
		}
		this.makeMove(xspeed,yspeed);
		
		this.tile(tile);
	}
}
var level = {
    Map : new Map('map0Layer1.png', "map0Layer2.png", 117, 15),
    MapCoords: [0,0],
    SheetPath: {},
    Tiles:{},
    Entities: [],
    tick : function(){
        for(var i = 0; i<this.Entities.length; i++){
            this.Entities[i].tick();
        }
		if(pressedKeys.contains(" Esc,")||this.Entities[0].Ypos()>720)
			return "level1.js";
		if(this.Entities[0].Xpos()>5600){
			this.Map = new Map("map1Layer1.png", 50, 15);
			this.Entities[0].Xpos(384);
			this.Entities[0].Ypos(240);
			this.MapCoords = [0,0]
		}
    }

}
load(window.gameDirectory+"spritesheet.js");
level.Entities.push(new Entity(192, 300, level.Tiles.PLAYER_STILL, 2, 4, EntityFunctions.PlayerFunction));
level.Entities[0].TopCollision(false); level.Entities[0].LeftCollision(false); level.Entities[0].RightCollision(false);
level.Entities[0].jumping = 0;
level.Entities[0].jumpTime = 15;
level.Entities[0].jumpSpeed = 6;
level.Entities[0].doubleJump = false;
level.Entities[0].java.setMapBounded(false);
level.Entities[0].setScreenScrollBox(window.width/4,window.height/4, window.width*2/4, window.height*2/4, true, false);
getsheet = null;
