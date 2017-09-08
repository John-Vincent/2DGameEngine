SRC = src/
BIN = classes/
CP = javac -d classes -Werror -Xlint

GAME = $(addprefix $(SRC)game/, game GUI HitBox SnapShot)
TILE = $(addprefix Tile/, AnimatedTile NormalTile Tile)
IMAGE = $(addprefix $(SRC)imaging/, $(TILE) GraphicsManager map)
INPUT = $(addprefix $(SRC)input/, InputHandler)
SCRIPT = $(addprefix $(SRC)script/, Entity ScriptManager)

SOURCES = $(addsuffix .java, $(GAME) $(IMAGE) $(INPUT) $(SCRIPT))

default: $(SOURCES)
	$(CP) $(SOURCES)
