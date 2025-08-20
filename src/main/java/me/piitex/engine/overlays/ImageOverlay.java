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
        this.fileName = imageLoader.getFile().getName();
        if (fileName.endsWith(".gif")) {
            this.image = imageLoader.buildRaw();
        } else {
            this.image = imageLoader.build();
        }
        this.path = imageLoader.getFile().getAbsolutePath();
        this.fitWidth = image.getWidth();
        this.fitHeight = image.getHeight();
        this.imageView = new ImageView();
    }

    public ImageOverlay(String imagePath) {
        ImageLoader loader = new ImageLoader(imagePath);
        if (imagePath.endsWith(".gif")) {
            this.image = loader.buildRaw();
        } else {
            this.image = loader.build();
        }
        this.fileName = loader.getFile().getName();
        this.path = loader.getFile().getAbsolutePath();
        this.fitWidth = image.getWidth();
        this.fitHeight = image.getHeight();
        this.imageView = new ImageView();
    }

    public ImageOverlay(String directory, String imagePath) {
        ImageLoader loader = new ImageLoader(directory, imagePath);
        if (imagePath.endsWith(".gif")) {
            this.image = loader.buildRaw();
        } else {
            this.image = loader.build();
        }
        this.path = loader.getFile().getAbsolutePath();
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
        this.path = imageLoader.getFile().getAbsolutePath();
        this.imageView = new ImageView();
        setX(x);
        setY(y);
    }


    public Image getImage() {
        return image;
    }

    public void setImage(ImageLoader loader) {
        if (loader != null) {
            this.image = loader.build();
            imageView.setImage(image);
        } else {
            this.image = null;
            imageView.setImage(null);
            if (path != null) {
                ImageLoader.imageCache.remove(path);
            }

            // Sometimes the file will still be loaded in the JVM preventing io operations.
            // GC call is a workaround that issue but not guaranteed.
            System.gc();
        }
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
        imageView.setImage(image);
        imageView.setPreserveRatio(preserveRatio);
        if (fitWidth > 0) {
            imageView.setFitWidth(fitWidth);
        }
        if (fitHeight > 0) {
            imageView.setFitHeight(fitHeight);
        }
        imageView.setTranslateX(getX());
        imageView.setTranslateY(getY());
        setInputControls(imageView);
        return imageView;
    }
}
