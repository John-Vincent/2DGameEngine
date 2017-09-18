SRC = src/
BIN = classes/

JC = javac -d classes -Werror -Xlint
CP = -cp .:src

RM = rm -r
ECHO = echo

GAME = $(addprefix $(BIN)game/, game GUI HitBox SnapShot)
TILE = $(addprefix Tile/, AnimatedTile NormalTile Tile)
IMAGE = $(addprefix $(BIN)imaging/, $(TILE) GraphicsManager map)
INPUT = $(addprefix $(BIN)input/, InputHandler)
SCRIPT = $(addprefix $(BIN)script/, Entity ScriptManager)

SOURCES = $(addsuffix .class, $(GAME) $(IMAGE) $(INPUT) $(SCRIPT))

default: makebin $(SOURCES)
	@$(ECHO) "finished"

$(BIN)%.class: $(SRC)%.java
	@$(ECHO)
	@$(JC) $(CP) $<

run: $(SOURCES)
	java -cp classes game.GUI

clean:
	@$(ECHO) "removing binaries"
	@[ ! -d $(BIN) ] || $(RM) $(BIN)

makebin:
	@[  -d $(BIN) ] || $(ECHO) "making bin folder"
	@[  -d $(BIN) ] || mkdir $(BIN)

.PHONY: makebin clean default run
