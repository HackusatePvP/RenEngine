# RenEngine
Simplified JavaFX framework for creating basic applications. Now supports JDK 25.

## Warning
The framework is untested for production. Will not work with mobile devices.

## Style/Design
[AtlantaFX](https://github.com/mkpaz/atlantafx) has been integrated into the framework.

## API
TODO

### Update
You no longer need to explicitly call `window.render()`. Window will auto update when adding and removing elements to renderers.

### Simple Usage

Always create a Main class which EXCLUDES javafx components.
```java
public class Main {
    public static void main(String[] args) {
        JavaFXLoad.main(args); // Call to your JavaFX main class. I always call it JavaFXLoad.
    }
}
```

Create your JavaFX entry point.

```java
import javafx.application.Application;
import me.piitex.engine.overlays.ButtonBuilder;
import me.piitex.engine.overlays.ButtonOverlay;

public class JavaFXLoad extends Application {
    @Override
    public void start(Stage s) {
        // Loads Atlanta css.
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        Window window = new WindowBuilder("App").setIcon(new ImageLoader(new File(App.getAppDirectory(), "logo.png"))).setDimensions(1920, 1080).build();

        // Always store the window as a static singleton. This makes accessing and modifying the window easier.
        App.window = window;

        // Handle closing the window.
        Stage stage = window.getStage();
        stage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });

        // This will create a system tray icon (Optional)
        FXTrayIcon icon = new FXTrayIcon(window.getStage(), new File(App.getAppDirectory(), "logo.png"), 128, 128);

        // Handle exiting from the icon (Optional)
        icon.addExitItem("Exit", e -> {
            Platform.exit();
            System.exit(0);
        });
        // Display the icon in the system tray.
        icon.show();

        // Start building the gui

        // Render your first view/container for the application.
        Container container = new HomeView();

        // Add the container to the window. Calling this function later in the process will allow the container to fully build before displaying.
        window.addContainer(container);
    }

    public static void main(String[] args) {
        // Always load your backend before calling launch();
        new App();

        // This function will call everything above. This is JavaFX API.
        // Renders GUI components
        launch();
    }
}
```
Create your first view.

```java
import me.piitex.engine.containers.EmptyContainer;
import me.piitex.engine.overlays.TextOverlay;

public class HomeView extends EmptyContainer {

    public HomeView() {
        // Build your first view
        TextOverlay text = new TextOverlay("Example text");
        text.setX(20);
        text.setY(20);
        addElement(text);
    }
}
```

Configure Maven to properly build an executable jar file.
```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.10.1</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>0.0.8</version>
                <executions>
                    <execution>
                        <!-- Default configuration for running with: mvn clean javafx:run -->
                        <id>default-cli</id>
                        <configuration>
                            You will need to change this!!!
                            <mainClass>me.piitex.app.Main</mainClass>
                            <launcher>app</launcher>
                            <jlinkZipName>app</jlinkZipName>
                            <jlinkImageName>app</jlinkImageName>
                            <noManPages>true</noManPages>
                            <stripDebug>true</stripDebug>
                            <noHeaderFiles>true</noHeaderFiles>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.2.4</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <finalName>character-chat-app</finalName>
                            <filters>
                                <filter>
                                    <artifact>*:*</artifact>
                                    <excludes>
                                        <exclude>META-INF/*.SF</exclude>
                                        <exclude>META-INF/*.DSA</exclude>
                                        <exclude>META-INF/*.RSA</exclude>
                                    </excludes>
                                </filter>
                            </filters>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    ALSO CHANGE THIS
                                    <mainClass>me.piitex.app.Main</mainClass>
                                </transformer>
                                <!-- Need for Ikonli fonts to work -->
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>false</filtering>
                <includes>
                    <include>**/jasperreports_extension.properties</include>
                    <include>**/tecsofti-fonts.xml</include>
                    <include>**/*.xml</include>
                </includes>
            </resource>
        </resources>
    </build>
```


## Examples
* [RenJava](https://github.com/HackusatePvP/RenJava) Visual Novel framework.
* [Chat-App](https://github.com/HackusatePvP/character-chat-app) AI chat interface designed for characters and roleplay.

## LICENSE
This is licensed under MIT.

Copyright 2025, RenEngine

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.