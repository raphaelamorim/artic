package br.ufrgs.artic.parser.omnipage;

import br.ufrgs.artic.exceptions.ParserException;
import br.ufrgs.artic.model.*;
import br.ufrgs.artic.parser.PageParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

import static br.ufrgs.artic.utils.XMLUtils.getElementsByTagName;

/**
 * The page parser for OmniPage Professional Version 18.
 */
public class OmniPageParser implements PageParser {

    public Page getPage(String pathToFile) throws ParserException {

        double averagePageFontSize = 0;
        int biggestTop = 0;
        int biggestLeft = 0;

        Element pageElement = getPageAsDocument(pathToFile).getDocumentElement();
        List<Element> paragraphs = getElementsByTagName("para", pageElement);

        int lineCounter = 0;
        int fontSizeSum = 0;
        Element previousWorkingElement = null;
        if (paragraphs != null && !paragraphs.isEmpty()) {

            for (Element paragraphElement : paragraphs) {

                List<Element> linesOfParagraph = getElementsByTagName("ln", paragraphElement);

                if (linesOfParagraph != null && !linesOfParagraph.isEmpty()) {
                    for (Element lineElement : linesOfParagraph) {
                        String fontFace = lineElement.getAttribute("fontFace");

                        if (fontFace == null || fontFace.isEmpty()) { //if yes, start run merge process
                            augmentLineElement(lineElement, previousWorkingElement);
                        } else {
                            previousWorkingElement = lineElement;
                        }

                        if (lineElement.getAttribute("t") != null && !lineElement.getAttribute("t").isEmpty() &&
                                lineElement.getAttribute("l") != null && !lineElement.getAttribute("l").isEmpty()) {
                            int top = Integer.parseInt(lineElement.getAttribute("t").replaceAll(",", "\\."));

                            if (top > biggestTop) {
                                biggestTop = top;
                            }

                            int left = Integer.parseInt(lineElement.getAttribute("l").replaceAll(",", "\\."));

                            if (left > biggestLeft) {
                                biggestLeft = left;
                            }
                        }

                        double fontSize = 0;
                        if (lineElement.getAttribute("fontSize") != null && !lineElement.getAttribute("fontSize").isEmpty()) {
                            fontSize = Double.valueOf(lineElement.getAttribute("fontSize").replaceAll(",", "\\."));
                        }


                        fontSizeSum += fontSize;
                        lineCounter++;
                    }
                }
            }

            averagePageFontSize = (double) fontSizeSum / lineCounter;
        }

        Page page = new Page(averagePageFontSize, biggestTop, biggestLeft);
        List<Line> lines = getLines(pageElement, page);
        page.setLines(lines);

        return page;
    }

    private Document getPageAsDocument(String pathToXML) throws ParserException {
        try (InputStream inputStream = new FileInputStream(pathToXML)) {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            return db.parse(inputStream);

        } catch (ParserConfigurationException | SAXException e) {
            throw new ParserException(String.format("The content of %s is an invalid XML file for OmniPage.", pathToXML), e);
        } catch (IOException e) {
            throw new ParserException(String.format("Could not open %s. The does not exists or do not allow us to open.", pathToXML), e);
        }
    }

    private List<Line> getLines(Element pageElement, Page page) {
        List<Line> lines = new ArrayList<>();

        List<Element> sections = getElementsByTagName("section", pageElement);

        List<Element> paragraphs = getElementsByTagName("para", pageElement);

        Integer lineCounter = 0;
        Boolean foundIntroOrAbstract = false;
        Line previousLine = null;
        Word previousWord = null;

        Context context = null;
        Element currentContext = sections.get(0);
        if (paragraphs != null && !paragraphs.isEmpty()) {

            for (Element paragraphElement : paragraphs) {

                List<Element> sectionParagraphs = getElementsByTagName("para", currentContext);
                if (context == null || !sectionParagraphs.contains(paragraphElement)) {
                    context = new Context();
                }

                Paragraph paragraph = Paragraph.NEW;

                List<Element> linesOfParagraph = getElementsByTagName("ln", paragraphElement);

                if (linesOfParagraph != null && !linesOfParagraph.isEmpty()) {
                    int currentLineIndex = 0;
                    for (Element lineElement : linesOfParagraph) {

                        String lineContent = getOriginalText(lineElement);

                        if (lineContent != null && Pattern.compile("intro|abstract", Pattern.CASE_INSENSITIVE).
                                matcher(lineContent).find()) {
                            foundIntroOrAbstract = true;
                        }

                        Line line = getLine(page, lineCounter, foundIntroOrAbstract, previousLine,
                                paragraphElement, paragraph, lineElement, lineContent);

                        lines.add(line);

                        List<Element> words = getElementsByTagName("wd", lineElement);
                        int currentWordIndex = 0;

                        for (Element wordElement : words) {

                            Word word = getWord(page, previousWord, paragraphElement.getAttribute("alignment"),
                                    line, currentWordIndex, currentLineIndex, wordElement, context);

                            line.getWords().add(word);
                            previousWord = word;

                            currentWordIndex++;
                        }

                        paragraph = Paragraph.SAME;
                        previousLine = line;
                        lineCounter++;
                        currentLineIndex++;
                    }
                }
            }
        }

        return lines;
    }

    private Word getWord(Page page, Word previousWord, String alignment, Line line, int currentWordIndex, int currentLineIndex, Element wordElement, Context context) {

        String wordContent = getOriginalText(wordElement);

        String fontFace = wordElement.getAttribute("fontFace");
        fontFace = (fontFace != null && !fontFace.isEmpty()) ? fontFace : line.getFontFace();

        FontSize fontSize = (wordElement.getAttribute("fontSize") != null &&
                !wordElement.getAttribute("fontSize").isEmpty()) ?
                getFontSize(wordElement, page.getAverageFontSize()) : line.getFontSize();

        boolean bold = (wordElement.getAttribute("bold") != null &&
                !wordElement.getAttribute("bold").isEmpty()) ?
                getBooleanValue(wordElement.getAttribute("bold")) : line.getBold();

        boolean italic = (wordElement.getAttribute("italic") != null &&
                !wordElement.getAttribute("italic").isEmpty()) ?
                getBooleanValue(wordElement.getAttribute("italic")) : line.getItalic();

        boolean underline = (wordElement.getAttribute("underlined") != null &&
                !wordElement.getAttribute("underlined").isEmpty()) ?
                getBooleanValue(wordElement.getAttribute("underlined"), "none") : line.getUnderline();

        return new Word.Builder(currentWordIndex, wordContent, context)
                .previousWord(previousWord).line(line).lineIndex(currentLineIndex)
                .alignment(Alignment.get(alignment))
                .bold(bold)
                .italic(italic)
                .underline(underline)
                .fontSize(fontSize)
                .fontFace(fontFace)
                .left(Integer.parseInt(wordElement.getAttribute("l")))
                .top(Integer.parseInt(wordElement.getAttribute("t")))
                .right(Integer.parseInt(wordElement.getAttribute("r")))
                .bottom(Integer.parseInt(wordElement.getAttribute("b"))).build();
    }

    private Line getLine(Page page, Integer lineCounter, Boolean foundIntroOrAbstract,
                         Line previousLine, Element paragraphElement, Paragraph paragraph,
                         Element lineElement, String textContent) {
        Line.Builder lineBuilder = new Line.Builder(lineCounter, page)
                .alignment(Alignment.get(paragraphElement.getAttribute("alignment")))
                .previousLine(previousLine)
                .content(textContent)
                .bold(getBooleanValue(lineElement.getAttribute("bold")))
                .italic(getBooleanValue(lineElement.getAttribute("italic")))
                .underline(getBooleanValue(lineElement.getAttribute("underlined"), "none"))
                .fontSize(getFontSize(lineElement, page.getAverageFontSize()))
                .fontFace(lineElement.getAttribute("fontFace"))
                .left(Integer.parseInt(lineElement.getAttribute("l")))
                .top(Integer.parseInt(lineElement.getAttribute("t")))
                .right(Integer.parseInt(lineElement.getAttribute("r")))
                .bottom(Integer.parseInt(lineElement.getAttribute("b")));


        if (!foundIntroOrAbstract) {  //special case for headers
            lineBuilder.paragraph(Paragraph.HEADER);
        } else {
            lineBuilder.paragraph(paragraph);
        }

        //create the line instance here
        return lineBuilder.build();
    }

    private boolean getBooleanValue(String valueString, String falseValue) {
        return valueString != null && !valueString.isEmpty() && !valueString.equals(falseValue);
    }

    public FontSize getFontSize(Element element, double averageFontSize) {
        FontSize fontSize = FontSize.NORMAL;
        if (element.getAttribute("fontSize") != null && !element.getAttribute("fontSize").isEmpty()) {
            double fontSizeNumber = Double.valueOf(element.getAttribute("fontSize").replaceAll(",", "\\."));
            fontSize = FontSize.get(fontSizeNumber, averageFontSize);
        }

        return fontSize;
    }

    private boolean getBooleanValue(String valueString) {
        boolean value = false;
        if (valueString != null && !valueString.isEmpty()) {
            value = Boolean.valueOf(valueString);
        }
        return value;
    }

    private String getOriginalText(Element element) {
        return (element != null && element.getTextContent() != null && !element.getTextContent().isEmpty()) ?
                element.getTextContent().trim().replaceAll("\\s+", " ") : "";
    }

    /**
     * This augment the line element with the properties from the "run" child tags.
     *
     * @param lineElement     the line element without the desired properties
     * @param previousElement the previous element to be used in the last case
     */
    private void augmentLineElement(Element lineElement, Element previousElement) {

        List<Element> runsOfLine = getElementsByTagName("run", lineElement);

        Map<Double, Integer> fontSizeMapCount = new HashMap<>();

        if (runsOfLine != null && !runsOfLine.isEmpty()) {

            for (Element currentRun : runsOfLine) {
                String textContent = currentRun.getTextContent().replaceAll(" ", "").replaceAll("\n", "");
                if (textContent.length() > 3) {
                    checkMissingElements(lineElement, currentRun, fontSizeMapCount);
                }
            }

            String lineFontSize = lineElement.getAttribute("fontSize");

            if ((lineFontSize == null || lineFontSize.isEmpty()) && !fontSizeMapCount.isEmpty()) {
                lineElement.setAttribute("fontSize", String.format("%.2f", getFontSize(fontSizeMapCount)));
            }

        } else {
            checkMissingElements(lineElement, previousElement, fontSizeMapCount);
        }

    }

    private void checkMissingElements(Element lineElement, Element workingElement, Map<Double, Integer> fontSizeMapCount) {
        checkMissingAttribute(lineElement, workingElement.getAttribute("bold"), "bold");
        checkMissingAttribute(lineElement, workingElement.getAttribute("italic"), "italic");
        checkMissingAttribute(lineElement, workingElement.getAttribute("fontFace"), "fontFace");
        checkMissingAttribute(lineElement, workingElement.getAttribute("fontSize"), "fontSize");
        checkMissingAttribute(lineElement, workingElement.getAttribute("fontFamily"), "fontFamily");

        String lineUnderlineString = lineElement.getAttribute("underlined");
        String workingUnderlineString = workingElement.getAttribute("underlined");
        if ((lineUnderlineString == null || lineUnderlineString.isEmpty()
                || lineUnderlineString.equals("none")) &&
                workingUnderlineString != null && !workingUnderlineString.isEmpty()
                && !workingUnderlineString.equals("none")) {
            lineElement.setAttribute("underlined", workingUnderlineString);
        }

        String workingFontSize = workingElement.getAttribute("fontSize");
        if (workingFontSize != null && !workingFontSize.isEmpty()) {
            Double currentFontSize = Double.valueOf(workingFontSize);
            Integer currentFontCount = fontSizeMapCount.containsKey(currentFontSize) ? fontSizeMapCount.get(currentFontSize) + 1 : 0;
            fontSizeMapCount.put(currentFontSize, currentFontCount);
        }
    }

    private Double getFontSize(Map<Double, Integer> fontSizeMapCount) {

        Set<Map.Entry<Double, Integer>> fontSizeEntries = fontSizeMapCount.entrySet();
        Map.Entry<Double, Integer> bestEntry = fontSizeEntries.iterator().next();

        for (Map.Entry<Double, Integer> currentFontSizeEntry : fontSizeEntries) {

            if (currentFontSizeEntry.getValue() > bestEntry.getValue() ||
                    currentFontSizeEntry.getKey() < bestEntry.getKey()) {
                bestEntry = currentFontSizeEntry;
            }
        }

        return bestEntry.getKey();
    }

    private void checkMissingAttribute(Element lineElement, String currentAttribute, String type) {
        String lineTypeString = lineElement.getAttribute(type);
        if ((lineTypeString == null || lineTypeString.isEmpty())
                && currentAttribute != null && !currentAttribute.isEmpty()) {
            lineElement.setAttribute(type, currentAttribute);
        }
    }
}
