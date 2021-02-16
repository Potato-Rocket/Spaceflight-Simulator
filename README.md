# Spaceflight-Simulator
A Java application to simulate 3D gravitational physics. Currently, this project is in early 
stages of development.

Javadoc tool should function to generate explanation of the code.

### Setup
To get it working, your .properties files should be set up, and their directory configured. To 
do this, first copy example_setup.properties and example_system.properties to a folder in your .
config directory (or wherever you want to keep them). Then, set the final field "CONFIG_DIR" in 
"space.sim.config.Setup" to the directory of that folder.

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
