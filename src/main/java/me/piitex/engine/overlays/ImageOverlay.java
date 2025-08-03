package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import me.piitex.engine.loaders.ImageLoader;

public class ImageOverlay extends Overlay {
    private final ImageView imageView;
    private Image image;
    private double fitWidth, fitHeight;
    private boolean preserveRatio = true;
    private final String fileName;
    private String path = "Unknown";

    public ImageOverlay(Image image) {
        this.image = image;
        this.fitWidth = image.getWidth();
        this.fitHeight = image.getHeight();
        this.fileName = "Unknown";
        this.imageView = new ImageView();
    }

    public ImageOverlay(WritableImage image) {
        this.image = image;
        this.fitWidth = image.getWidth();
        this.fitHeight = image.getHeight();
        this.fileName = "Unknown";
        this.imageView = new ImageView();
    }

    public ImageOverlay(ImageLoader imageLoader) {
        this.image = imageLoader.build();
        this.fitWidth = image.getWidth();
        this.fitHeight = image.getHeight();
        this.fileName = imageLoader.getFile().getName();
        this.imageView = new ImageView();
    }

    public ImageOverlay(String imagePath) {
        ImageLoader loader = new ImageLoader(imagePath);
        this.image = loader.build();
        this.fileName = loader.getFile().getName();
        this.path = imagePath;
        this.fitWidth = image.getWidth();
        this.fitHeight = image.getHeight();
        this.imageView = new ImageView();
    }

    public ImageOverlay(String directory, String imagePath) {
        ImageLoader loader = new ImageLoader(directory, imagePath);
        this.image = loader.build();
        this.fileName = loader.getFile().getName();
        this.fitWidth = image.getWidth();
        this.fitHeight = image.getHeight();
        this.imageView = new ImageView();
    }

    public ImageOverlay(Image image, double x, double y) {
        this.image = image;
        this.fitWidth = image.getWidth();
        this.fitHeight = image.getHeight();
        this.fileName = "Unknown";
        this.imageView = new ImageView();
        setX(x);
        setY(y);
    }

    public ImageOverlay(ImageLoader imageLoader, double x, double y) {
        this.image = imageLoader.build();
        this.fitWidth = image.getWidth();
        this.fitHeight = image.getHeight();
        this.fileName = imageLoader.getFile().getName();
        this.imageView = new ImageView();
        setX(x);
        setY(y);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(ImageLoader loader) {
        this.image = loader.build();
        imageView.setImage(image);
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isPreserveRatio() {
        return preserveRatio;
    }

    public void setPreserveRatio(boolean preserveRatio) {
        this.preserveRatio = preserveRatio;
        imageView.setPreserveRatio(preserveRatio);
    }

    public double getFitWidth() {
        return fitWidth;
    }

    public void setFitWidth(double fitWidth) {
        this.fitWidth = fitWidth;
        imageView.setFitWidth(fitWidth);
    }

    public double getFitHeight() {
        return fitHeight;
    }

    public void setFitHeight(double fitHeight) {
        this.fitHeight = fitHeight;
        imageView.setFitHeight(getFitHeight());
    }

    @Override
    public Node render() {
        Image image = getImage();
        imageView.setImage(image);
        imageView.setPreserveRatio(preserveRatio);
        if (fitWidth != 0) {
            imageView.setFitWidth(fitWidth);
        }
        if (fitHeight != 0) {
            imageView.setFitHeight(fitHeight);
        }
        imageView.setTranslateX(getX());
        imageView.setTranslateY(getY());
        setInputControls(imageView);
        return imageView;
    }
}
