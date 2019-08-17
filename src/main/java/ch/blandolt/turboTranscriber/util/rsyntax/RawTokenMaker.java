package ch.blandolt.turboTranscriber.util.rsyntax;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;

import javax.swing.text.Segment;

public class RawTokenMaker extends AbstractTokenMaker {
    private static final int NULL = Token.NULL;
    private static final int WHITESPACE = Token.WHITESPACE;
    private static final int TAG_DELIMITER = Token.MARKUP_TAG_DELIMITER;
    private static final int ABBREVIATION = Token.REGEX;
    private static final int COMMENT_EOL = Token.COMMENT_EOL;
    private static final int COMMENT_MULTILINE = Token.COMMENT_MULTILINE;
    private static final int TAG_NAME = Token.MARKUP_TAG_NAME;
    private static final int TEXT = Token.IDENTIFIER;
    private static final int GLYPH = Token.LITERAL_STRING_DOUBLE_QUOTE;

    private int currentTokenStart;
    private int currentTokenType;
    private boolean tag_open = false;
    private boolean abbreviation_open = false;
    private boolean glyph_open = false;

    // TODO: highlight comments after abbreviations/glyphs

    @Override
    public TokenMap getWordsToHighlight() {
        TokenMap map = new TokenMap();

        map.put("miracle", Token.RESERVED_WORD);

        return map;
    }

    @Override
    public Token getTokenList(Segment text, int initialTokenType, int startOffset) {
        resetTokenList();

        char[] array = text.array;
        int offset = text.offset;
        int count = text.count;
        int end = offset + count;



        // Token starting offsets are always of the form:
        // 'startOffset + (currentTokenStart-offset)', but since startOffset and
        // offset are constant, tokens' starting positions become:
        // 'newStartOffset+currentTokenStart'.
        int newStartOffset = startOffset - offset;

        currentTokenStart = offset;
        currentTokenType  = initialTokenType;

        for (int i=offset; i<end; i++) {

            char c = array[i];

            switch (currentTokenType) {

                case NULL:

                    currentTokenStart = i;   // Starting a new token here.

                    switch (c) {

                        case ' ':
                        case '\t':
                            currentTokenType = WHITESPACE;
                            break;

                        case '#':
                            currentTokenType = COMMENT_EOL;
                            break;

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                currentTokenType = COMMENT_MULTILINE;
                            }
                            break;

                        case '[':
                            currentTokenType = TAG_DELIMITER;
                            tag_open = true;
                            break;

                        case '(':
                            currentTokenType = ABBREVIATION;
                            abbreviation_open = true;
                            break;

                        case '{':
                            currentTokenType = GLYPH;
                            glyph_open = true;
                            break;

                        default:
                            currentTokenType = TEXT;
                            break;

                    } // End of switch (c).

                    break;

                case WHITESPACE:

                    switch (c) {

                        case ' ':
                        case '\t':
                            break; // still whitespace

                        case '#':
                            addToken(text, currentTokenStart,i-1, WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = COMMENT_EOL;
                            break;

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                addToken(text, currentTokenStart,i-1, WHITESPACE, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = COMMENT_MULTILINE;
                            }
                            break;

                        case '[':
                            addToken(text, currentTokenStart,i-1, WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = TAG_DELIMITER;
                            tag_open = true;
                            break;

                        case ']':
                            addToken(text, currentTokenStart,i-1, WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = TAG_DELIMITER;
                            tag_open = false;
                            break;

                        case '(':
                            addToken(text, currentTokenStart,i-1, WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = ABBREVIATION;
                            abbreviation_open = true;
                            break;

                        case '{':
                            addToken(text, currentTokenStart,i-1, WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = GLYPH;
                            glyph_open = true;
                            break;

                        default:
                            addToken(text, currentTokenStart,i-1, WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            if (tag_open)
                                currentTokenType = TAG_NAME;
                            else
                                currentTokenType = TEXT;
                            break;

                    } // End of switch (c).

                    break;

                default: // Should never happen
                case TEXT:

                    switch (c) {

                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart,i-1, TEXT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = WHITESPACE;
                            break;

                        case '#':
                            addToken(text, currentTokenStart,i-1, TEXT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = COMMENT_EOL;
                            break;

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                addToken(text, currentTokenStart,i-1, TEXT, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = COMMENT_MULTILINE;
                            }
                            break;

                        case '[':
                            addToken(text, currentTokenStart,i-1, TEXT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = TAG_DELIMITER;
                            tag_open = true;
                            break;

                        case ']':
                            addToken(text, currentTokenStart,i-1, TEXT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = TAG_DELIMITER;
                            tag_open = false;
                            break;

                        case '(':
                            addToken(text, currentTokenStart,i-1, TEXT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = ABBREVIATION;
                            abbreviation_open = true;
                            break;

                        case '{':
                            addToken(text, currentTokenStart,i-1, TEXT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = GLYPH;
                            glyph_open = true;
                            break;

                        default:
                            if (tag_open) {
                                addToken(text, currentTokenStart, i - 1, TEXT, newStartOffset + currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = TAG_NAME;
                            }
                            else
                                break;//still text
                            break;

                    } // End of switch (c).


                    break;

                case TAG_DELIMITER:

                    switch (c) {

                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart,i-1, TAG_DELIMITER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = WHITESPACE;
                            break;

                        case '#':
                            addToken(text, currentTokenStart,i-1, TAG_DELIMITER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = COMMENT_EOL;
                            break;

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                addToken(text, currentTokenStart,i-1, TAG_DELIMITER, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = COMMENT_MULTILINE;
                            }
                            break;

                        case ';':
                        case ':':
                        case '=':
                            addToken(text, currentTokenStart,i-1, TAG_DELIMITER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.OPERATOR;
                            break;

                        case ']':
                            addToken(text, currentTokenStart,i-1, TAG_DELIMITER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = TAG_DELIMITER;
                            tag_open = false;
                            break;

                        case '(':
                            addToken(text, currentTokenStart,i-1, TAG_DELIMITER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = ABBREVIATION;
                            abbreviation_open = true;
                            break;

                        case '{':
                            addToken(text, currentTokenStart,i-1, TAG_DELIMITER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = GLYPH;
                            glyph_open = true;
                            break;

                        default:
                            addToken(text, currentTokenStart,i-1, TAG_DELIMITER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            if (tag_open)
                                currentTokenType = TAG_NAME;
                            else
                                currentTokenType = TEXT;
                            break;

                    } // End of switch (c).
                    break;

                case Token.OPERATOR:
                    addToken(text, currentTokenStart,i-1, Token.OPERATOR, newStartOffset+currentTokenStart);
                    currentTokenStart = i;


                    switch (c) {

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                currentTokenType = COMMENT_MULTILINE;
                            }
                            break;

                        case '[':
                            currentTokenType = TAG_DELIMITER;
                            tag_open = true;
                            break;

                        case ']':
                            currentTokenType = TAG_DELIMITER;
                            tag_open = false;
                            break;

                        case '(':
                            currentTokenType = ABBREVIATION;
                            abbreviation_open = true;
                            break;

                        case '{':
                            currentTokenType = GLYPH;
                            glyph_open = true;
                            break;

                        default:
                            if (tag_open)
                                currentTokenType = TAG_NAME;
                            else
                                currentTokenType = TEXT;
                            break;

                    } // End of switch (c).
                    break;

                case COMMENT_EOL:
                    i = end - 1;
                    addToken(text, currentTokenStart,i, currentTokenType, newStartOffset+currentTokenStart);
                    // We need to set token type to null so at the bottom we don't add one more token.
                    currentTokenType = NULL;
                    break;

                case COMMENT_MULTILINE:
                    if (c == '/' && i > 0 && array[i-1]=='*'){
                        i++;
                        addToken(text, currentTokenStart,i-1, COMMENT_MULTILINE, newStartOffset+currentTokenStart);
                        currentTokenStart = i;
                        if (array.length>i){
                            c = array[i];
                        } else {
                            currentTokenType = COMMENT_MULTILINE;
                            break;
                        }
                        switch (c) {

                            case ' ':
                            case '\t':
                                currentTokenType = WHITESPACE;
                                break;

                            case '#':
                                currentTokenType = COMMENT_EOL;
                                break;

                            case '/':
                                if (i+1 < array.length && array[i+1] == '*') {
                                    currentTokenType = COMMENT_MULTILINE;
                                }
                                break;

                            case '[':
                                currentTokenType = TAG_DELIMITER;
                                tag_open = true;
                                break;

                            case ']':
                                currentTokenType = TAG_DELIMITER;
                                tag_open = false;
                                break;

                            case '(':
                                currentTokenType = ABBREVIATION;
                                abbreviation_open = true;
                                break;

                            case '{':
                                currentTokenType = GLYPH;
                                glyph_open = true;
                                break;

                            default:
                                if (tag_open) {
                                    currentTokenType = TAG_NAME;
                                } else {
                                    currentTokenType = TEXT;
                                }
                                break;

                        } // End of switch (c).
                    } else {
                    }
                    break;

                case TAG_NAME:


                    switch (c) {

                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart,i-1, TAG_NAME, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = WHITESPACE;
                            break;

                        case ';':
                        case ':':
                        case '=':
                            addToken(text, currentTokenStart,i-1, TAG_NAME, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.OPERATOR;
                            break;

                        case '#':
                            addToken(text, currentTokenStart,i-1, TAG_NAME, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = COMMENT_EOL;
                            break;

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                addToken(text, currentTokenStart,i-1, TAG_NAME, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = COMMENT_MULTILINE;
                            }
                            break;

                        case ']':
                            addToken(text, currentTokenStart,i-1, TAG_NAME, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = TAG_DELIMITER;
                            tag_open = false;
                            break;

                        default:
                            if (tag_open)
                                break;
                            else {
                                addToken(text, currentTokenStart,i-1, TAG_NAME, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = TEXT;
                            }
                            break;

                    } // End of switch (c).
                    break;

                case ABBREVIATION: // abbreviation


                    switch (c) {

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                addToken(text, currentTokenStart,i-1, ABBREVIATION, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = COMMENT_MULTILINE;
                            }
                            break;

                        case '{':
                            addToken(text, currentTokenStart,i-1, ABBREVIATION, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = GLYPH;
                            break;

                        case ')':
                            i++;
                            addToken(text, currentTokenStart,i-1, ABBREVIATION, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            abbreviation_open= false;
                            if (array.length <= i){
                                currentTokenType = NULL;
                                break;
                            } else {
                                c = array[i];
                                switch (c){
                                    case '[':
                                        currentTokenType = TAG_DELIMITER;
                                        break;
                                    default:
                                        currentTokenType = TEXT;
                                } // end switch c
                            }
                            break;

                        default:
                            break;

                    } // End of switch (c).
                    break;

                case GLYPH: // abbreviation


                    switch (c) {

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                addToken(text, currentTokenStart,i-1, GLYPH, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = COMMENT_MULTILINE;
                            }
                            break;

                        case '}':
                            i++;
                            addToken(text, currentTokenStart,i-1, GLYPH, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            glyph_open = false;
                            if (array.length <= i){
                                currentTokenType = NULL;
                                break;
                            } else {
                                c = array[i];
                                switch (c){
                                    case '[':
                                        currentTokenType = TAG_DELIMITER;
                                        break;
                                    default:
                                        if (abbreviation_open)
                                            currentTokenType = ABBREVIATION;
                                        else
                                            currentTokenType = TEXT;
                                } // end switch c
                            }
                            break;

                        default:
                            break;

                    } // End of switch (c).
                    break;


            } // End of switch (currentTokenType).

        } // End of for (int i=offset; i<end; i++).

        switch (currentTokenType) {

            // Remember what token type to begin the next line with.
            case COMMENT_MULTILINE:
                addToken(text, currentTokenStart,end-1, currentTokenType, newStartOffset+currentTokenStart);
                break;

            // Do nothing if everything was okay.
            case NULL:
                addNullToken();
                break;

            // All other token types don't continue to the next line...
            default:
                addToken(text, currentTokenStart,end-1, currentTokenType, newStartOffset+currentTokenStart);
                addNullToken();

        }

        // Return the first token in our linked list.
        return firstToken;
    }

    @Override
    public void addToken(Segment segment, int start, int end, int tokenType, int startOffset) {
        // TODO: edit, whatever needs editing
        super.addToken(segment, start, end, tokenType, startOffset);
    }
}
