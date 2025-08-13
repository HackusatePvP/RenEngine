package me.piitex.engine.overlays;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import me.piitex.engine.hanlders.events.InputSetEvent;
import me.piitex.engine.hanlders.events.OverlaySubmitEvent;
import me.piitex.engine.loaders.FontLoader;
import me.piitex.engine.overlays.events.IInputSetEvent;
import me.piitex.engine.overlays.events.IOverlaySubmit;
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
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class RichTextAreaOverlay extends Overlay implements Region {
    private StyledTextArea<Object, String> textArea;
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;
    private final String defaultInput;
    private String currentText;
    private FontLoader fontLoader;
    private Color textFill = Color.BLACK;
    boolean enabled = true;
    private IInputSetEvent iInputSetEvent;
    private IOverlaySubmit iOverlaySubmit;
    private ContextMenu contextMenu;
    private String backgroundColor;
    private String borderColor;

    private final ScheduledExecutorService spellCheckExecutor;
    private ScheduledFuture<?> spellCheckFuture;

    private final JLanguageTool langTool = new JLanguageTool(Languages.getLanguageForShortCode("en-US"));

    public RichTextAreaOverlay(String defaultInput, double width, double height) {
        this.defaultInput = defaultInput;
        this.width = width;
        this.height = height;
        this.spellCheckExecutor = Executors.newScheduledThreadPool(2);

        textArea = new StyledTextArea<>(
                null,
                (paragraphTextFlow, paragraphStyle) -> {},
                "default",
                applyTextStyle()
        );
    }

    public RichTextAreaOverlay(String defaultInput, String hintText, double width, double height) {
        this.defaultInput = defaultInput;
        this.width = width;
        this.height = height;
        this.spellCheckExecutor = Executors.newScheduledThreadPool(2);

        textArea = new StyledTextArea<>(
                null,
                (paragraphTextFlow, paragraphStyle) -> {},
                "default",
                applyTextStyle()
        );
    }

    public BiConsumer<TextExt, String> applyTextStyle() {
        BiConsumer<TextExt, String> applyTextStyle = (textExt, styleType) -> {
            textExt.setFill(textFill);
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
        };

        return applyTextStyle;
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText;

        textArea.replaceText(currentText);

        List<RuleMatch> matches;
        try {
            matches = langTool.check(currentText);
            applyHighlighting(matches, currentText.length(), textArea);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Apply highlighting on the JavaFX Application Thread
    }

    public String getCurrentText() {
        return currentText;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        updateColoring();
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
        updateColoring();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        textArea.setDisable(!enabled);
    }

    public void setTextFill(Color textFill) {
        this.textFill = textFill;

        // Not sure if this will work like this.
        textArea = new StyledTextArea<>(
                null,
                (paragraphTextFlow, paragraphStyle) -> {},
                "default",
                applyTextStyle());
    }

    public boolean isEnabled() {
        return enabled;
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

        for (Category category : langTool.getCategories().values()) {
            langTool.enableRuleCategory(category.getId());
        }
        // Running this every time the user types can cause lag when typing.
        // This will run every 500ms to check for spell checking
        // If the user types during the scheduler it will shut down and re-run in 500ms.
        // This is also a very intensive task.
        textArea.textProperty().addListener((obs, oldText, newText) -> {
            if (getiInputSetEvent() != null) {
                getiInputSetEvent().onInputSet(new InputSetEvent(this, newText));
            }

            if (spellCheckFuture != null && !spellCheckFuture.isDone()) {
                spellCheckFuture.cancel(true); // Interrupt if running, remove from queue
            }

            // Schedule the spell-check to run after a delay
            spellCheckFuture = spellCheckExecutor.schedule(() -> {
                try {
                    List<RuleMatch> matches = langTool.check(newText);
                    // Apply highlighting on the JavaFX Application Thread
                    Platform.runLater(() -> applyHighlighting(matches, newText.length(), textArea));
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle specific exception, e.g., logging
                }
            }, 200, TimeUnit.MILLISECONDS); // 500ms debounce

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

        updateColoring();

        textArea.setPadding(new Insets(5));

        // Initial content setting and highlighting
        if (defaultInput != null && !defaultInput.isEmpty()) {
            textArea.replaceText(defaultInput);
        }


        // Apply initial highlighting if there's text
        if (!textArea.getText().isEmpty()) {
            if (!spellCheckFuture.isDone()) {
                spellCheckFuture.cancel(false);
            }
            spellCheckFuture = spellCheckExecutor.schedule(() -> {
                try {
                    List<RuleMatch> initialMatches = langTool.check(textArea.getText());
                    Platform.runLater(() -> applyHighlighting(initialMatches, textArea.getText().length(), textArea));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, 200, TimeUnit.MILLISECONDS);
        }

        if (getiOverlaySubmit() != null) {
            textArea.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    if (event.isShiftDown()) {
                        textArea.insertText(textArea.getCaretPosition(), "\n");
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

    private String cssColor(Color color) {
        return String.format("rgba(%d, %d, %d, %f)",
                (int) (255 * color.getRed()),
                (int) (255 * color.getGreen()),
                (int) (255 * color.getBlue()),
                color.getOpacity());
    }

    private void updateColoring() {
        StringBuilder appender = new StringBuilder();

        if (backgroundColor != null && !backgroundColor.isEmpty()) {
            appender.append("-fx-background-color: ").append(backgroundColor).append(";");
        }
        if (borderColor != null && !borderColor.isEmpty()) {
            appender.append("-fx-border-color: ").append(borderColor).append(" ;");
            appender.append("-fx-border-width: 1px;");
            appender.append("-fx-border-radius: 4px;");
        }

        if (!appender.isEmpty()) {
            textArea.setStyle(appender.toString());
        }
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
        textArea.setMinWidth(w);
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
        textArea.setMinHeight(h);
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
        textArea.setPrefWidth(w);
    }

    @Override
    public void setPrefHeight(double h) {
        this.prefHeight = h;
        textArea.setPrefHeight(h);
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
        textArea.setMaxWidth(w);
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
        textArea.setMaxHeight(h);
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

}
