package me.piitex.engine.overlays;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import me.piitex.engine.loaders.FontLoader;
import me.piitex.engine.overlays.events.IInputSetEvent;
import me.piitex.engine.overlays.events.IOverlaySubmit;
import me.piitex.engine.overlays.events.OverlaySubmitEvent;
import org.fxmisc.richtext.StyledTextArea;
import org.fxmisc.richtext.TextExt;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.languagetool.JLanguageTool;

import org.languagetool.Languages;
import org.languagetool.rules.Category;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class RichTextAreaOverlay extends Overlay implements Region {
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;
    private final String defaultInput;
    private FontLoader fontLoader;
    private String hintText = "";
    private String currentText;
    private Color textFill = Color.BLACK;
    private boolean renderBorder = true;
    private boolean enabled = true;
    private IInputSetEvent iInputSetEvent;
    private IOverlaySubmit iOverlaySubmit;
    private ContextMenu contextMenu;

    public RichTextAreaOverlay(String defaultInput, double width, double height) {
        this.defaultInput = defaultInput;
        this.width = width;
        this.height = height;
    }

    public RichTextAreaOverlay(String defaultInput, String hintText, double width, double height) {
        this.defaultInput = defaultInput;
        this.hintText = hintText;
        this.width = width;
        this.height = height;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setWidth(double w) {
        this.width = w;
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
    }

    @Override
    public double getPrefWidth() {
        return prefWidth;
    }

    @Override
    public double getPrefHeight() {
        return prefHeight;
    }

    @Override
    public void setPrefWidth(double w) {
        this.prefWidth = w;
    }

    @Override
    public void setPrefHeight(double h) {
        this.prefHeight = h;
    }

    @Override
    public double getMaxWidth() {
        return maxWidth;
    }

    @Override
    public double getMaxHeight() {
        return maxHeight;
    }

    @Override
    public void setMaxWidth(double w) {
        this.maxWidth = w;
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
    }

    @Override
    public double getScaleWidth() {
        return scaleWidth;
    }

    @Override
    public void setScaleWidth(double w) {
        this.scaleWidth = w;
    }

    @Override
    public double getScaleHeight() {
        return scaleHeight;
    }

    @Override
    public void setScaleHeight(double h) {
        this.scaleHeight = h;
    }


    public IInputSetEvent getiInputSetEvent() {
        return iInputSetEvent;
    }

    public void onInputSetEvent(IInputSetEvent iInputSetEvent) {
        this.iInputSetEvent = iInputSetEvent;
    }

    public IOverlaySubmit getiOverlaySubmit() {
        return iOverlaySubmit;
    }

    public void onSubmit(IOverlaySubmit iOverlaySubmit) {
        this.iOverlaySubmit = iOverlaySubmit;
    }

    @Override
    public Node render() {
        BiConsumer<TextExt, String> applyTextStyle = (textExt, styleType) -> {
            if (styleType != null && styleType.equals("misspelled")) {
                textExt.setUnderlineColor(Color.RED);
                textExt.setUnderlineDashArray(new Number[]{2.0, 2.0});
                textExt.setUnderlineWidth(1.0);
                textExt.setUnderlineCap(StrokeLineCap.BUTT);
            } else if (styleType != null && styleType.equals("grammarError")) {
                textExt.setUnderlineColor(Color.BLUE);
                textExt.setUnderlineDashArray(new Number[]{1.0, 1.0});
                textExt.setUnderlineWidth(1.0);
                textExt.setUnderlineCap(StrokeLineCap.BUTT);
            } else {
                textExt.setUnderlineColor(null);
                textExt.setUnderlineDashArray(null);
                textExt.setUnderlineWidth(0.0);
                textExt.setUnderlineCap(null);
            }
            textExt.getStyleClass().addAll(getStyles());
        };

        StyledTextArea<Object, String> textArea = new StyledTextArea<>(
                null,
                (paragraphTextFlow, paragraphStyle) -> {},
                "default",
                applyTextStyle
        );
        textArea.setWrapText(true);

        if (getWidth() > 0) {
            textArea.setMinWidth(width);
        }

        if (getHeight() > 0) {
            textArea.setMinHeight(height);
        }

        if (getPrefWidth() > 0) {
            textArea.setPrefWidth(prefWidth);
        }

        if (getPrefHeight() > 0) {
            textArea.setPrefHeight(prefHeight);
        }

        if (getMaxWidth() > 0) {
            textArea.setMaxWidth(maxWidth);
        }

        if (getMaxHeight() > 0) {
            textArea.setMaxHeight(maxHeight);
        }


        JLanguageTool langTool = new JLanguageTool(Languages.getLanguageForShortCode("en-US"));
        for (Category category : langTool.getCategories().values()) {
            langTool.enableRuleCategory(category.getId());
        }

        // Running this every time the user types can cause lag when typing.
        // This will run every 500ms to check for spell checking
        // If the user types during the scheduler it will shut down and re-run in 500ms
        final ScheduledExecutorService[] spellCheckScheduler = {Executors.newSingleThreadScheduledExecutor()};
        textArea.textProperty().addListener((obs, oldText, newText) -> {
            spellCheckScheduler[0].shutdownNow(); // Cancel previous task
            spellCheckScheduler[0] = Executors.newSingleThreadScheduledExecutor(); // New scheduler

            spellCheckScheduler[0].schedule(() -> {
                try {
                    final List<RuleMatch> matches = langTool.check(newText);
                    Platform.runLater(() -> applyHighlighting(matches, newText.length(), textArea));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, 500, TimeUnit.MILLISECONDS);
        });

        // Set up context menu for suggestions on right-click
        textArea.setOnMouseClicked(event -> {
            if (contextMenu != null) {
                contextMenu.hide();
            }
            if (event.getButton() == MouseButton.SECONDARY) {
                int charPos = textArea.hit(event.getX(), event.getY()).getInsertionIndex();
                String fullText = textArea.getText();
                try {
                    List<RuleMatch> matches = langTool.check(fullText);
                    RuleMatch relevantMatch = null;
                    for (RuleMatch match : matches) {
                        if (charPos >= match.getFromPos() && charPos <= match.getToPos()) {
                            relevantMatch = match;
                            break;
                        }
                    }

                    if (relevantMatch != null && !relevantMatch.getSuggestedReplacements().isEmpty()) {
                        contextMenu = createSpellCheckContextMenu(
                                relevantMatch.getSuggestedReplacements(),
                                relevantMatch.getFromPos(),
                                relevantMatch.getToPos(),
                                textArea
                        );
                        contextMenu.show(textArea, event.getScreenX(), event.getScreenY());
                    } else {
                        // Fallback to existing word-based spell check context if no general match
                        int[] wordBounds = getWordBoundsAtPosition(fullText, charPos);
                        if (wordBounds != null) {
                            String wordUnderCursor = fullText.substring(wordBounds[0], wordBounds[1]);
                            List<RuleMatch> wordMatches = langTool.check(wordUnderCursor);
                            wordMatches.stream()
                                    .filter(match -> match.getRule().isDictionaryBasedSpellingRule() &&
                                            match.getFromPos() == 0 && match.getToPos() == wordUnderCursor.length())
                                    .findFirst()
                                    .ifPresent(misspelledMatch -> {
                                        if (!misspelledMatch.getSuggestedReplacements().isEmpty()) {
                                            contextMenu = createSpellCheckContextMenu(
                                                    misspelledMatch.getSuggestedReplacements(),
                                                    wordBounds[0],
                                                    wordBounds[1],
                                                    textArea
                                            );
                                            contextMenu.show(textArea, event.getScreenX(), event.getScreenY());
                                        }
                                    });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Atlanta FX does not work with RichText
        // Could create a custom theme instead of setting the background manually
        textArea.setStyle("-fx-background-color: #161b22;");

        // Initial content setting and highlighting
        if (defaultInput != null && !defaultInput.isEmpty()) {
            textArea.replaceText(defaultInput);
        }

        // Apply initial highlighting if there's text
        if (!textArea.getText().isEmpty()) {
            try {
                List<RuleMatch> initialMatches = langTool.check(textArea.getText());
                Platform.runLater(() -> applyHighlighting(initialMatches, textArea.getText().length(), textArea));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (getiOverlaySubmit() != null) {
            textArea.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    if (event.isShiftDown()) {
                        textArea.appendText("\n");
                    } else {
                        getiOverlaySubmit().onSubmit(new OverlaySubmitEvent(this, event));
                    }
                }
            });
        }


        return textArea;
    }

    private void applyHighlighting(List<RuleMatch> matches, int fullTextLength, StyledTextArea<Object, String> textArea) {
        StyleSpansBuilder<String> spansBuilder = new StyleSpansBuilder<>();
        int lastPos = 0;

        for (RuleMatch match : matches) {
            int from = match.getFromPos();
            int to = match.getToPos();

            from = Math.max(0, from);
            to = Math.min(fullTextLength, to);
            if (from >= to) continue;

            spansBuilder.add("default", from - lastPos);

            if (match.getRule().isDictionaryBasedSpellingRule()) {
                spansBuilder.add("misspelled", to - from);
            } else {
                spansBuilder.add("grammarError", to - from);
            }
            lastPos = to;
        }
        spansBuilder.add("default", fullTextLength - lastPos); // Remaining text
        textArea.setStyleSpans(0, spansBuilder.create());
    }

    private int[] getWordBoundsAtPosition(String text, int charPos) {
        if (text == null || text.isEmpty() || charPos < 0 || charPos >= text.length()) {
            return null;
        }
        BreakIterator wordIterator = BreakIterator.getWordInstance();
        wordIterator.setText(text);

        int end = wordIterator.following(charPos);
        if (end == BreakIterator.DONE) end = text.length();

        int start = wordIterator.previous();
        if (start == BreakIterator.DONE) start = 0;

        while (start > 0 && Character.isLetterOrDigit(text.charAt(start - 1))) {
            start--;
        }
        while (end < text.length() && Character.isLetterOrDigit(text.charAt(end))) {
            end++;
        }

        if (start < end && text.substring(start, end).trim().matches("[a-zA-Z]+")) {
            return new int[]{start, end};
        }
        return null;
    }

    private ContextMenu createSpellCheckContextMenu(List<String> suggestions, int wordStart, int wordEnd, StyledTextArea<Object, String> textArea) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getStyleClass().clear();
        contextMenu.getStyleClass().add(Styles.TEXT_NORMAL);
        if (suggestions.isEmpty()) {
            MenuItem noSuggestions = new MenuItem("No suggestions");
            noSuggestions.setDisable(true);
            contextMenu.getItems().add(noSuggestions);
        } else {
            suggestions.stream().limit(5).forEach(suggestion -> {
                MenuItem item = new MenuItem(suggestion);
                item.setOnAction(e -> textArea.replaceText(wordStart, wordEnd, suggestion));
                contextMenu.getItems().add(item);
            });
        }
        return contextMenu;
    }


}
