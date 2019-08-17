package ch.blandolt.turboTranscriber.util.rsyntax;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;

import javax.swing.text.Segment;

public class RawTokenMaker extends AbstractTokenMaker {

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

                case Token.NULL:

                    currentTokenStart = i;   // Starting a new token here.

                    switch (c) {

                        case ' ':
                        case '\t':
                            currentTokenType = Token.WHITESPACE;
                            break;

                        case '#':
                            currentTokenType = Token.COMMENT_EOL;
                            break;

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                currentTokenType = Token.COMMENT_MULTILINE;
                            }
                            break;

                        case '{':
                        case '[':
                        case '(':
                            currentTokenType = Token.MARKUP_TAG_DELIMITER;
                            delimiter_open++;
                            break;

                        case '}':
                        case ']':
                        case ')':
                            currentTokenType = Token.MARKUP_TAG_DELIMITER;
                            delimiter_open--;
                            break;

                        default:
                            if (delimiter_open > 0)
                                currentTokenType = Token.MARKUP_TAG_NAME;
                            else
                                currentTokenType = Token.IDENTIFIER;
                            break;

                    } // End of switch (c).

                    break;

                case Token.WHITESPACE:

                    switch (c) {

                        case ' ':
                        case '\t':
                            break; // still whitespace

                        case '#':
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.COMMENT_EOL;
                            break;

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = Token.COMMENT_MULTILINE;
                            }
                            break;

                        case '{':
                        case '[':
                        case '(':
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.MARKUP_TAG_DELIMITER;
                            delimiter_open++;
                            break;

                        case '}':
                        case ']':
                        case ')':
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.MARKUP_TAG_DELIMITER;
                            delimiter_open--;
                            break;

                        default:
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            if (delimiter_open > 0)
                                currentTokenType = Token.MARKUP_TAG_NAME;
                            else
                                currentTokenType = Token.IDENTIFIER;
                            break;

                    } // End of switch (c).

                    break;

                default: // Should never happen
                case Token.IDENTIFIER:

                    switch (c) {

                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            break;

                        case '#':
                            addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.COMMENT_EOL;
                            break;

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = Token.COMMENT_MULTILINE;
                            }
                            break;

                        case '{':
                        case '[':
                        case '(':
                            addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.MARKUP_TAG_DELIMITER;
                            delimiter_open++;
                            break;

                        case '}':
                        case ']':
                        case ')':
                            addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.MARKUP_TAG_DELIMITER;
                            delimiter_open--;
                            break;

                        default:
                            if (delimiter_open > 0) {
                                addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = Token.MARKUP_TAG_NAME;
                            }
                            else
                                break;//still text
                            break;

                    } // End of switch (c).


                    break;

                case Token.MARKUP_TAG_DELIMITER:

                    switch (c) {

                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart,i-1, Token.MARKUP_TAG_DELIMITER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            break;

                        case '#':
                            addToken(text, currentTokenStart,i-1, Token.MARKUP_TAG_DELIMITER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.COMMENT_EOL;
                            break;

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                addToken(text, currentTokenStart,i-1, Token.MARKUP_TAG_DELIMITER, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = Token.COMMENT_MULTILINE;
                            }
                            break;

                        case '{':
                        case '[':
                        case '(':
                            addToken(text, currentTokenStart,i-1, Token.MARKUP_TAG_DELIMITER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.MARKUP_TAG_DELIMITER;
                            delimiter_open++;
                            break;

                        case '}':
                        case ']':
                        case ')':
                            addToken(text, currentTokenStart,i-1, Token.MARKUP_TAG_DELIMITER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.MARKUP_TAG_DELIMITER;
                            delimiter_open--;
                            break;

                        default:
                            addToken(text, currentTokenStart,i-1, Token.MARKUP_TAG_DELIMITER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            if (delimiter_open > 0)
                                currentTokenType = Token.MARKUP_TAG_NAME;
                            else
                                currentTokenType = Token.IDENTIFIER;
                            break;

                    } // End of switch (c).
                    break;

                case Token.COMMENT_EOL:
                    i = end - 1;
                    addToken(text, currentTokenStart,i, currentTokenType, newStartOffset+currentTokenStart);
                    // We need to set token type to null so at the bottom we don't add one more token.
                    currentTokenType = Token.NULL;
                    break;

                case Token.COMMENT_MULTILINE:
                    if (c == '/' && i > 0 && array[i-1]=='*'){
                        i++;
                        addToken(text, currentTokenStart,i-1, Token.COMMENT_MULTILINE, newStartOffset+currentTokenStart);
                        currentTokenStart = i;
                        if (array.length>i){
                            c = array[i];
                        } else {
                            currentTokenType = Token.COMMENT_MULTILINE;
                            break;
                        }
                        switch (c) {

                            case ' ':
                            case '\t':
                                currentTokenType = Token.WHITESPACE;
                                break;

                            case '#':
                                currentTokenType = Token.COMMENT_EOL;
                                break;

                            case '/':
                                if (i+1 < array.length && array[i+1] == '*') {
                                    currentTokenType = Token.COMMENT_MULTILINE;
                                }
                                break;

                            case '{':
                            case '[':
                            case '(':
                                currentTokenType = Token.MARKUP_TAG_DELIMITER;
                                delimiter_open++;
                                break;

                            case '}':
                            case ']':
                            case ')':
                                currentTokenType = Token.MARKUP_TAG_DELIMITER;
                                delimiter_open--;
                                break;

                            default:
                                if (delimiter_open > 0) {
                                    currentTokenType = Token.MARKUP_TAG_NAME;
                                } else {
                                    currentTokenType = Token.IDENTIFIER;
                                }
                                break;

                        } // End of switch (c).
                    } else {
                    }
                    break;

                case Token.MARKUP_TAG_NAME:


                    switch (c) {

                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart,i-1, Token.MARKUP_TAG_NAME, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            break;

                        case '#':
                            addToken(text, currentTokenStart,i-1, Token.MARKUP_TAG_NAME, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.COMMENT_EOL;
                            break;

                        case '/':
                            if (i+1 < array.length && array[i+1] == '*') {
                                addToken(text, currentTokenStart,i-1, Token.MARKUP_TAG_NAME, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = Token.COMMENT_MULTILINE;
                            }
                            break;

                        case '{':
                        case '[':
                        case '(':
                            addToken(text, currentTokenStart,i-1, Token.MARKUP_TAG_NAME, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.MARKUP_TAG_DELIMITER;
                            delimiter_open++;
                            break;

                        case '}':
                        case ']':
                        case ')':
                            addToken(text, currentTokenStart,i-1, Token.MARKUP_TAG_NAME, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.MARKUP_TAG_DELIMITER;
                            delimiter_open--;
                            break;

                        default:
                            if (delimiter_open > 0)
                                break;
                            else {
                                addToken(text, currentTokenStart,i-1, Token.MARKUP_TAG_NAME, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = Token.IDENTIFIER;
                            }
                            break;

                    } // End of switch (c).
                    break;


            } // End of switch (currentTokenType).

        } // End of for (int i=offset; i<end; i++).

        switch (currentTokenType) {

            // Remember what token type to begin the next line with.
            case Token.COMMENT_MULTILINE:
                addToken(text, currentTokenStart,end-1, currentTokenType, newStartOffset+currentTokenStart);
                break;

            // Do nothing if everything was okay.
            case Token.NULL:
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
