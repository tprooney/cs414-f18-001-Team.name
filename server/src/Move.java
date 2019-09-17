package src;

import java.util.Arrays;

class Move {

    private Game game;
    int selectedRow = -1;
    int selectedCol = -1;
    String[][] validTiles = new String[9][7];

    Move(Game current_game){
        game = current_game;
        for(int i=0; i<9; i++){
            for(int j=0; j<7; j++){
                validTiles[i][j] = "_";
            }
        }
    }

    void printValidTiles(){
        for(int i=0; i<9; i++) System.out.println(Arrays.toString(validTiles[i]));
    }

    void updateValidTiles(){
        if(selectedRow != -1 && selectedCol != -1) {
            if(selectedCol <= 5) handleUpdate(selectedRow, selectedCol+1);
            if(selectedCol >= 1) handleUpdate(selectedRow, selectedCol-1);
            if(selectedRow <= 7) handleUpdate(selectedRow+1, selectedCol);
            if(selectedRow >= 1) handleUpdate(selectedRow-1, selectedCol);
        } else {
            for(int i=0; i<9; i++){
                for(int j=0; j<7; j++){
                    validTiles[i][j] = "_";
                }
            }
        }
        System.out.println("");
        printValidTiles();
    }

    private void handleUpdate(int row, int col) {
        // checks tile conditions and puts a "*" in the corresponding location of validTiles if the tile can be moved to from location (selectedRow, selectedCol)
        if (!isFriendlyUnit(row, col)) { // if there is no friendly unit occupying the tile
            if (!isWater(row, col) || isRat(selectedRow, selectedCol)) { // if there is no water (unless the unit being moved is a rat)
                if((isEnemyUnit(row, col) && canCaptureUnit(row, col)) || !isEnemyUnit(row, col)) // if there is an enemy unit that the unit being moved can capture, or there is no enemy
                    validTiles[row][col] = "*";
            } else if (isLionOrTiger(selectedRow, selectedCol)) { // if the unit being moved is a lion or tiger
                handleLionTigerMovement();
            }
        }
    }

    private void handleLionTigerMovement(){
    }

    boolean isFriendlyUnit(int row, int col){
        // returns true if the game piece at row, col belongs to the current player, else returns false.
        char friendly = game.turn.charAt(0); //b if turn is blue, r if turn is red
        return game.board[row][col].charAt(0) == friendly;
    }

    private boolean isEnemyUnit(int row, int col){
        // returns true if the game piece at row, col belongs to the opponent of the current player, else returns false.
        char friendly = game.turn.charAt(0);
        char enemy = ' ';
        if(friendly == 'b') enemy = 'r';
        if(friendly == 'r') enemy = 'b';
        return game.board[row][col].charAt(0) == enemy;
    }

    private boolean canCaptureUnit(int row, int col){
        // returns true if the piece at row, col has a lesser or equal power to the piece at selectedRow, SelectedCol
        return (game.board[selectedRow][selectedCol].charAt(1) >= game.board[row][col].charAt(1)) && !isWater(selectedRow, selectedCol);
    }

    private boolean isLionOrTiger(int row, int col){
        // returns true if the piece at row, col is a lion or a tiger, else returns false.
        return game.board[row][col].charAt(1) == '7' || game.board[row][col].charAt(1) == '6';
    }

    private boolean isRat(int row, int col){
        // returns true if the piece at row, col is a rat, else returns false.
        return game.board[row][col].charAt(1) == '1';
    }

    private boolean isWater(int row, int col){
        // returns true if the tile at row, col contains water, else returns false.
        return (row == 3 || row == 4 || row == 5) && (col == 1 || col == 2 || col == 4 || col == 5);
    }

    private boolean isTrap(int row, int col){
        // returns true if the tile at row, col contains a trap, else returns false.
        return (((row == 0 || row == 8) && (col == 2 || col == 4)) || ((row == 1 || row == 7) && (col == 3)));
    }
}
