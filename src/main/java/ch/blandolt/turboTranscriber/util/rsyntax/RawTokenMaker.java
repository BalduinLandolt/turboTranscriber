package ch.blandolt.turboTranscriber.util.rsyntax;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;

import javax.swing.text.Segment;

public class RawTokenMaker extends AbstractTokenMaker {
    public static final int NONE = 0;
    public static final int WHITESPACE = 1;
    public static final int TEXT = 2;
    public static final int DELIMITER = 3;
    public static final int COMMENT_INLINE = 4;
    public static final int COMMENT_MULTI = 5;
    public static final int OTHERS = 99; // TODO: expand (glyph, abbreviation, ...)

    private int currentTokenStart;
    private int currentTokenType;
    private int delimiter_open = 0;

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

                case NONE:

                    currentTokenStart = i;   // Starting a new token here.

                    switch (c) {

                        case ' ':
                        case '\t':
                            currentTokenType = WHITESPACE;
                            break;

                        case '#':
                            currentTokenType = COMMENT_INLINE;
                            break;

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                currentTokenType = COMMENT_MULTI;
                            }
                            break;

                        case '{':
                        case '[':
                        case '(':
                            currentTokenType = DELIMITER;
                            delimiter_open++;
                            break;

                        case '}':
                        case ']':
                        case ')':
                            currentTokenType = DELIMITER;
                            delimiter_open--;
                            break;

                        default:
                            if (delimiter_open > 0)
                                currentTokenType = OTHERS;
                            else
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
                            currentTokenType = COMMENT_INLINE;
                            break;

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                addToken(text, currentTokenStart,i-1, WHITESPACE, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = COMMENT_MULTI;
                            }
                            break;

                        case '{':
                        case '[':
                        case '(':
                            addToken(text, currentTokenStart,i-1, WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = DELIMITER;
                            delimiter_open++;
                            break;

                        case '}':
                        case ']':
                        case ')':
                            addToken(text, currentTokenStart,i-1, WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = DELIMITER;
                            delimiter_open--;
                            break;

                        default:
                            addToken(text, currentTokenStart,i-1, WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            if (delimiter_open > 0)
                                currentTokenType = OTHERS;
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
                            currentTokenType = COMMENT_INLINE;
                            break;

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                addToken(text, currentTokenStart,i-1, TEXT, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = COMMENT_MULTI;
                            }
                            break;

                        case '{':
                        case '[':
                        case '(':
                            addToken(text, currentTokenStart,i-1, TEXT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = DELIMITER;
                            delimiter_open++;
                            break;

                        case '}':
                        case ']':
                        case ')':
                            addToken(text, currentTokenStart,i-1, TEXT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = DELIMITER;
                            delimiter_open--;
                            break;

                        default:
                            if (delimiter_open > 0) {
                                addToken(text, currentTokenStart, i - 1, TEXT, newStartOffset + currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = OTHERS;
                            }
                            else
                                break;//still text
                            break;

                    } // End of switch (c).


                    break;

                case DELIMITER:

                    switch (c) {

                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart,i-1, DELIMITER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = WHITESPACE;
                            break;

                        case '#':
                            addToken(text, currentTokenStart,i-1, DELIMITER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = COMMENT_INLINE;
                            break;

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                addToken(text, currentTokenStart,i-1, DELIMITER, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = COMMENT_MULTI;
                            }
                            break;

                        case '{':
                        case '[':
                        case '(':
                            addToken(text, currentTokenStart,i-1, DELIMITER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = DELIMITER;
                            delimiter_open++;
                            break;

                        case '}':
                        case ']':
                        case ')':
                            addToken(text, currentTokenStart,i-1, DELIMITER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = DELIMITER;
                            delimiter_open--;
                            break;

                        default:
                            addToken(text, currentTokenStart,i-1, DELIMITER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            if (delimiter_open > 0)
                                currentTokenType = OTHERS;
                            else
                                currentTokenType = TEXT;
                            break;

                    } // End of switch (c).
                    break;

                case COMMENT_INLINE:
                    i = end - 1;
                    addToken(text, currentTokenStart,i, currentTokenType, newStartOffset+currentTokenStart);
                    // We need to set token type to null so at the bottom we don't add one more token.
                    currentTokenType = Token.NULL;
                    break;

                case COMMENT_MULTI:
                    if (c != '/' && i > 1 && array[i-1]=='/' && array[i-2]=='*'){
                        addToken(text, currentTokenStart,i-1, COMMENT_MULTI, newStartOffset+currentTokenStart);
                        currentTokenStart = i;
                        switch (c) {

                            case ' ':
                            case '\t':
                                currentTokenType = WHITESPACE;
                                break;

                            case '#':
                                currentTokenType = COMMENT_INLINE;
                                break;

                            case '/':
                                if (i+1 < array.length && array[i+1] == '*') {
                                    currentTokenType = COMMENT_MULTI;
                                }
                                break;

                            case '{':
                            case '[':
                            case '(':
                                currentTokenType = DELIMITER;
                                delimiter_open++;
                                break;

                            case '}':
                            case ']':
                            case ')':
                                currentTokenType = DELIMITER;
                                delimiter_open--;
                                break;

                            default:
                                if (delimiter_open > 0) {
                                    currentTokenType = OTHERS;
                                } else {
                                    currentTokenType = TEXT;
                                }
                                break;

                        } // End of switch (c).
                    } else {
                    }
                    break;

                case OTHERS:


                    switch (c) {

                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart,i-1, OTHERS, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = WHITESPACE;
                            break;

                        case '#':
                            addToken(text, currentTokenStart,i-1, OTHERS, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = COMMENT_INLINE;
                            break;

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                addToken(text, currentTokenStart,i-1, OTHERS, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = COMMENT_MULTI;
                            }
                            break;

                        case '{':
                        case '[':
                        case '(':
                            addToken(text, currentTokenStart,i-1, OTHERS, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = DELIMITER;
                            delimiter_open++;
                            break;

                        case '}':
                        case ']':
                        case ')':
                            addToken(text, currentTokenStart,i-1, OTHERS, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = DELIMITER;
                            delimiter_open--;
                            break;

                        default:
                            if (delimiter_open > 0)
                                break;
                            else {
                                addToken(text, currentTokenStart,i-1, OTHERS, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = TEXT;
                            }
                            break;

                    } // End of switch (c).
                    break;


            } // End of switch (currentTokenType).

        } // End of for (int i=offset; i<end; i++).

        switch (currentTokenType) {

            // Remember what token type to begin the next line with.
            case COMMENT_MULTI:
                addToken(text, currentTokenStart,end-1, currentTokenType, newStartOffset+currentTokenStart);
                break;

            // Do nothing if everything was okay.
            case NONE:
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
        switch (tokenType){
            case NONE: tokenType = Token.NULL;
                break;
            case WHITESPACE: tokenType = Token.WHITESPACE;
                break;
            case TEXT: tokenType = Token.IDENTIFIER;
                break;
            case DELIMITER: tokenType = Token.MARKUP_TAG_DELIMITER;
                break;
            case COMMENT_INLINE: tokenType = Token.COMMENT_EOL;
                break;
            case COMMENT_MULTI: tokenType = Token.COMMENT_MULTILINE;
                break;
            default: tokenType = Token.MARKUP_TAG_NAME;
                break;
        }
        super.addToken(segment, start, end, tokenType, startOffset);
    }
}
