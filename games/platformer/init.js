/* global level */

var window = {
	width: 1300, 
	height: parseInt(1300/12*9), 
	base: 16,
	scale: 3,
	tick: 0,
	debug: false,
	update: function(){
		SnapShot.setMapCoords(level.MapCoords);
		if(SnapShot.getGraphics() !== level.GraphicsManager)
			SnapShot.setSpriteSheet(level.GraphicsManager);
		if(SnapShot.getMap() !== level.Map)
			SnapShot.setMap(level.Map);
		SnapShot.setEntites(JavaEntities);
	},
	gameDirectory: 'games/platformer/'
}

function getWindow(){
	return window;
}

var level;

//java entity wrapper so users can attack custom fuctions to their entities
function Entity(x, y, tile, scale, movespeed, tick){
	this.java = new JavaEntity(x, y, tile, scale, movespeed);
	JavaEntities.add(this.java);
	if(tick=== undefined)
		this.tick = function(){
		
		};
	else
		this.tick = tick;
}
Entity.prototype.isIn = function(tile){
	return this.java.isIn(tile, level.Map, level.GraphicsManager, window.tick);
}
Entity.prototype.isTopCollided = function(){
	return this.java.isTopCollided();
}
Entity.prototype.isBottomCollided = function(){
	return this.java.isBottomCollided();
}
Entity.prototype.isLeftCollided = function(){
	return this.java.isLeftCollided();
}
Entity.prototype.isRightCollided = function(){
	return this.java.isRightCollided();
}
Entity.prototype.setScreenScrollBox = function( xmin, ymin, xmax, ymax, x, y){
    if(xmin === undefined || xmax === undefined || ymin === undefined || ymax === undefined)
        return;
    if(x===undefined)
        x = true;
    if(y===undefined)
        y = true;
    this.java.setScreenScrollBox(xmin, ymin, xmax, ymax, x, y);
}
Entity.prototype.makeMove = function(xspeed, yspeed){
	level.MapCoords = this.java.makeMove(xspeed, yspeed, level.GraphicsManager, level.MapCoords, level.Map, JavaEntities, window.tick);
	}
Entity.prototype.toggleX = function(){
	this.java.toggleInvertX();
}
Entity.prototype.toggleY = function(){
	this.java.toggleInvertY();
}
Entity.prototype.moveSpeed = function(speed){
	if(speed === undefined)
		return this.java.getMovespeed();
	this.java.setMovespeed(speed);
}
Entity.prototype.tile = function(tile){
	if(tile===undefined)
		return this.java.getTile();
	this.java.setTile(tile);
}
Entity.prototype.scale = function(scale){
	if(scale === undefined)
		return this.java.getScale();
	this.java.setScale(scale);
};
Entity.prototype.NoClip = function(bool){
	this.java.setNoClip(bool);
}	
Entity.prototype.invertX = function(bool){
	if(bool === undefined)
		return this.java.isInvertX();
	this.java.setInvertX(bool);
};
Entity.prototype.invertY = function(bool){
		if(bool === undefined)
			return this.java.isInvertY();
		this.java.setInvertY(bool);
};
Entity.prototype.Xpos = function(x){
	if(x === undefined)
		return this.java.getX();
	this.java.setX(x);
};
Entity.prototype.setCollisionTypes = function(top, right, bottom, left){
	this.java.setBottomCollision(bottom);
	this.java.setTopCollision(top);
	this.java.setLeftCollision(left);
	this.java.setRightCollision(right);
}
Entity.prototype.Ypos = function(y){
	if(y === undefined)
		return this.java.getY();
	this.java.setY(y);
};
Entity.prototype.isTouching = function(tile){
	return this.java.isTouching(tile, level.Map, level.GraphicsManager, window.tick);
}
Entity.prototype.remove = function(){
	for(var i = 0; i<JavaEntites.length; i++){
		if(this.java == JavaEntites.get(i)){
			JavaEntites.remove(i);
			break;
		}	
	}
}

//end of Entity


var setlevel = function(){
    JavaEntities.clear();
    if(typeof levelfile == "undefined")
	levelfile = "level1.js";
    load(window.gameDirectory+levelfile);
	levelfile = undefined;
}

var init = function(){
    setlevel();
    window.update();
}

var EntryPoint = function(){
	levelfile = level.tick();
	if(levelfile !== undefined)
		setlevel();
	window.tick+=1;
	window.update();
}

//Nashorn wont allow iife's in the global space
function getWindow(){
	return window;
};
getWindow();