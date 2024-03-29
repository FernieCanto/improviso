package improviso;

import java.util.*;
import org.w3c.dom.Element;
/**
 * RESPONSABILIDADES DA TRILHA
 *  - Buscar próximo padrão e devolver noteDefinitions (todas, ou com determinada duração)
 *  - Identificar corretamente o fim de sua execução
 * @author fernando
 */
public class Track {
    protected GroupMessage message;
    protected Pattern currentPattern;
    protected Group rootGroup;
    protected int currentPosition;
    protected double relativePosition = 0.0;
   
    Track(Group g) {
        this.rootGroup = g;
        this.currentPosition = 0;
    }

    /**
     * Produces a Track from a "Track" XML element. All underlying elements will
     * be generated as well.
     * @param XMLLib Library of XML elements
     * @param element "Track" element to be processed
     * @return 
     */
    static Track generateTrackXML(XMLLibrary XMLLib, Element element)
        throws ImprovisoException {
        Track t;
        Group g;
        
        if(element.hasAttribute("group"))
            g = Group.generateGroupXML(XMLLib, XMLLib.groups.get(element.getAttribute("group")));
        else
            g = Group.generateGroupXML(XMLLib, (Element)element.getChildNodes().item(1));
        t = new Track(g);
        
        return t;
    }
    
    /**
     * Recovers the next Pattern to be executed by sending a message to the
     * root Group of the Track. The Message produced by the Groups will be
     * returned
     * @return Message produced by the Group tree
     */
    public GroupMessage selectNextPattern() {
        message = this.rootGroup.execute();
        currentPattern = this.rootGroup.getSelectedPattern();
        return message;
    }
    
    /**
     * Prepares the Track for a new execution of its Section, updating its
     * current position and resetting its Group tree.
     * @param position 
     */
    public void initialize(int position) {
        this.currentPosition = position;
        this.relativePosition = 0.0;
        this.rootGroup.resetGroup();
    }
    
    /**
     * Get the Track's current position within the Composition.
     * @return Position in ticks
     */
    public int getCurrentPosition() {
      return currentPosition;
    }
    
    /**
     * Obtains the ending position of the currently selected Pattern.
     * @return 
     */
    public int getEnd() {
      return currentPosition + currentPattern.getLength();
    }
    
    /**
     * Fully executes the last selected Pattern of the Track, given the Track's
     * current position within the Section, receiving the list of Notes produced
     * by the Pattern.
     * @param position The position of the Track in the Section.
     * @return 
     */
    public ArrayList<Note> execute(double newRelativePosition) {
        return this.execute(newRelativePosition, Pattern.WHOLE);
    }
    
    /**
     * Executes the last selected Pattern of the Track, given the Track's
     * current position within the Section and the maximum allowed duration for
     * the Pattern, receiving the list of Notes produced by the Pattern.
     * @param newRelativePosition The position of the Track in the Section.
     * @param duration The maximum duration for the Pattern. All notes that exceed
     * that duration will be discarded.
     * @return Sequência de noteDefinitions geradas.
     */
    public ArrayList<Note> execute(double newRelativePosition, int length) {
        ArrayList<Note> notes = currentPattern.execute(this.currentPosition, relativePosition, newRelativePosition, length);
        this.currentPosition += currentPattern.getLength();
        relativePosition = newRelativePosition;
        return notes;
    }
}