var EntityFunctions = {
    PlayerFunction: function(){
		var tile= this.tile(), xspeed = 0, yspeed = this.jumpSpeed, movespeed = this.moveSpeed(), input = false, 
		onLadder = this.isTouching(level.Tiles.LADDER_TOP)||this.isTouching(level.Tiles.LADDER_CENTER)||this.isTouching(level.Tiles.LADDER_BOTTOM),
		onGround = this.isBottomCollided();
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
		if(activeKeys.matches(".*( W,| Space,| Up,).*")){
			input = true;
			if(onLadder){
				tile = level.Tiles.PLAYER_LADDER_MOVING;
				yspeed-=(this.jumpSpeed+3);
			}
			else{
				if(this.jumping == 0)
					tile = level.Tiles.PLAYER_JUMP_START;
				if(this.jumping<this.jumpTime){
					if(this.jumping<(this.jumpTime-this.jumpSpeed))
						yspeed -= this.jumpSpeed*2;
					else
						yspeed -= (this.jumpSpeed + (this.jumpTime-this.jumping))
					tile = level.Tiles.PLAYER_JUMP_UP;
				}
				else{
					if(this.jumping<(this.jumpTime+this.jumpSpeed)){
						yspeed -= ((this.jumpTime+this.jumpSpeed) - this.jumping)
					}
					tile = level.Tiles.PLAYER_JUMP_DOWN;
					if(onGround)
						tile = level.Tiles.PLAYER_JUMP_LAND;
				}
				this.jumping+=1;
			}
		}
		if(activeKeys.matches(".*( S,| Down,).*")&&onLadder){
			tile = level.Tiles.PLAYER_LADDER_MOVING;
			input = true;
			yspeed = 3;
		}
		if(pressedKeys.matches(".*( W,| Space,| Up,).*")){
			if(!this.doubleJump){
				this.jumping = true
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
    Map : new Map('map0Layer1.png', "map0Layer2.png", 200, 21),
    MapCoords: [0,0],
    SheetPath: {},
    Tiles:{},
    Entities: [],
    tick : function(){
        for(var i = 0; i<this.Entities.length; i++){
            this.Entities[i].tick();
        }
		if(pressedKeys.contains(" Esc,")||this.Entities[0].Ypos()>955)
			return "level1.js";
    }

}
load(window.gameDirectory+"spritesheet.js");
level.Entities.push(new Entity(192, 624, level.Tiles.PLAYER_STILL, 2, 5, EntityFunctions.PlayerFunction));
level.Entities[0].setCollisionTypes(false,false,true,false);
level.Entities[0].jumping = 0;
level.Entities[0].jumpTime = 20;
level.Entities[0].jumpSpeed = 6;
level.Entities[0].doubleJump = false;
level.Entities[0].setScreenScrollBox(window.width/4,window.height/4, window.width*2/4, window.height*2/4, true, false);
getsheet = null;
