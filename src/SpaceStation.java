//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title:    Among Us virtual game to add Amoug, set an imposter, move Amoug, 
//           and have the imposter go around and remove the other players
// Course:   CS 300 Fall 2023
//
// Author:   Sierra Reschke
// Email:    sgreschke@wisc.edu
// Lecturer: Hobbes LeGault
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name:    N/A
// Partner Email:   (email address of your programming partner)
// Partner Lecturer's Name: (name of your partner's lecturer)
// 
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
//   ___ Write-up states that pair programming is allowed for this assignment.
//   ___ We have both read and understand the course Pair Programming Policy.
//   ___ We have registered our team prior to the team registration deadline.
//
///////////////////////// ALWAYS CREDIT OUTSIDE HELP //////////////////////////
//
// Persons:         N/A
// Online Sources:  Utilized chatGPT to figure out how to convert an int to
//                  a float value
//                  output: 
//                  int intValue = 42; // An example integer value
//                  float floatValue = (float) intValue; // Cast int to float
//                  Utilized Amogus class Javadoc to learn how the given 
//                  Amogus methods and constructors are defined
//
///////////////////////////////////////////////////////////////////////////////


import java.io.File;
import processing.core.PImage;

/**
 * The SpaceStation class allows for Amogus players to be added,
 * an imposter to be set, players to be moved, and the imposter to kill
 * other players
 * 
 * @author Sierra Reschke
 *
 */
public class SpaceStation {


  //private static int bgColor;
  //private static PImage sprite;
  private static PImage background;
  private static Amogus[] players;
  private static final int NUM_PLAYERS = 8;
  private static int impostorIndex;


  /**
   * Main method that launches this application
   * 
   * @param args list of input arguments if any
   */
  public static void main(String[] args) {
    Utility.runApplication(); // starts the application

  }

  /**
   * Setup and define variables
   * 
   */
  public static void setup() {
    //bgColor = Utility.randGen.nextInt(); // only initializes, doesn't affect display window
    //sprite = Utility.loadImage("images"+File.separator+"sprite1.png");
    background = Utility.loadImage("images"+File.separator+"background.jpeg");
    players = new Amogus[NUM_PLAYERS];
    int colorRandomInt = Utility.randGen.nextInt(3) + 1;
    players[0] = new Amogus(colorRandomInt);
    //nextIndex++;

    impostorIndex = Utility.randGen.nextInt(7) + 1; // between 1 and max players of 8
    System.out.println("Impostor index: " + impostorIndex);
  }

  /**
   * Draws background, prints players if index is valid and imposter is not covering
   */
  public static void draw() {
    Utility.image(background, 600,500);

    boolean mouseOverAnyAmogus = false;


    for (int i = 0; i < NUM_PLAYERS; i++ ) {
      if (players[i] != null) {

        boolean mouseOverCurrentAmogus = isMouseOver(players[i]);
        
        if (!mouseOverAnyAmogus && mouseOverCurrentAmogus) {
          mouseOverAnyAmogus = true;
        }

        for (int j = 0; j < NUM_PLAYERS; j++) {
          if (players[j] != null) {
            boolean isOverlapping = overlap(players[i], players[j]);
            if (isOverlapping) {
              if (players[j].isImpostor()) {
                players[i].unalive();
              }
            }
          }
        }

        if (players[i] != null) {
          players[i].draw();
        }

      }
    }

    //System.out.println("Mouse over any Amogus: " + mouseOverAnyAmogus);

  }
  /**
   * Determines if a key was pressed
   * 
   */
  public static void keyPressed() {
    
    //int nextIndex = 1;
    int counter = 0;
    
    for (int i = 0; i < NUM_PLAYERS; i++) {
      if (players[i] != null) {
        counter++;
      }
    }
    System.out.println(counter);

    if (counter == 8) {
      return;
    }
    
    if (Utility.key() == 'a' && counter < NUM_PLAYERS) {
      int randLocationX = Utility.randGen.nextInt(Utility.width()) + 1;
      int randLocationY = Utility.randGen.nextInt(Utility.height()) + 1;

      int colorRandomInt = Utility.randGen.nextInt(3) + 1;
      if (counter == impostorIndex) {
        players[counter] = new Amogus(colorRandomInt, randLocationX, randLocationY, true);
      }
      else {
        players[counter] = new Amogus(colorRandomInt, randLocationX, randLocationY, false);
      }
    }
  }
  
  /**
   * Determines if mouse is over an amogus
   * 
   */
  public static boolean isMouseOver(Amogus amogus) {
    float amogusCenterX = amogus.getX();
    //System.out.println("amogusCenterX: " + amogusCenterX);
    float amogusCenterY = amogus.getY();
    //System.out.println("amogusCenterY: " + amogusCenterY);

    int amogusWidth = amogus.image().width; 
    //System.out.println("amogusWidth: " + amogusWidth);
    int amogusHeight = amogus.image().height; 
    //System.out.println("amogusHeight: " + amogusHeight);

    float mouseLocationX = Utility.mouseX();
    //System.out.println("mouseX: " + mouseLocationX);
    float mouseLocationY = Utility.mouseY();
    //System.out.println("mouseY: " + mouseLocationY);

    float amogusLeftBound = amogusCenterX - (float) (amogusWidth/2);
    float amogusRightBound = amogusCenterX + (float) (amogusWidth/2);
    float amogusTopBound = amogusCenterY - (float) (amogusHeight/2);
    float amogusBottomBound = amogusCenterY + (float) (amogusHeight/2);

    if ((mouseLocationX > amogusLeftBound) && (mouseLocationX < amogusRightBound)) {
      if ((mouseLocationY < amogusBottomBound) && (mouseLocationY > amogusTopBound)) {
        return true;
      }
    }

    return false;
  }
  /**
   * Determines if mouse is pressed 
   */
  public static void mousePressed() {
    boolean mouseOver = false;
    int lowestIndexAmogus = 0;
    for (int i = 0; i < NUM_PLAYERS; i++) {
      if (players[i] != null && isMouseOver(players[i])) {
        mouseOver = true;
        lowestIndexAmogus = i;
        break;
      }
    }
    players[lowestIndexAmogus].startDragging();
  }

  /**
   * Determines if mouse is released and stops dragging
   */
  public static void mouseReleased() {
    for (int i = 0; i < NUM_PLAYERS; i++) {
      if (players[i] != null) {
        players[i].stopDragging();
      }
    }
  }
  
  /**
   * Create an empty new list String[][] with a given capacity. The list should be empty, meaning
   * that it must contain null references only. For instance if the capacity of the list to 
   * create is 4, the returned array must contain 4 null references.
   * 
   * @param amogus1 first amogus
   * @param amogus2 second amogus (order independent)
   */
  public static boolean overlap(Amogus amogus1, Amogus amogus2) {    
    // amogus1 coordinates
    float amogus1CenterX = amogus1.getX();
    float amogus1CenterY = amogus1.getY();

    int amogus1Width = amogus1.image().width; 
    int amogus1Height = amogus1.image().height; 

    float amogus1LeftBound = amogus1CenterX - (float) (amogus1Width/2);
    float amogus1RightBound = amogus1CenterX + (float) (amogus1Width/2);
    float amogus1TopBound = amogus1CenterY - (float) (amogus1Height/2);
    float amogus1BottomBound = amogus1CenterY + (float) (amogus1Height/2);


    // amogus2 coordinates
    float amogus2CenterX = amogus2.getX();
    float amogus2CenterY = amogus2.getY();

    int amogus2Width = amogus2.image().width; 
    int amogus2Height = amogus2.image().height; 

    float amogus2LeftBound = amogus2CenterX - (float) (amogus2Width/2);
    float amogus2RightBound = amogus2CenterX + (float) (amogus2Width/2);
    float amogus2TopBound = amogus2CenterY - (float) (amogus2Height/2);
    float amogus2BottomBound = amogus2CenterY + (float) (amogus2Height/2);

    if ((amogus1LeftBound < amogus2RightBound) 
        && (amogus2LeftBound < amogus1RightBound)
        && (amogus1TopBound < amogus2BottomBound)
        && (amogus2TopBound < amogus1BottomBound)) {
      return true;
    }

    return false;

  }

}
