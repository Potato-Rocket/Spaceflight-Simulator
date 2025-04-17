# Spaceflight-Simulator
A Java application to simulate 3D gravitational physics. Currently, this project is in early 
stages of development.

Javadoc tool should function to generate explanation of the code.

### Setup
To get it working, your .properties files should be set up. Create the directory 
"spaceflight-simulator" in your .config, and copy the example .properties files to that 
directory. Then, rename "example_setup.properties" to "setup.properties" and modify settings 
however you want. The format is explained in the .properties files.

### Usage
When you start the program, the view will automatically encompass every one of your bodies. To 
rotate the view, click and drag on the screen. To zoom in or out, you can use the scroll wheel. 
Alternatively, these keybindings can be used:

W - Tilt view up.<br>
S - Tilt view down.<br>
A - Rotate view left.<br>
D - Rotate view right.<br>

J - Zoom in.<br>
K - Zoom out.<br>

To increase or decrease the virtual time-scale, use the following keybindings:

Q - Slow down time.<br>
E - Speed up time.

To toggle the center of the view between different bodies, use these keybindings:

H - Go to previous body.<br>
L - Go to next body.

Finally, these keybinding can be used for other functions:

F1 - Reset viewing angle and zoom.
F2 - Toggle whether to view planet sizes to scale with distance or on a log scale.
F3 - Toggle whether to show extra info on screen.
