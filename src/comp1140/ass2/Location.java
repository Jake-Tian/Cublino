package comp1140.ass2;

public class Location {
    // The respective coordinates of the horizontal and vertical
    private int x;
    private int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Location(String loc){
        int col = loc.charAt(0)-96;
        int row = Integer.parseInt(loc.substring(1));
        this.x = col;
        this.y = row;
    }

    public String toString(){
        char col = (char) (x + 96);
        int row = y;
        return col + "" + row;
    }

    // Getters and Setters

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Return whether the current point is same as the other given point.
     * Two points are equal if they have the same x coordinate and y coordinate.
     *
     * @param other The other location needs to compare with
     * @return Whether two points are the same
     */
    public boolean equals(Location other) {
        return (x == other.x) && (y == other.y);
    }

    /**
     * To determine if it is possible to jump to other location
     *
     * @param other The destination of jumping piece
     * @param state The current state on the board
     * @return Whether the piece can get to the location
     */
     public boolean isValidJump(Location other, Piece[] state) {

         Location midPoint;     // Define a midPoint for the current Location to jump through

         // Find the Location of the midPoint

         // The point is jumping vertically
         if (x == other.x && Math.abs(y-other.y) == 2) midPoint = new Location(x,(y+other.y)/2);
         // The point is jumping horizontally
         else if (y == other.y && Math.abs(x-other.x) == 2) midPoint = new Location((x+other.x)/2,y);
         // The jumping point and its destination must be on the same line with a distance of 2
         else return false;

         boolean midOnState = false;            // If the midPoint is on the state
         boolean destinationTaken = false;      // If there is already a point taken on the destination

         // Check every piece in the state
         for (Piece piece : state) {
             if (piece != null) {
                 if (midPoint.equals(piece.getLoc())) midOnState = true;
                 if (other.equals(piece.getLoc())) destinationTaken = true;
             }
         }

         // The midPoint must appear on the state and the destination cannot be taken
         return (midOnState && !destinationTaken);
     }





    /**
     * Returns the distance of pieces inline with each other
     * @param other The other location to measure the distance to
     * @return The distance in units
     */
    public int getDistance(Location other) {
        return Math.abs(other.getX() - this.x) + Math.abs(other.getY() - this.y);
    }

}
