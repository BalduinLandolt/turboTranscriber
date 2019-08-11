/**
 * 
 */
package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

/**
 * @author Balduin Landolt
 *
 */
public abstract class AbstractAtomarTranscriptionSegment extends AbstractTranscriptionObject {
    private TTPlainTextSegment content;

    public AbstractAtomarTranscriptionSegment(TTPlainTextSegment content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content.getTextContent();
    }
}
