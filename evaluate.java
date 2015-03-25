/**
 * Created by prasanna on 2015-03-24.
 */
class evaluate {
    int evaluate(QuartoPiece newPiece, int pieceCount  int oldScore) {
/*This is a skeleton of the code that needs to be done */
        int newScore;
        if (pieceCount == 0) {
            return 1;
        }
        /* if along diagonal add the elements in common to the score */
        int row = newPiece.getRow() ;
        int column = newPiece.getColumn();
        if (row == column){
            /* do the calculating shit */
        }
        /* add common elements along row to score */


        /* add common elements along column to score */



        return (oldScore+newScore)/(pieceCount+1);
    }
}
